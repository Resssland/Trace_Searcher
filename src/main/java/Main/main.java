package Main;


import Logger.Logger;
import Configuration.Config;
import Frames.PassFrame;

import javax.swing.*;

public class main {
    public static void main(String[] Args){

        try {
            new PassFrame(new Config(new java.io.File(".").getAbsolutePath()));
        }
        catch (Exception e){JOptionPane.showMessageDialog(null,
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }

    }
}
