package com.company;

import java.util.ArrayList;

public class Score {
    private final ArrayList<ArrayList<Integer>> winsCombinations;
    private final int winLength;
    public Score(ArrayList<ArrayList<Integer>> winsCombinations, int winLength) {
        this.winsCombinations = winsCombinations;
        this.winLength = winLength;
    }

    private int countCell(ArrayList<Integer> comb, ArrayList<Integer> pole, int player) {
        int count = 0;
        for (Integer pos : comb) {
            if (pole.get(pos) == null) {
                continue;
            }
            if (pole.get(pos) == player) {
                count++;
            }
        }
        return count;
    }

    private boolean isBockWin(ArrayList<Integer> comb, ArrayList<Integer> pole, int player) {
        return countCell(comb, pole, -player) == (winLength-1) && countCell(comb, pole, player) == 1;
    }

    private boolean isCombinationOpen(ArrayList<Integer> comb, ArrayList<Integer> pole, int player) {
        for (Integer pos : comb) {
            if (pole.get(pos) == null) {
                continue;
            }
            if (pole.get(pos) == -player) {
                return false;
            }
        }
        return true;
    }

    private int scoreCountCell(ArrayList<Integer> comb, ArrayList<Integer> pole, int player) {
        int count = countCell(comb, pole, player);
        if (count >= 1 && count <= winLength) {
            return (int)Math.pow(15, count - 1);
        }
        return count;
    }

    public int getScore(ArrayList<Integer> pole, int player) {
        int score = 0;
        for (ArrayList<Integer> comb : winsCombinations) {
//            if (isBockWin(comb, pole, player)) {
//                score += (int)Math.pow(10, winLength);
//            }
            if (isCombinationOpen(comb, pole, player)) {
                score += scoreCountCell(comb, pole, player);
            }
        }
        return score;
    }


}
