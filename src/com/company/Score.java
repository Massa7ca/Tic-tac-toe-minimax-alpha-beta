package com.company;


import java.util.ArrayList;

public class Score {
    private final int winLength;
    public Score(int winLength) {
        this.winLength = winLength;
    }

    private int countCell(int[] comb, byte[] pole, byte player) {
        int count = 0;
        for (int pos : comb) {
            if (pole[pos] == player) {
                count++;
            }
        }
        return count;
    }

    private boolean isCenter(ArrayList<Integer> places) {
        int length = places.size();
        if (length % 2 == 0) {
            if (!places.get(length / 2).equals(0) || !places.get((length / 2) - 1).equals(0)) {
                return true;
            }
        }
        return !places.get(length / 2).equals(0);
    }

    private int score(ArrayList<Integer> comb, ArrayList<Integer> pole, int player) {
        var places = new ArrayList<Integer>();
        for (Integer cord : comb) {
            int pos = pole.get(cord);
            if (pos == 0 || pos == player) {
                places.add(pos);
            }
        }
        int count = (int)places.stream().filter(i -> i == player).count();
        if (count == 1) {
            boolean isCenter = isCenter(places);
            if (isCenter) {
                return (int)Math.pow(10, 2);
            }
            return (int)Math.pow(10, 1);
        }
        int score = 0;
        int cof = 0;
        boolean playerMet = false;
        for(int i: places) {
            if (i == player) {
                playerMet = true;
                if (cof > 0) {
                    score += (int)Math.pow(3, cof);
                    cof = 0;
                }
                score *= 10;
            } else if (playerMet && i == 0) {
                cof ++;
            }
        }
        return score;
    }

    private boolean isBockWin(int[] comb, byte[] pole, byte player) {
        return countCell(comb, pole, (byte) -player) == (winLength-1) && countCell(comb, pole, player) == 1;
    }

    private boolean isCombinationOpen(int[] comb, byte[] pole, byte player) {
        int countEmpty = 0;
        for (int pos : comb) {
            int poleValue = pole[pos];
            if (poleValue == 0) {
                countEmpty++;
            } else if (poleValue == -player) {
                return false;
            }
        }
        return countEmpty != winLength;
    }

    private int scoreCountCell(int[] comb, byte[] pole, byte player) {
        int count = countCell(comb, pole, player);
        if (count == 0) {
            return 0;
        }
        return (int)Math.pow(15, count);
    }

    public int getScore(byte[] pole, byte player, int[][] winningCombinations) {
        int score = 0;
        for (int[] comb : winningCombinations) {
//            if (isBockWin(comb, pole, player)) {
//                score += (int)Math.pow(15, winLength);
//            }
            if (isCombinationOpen(comb, pole, player)) {
                score += scoreCountCell(comb, pole, player);
            }
        }
        return score;
    }


}
