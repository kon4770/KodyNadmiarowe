package com.RedundancyCodes.pw.edu.pl.services.impl.wrappers;

import com.RedundancyCodes.pw.edu.pl.services.WrapperInterface;

import java.util.HashSet;
import java.util.Set;

public class SimpleParityCheck implements WrapperInterface {
    private Set<Integer> chunkToResendList = new HashSet<>();

    @Override
    public StringBuilder[] encode(StringBuilder[] bitArray) {
        int chunkN = bitArray.length;
        for (int i = 0; i < chunkN; i++) {
            bitArray[i].append(getParityBit(bitArray[i]));
        }
        return bitArray;
    }

    @Override
    public StringBuilder[] decode(StringBuilder[] bitArray) {
        int chunkSize = bitArray[0].length() - 1;
        int chunkN = bitArray.length;
        for (int i = 0; i < chunkN; i++) {
            StringBuilder current = bitArray[i];
            char suspectBit = getParityBit(current.substring(0,chunkSize));
            if (suspectBit != current.charAt(chunkSize)) {
                chunkToResendList.add(i);
            }
            current.setLength(chunkSize);
        }
        return bitArray;
    }

    @Override
    public Set<Integer> getChunkToResendSet() {
        return chunkToResendList;
    }

    public static char getParityBit(String array){
        boolean hasParity = true;
        for(int cIndex = 0;cIndex<array.length();cIndex++){
            if(array.charAt(cIndex)=='1'){
                hasParity = !hasParity;
            }
        }
        return hasParity?'0':'1';
    }

    public static char getParityBit(StringBuilder array){
        boolean hasParity = true;
        for(int cIndex = 0;cIndex<array.length();cIndex++){
            if(array.charAt(cIndex)=='1'){
                hasParity = !hasParity;
            }
        }
        return hasParity?'0':'1';
    }
}
