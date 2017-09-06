package com.tripayapp.util;

import com.tripayapp.model.TransactionListDTO;

import java.util.Comparator;

/**
 * Created by vibhanshu on 23/11/16.
 */
public class TransactionComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        TransactionListDTO dtoOne = (TransactionListDTO) o1;
        TransactionListDTO dtoTwo = (TransactionListDTO) o2;
        long idOne = dtoOne.getId();
        long idTwo = dtoTwo.getId();
        if(idOne == idTwo)
            return 0;
        else if(idOne < idTwo)
            return 1;
        else
            return -1;

    }
}
