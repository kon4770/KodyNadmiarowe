package codeAlters;

public class Reformator {
    private int singleChunkBufferSize;
    private int chunkSize;
    private int chunkNumber;
    private int finalChunkSize;
    private int originalLength;

    public Reformator(int chunkSize, int singleChunkBufferSize) {
        this.singleChunkBufferSize = singleChunkBufferSize;
        this.chunkSize = chunkSize;
    }

    public StringBuilder[] divide(int width, int height, int[][] matrix) {
        String bitString  = String.valueOf(getContinuesCharOfBits(width,height,matrix));
        int length = bitString.length();
        originalLength = length;
        int nWholePieces = length / chunkSize;
        String buffer = "0";
        buffer = buffer.repeat(singleChunkBufferSize);
        int rest = length % chunkSize;
        if (rest != 0) {
            String endFiller = "0";
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

    public String extractEsseneAndMergeString(StringBuilder[] bitArray) {
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

    private char[] getContinuesCharOfBits(int width, int height, int[][] matrix){
        char[] bitArray = new char[width * height * 32];
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char[] singleInteger = Integer.toBinaryString(matrix[y][x]).replace(' ', '0').toCharArray();
                for (int locIndex = 0; locIndex < 32; locIndex++) {
                    bitArray[index + locIndex] = singleInteger[locIndex];
                }
                index += 32;
            }
        }
        return bitArray;
    }

    public int[] mergeToOneIntegerArray(int width, int height, String bitString) {
        int index = 0;
        int[] integerResultArray = new int[width* height];
        for (int i = 0; i < width * height; i++) {
            String bitSubString = bitString.substring(i * 32, i * 32 + 32);
            int bitRepresentationStart = bitSubString.indexOf('1');
            if (bitRepresentationStart > 0) {
                bitSubString = bitSubString.substring(bitRepresentationStart);
            }
            integerResultArray[index++] = Integer.parseUnsignedInt(bitSubString, 2);
        }
        return integerResultArray;
    }
}
