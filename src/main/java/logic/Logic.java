package logic;

import codeAlters.NoiseProducer;
import codeAlters.Reformator;
import codeWrappers.*;

public class Logic {
    public void run() {
        PNGReader reader = new PNGReader();
        int[][] valueMatrix = reader.readImageToIntegerArray("Fale.png");
        Reformator bufferAndChunk = new Reformator(500, 0);
        StringBuilder[] bitMatrix = bufferAndChunk.divide(reader.getWidth(), reader.getHeight(), valueMatrix);
        System.out.println(bitMatrix.length + " chunk count after reformator");
        WrapperInterface wrapper = new HummingCheck();
        StringBuilder[] encodedMatrix = wrapper.encode(bitMatrix);
        NoiseProducer noiseProducer = new NoiseProducer(100);
        StringBuilder[] dirtyEncodedMatrix = noiseProducer.introduceNoise(encodedMatrix);
        System.out.println(noiseProducer.getContaminatedChunkSet().size());
        StringBuilder[] decodedMatrix = wrapper.decode(dirtyEncodedMatrix);
        DataChecker dt = new DataChecker();
        dt.checkWithOriginal(wrapper.getChunkToResendSet(), noiseProducer.getContaminatedChunkSet());
        System.out.println("true  pos  " + dt.getTPBrokenChunks().size());
        System.out.println((float)dt.getTPBrokenChunks().size() / dt.getSetSumSize());
        System.out.println("false pos " + dt.getFPBrokenChunks().size());
        System.out.println((float)dt.getFPBrokenChunks().size() / dt.getSetSumSize());
        System.out.println("false neg " + dt.getFNBrokenChunks().size());
        System.out.println((float)dt.getFNBrokenChunks().size() / dt.getSetSumSize());
        //System.out.println((float)wrapper.getChunkToResendSet().size()/noiseProducer.getContaminatedChunkSet().size());

        String bitString = bufferAndChunk.extractEsseneAndMergeString(decodedMatrix);
        int[] integerResultArray = bufferAndChunk.mergeToOneIntegerArray(reader.getWidth(), reader.getHeight(), bitString);
        PNGWriter writer = new PNGWriter(reader.getWidth(), reader.getHeight());
        writer.convertToFile(integerResultArray, "Result.png");
    }
}
