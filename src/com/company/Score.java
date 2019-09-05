package com.company;

import java.util.ArrayList;

public class Score {
    private ArrayList<ArrayList<Integer>> Corbinations;
    private int pobednayaDlinna;
    public Score(ArrayList<ArrayList<Integer>> Corbinations, int pobednayaDlinna){
        this.Corbinations = Corbinations;
        this.pobednayaDlinna = pobednayaDlinna;
    }

    private int sravni(ArrayList<Integer> pole, ArrayList<Integer> comb, int player){
        int kol = 0;
        for (int i: comb){
            if(pole.get(i) != null && pole.get(i) == player){
                kol ++;
            }else if(pole.get(i) != null && pole.get(i) == -player){
                return 0;
            }
        }
        if (kol == pobednayaDlinna-1) {
            return 50;
        } else if(kol == pobednayaDlinna-2){
            return 25;
        }
        return 0;
    }

    public int getOchenku(ArrayList<Integer> pole, int player){
        int ochki = 0;
        ArrayList<Integer> cord = new ArrayList<>(pobednayaDlinna);
        for(ArrayList<Integer> comb: Corbinations){
            for(Integer tt: comb){
                cord.add(pole.get(tt));
            }
            int och = sravni(pole, comb, player);
            if(och != 0){
                ochki += och;
            }
            cord.clear();
        }
        return ochki;
    }


}
