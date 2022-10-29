package logic;

import codeAlters.Reformator;
import codeAlters.NoiseProducer;
import codeWrappers.*;

public class Logic {
    public void run() {
        PNGReader reader = new PNGReader();
        int[][] valueMatrix = reader.readImageToIntegerArray("Fale.png");
        Reformator bufferAndChunk = new Reformator(11, 0);
        StringBuilder[] bitMatrix = bufferAndChunk.divide(reader.getWidth(),reader.getHeight(),valueMatrix);

        System.out.println(bitMatrix.length + " bitMatrix after reformator");
        WrapperInterface wrapper = new HummingCheck();
        StringBuilder[] encodedMatrix = wrapper.encode(bitMatrix);
        NoiseProducer noiseProducer = new NoiseProducer(1);
        StringBuilder[] dirtyEncodedMatrix = noiseProducer.introduceNoise(encodedMatrix);
        System.out.println(noiseProducer.getContaminatedChunkSet().size());
        StringBuilder[] decodedMatrix  = wrapper.decode(dirtyEncodedMatrix);
        System.out.println(wrapper.getChunkToResendSet().size());
        System.out.println((float)wrapper.getChunkToResendSet().size()/noiseProducer.getContaminatedChunkSet().size());

        String bitString = bufferAndChunk.extractEsseneAndMergeString(decodedMatrix);
        int[] integerResultArray = bufferAndChunk.mergeToOneIntegerArray(reader.getWidth(),reader.getHeight(),bitString);
        PNGWriter writer = new PNGWriter(reader.getWidth(), reader.getHeight());
        writer.convertToFile(integerResultArray, "Result.png");
    }
}
