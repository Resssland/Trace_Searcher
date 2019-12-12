package Logger;

import Configuration.Config;
import javax.swing.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Logger {
    public Logger(){
        File file =new File(new Config(new java.io.File(".").getAbsolutePath()).getPath()+ "/log.txt");
        file.delete();
    }
    public static void log(String data,JFrame fr) {
        Calendar cal =GregorianCalendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        System.out.println("---->"+dateFormat.format(cal.getTime())+"  "+data);

        File file;
        try {
            file = new File(new Config(new java.io.File(".").getAbsolutePath()).getPath()+ "/log.txt");
            if (file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file, true);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("---->" + dateFormat.format(cal.getTime()) + "  " + data+"\n");
            bufferedWriter.close();
        }
        catch(IOException e){JOptionPane.showMessageDialog(fr,e.getMessage(),"IOException",JOptionPane.WARNING_MESSAGE);}

    }
}