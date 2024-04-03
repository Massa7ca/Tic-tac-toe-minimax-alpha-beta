package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class Game extends JFrame {
    private final int x;
    private final int y;
    private final int squareSize;
    private final int fieldSize;
    private int firstMove;
    private final int winLength;
    private final int cellCount;
    private final ArrayList<ArrayList<Integer>> field;
    public ArrayList<ArrayList<Integer>> winsCombinations = new ArrayList<>();
    private final Ai bot;
    public Game(int x, int y, int fieldSize, int squareSize, int depth, int winLength, int firstMove) {
        this.x = x;
        this.y = y;
        this.squareSize = squareSize;
        this.fieldSize = fieldSize;
        this.cellCount = fieldSize * fieldSize;
        this.field = new ArrayList<>(fieldSize);
        this.winLength = winLength;
        this.firstMove = firstMove;
        for (int y1 = 0; y1 != fieldSize; y1++) {
            for (int x1 = 0; x1 != fieldSize ; x1++) {
                field.add(new ArrayList<>(Arrays.asList((squareSize * x1) + x,
                        (y1 * squareSize) + y, 0)));
            }
        }
        generateWinsCombinations();
        initDraw();
        bot = new Ai(depth, winLength, winsCombinations);
        if (firstMove == -1) {
            click(0, 0);
        }
    }


    private void generateWinsCombinations() {
        //Shitaet cordinati dlya diaganaley
        for (int prib = -1; prib != 3; prib += 2) {
            for (int i = 0; i != cellCount ; i++) {
                ArrayList<Integer> winsLine = new ArrayList<>();
                int cord = i, oldCord;
                winsLine.add(cord);
                for (int j = 0; j != fieldSize; j++) {
                    oldCord = cord;
                    cord += fieldSize + prib;
                    int nomerStroki = cord / fieldSize;
                    int oldNomerStroki2 = oldCord / fieldSize;
                    if (cord >= cellCount || nomerStroki  != oldNomerStroki2+1){
                        winsLine.clear();
                        break;
                    }
                    winsLine.add(cord);
                    if(winsLine.size() == winLength){
                        winsCombinations.add(new ArrayList<>(winsLine));
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
                if(j % fieldSize == 0){
                    winsLine.clear();
                    break;
                }
                winsLine.add(j);
                if(winsLine.size() == winLength){
                    winsCombinations.add(new ArrayList<>(winsLine));
                    winsLine.clear();
                }
            }
        }

        //Shitaet cordinati dlya wirtikaliy
        for (int i = 0; i != cellCount-(fieldSize *(winLength -1)) ; i++) {
            ArrayList<Integer> winsLine = new ArrayList<>();
            for (int j = 0; j != cellCount; j += fieldSize) {
                if(j+i >= cellCount){
                    break;
                }
                winsLine.add(j+i);
                if(winsLine.size() == winLength){
                    winsCombinations.add(new ArrayList<>(winsLine));
                    winsLine.clear();
                }
            }
        }
        for (var com : winsCombinations) {
            Collections.sort(com);
        }

        winsCombinations = new ArrayList<>(new HashSet<>(winsCombinations));

    }

    private void initDraw() {
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        add(panel);
        setSize(x * 2 + squareSize * fieldSize, y * 2 + squareSize * fieldSize);
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                    click(e.getX(), e.getY());
            }
        });
    }

    private void click(int x, int y) {
        int index = centreCell(x, y);
        this.repaint();
        if (firstMove == 1 && index != -1 && field.get(index).get(2) == 0) {
            field.get(index).set(2, 1);
            firstMove = -1;
        }

        index = bot.getMove(getField());

        if (firstMove == -1 && index != -1 && field.get(index).get(2) == 0) {
            field.get(index).set(2, -1);
            firstMove = 1;
        }

    }

    private OptimizedHashArrayList<Integer> getField() {
        OptimizedHashArrayList<Integer> pole = new OptimizedHashArrayList<>();
        for(int i = 0; i != field.size(); i ++){
            pole.add(field.get(i).get(2));
        }
        return pole;
    }

    private int centreCell(int x, int y) {
        for (int i = 0; i != field.size(); i++) {
            ArrayList<Integer> cell = field.get(i);
            int x1 = cell.get(0);
            int y1 = cell.get(1);
            if(x1 < x && x < x1 + squareSize && y1 < y && y < y1 + squareSize){
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
        for (ArrayList<Integer> cell: field) {
            int x = cell.get(0);
            int y = cell.get(1);
            g2D.drawRect(x, y, squareSize, squareSize);
            if (cell.get(2) == 1){
                g2D.setColor(Color.red);
                g2D.drawLine(x + 10, y + 10, x + squareSize - 10, y + squareSize - 10);
                g2D.drawLine(x + 10, y + squareSize - 10, x + squareSize - 10, y + 10);
                g2D.setColor(Color.black);
            }else if (cell.get(2) == -1){
                g2D.setColor(Color.blue);
                g2D.drawOval(x + 10, y + 10, squareSize - 20, squareSize - 20);
                g2D.setColor(Color.black);
            }

        }
    }
}
