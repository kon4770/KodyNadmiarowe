package codeAlters;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class NoiseProducer {
    private int noiseDensityPerMillionBits;
    private Set<Integer> contaminatedChunkSet = new HashSet<>();

    public NoiseProducer(int noiseDensityPerMillionBits) {
        this.noiseDensityPerMillionBits = noiseDensityPerMillionBits;
    }

    public StringBuilder[] introduceNoise(StringBuilder[] cleanData) {
        StringBuilder[] contaminatedData = cleanData;
        int chunkSize = cleanData[0].length();
        int chunkNumber = cleanData.length;

        BigInteger bitsToContaminate = (BigInteger.valueOf(chunkSize)
                .multiply(BigInteger.valueOf(chunkNumber)
                        .multiply(BigInteger.valueOf(noiseDensityPerMillionBits))
                                .divide(BigInteger.valueOf(1000000))));
        long maxBitIndex = chunkSize * chunkNumber;
        if (bitsToContaminate.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
            bitsToContaminate = BigInteger.valueOf(Integer.MAX_VALUE);
        }
        int bitsToContaminateInteger = bitsToContaminate.intValue();
        for (int i = 0; i < bitsToContaminateInteger; i++) {
            long randomBit = ThreadLocalRandom.current().nextLong(maxBitIndex);
            int chunkId = (int) randomBit / chunkSize;
            int bitIdInsideChunk = (int) randomBit % chunkSize;
            contaminatedData[chunkId] = flipBit(contaminatedData[chunkId], bitIdInsideChunk);
            contaminatedChunkSet.add(chunkId);
        }
        System.out.println(cleanData.length);

        return contaminatedData;
    }

    private StringBuilder flipBit(StringBuilder cleanArray, int bitIndex) {
        String replaceWith = cleanArray.charAt(bitIndex) == '0' ? "1" : "0";
        StringBuilder contaminatedArray = cleanArray.replace(bitIndex, bitIndex + 1, replaceWith);
        return contaminatedArray;
    }

    public Set<Integer> getContaminatedChunkSet() {
        return contaminatedChunkSet;
    }
}
