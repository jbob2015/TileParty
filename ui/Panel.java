package ui;

import static helpers.Handler.*;

import java.util.*;

import main.*;

public class Panel {
    private Button[][] buttonGrid;
    private ArrayList<Button> buttonList = new ArrayList<>();
    private Texture background;
    public int x, y, width, height, bWidth, bHeight;
    public String name;
    public static final int SPACE = 8;

    public Panel(String name, int x, int y, int width, int height, int bWidth,
            int bHeight) {
        this.buttonGrid = new Button[width][height];
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bWidth = bWidth;
        this.bHeight = bHeight;
        this.background = loadTex("panel");
    }

    public Panel(String name, int x, int y, int width, int height) {
        this.buttonGrid = new Button[width][height];
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bWidth = 32;
        this.bHeight = 32;
        this.background = loadTex("panel");
    }

    // INDEX OUT OF BOUNDS POTENTIAL
    public void addButton(String name, int x, int y) {
        Button temp = new Button(name,
                this.x + x * this.bWidth + (x + 1) * SPACE,
                this.y + y * this.bHeight + (y + 1) * SPACE);
        this.buttonGrid[x][y] = temp;
        this.buttonList.add(temp);
    }

    public void addButton(Button name, int x, int y) {
        name.x = this.x + x * this.bWidth + (x + 1) * SPACE;
        name.y = this.y + y * this.bHeight + (y + 1) * SPACE;
        this.buttonGrid[x][y] = name;
        this.buttonList.add(name);
    }

    public void addButton(String string, int i, int j, boolean b) {
        Button temp = new Button(string,
                this.x + i * this.bWidth + (i + 1) * SPACE,
                this.y + j * this.bHeight + (j + 1) * SPACE, b);
        this.buttonGrid[i][j] = temp;
        this.buttonList.add(temp);
    }

    public void addButton(String string, int i, int j, double scale,
            boolean b) {
        Button temp = new Button(string,
                this.x + i * this.bWidth + (i + 1) * SPACE,
                this.y + j * this.bHeight + (j + 1) * SPACE, scale, b);
        this.buttonGrid[i][j] = temp;
        this.buttonList.add(temp);
    }

    public void addButton(String name, int x, int y, double scale) {
        Button temp = new Button(name,
                this.x + x * this.bWidth + (x + 1) * SPACE,
                this.y + y * this.bHeight + (y + 1) * SPACE, scale);
        this.buttonGrid[x][y] = temp;
    }

    // NO DUPLICATES
    public Button getButton(String name) {
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                if (this.buttonGrid[i][j] != null
                        && this.buttonGrid[i][j].name.equals(name)) {
                    return this.buttonGrid[i][j];
                }
            }
        }
        return null;
    }

    protected void draw() {
        drawQuadTex(this.background, this.x, this.y,
                this.width * this.bWidth + (this.width + 1) * SPACE,
                this.height * this.bHeight + (this.height + 1) * SPACE);
        for (int i = 0; i < this.buttonGrid.length; i++) {
            for (int j = 0; j < this.buttonGrid[0].length; j++) {
                if (this.buttonGrid[i][j] != null) {
                    this.buttonGrid[i][j].draw();
                }
            }
        }
    }

    public Texture getBackground() {
        return this.background;
    }

    public void setBackground(Texture background) {
        this.background = background;
    }

    public ArrayList<Button> getButtonList() {
        return this.buttonList;
    }

    public boolean isClicked(String name) {
        for (Button b : this.buttonList) {
            if (b.name.equals(name)) {
                return b.isClicked();
            }
        }
        return false;
    }

}
