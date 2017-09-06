package com.tripayapp.validation;

import com.tripayapp.entity.PQService;
import com.tripayapp.model.VNetDTO;
import com.tripayapp.model.VNetError;
import com.tripayapp.repositories.PQServiceRepository;

public class VNetValidation {
    private final PQServiceRepository pqServiceRepository;

    public VNetValidation(PQServiceRepository pqServiceRepository) {
        this.pqServiceRepository = pqServiceRepository;
    }
    public VNetError validateRequest(VNetDTO dto){
        VNetError error = new VNetError();
        boolean valid = true;
        PQService service = pqServiceRepository.findServiceByCode(dto.getServiceCode());
        if (!CommonValidation.isAmountInMinMaxRange(service.getMinAmount(), service.getMaxAmount(),
                dto.getAmount())) {
            error.setAmount("Amount should be between Rs. " + service.getMinAmount() + " to Rs. "
                    + service.getMaxAmount());
            valid = false;
        }
        error.setValid(valid);
        return error;
    }
}
