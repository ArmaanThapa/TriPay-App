package com.tripayapp.startup;


import com.tripayapp.model.Status;
import com.tripayapp.repositories.PQVersionRepository;
import com.tripayapp.entity.PQVersion;

import java.util.List;

public class VersionCreator {

    private final PQVersionRepository pqVersionRepository;
    private int lastVersion = 10 ;
    public VersionCreator(PQVersionRepository pqVersionRepository){
        this.pqVersionRepository= pqVersionRepository;
    }
    public void create(){

    }
    public void createOne() {
        System.err.println("inside create method");
        for (int i = 1; i <= lastVersion; i++) {
        for (int j = 0; j < 10; j++) {
            PQVersion version = pqVersionRepository.findByVersionNo(i, j);
            if (version == null) {
                version = new PQVersion();
                version.setVersionCode(i);
                version.setSubversionCode(j);
                version.setStatus(Status.Active);
                pqVersionRepository.save(version);
            }
        }
    }


    }

}
