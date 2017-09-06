package com.tripayapp.controller.api.v1;

import com.tripayapp.api.ISendMoneyApi;
import com.tripayapp.api.ITransactionApi;
import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.*;
import com.tripayapp.model.*;
import com.tripayapp.model.error.ChangePasswordError;
import com.tripayapp.model.error.RegisterError;
import com.tripayapp.model.error.SendMoneyMobileError;
import com.tripayapp.model.error.TransactionError;
import com.tripayapp.model.mobile.*;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.PGDetailsRepository;
import com.tripayapp.repositories.TPTransactionRepository;
import com.tripayapp.repositories.UserRepository;
import com.tripayapp.repositories.UserSessionRepository;
import com.tripayapp.session.PersistingSessionRegistry;
import com.tripayapp.util.Authorities;
import com.tripayapp.util.ComparatorUtil;
import com.tripayapp.util.ConvertUtil;
import com.tripayapp.util.SecurityUtil;
import com.tripayapp.validation.RegisterValidation;
import com.tripayapp.validation.SendMoneyValidation;
import com.tripayapp.validation.TransactionValidation;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/Api/{version}/{role}/{device}/{language}/Merchant")
public class MerchantController {

    private final IUserApi userApi;
    private final TPTransactionRepository tpTransactionRepository;
    private final PGDetailsRepository pgDetailsRepository;
    private final UserSessionRepository userSessionRepository;
    private final PersistingSessionRegistry persistingSessionRegistry;
    private final ITransactionApi transactionApi;
    private final RegisterValidation registerValidation;
    private final TransactionValidation transactionValidation;
    private final ISendMoneyApi sendMoneyApi;


    public MerchantController(IUserApi userApi, TPTransactionRepository tpTransactionRepository, PGDetailsRepository pgDetailsRepository, UserSessionRepository userSessionRepository, PersistingSessionRegistry persistingSessionRegistry, ITransactionApi transactionApi,RegisterValidation registerValidation,TransactionValidation transactionValidation,ISendMoneyApi sendMoneyApi) {
        this.userApi = userApi;
        this.tpTransactionRepository = tpTransactionRepository;
        this.pgDetailsRepository = pgDetailsRepository;
        this.userSessionRepository = userSessionRepository;
        this.persistingSessionRegistry = persistingSessionRegistry;
        this.transactionApi = transactionApi;
        this.registerValidation = registerValidation;
        this.transactionValidation = transactionValidation;
        this.sendMoneyApi = sendMoneyApi;
    }


