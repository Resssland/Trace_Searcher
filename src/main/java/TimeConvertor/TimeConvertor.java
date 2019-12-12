package TimeConvertor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeConvertor {
    public static Calendar StringToCalendar(String date){
        Pattern pattern=Pattern.compile("\\d+");
        Matcher match =pattern.matcher(date);
        ArrayList<Integer> intDate=new ArrayList<Integer>();
        while (match.find()){
            intDate.add(Integer.parseInt(date.substring(match.start(),match.end())));
        }
        Calendar resultCalendar=new GregorianCalendar();
        resultCalendar.set(Calendar.YEAR,intDate.get(0));
        resultCalendar.set(Calendar.MONTH,intDate.get(1)-1);
        resultCalendar.set(Calendar.DAY_OF_MONTH,intDate.get(2));
        resultCalendar.set(Calendar.HOUR_OF_DAY,intDate.get(3));
        resultCalendar.set(Calendar.MINUTE,intDate.get(4));
        if(intDate.size()>6){
            resultCalendar.set(Calendar.SECOND,intDate.get(5));
            resultCalendar.set(Calendar.MILLISECOND,intDate.get(6));}
        return resultCalendar;
    }

    public static String CalendarToString (Calendar date){
        String strDate="";
        String month; if((date.get(Calendar.MONTH)+1)<10)
        {month="0"+(date.get(Calendar.MONTH)+1); } else{month=""+(date.get(Calendar.MONTH)+1);}

        String day; if(date.get(Calendar.DAY_OF_MONTH)<10)
        {day="0"+date.get(Calendar.DAY_OF_MONTH); } else{day=""+date.get(Calendar.DAY_OF_MONTH);}

        String hour; if(date.get(Calendar.HOUR_OF_DAY)<10)
        {hour="0"+date.get(Calendar.HOUR_OF_DAY); } else{hour=""+date.get(Calendar.HOUR_OF_DAY);}

        String minute; if(date.get(Calendar.MINUTE)<10)
        {minute="0"+date.get(Calendar.MINUTE); } else{minute=""+date.get(Calendar.MINUTE);}

        strDate+=date.get(Calendar.YEAR)+"-"+month+"-"+day+" "+hour+":"+minute;
        return strDate;
    }

    public static Calendar StickTogetherTime(Date day, Date time){
        Calendar calMain=Calendar.getInstance();
        calMain.setTime(day);

        Calendar calTemp=Calendar.getInstance();
        calTemp.setTime(time);

        calMain.set(Calendar.HOUR_OF_DAY,calTemp.get(Calendar.HOUR_OF_DAY));
        calMain.set(Calendar.SECOND,calTemp.get(Calendar.SECOND));
        calMain.set(Calendar.MINUTE,calTemp.get(Calendar.MINUTE));

        return calMain;
    }
}
