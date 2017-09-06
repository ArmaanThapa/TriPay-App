package com.tripayapp.controller.api.v2;

import com.tripayapp.api.ITransactionApi;
import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.PQTransaction;
import com.tripayapp.entity.User;
import com.tripayapp.entity.UserSession;
import com.tripayapp.mail.util.MailTemplate;
import com.tripayapp.model.*;
import com.tripayapp.model.error.*;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.UserSessionRepository;
import com.tripayapp.session.PersistingSessionRegistry;
import com.tripayapp.util.Authorities;
import com.tripayapp.util.SecurityUtil;
import com.tripayapp.validation.CommonValidation;
import com.tripayapp.validation.RegisterValidation;
import com.tripayapp.validation.TransactionValidation;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/Api/v2/{role}/{device}/{language}")
public class UserController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final IUserApi userApi;
	private final ITransactionApi transactionApi;
	private final RegisterValidation registerValidation;
	private final UserSessionRepository userSessionRepository;
	private final PersistingSessionRegistry persistingSessionRegistry;
	private final TransactionValidation transactionValidation;
	public UserController(IUserApi userApi, ITransactionApi transactionApi, RegisterValidation registerValidation,
			UserSessionRepository userSessionRepository, PersistingSessionRegistry persistingSessionRegistry,TransactionValidation transactionValidation) {
		this.userApi = userApi;
		this.transactionApi = transactionApi;
		this.registerValidation = registerValidation;
		this.userSessionRepository = userSessionRepository;
		this.persistingSessionRegistry = persistingSessionRegistry;
		this.transactionValidation = transactionValidation;

	}

	@RequestMapping(value = "/Validate/Transaction", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ResponseDTO> validateTransaction(@PathVariable(value = "role") String role,
													@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
													@RequestBody TransactionDTO dto, @RequestHeader(value = "hash", required = true) String hash,
													HttpServletRequest request, HttpServletResponse response){

		ResponseDTO result = new ResponseDTO();
		if (role.equalsIgnoreCase("User")) {
			String sessionId = dto.getSessionId();
			UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
			if (userSession != null) {
				UserDTO user = userApi.getUserById(userSession.getUser().getId());
				if (user.getAuthority().contains(Authorities.USER)
						&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
					persistingSessionRegistry.refreshLastRequest(sessionId);
                    dto.setSenderUsername(user.getUsername());
                    TransactionError error = transactionValidation.validateGenericTransaction(dto);
                    result.setStatus(ResponseStatus.SUCCESS);
                    result.setMessage(error.getMessage());
                    result.setDetails(error);
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
		} else {
			result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
			result.setMessage("Failed, Unauthorized user.");
			result.setDetails("Failed, Unauthorized user.");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);

	}
	@RequestMapping(value = "/EditProfile/Process", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> processEditedDetails(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody RegisterDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		if (role.equalsIgnoreCase("User")) {
			String sessionId = dto.getSessionId();
			UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
			if (userSession != null) {
				UserDTO user = userApi.getUserById(userSession.getUser().getId());
				if (user.getAuthority().contains(Authorities.USER)
						&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
					persistingSessionRegistry.refreshLastRequest(sessionId);
					RegisterError error = new RegisterError();
					error = registerValidation.validateEditUser(dto);
					if (error.isValid()) {
						dto.setUsername(user.getUsername());
						userApi.editUser(dto);
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("Profile has been edited successfully.");
						result.setDetails("Profile has been edited successfully.");
					} else {
						result.setStatus(ResponseStatus.BAD_REQUEST);
						result.setMessage("Failed, invalid request.");
						result.setDetails(error);
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
		} else {
			result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
			result.setMessage("Failed, Unauthorized user.");
			result.setDetails("Failed, Unauthorized user.");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/UpdatePassword/Process", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ResponseDTO> processNewPassword(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestHeader(value = "hash", required = true) String hash, HttpServletRequest request,
			HttpServletResponse response, @RequestBody ChangePasswordDTO change) {
		ResponseDTO result = new ResponseDTO();
		ChangePasswordError error = registerValidation.validateChangePasswordDTO(change);
		String sessionId = change.getSessionId();
		if (role.equalsIgnoreCase("User")) {
			if (error.isValid()) {
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.USER)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(sessionId);
						userApi.renewPasswordFromAccount(change);
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("Password Upadated Successfully.");
						result.setDetails("Password Upadated Successfully.");

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
			} else {
				result.setStatus(ResponseStatus.BAD_REQUEST);
				result.setMessage("Failed, invalid request.");
				result.setDetails(error);
			}
		} else {
			result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
			result.setMessage("Failed, Unauthorized user.");
			result.setDetails("Failed, Unauthorized user.");
		}

		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/UploadPicture/Process", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> processProfilePicture(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody UploadPictureDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, Exception {
		String stringFile = dto.getP();
		String fileFormat = dto.getF();
		String sessionId = dto.getS();
		System.err.println("Session ::" + sessionId);
		System.err.println("P ::" + stringFile);
		System.err.println("f " + fileFormat);

		ResponseDTO result = new ResponseDTO();
		if (role.equalsIgnoreCase("User")) {
			UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
			if (userSession != null) {
				UserDTO user = userApi.getUserById(userSession.getUser().getId());
				if (user.getAuthority().contains(Authorities.USER)
						&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
					persistingSessionRegistry.refreshLastRequest(sessionId);
					try {

						userApi.saveImage(userSession.getUser(), dto.getP());
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("Profile picture updated successfully.");
						result.setDetails(dto.getP());
					} catch (IllegalStateException e) {
						result.setStatus(ResponseStatus.INTERNAL_SERVER_ERROR);
						result.setMessage("Failed, Please try again later.");
						result.setDetails("Failed, Please try again later.");
					}

				} else {
					result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
					result.setMessage("Failed, Unauthorized user.");
					result.setDetails("Failed, Unauthorized user.");
				}
			} else {
				result.setStatus(ResponseStatus.INVALID_SESSION);
				result.setMessage("Failed, Please try again later.");
				result.setDetails("Failed, Please try again later.");
			}
		} else {
			result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
			result.setMessage("Failed, Unauthorized user.");
			result.setDetails("Failed, Unauthorized user.");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	// @RequestMapping(value = "/UploadPicture/Process", method =
	// RequestMethod.POST, produces = {
	// MediaType.APPLICATION_JSON_VALUE })
	// ResponseEntity<ResponseDTO> processProfilePicture(@PathVariable(value =
	// "role") String role,
	// @PathVariable(value = "device") String device, @PathVariable(value =
	// "language") String language,
	// @ModelAttribute UploadPictureDTO dto, @RequestHeader(value = "hash",
	// required = true) String hash,
	// HttpServletRequest request, HttpServletResponse response) {
	// MultipartFile file = dto.getProfilePic();
	// String sessionId = dto.getSessionId();
	// ResponseDTO result = new ResponseDTO();
	// if (role.equalsIgnoreCase("User")) {
	// UserSession userSession =
	// userSessionRepository.findByActiveSessionId(sessionId);
	// if (userSession != null) {
	// UserDTO user = userApi.getUserById(userSession.getUser().getId());
	// if (user.getAuthority().contains(Authorities.USER)
	// && user.getAuthority().contains(Authorities.AUTHENTICATED)) {
	// persistingSessionRegistry.refreshLastRequest(sessionId);
	// String rootDirectory =
	// request.getSession().getServletContext().getRealPath("/");
	// String[] format = file.getContentType().split("/");
	// if (file.getContentType().contains("image")) {
	// File dirs = new File(rootDirectory + "/resources/profileImages/" +
	// System.currentTimeMillis()
	// + "." + format[1]);
	// try {
	// dirs.mkdirs();
	// file.transferTo(dirs);
	// String url = "/resources/profileImages/" + dirs.getName();
	// userApi.saveImage(userSession.getUser(), url);
	// result.setStatus(ResponseStatus.SUCCESS);
	// result.setMessage("Profile picture updated successfully.");
	// result.setDetails(url);
	// } catch (IllegalStateException | IOException e) {
	// result.setStatus(ResponseStatus.INTERNAL_SERVER_ERROR);
	// result.setMessage("Failed, Please try again later.");
	// result.setDetails("Failed, Please try again later.");
	// }
	// } else {
	// result.setStatus(ResponseStatus.BAD_REQUEST);
	// result.setMessage("Failed, invalid request.");
	// result.setDetails("Failed, invalid request.");
	// }
	// } else {
	// result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
	// result.setMessage("Failed, Unauthorized user.");
	// result.setDetails("Failed, Unauthorized user.");
	// }
	// } else {
	// result.setStatus(ResponseStatus.INVALID_SESSION);
	// result.setMessage("Failed, Please try again later.");
	// result.setDetails("Failed, Please try again later.");
	// }
	// } else {
	// result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
	// result.setMessage("Failed, Unauthorized user.");
	// result.setDetails("Failed, Unauthorized user.");
	// }
	// return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	// }

	@RequestMapping(value = "/VerifyEmail/Process", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> verifyEmail(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody VerifyEmailDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		if (role.equalsIgnoreCase("User")) {
			VerifyEmailError error = new VerifyEmailError();
			error = registerValidation.checkMailError(dto);
			if (error.isValid()) {
				UserSession userSession = userSessionRepository.findByActiveSessionId(dto.getSessionId());
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.USER)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(dto.getSessionId());
						if (userApi.activateEmail(dto.getKey())) {
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("Email is verified successfully.");
							result.setDetails("Email is verified successfully.");
						} else {
							result.setStatus(ResponseStatus.FAILURE);
							result.setMessage("Failed to verify email.");
							result.setDetails("Failed to verify email.");
						}
					} else {
						result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
						result.setMessage("Failed, Unauthorized user.");
						result.setDetails("Failed, Unauthorized user.");
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Failed, Please try again later.");
					result.setDetails("Failed, Please try again later.");
				}
			} else {
				result.setStatus(ResponseStatus.BAD_REQUEST);
				result.setMessage("Failed, invalid request.");
				result.setDetails(error);
			}
		} else {
			result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
			result.setMessage("Failed, Unauthorized user.");
			result.setDetails("Failed, Unauthorized user.");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/GetUserDetails", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> processDataCardTopup(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody SessionDTO session, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(session, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				MobileTopupError error = new MobileTopupError();
				String sessionId = session.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.USER)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(sessionId);
						Map<String, Object> detail = new HashMap<String, Object>();
						User activeUser = userApi.findByUserName(user.getUsername());
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("User details received.");
						detail.put("userDetail", user);
						detail.put("accountDetail", activeUser.getAccountDetail());
						result.setDetails(detail);
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
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
				result.setMessage("Failed, Unauthorized user.");
				result.setDetails("Failed, Unauthorized user.");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Failed, Please try again later.");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/Invite/Email", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> inviteByEmail(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody InviteFriendsDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				MobileTopupError error = new MobileTopupError();
				String sessionId = dto.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.USER)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(sessionId);
						userApi.inviteByEmailAddress("Welcome To VPayQwik", MailTemplate.INVITE_FRIEND, dto.getEmail());
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("Invitation sent to user");

					} else {
						result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
						result.setMessage("User | Invite Friend");
						result.setDetails("Permission Not Granted");
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("User | Invite Friend");
					result.setDetails("Invalid Session");
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("User | Invite Friend");
				result.setDetails("Permission Not Granted");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/Invite/Mobile", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> inviteByMobile(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody InviteFriendsDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				MobileTopupError error = new MobileTopupError();
				String sessionId = dto.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.USER)
							&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
						persistingSessionRegistry.refreshLastRequest(sessionId);
						User u = userApi.findByUserName(user.getUsername());
						userApi.inviteByMobile(dto.getMobileNo(), dto.getMessage(),u);
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("User | Invite Friend");
					} else {
						result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
						result.setMessage("User | Invite Friend");
						result.setDetails("Permission Not Granted");
					}
				} else {
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("User | Invite Friend");
					result.setDetails("Invalid Session");
				}
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("User | Invite Friend");
				result.setDetails("Permission Not Granted");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid Secure Hash");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/GetReceipts", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> getUserReceipts(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody PagingDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response)
			throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		System.err.println("IN SIDE");
		System.err.println("11");
		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				System.err.println("11");
				UserSession userSession = userSessionRepository.findByActiveSessionId(dto.getSessionId());
				if (userSession != null) {
					System.err.println("11");
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user != null) {
						System.err.println("11");
						if (user.getAuthority().contains(Authorities.USER)
								&& user.getAuthority().contains(Authorities.AUTHENTICATED)) {
							System.err.println("11");
							Sort sort = new Sort(Sort.Direction.DESC, "id");
							Pageable pageable = new PageRequest(dto.getPage(), dto.getSize(), sort);
							Page<PQTransaction> pg = transactionApi.getTotalTransactionsOfUser(pageable,
									user.getUsername());
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("User receipts");
							result.setDetails(pg);
						} else {
							System.err.println("11");
							result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
							result.setMessage("Failed to get user receipts");
							result.setDetails("Failed to get user receipts");
						}
					}
				} else {
					System.err.println("11");
					result.setStatus(ResponseStatus.INVALID_SESSION);
					result.setMessage("Please login and try again.");
					result.setDetails("Please login and try again.");
				}
			} else {
				System.err.println("11");
				result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
				result.setMessage("Unauthorized user.");
				result.setDetails("Unauthorized user.");
			}
		} else {
			System.err.println("11");
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Invalid request.");
			result.setDetails("Invalid request.");
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/ReSendEmailOTP", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> processResendEmailOTP(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody SessionDTO session, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
		System.err.println("session ID :: " + session.getSessionId());

		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(session, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				MobileTopupError error = new MobileTopupError();
				String sessionId = session.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.USER)) {
						User userEmail = userApi.findByUserName(user.getUsername());
						userApi.reSendEmailOTP(userEmail);
						Map<String, Object> detail = new HashMap<String, Object>();
						result.setStatus(ResponseStatus.SUCCESS);
						result.setMessage("User details received.");
						detail.put("userDetail", user);
						result.setDetails(detail);
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
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
				result.setMessage("Failed, Unauthorized user.");
				result.setDetails("Failed, Unauthorized user.");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Failed, Please try again later.");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/ChangeEmail", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<ResponseDTO> processChangeEmail(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody ChangeEmailDTO session, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
		System.err.println("session ID :: " + session.getSessionId());

		ResponseDTO result = new ResponseDTO();
		boolean isValidHash = SecurityUtil.isHashMatches(session, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				MobileTopupError error = new MobileTopupError();
				String sessionId = session.getSessionId();
				UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
				if (userSession != null) {
					UserDTO user = userApi.getUserById(userSession.getUser().getId());
					if (user.getAuthority().contains(Authorities.USER)) {
						User userEmail = userApi.findByUserName(user.getUsername());
						boolean isValidEmail = CommonValidation.isValidMail(session.getEmail());
						if (isValidEmail) {
							userApi.changeEmail(userEmail, session.getEmail());
							Map<String, Object> detail = new HashMap<String, Object>();
							result.setStatus(ResponseStatus.SUCCESS);
							result.setMessage("Email ID updated Successfully");
							UserDTO updatedUser = userApi.getUserById(userSession.getUser().getId());
							detail.put("userDetail", updatedUser);
							result.setDetails(detail);
						}
						result.setStatus(ResponseStatus.BAD_REQUEST);
						result.setMessage("Email, Invaild Email ID");

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
			} else {
				result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
				result.setMessage("Failed, Unauthorized user.");
				result.setDetails("Failed, Unauthorized user.");
			}
		} else {
			result.setStatus(ResponseStatus.BAD_REQUEST);
			result.setMessage("Failed, Please try again later.");
		}
		return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
	}
}
