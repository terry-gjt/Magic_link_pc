//主要的截屏代码部分是：
//Robot ro=new Robot();
//Toolkit tk=Toolkit.getDefaultToolkit();
//Dimension di=tk.getScreenSize();
//Rectangle rec=new Rectangle(0,0,di.width,di.height);
//BufferedImage bi=ro.createScreenCapture(rec);

//(源代码来源于CSDN)

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CaptureScreen extends JFrame implements ActionListener{
    private JButton start,cancel,saveAll;
    private JPanel c;
    private BufferedImage get;
    private JTabbedPane jtp;//一个放置很多份图片
    private int index;//一个一直会递增的索引,用于标认图片
    private JRadioButton java,system;//JAVA界面,系统界面
    /** Creates a new instance of CaptureScreen */
    public CaptureScreen() {
        super("Magic_link(桌面版)");
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception exe){
            exe.printStackTrace();
        }
        initWindow();
        initOther();
        initTrayIcon();
    }
    private void initOther(){
        jtp=new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT);
    }
    private void initWindow(){
        start=new JButton("开始截取");
        saveAll=new JButton("保存所有");
        cancel=new JButton("退出");
        start.addActionListener(this);
        saveAll.addActionListener(this);
        cancel.addActionListener(this);
        JPanel buttonJP=new JPanel();
        c=new JPanel(new BorderLayout());
        c.setBackground(Color.BLACK);
        JLabel jl=new JLabel("屏幕截取",JLabel.CENTER);
        jl.setFont(new Font("黑体",Font.BOLD,40));
        jl.setForeground(Color.RED);
        c.add(jl,BorderLayout.CENTER);
        buttonJP.add(start);
        buttonJP.add(saveAll);
        buttonJP.add(cancel);
        buttonJP.setBorder(BorderFactory.createTitledBorder("公共操作区"));
        JPanel jp=new JPanel();//放两个单选按钮的面板
        jp.add(java=new JRadioButton("java界面"));
        jp.add(system=new JRadioButton("系统界面",true));
        java.addActionListener(this);
        system.addActionListener(this);
        jp.setBorder(BorderFactory.createTitledBorder("界面风格"));
        ButtonGroup bg=new ButtonGroup();
        bg.add(java);
        bg.add(system);
        JPanel all=new JPanel();
        all.add(jp);
        all.add(buttonJP);
        this.getContentPane().add(c,BorderLayout.CENTER);
        this.getContentPane().add(all,BorderLayout.SOUTH);
        this.setSize(500,400);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                CaptureScreen.this.setVisible(false);
            }
        });
    }
    private void initTrayIcon(){
        try{
            SystemTray st=SystemTray.getSystemTray();
            Image im=ImageIO.read(this.getClass().getResource("bg.gif"));
            PopupMenu pm=new PopupMenu("弹出菜单");
            pm.add(new MenuItem("关于")).addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    JOptionPane.showMessageDialog(CaptureScreen.this,"<html><Font color=red><center><h2>关于</h2></center></Font>" +
                            "这是一款纯JAVA的屏幕截取程序<br>在以前的基础上增加了一些常用的功能,<br>" +
                            "比如,批量保存,多幅截取,复制到系统粘帖板...<br>" +
                            "在使用过程中有任何问题,欢迎联系.<br>" +
                            "<Font size=5 color=blue>作者:terry<br>" +
                            "<i>QQ:3211616604</i><br></Font></html>");
                }
            });
            pm.addSeparator();
            pm.add(new MenuItem("显示主窗口")).addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    CaptureScreen.this.setVisible(true);
                }
            });
            pm.add(new MenuItem("开始截取")).addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    doStart();
                }
            });
            pm.add(new MenuItem("退出程序")).addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    System.exit(0);
                }
            });
            TrayIcon ti=new TrayIcon(im,"JAVA屏幕截取",pm);
            st.add(ti);
            ti.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae){
                    CaptureScreen.this.setVisible(true);
                }
            });
        }catch(Exception exe){
            exe.printStackTrace();
        }
    }
    private void updates(){
        this.setVisible(true);
        if(get!=null){
            //如果索引是0,则表示一张图片都没有被加入过,
            //则要清除当前的东西,重新把tabpane放进来
            if(index==0){
                c.removeAll();
                c.add(jtp,BorderLayout.CENTER);
            }else{//否则的话,直接对tabpane添加面板就可以了
                //就什么都不用做了
            }
            PicPanel pic=new PicPanel(get);
            jtp.addTab("图片"+(++index),pic);
            jtp.setSelectedComponent(pic);
            SwingUtilities.updateComponentTreeUI(c);
        }
    }
    private void doStart(){
        try{
            this.setVisible(false);
            Thread.sleep(500);//睡500毫秒是为了让主窗完全不见
            Robot ro=new Robot();
            Toolkit tk=Toolkit.getDefaultToolkit();
            Dimension di=tk.getScreenSize();
            Rectangle rec=new Rectangle(0,0,di.width,di.height);
            BufferedImage bi=ro.createScreenCapture(rec);
            JFrame jf=new JFrame();
            Temp temp=new Temp(jf,bi,di.width,di.height);
            jf.getContentPane().add(temp,BorderLayout.CENTER);
            jf.setUndecorated(true);
            jf.setSize(di);
            jf.setVisible(true);
            jf.setAlwaysOnTop(true);
        } catch(Exception exe){
            exe.printStackTrace();
        }
    }
    /**
     *公共方法,处理保存所有的图片
     */
    public void doSaveAll(){
        if(jtp.getTabCount()==0){
            JOptionPane.showMessageDialog(this,"图片不能为空!!","错误",JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFileChooser jfc=new JFileChooser(".");
        jfc.addChoosableFileFilter(new GIFfilter());
        jfc.addChoosableFileFilter(new BMPfilter());
        jfc.addChoosableFileFilter(new JPGfilter());
        jfc.addChoosableFileFilter(new PNGfilter());
        int i=jfc.showSaveDialog(this);
        if(i==JFileChooser.APPROVE_OPTION){
            File file=jfc.getSelectedFile();
            String about="PNG";
            String ext=file.toString().toLowerCase();
            javax.swing.filechooser.FileFilter ff=jfc.getFileFilter();
            if(ff instanceof JPGfilter){
                about="JPG";
            } else if(ff instanceof PNGfilter){
                about="PNG";
            }else if(ff instanceof BMPfilter){
                about="BMP";
            }else if(ff instanceof GIFfilter){
                about="GIF";
            }
            if(ext.endsWith(about.toLowerCase())){
                ext=ext.substring(0,ext.lastIndexOf(about.toLowerCase()));
            }
            //起一个线程去保存这些图片并显示出进度条
            new SaveAllThread(ext,about).setVisible(true);
        }
    }
    //专门用来保存所有图片的线程类,它还要显示出保存的进度条
    private class SaveAllThread extends JDialog implements Runnable{
        private String name;//文件名头部份
        private String ext;//文件格式
        private JProgressBar jpb;//一个进度条
        private JLabel info;// 一个信息显示条
        private int allTask,doneTask;//所有任务,已完成任务
        public SaveAllThread(String name,String ext){
            super(CaptureScreen.this,"保存",true);
            this.name=name;
            this.ext=ext;
            initWindow();
        }
        private void initWindow(){
            jpb=new JProgressBar();
            allTask=jtp.getTabCount();
            jpb.setMaximum(allTask);
            jpb.setMinimum(0);
            jpb.setValue(0);
            jpb.setStringPainted(true);
            setProgressBarString();
            info=new JLabel("正在保存到:");
            this.getContentPane().setBackground(Color.CYAN);
            this.add(info,BorderLayout.NORTH);
            this.add(jpb,BorderLayout.SOUTH);
            this.setUndecorated(true);
            this.setSize(300,100);
            this.setLocationRelativeTo(CaptureScreen.this);
            new Thread(this).start();
        }
        private void setProgressBarString(){
            jpb.setString(""+doneTask+"/"+allTask);
        }
        public void run(){
            try{
                for(int i=0;i<allTask;i++){
                    PicPanel pp=(PicPanel)jtp.getComponentAt(i);
                    BufferedImage image=pp.getImage();
                    File f= new File(name+(doneTask+1)+"."+ext.toLowerCase());
                    info.setText("<html><b>正在保存到:</b><br>"+f.toString()+"</html>");
                    ImageIO.write(image,ext,f);
                    doneTask++;
                    jpb.setValue(doneTask);
                    setProgressBarString();
                    Thread.sleep(500);
                }
                JOptionPane.showMessageDialog(this,"保存完毕!!");
                this.dispose();
            }catch(Exception exe){
                exe.printStackTrace();
                this.dispose();
            }
        }
    }
    /**
     *公用的处理保存图片的方法
     *这个方法不再私有了
     */
    public void doSave(BufferedImage get){
        try{
            if(get==null){
                JOptionPane.showMessageDialog(this,"图片不能为空!!","错误",JOptionPane.ERROR_MESSAGE);
                return;
            }
            JFileChooser jfc=new JFileChooser(".");
            jfc.addChoosableFileFilter(new GIFfilter());
            jfc.addChoosableFileFilter(new BMPfilter());
            jfc.addChoosableFileFilter(new JPGfilter());
            jfc.addChoosableFileFilter(new PNGfilter());
            int i=jfc.showSaveDialog(this);
            if(i==JFileChooser.APPROVE_OPTION){
                File file=jfc.getSelectedFile();
                String about="PNG";
                String ext=file.toString().toLowerCase();
                javax.swing.filechooser.FileFilter ff=jfc.getFileFilter();
                if(ff instanceof JPGfilter){
                    about="JPG";
                    if(!ext.endsWith(".jpg")){
                        String ns=ext+".jpg";
                        file=new File(ns);
                    }
                } else if(ff instanceof PNGfilter){
                    about="PNG";
                    if(!ext.endsWith(".png")){
                        String ns=ext+".png";
                        file=new File(ns);
                    }
                }else if(ff instanceof BMPfilter){
                    about="BMP";
                    if(!ext.endsWith(".bmp")){
                        String ns=ext+".bmp";
                        file=new File(ns);
                    }
                }else if(ff instanceof GIFfilter){
                    about="GIF";
                    if(!ext.endsWith(".gif")){
                        String ns=ext+".gif";
                        file=new File(ns);
                    }
                }
                if(ImageIO.write(get,about,file)){
                    JOptionPane.showMessageDialog(this,"保存成功！");
                } else
                    JOptionPane.showMessageDialog(this,"保存失败！");
            }
        } catch(Exception exe){
            exe.printStackTrace();
        }
    }
    /**
     *公共的处理把当前的图片加入剪帖板的方法
     */
    public void doCopy(final BufferedImage image){
        try{
            if(get==null){
                JOptionPane.showMessageDialog(this,"图片不能为空!!","错误",JOptionPane.ERROR_MESSAGE);
                return;
            }
            Transferable trans = new Transferable(){
                public DataFlavor[] getTransferDataFlavors() {
                    return new DataFlavor[] { DataFlavor.imageFlavor };
                }
                public boolean isDataFlavorSupported(DataFlavor flavor) {
                    return DataFlavor.imageFlavor.equals(flavor);
                }
                public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                    if(isDataFlavorSupported(flavor))
                        return image;
                    throw new UnsupportedFlavorException(flavor);
                }
            };
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans, null);
            JOptionPane.showMessageDialog(this,"已复制到系统粘帖板!!");
        }catch(Exception exe){
            exe.printStackTrace();
            JOptionPane.showMessageDialog(this,"复制到系统粘帖板出错!!","错误",JOptionPane.ERROR_MESSAGE);
        }
    }
    //处理关闭事件
    private void doClose(Component c){
        jtp.remove(c);
        c=null;
        System.gc();
    }
    public void actionPerformed(ActionEvent ae){
        Object source=ae.getSource();
        if(source==start){
            doStart();
        } else if(source==cancel){
            System.exit(0);
        }else if(source==java){
            try{
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(this);
            }catch(Exception exe){
                exe.printStackTrace();
            }
        }else if(source==system){
            try{
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(this);
            }catch(Exception exe){
                exe.printStackTrace();
            }
        }else if(source==saveAll){
            doSaveAll();
        }
    }
    //一个内部类,它表示一个面板,一个可以被放进tabpane的面板
