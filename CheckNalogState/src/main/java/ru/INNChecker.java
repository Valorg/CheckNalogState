package ru;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class INNChecker {
    private static final int[] MULT_N1 = {7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
    private static final int[] MULT_N2 = {3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
    private static final int[] MULT_N = {2, 4, 10, 3, 5, 9, 4, 6, 8};

    public static boolean isCorrectINN(String INN){
        int[] INNIntArr = INN.chars().map(Character::getNumericValue).toArray();
        if(INNIntArr.length == 10)
            return INNIntArr[INNIntArr.length-1] == getChecksum(INNIntArr, MULT_N);
        else{
            boolean a = INNIntArr[INNIntArr.length-2] == getChecksum(INNIntArr, MULT_N1);
            boolean b = INNIntArr[INNIntArr.length-1] == getChecksum(INNIntArr, MULT_N2);
            return (a && b);
        }
    }

    public static int getChecksum(int[] arr, int[] mult) {
        int checksum = 0;
        for (int i = 0; i < mult.length; i++) {
            checksum += (arr[i] * mult[i]);
        }
        return (checksum % 11) % 10;
    }
}
