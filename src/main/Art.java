import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class Art {
    public static final int NUM_LINES = 3000;
    public static final int LENGTH = 20;
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 700;
    public static final double SPACE_FACTOR = 2.5;
    public static final int[] XDIFF = { 1, 1, 0, -1, -1, -1, 0, 1 };
    public static final int[] YDIFF = { 0, -1, -1, -1, 0, 1, 1, 1 };
    public static final boolean CONSIDER_NODES = true;
    public static final String SAVE = "./resources/OctoArt.png";

    protected static record Line(int x1, int y1, int angle, boolean split) {
    }

    private static BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private static HashSet<Line> lines = new HashSet<>();
    private static LinkedList<Line> next = new LinkedList<>();
    private static HashSet<Pair<Integer, Integer>> starts = new HashSet<>();

    public static void paint() {
        Graphics g = bufferedImage.getGraphics();
        Line start = new Line(WIDTH / 2, HEIGHT / 2 - LENGTH, 270, true), hold;
        next.add(start);
        while (lines.size() < NUM_LINES && next.peek() != null) {
            hold = next.poll();
            draw(hold, g);
            generateNext(hold);
            lines.add(hold);
            if (CONSIDER_NODES) {
                Pair<Integer, Integer> pp = new Pair<Integer, Integer>(hold.x1, hold.y1);
                starts.add(pp);
            }
        }
        RenderedImage rendImage = bufferedImage;
        File file = new File(SAVE);
        try {
            ImageIO.write(rendImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void draw(Line line, Graphics g) {
        int x2 = line.x1 + XDIFF[line.angle / 45] * LENGTH;
        int y2 = line.y1 + YDIFF[line.angle / 45] * LENGTH;
        if ((line.angle / 45) % 2 == 0) {
            g.drawLine(line.x1, line.y1, x2, y2);
            return;
        }
        Line temp1, temp2, temp3, temp4;
        switch (line.angle) {
            case 45 -> {
                temp1 = new Line(line.x1, line.y1 - LENGTH, 315, true);
                temp2 = new Line(line.x1, line.y1 - LENGTH, 315, false);
                temp3 = new Line(line.x1 + LENGTH, line.y1, 135, true);
                temp4 = new Line(line.x1 + LENGTH, line.y1, 135, false);
            }
            case 135 -> {
                temp1 = new Line(line.x1, line.y1 - LENGTH, 225, true);
                temp2 = new Line(line.x1, line.y1 - LENGTH, 225, false);
                temp3 = new Line(line.x1 - LENGTH, line.y1, 45, true);
                temp4 = new Line(line.x1 - LENGTH, line.y1, 45, false);
            }
            case 225 -> {
                temp1 = new Line(line.x1, line.y1 + LENGTH, 135, true);
                temp2 = new Line(line.x1, line.y1 + LENGTH, 135, false);
                temp3 = new Line(line.x1 - LENGTH, line.y1, 315, true);
                temp4 = new Line(line.x1 - LENGTH, line.y1, 315, false);
            }
            case 315 -> {
                temp1 = new Line(line.x1, line.y1 + LENGTH, 45, true);
                temp2 = new Line(line.x1, line.y1 + LENGTH, 45, false);
                temp3 = new Line(line.x1 + LENGTH, line.y1, 225, true);
                temp4 = new Line(line.x1 + LENGTH, line.y1, 225, false);
            }
            default -> {
                return;
            }
        }
        if (lines.contains(temp1) || lines.contains(temp2) || lines.contains(temp3) || lines.contains(temp4)) {
            g.drawLine(line.x1, line.y1, (int) (line.x1 + (x2 - line.x1) / SPACE_FACTOR),
                    (int) (line.y1 + (y2 - line.y1) / SPACE_FACTOR));
            g.drawLine((int) (x2 - (x2 - line.x1) / SPACE_FACTOR), (int) (y2 - (y2 - line.y1) / SPACE_FACTOR), x2, y2);
        } else {
            g.drawLine(line.x1, line.y1, x2, y2);
        }
    }

    public static void generateNext(Line line) {
        int x2 = line.x1 + XDIFF[line.angle / 45] * LENGTH;
        int y2 = line.y1 + YDIFF[line.angle / 45] * LENGTH;
        Line temp1, temp2;
        if (x2 < 0 || x2 > WIDTH || y2 < 0 || y2 > HEIGHT) {
            return;
        }
        if (CONSIDER_NODES) {
            Pair<Integer, Integer> node = new Pair<Integer, Integer>(x2, y2);
            if (starts.contains(node)) {
                return;
            }
        }
        if (!line.split) {
            temp1 = new Line(x2, y2, (line.angle + 45) % 360, true);
            if (!lines.contains(temp1)) {
                next.add(temp1);
            }
            return;
        }
        temp1 = new Line(x2, y2, (line.angle - 45 + 360) % 360, true);
        temp2 = new Line(x2, y2, (line.angle + 45 + 360) % 360, false);
        if (!lines.contains(temp1)) {
            next.add(temp1);
        }
        if (!lines.contains(temp2)) {
            next.add(temp2);
        }
    }

    public static void main(String[] args) {
        paint();
    }
}