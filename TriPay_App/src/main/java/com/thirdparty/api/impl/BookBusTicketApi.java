package com.thirdparty.api.impl;

import java.util.Date;

import com.tripayapp.api.ITransactionApi;
import com.tripayapp.entity.PQService;
import com.tripayapp.entity.PQTransaction;
import com.tripayapp.entity.TravelBusDetail;
import com.tripayapp.entity.TravelBusTransaction;
import com.tripayapp.entity.TravelSeatDetail;
import com.tripayapp.entity.TravelUserDetail;
import com.tripayapp.entity.User;
import com.tripayapp.model.Status;
import com.tripayapp.repositories.BusDetailRepository;
import com.tripayapp.repositories.BusTransactionRepository;
import com.tripayapp.repositories.PassengerDetailsRepository;
import com.tripayapp.repositories.SeatDetailRepository;
import com.tripayapp.repositories.TravelUserDetailRepository;
import com.tripayapp.util.StartupUtil;
import com.tripayapp.validation.CommonValidation;
import com.thirdparty.api.IBookBusTicketApi;
import com.thirdparty.model.request.BookBusTicketRequest;

public class BookBusTicketApi implements IBookBusTicketApi {

	private final BusDetailRepository busDetailRepository;
	private final SeatDetailRepository seatDetailRepository;
	private final TravelUserDetailRepository travelUserDetailRepository;
	private final BusTransactionRepository busTransactionRepository;
	private final PassengerDetailsRepository passengerDetailsRepository;
	private final ITransactionApi transactionApi;
	
	
	public  BookBusTicketApi(BusDetailRepository busDetailRepository,SeatDetailRepository seatDetailRepository,
			TravelUserDetailRepository travelUserDetailRepository,BusTransactionRepository busTransactionRepository,
			PassengerDetailsRepository passengerDetailsRepository, ITransactionApi transactionApi) {
	this.busDetailRepository = busDetailRepository;
	this.seatDetailRepository = seatDetailRepository;
	this.travelUserDetailRepository = travelUserDetailRepository;
	this.busTransactionRepository = busTransactionRepository;
	this.passengerDetailsRepository = passengerDetailsRepository;
    this.transactionApi = transactionApi;
	}
	
	@Override
	public void saveBusTicket(BookBusTicketRequest req, User user, PQService service) {
		
		TravelSeatDetail seatDetail = new TravelSeatDetail();
		seatDetail.setFare(req.getFare());
		// seatDetail.setLadiesSeat(req.getLadiesSeat());
		// seatDetail.setNetFare(req.getNetFare());
		seatDetail.setNoOfSeats(req.getNoOfSeats());
		seatDetail.setSeatNo(req.getSeatNo());
		seatDetail.setCancelPolicy(req.getCancelPolicy());
		seatDetailRepository.save(seatDetail);
		
		TravelUserDetail userDetail = new TravelUserDetail();
		userDetail.setAddress(req.getAddress());
		userDetail.setAge(req.getAge());
		userDetail.setEmailId(req.getEmailId());
		userDetail.setGender(req.getGender());
		userDetail.setMobileNo(req.getMobileNo());
		userDetail.setName(req.getName());
		userDetail.setPostalCode(req.getPostalCode());
		travelUserDetailRepository.save(userDetail);
		
		TravelBusDetail busDetail = new TravelBusDetail();
		busDetail.setActualFare(req.getFare());
		busDetail.setBlockRefNo(req.getBlockingRefNo());
		busDetail.setBookingDate(req.getBookingDate());
		busDetail.setBookingRefNo(req.getBookingRefNo());
		busDetail.setBusTypeName(req.getBusTypeName());
		busDetail.setDepartureTime(req.getDepartureTime());
		busDetail.setDestinationId(req.getDestinationId());
		busDetail.setDestinationName(req.getDestinationName());
		busDetail.setJourneyDate(req.getJourneyDate());
		busDetail.setProvider(req.getProvider());
		busDetail.setReturnDate(req.getReturnDate());
		busDetail.setSourceId(req.getSourceId());
		busDetail.setSourceName(req.getSourceName());
		busDetail.setTravelOperator(req.getTravelOperator());
		busDetail.setTripId(req.getTripId());
		busDetail.setSeatDetail(seatDetail);
		busDetail.setUserDetail(userDetail);
		busDetail.setServiceCharge(req.getServiceCharge());
		busDetail.setServiceTax(req.getServiceTax());
		busDetailRepository.save(busDetail);

		double amount=Double.parseDouble(req.getFare());
		String transactionRefNo=req.getBookingRefNo();
		
		System.err.println("amount  "+amount);
		System.err.println("USERNAME    "+user.getUsername());
		System.err.println("referenceNOOOO   "+transactionRefNo);
		
		transactionApi.initiateBusBooking(amount,
				"Bus Ticket Booked Rs " + req.getFare() + " from " + req.getSourceName() + " to "
						+ req.getDestinationName() + " to RefNo. " + req.getBookingRefNo(),
						service, transactionRefNo,user.getUsername(), StartupUtil.TRAVEL,  "");
		
		PQTransaction transaction = transactionApi.getTransactionByRefNo(transactionRefNo + "D");
		System.err.println("Trasaction :: " + transaction);
		PQTransaction PqTransaction = transactionApi.getTransactionByRefNo(transactionRefNo + "D");
		System.err.println("Trasaction :: " + transaction);
		if (transaction != null) {
			String busTransactionRefNo = req.getTransactionRefNo();
			TravelBusTransaction busTransaction = new TravelBusTransaction();
			busTransaction.setAmount(req.getFare());
			busTransaction.setBookingRefNo(req.getBookingRefNo());
			busTransaction.setBusTransactionRefNo(busTransactionRefNo);
			busTransaction.setStatus(Status.Initiated);
			busTransaction.setTransaction(transaction);
			busTransaction.setDescription(transaction.getDescription());
			busTransaction.setBlockRefNo(req.getBlockingRefNo());
			busTransaction.setBookingDate(req.getBookingDate());
			busTransaction.setBusDetail(busDetail);
			busTransactionRepository.save(busTransaction);
		}
	}

