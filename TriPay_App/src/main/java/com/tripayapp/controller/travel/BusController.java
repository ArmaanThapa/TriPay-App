package com.tripayapp.controller.travel;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.PQService;
import com.tripayapp.entity.User;
import com.tripayapp.entity.UserSession;
import com.tripayapp.model.UserDTO;
import com.tripayapp.model.UserType;
import com.tripayapp.model.error.BlockBusTicketError;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.PQServiceRepository;
import com.tripayapp.repositories.UserSessionRepository;
import com.tripayapp.session.PersistingSessionRegistry;
import com.tripayapp.util.Authorities;
import com.tripayapp.util.ClientException;
import com.tripayapp.util.LogCat;
import com.tripayapp.util.SecurityUtil;
import com.tripayapp.validation.BusValidation;
import com.thirdparty.api.IBookBusTicketApi;
import com.thirdparty.model.request.BookBusTicketRequest;

@Controller
@RequestMapping("/Api/v1/{role}/{device}/{language}")
public class BusController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final UserSessionRepository userSessionRepository;
	private final PersistingSessionRegistry persistingSessionRegistry;
	private final IBookBusTicketApi bookBusTicketApi;
	private final BusValidation busValidation;
	private final IUserApi userApi;
	private final PQServiceRepository pQServiceRepository;
	
	public BusController(UserSessionRepository userSessionRepository, PersistingSessionRegistry persistingSessionRegistry,
							IBookBusTicketApi bookBusTicketApi, BusValidation busValidation,IUserApi userApi,
							PQServiceRepository pQServiceRepository) {
		this.userSessionRepository = userSessionRepository;
		this.persistingSessionRegistry = persistingSessionRegistry;
		this.bookBusTicketApi = bookBusTicketApi;
		this.busValidation = busValidation;
		this.userApi = userApi;
		this.pQServiceRepository = pQServiceRepository;
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/SaveBookingDetails", produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> saveBusDetails(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody BookBusTicketRequest dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response)
					throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		ResponseDTO resp = new ResponseDTO();
		System.err.println("-----------------Inside Bus Controller--------------");
		logger.info("Inside | TravelBusController | SaveBusDetail");
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				try {
					String sessionId = dto.getSessionId();
					UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
					if (userSession != null) {
						UserDTO userDto = userApi.getUserById(userSession.getUser().getId());
						if (userDto.getAuthority().contains(Authorities.USER)
								&& userDto.getAuthority().contains(Authorities.AUTHENTICATED)) {
							persistingSessionRegistry.refreshLastRequest(sessionId);
							User user = userApi.findByUserName(userDto.getUsername());
							if (bookBusTicketApi.checkBalance(user, dto)) {

								PQService service = pQServiceRepository.findServiceByCode("VBUS");
								bookBusTicketApi.saveBusTicket(dto, user, service);
								resp.setStatus(ResponseStatus.SUCCESS);
								resp.setMessage("Bus Detail Saved");
								resp.setDetails("Bus Detail Saved");
								System.err.println("Bus Detail Saved");
//								resp.setBalance(userApi.getBalanceByUserAccount(user.getAccountDetail()));
								return new ResponseEntity<ResponseDTO>(resp, HttpStatus.OK);
							}
							resp.setStatus(ResponseStatus.FAILURE);
							resp.setMessage("Insufficient Funds");
							resp.setDetails("Insufficient Funds");
							System.err.println("Insufficient Funds");
							return new ResponseEntity<ResponseDTO>(resp, HttpStatus.OK);
						}
						resp.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
						resp.setMessage("Un-Authorised Role");
						resp.setDetails("Un-Authorised Role");
						return new ResponseEntity<ResponseDTO>(resp, HttpStatus.OK);
					} else {
						resp.setStatus(ResponseStatus.INVALID_SESSION);
						resp.setMessage("Invalid Session");
						resp.setDetails("Invalid Session");
						return new ResponseEntity<ResponseDTO>(resp, HttpStatus.OK);
					}
				} catch (Exception e) {
//					LogCat.print("Exception Caught");
					e.printStackTrace();
					resp.setStatus(ResponseStatus.INTERNAL_SERVER_ERROR);
					resp.setMessage("Internal server error");
					resp.setDetails(e.getMessage());
					return new ResponseEntity<ResponseDTO>(resp, HttpStatus.OK);
				}
			} else {
				resp.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				resp.setMessage("Not a valid user");
				return new ResponseEntity<ResponseDTO>(resp, HttpStatus.OK);
			}
		} else {
			resp.setStatus(ResponseStatus.BAD_REQUEST);
			resp.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<ResponseDTO>(resp,HttpStatus.OK);
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/BookBusResponse", produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> BookBusResponse(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody BookBusTicketRequest dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		logger.info("Inside | TravelBusController | SaveBusDetail");
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				try {
					String sessionId = dto.getSessionId();
					UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
					if (userSession != null) {
						UserDTO userDto = userApi.getUserById(userSession.getUser().getId());
						if (userDto.getAuthority().contains(Authorities.USER)
								&& userDto.getAuthority().contains(Authorities.AUTHENTICATED)) {
							persistingSessionRegistry.refreshLastRequest(sessionId);
							User user = userApi.findByUserName(userDto.getUsername());

							// Call Bus Ticket Booked API
							bookBusTicketApi.bookBusTicket(dto, user);
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("Bus Ticket Booked");
							result.setDetails("Bus Ticket Booked");
							result.setBalance(userApi.getBalanceByUserAccount(user.getAccountDetail()));
							System.err.println("Bus Ticket Booked");
						}
					} else {
						result.setStatus(ResponseStatus.INVALID_SESSION);
						result.setMessage("Invalid Session");
						result.setDetails("Invalid Session");
					}
				} catch (Exception e) {
					e.printStackTrace();
					result.setStatus(ResponseStatus.INTERNAL_SERVER_ERROR);
					result.setMessage("Internal server error");
					result.setDetails(e.getMessage());
					return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("Not a valid user");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/failBookBusResponse", produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> failBookBusResponse(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody BookBusTicketRequest dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		logger.info("Inside | TravelBusController | SaveBusDetail");
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				try {
					String sessionId = dto.getSessionId();
					UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
					if (userSession != null) {
						UserDTO userDto = userApi.getUserById(userSession.getUser().getId());
						if (userDto.getAuthority().contains(Authorities.USER)
								&& userDto.getAuthority().contains(Authorities.AUTHENTICATED)) {
							persistingSessionRegistry.refreshLastRequest(sessionId);
							User user = userApi.findByUserName(userDto.getUsername());
							// call bus ticket Cancel API
							bookBusTicketApi.failBookBusTicket(dto);
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("Bus Ticket Cancelled");
							result.setDetails("Bus Ticket Cancelled");
							System.err.println("Bus Ticket Cancelled");
						}
					} else {
						result.setStatus(ResponseStatus.INVALID_SESSION);
						result.setMessage("Invalid Session");
						result.setDetails("Invalid Session");
					}
				} catch (Exception e) {
					e.printStackTrace();
					result.setStatus(ResponseStatus.INTERNAL_SERVER_ERROR);
					result.setMessage("Internal server error");
					result.setDetails(e.getMessage());
					return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("Not a valid user");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	
}
