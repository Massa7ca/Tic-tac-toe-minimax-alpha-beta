package com.company;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Ai {
    private final int comp;
    private final int human;
    private int depth;
    private final HashMap<Pair<ArrayList<Integer>, Integer>, Integer> cache;
    private final ArrayList<ArrayList<Integer>> winningCombinations;
    private final ArrayList<Integer> moveTimeHistory = new ArrayList<>();
    private final Score score;
    public Ai(int depth, int winLength, ArrayList<ArrayList<Integer>> winningCombinations){
        this.comp = -1;
        this.human = 1;
        this.depth = depth;
        this.cache = new HashMap<>();
        this.winningCombinations = winningCombinations;
        this.score = new Score(winningCombinations, winLength);
    }
    public int getMove(OptimizedHashArrayList<Integer> pole) {
        winningCombinations.removeIf(it -> combIsFull(it, pole));
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
        System.out.println("end search depth = " + depth + " move cell = " + hod[1] + " score = " + hod[0] + " time " + time);
        return hod[1];

    }

    private boolean combIsFull(ArrayList<Integer> comb, ArrayList<Integer> pole) {
        for (Integer pos : comb) {
            if (pole.get(pos) == 0) {
                return false;
            }
        }
        return true;
    }
    private long countTimeFast(ArrayList<Integer> moveTimeHistory) {
        return moveTimeHistory.stream().filter(i -> i < 1000).count();
    }

    private long count(ArrayList<Integer> pole) {
        return pole.stream().filter(i -> i == 0).count();
    }

    private boolean ownedOnePlayer(ArrayList<Integer> comb, ArrayList<Integer> pole) {
        int first = pole.get(comb.get(0));
        for (int i = 1; i != comb.size(); i++) {
            int pos = comb.get(i);
            if (pole.get(pos) == 0 || pole.get(pos) != first) {
                return false;
            }
        }
        return true;
    }

    private ArrayList<Integer> emptyCells(ArrayList<Integer> pole){
        return IntStream.range(0, pole.size())
                .filter(i -> pole.get(i) == 0)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private int score(int who, ArrayList<Integer> gameState, int player) {
        if (who == comp) {
            return 100000000;
        } else if (who == human) {
            return -100000000;
        }

        int score;
        var key = new Pair<>(gameState, player);
        var scoreCache = cache.get(key);
        if (scoreCache != null) {
            return scoreCache;
        }
        if (player == comp) {
            score = this.score.getScore(gameState, comp) - this.score.getScore(gameState, human);
        } else {
            score = this.score.getScore(gameState, human) - this.score.getScore(gameState, comp);
        }
        cache.put(key, score);
        return score;
    }

    public int isWin(OptimizedHashArrayList<Integer> pole) {
        for (ArrayList<Integer> winComb: winningCombinations) {
            if (ownedOnePlayer(winComb, pole)) {
                return pole.get(winComb.get(0));
            }
        }
        return 0;
    }

    private int[] minimaxAlphaBeta(OptimizedHashArrayList<Integer> gameState, int depth, int alpha, int beta, int player) {
        int gameResult = isWin(gameState);
        if (depth == 0 || gameResult != 0 || count(gameState) == 0) {
            return new int[] {score(gameResult, gameState, player), -1};
        }

        int bestMove = -1;
        int scoreEval = player == comp ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        List<Integer> availableMoves = emptyCells(gameState);

        for (Integer move : availableMoves) {
            gameState.set(move, player);
            int eval = minimaxAlphaBeta(gameState, depth - 1, alpha, beta, -player)[0];
            gameState.set(move, 0);

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
