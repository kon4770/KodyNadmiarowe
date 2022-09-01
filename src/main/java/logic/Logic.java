package logic;

import codeAlters.BufferAndChunkSetter;
import codeAlters.NoiseProducer;
import codeWrappers.CyclicRedundancyCheck;
import codeWrappers.TwoDimParityCheck;
import codeWrappers.WrapperInterface;

public class Logic {
    public void run() {
        PNGReader reader = new PNGReader();
        int[][] before = reader.readImageToIntegerArray("Fale.png");
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

        BufferAndChunkSetter bufferAndChunk = new BufferAndChunkSetter(17, 0);
        StringBuilder[] bitMatrix = bufferAndChunk.divideString(String.valueOf(bitArray));


        CyclicRedundancyCheck cyclicRedundancyCheck = new CyclicRedundancyCheck(5,bitMatrix);
        cyclicRedundancyCheck.encode();

        NoiseProducer noiseProducer = new NoiseProducer(1);
        bitMatrix = noiseProducer.introduceNoise(bitMatrix);
        System.out.println("Bledy sa w " + noiseProducer.getContaminatedChunkSet());
        System.out.println(noiseProducer.getContaminatedChunkSet().size());

        cyclicRedundancyCheck.decode();


        /*
        tutaj bedzie cala logika
         */

        index = 0;
        int[] integerResultArray = new int[width * height];
//        String bitString = new String(bitArray);
        String bitString = new String(bufferAndChunk.extractEsseneAndMergeString(bitMatrix));
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
