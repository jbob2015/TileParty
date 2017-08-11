package ui;

import static helpers.Handler.*;

import helpers.*;
import main.*;

public class Button {
    public String name;
    private Texture hoverTex, texture;
    public int x, y, width, height;
    public static int sound = loadSound("click");
    private static Source source = new Source();
    private boolean isStatic = false;

    public Button(String s, int x, int y) {
        this.name = s;
        this.texture = loadTex(s);
        this.hoverTex = loadTex(s + "1");
        this.x = x;
        this.y = y;
        this.width = this.texture.getImageWidth();
        this.height = this.texture.getImageHeight();
    }

    public Button(String s, int x, int y, boolean isStatic) {
        this.name = s;
        this.texture = loadTex(s);
        if (!isStatic) {
            this.hoverTex = loadTex(s + "1");
        }
        this.x = x;
        this.y = y;
        this.width = this.texture.getImageWidth();
        this.height = this.texture.getImageHeight();
        this.isStatic = isStatic;
    }

    public Button(String s, int x, int y, double scale) {
        this.name = s;
        this.texture = loadTex(s);
        this.hoverTex = loadTex(s + "1");
        this.x = x;
        this.y = y;
        this.width = (int) (this.texture.getImageWidth() * scale);
        this.height = (int) (this.texture.getImageHeight() * scale);
    }

    public Button(String s, int x, int y, double scale, boolean isStatic) {
        this.name = s;
        this.texture = loadTex(s);
        if (!isStatic) {
            this.hoverTex = loadTex(s + "1");
        }
        this.x = x;
        this.y = y;
        this.width = (int) (this.texture.getImageWidth() * scale);
        this.height = (int) (this.texture.getImageHeight() * scale);
        this.isStatic = isStatic;
    }

    public boolean isClicked() {
        boolean clicked = Mouse.leftClick() && this.mouseOver();
        if (clicked) {
            source.play(sound);
        }
        return clicked;
    }

    public boolean mouseOver() {
        return Mouse.getX() >= this.x && Mouse.getX() < this.x + this.width
                && Mouse.getY() >= this.y
                && Mouse.getY() < this.y + this.height;
    }

    //TODO I forgot what this was for -.-
    public void draw() {
        if (this.mouseOver()) {
            if (!this.isStatic) {
                drawQuadTex(this.hoverTex, this.x, this.y, this.width,
                        this.height);
            } else {
                drawQuadTex(this.texture, this.x, this.y, this.width,
                        this.height);
            }
        } else {
            drawQuadTex(this.texture, this.x, this.y, this.width, this.height);
        }
    }
}
