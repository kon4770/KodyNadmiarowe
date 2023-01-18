package com.RedundancyCodes.pw.edu.pl.domain;

import com.RedundancyCodes.pw.edu.pl.services.WrapperInterface;
import com.RedundancyCodes.pw.edu.pl.services.impl.wrappers.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RedundancyMethodProvider {

    private List<Integer> parameters;

    private String method;

    private WrapperInterface wrapper;

    public WrapperInterface getRedundancyMethod() {
        if (wrapper != null) {
            return wrapper;
        }
        if (method.compareTo("HummingCheck") == 0) {
            wrapper = new HummingCheck();
        }
        if (method.compareTo("CyclicRedundancyCheck") == 0) {
            wrapper = new CyclicRedundancyCheck(parameters.get(1));
        }
        if (method.compareTo("ISBNCheck") == 0) {
            wrapper = new ISBNCheck();
        }
        if (method.compareTo("SimpleParityCheck") == 0) {
            wrapper = new SimpleParityCheck();
        }
        if (method.compareTo("TwoDimParityCheck") == 0) {
            wrapper = new TwoDimParityCheck(parameters.get(0));
        }
        if (method.compareTo("SimpleRepetitionCheck") == 0) {
            wrapper = new SimpleRepetitionCheck();
        }
        return wrapper;
    }
}
