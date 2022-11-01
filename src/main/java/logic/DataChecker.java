package logic;

import java.util.HashSet;
import java.util.Set;

public class DataChecker {

    private StringBuilder[] orgBitArray;
    private Set<Integer> tpBrokenChunks = new HashSet<>();
    private Set<Integer> fpBrokenChunks = new HashSet<>();

    public boolean checkWithOriginal(StringBuilder[] susBitArray) {
        int chunkSize = orgBitArray[0].length();
        for (int chunkId = 0; chunkId < susBitArray.length; chunkId++) {
            for (int bitIndex = 0; bitIndex < chunkSize; bitIndex++) {
                if (susBitArray[chunkId].charAt(bitIndex) != orgBitArray[chunkId].charAt(bitIndex)) {
                    tpBrokenChunks.add(chunkId);
                }
            }
//            if (brokenChunks.contains(chunkId)) {
//                System.out.println("sus = " + susBitArray[chunkId]);
//                System.out.println("org = " + orgBitArray[chunkId]);
//            }
        }
        return tpBrokenChunks.isEmpty();
    }

    public boolean checkChunkDetectionAccuracy(Set<Integer> susChunks){
        for(Integer index: susChunks){
            if(tpBrokenChunks.contains(index)){
                continue;
            }
            fpBrokenChunks.add(index);
        }
        return fpBrokenChunks.isEmpty();
    }

    public void saveData(StringBuilder[] bitArray) {
        this.orgBitArray = new StringBuilder[bitArray.length];
        for (int i = 0; i < bitArray.length; i++) {
            this.orgBitArray[i] = new StringBuilder(bitArray[i].toString());
        }
    }

    public Set<Integer> getTPBrokenChunks() {
        return tpBrokenChunks;
    }

    public Set<Integer> getFPBrokenChunks() {
        return fpBrokenChunks;
    }

}
