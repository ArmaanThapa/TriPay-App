package com.thirdparty.api;

import com.tripayapp.entity.PQService;
import com.tripayapp.entity.User;
import com.tripayapp.model.mobile.ResponseDTO;
import com.tripayapp.util.ClientException;
import com.thirdparty.model.request.BookBusTicketRequest;

public interface IBookBusTicketApi {

	void saveBusTicket(BookBusTicketRequest req, User user, PQService service);

	void bookBusTicket(BookBusTicketRequest req, User user);

	void failBookBusTicket(BookBusTicketRequest req);

	boolean checkBalance(User user, BookBusTicketRequest req);
	
//	BookBusTicketResponse bookBusTicket(BookBusTicketRequest request);
	
//	ResponseDTO saveBusDetails (BookBusTicketRequest request) throws ClientException;
}
