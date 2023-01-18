package com.RedundancyCodes.pw.edu.pl.services;

import java.util.Set;

public interface WrapperInterface {
    public StringBuilder[] encode(StringBuilder[] bitArray);

    public StringBuilder[] decode(StringBuilder[] bitArray);

    public Set<Integer> getChunkToResendSet();
}
