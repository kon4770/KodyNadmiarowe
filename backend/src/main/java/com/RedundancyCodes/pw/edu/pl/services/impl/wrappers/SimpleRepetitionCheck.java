package com.RedundancyCodes.pw.edu.pl.services.impl.wrappers;

import com.RedundancyCodes.pw.edu.pl.services.WrapperInterface;

import java.util.HashSet;
import java.util.Set;

public class SimpleRepetitionCheck implements WrapperInterface {

    private Set<Integer> chunkToResendSet = new HashSet<>();

    @Override
    public StringBuilder[] encode(StringBuilder[] bitArray) {
        for (int i = 0; i < bitArray.length; i++) {
            StringBuilder encodedString = new StringBuilder();
            for (int charId = 0; charId < bitArray[i].length(); charId++) {
                char c = bitArray[i].charAt(charId);
                encodedString.append(c).append(c).append(c);
            }
            bitArray[i] = encodedString;
        }
        return bitArray;
    }

    @Override
    public StringBuilder[] decode(StringBuilder[] bitArray) {
        for (int i = 0; i < bitArray.length; i++) {
            StringBuilder decodedString = new StringBuilder();
            int charId = 0;
            int zeros = 0;
            int ones = 0;
            while (charId < bitArray[i].length()) {
                if (bitArray[i].charAt(charId) == '0') {
                    zeros++;
                } else {
                    ones++;
                }
                if ((charId + 1) % 3 == 0) {
                    if (zeros == 0 || ones == 0) {
                        if (zeros > ones) {
                            decodedString.append('0');
                        } else {
                            decodedString.append('1');
                        }
                    } else {
                        chunkToResendSet.add(i);
                        if (zeros > ones) {
                            decodedString.append('0');
                        } else {
                            decodedString.append('1');
                        }
                    }
                    zeros = 0;
                    ones = 0;
                }
                charId++;
            }
            bitArray[i] = decodedString;
        }
        return bitArray;
    }

    @Override
    public Set<Integer> getChunkToResendSet() {
        return chunkToResendSet;
    }
}
