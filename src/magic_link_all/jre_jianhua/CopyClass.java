package magic_link_all.jre_jianhua;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
 
public class CopyClass{
    //此class用于精简jre中lib下的jar包,提取jar包中的class，需要首先将jar包解压成文件夹
	private static String source = "G:\\Magic_java\\jre1.8\\lib\\";		// 类源目录
	private static String dest = "G:\\Magic_java\\jrenew\\lib\\";		// 类拷贝目的目录
	private static String txturl = "G:\\Magic_java\\class_demo2.txt";		// 简化后txt的路径
    private static String[] jarArr = new String[]{"rt","charsets"};       //需要的提取的jar文件名
	/***
	 * 
	 * @param source 类源目录
	 * @param dest   类拷贝目的目录
	 * @param jarArr 需要的提取的jar文件名
	 */
	public CopyClass(String source,String dest,String[] jarArr){
		this.source=source;
		this.dest=dest;
		this.jarArr=jarArr;
	}
 
	public static void main(String[] args){
		CopyClass obj = new CopyClass(source,dest,jarArr);
		obj.readAndCopy(txturl);
    }
    
    /***
    * @param logName 提取class明细
    */
    public void readAndCopy(String logName){
        int count = 0;	// 用于记录成功拷贝的类数
        int wrong =0;
        try{
            FileInputStream fi = new FileInputStream(logName);
            InputStreamReader ir = new InputStreamReader(fi);
            BufferedReader br = new BufferedReader(ir);
            
            String string = br.readLine();
            while(string != null){
//                System.out.println("线程读取到。。。。。。。。。。。。："+string);
                if(copyClass(string) == true)
                count++;
                else{
                    wrong++;
                    System.out.println("线程读取"+string+"失败 "  + "已读取数量: "+ count  );
                }
                string = br.readLine();
            }
        }
        catch (IOException e){
        System.out.println("线程异常: " + e.toString());
        }
        System.out.println("成功拷贝数量: " + count+"失败数量："+wrong);
    }
    
    /***
    * 从原jar路径提取相应的类到目标路径，如将java/lang/CharSequence类从rt目录提取到rt1目录
    * @param string 提取类的全路径
    * @return
    * @throws IOException
    */
    public boolean copyClass(String string) throws IOException{
        int x=string.lastIndexOf("/");
        if (x==-1){return false;  //防止自己定义的类引起中断，如Main
        }
//        System.out.println("线程运行 copyClass：" + string + "" );
        String classDir = string.substring(0,x);
//        System.out.println("线程运行 classDir：" + classDir + "" );
        String className = string.substring(x+1,string.length()) + ".class";
//        System.out.println("线程运行 className：" + className + "" );
        boolean result =false;
        
        for(String jarname : jarArr){
            File srcFile = new File(source + "/"+jarname+"/" + classDir + "/" + className);
            if(!srcFile.exists()){
//                System.out.println("线程异常 " + jarname+"/" + classDir + "/" + className+"不存在" );
            continue;
            }
            byte buf[] = new byte[256];
            FileInputStream fin = new FileInputStream(srcFile);
            /* 目标目录不存在,创建 */
            File destDir = new File(dest + "/"+jarname+"1/" + classDir);
            if(!destDir.exists())
            destDir.mkdirs();
            File destFile = new File(destDir + "/" + className);
            FileOutputStream fout = new FileOutputStream(destFile);
            int len = 0;
            while((len = fin.read(buf)) != -1){
                fout.write(buf,0,len);
            }
            fout.flush();
            result = true;
            break;
        }
        return result;
    }
}
