import javax.swing.*;
import java.io.IOException;

public class GameFrame extends JFrame {

    GameFrame() throws IOException {
        //GamePanel panel=new GamePanel();
        this.add(new GamePanel());  //(panel());---using shortkey
        this.setTitle("Anaconda");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();// ---using other component as draw,gameover,...
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private void add(GamePanel gamePanel) {
    }
}
