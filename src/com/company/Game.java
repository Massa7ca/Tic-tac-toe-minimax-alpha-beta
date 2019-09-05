package com.company;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Game extends JFrame {
    private int x;
    private int y;
    private int razmerKvadrata;
    private int razmerPole;
    private int hod =  1;
    private final int pobednayaDlinna;
    private int cellCount;
    private ArrayList<ArrayList<Integer>> pole;
    private int deph;
    public ArrayList<ArrayList<Integer>> Corbinations = new ArrayList<>();
    Ai bot;
    public Game(int x, int y, int razmerPole, int razmerKvadrata, int deph, int pobednayaDlinna){
        this.x = x;
        this.y = y;
        this.razmerKvadrata = razmerKvadrata;
        this.razmerPole = razmerPole;
        this.cellCount = razmerPole * razmerPole;
        this.pole = new ArrayList<>(razmerPole * razmerPole);
        this.deph = deph;
        this.pobednayaDlinna = pobednayaDlinna;
        for (int y1 = 0; y1 != razmerPole; y1++) {
            for (int x1 = 0; x1 != razmerPole ; x1++) {
                pole.add(new ArrayList<>(Arrays.asList((razmerKvadrata * x1) + x,
                        (y1 * razmerKvadrata) + y, null)));
            }
        }
        winsCorbinations();
        initDraw();
        bot = new Ai(deph,  pobednayaDlinna, Corbinations);
        if(hod == -1){
            click(0, 0);
        }
    }


    private void winsCorbinations(){
        //Shitaet cordinati dlya diaganaley
        for (int prib = -1; prib != 3; prib += 2) {
            for (int i = 0; i != cellCount ; i++) {
                ArrayList<Integer> winsLine = new ArrayList<>();
                int cord = i, oldCord;
                winsLine.add(cord);
                for (int j = 0; j != razmerPole; j++) {
                    oldCord = cord;
                    cord += razmerPole + prib;
                    int nomerStroki = cord / razmerPole;
                    int oldNomerStroki2 = oldCord / razmerPole;
                    if (cord >= cellCount || nomerStroki  != oldNomerStroki2+1){
                        winsLine.clear();
                        break;
                    }
                    winsLine.add(cord);
                    if(winsLine.size() == pobednayaDlinna){
                        Corbinations.add((ArrayList)winsLine.clone());
                        winsLine.clear();
                    }
                }
            }
        }

        //Shitaet cordinati dlya gorizontaliy
        for (int i = 0; i != cellCount ; i++) {
            ArrayList<Integer> winsLine = new ArrayList<>();
            winsLine.add(i);
            for (int j = i+1; j != cellCount ; j++) {
                if(j % razmerPole == 0){
                    winsLine.clear();
                    break;
                }
                winsLine.add(j);
                if(winsLine.size() == pobednayaDlinna){
                    Corbinations.add((ArrayList)winsLine.clone());
                    winsLine.clear();
                }
            }
        }

        //Shitaet cordinati dlya wirtikaliy
        for (int i = 0; i != cellCount-(razmerPole*(pobednayaDlinna-1)) ; i++) {
            ArrayList<Integer> winsLine = new ArrayList<>();
            for (int j = 0; j != cellCount; j += razmerPole) {
                if(j+i >= cellCount){
                    break;
                }
                winsLine.add(j+i);
                if(winsLine.size() == pobednayaDlinna){
                    Corbinations.add((ArrayList)winsLine.clone());
                    winsLine.clear();
                }
            }
        }
        for(ArrayList<Integer> t : Corbinations){
            Collections.sort(t);
        }
        //System.out.println(Corbinations);
    }

    private void initDraw(){
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        add(panel);
        setSize(x + x + razmerKvadrata * razmerPole, y + y + razmerKvadrata * razmerPole);
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                click(e.getX(), e.getY());
                //System.out.println("Click position (X, Y):  " + e.getX() + ", " + e.getY());
            }
        });
    }

    private void click(int x, int y){
        int index = centrKletki(x, y);
        if(hod == 1 && pole.get(index).get(2) == null) {
            pole.get(index).set(2, 1);
            hod = -1;
            this.repaint();
        }

        long t = System.nanoTime();
        index = bot.getMove(getPole());
        System.out.println("Time " + ((System.nanoTime() - t) / 1000000));
        if(hod == -1) {
            pole.get(index).set(2, -1);
            hod = 1;
        }
//        else if(hod == -1 && pole.get(index).get(2) == null){
//            pole.get(index).set(2, -1);
//            hod = 1;
//        }
        this.repaint();

    }

    private ArrayList<Integer> getPole(){
        ArrayList<Integer> Pole = new ArrayList<>(cellCount);
        for(int i = 0; i != pole.size(); i ++){
            Pole.add(pole.get(i).get(2));
        }
        return Pole;
    }

    private int centrKletki(int x, int y){
        for (int i = 0; i != pole.size(); i++) {
            ArrayList<Integer> cell = pole.get(i);
            int x1 = cell.get(0);
            int y1 = cell.get(1);
            if(x1 < x && x < x1 + razmerKvadrata && y1 < y && y < y1 + razmerKvadrata){
                return i;
            }
        }
        return -1;
    }

    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setStroke(new BasicStroke(5));
        RenderingHints hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHints(hints);
        for (ArrayList<Integer> cell: pole) {
            int x = cell.get(0);
            int y = cell.get(1);
            g2D.drawRect(x, y, razmerKvadrata, razmerKvadrata);
            if (cell.get(2) != null && cell.get(2) == 1){
                g2D.setColor(Color.red);
                g2D.drawLine(x + 10, y + 10, x + razmerKvadrata - 10, y + razmerKvadrata - 10);
                g2D.drawLine(x + 10, y + razmerKvadrata - 10, x + razmerKvadrata - 10, y + 10);
                g2D.setColor(Color.black);
            }else if (cell.get(2) != null && cell.get(2) == -1){
                g2D.setColor(Color.blue);
                g2D.drawOval(x + 10, y + 10, razmerKvadrata - 20, razmerKvadrata - 20);
                g2D.setColor(Color.black);
            }

        }
    }
}
