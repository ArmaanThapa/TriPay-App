package com.tripayapp.startup;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tripayapp.api.ITelcoApi;
import com.tripayapp.entity.TelcoCircle;
import com.tripayapp.entity.TelcoMap;
import com.tripayapp.entity.TelcoOperator;
import com.tripayapp.entity.TelcoPlans;
import com.tripayapp.util.CommonUtil;
import com.tripayapp.util.StartupUtil;

public class TelcoCreator {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private ITelcoApi telcoApi;

	public TelcoCreator(ITelcoApi telcoApi) {
		this.telcoApi = telcoApi;
	}

//	public void create() {}
	public void create() {

		String fileNameTelcoCircle = StartupUtil.CSV_FILE + "telco_circle.csv";
		ArrayList<TelcoCircle> telcoCircles = readTelcoCircleFromFile(fileNameTelcoCircle);
		if (telcoCircles != null) {
			for (TelcoCircle telcoCircle : telcoCircles) {
				logger.debug("Telco Circle :: " + telcoCircle.getName());
				telcoApi.saveTelcoCircle(telcoCircle);
			}
		}

		String fileNameTelcoOperator = StartupUtil.CSV_FILE + "telco_operator.csv";
		ArrayList<TelcoOperator> telcoOperators = readTelcoOperatorFromFile(fileNameTelcoOperator);
		if (telcoOperators != null) {
			for (TelcoOperator telcoOperator : telcoOperators) {
				logger.debug("Telco Operator :: " + telcoOperator.getName());
				telcoApi.saveTelcoOperator(telcoOperator);
			}
		}

		String fileNameTelcoPlans = StartupUtil.CSV_FILE + "telco_plans.csv";
		ArrayList<TelcoPlans> telcoPlans = readTelcoPlansFromFile(fileNameTelcoPlans);
		Long countTelcoPlans = telcoApi.countTelcoPlans();
		if (telcoPlans != null) {
			CommonUtil.sleep(3000);
			if (telcoPlans.size() != countTelcoPlans) {
				CommonUtil.sleep(3000);
				for (TelcoPlans telcoPlan : telcoPlans) {
					logger.debug("Telco Plan ::" + telcoPlan.getDescription());
					telcoApi.saveTelcoPlans(telcoPlan);
				}
			}
		}

		String fileNameTelcoMap = StartupUtil.CSV_FILE + "telco_map.csv";
		ArrayList<TelcoMap> telcoMaps = readTelcoMapFromFile(fileNameTelcoMap);
		Long countTelcoMap = telcoApi.countTelcoMap();
		if (telcoMaps != null) {
			CommonUtil.sleep(3000);
			if (telcoMaps.size() != countTelcoMap) {
				CommonUtil.sleep(3000);
				for (TelcoMap telcoMap : telcoMaps) {
					logger.debug("Telco Map ::" + telcoMap.getNumber());
					telcoApi.saveTelcoMap(telcoMap);
				}
			}
		}
	}

