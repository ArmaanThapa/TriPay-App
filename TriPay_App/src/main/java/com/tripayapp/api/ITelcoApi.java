package com.tripayapp.api;

import java.util.List;

import com.tripayapp.entity.TelcoCircle;
import com.tripayapp.entity.TelcoMap;
import com.tripayapp.entity.TelcoOperator;
import com.tripayapp.entity.TelcoPlans;
import com.tripayapp.model.TelcoCircleDTO;
import com.tripayapp.model.TelcoOperatorDTO;
import com.tripayapp.model.TelcoPlansDTO;

public interface ITelcoApi {
	
	List<TelcoOperator> getAllOperators();

	List<TelcoCircle> getAllCircles();

	TelcoCircle findTelcoCircleByCode(String code);

	TelcoCircleDTO findTelcoCircleDTOByCode(String code);

	void saveTelcoCircle(TelcoCircle dto);

	TelcoOperator findTelcoOperatorByCode(String code);

	TelcoOperatorDTO findTelcoOperatorDTOByCode(String code);

	void saveTelcoOperator(TelcoOperator dto);

	TelcoPlans findTelcoPlans(TelcoPlans plans);

	void saveTelcoPlans(TelcoPlans dto);

	Long countTelcoPlans();

	TelcoMap findTelcoMapByNumber(String number);

	void saveTelcoMap(TelcoMap dto);

	Long countTelcoMap();

	public List<TelcoOperatorDTO> getAllOperatorsDTO();

	public List<TelcoCircleDTO> getAllCirclesDTO();

	public List<TelcoPlans> getTelcoPlansByOperatorCircle(TelcoOperator operator, TelcoCircle circle);

	public List<TelcoPlansDTO> getTelcoPlansDTOByOperatorCircle(TelcoOperatorDTO operatorDTO, TelcoCircleDTO circleDTO);

}
