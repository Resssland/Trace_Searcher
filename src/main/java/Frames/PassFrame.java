package Frames;

import Logger.Logger;
import Configuration.Config;
import SSH.SSHConector;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PassFrame {

    private Config conf;
    private JPasswordField MFField;
    private JTextField UserField;
    private JFrame jf;
    private JTextField hostsFileField;

    public PassFrame(Config d){

        conf=d;
        jf = new JFrame("Enter passwords");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Logger log =new Logger();

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        jf.setSize(width/4,(int)(height/5.5f));
        jf.setLocation(width/2-jf.getWidth()/3,height/2-jf.getHeight()/4);
        Logger.log("Creating password frame", jf);

        JLabel UserLabel=new JLabel("  User");
        JLabel MFLabel =new JLabel("  MF Password");

        UserField=new JTextField("rkstarod");
        MFField = new JPasswordField();

        JButton StartButton=new JButton("Start");
        JButton ConfigButton=new JButton("Configuration");
        JButton hostFileButton =new JButton("Select a path to a hosts file");

        hostsFileField = new JTextField();

        hostFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser hostsFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                hostsFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int returnValue = hostsFileChooser.showOpenDialog(null);
                if(returnValue==JFileChooser.APPROVE_OPTION){
                    hostsFileField.setText(hostsFileChooser.getSelectedFile().getAbsolutePath());
                }

            }
        });


        JPanel mainPanel=new JPanel(new GridLayout(4,2,10,10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        mainPanel.add(UserLabel);
        mainPanel.add(UserField);
        mainPanel.add(MFLabel);
        mainPanel.add(MFField);
        //mainPanel.add(ConfigButton);
        mainPanel.add(hostFileButton);
        mainPanel.add(hostsFileField);

        mainPanel.add(new JPanel());
        mainPanel.add(StartButton);

        jf.add(mainPanel);
        jf.setVisible(true);

        StartButton.addActionListener(new StartButtonActClass());
        KeyStroke keyStroke = KeyStroke.getKeyStroke("pressed ENTER");
        MFField.getInputMap().put(keyStroke, new StartButtonActClass());
        UserField.getInputMap().put(keyStroke, new StartButtonActClass());

    }

    public class StartButtonActClass extends AbstractAction{

        public void actionPerformed(ActionEvent e) {

            try {
                Logger.log("----Start checking passwords----", jf);
                String user = UserField.getText();
                Logger.log("User:" + user, jf);

                if (SSHConector.checkPassword(new String(MFField.getPassword()), UserField.getText(), conf.getHostChecker()))
                {
                    Logger.log("MF password confirm", jf);
                    Logger.log("Attempt to start a searching frame", jf);
                    new SearchTraceFrame(conf);
                    conf.setUser(UserField.getText());
                    conf.setMfPassword(new String(MFField.getPassword()));
                    jf.dispose();
                }
                else{
                    Logger.log("MF password not confirm", jf);
                    MFField.setText("");
                }
                if(hostsFileField.getText()!=null && !hostsFileField.getText().equals("")) {
                    conf.setPath(hostsFileField.getText());
                    conf.parseFile();
                }


            }

            catch(Exception exp){JOptionPane.showMessageDialog(jf,
                    exp.getMessage(),
                    "Exception",
                    JOptionPane.WARNING_MESSAGE);
            }
        }
    }

}
