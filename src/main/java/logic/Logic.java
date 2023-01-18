package logic;

import codeAlters.NoiseProducer;
import codeAlters.Reformator;
import codeWrappers.*;

import java.util.Arrays;

public class Logic {
    public void run() {
        PNGReader reader = new PNGReader();
        int[][] valueMatrix = reader.readImageToIntegerArray("Small.png");
        Reformator bufferAndChunk = new Reformator(60, 0);
        StringBuilder[] bitMatrix = bufferAndChunk.divide(reader.getWidth(), reader.getHeight(), valueMatrix);
        System.out.println(bitMatrix.length + " chunk count after reformator");
        WrapperInterface wrapper = new HummingCheck();
        System.out.println(Arrays.toString(bitMatrix));
        StringBuilder[] encodedMatrix = wrapper.encode(bitMatrix);
//        NoiseProducer noiseProducer = new NoiseProducer(50000);
//        StringBuilder[] dirtyEncodedMatrix = noiseProducer.introduceNoise(encodedMatrix);
//        System.out.println(noiseProducer.getContaminatedChunkSet().size());
        System.out.println(Arrays.toString(encodedMatrix));
        encodedMatrix[0].setCharAt(67,'0');
        encodedMatrix[0].setCharAt(1,'0');
        encodedMatrix[0].setCharAt(3,'0');
        System.out.println(Arrays.toString(encodedMatrix));
        StringBuilder[] decodedMatrix = wrapper.decode(encodedMatrix);
        System.out.println(Arrays.toString(decodedMatrix));
        System.out.println(wrapper.getChunkToResendSet());
//        DataChecker dt = new DataChecker();
//        dt.checkWithOriginal(wrapper.getChunkToResendSet(), noiseProducer.getContaminatedChunkSet());
//        System.out.println("true  pos  " + dt.getTPBrokenChunks().size());
//        System.out.println((float)dt.getTPBrokenChunks().size() / dt.getSetSumSize());
//        System.out.println("false pos " + dt.getFPBrokenChunks().size());
//        System.out.println((float)dt.getFPBrokenChunks().size() / dt.getSetSumSize());
//        System.out.println("false neg " + dt.getFNBrokenChunks().size());
//        System.out.println((float)dt.getFNBrokenChunks().size() / dt.getSetSumSize());
        //System.out.println((float)wrapper.getChunkToResendSet().size()/noiseProducer.getContaminatedChunkSet().size());

        String bitString = bufferAndChunk.extractEsseneAndMergeString(decodedMatrix);
        int[] integerResultArray = bufferAndChunk.mergeToOneIntegerArray(reader.getWidth(), reader.getHeight(), bitString);
        PNGWriter writer = new PNGWriter(reader.getWidth(), reader.getHeight());
        writer.convertToFile(integerResultArray, "Result.png");
    }
}
