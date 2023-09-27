import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.LinkedList;

public class Art extends Frame {
    public final int NUM_LINES = 700;
    public final int LENGTH = 40;
    public final int WIDTH = 1200;
    public final int HEIGHT = 700;
    public final double SPACE_FACTOR = 2.5;
    public final int[] XDIFF = { 1, 1, 0, -1, -1, -1, 0, 1 };
    public final int[] YDIFF = { 0, -1, -1, -1, 0, 1, 1, 1 };
    public final boolean CONSIDER_NODES = true;

    public Art() {
        setVisible(true);
        setSize(WIDTH, HEIGHT);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private static record Line(int x1, int y1, int angle, boolean split) {
    }

    private HashSet<Line> lines = new HashSet<>();
    private LinkedList<Line> next = new LinkedList<>();

    public void paint(Graphics g) {
        Line start = new Line(WIDTH / 2, HEIGHT / 2 - LENGTH, 270, true), hold;
        next.add(start);
        while (lines.size() < NUM_LINES) {
            hold = next.poll();
            draw(hold, g);
            generateNext(hold);
            lines.add(hold);
        }
    }

    public void draw(Line line, Graphics g) {
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

    public void generateNext(Line line) {
        int x2 = line.x1 + XDIFF[line.angle / 45] * LENGTH;
        int y2 = line.y1 + YDIFF[line.angle / 45] * LENGTH;
        Line temp1, temp2;
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
        new Art();
    }
}