package magic_link_all.jre_jianhua;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author terry_gjt@foxmail.com
 *  此class用于精简jre的lib，看程序用了哪几个jar
 * 	调用下面这个命令生成class.txt
 * 	java -jar -verbose G:\Magic_java\Magic_java.jar >> class.txt
 */
public class GetRarName {
	private static String sourse = "G:\\Magic_java\\class.txt";
	private static String outAddress = "G:\\Magic_java\\rar_need.txt";
 
	public static void main(String[] args) {
		Set<String> list = new HashSet<String>();//HashSet只存储不重复的值
		try {
			int count_L = 0;
			int loopCount = 0;
			// 源文件位置，打开它
			FileInputStream fin = new FileInputStream(sourse);
			InputStreamReader isr = new InputStreamReader(fin);
			BufferedReader br = new BufferedReader(isr);
			// 输出文件位置
			FileOutputStream fout = new FileOutputStream(outAddress);
			OutputStreamWriter osw = new OutputStreamWriter(fout);
			BufferedWriter bw = new BufferedWriter(osw);
			// 读一行
			String sp = br.readLine();
			// 只要没有读到文件尾就一直执行
			while (sp!=null ) {
				// 只读取以"jar]"结尾的行
				if (sp.substring(sp.length()-4).equals("jar]")) {
					int x = sp.lastIndexOf("lib");
					if (x != -1) {
						String jarname = sp.substring(x + 4, sp.length()-1) ;
//						System.out.println("线程运行 className：" + jarname + "" );
						list.add(jarname);
					}else System.out.println("线程运行，这条不是："+sp );
					// 这里在输出你的文件信息。加工后用于后续操作。

					// 程序需要的类文件数目。
					count_L++;
				}
				// 读行
				sp = br.readLine();
			}
			for (String i:list) {
				System.out.println(i);
				bw.write(i + '\n');;
			}

			// 两个测试输出
			System.out.println(count_L);
			System.out.println(loopCount);
			// 千万要把两个文件关闭！！！
			br.close();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}