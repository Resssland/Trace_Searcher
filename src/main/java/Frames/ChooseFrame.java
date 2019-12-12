package Frames;

import javax.swing.*;
import java.awt.*;

public class ChooseFrame {

    public ChooseFrame(){
        //Initializing  Frame
        JFrame jf=new JFrame();
        jf.setDefaultCloseOperation(jf.EXIT_ON_CLOSE);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();

        jf.setBounds(width/2-250,height/2-250,width/4,height/2);
        jf.setVisible(true);
        jf.setLayout(null);

        //Initializing buttons
        JButton ReratButton=new JButton("Перетарификация и поиск трассировки");
        JButton SearchButton=new JButton("Поиск трассировки");
        JButton  ConfigButton=new JButton("Конфигурация");
        JButton BackButton=new JButton("B");

        ReratButton.setBounds((jf.getWidth()-jf.getWidth()*2/3)/2,jf.getHeight()/10,jf.getWidth()*2/3,jf.getHeight()/10);
        SearchButton.setBounds((jf.getWidth()-jf.getWidth()*2/3)/2,3*jf.getHeight()/10,jf.getWidth()*2/3,jf.getHeight()/10);
        ConfigButton.setBounds((jf.getWidth()-jf.getWidth()*2/3)/2,5*jf.getHeight()/10,jf.getWidth()*2/3,jf.getHeight()/10);
        BackButton.setBounds((jf.getWidth()-jf.getWidth()*2/3)/2,7*jf.getHeight()/10,jf.getWidth()/10,jf.getHeight()/10);

        //Add Buttons on the frame
        jf.add(ReratButton);
        jf.add(SearchButton);
        jf.add(ConfigButton);
        jf.add(BackButton);

        //Show the frame
        jf.setVisible(true);


    }
}
