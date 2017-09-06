package com.tripayapp.controller.api.v1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tripayapp.api.ITelcoApi;
import com.tripayapp.entity.TelcoMap;
import com.tripayapp.model.PlanDTO;
import com.tripayapp.model.SessionDTO;
import com.tripayapp.model.TelcoCircleDTO;
import com.tripayapp.model.TelcoDTO;
import com.tripayapp.model.TelcoOperatorDTO;
import com.tripayapp.model.TelcoPlansDTO;
import com.tripayapp.model.mobile.CircleOperatorDTO;
import com.tripayapp.model.mobile.PlansDTO;
import com.tripayapp.model.mobile.ResponseStatus;
import com.tripayapp.util.SecurityUtil;

@Controller
@RequestMapping("/Api/v1/{role}/{device}/{language}/Plans")
public class BrowsePlansController implements MessageSourceAware {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private MessageSource messageSource;

	private final ITelcoApi telcoApi;

	public BrowsePlansController(ITelcoApi telcoApi) {
		this.telcoApi = telcoApi;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/GetTelco", produces = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<String> getUserList(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestHeader(value = "hash", required = true) String hash, HttpServletRequest request,
			HttpServletResponse response, @RequestBody TelcoDTO dto)
					throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				if (dto.getMobileNumber() != null) {
					String fourDigit = dto.getMobileNumber().substring(0, Math.min(dto.getMobileNumber().length(), 4));
					String fiveDigit = dto.getMobileNumber().substring(0, Math.min(dto.getMobileNumber().length(), 5));
					TelcoMap telcoMap = telcoApi.findTelcoMapByNumber(fourDigit);
					if (telcoMap == null) {
						telcoMap = telcoApi.findTelcoMapByNumber(fiveDigit);
					}
					if (telcoMap != null) {
						ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
						String json = ow.writeValueAsString(telcoMap);
						return new ResponseEntity<String>(json, HttpStatus.OK);
					}
				}
			}
		} else {
			return new ResponseEntity<String>("Invalid Secure Hash", HttpStatus.OK);
		}
		return new ResponseEntity<String>("{\"telcoMap\":[]}", HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/GetOperatorsCircles", produces = {
			MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<String> getOperators(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody SessionDTO session, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response)
					throws JSONException, JsonMappingException, IOException {
		String json = "";
		boolean isValidHash = SecurityUtil.isHashMatches(session, hash);
		if (isValidHash) {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			if (role.equalsIgnoreCase("User")) {
				CircleOperatorDTO circleOperator = new CircleOperatorDTO();
				List<TelcoOperatorDTO> operators = telcoApi.getAllOperatorsDTO();
				List<TelcoCircleDTO> circles = telcoApi.getAllCirclesDTO();
				if (operators != null && circles != null) {
					circleOperator.setStatus(ResponseStatus.SUCCESS);
					circleOperator.setMessage("Get Operator and circles");
					circleOperator.setCircles(circles);
					circleOperator.setOperators(operators);
					json = ow.writeValueAsString(circleOperator);
				} else {
					circleOperator.setStatus(ResponseStatus.FAILURE);
					circleOperator.setMessage("Get Operator and circles");
					circleOperator.setCircles(new ArrayList<TelcoCircleDTO>());
					circleOperator.setOperators(new ArrayList<TelcoOperatorDTO>());
					json = ow.writeValueAsString(circleOperator);
				}
			}
		} else {
			return new ResponseEntity<String>("Invalid Secure Hash", HttpStatus.OK);
		}
		return new ResponseEntity<String>(json, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/GetPlans", produces = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<String> getPlansByOperatorCircle(@PathVariable(value = "role") String role,
			@PathVariable(value = "device") String device, @PathVariable(value = "language") String language,
			@RequestBody PlanDTO dto, @RequestHeader(value = "hash", required = true) String hash,
			HttpServletRequest request, HttpServletResponse response)
					throws JSONException, JsonMappingException, IOException {
		String json = "";
		boolean isValidHash = SecurityUtil.isHashMatches(dto, hash);
		if (isValidHash) {
			if (role.equalsIgnoreCase("User")) {
				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				PlansDTO pdto = new PlansDTO();
				TelcoOperatorDTO operatorDTO = telcoApi.findTelcoOperatorDTOByCode(dto.getOperatorCode());
				TelcoCircleDTO circleDTO = telcoApi.findTelcoCircleDTOByCode(dto.getCircleCode());
				if (operatorDTO != null && circleDTO != null) {
					List<TelcoPlansDTO> plans = telcoApi.getTelcoPlansDTOByOperatorCircle(operatorDTO, circleDTO);
					if (plans != null) {
						pdto.setStatus(ResponseStatus.SUCCESS);
						pdto.setMessage("Success");
						pdto.setCircle(circleDTO);
						pdto.setOperator(operatorDTO);
						pdto.setPlans(plans);
						json = ow.writeValueAsString(pdto);
						return new ResponseEntity<String>(json, HttpStatus.OK);
					}
				}
				pdto.setStatus(ResponseStatus.FAILURE);
				pdto.setMessage("Failed");
				pdto.setOperator(new TelcoOperatorDTO());
				pdto.setCircle(new TelcoCircleDTO());
				pdto.setPlans(new ArrayList<TelcoPlansDTO>());
				json = ow.writeValueAsString(pdto);
			}
		} else {
			json = "Invalid Secure Hash";
		}
		return new ResponseEntity<String>(json, HttpStatus.OK);
	}

}
