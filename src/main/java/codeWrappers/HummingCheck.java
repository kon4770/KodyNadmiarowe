package codeWrappers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HummingCheck implements WrapperInterface {

    private Set<Integer> chunkToResendList = new HashSet<>();
    private Set<Integer> parityBitLocations = new HashSet<>();

    @Override
    public StringBuilder[] encode(StringBuilder[] bitArray) {
        Set<Integer> onePositions;
        Set<Integer> parityBitLocations = getParityBitIndexes(bitArray[0].length());
        for (int i = 0; i < bitArray.length; i++) {
            char[] chunk = new char[bitArray[i].length() + parityBitLocations.size() + 1];
            onePositions = new HashSet<>();
            int j = 0;
            chunk[0] = '0';
            for (int bitIndex = 1; bitIndex < chunk.length; bitIndex++) {
                if (parityBitLocations.contains(bitIndex)) {
                    chunk[bitIndex] = '0';
                } else {
                    if (bitArray[i].charAt(j) == '1') {
                        onePositions.add(bitIndex);
                    }
                    chunk[bitIndex] = bitArray[i].charAt(j);
                    j++;
                }
            }
            int xorOfSet = xorOfArray(onePositions);
            Set<Integer> bitsToFlip = getBitsToFlip(xorOfSet);
            for (int index : bitsToFlip) {
                chunk[index] = '1';
            }
            if ((onePositions.size() + bitsToFlip.size()) % 2 == 1) {
                chunk[0] = '1';
            }
            bitArray[i] = new StringBuilder(String.valueOf(chunk));
        }
        this.parityBitLocations = parityBitLocations;
        return bitArray;
    }

    @Override
    public StringBuilder[] decode(StringBuilder[] bitArray) {
        Set<Integer> onePositions;
        Map<Integer, Integer> normalizedChunkIndexes = new HashMap<>();
        int trueIndex = 0;
        for (int i = 0; i < bitArray[0].length(); i++) {
            if (parityBitLocations.contains(i)) {
                continue;
            }
            normalizedChunkIndexes.put(i, trueIndex);
            trueIndex++;
        }
        for (int i = 0; i < bitArray.length; i++) {
            char[] chunk = new char[bitArray[i].length() - parityBitLocations.size() - 1];
            onePositions = new HashSet<>();
            int j = 0;
            for (int bitIndex = 1; bitIndex < bitArray[i].length(); bitIndex++) {
                if (bitArray[i].charAt(bitIndex) == '1') {
                    onePositions.add(bitIndex);
                }
                if (parityBitLocations.contains(bitIndex)) {
                    continue;
                }
                chunk[j] = bitArray[i].charAt(bitIndex);
                j++;
            }
            int xorOfSet = xorOfArray(onePositions);
            Set<Integer> falseBitsToFlip = getBitsToFlip(xorOfSet);
            for (int falseIndex : falseBitsToFlip) {
                Integer index = normalizedChunkIndexes.get(falseIndex);
                if (index == null) {
                    if (chunk[falseIndex] == '1') {
                        onePositions.remove(falseIndex);
                    } else {
                        chunk[falseIndex] = '1';
                        onePositions.add(falseIndex);
                    }
                } else {
                    if (chunk[index] == '1') {
                        chunk[index] = '0';
                        onePositions.remove(index);
                    } else {
                        chunk[index] = '1';
                        onePositions.add(index);
                    }
                }
            }
            if (onePositions.size() % 2 == bitArray[i].charAt(0)) {
                chunkToResendList.add(i);
            }
            bitArray[i] = new StringBuilder(String.valueOf(chunk));
        }
        return bitArray;
    }

    @Override
    public Set<Integer> getChunkToResendSet() {
        return chunkToResendList;
    }

    private Set<Integer> getParityBitIndexes(int dataBitCount) {
        Set<Integer> result = new HashSet<>();
        int j = 0;
        int ind = 0;
        int parityCount = 0;
        while (ind < dataBitCount) {
            if (Math.pow(2, parityCount) == ind + parityCount + 1) {
                parityCount++;
            } else {
                ind++;
            }
        }
        for (int i = 1; i <= dataBitCount + parityCount; i++) {
            if (Math.pow(2, j) == i) {
                result.add(i);
                j++;
            }
        }
        return result;
    }

    private Set<Integer> getBitsToFlip(int value) {
        Set<Integer> result = new HashSet<>();
        String bitString = Integer.toBinaryString(value);
        for (int i = 0; i < bitString.length(); i++) {
            if (bitString.charAt(i) == '1') {
                result.add(Integer.parseInt(bitString.charAt(i) + "0".repeat(bitString.length() - i - 1), 2));
            }
        }
        return result;
    }

    private int xorOfArray(Set<Integer> set) {
        int xor_arr = 0;
        for (Integer value : set) {
            xor_arr = xor_arr ^ value;
        }
        return xor_arr;
    }
}
