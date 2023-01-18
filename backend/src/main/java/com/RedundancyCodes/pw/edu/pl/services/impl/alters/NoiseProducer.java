package com.RedundancyCodes.pw.edu.pl.services.impl.alters;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class NoiseProducer {
    private int noiseDensityPerMillionBits;
    private Set<Integer> contaminatedChunkSet = new HashSet<>();
    private Set<Integer> cleanChunkSet = new HashSet<>();

    private char zeroChar = '0';
    private char oneChar = '1';

    public NoiseProducer(int noiseDensityPerMillionBits) {
        this.noiseDensityPerMillionBits = noiseDensityPerMillionBits;
    }

    public StringBuilder[] introduceNoise(StringBuilder[] cleanData) {
        StringBuilder[] contaminatedData = cleanData;
        int chunkSize = cleanData[0].length();
        int chunkNumber = cleanData.length;

        BigInteger bitsToContaminate = (((BigInteger.valueOf(chunkSize)
                .multiply(BigInteger.valueOf(chunkNumber)
                        .multiply(BigInteger.valueOf(noiseDensityPerMillionBits))))
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

        for (int i = 0; i < cleanData.length; i++) {
            if (!contaminatedChunkSet.contains(i)) {
                cleanChunkSet.add(i);
            }
        }
        return contaminatedData;
    }

    private StringBuilder flipBit(StringBuilder array, int bitIndex) {
        if (array.charAt(bitIndex) == zeroChar) {
            array.setCharAt(bitIndex, oneChar);
        } else {
            array.setCharAt(bitIndex, zeroChar);
        }
        return array;
    }

    public Set<Integer> getContaminatedChunkSet() {
        return contaminatedChunkSet;
    }

    public Set<Integer> getCleanChunkSet() {
        return cleanChunkSet;
    }
}
