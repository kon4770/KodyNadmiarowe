package logic;

import codeAlters.Reformator;
import codeAlters.NoiseProducer;
import codeWrappers.*;

public class Logic {
    public void run() {
        PNGReader reader = new PNGReader();
        int[][] valueMatrix = reader.readImageToIntegerArray("Kocham.png");
        Reformator bufferAndChunk = new Reformator(9, 0);
        StringBuilder[] bitMatrix = bufferAndChunk.divide(reader.getWidth(),reader.getHeight(),valueMatrix);


        WrapperInterface wrapper = new ISBNCheck();
        StringBuilder[] encodedMatrix = wrapper.encode(bitMatrix);
        NoiseProducer noiseProducer = new NoiseProducer(5);
        StringBuilder[] dirtyEncodedMatrix = noiseProducer.introduceNoise(encodedMatrix);
        System.out.println(noiseProducer.getContaminatedChunkSet().size());

        StringBuilder[] decodedMatrix  = wrapper.decode(dirtyEncodedMatrix);

        int index = 0;
        int[] integerResultArray = new int[reader.getWidth() * reader.getHeight()];
        String bitString = bufferAndChunk.extractEsseneAndMergeString(decodedMatrix);
        for (int i = 0; i < reader.getHeight() * reader.getWidth(); i++) {
            String bitSubString = bitString.substring(i * 32, i * 32 + 32);
            int bitRepresentationStart = bitSubString.indexOf('1');
            if (bitRepresentationStart > 0) {
                bitSubString = bitSubString.substring(bitRepresentationStart);
            }
            integerResultArray[index++] = Integer.parseUnsignedInt(bitSubString, 2);
        }
        PNGWriter writer = new PNGWriter(reader.getWidth(), reader.getHeight());
        writer.convertToFile(integerResultArray, "Result.png");
    }
}
