package codeWrappers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class TwoDimParityCheck implements WrapperInterface {
    private int parallelRowNumber = 0;
    private Set<Integer> chunkToResendSet = new TreeSet<>();



    public TwoDimParityCheck(int parallelRowNumber) {
        this.parallelRowNumber = parallelRowNumber;
    }

    @Override
    public StringBuilder[] encode(StringBuilder[] bitArray) {
        SimpleParityCheck simpleParityCheck = new SimpleParityCheck();
        bitArray =  simpleParityCheck.encode(bitArray);
        int chunkSize = bitArray[0].length();
        StringBuilder newChunk = new StringBuilder();
        StringBuilder verticalChunk = new StringBuilder();
        for (int startingBitChunkIndex = 0; startingBitChunkIndex < bitArray.length; startingBitChunkIndex++) {
            for (int bitIndex = 0; bitIndex < chunkSize; bitIndex++) {
                for (int bitChunkIndex = 0; bitChunkIndex < parallelRowNumber; bitChunkIndex++) {
                    int currentRowBit = startingBitChunkIndex + bitChunkIndex;
                    if (currentRowBit >= bitArray.length) {
                        continue;
                    }
                    verticalChunk.append(bitArray[currentRowBit].charAt(bitIndex));
                }
                newChunk.append(SimpleParityCheck.getParityBit(verticalChunk));
                verticalChunk = new StringBuilder();
            }
//            parityChunks.add(Math.min(startingBitChunkIndex + parallelRowNumber, bitArray.length+1));
            bitArray = insertX(bitArray.length, bitArray, newChunk, Math.min(startingBitChunkIndex + parallelRowNumber, bitArray.length + 1));
            newChunk = new StringBuilder();
            startingBitChunkIndex += parallelRowNumber;
        }
        return bitArray;
    }

    private StringBuilder[] insertX(int n, StringBuilder arr[], StringBuilder x, int pos) {
        int i;
        StringBuilder[] newArr = new StringBuilder[n + 1];
        for (i = 0; i < n + 1; i++) {
            if (i < pos - 1)
                newArr[i] = arr[i];

            else if (i == pos - 1)
                newArr[i] = x;
            else
                newArr[i] = arr[i - 1];
        }
        return newArr;
    }

    private StringBuilder[] removeX(int n, StringBuilder arr[], int posToRem) {
        int i;
        StringBuilder[] newArr = new StringBuilder[n - 1];
        for (i = 0; i < n - 1; i++) {
            if (i < posToRem)
                newArr[i] = arr[i];
            else
                newArr[i] = arr[i + 1];
        }
        return newArr;
    }

    private void normalizeChunkToResentSet(int idRemoved) {
        chunkToResendSet.forEach(n -> {if(n>idRemoved){
            chunkToResendSet.remove(n);
            chunkToResendSet.add(n-1);
        }
        });
    }

    @Override
    public StringBuilder[] decode(StringBuilder[] bitArray) {
        SimpleParityCheck simpleParityCheck = new SimpleParityCheck();
        bitArray = simpleParityCheck.decode(bitArray);
        int chunkSize = bitArray[0].length();
        StringBuilder newChunk = new StringBuilder();
        StringBuilder verticalChunk = new StringBuilder();
        for (int startingBitChunkIndex = 0; startingBitChunkIndex < bitArray.length; startingBitChunkIndex += parallelRowNumber) {
            for (int bitIndex = 0; bitIndex < chunkSize; bitIndex++) {
                for (int bitChunkIndex = 0; bitChunkIndex < parallelRowNumber; bitChunkIndex++) {
                    int currentRowBit = startingBitChunkIndex + bitChunkIndex;
                    if (currentRowBit >= bitArray.length) {
                        continue;
                    }
                    verticalChunk.append(bitArray[currentRowBit].charAt(bitIndex));
                }
                newChunk.append(SimpleParityCheck.getParityBit(verticalChunk));
                verticalChunk = new StringBuilder();
            }
            int index = startingBitChunkIndex + parallelRowNumber;
            if (newChunk.compareTo(bitArray[index]) != 0) {
                for (int i = startingBitChunkIndex; i < parallelRowNumber; i++) {
                    chunkToResendSet.add(i);
                }
            }
            bitArray = removeX(bitArray.length, bitArray, index);
            normalizeChunkToResentSet(index);
            newChunk = new StringBuilder();
        }
        return bitArray;
    }

    @Override
    public Set<Integer> getChunkToResendSet() {
        return chunkToResendSet;
    }
}
