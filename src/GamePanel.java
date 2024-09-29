import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;
import java.awt.Dimension;
import java.awt.Toolkit;
public class GamePanel extends JPanel implements ActionListener
{
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    final int SCREEN_WiDTH=(int)(screenSize.width*0.99);

    final int SCREEN_HEIGHT=(int)(screenSize.height*0.9);
    static final int UNIT_SIZE=25;
    final int GAME_UNITS=(SCREEN_WiDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY=100;
    final int x[]=new int[GAME_UNITS];
    final int y[]=new int[GAME_UNITS];
    int bodyParts=1;
    int ratEaten;
    int ratX;
    int ratY;
    char direction='L';
    boolean running= true;
    Timer timer;
    Random random;
    String playerName;
    int high_Score = 0;
    GamePanel() throws IOException     //constractor for gamepanel
    {
        random=new Random();
        this.setPreferredSize(new Dimension(SCREEN_WiDTH,SCREEN_HEIGHT));
        this.setBackground(Color.DARK_GRAY);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame() throws IOException {
        playerName= String.valueOf(loadPlayerName());
        playerName = JOptionPane.showInputDialog(this, "Enter your name:");
        saveHighScore(playerName,high_Score);

        newRat();
        running=true;
        timer=new Timer(DELAY,this);
        timer.start();
    }

    private int loadPlayerName() {
        return 0;
    }

    private void savePlayerName(String playerName) {

    }


    public int loadHighScore(String playerName) {
        try {
            Path path = Paths.get("highscores_" + playerName + ".txt");
            if (Files.exists(path)) {
                java.util.List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                for (String line : lines) {
                    String[] parts = line.split(":");
                    if (parts.length == 2 && parts[0].trim().equals(playerName)) {
                        return Integer.parseInt(parts[1].trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0; // Default value if no high score found for the player
    }


    // Save both player name and high score to the same file
    public void saveHighScore(String playerName, int score) {
        try {
            Path path = Paths.get("highscore.txt");
            String data = playerName + ":" + score + System.lineSeparator();
            Files.write(path, data.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] loadHighScore() {
        String[] highScoreData = new String[2]; // 0: Name, 1: Score
        try {
            Path path = Paths.get("highscore.txt");
            if (Files.exists(path)) {
                java.util.List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                if (!((java.util.List<?>) lines).isEmpty()) {
                    String[] parts = new String[0];
                    try {
                        ((java.util.List<?>) lines).get(0).wait(Long.parseLong(":"));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (parts.length == 2) {
                        highScoreData[0] = parts[0].trim(); // Player's name
                        highScoreData[1] = parts[1].trim(); // High score
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return highScoreData;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if(running) {
//        for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++){
//            g.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE,SCREEN_HEIGHT);
//            g.drawLine(0,i*UNIT_SIZE ,SCREEN_WiDTH,i*UNIT_SIZE);
//        }
            g.setColor(Color.GREEN);
            g.fillOval(ratX, ratY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(75, 18, 4));
//                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }//scoring here

            g.setColor(Color.white);
            g.setFont(new Font("Arial",Font.PLAIN,50));
            g.drawString("Score : "+ratEaten,1275,60);
        }
        else{
            gameOver(g);
        }
    }
    public void newRat(){
        ratX=random.nextInt((int)(SCREEN_WiDTH/UNIT_SIZE))*UNIT_SIZE;
        ratY=random.nextInt((int) (SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move(){
        for(int i=bodyParts;i>0;i--){
            x[i]=x[i-1];
            y[i]=y[i-1];

        }
        switch (direction){
            case 'U':
                y[0]=y[0]-UNIT_SIZE;
                break;
            case 'D':
                y[0]=y[0]+UNIT_SIZE;
                break;
            case 'L':
                x[0]=x[0]-UNIT_SIZE;
                break;
            case 'R':
                x[0]=x[0]+UNIT_SIZE;
                break;
        }
    }
    public void checkRat(){
        if((x[0]==ratX)&&(y[0]==ratY)){
            bodyParts++;
            ratEaten++;
            newRat();
        }
    }
    public void checkCollisions(){
        // checks if head touch its bodyparts
        for(int i=bodyParts;i>0;i--){
            if((x[0]==x[i])&&(y[0]==y[i])){
                running=false;
            }
        }
        // Check if the head touches any border
        if (x[0] < 0 || x[0] >= SCREEN_WiDTH || y[0] < 0 || y[0] >= SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }

    }

    public void gameOver(Graphics g){
        //score other page
        g.setColor(Color.green);
        g.setFont(new Font("Arial",Font.PLAIN,70));
        g.drawString("Score : "+ratEaten,500,90);
        //Game over text
        g.setColor(Color.RED);
        if(ratEaten>=20) {
            g.setFont(new Font("Arial",Font.PLAIN,75));
            g.drawString("WELL Played Game over",500,350);
        }else {
            g.setFont(new Font("Arial",Font.PLAIN,75));
            g.drawString("Game over",500,350);
        }
        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial",Font.PLAIN,75));
        g.drawString("Press SPACE to Restart",300,600);

        // Save the high score to a text file if a new high score was achieved
        g.setColor(Color.yellow);
        g.setFont(new Font("Arial",Font.PLAIN,50));
        g.drawString("Highest Score: "+high_Score,1100,60);
        if (ratEaten > high_Score) {
            high_Score = ratEaten;
            saveHighScore(playerName,high_Score);
        }
        g.setColor(Color.orange);
        g.setFont(new Font("Arial", Font.PLAIN, 50));
        g.drawString("Player: " + playerName, 30, 300);
        g.drawString("Your Score: " + ratEaten, 30, 400);


    }


    public void restart() throws IOException {
        if (ratEaten > high_Score) {
            high_Score = ratEaten;
            saveHighScore(playerName,high_Score);
        }
        // Reset the snake's position to the center of the screen
        x[0] = random.nextInt((SCREEN_WiDTH / UNIT_SIZE)) * UNIT_SIZE;
        y[0] = random.nextInt((SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        // Reset the rest of the snake's body
        for (int i = 1; i < bodyParts; i++) {
            x[i] = x[0]-i*UNIT_SIZE;
            y[i] = y[0];
        }
        Path path=Paths.get("highscore.txt");
        playerName = JOptionPane.showInputDialog(this, "Enter your name:");
        saveHighScore(playerName,high_Score);
        bodyParts = 1;
        ratEaten = 0;
        newRat();
        running = true;
        direction = 'R';
        timer.start();
        repaint();


    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkRat();
            checkCollisions();

        }
        repaint();
    }
    public  class MyKeyAdapter implements KeyListener {

        @Override
        public void keyPressed(KeyEvent e){

            if(e.getKeyCode()==KeyEvent.VK_SPACE){
                try {
                    restart();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction!='R'){
                        direction='L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction!='L'){
                        direction='R';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction!='U'){
                        direction='D';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction!='D'){
                        direction='U';
                    }
                    break;
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
            //timer.stop();
        }
    }
}
