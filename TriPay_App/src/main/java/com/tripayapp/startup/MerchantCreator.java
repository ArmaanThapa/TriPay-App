package com.tripayapp.startup;

import com.instantpay.util.InstantPayConstants;
import com.tripayapp.entity.PGDetails;
import com.tripayapp.entity.PQService;
import com.tripayapp.entity.User;
import com.tripayapp.repositories.*;
import com.tripayapp.util.SecurityUtil;
import com.tripayapp.util.StartupUtil;

import java.util.ArrayList;
import java.util.List;

public class MerchantCreator {

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final PGDetailsRepository pgDetailsRepository;
    private final PQServiceRepository pqServiceRepository;
    private final PQCommissionRepository pqCommissionRepository;

    public MerchantCreator(UserRepository userRepository, UserDetailRepository userDetailRepository, PGDetailsRepository pgDetailsRepository, PQServiceRepository pqServiceRepository, PQCommissionRepository pqCommissionRepository) {
        this.userRepository = userRepository;
        this.userDetailRepository = userDetailRepository;
        this.pgDetailsRepository = pgDetailsRepository;
        this.pqServiceRepository = pqServiceRepository;
        this.pqCommissionRepository = pqCommissionRepository;
    }

    public void create(){



    }

}