	@Override
	public void bookBusTicket(BookBusTicketRequest req, User user) {
		System.err.println("----------------------");

		PQTransaction transaction = transactionApi.getTransactionByRefNo(req.getTransactionRefNo() + "D");
		System.err.println("Trasaction :: " + transaction);
		if (transaction != null) {
			System.err.println("TRANSation failure " + transaction.getTransactionRefNo());
			transactionApi.successTravelBus(req.getTransactionRefNo());
		}

		TravelBusTransaction busTransaction = busTransactionRepository.findByTransactionRefNo(req.getTransactionRefNo());
		if (busTransaction != null) {
			TravelBusDetail busDetail = busTransaction.getBusDetail();
			busDetailRepository.updateTravelBusDetailByBusId(req.getApiRefNo(), req.getBlockId(), req.getBookingDate(),
					req.getClientId(), busDetail.getId());
			busTransaction.setStatus(Status.Booked);

			busTransactionRepository.save(busTransaction);

		}
	}

	@Override
	public void failBookBusTicket(BookBusTicketRequest req) {
		PQTransaction transaction = transactionApi.getTransactionByRefNo(req.getTransactionRefNo() + "D");
		System.err.println("Trasaction :: " + transaction);
		if (transaction != null) {
			System.err.println("Transation failure " + transaction.getTransactionRefNo());
			transactionApi.failedBillPaymentNew(req.getTransactionRefNo());
		}


	}

	@Override
	public boolean checkBalance(User user, BookBusTicketRequest req) {
		boolean valid = false;
		double amount = Double.parseDouble(req.getFare());
		if (CommonValidation.balanceCheck(user.getAccountDetail().getBalance(), amount)) {
			valid = true;
		}
		return valid;
	}

	/*@Override
	public ResponseDTO saveBusDetails(BookBusTicketRequest request) throws ClientException {
		
		BusDetails busDetails = new BusDetails();
		BusPassengerTrip busPassengerTrip = new BusPassengerTrip();
		
		busDetails.setBoardingId(request.getBoardingId());
		busDetails.setBoardingAddress(request.getBoardingAddress());
		busDetails.setSource(request.getSourceName());
		busDetails.setDestination(request.getDestinationName());
		busDetails.setBusOperator(request.getTravelOperator());
	//	busDetails.setBusType(BusType.valueOf(request.getBusTypeName()));
		busDetails.setCreated(new Date());
		
		PassengerDetails passengerDetails = new PassengerDetails();
		passengerDetails.setTitle(Salutation.valueOf(request.getSalutation()));
		passengerDetails.setName(request.getName());
		passengerDetails.setAge(Long.parseLong(request.getAge()));
		passengerDetails.setGender(Gender.valueOf(request.getGender()));
		passengerDetails.setMobile(request.getMobileNo());
		passengerDetails.setAlternateMobile(request.getEmergencyMobileNo());
		passengerDetails.setEmail(request.getEmailId());
		passengerDetails.setCreated(new Date());
		
		PQTransaction transaction = transactionApi.getTransactionByRefNo(request.getTransactionRefNo() + "D");
		System.err.println("Trasaction :: " + transaction);
		if (transaction != null) {
			String busTransactionRefNo = request.getTransactionRefNo();
			TravelBusTransaction busTransaction = new TravelBusTransaction();
			busTransaction.setAmount(request.getFare());
			busTransaction.setBookingRefNo(request.getBookingRefNo());
			busTransaction.setBusTransactionRefNo(busTransactionRefNo);
			busTransaction.setStatus(Status.Success);
			busTransaction.setTransaction(transaction);
			busTransaction.setDescription(transaction.getDescription());
			busTransaction.setBlockRefNo(request.getBlockingRefNo());
	//		busTransaction.setBookingDate(request.getBookingDate());
	//		busTransaction.setBusDetail(busDetails);
			
			busTransactionRepository.save(busTransaction);
		}
		
		BusTripDetails tripDetails = new BusTripDetails();
		tripDetails.setBlockingRefNo(request.getBlockingRefNo());
		tripDetails.setBookingRefNo(request.getBookingRefNo());
		tripDetails.setBookingStatus(Status.valueOf(request.getBookingStatus()));
		
		
		
		
		busDetailRepository.save(busDetails);
		passengerDetailsRepository.save(passengerDetails);
		
		
		return null;
	}
*/

}

