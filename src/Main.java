import magic_link_all.ui.FirstUI;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new FirstUI();
            }
        });
    }
}
