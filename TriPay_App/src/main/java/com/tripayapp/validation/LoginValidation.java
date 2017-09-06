package com.tripayapp.validation;

import com.tripayapp.model.LoginDTO;
import com.tripayapp.model.error.LoginError;

public class LoginValidation {

    public LoginError checkLoginValidation(LoginDTO login){
        LoginError loginError = new LoginError();
        loginError.setSuccess(true);
        if(CommonValidation.isNull(login.getUsername())){
            loginError.setSuccess(false);
            loginError.setMessage("Enter Username");
        }else if(CommonValidation.isNull(login.getPassword())){
            loginError.setSuccess(false);
            loginError.setMessage("Enter Password");
        }else if(CommonValidation.isNull(login.getIpAddress())){
            loginError.setMessage("Not a valid device");
            loginError.setSuccess(false);
        }
        return loginError;
    }
}
