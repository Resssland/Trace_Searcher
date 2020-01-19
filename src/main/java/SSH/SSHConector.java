package SSH;

import Configuration.Config;
import com.jcraft.jsch.*;
import Logger.Logger;

import java.io.*;

public class SSHConector {

    public static boolean checkPassword(String password,String user,String host){
        try {
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            Session s = jsch.getSession(user, host, 22);
            s.setPassword(password);
            s.setConfig(config);

            s.connect();
            Channel ch =s.openChannel("exec");

            String command="whoami";
            ((ChannelExec)ch).setCommand(command);

            ((ChannelExec)ch).setErrStream(System.err);

            InputStream in=ch.getInputStream();
            ch.connect();

            byte[] tmp=new byte[1024];
            String result="";
            while(true){
                while(in.available()>0){
                    int i=in.read(tmp, 0, 1024);
                    if(i<0)break;
                    result=new String(tmp, 0, i);
                }

                if(ch.isClosed()){
                    //System.out.println("exit-status: "+ch.getExitStatus());
                    break;
                }
                try{Thread.sleep(1000);}catch(Exception ee){ee.printStackTrace();}
            }

            ch.disconnect();
            s.disconnect();
            Logger.log("Result of checking:"+result,null);
            return true;
        }
        catch(Exception e){Logger.log(e.getMessage(),null);
            return false;}

    }

    public static String bashExecutor(Config configuration, String host, String command,String sudoUser) {

        String result="";
        try{

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            Session s = jsch.getSession(configuration.getUser(), host, 22);
            s.setPassword(configuration.getMfPassword());
            s.setConfig(config);
            s.connect();
            Channel ch = s.openChannel("exec");
            String commando="echo -e \'"+configuration.getMfPassword()+"\\n\' | sudo -iSu "+sudoUser+" && sudo -iSu "+sudoUser+" bash -c \" "+command+" \" ";
            Logger.log("echo -e \'password\\n\' | sudo -iSu "+sudoUser+" && sudo -iSu "+sudoUser+" bash -c \" "+command+" \" ",null);
            ((ChannelExec) ch).setCommand(commando);
            ((ChannelExec) ch).setErrStream(System.err);
            InputStream in = ch.getInputStream();
            ch.connect();

            byte[] tmp = new byte[10240];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 10240);
                    if (i < 0) break;
                    result += new String(tmp, 0, i);
                }

                if (ch.isClosed()) {
                    Logger.log("Result of execution command:"+"exit-status: " + ch.getExitStatus(),null);
                    break;
                }
                try {Thread.sleep(1000);}
                catch (Exception ee) {return null;}
            }
            ch.disconnect();
            s.disconnect();
            return result;
        }
        catch(Exception e){e.printStackTrace(); return null;}




    }

    public static void bashExecutorInFile(Config configuration, String host, String command,String fileName,String sudoUser) {
        String data=bashExecutor(configuration,host,command,sudoUser);
        String path=new File(".").getAbsolutePath();
        File file =new File(path+"\\"+fileName);
        try {

            OutputStream os = new FileOutputStream(file);
            os.write(data.getBytes());
            os.close();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }

}
