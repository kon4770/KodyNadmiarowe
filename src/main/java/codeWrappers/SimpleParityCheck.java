package codeWrappers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimpleParityCheck implements WrapperInterface {

    private boolean hasChanged = false;
    public StringBuilder[] bitArray;

    private Set<Integer> chunkToResendList = new HashSet<>();

    public SimpleParityCheck(StringBuilder[] bitArray) {
        this.bitArray = bitArray;
    }

    @Override
    public boolean hasChanged() {
        return hasChanged;
    }

    @Override
    public void encode() {
        if (bitArray == null || bitArray.length == 0) {
            return;
        }
        int chunkN = bitArray.length;
        for (int i = 0; i < chunkN; i++) {
            bitArray[i].append(getParityBit(bitArray[i]));
        }
    }

    @Override
    public void decode(StringBuilder[] bitArray) {
        if (bitArray == null || bitArray.length == 0) {
            return;
        }
        int chunkSize = bitArray[0].length() - 1;
        int chunkN = bitArray.length;
        for (int i = 0; i < chunkN; i++) {
            StringBuilder current = bitArray[i];
            char suspectBit = getParityBit(current.substring(0,chunkSize));
            if (suspectBit != current.charAt(chunkSize)) {
                hasChanged = true;
                chunkToResendList.add(i);
            }
            current.setLength(chunkSize);
        }
    }

    @Override
    public Set<Integer> getChunkToResendSet() {
        return chunkToResendList;
    }

    public static char getParityBit(String array){
        boolean hasParity = true;
        for(int cIndex = 0;cIndex<array.length();cIndex++){
            if(array.charAt(cIndex)=='1'){
                hasParity = !hasParity;
            }
        }
        return hasParity?'0':'1';
    }

    public static char getParityBit(StringBuilder array){
        boolean hasParity = true;
        for(int cIndex = 0;cIndex<array.length();cIndex++){
            if(array.charAt(cIndex)=='1'){
                hasParity = !hasParity;
            }
        }
        return hasParity?'0':'1';
    }
}
