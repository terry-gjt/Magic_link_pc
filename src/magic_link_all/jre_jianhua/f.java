package magic_link_all.jre_jianhua;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class f {
    List<String> list;
    static String ss="G:\\Magic_java\\class.txt";
    public static void main(String[] args) {
        try {
            findJar(ss);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
    public static List<String> findJar (String txtPath) throws IOException {
        List<String> list = new ArrayList<>();
        Files.readAllLines(Paths.get(txtPath), Charset.forName ("UTF-8")). forEach(s ->{
            if (s.contains ("CtripPro_jar") && !list.contains(s.split("CtripPro_jar")[1])) {
                list. add(s. split("CtripPro_jar")[1]);
            }
        });
      return list;
    }
}
