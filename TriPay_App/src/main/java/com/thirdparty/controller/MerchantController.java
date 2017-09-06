package com.thirdparty.controller;


import com.instantpay.model.response.TransactionResponse;
import com.tripayapp.api.ITransactionApi;
import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.PQService;
import com.tripayapp.entity.TPTransaction;
import com.tripayapp.entity.User;
import com.tripayapp.entity.UserSession;
import com.tripayapp.model.UserDTO;
import com.tripayapp.model.error.TransactionError;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.PQServiceRepository;
import com.tripayapp.repositories.UserSessionRepository;
import com.tripayapp.session.PersistingSessionRegistry;
import com.tripayapp.util.Authorities;
import com.tripayapp.util.ConvertUtil;
import com.tripayapp.util.SecurityUtil;
import com.tripayapp.validation.TransactionValidation;
import com.thirdparty.api.IMerchantApi;
import com.tripayapp.model.mobile.ResponseDTO;
import com.thirdparty.model.request.AuthenticationDTO;
import com.thirdparty.model.request.PaymentDTO;
import com.thirdparty.model.request.StatusCheckDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/Authenticate")
public class MerchantController {

    private final IMerchantApi merchantApi;
    private final IUserApi userApi;
    private final TransactionValidation transactionValidation;
    private final UserSessionRepository userSessionRepository;
    private final PersistingSessionRegistry persistingSessionRegistry;
    private final PQServiceRepository pqServiceRepository;

    public MerchantController(IMerchantApi merchantApi, IUserApi userApi, TransactionValidation transactionValidation, UserSessionRepository userSessionRepository, PersistingSessionRegistry persistingSessionRegistry, PQServiceRepository pqServiceRepository) {
        this.merchantApi = merchantApi;
        this.userApi = userApi;
        this.transactionValidation = transactionValidation;
        this.userSessionRepository = userSessionRepository;
        this.persistingSessionRegistry = persistingSessionRegistry;
        this.pqServiceRepository = pqServiceRepository;
    }

    @RequestMapping(value="/Merchant",method= RequestMethod.POST,produces= {MediaType.APPLICATION_JSON_VALUE},consumes={MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<ResponseDTO> authenticateMerchant(@RequestBody AuthenticationDTO dto, HttpServletRequest request, HttpServletResponse response){
        System.err.println("before request");
        ResponseDTO result = new ResponseDTO();
        User user = userApi.findById(dto.getId());
        if(user != null) {
            String authority = user.getAuthority();
            if(authority.contains(Authorities.MERCHANT) && authority.contains(Authorities.AUTHENTICATED)) {
                String token = merchantApi.getTokenByMerchant(user);
                dto.setToken(token);
                String receivedHash = dto.getHash();
                try {
                    String calculatedHash = SecurityUtil.md5(ConvertUtil.convertAuthenticationDTO(dto));
                    System.err.print(calculatedHash);
                    if(receivedHash.equalsIgnoreCase(calculatedHash)){
                        result = merchantApi.authenticateMerchant(dto,user);
                    }else {
                        result.setStatus(ResponseStatus.INVALID_HASH);
                        result.setMessage("Invalid Hash");
                        result.setDetails("Invalid Hash");
                    }

                    System.err.println("afer request");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
                result.setMessage("Unauthorized User");
                result.setDetails("Permission not granted");
            }
        }else {
            result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
            result.setMessage("Unauthorized User");
            result.setDetails("User Not Avaiable");
        }
        return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
    }

    @RequestMapping(value="/Payment",method=RequestMethod.POST,produces={MediaType.APPLICATION_JSON_VALUE},consumes={MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<ResponseDTO> authenticatePayment(@RequestBody PaymentDTO dto,HttpServletRequest request,HttpServletResponse response){
        ResponseDTO result = new ResponseDTO();
        String sessionId = dto.getSessionId();
        UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
        if (userSession != null) {
            UserDTO user = userApi.getUserById(userSession.getUser().getId());
            if (user.getAuthority().contains(Authorities.USER)
                    && user.getAuthority().contains(Authorities.AUTHENTICATED)) {
                persistingSessionRegistry.refreshLastRequest(sessionId);
                User merchant = userApi.findById(dto.getId());
                if(merchant != null) {
                    String authority  =  merchant.getAuthority();
                    if(authority.contains(Authorities.MERCHANT) && authority.contains(Authorities.AUTHENTICATED)) {
                        PQService service = merchantApi.getServiceByMerchant(merchant);
                        TransactionError transactionError = transactionValidation
                                .validateMerchantTransaction(String.valueOf(dto.getNetAmount()), user.getUsername(), service);
                        if (transactionError.isValid()) {
                            User u = userApi.findByUserName(user.getUsername());
                            result = merchantApi.authenticatePayment(dto, u , merchant);
                            result.setBalance(u.getAccountDetail().getBalance());
                            return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
                        } else {
                            result.setStatus(ResponseStatus.FAILURE);
                            result.setMessage(transactionError.getCode());
                            result.setDetails(transactionError.getMessage());
                        }
                    }else{
                        result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
                        result.setMessage("Unauthorized User");
                        result.setDetails("Unauthorized User");
                    }
                }
            } else {
                result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
                result.setMessage("Failed, Unauthorized user.");
                result.setDetails("Failed, Unauthorized user.");
            }
        } else {
            result.setStatus(ResponseStatus.INVALID_SESSION);
            result.setMessage("Please, login and try again.");
            result.setDetails("Please, login and try again.");

        }
        return new ResponseEntity<ResponseDTO>(result,HttpStatus.OK);
    }


    @RequestMapping(value="/StatusCheck",method=RequestMethod.POST,produces={MediaType.APPLICATION_JSON_VALUE},consumes={MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<ResponseDTO> authenticatePayment(@RequestBody StatusCheckDTO dto, HttpServletRequest request, HttpServletResponse response){
        ResponseDTO result = new ResponseDTO();

                User merchant = userApi.findById(dto.getMerchantId());
                if (merchant != null) {
                    String authority = merchant.getAuthority();
                    if (authority.contains(Authorities.MERCHANT) && authority.contains(Authorities.AUTHENTICATED)) {
                            boolean isValidKey = merchantApi.containsValidSecretKey(merchant,dto.getSecretKey());
                            if(isValidKey){
                                TPTransaction merchantTransaction = merchantApi.getByTransactionRefNo(merchant,dto.getMerchantRefNo());
                                if(merchantTransaction != null){
                                    result.setStatus(ResponseStatus.SUCCESS);
                                    result.setMessage("Transaction Details");
                                    result.setDetails(com.thirdparty.util.ConvertUtil.convertTransaction(merchantTransaction));
                                }else {
                                    result.setStatus(ResponseStatus.BAD_REQUEST);
                                    result.setMessage("Transaction Not Available");
                                    result.setDetails("Transaction Not Available");
                                }
                            }else {
                                result.setStatus(ResponseStatus.BAD_REQUEST);
                                result.setMessage("Not a valid key/token");
                                result.setDetails("Not a valid key/token");
                            }
                    }else {
                        result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
                        result.setMessage("Not a valid merchant id");
                        result.setMessage("Not a valid merchant id");
                    }
                }else {
                    result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
                    result.setMessage("Not a valid user");
                    result.setDetails("Not a valid user");
                }
        return new ResponseEntity<ResponseDTO>(result,HttpStatus.OK);
    }
}
