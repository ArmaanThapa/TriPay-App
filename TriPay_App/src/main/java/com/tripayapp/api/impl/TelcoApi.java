package com.tripayapp.api.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tripayapp.api.ITelcoApi;
import com.tripayapp.entity.TelcoCircle;
import com.tripayapp.entity.TelcoMap;
import com.tripayapp.entity.TelcoOperator;
import com.tripayapp.entity.TelcoPlans;
import com.tripayapp.model.TelcoCircleDTO;
import com.tripayapp.model.TelcoOperatorDTO;
import com.tripayapp.model.TelcoPlansDTO;
import com.tripayapp.repositories.TelcoCircleRepository;
import com.tripayapp.repositories.TelcoMapRepository;
import com.tripayapp.repositories.TelcoOperatorRepository;
import com.tripayapp.repositories.TelcoPlansRepository;
import com.tripayapp.util.ConvertUtil;

public class TelcoApi implements ITelcoApi {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final TelcoCircleRepository telcoCircleRepository;
	private final TelcoOperatorRepository telcoOperatorRepository;
	private final TelcoPlansRepository telcoPlansRepository;
	private final TelcoMapRepository telcoMapRepository;

	public TelcoApi(TelcoCircleRepository telcoCircleRepository, TelcoOperatorRepository telcoOperatorRepository,
			TelcoPlansRepository telcoPlansRepository, TelcoMapRepository telcoMapRepository) {
		this.telcoCircleRepository = telcoCircleRepository;
		this.telcoOperatorRepository = telcoOperatorRepository;
		this.telcoPlansRepository = telcoPlansRepository;
		this.telcoMapRepository = telcoMapRepository;
	}

	@Override
	public TelcoCircle findTelcoCircleByCode(String code) {
		return telcoCircleRepository.findTelcoCircleByCode(code);
	}

	@Override
	public TelcoOperator findTelcoOperatorByCode(String code) {
		return telcoOperatorRepository.findTelcoOperatorByCode(code);
	}

	@Override
	public TelcoPlans findTelcoPlans(TelcoPlans plans) {
		return telcoPlansRepository.findTelcoPlans(plans.getOperator().getCode(), plans.getCircle().getCode(),
				plans.getPlanName(), plans.getAmount());
	}

	@Override
	public Long countTelcoPlans() {
		return telcoPlansRepository.countTelcoPlans();
	}

	@Override
	public Long countTelcoMap() {
		return telcoMapRepository.countTelcoMap();
	}

	@Override
	public TelcoMap findTelcoMapByNumber(String number) {
		return telcoMapRepository.findTelcoMapByNumber(number);
	}

	@Override
	public void saveTelcoCircle(TelcoCircle dto) {
		TelcoCircle circle = findTelcoCircleByCode(dto.getCode());
		if (circle == null) {
			telcoCircleRepository.save(dto);
		}
	}

	@Override
	public void saveTelcoOperator(TelcoOperator dto) {
		TelcoOperator operator = findTelcoOperatorByCode(dto.getCode());
		if (operator == null) {
			telcoOperatorRepository.save(dto);
		}
	}

	@Override
	public void saveTelcoPlans(TelcoPlans dto) {
		TelcoPlans plans = findTelcoPlans(dto);
		if (plans == null) {
			telcoPlansRepository.save(dto);
		}
	}

	@Override
	public void saveTelcoMap(TelcoMap dto) {
		TelcoMap map = findTelcoMapByNumber(dto.getNumber());
		if (map == null) {
			telcoMapRepository.save(dto);
		}
	}

	@Override
	public List<TelcoOperator> getAllOperators() {
		return telcoOperatorRepository.findAllOperators();
	}

	@Override
	public List<TelcoCircle> getAllCircles() {
		return telcoCircleRepository.findAllCircles();
	}

	@Override
	public List<TelcoOperatorDTO> getAllOperatorsDTO() {
		List<TelcoOperator> operators = telcoOperatorRepository.findAllOperators();
		if (operators != null && operators.size() != 0) {
			List<TelcoOperatorDTO> telcoOperatorDTOs = ConvertUtil.convertTelcoOperatorList(operators);
			return telcoOperatorDTOs;
		}
		return null;
	}

	@Override
	public List<TelcoCircleDTO> getAllCirclesDTO() {

		List<TelcoCircle> circles = telcoCircleRepository.findAllCircles();
		if (circles != null && circles.size() != 0) {
			List<TelcoCircleDTO> telcoCircleDTOs = ConvertUtil.convertTelcoCircleList(circles);
			return telcoCircleDTOs;
		}
		return null;
	}

	@Override
	public TelcoCircleDTO findTelcoCircleDTOByCode(String code) {
		TelcoCircle circle = telcoCircleRepository.findTelcoCircleByCode(code);
		if (circle != null) {
			return ConvertUtil.convertTelcoCircle(circle);
		}
		return null;
	}

	@Override
	public TelcoOperatorDTO findTelcoOperatorDTOByCode(String code) {
		TelcoOperator operator = telcoOperatorRepository.findTelcoOperatorByCode(code);
		if (operator != null) {
			return ConvertUtil.convertTelcoOperator(operator);
		}
		return null;
	}

	@Override
	public List<TelcoPlans> getTelcoPlansByOperatorCircle(TelcoOperator operator, TelcoCircle circle) {
		if (operator != null && circle != null) {
			List<TelcoPlans> telcoPlans = telcoPlansRepository.findTelcoPlansByOperatorCircle(operator.getCode(),
					circle.getCode());
			return telcoPlans;
		}
		return null;
	}
	
	@Override
	public List<TelcoPlansDTO> getTelcoPlansDTOByOperatorCircle(TelcoOperatorDTO operatorDTO, TelcoCircleDTO circleDTO) {
		if (operatorDTO != null && circleDTO != null) {
			List<TelcoPlans> telcoPlans = telcoPlansRepository.findTelcoPlansByOperatorCircle(operatorDTO.getCode(),
					circleDTO.getCode());
			if (telcoPlans != null) {
				return ConvertUtil.convertTelcoPlansList(telcoPlans);
			}
		}
		return null;
	}

}
