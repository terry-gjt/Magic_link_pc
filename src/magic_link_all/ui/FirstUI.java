package magic_link_all.ui;

import magic_link_all.common.Connection;
import magic_link_all.common.Hander;
import magic_link_all.common.ShotAndSend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Date;

import static magic_link_all.common.Safe.decipher;
import static magic_link_all.common.Safe.getfront;

public class FirstUI extends JFrame  implements ActionListener, Hander {
    private PipedOutputStream pos;
    private PipedInputStream pis;
    private Connection connection;
    private JButton SendMessageButton,ExitButton,AskButton,connect;
    private JTextField miyaotext,MessageText;
    private JLabel MessgaeShowArea;
    private JPanel c;
    private BufferedImage get;
    private JTabbedPane jtp;//一个放置很多份图片
    private int index;//一个一直会递增的索引,用于标认图片
    private JRadioButton UserButtun,AdminButtun;//User界面,Admin界面
    public FirstUI() {
        super("Magic_link(桌面版)");
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception exe){
            exe.printStackTrace();
        }
        initWindow();
    }
    private void initWindow(){
        c=new JPanel(new BorderLayout());//图片区面板
        c.setBackground(Color.BLACK);
        JLabel jl=new JLabel("请先输入密钥连接投影仪",JLabel.CENTER);
        jl.setFont(new Font("黑体",Font.BOLD,30));
        jl.setForeground(Color.GRAY);
        c.add(jl,BorderLayout.CENTER);

        JPanel MessgaeShowJp=new JPanel();//信息区面板

        MessgaeShowJp.setBackground(Color.white);
        MessgaeShowJp.setBorder(BorderFactory.createTitledBorder("信息："));
        MessgaeShowArea=new JLabel("-----------");
        MessgaeShowArea.setFont(new Font("黑体",Font.BOLD,20));
        MessgaeShowArea.setForeground(Color.black);
        MessgaeShowJp.add(MessgaeShowArea);

        JPanel ChooseJp=new JPanel();//单选区面板
        ChooseJp.add(UserButtun=new JRadioButton("用户登录",true));
        ChooseJp.add(AdminButtun=new JRadioButton("管理员登录"));
        UserButtun.addActionListener(this);
        AdminButtun.addActionListener(this);
        ChooseJp.setBorder(BorderFactory.createTitledBorder("登录方式"));
        ButtonGroup ChooseButtonGroup=new ButtonGroup();
        ChooseButtonGroup.add(UserButtun);
        ChooseButtonGroup.add(AdminButtun);

        JPanel LoginJP=new JPanel();//登录面板
        connect=new JButton("连接");
        connect.addActionListener(this);
        miyaotext=new JTextField(8);
        miyaotext.setText("257133");
        LoginJP.add(miyaotext);
        LoginJP.add(connect);
        LoginJP.setBorder(BorderFactory.createTitledBorder("请输入登录密钥"));

        JPanel buttonJP=new JPanel();//操作区面板
        SendMessageButton=new JButton("发送信息");
        AskButton=new JButton("请求投屏");
        ExitButton=new JButton("退出");
        SendMessageButton.addActionListener(this);
        AskButton.addActionListener(this);
        ExitButton.addActionListener(this);
        MessageText=new JTextField(8);
        MessageText.setText("你好啊");
        buttonJP.setLayout(new FlowLayout());
        buttonJP.add(MessageText);
        buttonJP.add(SendMessageButton);
        buttonJP.add(AskButton);
        buttonJP.add(ExitButton);
        buttonJP.setBorder(BorderFactory.createTitledBorder("操作区"));

        JPanel SouthJP=new JPanel();//几个区合并的面板
        SouthJP.add(ChooseJp);
        SouthJP.add(LoginJP);
        SouthJP.add(buttonJP);

        this.getContentPane().add(c,BorderLayout.CENTER);
        this.getContentPane().add(MessgaeShowJp,BorderLayout.EAST);
        this.getContentPane().add(SouthJP,BorderLayout.SOUTH);
        this.setSize(800,600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
//        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                FirstUI.this.setVisible(false);
                System.exit(0);
            }
        });
    }
    private void SendMessage(String s){
        if(connection!=null){
            connection.sendstring(s);
        }
    }
    public void StartConnect() {
        if(connection!=null)//连接中
        {
            connection.stopconnect();//关闭连接
            connect.setText("连接");
            miyaotext.setEnabled(true);
        }else
        {
            String ss=decipher(miyaotext.getText());
            connection=new Connection(ss,this);
            connection.startconnect();//打开连接
            connect.setText("断开");
            miyaotext.setEnabled(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object source=ae.getSource();
        if(source==SendMessageButton){
            String t=MessageText.getText();
            SendMessage(t);
        } else if(source==ExitButton){
            System.exit(0);
        }else if(source==AskButton){
            SendMessage("0001.,."+connection.getmynum());
        }else if(source==connect){
            StartConnect();
        }else if(source==UserButtun){
        }else if(source==AdminButtun){
        }
    }

    @Override
    public void receivemess(String ss) {
        String ff=getfront(ss);
        switch (ff){
            case "0002":
                Robot  r   = null;
                try {
                    r = new Robot();
                    System.out.println( "延时前:"+new Date().toString()  );
                    r.delay(   1000   );
                    System.out.println( "延时前:"+new Date().toString()  );
                } catch (Exception e) {
                    System.out.println( "线程异常:"+e.toString()  );
                }
                ShotAndSend shotter=new ShotAndSend(decipher(miyaotext.getText()));
                shotter.start();
                break;
            case "no":
                MessgaeShowArea.setText(ss);
                break;
        }
    }
}
