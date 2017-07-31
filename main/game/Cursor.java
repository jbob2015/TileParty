package main.game;

import static helpers.Handler.*;

import main.*;

public class Cursor {

    public int x, y;
    public Texture cursor;

    public Cursor(int x, int y, int id) {
        this.x = x;
        this.y = y;
        if (id < 5) {
            this.cursor = new Texture("cursor" + id);
        } else {
            this.cursor = new Texture("cursor");
        }
    }

    public void draw() {
        drawQuadTex(this.cursor, this.x, this.y, this.cursor.getWidth(),
                this.cursor.getHeight());
    }

}