	public ArrayList<TelcoCircle> readTelcoCircleFromFile(String fileName) {
		ArrayList<TelcoCircle> circles = new ArrayList<>();

		BufferedReader br = null;
		String line = "";
		try {

			br = new BufferedReader(new FileReader(fileName));
			TelcoCircle c = null;
			while ((line = br.readLine()) != null) {
				Pattern p = Pattern.compile("(([^\"][^,]*)|\"([^\"]*)\"),?");
				Matcher m = p.matcher(line);

				c = new TelcoCircle();
				String value = null;
				int index = 1;
				while (m.find()) {
					if (m.group(2) != null) {
						value = m.group(2);
					}

					if (m.group(3) != null) {
						value = m.group(3);
					}

					if (value != null) {
						if (c != null) {
							switch (index) {
							case 1:
								c.setCode(value);
								break;
							case 2:
								c.setName(value);
								break;
							default:
								break;
							}
							index = index + 1;
						}
					}

					if (m.hitEnd()) {
						circles.add(c);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return circles;
	}

	public ArrayList<TelcoOperator> readTelcoOperatorFromFile(String fileName) {
		ArrayList<TelcoOperator> operators = new ArrayList<>();

		BufferedReader br = null;
		String line = "";
		try {
			br = new BufferedReader(new FileReader(fileName));
			TelcoOperator c = null;
			while ((line = br.readLine()) != null) {
				Pattern p = Pattern.compile("(([^\"][^,]*)|\"([^\"]*)\"),?");
				Matcher m = p.matcher(line);

				c = new TelcoOperator();
				String value = null;
				int index = 1;
				while (m.find()) {
					if (m.group(2) != null) {
						value = m.group(2);
					}

					if (m.group(3) != null) {
						value = m.group(3);
					}

					if (value != null) {
						if (c != null) {
							switch (index) {
							case 1:
								c.setCode(value);
								break;
							case 2:
								c.setName(value);
								break;
							case 3:
								c.setServiceCode(value);
								break;
							case 4:
								c.setTopupCode(value);
								break;
							case 5:
								c.setFlexiCode(value);
								break;
							case 6:
								c.setSpecialCode(value);
								break;
							case 7:
								c.setValidityCode(value);
								break;
							default:
								break;
							}
							index = index + 1;
						}
					}

					if (m.hitEnd()) {
						operators.add(c);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return operators;
	}

	public ArrayList<TelcoPlans> readTelcoPlansFromFile(String fileName) {
		ArrayList<TelcoPlans> operators = new ArrayList<>();
		BufferedReader br = null;
		String line = "";
		try {

			br = new BufferedReader(new FileReader(fileName));
			TelcoPlans c = null;
			while ((line = br.readLine()) != null) {
				Pattern p = Pattern.compile("(([^\"][^,]*)|\"([^\"]*)\"),?");
				Matcher m = p.matcher(line);

				c = new TelcoPlans();
				String value = null;
				int index = 1;
				while (m.find()) {
					if (m.group(2) != null) {
						value = m.group(2);
					}

					if (m.group(3) != null) {
						value = m.group(3);
					}

					if (value != null) {
						if (c != null) {
							switch (index) {
							case 1:
								TelcoOperator o = telcoApi.findTelcoOperatorByCode(value);
								c.setOperator(o);
								break;
							case 2:
								c.setState(value);
								break;
							case 3:
								c.setPlanName(value);
								break;
							case 4:
								c.setAmount(value);
								break;
							case 5:
								c.setDescription(value);
								break;
							case 6:
								TelcoCircle tc = telcoApi.findTelcoCircleByCode(value);
								c.setCircle(tc);
								break;
							case 7:
								c.setValidity(value);
								break;
							case 8:
								c.setPlanType(value);
								break;
							case 9:
								c.setOperatorCode(value);
								break;
							case 10:
								c.setSmsDaakCode("V"+value);
								break;
							default:
								break;
							}
							index = index + 1;
						}
					}

					if (m.hitEnd()) {
						if (c.getOperator() != null && c.getCircle() != null) {
							operators.add(c);
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return operators;
	}

	public ArrayList<TelcoMap> readTelcoMapFromFile(String fileName) {
		ArrayList<TelcoMap> maps = new ArrayList<>();

		BufferedReader br = null;
		String line = "";
		try {

			br = new BufferedReader(new FileReader(fileName));
			TelcoMap c = null;
			while ((line = br.readLine()) != null) {
				Pattern p = Pattern.compile("(([^\"][^,]*)|\"([^\"]*)\"),?");
				Matcher m = p.matcher(line);

				c = new TelcoMap();
				String value = null;
				int index = 1;
				while (m.find()) {
					if (m.group(2) != null) {
						value = m.group(2);
					}

					if (m.group(3) != null) {
						value = m.group(3);
					}

					if (value != null) {
						if (c != null) {
							switch (index) {
							case 1:
								c.setNumber(value);
								break;
							case 2:
								TelcoCircle tc = telcoApi.findTelcoCircleByCode(value);
								c.setCircle(tc);
								break;
							case 3:
								TelcoOperator to = telcoApi.findTelcoOperatorByCode(value);
								c.setOperator(to);
								break;
							default:
								break;
							}
							index = index + 1;
						}
					}

					if (m.hitEnd()) {
						if (c.getCircle() != null && c.getOperator() != null) {
							maps.add(c);
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return maps;
	}

}
