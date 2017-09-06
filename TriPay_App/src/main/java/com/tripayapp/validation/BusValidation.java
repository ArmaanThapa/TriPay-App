package com.tripayapp.validation;

import com.tripayapp.model.error.BlockBusTicketError;
import com.thirdparty.model.request.BookBusTicketRequest;

public class BusValidation {

	public BlockBusTicketError checkBusDetailsError (BookBusTicketRequest request) {
		
		BlockBusTicketError error = new BlockBusTicketError();
		boolean valid = true;
		
		if(CommonValidation.isNull(request.getTransactionRefNo())) {
			error.setTransactionRefNo("Transaction ref no cannot be null");
			valid = false;
		}
		
		
		
		
		
			
			
		error.setValid(valid);
		return error;
		
	}
}
