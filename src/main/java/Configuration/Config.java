package Configuration;

import java.util.Calendar;

public class Config {
    public Config(String path){
        this.path=path;
    }

    private String user;
    private String mfPassword;
    private long msisdn;
    private int macroregion;
    private String host;

    private Calendar dateFrome;
    private Calendar dateTo;

    private String path;
    private String hostChecker="msk-lbrt-app01";
    private String bisService="service_gfcli.megafon.ru";

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
        return macroregion;
    }

    public void setMacroregion(int macroregion) {
        this.macroregion = macroregion;
        switch (macroregion){
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

    public String getHost() {
        return host;
    }
}