//也有自己的一套处理保存和复制的方法
    private class PicPanel extends JPanel implements ActionListener{
        JButton save,copy,close;//表示保存,复制,关闭的按钮
        BufferedImage get;//得到的图片
        public PicPanel(BufferedImage get){
            super(new BorderLayout());
            this.get=get;
            initPanel();
        }
        public BufferedImage getImage(){
            return get;
        }
        private void initPanel(){
            save=new JButton("保存(S)");
            copy=new JButton("复制到剪帖板(C)");
            close=new JButton("关闭(X)");
            save.setMnemonic('S');
            copy.setMnemonic('C');
            close.setMnemonic('X');
            JPanel buttonPanel=new JPanel();
            buttonPanel.add(copy);
            buttonPanel.add(save);
            buttonPanel.add(close);
            JLabel icon=new JLabel(new ImageIcon(get));
            this.add(new JScrollPane(icon),BorderLayout.CENTER);
            this.add(buttonPanel,BorderLayout.SOUTH);
            save.addActionListener(this);
            copy.addActionListener(this);
            close.addActionListener(this);
        }
        public void actionPerformed(ActionEvent e) {
            Object source=e.getSource();
            if(source==save){
                doSave(get);
            }else if(source==copy){
                doCopy(get);
            }else if(source==close){
                get=null;
                doClose(this);
            }
        }
    }
    //保存BMP格式的过滤器
    private class BMPfilter extends javax.swing.filechooser.FileFilter{
        public BMPfilter(){
        }
        public boolean accept(File file){
            if(file.toString().toLowerCase().endsWith(".bmp")||
                    file.isDirectory()){
                return true;
            } else
                return false;
        }
        public String getDescription(){
            return "*.BMP(BMP图像)";
        }
    }
    //保存JPG格式的过滤器
    private class JPGfilter extends javax.swing.filechooser.FileFilter{
        public JPGfilter(){
        }
        public boolean accept(File file){
            if(file.toString().toLowerCase().endsWith(".jpg")||
                    file.isDirectory()){
                return true;
            } else
                return false;
        }
        public String getDescription(){
            return "*.JPG(JPG图像)";
        }
    }
    //保存GIF格式的过滤器
    private class GIFfilter extends javax.swing.filechooser.FileFilter{
        public GIFfilter(){
        }
        public boolean accept(File file){
            if(file.toString().toLowerCase().endsWith(".gif")||
                    file.isDirectory()){
                return true;
            } else
                return false;
        }
        public String getDescription(){
            return "*.GIF(GIF图像)";
        }
    }
    //保存PNG格式的过滤器
    private class PNGfilter extends javax.swing.filechooser.FileFilter{
        public boolean accept(File file){
            if(file.toString().toLowerCase().endsWith(".png")||
                    file.isDirectory()){
                return true;
            } else
                return false;
        }
        public String getDescription(){
            return "*.PNG(PNG图像)";
        }
    }
    //一个暂时类，用于显示当前的屏幕图像
    private class Temp extends JPanel implements MouseListener,MouseMotionListener{
        private BufferedImage bi;
        private int width,height;
        private int startX,startY,endX,endY,tempX,tempY;
        private JFrame jf;
        private Rectangle select=new Rectangle(0,0,0,0);//表示选中的区域
        private Cursor cs=new Cursor(Cursor.CROSSHAIR_CURSOR);//表示一般情况下的鼠标状态
        private States current= States.DEFAULT;// 表示当前的编辑状态
        private Rectangle[] rec;//表示八个编辑点的区域
        //下面四个常量,分别表示谁是被选中的那条线上的端点
        public static final int START_X=1;
        public static final int START_Y=2;
        public static final int END_X=3;
        public static final int END_Y=4;
        private int currentX,currentY;//当前被选中的X和Y,只有这两个需要改变
        private Point p=new Point();//当前鼠标移的地点
        private boolean showTip=true;//是否显示提示.如果鼠标左键一按,则提示不再显了
        public Temp(JFrame jf,BufferedImage bi,int width,int height){
            this.jf=jf;
            this.bi=bi;
            this.width=width;
            this.height=height;
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
            initRecs();
        }
        private void initRecs(){
            rec=new Rectangle[8];
            for(int i=0;i<rec.length;i++){
                rec[i]=new Rectangle();
            }
        }
        public void paintComponent(Graphics g){
            g.drawImage(bi,0,0,width,height,this);
            g.setColor(Color.RED);
            g.drawLine(startX,startY,endX,startY);
            g.drawLine(startX,endY,endX,endY);
            g.drawLine(startX,startY,startX,endY);
            g.drawLine(endX,startY,endX,endY);
            int x=startX<endX?startX:endX;
            int y=startY<endY?startY:endY;
            select=new Rectangle(x,y,Math.abs(endX-startX),Math.abs(endY-startY));
            int x1=(startX+endX)/2;
            int y1=(startY+endY)/2;
            g.fillRect(x1-2,startY-2,5,5);
            g.fillRect(x1-2,endY-2,5,5);
            g.fillRect(startX-2,y1-2,5,5);
            g.fillRect(endX-2,y1-2,5,5);
            g.fillRect(startX-2,startY-2,5,5);
            g.fillRect(startX-2,endY-2,5,5);
            g.fillRect(endX-2,startY-2,5,5);
            g.fillRect(endX-2,endY-2,5,5);
            rec[0]=new Rectangle(x-5,y-5,10,10);
            rec[1]=new Rectangle(x1-5,y-5,10,10);
            rec[2]=new Rectangle((startX>endX?startX:endX)-5,y-5,10,10);
            rec[3]=new Rectangle((startX>endX?startX:endX)-5,y1-5,10,10);
            rec[4]=new Rectangle((startX>endX?startX:endX)-5,(startY>endY?startY:endY)-5,10,10);
            rec[5]=new Rectangle(x1-5,(startY>endY?startY:endY)-5,10,10);
            rec[6]=new Rectangle(x-5,(startY>endY?startY:endY)-5,10,10);
            rec[7]=new Rectangle(x-5,y1-5,10,10);
            if(showTip){
                g.setColor(Color.CYAN);
                g.fillRect(p.x,p.y,170,20);
                g.setColor(Color.RED);
                g.drawRect(p.x,p.y,170,20);
                g.setColor(Color.BLACK);
                g.drawString("请按住鼠标左键不放选择截图区",p.x,p.y+15);
            }
        }
        //根据东南西北等八个方向决定选中的要修改的X和Y的座标
        private void initSelect(States state){
            switch(state){
                case DEFAULT:
                    currentX=0;
                    currentY=0;
                    break;
                case EAST:
                    currentX=(endX>startX?END_X:START_X);
                    currentY=0;
                    break;
                case WEST:
                    currentX=(endX>startX?START_X:END_X);
                    currentY=0;
                    break;
                case NORTH:
                    currentX=0;
                    currentY=(startY>endY?END_Y:START_Y);
                    break;
                case SOUTH:
                    currentX=0;
                    currentY=(startY>endY?START_Y:END_Y);
                    break;
                case NORTH_EAST:
                    currentY=(startY>endY?END_Y:START_Y);
                    currentX=(endX>startX?END_X:START_X);
                    break;
                case NORTH_WEST:
                    currentY=(startY>endY?END_Y:START_Y);
                    currentX=(endX>startX?START_X:END_X);
                    break;
                case SOUTH_EAST:
                    currentY=(startY>endY?START_Y:END_Y);
                    currentX=(endX>startX?END_X:START_X);
                    break;
                case SOUTH_WEST:
                    currentY=(startY>endY?START_Y:END_Y);
                    currentX=(endX>startX?START_X:END_X);
                    break;
                default:
                    currentX=0;
                    currentY=0;
                    break;
            }
        }
        public void mouseMoved(MouseEvent me){
            doMouseMoved(me);
            initSelect(current);
            if(showTip){
                p=me.getPoint();
                repaint();
            }
        }
        //特意定义一个方法处理鼠标移动,是为了每次都能初始化一下所要选择的地区
        private void doMouseMoved(MouseEvent me){
            if(select.contains(me.getPoint())){
                this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
                current= States.MOVE;
            } else{
                States[] st= States.values();
                for(int i=0;i<rec.length;i++){
                    if(rec[i].contains(me.getPoint())){
                        current=st[i];
                        this.setCursor(st[i].getCursor());
                        return;
                    }
                }
                this.setCursor(cs);
                current= States.DEFAULT;
            }
        }
        public void mouseExited(MouseEvent me){
        }
        public void mouseEntered(MouseEvent me){
        }
        public void mouseDragged(MouseEvent me){
            int x=me.getX();
            int y=me.getY();
            if(current== States.MOVE){
                startX+=(x-tempX);
                startY+=(y-tempY);
                endX+=(x-tempX);
                endY+=(y-tempY);
                tempX=x;
                tempY=y;
            }else if(current== States.EAST||current== States.WEST){
                if(currentX==START_X){
                    startX+=(x-tempX);
                    tempX=x;
                }else{
                    endX+=(x-tempX);
                    tempX=x;
                }
            }else if(current== States.NORTH||current== States.SOUTH){
                if(currentY==START_Y){
                    startY+=(y-tempY);
                    tempY=y;
                }else{
                    endY+=(y-tempY);
                    tempY=y;
                }
            }else if(current== States.NORTH_EAST||current== States.NORTH_EAST||
                    current== States.SOUTH_EAST||current== States.SOUTH_WEST){
                if(currentY==START_Y){
                    startY+=(y-tempY);
                    tempY=y;
                }else{
                    endY+=(y-tempY);
                    tempY=y;
                }
                if(currentX==START_X){
                    startX+=(x-tempX);
                    tempX=x;
                }else{
                    endX+=(x-tempX);
                    tempX=x;
                }
            }else{
                startX=tempX;
                startY=tempY;
                endX=me.getX();
                endY=me.getY();
            }
            this.repaint();
        }
        public void mousePressed(MouseEvent me){
            showTip=false;
            tempX=me.getX();
            tempY=me.getY();
        }
        public void mouseReleased(MouseEvent me){
            if(me.isPopupTrigger()){
                if(current== States.MOVE){
                    showTip=true;
                    p=me.getPoint();
                    startX=0;
                    startY=0;
                    endX=0;
                    endY=0;
                    repaint();
                } else{
                    jf.dispose();
                    updates();
                }
            }
        }
        public void mouseClicked(MouseEvent me){
            if(me.getClickCount()==2){
                //Rectangle rec=new Rectangle(startX,startY,Math.abs(endX-startX),Math.abs(endY-startY));
                Point p=me.getPoint();
                if(select.contains(p)){
                    if(select.x+select.width<this.getWidth()&&select.y+select.height<this.getHeight()){
                        get=bi.getSubimage(select.x,select.y,select.width,select.height);
                        jf.dispose();
                        updates();
                    }else{
                        int wid=select.width,het=select.height;
                        if(select.x+select.width>=this.getWidth()){
                            wid=this.getWidth()-select.x;
                        }
                        if(select.y+select.height>=this.getHeight()){
                            het=this.getHeight()-select.y;
                        }
                        get=bi.getSubimage(select.x,select.y,wid,het);
                        jf.dispose();
                        updates();
                    }
                }
            }
        }
    }
    public static void main(String args[]){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new CaptureScreen();
            }
        });
    }
}
//一些表示状态的枚举
enum States{
    NORTH_WEST(new Cursor(Cursor.NW_RESIZE_CURSOR)),//表示西北角
    NORTH(new Cursor(Cursor.N_RESIZE_CURSOR)),
    NORTH_EAST(new Cursor(Cursor.NE_RESIZE_CURSOR)),
    EAST(new Cursor(Cursor.E_RESIZE_CURSOR)),
    SOUTH_EAST(new Cursor(Cursor.SE_RESIZE_CURSOR)),
    SOUTH(new Cursor(Cursor.S_RESIZE_CURSOR)),
    SOUTH_WEST(new Cursor(Cursor.SW_RESIZE_CURSOR)),
    WEST(new Cursor(Cursor.W_RESIZE_CURSOR)),
    MOVE(new Cursor(Cursor.MOVE_CURSOR)),
    DEFAULT(new Cursor(Cursor.DEFAULT_CURSOR));
    private Cursor cs;
    States(Cursor cs){
        this.cs=cs;
    }
    public Cursor getCursor(){
        return cs;
    }
}