    @RequestMapping(value="/GetTransactions",method = RequestMethod.POST,consumes= MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ResponseDTO> getTotalTransactions(@PathVariable(value = "version") String verison, @PathVariable(value = "role") String role, @PathVariable(value="device") String device, @PathVariable(value="language") String language, @RequestBody SessionDTO dto, @RequestHeader(value="hash",required=false) String hash, HttpServletRequest request, HttpServletResponse response){
        ResponseDTO result = new ResponseDTO();
        boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);

        if (isValidHash) {
            if (role.equalsIgnoreCase("Merchant")) {
                String sessionId = dto.getSessionId();
                UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
                if (userSession != null) {
                    UserDTO user = userApi.getUserById(userSession.getUser().getId());
                    if (user.getAuthority().contains(Authorities.MERCHANT) && user.getAuthority().contains(Authorities.AUTHENTICATED)) {
                        persistingSessionRegistry.refreshLastRequest(sessionId);
                        List<TPTransaction> transactionsList = tpTransactionRepository.findByMerchant(userSession.getUser());
                        result.setStatus(ResponseStatus.SUCCESS);
                        result.setMessage("PG Transactions");
                        result.setDetails(transactionsList);
                        return new ResponseEntity<ResponseDTO> (result,HttpStatus.OK);
                    } else {
                        result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
                        result.setMessage("Failed, Unauthorized user.");
                        result.setDetails("Failed, Unauthorized user.");
                        return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
                    }
                } else {
                    result.setStatus(ResponseStatus.INVALID_SESSION);
                    result.setMessage("Please, login and try again.");
                    result.setDetails("Please, login and try again.");
                    return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
                }
            } else {
                result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
                result.setMessage("Failed, Unauthorized user.");
                result.setDetails("Failed, Unauthorized user.");
                return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);

            }
        } else {
            result.setStatus(ResponseStatus.INVALID_HASH);
            result.setMessage("Failed, Please try again later.");
            return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
        }

    }

    @RequestMapping(value="/GetVPayQwikTransactions",method = RequestMethod.POST,consumes= MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ResponseDTO> getVPayQwikTransactions(@PathVariable(value = "version") String verison, @PathVariable(value = "role") String role, @PathVariable(value="device") String device, @PathVariable(value="language") String language, @RequestHeader(value="hash",required=false) String hash, @RequestBody PagingDTO dto, HttpServletRequest request, HttpServletResponse response){
        ResponseDTO result = new ResponseDTO();
        boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);

        if (isValidHash) {
            if (role.equalsIgnoreCase("Merchant")) {
                String sessionId = dto.getSessionId();
                UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
                if (userSession != null) {
                    UserDTO user = userApi.getUserById(userSession.getUser().getId());
                    if (user.getAuthority().contains(Authorities.MERCHANT) && user.getAuthority().contains(Authorities.AUTHENTICATED)) {
                        persistingSessionRegistry.refreshLastRequest(sessionId);
                        User merchant = userApi.findByUserName(user.getUsername());
                        PQService service  = pgDetailsRepository.findServiceByUser(merchant);
                        Sort sort = new Sort(Sort.Direction.DESC,"id");
                        Pageable page = new PageRequest(dto.getPage(),dto.getSize(),sort);
                        List<PQTransaction> transactionList = transactionApi.transactionListByService(service);
                        List<User> userList = userApi.getAllUsers();
                        List<MTransactionResponseDTO> list = ConvertUtil.getMerchantTransactions(transactionList,userList);
                        Collections.sort(list,new ComparatorUtil());
                        int start = page.getOffset();
                        int end = (start + page.getPageSize()) > list.size() ? list.size() : (start + page.getPageSize());
                        List<MTransactionResponseDTO> subList = list.subList(start,end);
                        Page<MTransactionResponseDTO> resultSet  =  new PageImpl<MTransactionResponseDTO>(subList,page,list.size());
                        result.setStatus(ResponseStatus.SUCCESS);
                        result.setMessage("VPayQwik Merchant Transactions");
                        result.setDetails(resultSet);
                        return new ResponseEntity<ResponseDTO> (result,HttpStatus.OK);
                    } else {
                        result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
                        result.setMessage("Failed, Unauthorized user.");
                        result.setDetails("Failed, Unauthorized user.");
                        return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
                    }
                } else {
                    result.setStatus(ResponseStatus.INVALID_SESSION);
                    result.setMessage("Please, login and try again.");
                    result.setDetails("Please, login and try again.");
                    return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
                }
            } else {
                result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
                result.setMessage("Failed, Unauthorized user.");
                result.setDetails("Failed, Unauthorized user.");
                return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
            }
        } else {
            result.setStatus(ResponseStatus.INVALID_HASH);
            result.setMessage("Failed, Please try again later.");
            return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
        }
    }

    @RequestMapping(value="/ChangePassword",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ResponseDTO> changePassword(@PathVariable(value = "version") String verison, @PathVariable(value = "role") String role, @PathVariable(value="device") String device, @PathVariable(value="language") String language, @RequestHeader(value="hash",required=false) String hash, @RequestBody ChangePasswordDTO dto, HttpServletRequest request, HttpServletResponse response){
        ResponseDTO result = new ResponseDTO();
        boolean isValidHash = SecurityUtil.isHashMatches(dto,hash);
        if(isValidHash){
            if(role.equalsIgnoreCase("Merchant")) {
                String sessionId = dto.getSessionId();
                UserSession session = userSessionRepository.findByActiveSessionId(sessionId);
                if(session != null){
                    UserDTO user = userApi.getUserById(session.getUser().getId());
                    if(user != null){
                        String authority = user.getAuthority();
                        dto.setUsername(user.getUsername());
                        if(authority.contains(Authorities.MERCHANT) && authority.contains(Authorities.AUTHENTICATED)){
                            persistingSessionRegistry.refreshLastRequest(sessionId);
                            ChangePasswordError error = registerValidation.validateChangePasswordDTO(dto);
                            if(error.isValid()){
                                userApi.renewPasswordFromAccount(dto);
                                result.setStatus(ResponseStatus.SUCCESS);
                                result.setMessage("Password Updated Successfully.");
                                result.setDetails("Password Updated Successfully.");
                            }else {
                                result.setStatus(ResponseStatus.BAD_REQUEST);
                                result.setMessage(error.getPassword());
                            }
                        }else {
                            result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
                            result.setMessage("Failed,Please login and try again");
                        }
                    }
                }else {
                    result.setStatus(ResponseStatus.INVALID_SESSION);
                    result.setMessage("Please login and Try Again");
                }
            }else {
                result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
                result.setMessage("Failed,Unauthorized User");
            }
        }else {
            result.setStatus(ResponseStatus.INVALID_HASH);
            result.setMessage("Not a valid hash");
        }
        return new ResponseEntity<ResponseDTO>(result,HttpStatus.OK);
    }

    @RequestMapping(value = "/RequestOffline",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ResponseDTO> requestPaymentFromUser(@PathVariable(value = "version") String version,@PathVariable("role") String role,@PathVariable("device") String device,@PathVariable("language") String language,@RequestHeader(value = "hash") String hash,@RequestBody PaymentRequestDTO dto,HttpServletRequest request,HttpServletResponse response){
        ResponseDTO result = new ResponseDTO();
        boolean isValidHash = SecurityUtil.isHashMatches(dto,hash);
        if(isValidHash){
            if(role.equalsIgnoreCase("Merchant")) {
                String sessionId = dto.getSessionId();
                UserSession session = userSessionRepository.findByActiveSessionId(sessionId);
                if(session != null){
                    UserDTO merchant = userApi.getUserById(session.getUser().getId());
                    if(merchant != null){
                        String authority = merchant.getAuthority();
                        if(authority.contains(Authorities.MERCHANT) && authority.contains(Authorities.AUTHENTICATED)){
                            persistingSessionRegistry.refreshLastRequest(sessionId);
                            User user = userApi.findByUserName(dto.getMobileNumber());
                            User currentUser = userApi.findByUserName(merchant.getUsername());
                            //TODO checks if user is available
                            if(user != null){
                                //TODO checks whether service is available for merchant
                                PQService service = pgDetailsRepository.findServiceByUser(currentUser);
                                if(service != null) {
                                    //TODO checks transaction validation
                                    TransactionError error = transactionValidation.validateOfflinePayment(dto.getAmount(),currentUser,user,service);
                                    if(error.isValid()) {
                                        //TODO send OTP to user
                                            result = userApi.requestOfflinePayment(dto,currentUser,user);

                                    }else {
                                        result.setStatus(ResponseStatus.FAILURE);
                                        result.setMessage(error.getMessage());
                                    }

                                }else {
                                    result.setStatus(ResponseStatus.FAILURE);
                                    result.setMessage("Services not defined for your account");
                                }
                            }else {
                                result.setStatus(ResponseStatus.FAILURE);
                                result.setMessage("User Not Exists");
                            }
                        }else {
                            result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
                            result.setMessage("Failed,Please login and try again");
                        }
                    }
                }else {
                    result.setStatus(ResponseStatus.INVALID_SESSION);
                    result.setMessage("Please login and Try Again");
                }
            }else {
                result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
                result.setMessage("Failed,Unauthorized User");
            }
        }else {
            result.setStatus(ResponseStatus.INVALID_HASH);
            result.setMessage("Not a valid hash");
        }
        return new ResponseEntity<ResponseDTO>(result,HttpStatus.OK);
    }



    @RequestMapping(value = "/ProcessOffline",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ResponseDTO> processPaymentFromUser(@PathVariable(value = "version") String version,@PathVariable("role") String role,@PathVariable("device") String device,@PathVariable("language") String language,@RequestHeader(value = "hash") String hash,@RequestBody PaymentRequestDTO dto,HttpServletRequest request,HttpServletResponse response){
        ResponseDTO result = new ResponseDTO();
        boolean isValidHash = SecurityUtil.isHashMatches(dto,hash);
        if(isValidHash){
            if(role.equalsIgnoreCase("Merchant")) {
                String sessionId = dto.getSessionId();
                UserSession session = userSessionRepository.findByActiveSessionId(sessionId);
                if(session != null){
                    UserDTO merchant = userApi.getUserById(session.getUser().getId());
                    if(merchant != null){
                        String authority = merchant.getAuthority();
                        if(authority.contains(Authorities.MERCHANT) && authority.contains(Authorities.AUTHENTICATED)){
                            persistingSessionRegistry.refreshLastRequest(sessionId);
                            User user = userApi.findByUserName(dto.getMobileNumber());
                            User currentUser = userApi.findByUserName(merchant.getUsername());
                            //TODO checks if user is available
                            if(user != null){
                                //TODO checks whether service is available for merchant
                                PQService service = pgDetailsRepository.findServiceByUser(currentUser);
                                if(service != null) {
                                    //TODO checks transaction validation
                                    TransactionError error = transactionValidation.validateOfflinePayment(dto.getAmount(),currentUser,user,service);
                                    if(error.isValid()) {
                                        //TODO checks if OTP is valid
                                        String otp = user.getMobileToken();
                                        String receivedToken = dto.getOtp();
                                        if(otp.equals(receivedToken)) {
                                            //TODO perform pay at store
                                            user.setMobileToken(null);
                                            userApi.saveOrUpdateUser(user);
                                            PayStoreDTO pay = new PayStoreDTO();
                                            pay.setId(currentUser.getId());
                                            pay.setNetAmount(dto.getAmount());
                                            result = sendMoneyApi.preparePayStore(pay,user);
                                        }else  {
                                            result.setStatus(ResponseStatus.FAILURE);
                                            result.setMessage("OTP doesn't match");
                                        }

                                    }else {
                                        result.setStatus(ResponseStatus.FAILURE);
                                        result.setMessage(error.getMessage());
                                    }

                                }else {
                                    result.setStatus(ResponseStatus.FAILURE);
                                    result.setMessage("Services not defined for your account");
                                }
                            }else {
                                result.setStatus(ResponseStatus.FAILURE);
                                result.setMessage("User Not Exists");
                            }
                        }else {
                            result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
                            result.setMessage("Failed,Please login and try again");
                        }
                    }
                }else {
                    result.setStatus(ResponseStatus.INVALID_SESSION);
                    result.setMessage("Please login and Try Again");
                }
            }else {
                result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
                result.setMessage("Failed,Unauthorized User");
            }
        }else {
            result.setStatus(ResponseStatus.INVALID_HASH);
            result.setMessage("Not a valid hash");
        }
        return new ResponseEntity<ResponseDTO>(result,HttpStatus.OK);
    }
}
