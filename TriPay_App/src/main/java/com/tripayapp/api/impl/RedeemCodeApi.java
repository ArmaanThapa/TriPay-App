package com.tripayapp.api.impl;

import com.tripayapp.api.IRedeemCodeApi;
import com.tripayapp.entity.User;

public class RedeemCodeApi implements IRedeemCodeApi {

	public RedeemCodeApi() {
		// TODO Auto-generated constructor stub
	}
//	private final RedeemCodeRepository redeemCodeRepostory;

//	public RedeemCodeApi(RedeemCodeRepository redeemCodeRepostory) {
//		this.redeemCodeRepostory = redeemCodeRepostory;
//
//	}

	@Override
	public boolean codeUsered(User user) {
//		List<RedeemCode> codes = (List<RedeemCode>) redeemCodeRepostory.findAll();
//
//		for (RedeemCode redeemCode : codes) {
//			if (redeemCode.getUser().getId().equals(user.getId()))
//				return false;
//		}
		return true;
	}

}
