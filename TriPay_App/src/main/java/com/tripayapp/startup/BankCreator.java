package com.tripayapp.startup;


import com.tripayapp.entity.BankDetails;
import com.tripayapp.entity.Banks;
import com.tripayapp.entity.TelcoCircle;
import com.tripayapp.repositories.BankDetailRepository;
import com.tripayapp.repositories.BanksRepository;
import com.tripayapp.util.StartupUtil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BankCreator {


    private final BanksRepository banksRepository;
    private final BankDetailRepository bankDetailRepository;

    public BankCreator(BanksRepository banksRepository, BankDetailRepository bankDetailRepository) {
        this.banksRepository = banksRepository;
        this.bankDetailRepository = bankDetailRepository;
    }
    public void create(){
            String bankFilePath = StartupUtil.CSV_FILE+"banks.csv";
            String ifscOnePath  = StartupUtil.CSV_FILE+"ifsc_one.csv";
            String ifscTwoPath = StartupUtil.CSV_FILE+"ifsc_two.csv";
        ArrayList<Banks> banksArrayList = readBanksFromFile(bankFilePath);
        System.err.print(banksArrayList);
           if(banksArrayList != null) {
               for (Banks b : banksArrayList) {
                    Banks exists = banksRepository.findByCode(b.getCode());
                    if(exists == null){
                        banksRepository.save(b);
                    }
               }
           }
        ArrayList<BankDetails> ifscOne = readIFSCFromFile(ifscOnePath);
        System.err.print(ifscOne);
        saveIFSCByList(ifscOne);
        ArrayList<BankDetails> ifscTwo = readIFSCFromFile(ifscTwoPath);
        System.err.print(ifscTwo);
        saveIFSCByList(ifscTwo);


    }

    public void saveIFSCByList(ArrayList<BankDetails> bankDetailsArrayList){

        if(bankDetailsArrayList != null) {
            for (BankDetails b : bankDetailsArrayList) {
                BankDetails exists = bankDetailRepository.findByIfscCode(b.getIfscCode(),b.getBank());
                if(exists == null){
                    bankDetailRepository.save(b);
                }
            }
        }
    }

    public ArrayList<Banks> readBanksFromFile(String fileName) {
        ArrayList<Banks> banks = new ArrayList<>();

        BufferedReader br = null;
        String line = "";
        try {

            br = new BufferedReader(new FileReader(fileName));
            Banks b = null;
            while ((line = br.readLine()) != null) {
                Pattern p = Pattern.compile("(([^\"][^,]*)|\"([^\"]*)\"),?");
                Matcher m = p.matcher(line);

                b = new Banks();
                String value = null;
                int index = 1;
                while (m.find()) {
                    if (m.group(2) != null) {
                        value = m.group(2);
//                        System.err.print(value+"2");
                    }

                    if (m.group(3) != null) {
                        value = m.group(3);
//                        System.err.print(value+"3");
                    }


                    if (value != null) {
                        if (b != null) {
                            switch (index) {
                                case 1:
//                                    System.err.println("case 1 ::"+value);
                                    b.setName(value);
                                    break;
                                case 2:
//                                    System.err.println("case 2 ::"+value);
                                    b.setCode(value);
                                    break;
                                default:
                                    break;
                            }
                            index = index + 1;
                        }
                    }
                        banks.add(b);

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return banks;
    }


    public ArrayList<BankDetails> readIFSCFromFile(String fileOne) {
        ArrayList<BankDetails> bankDetails = new ArrayList<>();

        BufferedReader br = null;
        String line = "";
        try {

            br = new BufferedReader(new FileReader(fileOne));
            BankDetails b = null;
            while ((line = br.readLine()) != null) {
                Pattern p = Pattern.compile("(([^\"][^,]*)|\"([^\"]*)\"),?");
                Matcher m = p.matcher(line);

                b = new BankDetails();
                String value = null;
                int index = 1;
                while (m.find()) {
                    if (m.group(2) != null) {
                        value = m.group(2);
                    }

                    if (m.group(3) != null) {
                        value = m.group(3);
                    }


                    if (value != null) {
                        if (b != null) {
                            switch (index) {
                                case 1:
//                                    System.err.println("case 1 ::"+value);
                                    Banks temp = banksRepository.findByCode(value);
                                    b.setBank(temp);
                                    break;
                                case 2:
//                                    System.err.println("case 2 ::"+value);
                                    b.setIfscCode(value);
                                    break;
                                default:
                                    break;
                            }
                            index = index + 1;
                        }
                    }
                    bankDetails.add(b);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bankDetails;
    }

}
