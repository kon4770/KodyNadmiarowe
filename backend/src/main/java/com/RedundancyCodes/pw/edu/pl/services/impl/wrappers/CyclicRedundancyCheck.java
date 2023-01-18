package com.RedundancyCodes.pw.edu.pl.services.impl.wrappers;

import com.RedundancyCodes.pw.edu.pl.services.WrapperInterface;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CyclicRedundancyCheck implements WrapperInterface {
    private String divisor = "";
    private Set<Integer> chunkToResendList = new HashSet<>();

    public CyclicRedundancyCheck(int divisorIndex) {
        Map<Integer, Integer> dividerMap = new HashMap<>();
        dividerMap.putAll(Map.of(3, 0x5, 4, 0x9, 5, 0x12, 6, 0x23, 7, 0x44, 8, 0xD3, 10, 0x3EC, 11, 0x5C2, 12, 0xC07, 13, 0x1E7A));
        dividerMap.putAll(Map.of(14, 0x3016, 15, 0x740A, 16, 0x8810, 17, 0x1B42D, 21, 0x18144C, 24, 0xAEB6E5, 30, 0x30185CE3, 32, 0x82608EDB));
        this.divisor = String.format("%32s", Integer.toBinaryString(dividerMap.get(divisorIndex))).strip().concat("1");
    }


    public StringBuilder[] encode(StringBuilder[] bitArray) {
        StringBuilder rem;
        StringBuilder divisor = new StringBuilder(this.divisor);
        for (int i = 0; i < bitArray.length; i++) {
            rem = divideDataWithDivisor(bitArray[i], divisor);
            bitArray[i].append(rem.substring(0, rem.length() - 1));
        }
        return bitArray;
    }

    public StringBuilder[] decode(StringBuilder[] bitArray) {
        StringBuilder divisor = new StringBuilder(this.divisor);
        for (int i = 0; i < bitArray.length; i++) {
            if (!isDataCorrect(bitArray[i], divisor)) {
                chunkToResendList.add(i);
            }
        }
        return bitArray;
    }

    @Override
    public Set<Integer> getChunkToResendSet() {
        return chunkToResendList;
    }

    private StringBuilder divideDataWithDivisor(StringBuilder oldData, StringBuilder divisor) {

        StringBuilder data = new StringBuilder("0".repeat(oldData.length() + divisor.length()));
        StringBuilder rem = new StringBuilder("0".repeat(divisor.length()));

        data.replace(0, oldData.length(), oldData.toString());
        rem.replace(0, divisor.length(), data.substring(0, divisor.length()));

        for (int i = 0; i < oldData.length(); i++) {
            if (rem.charAt(0) == '1') {
                for (int j = 1; j < divisor.length(); j++) {
                    rem.setCharAt(j - 1, exorOperation(rem.charAt(j), divisor.charAt(j)));
                }
            } else {
                for (int j = 1; j < divisor.length(); j++) {
                    rem.setCharAt(j - 1, exorOperation(rem.charAt(j), '0'));
                }
            }
            try {
                rem.setCharAt(divisor.length() - 1,data.charAt(i + divisor.length()));
            } catch (Exception e) {
                System.out.println("CRC issue");
            }
        }
        return rem;
    }

    private char exorOperation(char x, char y) {
        if (x == y) {
            return '0';
        }
        return '1';
    }

    private boolean isDataCorrect(StringBuilder data, StringBuilder divisor) {
        StringBuilder rem = divideDataWithDivisor(data, divisor);
        for (int i = 0; i < rem.length(); i++) {
            if (rem.charAt(i) != '0') {
                return false;
            }
        }
        return true;
    }
}  