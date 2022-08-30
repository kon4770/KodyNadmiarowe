package codeAlters;

import java.util.Arrays;

public class BufferAndChunkSetter {
    private int singleChunkBufferSize;
    private int chunkSize;
    private int chunkNumber;
    private int finalChunkSize;
    private int originalLength;

    public BufferAndChunkSetter(int chunkSize, int singleChunkBufferSize) {
        this.singleChunkBufferSize = singleChunkBufferSize;
        this.chunkSize = chunkSize;
    }

    public StringBuilder[] divideString(String bitString) {
        int length = bitString.length();
        originalLength = length;
        int nWholePieces = length / chunkSize;
        String buffer = new String("0");
        buffer = buffer.repeat(singleChunkBufferSize);
        int rest = length % chunkSize;
        if (rest != 0) {
            String endFiller = new String("0");
            endFiller = endFiller.repeat(chunkSize - rest);
            bitString = bitString.concat(endFiller);
            nWholePieces++;
        }
        chunkNumber = nWholePieces;
        finalChunkSize = chunkSize + singleChunkBufferSize;
        StringBuilder[] resultArray = new StringBuilder[nWholePieces];
        int start = 0;
        int end = chunkSize;
        for (int i = 0; i < nWholePieces; i++) {
            resultArray[i] = new StringBuilder(bitString.substring(start, end)).append(buffer);
            start += chunkSize;
            end += chunkSize;
        }
        return resultArray;
    }

    public String extractEsenseAndMergeString(StringBuilder[] bitArray) {
        StringBuilder resultArray = new StringBuilder();
        for (int i = 0; i < chunkNumber; i++) {
            resultArray.append(bitArray[i].substring(0,chunkSize));
        }
        String result = resultArray.substring(0, originalLength);
        return result;
    }

    public int getChunkNumber() {
        return chunkNumber;
    }

    public int getFinalChunkSize() {
        return finalChunkSize;
    }
}
