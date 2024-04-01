package com.company;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Ai {
    private int countMoves = 0;
    private final int comp;
    private final int human;
    private int depth;
    private final ArrayList<ArrayList<Integer>> winningCombinations;
    private final Score score;
    public Ai(int depth, int winLength, ArrayList<ArrayList<Integer>> winningCombinations){
        this.comp = -1;
        this.human = 1;
        this.depth = depth;
        this.winningCombinations = winningCombinations;
        this.score = new Score(winningCombinations, winLength);
    }
    public int getMove(ArrayList<Integer> pole) {
        int sqr = (int)Math.sqrt(pole.size());
        if (countMoves % sqr  == 0 && countMoves != 0) {
            depth += 2;
        }
        System.out.println("start search depth = " + depth);
        int[] hod = minimaxAlphaBeta(pole, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, comp);
        System.out.println("end search depth = " + depth + " move cell = " + hod[1] + " score = " + hod[0]);
        countMoves++;
        return hod[1];

    }

    private long count(ArrayList<Integer> pole) {
        return pole.stream().filter(Objects::isNull).count();
    }

    private boolean ownedOnePlayer(ArrayList<Integer> comb, ArrayList<Integer> pole) {
        if (pole.get(comb.get(0)) == null) {
            return false;
        }
        int first = pole.get(comb.get(0));

        for (int i = 1; i != comb.size(); i++) {
            Integer pos = comb.get(i);
            if (pole.get(pos) == null) {
                return false;
            }
            if (pole.get(pos) != first) {
                return false;
            }
        }
        return true;
    }

    private ArrayList<Integer> emptyCells(ArrayList<Integer> pole){
        return IntStream.range(0, pole.size())
                .filter(i -> pole.get(i) == null)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private int score(int who, ArrayList<Integer> gameState, int player) {
        if (who == comp) {
            return 100000000;
        } else if (who == human) {
            return -100000000;
        }
        if (player == comp) {
            return score.getScore(gameState, player) - score.getScore(gameState, -player);
        } else {
            return score.getScore(gameState, -player) - score.getScore(gameState, player);
        }
    }

    public int isWin(ArrayList<Integer> pole) {
        for(ArrayList<Integer> winComb: winningCombinations){
            if (ownedOnePlayer(winComb, pole)) {
                return pole.get(winComb.get(0));
            }
        }
        return 0;
    }

    private int[] minimaxAlphaBeta(ArrayList<Integer> gameState, int depth, int alpha, int beta, int player) {
        int gameResult = isWin(gameState);
        if (depth == 0 || gameResult != 0 || count(gameState) == 0) {
            return new int[] {score(gameResult, gameState, player) * (depth + 1), -1};
        }

        int bestMove = -1;
        int scoreEval = player == comp ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        List<Integer> availableMoves = emptyCells(gameState);

        for (Integer move : availableMoves) {
            gameState.set(move, player);
            int eval = minimaxAlphaBeta(gameState, depth - 1, alpha, beta, -player)[0];
            gameState.set(move, null);

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
