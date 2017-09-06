package com.tripayapp.util;

import com.tripayapp.model.MTransactionResponseDTO;

import java.util.Comparator;

public class ComparatorUtil implements Comparator{
    @Override
    public int compare(Object o1, Object o2) {
        MTransactionResponseDTO transactionOne = (MTransactionResponseDTO) o1;
        MTransactionResponseDTO transactionTwo = (MTransactionResponseDTO) o2;
        long idOne = transactionOne.getId();
        long idTwo = transactionTwo.getId();
        if(idOne == idTwo)
            return 0;
        else if(idOne < idTwo)
            return 1;
        else
            return -1;
    }
}
