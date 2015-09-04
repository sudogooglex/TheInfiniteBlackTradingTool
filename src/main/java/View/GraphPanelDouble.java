package View;

import Model.Conf;
import Model.ItemDB;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class GraphPanelDouble extends JPanel {

    private final int width = 800;
    private final int heigth = 400;
    private final int padding = 40;
    private final int labelPadding = 25;
    private final Color lineColor1 = new Color(44, 102, 230, 180);
    private final Color lineColor2 = new Color(44, 240, 30, 240);
    private final Color pointColor = new Color(100, 100, 100, 180);
    private final Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private final int pointWidth = 4;
    private final int numberYDivisions = 10;
    private List<Long> scores1;
    private List<Long> scores2;

    public GraphPanelDouble(List<Long> scores1, List<Long> scores2, int ng) {
        this.scores1 = scores1;
        this.scores2 = scores2;
    }

    GraphPanelDouble() {
        scores1 = new ArrayList<>();
        scores2 = new ArrayList<>();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (scores1.size() - 1);
//        double max = Math.max(getMaxScore(scores1), getMaxScore(scores2)),
//                min = Math.min(getMinScore(scores1), getMinScore(scores2));
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxScore() - getMinScore());
//        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (max - min);

        // 1.
        List<Point> graphPoints1 = new ArrayList<>();
        for (int i = 0; i < scores1.size(); i++) {
            int x1 = (int) (i * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxScore() - scores1.get(i)) * yScale + padding);
            graphPoints1.add(new Point(x1, y1));
        }

        // 2.
        List<Point> graphPoints2 = new ArrayList<>();
        for (int i = 0; i < scores1.size(); i++) {
            int x1 = (int) (i * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxScore() - scores2.get(i)) * yScale + padding);
            graphPoints2.add(new Point(x1, y1));
        }

        // draw white background
        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLACK);

        // create hatch marks and grid lines for y axis.
        for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
            int y1 = y0;
            if (scores1.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                g2.setColor(Color.BLACK);
                double axisValue = (((getMinScore(scores1) + (getMaxScore()
                        - getMinScore()) * ((i * 1.0) / numberYDivisions)) * 100))
                        / (100.0 * 1_000);
                double yValue = ((int) ((getMinScore(scores1) + (getMaxScore() - getMinScore()) * ((i * 1.0) / numberYDivisions)) * 100)) / 100000.0;
                int yValueInt = (int)Math.round(yValue);
                String yLabel = String.format("%,d", yValueInt) + " k";
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        // 1. hatch marks and grid lines for x axis
        for (int i = 0; i < scores1.size(); i++) {
            if (scores1.size() > 1) {
                int x0 = i * (getWidth() - padding * 2 - labelPadding) / (scores1.size() - 1) + padding + labelPadding;
                int x1 = x0;
                int y0 = getHeight() - padding - labelPadding;
                int y1 = y0 - pointWidth;
                if ((i % ((int) ((scores1.size() / 20.0)) + 1)) == 0) {
                    g2.setColor(gridColor);
                    g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                    g2.setColor(Color.BLACK);
                    String xLabel = i + "";
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                }
                g2.drawLine(x0, y0, x1, y1);
            }
        }

        // 2.hatch marks and grid lines for x axis
        for (int i = 0; i < scores2.size(); i++) {
            if (scores2.size() > 1) {
                int x0 = i * (getWidth() - padding * 2 - labelPadding) / (scores2.size() - 1) + padding + labelPadding;
                int x1 = x0;
                int y0 = getHeight() - padding - labelPadding;
                int y1 = y0 - pointWidth;
                if ((i % ((int) ((scores2.size() / 20.0)) + 1)) == 0) {
                    g2.setColor(gridColor);
                    g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                    g2.setColor(Color.BLACK);
                    String xLabel = String.valueOf(i);
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                }
                g2.drawLine(x0, y0, x1, y1);
            }
        }

        // 1. create x and y axes 
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);

        Stroke oldStroke = g2.getStroke();
        g2.setColor(lineColor1);
        g2.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints1.size() - 1; i++) {
            int x1 = graphPoints1.get(i).x;
            int y1 = graphPoints1.get(i).y;
            int x2 = graphPoints1.get(i + 1).x;
            int y2 = graphPoints1.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setStroke(oldStroke);
        g2.setColor(pointColor);
        for (Point graphPoints11 : graphPoints1) {
            int x = graphPoints11.x - pointWidth / 2;
            int y = graphPoints11.y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        }

        // 2. create x and y axes 
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);

        Stroke oldStroke2 = g2.getStroke();
        g2.setColor(lineColor2);
        g2.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints2.size() - 1; i++) {
            int x1 = graphPoints2.get(i).x;
            int y1 = graphPoints2.get(i).y;
            int x2 = graphPoints2.get(i + 1).x;
            int y2 = graphPoints2.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setStroke(oldStroke);
        g2.setColor(pointColor);
        for (Point graphPoints21 : graphPoints2) {
            int x = graphPoints21.x - pointWidth / 2;
            int y = graphPoints21.y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        }
    }

//    @Override
//    public Dimension getPreferredSize() {
//        return new Dimension(width, heigth);
//    }
    private Long getMinScore(List<Long> scores) {
        Long minScore = Long.MAX_VALUE;
        for (Long score : scores) {
            minScore = Math.min(minScore, score);
        }
        return minScore;
    }

    private Long getMaxScore(List<Long> scores) {
        Long maxScore = Long.MIN_VALUE;
        for (Long score : scores) {
            maxScore = Math.max(maxScore, score);
        }
        return maxScore;
    }

    private Long getMaxScore() {
        Long maxScore1 = Long.MIN_VALUE, maxScore2 = Long.MIN_VALUE, maxScore;
        for (Long score : scores1) {
            maxScore1 = Math.max(maxScore1, score);
        }
        for (Long score : scores2) {
            maxScore2 = Math.max(maxScore2, score);
        }
        maxScore = Math.max(maxScore1, maxScore2);

        return maxScore;
    }

    private Long getMinScore() {
        Long minScore1 = Long.MAX_VALUE, minScore2 = Long.MAX_VALUE, minScore;
        for (Long score : scores1) {
            minScore1 = Math.min(minScore1, score);
        }
        for (Long score : scores2) {
            minScore2 = Math.min(minScore2, score);
        }
        minScore = Math.min(minScore1, minScore2);

        return minScore;
    }

    public void setScores(List<Long> scores) {
        scores1 = scores;
        invalidate();
        this.repaint();
    }

    public List<Long> getScores() {
        return scores1;
    }

    public void updatePriceGraph(List<ItemDB> data, String rarity) {
        Long pricePondereted;

        scores1 = new ArrayList<>();
        scores2 = new ArrayList<>();
        for (ItemDB id : data) {
            if (rarity.equals(id.getRarity())) {
//                // DOUBLE GRAPH :
//                 scores1.add(id.getPrice());
//                 scores2.add(id.getDurability());
                pricePondereted = id.getDurability() * id.getPrice() / 100;
                scores2.add(pricePondereted);
                scores1.add(id.getPrice());
            }
        }
        setPreferredSize(new Dimension(Conf.GRAPHL, Conf.GRAPHH));
    }
}
