package com.company;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;


public class Ai {
    private final byte comp;
    private final byte human;
    private int depth;
    private final HashMap<Pair<ByteArray, Integer>, Integer> cache;
    private int[][] winningCombinations;

    private int nodes = 0;
    private final ArrayList<Integer> moveTimeHistory = new ArrayList<>();
    private final Score score;
    public Ai(int depth, int winLength, int[][] winningCombinations){
        this.comp = -1;
        this.human = 1;
        this.depth = depth;
        this.cache = new HashMap<>();
        this.winningCombinations = winningCombinations;
        this.score = new Score(winLength);
    }
    public int getMove(byte[] pole) {
        deleteFilledCombinations(pole);
        if (countTimeFast(moveTimeHistory) == 4) {
            depth += 2;
            moveTimeHistory.clear();
            System.out.println("depth set = " + depth);
        }
        System.out.println("start search depth = " + depth);
        long t = System.nanoTime();
        int[] hod = minimaxAlphaBeta(pole, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, comp);
        int time = (int) ((System.nanoTime() - t) / 1000000);
        moveTimeHistory.add(time);
        System.out.println("end search depth = " + depth + " move cell = " + hod[1] + " score = " + hod[0] + " time " + time + " nodes = " + nodes);
        deleteFilledCombinations(pole);
        nodes = 0;
        return hod[1];

    }

    private void deleteFilledCombinations(byte[] pole) {
        for(int i = 0; i != winningCombinations.length; i++) {
            if(combIsFull(winningCombinations[i], pole)) {
                winningCombinations = Utils.removeRow(winningCombinations, i);
                deleteFilledCombinations(pole);
                break;
            }
        }
    }

    private boolean combIsFull(int[] comb, byte[] pole) {
        for (Integer pos : comb) {
            if (pole[pos] == 0) {
                return false;
            }
        }
        return true;
    }
    private long countTimeFast(ArrayList<Integer> moveTimeHistory) {
        return moveTimeHistory.stream().filter(i -> i < 1000).count();
    }

    private long count(byte[] pole) {
        int count = 0;
        for (int i: pole) {
            if (i == 0) {
               count ++;
            }
        }
        return count;
    }

    private boolean ownedOnePlayer(int[] comb, byte[] pole) {
        int first = pole[comb[0]];
        for (int i = 1; i != comb.length; i++) {
            int pos = comb[i];
            if (pole[pos] == 0 || pole[pos] != first) {
                return false;
            }
        }
        return true;
    }

    private int[] emptyCells(byte[] pole) {
        int[] temp = new int[pole.length];
        int count = 0;

        for (int i = 0; i != pole.length; i++) {
            if (pole[i] == 0) {
                temp[count++] = i;
            }
        }
        int[] t = new int[count];
        System.arraycopy(temp, 0, t, 0, count);
        return t;
    }

    private int score(int who, byte[] gameState, int player) {
        nodes ++;
        if (who == comp) {
            return 100000000;
        } else if (who == human) {
            return -100000000;
        }

        int score;
        var key = new Pair<>(new ByteArray(gameState), player);
        var scoreCache = cache.get(key);
        if (scoreCache != null) {
            return scoreCache;
        }
        if (player == comp) {
            score = this.score.getScore(gameState, comp, winningCombinations) - this.score.getScore(gameState, human, winningCombinations);
        } else {
            score = this.score.getScore(gameState, human, winningCombinations) - this.score.getScore(gameState, comp, winningCombinations);
        }
        cache.put(key, score);
        return score;
    }

    public int isWin(byte[] pole) {
        for (int[] winComb: winningCombinations) {
            if (ownedOnePlayer(winComb, pole)) {
                return pole[winComb[0]];
            }
        }
        return 0;
    }

    private int[] minimaxAlphaBeta(byte[] gameState, int depth, int alpha, int beta, byte player) {
        int gameResult = isWin(gameState);
        if (depth == 0 || gameResult != 0 || count(gameState) == 0) {
            return new int[] {score(gameResult, gameState, player), -1};
        }

        int bestMove = -1;
        int scoreEval = player == comp ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int[] availableMoves = emptyCells(gameState);

        for (int move : availableMoves) {
            gameState[move] = player;
            int eval = minimaxAlphaBeta(gameState, depth - 1, alpha, beta, (byte) -player)[0];
            gameState[move] = 0;

            if (player == comp) {
                if (eval > scoreEval) {
                    scoreEval = eval;
                    bestMove = move;
                    alpha = Math.max(alpha, eval);
                }
            } else {
                if (eval < scoreEval) {
                    scoreEval = eval;
                    bestMove = move;
                    beta = Math.min(beta, eval);
                }
            }
            if (beta <= alpha) break;
        }
        return new int[] {scoreEval, bestMove};
    }
}
