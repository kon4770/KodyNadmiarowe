package codeWrappers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TwoDimParityCheck implements WrapperInterface {

    private boolean hasChanged = false;
    private StringBuilder[] bitArray;

    private int chunkSize = 0;
    private int parallelRowNumber = 0;

    private Set<Integer> chunkToResendSet = new HashSet<>();
    private Map<Integer, Integer> chunkMap = new HashMap<>();


    public TwoDimParityCheck(StringBuilder[] bitArray, int parallelRowNumber) {
        this.bitArray = bitArray;
        this.parallelRowNumber = parallelRowNumber;
    }

    @Override
    public boolean hasChanged() {
        return false;
    }

    @Override
    public void encode() {
        SimpleParityCheck simpleParityCheck = new SimpleParityCheck(bitArray);
        simpleParityCheck.encode();
        bitArray = simpleParityCheck.bitArray;

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
    }
//
//    private Set<Integer> normalizeSet(int rowNum){
//        for(int i=0;i<s)
//    }

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

    public StringBuilder[] getBitArrayCopy() {
        StringBuilder[] newArray = new StringBuilder[bitArray.length];
        for (int i = 0; i < bitArray.length; i++) {
            newArray[i] = new StringBuilder(bitArray[i]);
        }
        return newArray;
    }


    @Override
    public void decode(StringBuilder[] bitArray) {
        SimpleParityCheck simpleParityCheck = new SimpleParityCheck(bitArray);
        simpleParityCheck.decode(bitArray);
        bitArray = simpleParityCheck.bitArray;
        //chunkToResendSet = simpleParityCheck.getChunkToResendSet();//Nie bezposrednia konwersja
//        chunkToResendSet.removeAll(parityChunks);
        int chunkSize = bitArray[0].length();
        StringBuilder newChunk = new StringBuilder();
        StringBuilder verticalChunk = new StringBuilder();
        for (int startingBitChunkIndex = 0; startingBitChunkIndex < bitArray.length; startingBitChunkIndex+= parallelRowNumber) {
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
            bitArray = removeX(bitArray.length,bitArray,index);
            newChunk = new StringBuilder();
        }
    }

    @Override
    public Set<Integer> getChunkToResendSet() {
        return chunkToResendSet;
    }
}
