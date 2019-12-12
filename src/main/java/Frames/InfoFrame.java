package Frames;

import javax.swing.*;
import java.awt.*;

public class InfoFrame {
    public InfoFrame(String info){
        JFrame jf =new JFrame("Info");
        JTextArea ja=new JTextArea(info);
        jf.add(ja);
        jf.pack();
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        jf.setLocation(width/2-jf.getWidth()/2,height/2-jf.getHeight()/2);
        jf.setVisible(true);

    }
}
