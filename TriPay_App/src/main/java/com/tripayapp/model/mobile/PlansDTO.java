package com.tripayapp.model.mobile;

import java.util.List;

import com.tripayapp.model.TelcoCircleDTO;
import com.tripayapp.model.TelcoOperatorDTO;
import com.tripayapp.model.TelcoPlansDTO;

public class PlansDTO {

	private String status;
	private String code;
	private String message;
	private TelcoCircleDTO circle;
	private TelcoOperatorDTO operator;
	private List<TelcoPlansDTO> plans;

	public String getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status.getKey();
		this.code = status.getValue();
	}

	public String getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public TelcoCircleDTO getCircle() {
		return circle;
	}

	public void setCircle(TelcoCircleDTO circle) {
		this.circle = circle;
	}

	public TelcoOperatorDTO getOperator() {
		return operator;
	}

	public void setOperator(TelcoOperatorDTO operator) {
		this.operator = operator;
	}

	public List<TelcoPlansDTO> getPlans() {
		return plans;
	}

	public void setPlans(List<TelcoPlansDTO> plans) {
		this.plans = plans;
	}

}
