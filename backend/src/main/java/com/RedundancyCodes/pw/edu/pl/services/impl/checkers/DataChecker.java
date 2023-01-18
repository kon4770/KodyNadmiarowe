package com.RedundancyCodes.pw.edu.pl.services.impl.checkers;

import java.util.HashSet;
import java.util.Set;

public class DataChecker {
    private Set<Integer> tpBrokenChunks = new HashSet<>();
    private Set<Integer> fpBrokenChunks = new HashSet<>();
    private Set<Integer> fnBrokenChunks = new HashSet<>();

    private Set<Integer> tnChunks = new HashSet<>();

    private int setSumSize = 0;

    public boolean checkWithOriginal(Set<Integer> sus, Set<Integer> tp) {
        Set<Integer> all = new HashSet<>();
        all.addAll(sus);
        all.addAll(tp);
        setSumSize = all.size();
        for (int index : all) {
            if (sus.contains(index) && tp.contains(index)) {
                tpBrokenChunks.add(index);
            } else if (sus.contains(index)) {
                fpBrokenChunks.add(index);
            } else {
                fnBrokenChunks.add(index);
            }
        }
        return fpBrokenChunks.isEmpty() && fnBrokenChunks.isEmpty();
    }

    public Set<Integer> getTPBrokenChunks() {
        return tpBrokenChunks;
    }

    public Set<Integer> getFPBrokenChunks() {
        return fpBrokenChunks;
    }

    public Set<Integer> getFNBrokenChunks() {
        return fnBrokenChunks;
    }

    public Set<Integer> getTnChunks() {
        return tnChunks;
    }

    public int getSetSumSize() {
        return setSumSize;
    }
}