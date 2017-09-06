/**
 * this controller manages all request for Versioning
 *
 */
package com.tripayapp.controller.api.v1;

import com.tripayapp.api.ISessionApi;
import com.tripayapp.api.IUserApi;
import com.tripayapp.entity.UserSession;
import com.tripayapp.model.SessionDTO;
import com.tripayapp.model.UserDTO;
import com.tripayapp.model.Utility;
import com.tripayapp.model.VersionDTO;
import com.tripayapp.model.mobile.*;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.repositories.UserSessionRepository;
import com.tripayapp.session.PersistingSessionRegistry;
import com.tripayapp.util.Authorities;
import com.tripayapp.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/Api/v1/{role}/{device}/{language}")
public class VersionController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final IUserApi userApi;
    private final UserSessionRepository userSessionRepository;
    private final PersistingSessionRegistry persistingSessionRegistry;
    private final ISessionApi sessionApi;

    public VersionController(IUserApi userApi,UserSessionRepository userSessionRepository,PersistingSessionRegistry persistingSessionRegistry,ISessionApi sessionApi){
        this.userApi = userApi;
        this.userSessionRepository = userSessionRepository;
        this.persistingSessionRegistry = persistingSessionRegistry;
        this.sessionApi = sessionApi;
    }


    @RequestMapping(value={"/Version/{operation}"},method= RequestMethod.POST ,produces= MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ResponseDTO> getAllVersions(@PathVariable(value = "role") String role,
                                               @PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
                                               @PathVariable(value= "operation") String operation ,@RequestBody SessionDTO dto, @RequestHeader(value = "hash", required = true) String hash,
                                               HttpServletRequest request, HttpServletResponse response){

        ResponseDTO result = new ResponseDTO();
        boolean isValidHash = SecurityUtil.isHashMatches(dto,hash);
        if(isValidHash) {
            if (role.equalsIgnoreCase("Admin")) {
                String sessionId = dto.getSessionId();
                UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
                if (userSession != null) {
                    UserDTO user = userApi.getUserById(userSession.getUser().getId());
                    if (user.getAuthority().contains(Authorities.ADMINISTRATOR)
                            && user.getAuthority().contains(Authorities.AUTHENTICATED)) {
                        persistingSessionRegistry.refreshLastRequest(sessionId);
                        result.setStatus(ResponseStatus.SUCCESS);
                        switch(operation.toUpperCase()){
                            case "ALL" :
                                result.setMessage("All Versions");
                                result.setDetails(userApi.getAllVersions());
                                break;
                            case "LATEST":
                                result.setMessage("Latest Version");
                                result.setDetails(userApi.getLatestVersion());
                                break;
                            default:
                                result.setMessage("Not a valid operation");
                                result.setDetails("not a valid operation");
                                break;
                        }

                    }else {
                        result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
                        result.setMessage("Permission Not Granted");
                        result.setDetails("Permission Not Granted");
                    }

                    }
                } else {
                result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
                result.setMessage("Failed,Unauthorized User");
                result.setDetails("Failed,Unauthorized User");
            }
        }else {
            result.setStatus(ResponseStatus.INVALID_HASH);
            result.setMessage("Invalid Hash");
            result.setDetails("Invalid Hash");
        }
        return new ResponseEntity<ResponseDTO>(result, HttpStatus.OK);
    }



    @RequestMapping(value={"/AuthenticateVersion","/ValidateVersion"},method=RequestMethod.POST ,produces=MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ResponseDTO> checkVersion(@PathVariable(value = "role") String role,
                                             @PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
                                             @RequestBody Utility utility, @RequestHeader(value = "hash", required = true) String hash,
                                             HttpServletRequest request, HttpServletResponse response){

        ResponseDTO result = new ResponseDTO();
        boolean isValidVersion = false;
        String info = null;
        String message = userApi.authenticateVersion(utility.getVersion());
        if(role.equalsIgnoreCase("User")) {
            if (message.contains("|")) {
                String[] parts = message.split("\\|");
                isValidVersion = Boolean.parseBoolean(parts[0]);
                info = parts[1];
                if (isValidVersion) {
                    result.setStatus(ResponseStatus.SUCCESS);
                } else {
                    result.setStatus(ResponseStatus.INVALID_VERSION);
                }
                result.setMessage(info);
            } else {
                result.setStatus(ResponseStatus.FAILURE);
                result.setMessage(message);
            }
        }
        return new ResponseEntity<ResponseDTO>(result,HttpStatus.OK);
    }

    @RequestMapping(value={"/NewVersion","/AddVersion","/UpdateVersion"},method=RequestMethod.POST ,produces=MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ResponseDTO> incrementVersion(@PathVariable(value = "role") String role,
                                                 @PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
                                                 @RequestBody VersionDTO version, @RequestHeader(value = "hash", required = true) String hash,
                                                 HttpServletRequest request, HttpServletResponse response){

        ResponseDTO result = new ResponseDTO();
        int versionCode = Integer.parseInt(version.getVersionCode());
        int subVersionCode = Integer.parseInt(version.getSubVersionCode());
        boolean isValidHash = SecurityUtil.isHashMatches(version,hash);
        if(isValidHash) {
            if (role.equalsIgnoreCase("Admin")) {
                String sessionId = version.getSessionId();
                UserSession userSession = userSessionRepository.findByActiveSessionId(sessionId);
                if (userSession != null) {
                    UserDTO user = userApi.getUserById(userSession.getUser().getId());
                    if (user.getAuthority().contains(Authorities.ADMINISTRATOR)
                            && user.getAuthority().contains(Authorities.AUTHENTICATED)) {
                        persistingSessionRegistry.refreshLastRequest(sessionId);
                        result.setStatus(ResponseStatus.SUCCESS);
                        int rowsUpdated = userApi.updateVersion(versionCode,subVersionCode);
                        if(rowsUpdated > 0){
                            result.setStatus(ResponseStatus.SUCCESS);
                            result.setMessage("Version Successfully Updated to "+version.getVersionCode()+"."+version.getSubVersionCode());
                        }else {
                            result.setStatus(ResponseStatus.FAILURE);
                            result.setMessage("Problem Updating Version,Please try again later");
                         }
                    }else {
                        result.setStatus(ResponseStatus.UNAUTHORIZED_ROLE);
                        result.setMessage("Permission Not Granted");
                        result.setDetails("Permission Not Granted");
                    }
                }
            } else {
                result.setStatus(ResponseStatus.UNAUTHORIZED_USER);
                result.setMessage("Failed,Unauthorized User");
                result.setDetails("Failed,Unauthorized User");
            }
        }else {
            result.setStatus(ResponseStatus.INVALID_HASH);
            result.setMessage("Invalid Hash");
            result.setDetails("Invalid Hash");
        }
        return new ResponseEntity<ResponseDTO>(result,HttpStatus.OK);
    }

}

