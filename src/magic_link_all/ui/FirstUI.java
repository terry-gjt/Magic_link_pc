package magic_link_all.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class FirstUI extends JFrame  implements ActionListener{
    private JButton StartButton,cancel,AskButton,connect;
    private JTextField miyao;
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
        miyao=new JTextField(8);
        LoginJP.add(miyao);
        LoginJP.add(connect);
        LoginJP.setBorder(BorderFactory.createTitledBorder("请输入登录密钥"));

        JPanel buttonJP=new JPanel();//操作区面板
        StartButton=new JButton("选择截取区域");
        AskButton=new JButton("请求投屏");
        cancel=new JButton("退出");
        StartButton.addActionListener(this);
        AskButton.addActionListener(this);
        cancel.addActionListener(this);
        buttonJP.add(StartButton);
        buttonJP.add(AskButton);
        buttonJP.add(cancel);
        buttonJP.setBorder(BorderFactory.createTitledBorder("操作区"));

        JPanel SouthJP=new JPanel();//几个区合并的面板
        SouthJP.add(ChooseJp);
        SouthJP.add(LoginJP);
        SouthJP.add(buttonJP);

        this.getContentPane().add(c,BorderLayout.CENTER);
        this.getContentPane().add(SouthJP,BorderLayout.SOUTH);
        this.setSize(800,600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                FirstUI.this.setVisible(false);
                System.exit(0);
            }
        });
    }
    private void initButton(){

    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
