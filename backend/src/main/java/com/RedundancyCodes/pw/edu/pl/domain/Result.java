package com.RedundancyCodes.pw.edu.pl.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    private int tp;

    private int tn;

    private int fp;

    private int fn;

}
