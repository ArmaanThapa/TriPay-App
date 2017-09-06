package com.tripayapp.model.mobile;

import java.util.List;

import com.tripayapp.model.TelcoCircleDTO;
import com.tripayapp.model.TelcoOperatorDTO;

public class CircleOperatorDTO {

	private String status;
	private String code;
	private String message;
	private List<TelcoCircleDTO> circles;
	private List<TelcoOperatorDTO> operators;

	public String getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status.getKey();
		code = status.getValue();
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

	public List<TelcoCircleDTO> getCircles() {
		return circles;
	}

	public void setCircles(List<TelcoCircleDTO> circles) {
		this.circles = circles;
	}

	public List<TelcoOperatorDTO> getOperators() {
		return operators;
	}

	public void setOperators(List<TelcoOperatorDTO> operators) {
		this.operators = operators;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
