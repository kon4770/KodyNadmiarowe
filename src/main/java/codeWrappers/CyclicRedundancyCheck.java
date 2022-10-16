package codeWrappers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CyclicRedundancyCheck implements WrapperInterface {
    private String divisor = "";
    private Set<Integer> chunkToResendList = new HashSet<>();

    public CyclicRedundancyCheck(int divisorIndex) {
        Map<Integer, Integer> dividerMap = new HashMap<>();
        dividerMap.putAll(Map.of(3, 0x5, 4, 0x9, 5, 0x12, 6, 0x23, 7, 0x44, 8, 0xD3, 10, 0x3EC, 11, 0x5C2, 12, 0xC07, 13, 0x1E7A));
        dividerMap.putAll(Map.of(14, 0x3016, 15, 0x740A, 16, 0x8810, 17, 0x1B42D, 21, 0x18144C, 24, 0xAEB6E5, 30, 0x30185CE3, 32, 0x82608EDB));
        this.divisor = String.format("%32s", Integer.toBinaryString(dividerMap.get(divisorIndex))).strip().concat("1");
    }


    public StringBuilder[] encode(StringBuilder[] bitArray) {
        StringBuilder rem;
        StringBuilder divisor = new StringBuilder(this.divisor);
        for (int i = 0; i < bitArray.length; i++) {
            rem = divideDataWithDivisor(bitArray[i], divisor);
            bitArray[i].append(rem.substring(0, rem.length() - 1));
        }
        return bitArray;
    }

    public StringBuilder[] decode(StringBuilder[] bitArray) {
        StringBuilder divisor = new StringBuilder(this.divisor);
        for (int i = 0; i < bitArray.length; i++) {
            if (!isDataCorrect(bitArray[i], divisor)) {
                chunkToResendList.add(i);
            }
        }
        return bitArray;
    }

    @Override
    public Set<Integer> getChunkToResendSet() {
        return chunkToResendList;
    }

    private StringBuilder divideDataWithDivisor(StringBuilder oldData, StringBuilder divisor) {
//        System.out.println("oldData " + oldData);
//        System.out.println("divisor " + divisor);

        StringBuilder data = new StringBuilder("0".repeat(oldData.length() + divisor.length()));
        StringBuilder rem = new StringBuilder("0".repeat(divisor.length()));

        data.replace(0, oldData.length(), oldData.toString());
        rem.replace(0, divisor.length(), data.substring(0, divisor.length()));

//        System.out.println(data);
//        System.out.println(rem);
        for (int i = 0; i < oldData.length(); i++) {
//            System.out.println((i + 1) + ".) First data bit is : " + rem.charAt(0));
//            System.out.print("Remainder : ");
            if (rem.charAt(0) == '1') {
                for (int j = 1; j < divisor.length(); j++) {
                    rem.setCharAt(j - 1, exorOperation(rem.charAt(j), divisor.charAt(j)));
//                    System.out.print(rem.charAt(j - 1));
                }
            } else {
                for (int j = 1; j < divisor.length(); j++) {
                    rem.setCharAt(j - 1, exorOperation(rem.charAt(j), '0'));
//                    System.out.print(rem.charAt(j - 1));
                }
            }
            try {
                rem.replace(divisor.length() - 1, divisor.length(), String.valueOf(data.charAt(i + divisor.length())));
            } catch (Exception e) {
//                System.out.println("asa " + (divisor.length() - 1) + " " + (i + divisor.length()));
//                System.out.println("Aaa");
            }
//            System.out.println(rem.charAt(divisor.length() - 1));
        }
//        System.out.println("rem " + rem);
        return rem;
    }

    private char exorOperation(char x, char y) {
        if (x == y) {
            return '0';
        }
        return '1';
    }

    private boolean isDataCorrect(StringBuilder data, StringBuilder divisor) {

        StringBuilder rem = divideDataWithDivisor(data, divisor);
//        System.out.println("Koncowy " + rem);
        for (int i = 0; i < rem.length(); i++) {
            if (rem.charAt(i) != '0') {
//                System.out.println("Currupted data received...");
                return false;
            }
        }
//        System.out.println("Data received without any error.");
        return true;
    }
}  