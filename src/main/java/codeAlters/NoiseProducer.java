package codeAlters;

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

        int bitsToContaminate = chunkSize * chunkNumber / 1000000 * noiseDensityPerMillionBits;
        System.out.println(chunkNumber * chunkSize);
        System.out.println(bitsToContaminate);
        long maxBitIndex = chunkSize * chunkNumber;
        for (int i = 0; i < bitsToContaminate; i++) {
            long randomBit = ThreadLocalRandom.current().nextLong(maxBitIndex);
            int chunkId = (int) randomBit / chunkSize;
            int bitIdInsideChunk = (int) randomBit % chunkSize;
            contaminatedData[chunkId] = flipBit(contaminatedData[chunkId], bitIdInsideChunk);
            contaminatedChunkSet.add(chunkId);
        }

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
