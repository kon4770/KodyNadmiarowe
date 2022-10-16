package codeWrappers;

import java.util.HashSet;
import java.util.Set;

public class ISBNCheck implements WrapperInterface{

    boolean isISBN10 = true;
    private Set<Integer> chunkToResendList = new HashSet<>();

    public StringBuilder[] encode(StringBuilder[] bitArray) {
        if (bitArray[0].length() == 12) {
            isISBN10 = false;
        }
        if (isISBN10) {
            for (int i = 0; i < bitArray.length; i++) {
                int sum = 0;
                for (int j = 0; j < 9; j++) {
                    sum += j * bitArray[i].charAt(j);
                }
                int residual = sum % 11;
                bitArray[i].append(String.format("%4s", Integer.toBinaryString(residual))
                        .replaceAll(" ", "0"));
            }
        } else {
            for (int i = 0; i < bitArray.length; i++) {
                int sum = 0;
                boolean alternator = true;
                for (int j = 0; j < 12; j++) {
                    if (alternator) {
                        sum += bitArray[i].charAt(j);
                    } else {
                        sum += 3 * bitArray[i].charAt(j);
                    }
                    alternator = !alternator;
                }
                int residual = 10 - sum % 10;
                bitArray[i].append(String.format("%4s", Integer.toBinaryString(residual))
                        .replaceAll(" ", "0"));
            }
        }
        return bitArray;
    }

    public StringBuilder[] decode(StringBuilder[] bitArray) {
        if (isISBN10) {
            for (int i = 0; i < bitArray.length; i++) {
                int sum = 0;
                for (int j = 0; j < 9; j++) {
                    sum += j * bitArray[i].charAt(j);
                }
                sum += Integer.parseInt(bitArray[i].substring(9), 2);
                if (sum % 11 != 0) {
                    chunkToResendList.add(i);
                }
            }
        } else {
            for (int i = 0; i < bitArray.length; i++) {
                int sum = 0;
                boolean alternator = true;
                for (int j = 0; j < 12; j++) {
                    if (alternator) {
                        sum += bitArray[i].charAt(j);
                    } else {
                        sum += 3 * bitArray[i].charAt(j);
                    }
                    alternator = !alternator;
                }
                sum += Integer.parseInt(bitArray[i].substring(12), 2);
                if (sum % 10 != 0) {
                    chunkToResendList.add(i);
                }
            }
        }
        return bitArray;
    }

    @Override
    public Set<Integer> getChunkToResendSet() {
        return chunkToResendList;
    }

    public boolean isISBNsetTo13SymbolVariant() {
        return !isISBN10;
    }
}



