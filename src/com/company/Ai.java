package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Ai {
    private int comp;
    private int human;
    private int depth;
    private int pobednayaDlinna;
    private ArrayList<ArrayList<Integer>> Corbinations;
    private int[] alp = {-1, -2000000};
    private int[] bet = {-1, 2000000};
    private Score ochenka;
    public Ai(int depth, int pobednayaDlinna, ArrayList<ArrayList<Integer>> Corbinations){
        this.comp = -1;
        this.human = 1;
        this.depth = depth;
        this.Corbinations = Corbinations;
        this.pobednayaDlinna =  pobednayaDlinna;
        this.ochenka = new Score(Corbinations, pobednayaDlinna);
    }
    public int getMove(ArrayList<Integer> pole){
        Integer kol = count(pole);
        if(depth > kol){
            depth = kol;
        }
        //System.out.println(ochenka.getOchenku(pole, comp));
        int[] hod = minimaxAlphaBeta(pole, depth, comp, alp, bet);
        System.out.println(hod[0] + " " + hod[1]);
        return hod[0];

    }

    private int count(ArrayList<Integer> pole){
        int c = 0;
        for(Integer i: pole){
            if(i == null){
                c++;
            }
        }
        return c;
    }

    private ArrayList<Integer> emptyCells(ArrayList<Integer> pole){
        ArrayList<Integer> pustie = new ArrayList<>(pole.size());
        for (int i = 0; i != pole.size(); i++) {
            if(pole.get(i) == null){
                pustie.add(i);
            }
        }
        return  pustie;
    }

    private boolean odinakovie(ArrayList<Integer> pole){
        if(pole.get(0) == null){
            return false;
        }
        int per = pole.get(0);
        for(int i = 1; i != pole.size(); i++){
            if(pole.get(i) == null || per != pole.get(i)){
                return false;
            }
        }
        return true;
    }

    private int score(int who, int player){
        if(who == comp){
            return 10000;
        } else if(who == human){
            return -10000;
        } else {
//            if(ochi != 0) {
//                if (player == comp) {
//                    return ochi;
//                } else {
//                    return -ochi;
//                }
//            }
            return 5;
        }
    }

    private int isWin(ArrayList<Integer> pole){
        ArrayList<Integer> cord = new ArrayList<>(pobednayaDlinna);
        for(ArrayList<Integer> wincomb: Corbinations){
            for(Integer tt: wincomb){
                cord.add(pole.get(tt));
            }
            if(odinakovie(cord)){
                return cord.get(0);
            }
            cord.clear();
        }
        return 0;
    }

    private int[] minimaxAlphaBeta(ArrayList<Integer> pole, int depth, int player,
                                       int[] alpha, int[] beta) {
        int who = isWin(pole);
        if(depth == 0 || who != 0){
            return new int[]{-1, score(who, player)};
        }
        if(player == comp){
            int[] best = new int[]{-1, -500000};
            for (Integer cell: emptyCells(pole)){
                pole.set(cell, player);
                int[] val = minimaxAlphaBeta(pole, depth - 1, -player, alpha, beta);
                pole.set(cell, null);
                val[0] = cell;
                if(val[1] > best[1]){
                    best = val;
                }
                if(best[1] >= alpha[1]){
                    alpha = best;
                }
                if(alpha[1] >= beta[1]){
                    break;
                }
            }
            return best;

        }else{
            int[] best = new int[]{-1, 500000};
            for (Integer cell: emptyCells(pole)){
                pole.set(cell, player);
                int[] val = minimaxAlphaBeta(pole, depth - 1, -player, alpha, beta);
                pole.set(cell, null);
                val[0] = cell;
                if(val[1] < best[1]){
                    best = val;
                }
                if(best[1] <= beta[1]){
                    beta = best;
                }
                if(alpha[1] >= beta[1]){
                    break;
                }
            }
            return best;
        }
    }



}
