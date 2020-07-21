import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Random;

public class Game extends Canvas {
    private final int width = 1000;
    private final int height = 1000;
    //private final int generations = 10000;
    private int[][] grid;
    private int population = 5;
    private int generation = 1;

    private JFrame frame = new JFrame("Game of Life");

    public Game() throws InterruptedException {
        grid = new int[width/9][height/10];

        // glider
        // grid[2][0] = 1;
        // grid[2][1] = 1;
        // grid[2][2] = 1;
        // grid[1][2] = 1;
        // grid[0][1] = 1;

        Random random = new Random();
        for(int x = 0; x < width/9; x++) {
            for(int y = 0; y < height/10; y++) {
                if(random.nextInt() % 2 == 0) {
                    grid[x][y] = 1;
                    population++;
                }
            }
        }

        init(this);

        while(true) {
            draw();
            update();
            frame.setTitle("Game of Life - Generation: " + generation + ", Population: " + population);
            Thread.sleep(100);
        }
    }

    private void init(Game ex) {
        frame.getContentPane().add(ex);
        frame.setSize((width)+20, (height)+50);
        frame.setResizable(false);
        frame.setFocusable(true);
        frame.setUndecorated(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.requestFocus();
    }

    private void update() {
        int[][] tempGrid = new int[width/9][height/10];
        for(int x = 0; x < tempGrid.length; x++) {
            for(int y = 0; y < tempGrid[0].length; y++) {
                int neighbors = 0;

                // left of cell
                if(x > 0 && grid[x-1][y] != 0) {
                    neighbors++;
                }
                // right of cell
                if(x < grid.length-1 && grid[x+1][y] != 0) {
                    neighbors++;
                }
                // top of cell
                if(y > 0 && grid[x][y-1] != 0) {
                    neighbors++;
                }
                // bottom of cell
                if(y < grid[0].length-1 && grid[x][y+1] != 0) {
                    neighbors++;
                }
                // top left of cell
                if(y > 0 && x > 0 && grid[x-1][y-1] != 0) {
                    neighbors++;
                }
                // top right of cell
                if(x < grid.length-1 && y > 0 && grid[x+1][y-1] != 0) {
                    neighbors++;
                }
                // bottom left of cell
                if(x > 0 && y < grid[0].length-1 && grid[x-1][y+1] != 0) {
                    neighbors++;
                }
                // bottom right of cell
                if(x < grid.length-1 && y < grid[0].length-1 && grid[x+1][y+1] != 0) {
                    neighbors++;
                }

                //  Rules:
                //  Any live cell with fewer than two live neighbours dies, as if by underpopulation.
                //  Any live cell with two or three live neighbours lives on to the next generation.
                //  Any live cell with more than three live neighbours dies, as if by overpopulation.
                //  Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.

                if(grid[x][y] == 1 && (neighbors < 2 || neighbors > 3)) {
                    tempGrid[x][y] = 0;
                    population--;
                    continue;
                }
                if(grid[x][y] == 0 && neighbors == 3) {
                    tempGrid[x][y] = 1;
                    population++;
                } else {
                    tempGrid[x][y] = grid[x][y];
                }

            }
        }
        grid = tempGrid; // use a temp grid to stop rules being applied in a meta-generation way (in between generations)
        generation++;
    }

    private void draw() {
        BufferStrategy bs = getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width+50, height+50);

        Graphics2D grr = (Graphics2D) g;

        for(int x = 0; x < grid.length; x++) {
            for(int y = 0; y < grid[0].length; y++) {
                if(grid[x][y] == 1) {
                    grr.setColor(Color.WHITE);
                    grr.drawString("x", 5 + (9 * x), 12 + (10 * y));
                }
            }
        }

        g.dispose();
        bs.show();
    }
}
