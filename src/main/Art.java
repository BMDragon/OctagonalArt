import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.LinkedList;

public class Art extends Frame {
    public final int LENGTH = 50;
    public final int WIDTH = 1200;
    public final int HEIGHT = 700;
    public final int[] XDIFF = { 1, 1, 0, -1, -1, -1, 0, 1 };
    public final int[] YDIFF = { 0, -1, -1, -1, 0, 1, 1, 1 };

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
        while (lines.size() < 500) {
            hold = next.poll();
            draw(hold, g);
            generateNext(hold);
            lines.add(hold);
        }
    }

    public void draw(Line line, Graphics g) {
        int x2 = line.x1 + XDIFF[line.angle / 45] * LENGTH;
        int y2 = line.y1 + YDIFF[line.angle / 45] * LENGTH;
        g.drawLine(line.x1, line.y1, x2, y2);
    }

    public void generateNext(Line line) {
        int x2 = line.x1 + XDIFF[line.angle / 45] * LENGTH;
        int y2 = line.y1 + YDIFF[line.angle / 45] * LENGTH;
        Line temp1, temp2;
        if (line.split) {
            temp1 = new Line(x2, y2, (line.angle - 45 + 360) % 360, true);
            temp2 = new Line(x2, y2, (line.angle + 45 + 360) % 360, false);
            if (!lines.contains(temp1)) {
                next.add(temp1);
            }
            if (!lines.contains(temp2)) {
                next.add(temp2);
            }
        } else {
            temp1 = new Line(x2, y2, (line.angle + 45) % 360, true);
            if (!lines.contains(temp1)) {
                next.add(temp1);
            }
        }
    }

    public static void main(String[] args) {
        new Art();
    }
}