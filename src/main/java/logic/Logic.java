package logic;

import codeAlters.BufferAndChunkSetter;
import codeWrappers.TwoDimParityCheck;
import codeWrappers.WrapperInterface;

public class Logic {
    public void run(){
        PNGReader reader = new PNGReader();
        int[][] before = reader.readImageToIntegerArray("DangerNoodle.png");
        int height = reader.getHeight();
        int width = reader.getWidth();
        char[] bitArray = new char[width * height * 32];
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char[] singleInteger = Integer.toBinaryString(before[y][x]).replace(' ', '0').toCharArray();
                for (int locIndex = 0; locIndex < 32; locIndex++) {
                    bitArray[index + locIndex] = singleInteger[locIndex];
                }
                index += 32;
            }
        }

        BufferAndChunkSetter bufferAndChunk = new BufferAndChunkSetter(64, 0);
        StringBuilder[] bitMatrix = bufferAndChunk.divideString(String.valueOf(bitArray));

//        WrapperInterface simpleParityCheck = new SimpleParityCheck(bitMatrix);
        WrapperInterface simpleParityCheck = new TwoDimParityCheck(bitMatrix,4);
        simpleParityCheck.encode();
        System.out.println("Encoding Done");
        char replaceWith = bitMatrix[0].charAt(3);
        bitMatrix[0].replace(3, 4, replaceWith == '0' ? "1" : "0");
        replaceWith = bitMatrix[8].charAt(3);
        bitMatrix[8].replace(3, 4, replaceWith == '0' ? "1" : "0");
        simpleParityCheck.decode(((TwoDimParityCheck) simpleParityCheck).getBitArrayCopy());
        System.out.println(simpleParityCheck.hasChanged());
        System.out.println(simpleParityCheck.getChunkToResendSet());

        /*
        tutaj bedzie cala logika
         */

        index = 0;
        int[] integerResultArray = new int[width * height];
//        String bitString = new String(bitArray);
        String bitString = new String(bufferAndChunk.extractEsenseAndMergeString(bitMatrix));
        for (int i = 0; i < height * width; i++) {
            String bitSubString = bitString.substring(i * 32, i * 32 + 32);
            int bitRepresentationStart = bitSubString.indexOf('1');
            if (bitRepresentationStart > 0) {
                bitSubString = bitSubString.substring(bitRepresentationStart);
            }
            integerResultArray[index++] = Integer.parseUnsignedInt(bitSubString, 2);
        }
        PNGWriter writer = new PNGWriter(width, height);
        writer.convertToFile(integerResultArray, "Result.png");
    }
}
