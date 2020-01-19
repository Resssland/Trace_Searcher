package Configuration;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {
    public Config(String path){
        this.path=path;
        hosts=new ArrayList<ArrayList<String>>(
                Arrays.asList(
                        new ArrayList<String>(),
                        new ArrayList<String>(),
                        new ArrayList<String>(),
                        new ArrayList<String>(),
                        new ArrayList<String>(),
                        new ArrayList<String>(),
                        new ArrayList<String>()
                )
        );


    }
    private ArrayList<ArrayList<String>> hosts=new ArrayList<ArrayList<String>>();
    private String user;
    private String mfPassword;
    private long msisdn;
    private int macroregionId=0;
    private String host;

    private Calendar dateFrome;
    private Calendar dateTo;

    private String path;
    private String hostChecker="msk-lbrt-app01";
    private String bisService="service_gfcli.megafon.ru";

    private ArrayList<ArrayList<String>> macroHosts=new ArrayList<ArrayList<String>>();

    public String getPath(){return  path;}

    public String getHostChecker(){return hostChecker;}

    public String getBisService(){return bisService;}

    public void setPath(String newPath){path=newPath;}

    public void setHostChecker(String newHostChecker){hostChecker=newHostChecker;}

    public void setBisService(String newBisService){bisService=newBisService;}

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMfPassword() {
        return mfPassword;
    }

    public void setMfPassword(String mfPassword) {
        this.mfPassword = mfPassword;
    }

    public long getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(long msisdn) {
        this.msisdn = msisdn;
    }

    public int getMacroregion() {
        return macroregionId;
    }

    public void setMacroregion(int macroregion) {
        this.macroregionId = macroregion;
        switch (macroregionId){
            case 100:host="nw";break;
            case 200:host="msk";break;
            case 300:host="msk";break;
            case 400:host="kvk";break;
            case 500:host="vlg";break;
            case 600:host="url";break;
            case 700:host="sib";break;
            case 800:host="dv";break;
        }
    }

    public Calendar getDateTo() {
        return dateTo;
    }

    public void setDateTo(Calendar dateTo) {
        this.dateTo = dateTo;
    }

    public Calendar getDateFrome() {
        return dateFrome;
    }

    public void setDateFrome(Calendar dateFrome) {
        this.dateFrome = dateFrome;
    }

    public String getMacro() {
        return host;
    }

    public void parseFile(){
        File hostsFile=new File(getPath());
        char[] rowHosts=new char[1000];
        if(hostsFile.exists()){
            try {
                BufferedReader reader = new BufferedReader(new FileReader(hostsFile));
                reader.read(rowHosts);
                reader.close();
            }
            catch (IOException e){e.printStackTrace();}
            System.out.println(new String(rowHosts));
            Pattern rowPattern=Pattern.compile("<\\s*macro\\s*=\\s*\"\\w+\"\\s*\\,\\s*id\\s*=\".+\"\\s*\\,\\s*host\\s*=\\s*\".+\"\\s*>");
            Matcher rowMatch =rowPattern.matcher(new String(rowHosts));
            while(rowMatch.find()){
                String[] tempString=(new String(rowHosts)).substring(rowMatch.start(),rowMatch.end()).split(",");
                String macro=tempString[0].split("\"")[1];
                String id=tempString[1].split("\"")[1];
                String host=tempString[2].split("\"")[1];
                switch (macro.toCharArray()[0]){
                    case 'n': { hosts.get(0).add(id);hosts.get(0).add(host); break;}
                    case 'm': { hosts.get(1).add(id);hosts.get(1).add(host); break;}
                    case 'k': { hosts.get(2).add(id);hosts.get(2).add(host); break;}
                    case 'v': { hosts.get(3).add(id);hosts.get(3).add(host); break;}
                    case 'u': { hosts.get(4).add(id);hosts.get(4).add(host); break;}
                    case 's': { hosts.get(5).add(id);hosts.get(5).add(host); break;}
                    case 'd': { hosts.get(6).add(id);hosts.get(6).add(host); break;}
                    default:break;
                }
            }


        }
    }

    public ArrayList<String> getCurrentHosts(){
        if(macroregionId!=0){
            switch (macroregionId){
                case 100:return hosts.get(0);
                case 200:return hosts.get(1);
                case 300:return hosts.get(1);
                case 400:return hosts.get(2);
                case 500:return hosts.get(3);
                case 600:return hosts.get(4);
                case 700:return hosts.get(5);
                case 800:return hosts.get(6);
            }

        }
        return null;
    }




}

