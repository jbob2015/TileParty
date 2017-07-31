package main;

import static helpers.Handler.*;

public class SpriteSheet {

    private Texture sheet;
    private int width, height, currentX, currentY;

    public SpriteSheet(String sprite, int width, int height) {
        this.sheet = loadTex(sprite);
        this.width = width;
        this.height = height;
        this.currentX = 0;
        this.currentY = 0;
    }

    public void draw(int xPos, int yPos) {
        drawSheetTex(this.sheet, this.currentX, this.currentY, xPos, yPos);
    }

    public void nextFrame() {
        if (this.currentX + 1 < this.width) {
            this.currentX++;
        } else if (this.currentY + 1 < this.height) {
            this.currentX = 0;
            this.currentY++;
        } else {
            this.currentX = 0;
            this.currentY = 0;
        }
    }
}
