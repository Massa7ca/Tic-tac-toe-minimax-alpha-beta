package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Utils {
    public static ArrayList<Integer> arraytoArrayList(int[] array) {
        return Arrays.stream(array)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static int[] arrayListToArray(ArrayList<Integer> array) {
        var t = new int[array.size()];
        for(int i = 0; i != array.size(); i++) {
            t[i] = array.get(i);
        }

        return t;
    }

    public static int[][] removeRow(int[][] original, int rowIndex) {
        int[][] result = new int[original.length - 1][];

        if (rowIndex >= 0) System.arraycopy(original, 0, result, 0, rowIndex);

        if (original.length - 1 - rowIndex >= 0)
            System.arraycopy(original, rowIndex + 1, result, rowIndex, original.length - 1 - rowIndex);

        return result;
    }
}
