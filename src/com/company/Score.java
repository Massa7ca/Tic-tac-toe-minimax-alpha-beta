package com.company;


import java.util.ArrayList;

public class Score {
    private final ArrayList<ArrayList<Integer>> winningCombinations;
    private final int winLength;
    public Score(ArrayList<ArrayList<Integer>> winningCombinations, int winLength) {
        this.winningCombinations = winningCombinations;
        this.winLength = winLength;
    }

    private int countCell(ArrayList<Integer> comb, ArrayList<Integer> pole, int player) {
        int count = 0;
        for (Integer pos : comb) {
            if (pole.get(pos) == 0) {
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
        int countEmpty = 0;
        for (Integer pos : comb) {
            if (pole.get(pos) == 0) {
                countEmpty ++;
                continue;
            }
            if (pole.get(pos) == -player) {
                return false;
            }
        }

        return countEmpty != winLength;
    }

    private int scoreCountCell(ArrayList<Integer> comb, ArrayList<Integer> pole, int player) {
        int count = countCell(comb, pole, player);
        if (count == 0) {
            return 0;
        }
        return (int)Math.pow(10, count);
    }

    public int getScore(ArrayList<Integer> pole, int player) {
        int score = 0;
        for (ArrayList<Integer> comb : winningCombinations) {
//            if (isBockWin(comb, pole, player)) {
//                score += (int)Math.pow(15, winLength);
//            }
            if (isCombinationOpen(comb, pole, player)) {
                score += (scoreCountCell(comb, pole, player) * 2);
            }
        }
        return score;
    }


}
