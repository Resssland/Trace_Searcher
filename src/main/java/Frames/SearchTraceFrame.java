package Frames;

import Configuration.Config;
import Logger.Logger;
import SSH.SSHConector;
import TimeConvertor.TimeConvertor;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchTraceFrame {
    private Config configuration;
    private JTextField SubsField;
    private JXDatePicker DateFromePicker;
    private JSpinner DateFromeTimeSpinner;
    private JXDatePicker DateToPicker;
    private JSpinner DateToTimeSpinner;
    private JComboBox MacroregionsBox;
    private JTable ResultTable;
    private JButton StartButton;
    private DefaultTableModel model;
    private JButton GetInfoButton;
    private JButton GetBrtTrace;

    private ArrayList<Dictionary> vocabularyHRS;

    public SearchTraceFrame(Config configuration) {
        this.configuration=configuration;
        JFrame SearchTraceMainFraim = new JFrame("Search a trace");
        SearchTraceMainFraim.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        SearchTraceMainFraim.setBounds(gd.getDisplayMode().getWidth()/2-gd.getDisplayMode().getWidth()/6, gd.getDisplayMode().getHeight()/2-gd.getDisplayMode().getHeight()/4, gd.getDisplayMode().getWidth()/3, gd.getDisplayMode().getHeight()/2);
        JPanel FramePanel=new JPanel();
        FramePanel.setLayout(new BoxLayout(FramePanel,BoxLayout.Y_AXIS));
        SearchTraceMainFraim.add(FramePanel);
        //Top Panel
        JPanel TopPanel=new JPanel(new GridBagLayout());
        TopPanel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));

        JLabel SubsLabel=new JLabel("Msisdn and macro");
        SubsLabel.setFont(new Font("Arial Narrow",Font.BOLD,16));

        SubsField = new JTextField();
        String[] macroArray={"100","200","300","400","500","600","700","800"};
        MacroregionsBox =new JComboBox(macroArray);
        MacroregionsBox.setSelectedIndex(0);


        //JPanel CentralPanel=new JPanel();
        GridLayout CentralPanelLayout= new GridLayout(4,3,5,5);
        TopPanel.setLayout(CentralPanelLayout);
        TopPanel.setBorder(BorderFactory.createEmptyBorder(5,10,10,10));

        ////DateFrom
        JLabel DateFromeLabel=new JLabel("Date from:");
        DateFromeLabel.setFont(new Font("Arial Narrow",Font.BOLD,16));

        DateFromePicker = new JXDatePicker();
        Calendar previouseDay=Calendar.getInstance();
        previouseDay.add(Calendar.DAY_OF_MONTH, -1);
        DateFromePicker.setDate(previouseDay.getTime());
        DateFromePicker.setFormats(new SimpleDateFormat("dd.MM.yyyy"));

        DateFromeTimeSpinner = new JSpinner( new SpinnerDateModel() );
        JSpinner.DateEditor FromeTimeEditor = new JSpinner.DateEditor(DateFromeTimeSpinner, "HH:mm:ss");
        DateFromeTimeSpinner.setEditor(FromeTimeEditor);
        DateFromeTimeSpinner.setValue(new Date());
        ////DateTo
        DateToPicker = new JXDatePicker();
        DateToPicker.setDate(Calendar.getInstance().getTime());
        DateToPicker.setFormats(new SimpleDateFormat("dd.MM.yyyy"));

        DateToTimeSpinner = new JSpinner( new SpinnerDateModel() );
        JSpinner.DateEditor ToTimeEditor = new JSpinner.DateEditor(DateToTimeSpinner, "HH:mm:ss");
        DateToTimeSpinner.setEditor(ToTimeEditor);
        DateToTimeSpinner.setValue(new Date());



        JLabel DateToLabel=new JLabel("Date to:");
        DateToLabel.setFont(new Font("Arial Narrow",Font.BOLD,16));

        StartButton=new JButton("Start");
        SubsField.setFont(new Font("Arial Narrow",Font.PLAIN,16));

        TopPanel.add(SubsLabel);
        TopPanel.add(SubsField);
        TopPanel.add(MacroregionsBox);

        TopPanel.add(DateFromeLabel);

        TopPanel.add(DateFromePicker);
        TopPanel.add(DateFromeTimeSpinner);
        TopPanel.add(DateToLabel);

        TopPanel.add(DateToPicker);
        TopPanel.add(DateToTimeSpinner);

        TopPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        TopPanel.add(StartButton);

        FramePanel.add(TopPanel);



        //Central panel
        JPanel TablePanel = new JPanel(new GridLayout(1,1));

        model = new DefaultTableModel();

        ResultTable =new JTable(model);

        model.addColumn("Date");
        model.addColumn("Session_id");
        model.addColumn("HRS");
        model.addColumn("BRT");

        JScrollPane TablePane= new JScrollPane(ResultTable);

        ResultTable.setRowHeight(25);
        ResultTable.setFont(new Font("Arial Narrow",Font.PLAIN,15));
        ResultTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        TablePane.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));

        JLabel ResultListLabel =new JLabel("Result list");
        ResultListLabel.setFont(new Font("Arial Narrow",Font.BOLD,15));
        ResultListLabel.setHorizontalAlignment(JLabel.CENTER);

        TablePanel.add(ResultListLabel);

        FramePanel.add(TablePanel);
        FramePanel.add(TablePane);

        //Bottom panel
        JPanel BottomPanel =new JPanel();
        BottomPanel.setLayout(new GridLayout(1,3,20,20));

        GetInfoButton=new JButton("Get more info");
        GetBrtTrace =new JButton("Get BRT trace");
        JButton GetHrsTrace=new JButton("Get HRS trace");

        BottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        BottomPanel.add(GetInfoButton);
        BottomPanel.add(GetBrtTrace);
        BottomPanel.add(GetHrsTrace);
        FramePanel.add(BottomPanel);

        SearchTraceMainFraim.setVisible(true);

        StartButton.addActionListener(new StartButtonAct());
        GetInfoAndBrtTraceAct brtAct=new GetInfoAndBrtTraceAct();
        GetInfoButton.addActionListener(brtAct);
        GetBrtTrace.addActionListener(brtAct);
        GetHrsTrace.addActionListener(new GetHrsTrace());

    }

    public  class  StartButtonAct implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            long msisdn;
            try{
                Logger.log("----Start searching brt trace sessions----",null);
                msisdn= Long.parseLong(SubsField.getText());
                configuration.setMsisdn(msisdn);
                configuration.setDateFrome(
                        TimeConvertor.StickTogetherTime(
                                DateFromePicker.getDate(),((Date) DateFromeTimeSpinner.getValue())
                        )
                );
                configuration.setDateTo(TimeConvertor.StickTogetherTime(DateToPicker.getDate(),((Date) DateToTimeSpinner.getValue())));
                configuration.setMacroregion(Integer.parseInt((String)MacroregionsBox.getSelectedItem()));
                Logger.log("Macroregion of searching:"+configuration.getMacroregion(),null);
                Logger.log("Start searching period:"+configuration.getDateFrome().toString(),null);
                Logger.log("End searching period:"+configuration.getDateTo().toString(),null);
                GetTraceSessions(configuration);
                Logger.log("----End searching brt trace sessions----",null);


            }catch (NumberFormatException exp){
                JOptionPane.showMessageDialog(null,
                        "Incorrect msisdn.",
                        "Incorrect msisdn.",
                        JOptionPane.ERROR_MESSAGE);
            }
        }


        private void GetTraceSessions(Config config){
            String command="find /data/brt/BRT/current/logs/ -mindepth 1  -newermt '"+TimeConvertor.CalendarToString(config.getDateFrome())+"' ! -newermt '"+TimeConvertor.CalendarToString(config.getDateTo())+"' -exec zgrep '= "+config.getMsisdn()+"' {} \\; | grep hrs";
            Logger.log("Command of searching sessions:"+command,null);
            StartButton.setEnabled(false);
            ArrayList<String> rowSessions=new ArrayList<String>();
            if(config.getCurrentHosts()!=null && !config.getCurrentHosts().isEmpty()){
                for(int i=1;i<config.getCurrentHosts().size();i+=2)
                {
                    rowSessions.add(config.getCurrentHosts().get(i-1));//id
                    rowSessions.add(config.getCurrentHosts().get(i));//host
                    rowSessions.add(SSHConector.bashExecutor(config, config.getCurrentHosts().get(i), command, "brt"));//sessions
                    vocabularyHRS.add(GetVocabulary(configuration,config.getCurrentHosts().get(i)));
                }
            }
            else {
                rowSessions.add("M0");
                rowSessions.add(config.getMacro() + "-lbrt-app01");
                rowSessions.add(SSHConector.bashExecutor(config, config.getMacro() + "-lbrt-app01", command, "brt"));
                vocabularyHRS.add(GetVocabulary(configuration,config.getMacro() + "-lbrt-app02"));
                rowSessions.add("S0");
                rowSessions.add(config.getMacro() + "-lbrt-app02");
                rowSessions.add(SSHConector.bashExecutor(config, config.getMacro() + "-lbrt-app02", command, "brt"));
                vocabularyHRS.add(GetVocabulary(configuration,config.getMacro() + "-lbrt-app02"));
            }
            StartButton.setEnabled(true);
            LoadToTableRowSession(rowSessions);
        }

        private void LoadToTableRowSession(ArrayList<String> rowSessions){

            //Dictionary vocabularyHRSMaster=GetVocabulary(configuration,"-lbrt-app01");
            //Dictionary vocabularyHRSSlave=GetVocabulary(configuration,"-lbrt-app02");

            int SessionCount=0;
            int vocabularyIndex=0;
            model.setRowCount(0);
            Pattern pattern= Pattern.compile("20.+= "+configuration.getMsisdn()+"");
            for(int i=2;i<rowSessions.size();i+=3) {
                Matcher match = pattern.matcher(rowSessions.get(i));
                while (match.find()) {

                    String tempArr[] = rowSessions.get(i).substring(match.start(), match.end()).split(" ");
                    model.addRow(new Object[]{TimeConvertor.CalendarToString(TimeConvertor.StringToCalendar(tempArr[0] + " " + tempArr[1])), tempArr[5], vocabularyHRS.get(vocabularyIndex).get(tempArr[3].substring(5, tempArr[3].length() - 1)),rowSessions.get(i-2) });
                    SessionCount++;
                }
                vocabularyIndex++;
            }
            Logger.log("Count of session rows"+SessionCount,null);
            /*
            match =pattern.matcher(rowString2);
            while (match.find()){

                String tempArr[]=rowString2.substring(match.start(),match.end()).split(" ");
                model.addRow(new Object[]{TimeConvertor.CalendarToString(TimeConvertor.StringToCalendar(tempArr[0]+" "+tempArr[1])),tempArr[5],vocabularyHRSSlave.get(tempArr[3].substring(5,tempArr[3].length()-1)),"LS0"});
                slaveSessionCount++;
            }
            Logger.log("Count of session rows in slave:"+slaveSessionCount,null);
            */

        }

        private Dictionary GetVocabulary(Config conf,String host){
            String command="zgrep [OffPost]Rating /data/brt/BRT/current/conf/brt_srv.xml | cut -d '\\\"' -f '4 5 6'";
            String result=SSHConector.bashExecutor(conf,host,command,"brt");
            Dictionary resultVocabulary=new Hashtable();
            String[] arrayREsult=result.split("\n");
            for (int i=0;i<arrayREsult.length;i++){
                String[] tmp=arrayREsult[i].split("\"");
                resultVocabulary.put(tmp[0],tmp[2]);
            }
            return resultVocabulary;
        }





    }

    public class GetHrsTrace implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Logger.log("----Start searching hrs trace----",null);
            int selectedRowIndex= ResultTable.getSelectedRow();
            if(selectedRowIndex!=-1){
                //Convert Time
                Object selectedSession = ResultTable.getValueAt(selectedRowIndex, 1).toString().substring(1,ResultTable.getValueAt(selectedRowIndex, 1).toString().length()-1);
                Object selectedDate = ResultTable.getValueAt(selectedRowIndex, 0);
                Object instanceBRT = ResultTable.getValueAt(selectedRowIndex, 3);

                Calendar dateOfSession=TimeConvertor.StringToCalendar(selectedDate.toString());
                dateOfSession.add(Calendar.HOUR_OF_DAY,+3);
                Calendar dateFromeSearch=(Calendar)dateOfSession.clone();
                Calendar dateToSearch=(Calendar)dateOfSession.clone();

                dateFromeSearch.add(Calendar.MINUTE,-10);
                dateToSearch.add(Calendar.DAY_OF_MONTH,+1);
                String command="find $(ps -aux|grep  [h]rs | grep [d]aemon | grep -ow '[-]ext_log /.*'| cut -d ' ' -f2|rev|cut -c 12- | rev) -mindepth 1  -newermt '"+TimeConvertor.CalendarToString(dateFromeSearch)+"' ! -newermt '"+TimeConvertor.CalendarToString(dateToSearch)+"' -exec zgrep '"+selectedSession+"' {} \\; | tail -1 | cut -d ' ' -f8";


                Logger.log("Command of searching cdr number:"+command,null);
                String cdrNumber=SSHConector.bashExecutor(configuration,(String)ResultTable.getValueAt(selectedRowIndex,2),command,"hrs");
                command="find $(ps -aux|grep  [h]rs | grep [d]aemon | grep -ow '[-]ext_log /.*'| cut -d ' ' -f2|rev|cut -c 12- | rev) -mindepth 1  -newermt '"+TimeConvertor.CalendarToString(dateFromeSearch)+"' ! -newermt '"+TimeConvertor.CalendarToString(dateToSearch)+"' -exec zgrep '"+cdrNumber+" ' {} \\;";
                Logger.log("Command of searching hrs trace:"+command,null);
                Logger.log("Cdr number of hrs trace"+cdrNumber,null);

                if (cdrNumber.length()>2){SSHConector.bashExecutorInFile(configuration,(String)ResultTable.getValueAt(selectedRowIndex,2),command,"hrs_trace.txt","hrs");
                    JOptionPane.showMessageDialog(null,"HRS trace saved in hrs_trace.txt","HRS Trace",JOptionPane.INFORMATION_MESSAGE);
                    Logger.log("HRS trace saved in hrs_trace.txt",null);
                }
                else{ JOptionPane.showMessageDialog(null,"HRS trace not found","HRS Trace",JOptionPane.WARNING_MESSAGE);
                    Logger.log("Hrs trace didn't found",null);

                }

            }
            else{Logger.log("Session didn't choose",null);}
            Logger.log("----End searching hrs trace----",null);

        }
    }


    public class GetInfoAndBrtTraceAct implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Logger.log("----Start searching brt trace/info----",null);

            int selectedRowIndex= ResultTable.getSelectedRow();
            if(selectedRowIndex!=-1) {
                Object selectedSession = ResultTable.getValueAt(selectedRowIndex, 1).toString().substring(1,ResultTable.getValueAt(selectedRowIndex, 1).toString().length()-1);
                Object selectedDate = ResultTable.getValueAt(selectedRowIndex, 0);
                Object instanceBRT = ResultTable.getValueAt(selectedRowIndex, 3);

                Calendar dateOfSession=TimeConvertor.StringToCalendar(selectedDate.toString());
                dateOfSession.add(Calendar.HOUR_OF_DAY,+3);
                Calendar dateFromeSearch=(Calendar)dateOfSession.clone();
                Calendar dateToSearch=(Calendar)dateOfSession.clone();

                dateFromeSearch.add(Calendar.MINUTE,-10);
                dateToSearch.add(Calendar.MINUTE,+30);

                String command="";
                if(actionEvent.getSource()==GetBrtTrace) {
                    command = "find /data/brt/BRT/current/logs/ -mindepth 1  -newermt '" + TimeConvertor.CalendarToString(dateFromeSearch) + "' ! -newermt '" + TimeConvertor.CalendarToString(dateToSearch) + "' -exec zgrep '" + selectedSession + "' {} \\; ";
                    Logger.log("Command of searching brt trace:"+command,null);
                }
                if(actionEvent.getSource()==GetInfoButton){
                    command="find /data/brt/BRT/current/logs/ -mindepth 1  -newermt '"+TimeConvertor.CalendarToString(dateFromeSearch)+"' ! -newermt '"+TimeConvertor.CalendarToString(dateToSearch)+"' -exec zgrep '"+selectedSession+"' {} \\; | grep hrs | cut -d '\\\"' -f '3 4 5'";}
                Logger.log("Command of searching session info:"+command,null);

                if(instanceBRT.toString().equals("LS0")){
                    if(actionEvent.getSource()==GetBrtTrace) {
                        SSHConector.bashExecutorInFile(configuration,configuration.getMacro()+"-lbrt-app02",command,"brt_trace.txt","brt");
                        JOptionPane.showMessageDialog(null,"Brt trace saved in brt_trace.txt","BRT Trace",JOptionPane.INFORMATION_MESSAGE);
                        Logger.log("Host:"+configuration.getMacro()+"-lbrt-app02",null);

                    }
                    if(actionEvent.getSource()==GetInfoButton){
                        String result=SSHConector.bashExecutor(configuration,configuration.getMacro()+"-lbrt-app02",command,"brt");
                        Logger.log("Host:"+configuration.getMacro()+"-lbrt-app02",null);
                        new InfoFrame(result);
                    }
                }
                if(instanceBRT.toString().equals("LM0")){
                    if(actionEvent.getSource()==GetBrtTrace){
                        SSHConector.bashExecutorInFile(configuration,configuration.getMacro()+"-lbrt-app01",command,"brt_trace.txt","brt");
                        JOptionPane.showMessageDialog(null,"Brt trace saved in brt_trace.txt","BRT Trace",JOptionPane.INFORMATION_MESSAGE);
                        Logger.log("Host:"+configuration.getMacro()+"-lbrt-app01",null);

                    }
                    if(actionEvent.getSource()==GetInfoButton){
                        String result=SSHConector.bashExecutor(configuration,configuration.getMacro()+"-lbrt-app01",command,"brt");
                        Logger.log("Host:"+configuration.getMacro()+"-lbrt-app01",null);
                        new InfoFrame(result);
                    }
                }
            }

            Logger.log("----End searching hrs trace----",null);
        }

    }

}
