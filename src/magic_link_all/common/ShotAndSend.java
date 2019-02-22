package magic_link_all.common;

/**
 * Created by terry on 2018-03-19.
 */


import com.sun.imageio.plugins.jpeg.JPEG;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ShotAndSend extends Thread{
    private  boolean isConnecting=false;
    private Socket mSocketClient=null;
    public boolean sendrun=false;
    public OutputStream photoout;
    public String sIP="192.168.43.1";
    private String recvMessageServer = "";
    private TempInf tempInf;
    public ShotAndSend(String sIP ) {
//        this.tempInf=tempInf;
        this.sIP=sIP;
    }
    private void doStop(){
        sendrun=false;
    }
    @Override
    public void run() {
        try {
            mSocketClient = new Socket(sIP,55566);            //取得输入、输出流
            photoout=mSocketClient.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (sendrun){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("线程异常.发送线程"+e.getMessage());
            }
            try{
                Robot ro=new Robot();
                Toolkit tk=Toolkit.getDefaultToolkit();
                Dimension di=tk.getScreenSize();
                Rectangle rec=new Rectangle(0,0,di.width,di.height);
                BufferedImage bi=ro.createScreenCapture(rec);
                sendbyte(imagetobyte(bi));
            } catch(Exception exe){
                exe.printStackTrace();
            }
            System.out.println("线程异常.发送线程, 这里执行");
        }
    }
    private byte[] imagetobyte(Image image){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write((RenderedImage)image,"PNG",bos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] baobyte=bos.toByteArray();;
        return baobyte;
    }
    private Image bytetoimage(byte[] bytes){
        InputStream buffin = new ByteArrayInputStream(bytes,0,bytes.length);
        try {
            BufferedImage img = ImageIO.read(buffin);
            return img;
        } catch (IOException e) {
            System.out.println("其他异常："+e.getMessage());
            return null;
        }
    }
    private void sendbyte(byte[] baobyte){
        if (baobyte != null) {
            try {
                if (photoout != null) {
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                    byte[] baobyte = baos.toByteArray();
                    int len = baobyte.length;//读取bitmap压缩后大小
                    System.out.println("线程运行, len大小为"+len/1024+"kb");
                    String  filelenth = String.valueOf(len);//读取文件长度
                    byte[] filelenthb = filelenth.getBytes();//长度字符串转byte
                    byte[] filelenthb2 = new byte[8];//标准化8位
                    for (int i = 0; i < filelenthb.length; i++)
                    {
                        filelenthb2[i] = filelenthb[i];
                    }
                    String t = new String(filelenthb2);
//                    String b=t.trim();//去空格
//                    int l = Integer.parseInt(b);//这里显示看看
                    photoout.write(filelenthb2, 0, 8);//发送长度
                    photoout.flush();
                    System.out.println("线程运行, 长度标识已发送");
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, photoout);
                    photoout.flush();
                    System.out.println("线程运行, bitmap已发送");
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("线程异常.sendbyte, "+e.toString());
            }
        }
        else {
            System.out.println("线程异常.sendbyte, bitmap为空");
        }
    }
}
