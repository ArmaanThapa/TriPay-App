package com.tripayapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.tripayapp.validation.CommonValidation;

public class Test {
	public static void main( String[] args )
    {
    	try{
    		System.err.println(445.24999999999995);
    		double netCommissionValue = 445.24999999999995;
    		String v = String.format("%.2f", netCommissionValue);
    		netCommissionValue = Double.parseDouble(v);
    		System.err.println(netCommissionValue);
    		

//    		String amt = "110";
//    		System.err.println("Amount :: " + amt);
//    		System.err.println("Changed Amount :: " + "0000000" + amt + "00");
    		
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	Date startDate = sdf.parse("2016-08-25 16:50:08");
        	Date endDate = sdf.parse("2016-08-25 16:50:08");
        	Date currentDate = sdf.parse("2016-12-29 13:05:14");
        	
//        	Calendar calendar = Calendar.getInstance();
//			calendar.setTimeInMillis(System.currentTimeMillis());
//			Date currentDate = sdf.parse(sdf.format(calendar.getTime()));
			

        	System.out.println(sdf.format(startDate));
        	System.out.println(sdf.format(endDate));
        	System.out.println(sdf.format(currentDate));
        	
        	
        	if(startDate.compareTo(currentDate)<0){
        		if(endDate.compareTo(currentDate)>0){
        			System.out.println("PromoCode Useable");
        		}
        		else{
        			System.out.println("PromoCode Date Expire");
        		}
        	}
        	
        	String promocode = "POMT123450";
        	String temp = promocode.trim();
    		if (temp.length() >= 6 && temp.length()<=10) {
    			System.err.println("6 to 10 digit");
    		} else {
    			System.err.println("not 6 to 10 digit");
    		}
//        	if(!CommonValidation.checkLength6(promocode))
//        		System.err.println("6 digit");
//        	else
//        		System.err.println("not 6 digit");
    		
    		String service = PromoCodeByService.findByService("LMC");
    		System.err.println("SERVICE :: " + service);
    	}catch(ParseException ex){
    		System.err.println("Unable to Parse");
//    		ex.printStackTrace();
    	}
    }
}
