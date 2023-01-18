package com.RedundancyCodes.pw.edu.pl.services.impl.wrappers;

import com.RedundancyCodes.pw.edu.pl.services.WrapperInterface;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class TwoDimParityCheck implements WrapperInterface {
    private int parallelRowNumber = 0;
    private Set<Integer> chunkToResendSet = new TreeSet<>();
    private int additionalChunkNumber = 0;

    public TwoDimParityCheck(int parallelRowNumber) {
        this.parallelRowNumber = parallelRowNumber;
    }

    @Override
    public StringBuilder[] encode(StringBuilder[] bitArray) {
        SimpleParityCheck simpleParityCheck = new SimpleParityCheck();
        StringBuilder[] firstStageEncoded = simpleParityCheck.encode(bitArray);
        this.additionalChunkNumber = (int)Math.ceil((float)firstStageEncoded.length / parallelRowNumber);
        int chunkSize = firstStageEncoded[0].length();
        int chunkNumber = firstStageEncoded.length;
        StringBuilder[] secondStageEncoded = new StringBuilder[additionalChunkNumber];
        int currentStartingRow = 0;
        for (int i = 0; i < additionalChunkNumber; i++) {
            secondStageEncoded[i] = new StringBuilder();
            for (int currBitIndex = 0; currBitIndex < chunkSize; currBitIndex++) {
                StringBuilder columnChunk = new StringBuilder();
                for (int parrRowIndex = 0; parrRowIndex < parallelRowNumber; parrRowIndex++) {
                    int currRow = currentStartingRow + parrRowIndex;
                    if (currRow >= chunkNumber) {
                        break;
                    }
                    columnChunk.append(firstStageEncoded[currRow].charAt(currBitIndex));
                }
                secondStageEncoded[i].append(SimpleParityCheck.getParityBit(columnChunk));
            }
            currentStartingRow += parallelRowNumber;
        }
        return arrayMerge(firstStageEncoded, secondStageEncoded);
    }

    private StringBuilder[] arrayMerge(StringBuilder[] a, StringBuilder[] b) {
        int a1 = a.length;

        int b1 = b.length;

        int c1 = a1 + b1;

        StringBuilder[] c = new StringBuilder[c1];

        System.arraycopy(a, 0, c, 0, a1);
        System.arraycopy(b, 0, c, a1, b1);
        return c;
    }

    private StringBuilder[] subArray(StringBuilder[] array, int end) {
        StringBuilder newArr[] = Arrays.copyOf(array, end);
        return newArr;
    }


    private boolean isEqual(StringBuilder a, StringBuilder b) {
        if (a.compareTo(b) == 0) {
            return true;
        }
        return false;
    }

    @Override
    public StringBuilder[] decode(StringBuilder[] bitArray) {
        int chunkSize = bitArray[0].length();
        int chunkNumber = bitArray.length - additionalChunkNumber;
        int currentStartingRow = 0;
        for (int i = 0; i < additionalChunkNumber; i++) {
            StringBuilder suspectChunk = new StringBuilder();
            for (int currBitIndex = 0; currBitIndex < chunkSize; currBitIndex++) {
                StringBuilder columnChunk = new StringBuilder();
                for (int parrRowIndex = 0; parrRowIndex < parallelRowNumber; parrRowIndex++) {
                    int currRow = currentStartingRow + parrRowIndex;
                    if (currRow >= chunkNumber) {
                        break;
                    }
                    columnChunk.append(bitArray[currRow].charAt(currBitIndex));
                }
                suspectChunk.append(SimpleParityCheck.getParityBit(columnChunk));
            }
            if (!isEqual(bitArray[chunkNumber + i], suspectChunk)) {
                for (int chunkId = 0; chunkId < parallelRowNumber; chunkId++) {
                    chunkToResendSet.add(currentStartingRow+chunkId);
                }
                chunkToResendSet.add(chunkNumber + i);
            }
            currentStartingRow += parallelRowNumber;
        }
        return subArray(bitArray, chunkNumber);
    }

    @Override
    public Set<Integer> getChunkToResendSet() {
        return chunkToResendSet;
    }
}
