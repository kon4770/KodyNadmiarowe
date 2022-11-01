package logic;

import codeAlters.NoiseProducer;
import codeAlters.Reformator;
import codeWrappers.ISBNCheck;
import codeWrappers.WrapperInterface;

public class Logic {
    public void run() {
        PNGReader reader = new PNGReader();
        int[][] valueMatrix = reader.readImageToIntegerArray("Fale.png");
        Reformator bufferAndChunk = new Reformator(9, 0);
        StringBuilder[] bitMatrix = bufferAndChunk.divide(reader.getWidth(), reader.getHeight(), valueMatrix);
        //System.out.println(Arrays.toString(bitMatrix));
        DataChecker dt = new DataChecker();
        dt.saveData(bitMatrix);
        //System.out.println(bitMatrix.length + " bitMatrix after reformator");
        WrapperInterface wrapper = new ISBNCheck();
        StringBuilder[] encodedMatrix = wrapper.encode(bitMatrix);
        NoiseProducer noiseProducer = new NoiseProducer(1);
        StringBuilder[] dirtyEncodedMatrix = noiseProducer.introduceNoise(encodedMatrix);
        //System.out.println(noiseProducer.getContaminatedChunkSet().size());
        StringBuilder[] decodedMatrix = wrapper.decode(dirtyEncodedMatrix);
        dt.checkWithOriginal(decodedMatrix);
        System.out.println(dt.getTPBrokenChunks().size());
        System.out.println(wrapper.getChunkToResendSet().size());
        dt.checkChunkDetectionAccuracy(wrapper.getChunkToResendSet());
        System.out.println(dt.getFPBrokenChunks().size());
        //System.out.println((float)wrapper.getChunkToResendSet().size()/noiseProducer.getContaminatedChunkSet().size());

        String bitString = bufferAndChunk.extractEsseneAndMergeString(decodedMatrix);
        int[] integerResultArray = bufferAndChunk.mergeToOneIntegerArray(reader.getWidth(), reader.getHeight(), bitString);
        PNGWriter writer = new PNGWriter(reader.getWidth(), reader.getHeight());
        writer.convertToFile(integerResultArray, "Result.png");
    }
}
