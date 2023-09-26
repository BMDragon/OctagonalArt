import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Art extends Frame {
    public Art(){
        setVisible(true);
        setSize(1800, 700);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
    }
    public void paint(Graphics g)
    {
        g.drawLine(100, 100, 200, 200);
    }
  
    public static void main(String[] args)
    {
        new Art();
    }


}