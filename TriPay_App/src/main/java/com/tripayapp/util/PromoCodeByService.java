package com.tripayapp.util;

public class PromoCodeByService {

	public static String findByService(String service) {
		try {
			if (service.equals("LMC"))
				return "PLMS";
			else if (service.equals("SMR"))
				return "PSMS";
			else if (service.equals("SMB"))
				return "PSMS";
			else if (service.equals("SMU"))
				return "PSMS";
			else if (service.equals("ATP"))
				return "PTPS";
			else if (service.equals("VTK"))
				return "PBPS";
			else if (service.equals("TTK"))
				return "PBPS";
			else if (service.equals("STK"))
				return "PBPS";
			else if (service.equals("DTK"))
				return "PBPS";
			else if (service.equals("ATK"))
				return "PBPS";
			else if (service.equals("VTV"))
				return "PBPS";
			else if (service.equals("OTV"))
				return "PBPS";
			else if (service.equals("TTV"))
				return "PBPS";
			else if (service.equals("STV"))
				return "PBPS";
			else if (service.equals("RTV"))
				return "PBPS";
			else if (service.equals("DTV"))
				return "PBPS";
			else if (service.equals("ATV"))
				return "PBPS";
			else if (service.equals("TTE"))
				return "PBPS";
			else if (service.equals("TPE"))
				return "PBPS";
			else if (service.equals("NDE"))
				return "PBPS";
			else if (service.equals("STE"))
				return "PBPS";
			else if (service.equals("SAE"))
				return "PBPS";
			else if (service.equals("REE"))
				return "PBPS";
			else if (service.equals("MPE"))
				return "PBPS";
			else if (service.equals("DOE"))
				return "PBPS";
			else if (service.equals("NUE"))
				return "PBPS";
			else if (service.equals("MDE"))
				return "PBPS";
			else if (service.equals("MEE"))
				return "PBPS";
			else if (service.equals("DRE"))
				return "PBPS";
			else if (service.equals("JUE"))
				return "PBPS";
			else if (service.equals("JRE"))
				return "PBPS";
			else if (service.equals("IPE"))
				return "PBPS";
			else if (service.equals("DNE"))
				return "PBPS";
			else if (service.equals("DHE"))
				return "PBPS";
			else if (service.equals("CCE"))
				return "PBPS";
			else if (service.equals("CWE"))
				return "PBPS";
			else if (service.equals("BYE"))
				return "PBPS";
			else if (service.equals("BRE"))
				return "PBPS";
			else if (service.equals("BME"))
				return "PBPS";
			else if (service.equals("BBE"))
				return "PBPS";
			else if (service.equals("AAE"))
				return "PBPS";
			else if (service.equals("ARE"))
				return "PBPS";
			else if (service.equals("MMG"))
				return "PBPS";
			else if (service.equals("IPG"))
				return "PBPS";
			else if (service.equals("GJG"))
				return "PBPS";
			else if (service.equals("GSG"))
				return "PBPS";
			else if (service.equals("ADG"))
				return "PBPS";
			else if (service.equals("TAI"))
				return "PBPS";
			else if (service.equals("ILI"))
				return "PBPS";
			else if (service.equals("IPI"))
				return "PBPS";
			else if (service.equals("BAI"))
				return "PBPS";
			else if (service.equals("TDB"))
				return "PTPS";
			else if (service.equals("TCL"))
				return "PTPS";
			else if (service.equals("RGL"))
				return "PTPS";
			else if (service.equals("MDL"))
				return "PTPS";
			else if (service.equals("BGL"))
				return "PTPS";
			else if (service.equals("ATL"))
				return "PTPS";
			else if (service.equals("VFC"))
				return "PTPS";
			else if (service.equals("TDC"))
				return "PTPS";
			else if (service.equals("RGC"))
				return "PTPS";
			else if (service.equals("MTC"))
				return "PTPS";
			else if (service.equals("IDC"))
				return "PTPS";
			else if (service.equals("BGC"))
				return "PTPS";
			else if (service.equals("ATC"))
				return "PTPS";
			else if (service.equals("ACC"))
				return "PTPS";
			else if (service.equals("VFP"))
				return "PTPS";
			else if (service.equals("VGP"))
				return "PTPS";
			else if (service.equals("VSP"))
				return "PTPS";
			else if (service.equals("UGP"))
				return "PTPS";
			else if (service.equals("USP"))
				return "PTPS";
			else if (service.equals("TGP"))
				return "PTPS";
			else if (service.equals("TSP"))
				return "PTPS";
			else if (service.equals("TCP"))
				return "PTPS";
			else if (service.equals("TMP"))
				return "PTPS";
			else if (service.equals("TVP"))
				return "PTPS";
			else if (service.equals("RGP"))
				return "PTPS";
			else if (service.equals("MTP"))
				return "PTPS";
			else if (service.equals("MMP"))
				return "PTPS";
			else if (service.equals("MSP"))
				return "PTPS";
			else if (service.equals("IDP"))
				return "PTPS";
			else if (service.equals("BGP"))
				return "PTPS";
			else if (service.equals("BVP"))
				return "PTPS";
			else if (service.equals("ACP"))
				return "PTPS";
			else if (service.equals("BUS"))
				return "PBPS";
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		}

		return "Not Available";
	}

	public static String findPromoServiceByPromoCode(String code) {
		return code;
	}
}
