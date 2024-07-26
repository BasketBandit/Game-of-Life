import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Game extends Canvas {
    private BufferedImage image = new BufferedImage(1920/2, 1080/2, BufferedImage.TYPE_INT_ARGB);
    private BufferedImage transientImage = new BufferedImage(1920/2, 1080/2, BufferedImage.TYPE_INT_ARGB);;
    private final int white = Color.WHITE.getRGB();
    private final int black = Color.BLACK.getRGB();
    //private final int generations = 10000;
    private int population = 0;
    private int generation = 1;

    private final JFrame frame = new JFrame("Game of Life");

    public Game() {
        Random random = new Random();
        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                if(random.nextInt() % 2 == 0) {
                    image.setRGB(x, y, white);
                    transientImage.setRGB(x, y, white);
                    population++;
                }  else {
                    image.setRGB(x, y, black);
                    transientImage.setRGB(x, y, black);
                }
            }
        }

        init(this);

        while(true) {
            draw();
            update();
            frame.setTitle("Game of Life - Generation: " + generation + ", Population: " + population);
            //Thread.sleep(25); // stops everything going mad
        }
    }

    private void init(Game game) {
        frame.getContentPane().add(game);
        frame.getContentPane().setSize(image.getWidth()*2, image.getHeight()*2);
        frame.setSize(frame.getContentPane().getSize());
        frame.setResizable(false);
        frame.setFocusable(true);
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.requestFocus();
        frame.pack();
    }

    private void update() {

        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                int neighbors = 0;

                // left of cell
                if(x > 0 && image.getRGB(x-1,y) != black) {
                    neighbors++;
                }
                // right of cell
                if(x < image.getWidth()-1 && image.getRGB(x+1,y) != black) {
                    neighbors++;
                }
                // top of cell
                if(y > 0 && image.getRGB(x,y-1) != black) {
                    neighbors++;
                }
                // bottom of cell
                if(y < image.getHeight()-1 && image.getRGB(x,y+1) != black) {
                    neighbors++;
                }
                // top left of cell
                if(y > 0 && x > 0 && image.getRGB(x-1,y-1) != black) {
                    neighbors++;
                }
                // top right of cell
                if(x < image.getWidth()-1 && y > 0 && image.getRGB(x+1,y-1) != black) {
                    neighbors++;
                }
                // bottom left of cell
                if(x > 0 && y < image.getHeight()-1 && image.getRGB(x-1,y+1) != black) {
                    neighbors++;
                }
                // bottom right of cell
                if(x < image.getWidth()-1 && y < image.getHeight()-1 && image.getRGB(x+1,y+1) != black) {
                    neighbors++;
                }

                //  Rules:
                //  Any live cell with fewer than two live neighbours dies, as if by underpopulation.
                //  Any live cell with two or three live neighbours lives on to the next generation.
                //  Any live cell with more than three live neighbours dies, as if by overpopulation.
                //  Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.

                if(image.getRGB(x, y) == white && (neighbors < 2 || neighbors > 3)) {
                    transientImage.setRGB(x, y, black);
                    population--;
                    continue;
                }
                if(image.getRGB(x,y) == black && neighbors == 3) {
                    transientImage.setRGB(x, y, white);
                    population++;
                }
            }
        }
        image.setData(transientImage.getData());
        generation++;
    }

    private void draw() {
        BufferStrategy bs = getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(2);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        Graphics2D grr = (Graphics2D) g;
        grr.drawImage(image.getScaledInstance(image.getWidth()*2, image.getHeight()*2, 0), null, null);

        g.dispose();
        bs.show();
    }
}
