package ui;

import static helpers.Handler.*;

import helpers.*;
import main.*;

public class CheckBox {

    public String name;
    private static Texture checked = new Texture("checkedbox");
    private static Texture unchecked = new Texture("uncheckedbox");
    private static Texture hoverChecked = new Texture("hovercheckedbox");
    private static Texture hoverUnchecked = new Texture("hoveruncheckedbox");
    public int x, y, width, height;
    public static int sound = loadSound("click");
    private static Source source = new Source();
    public boolean isChecked = false;
    public boolean isEditable = true;

    public CheckBox(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = CheckBox.checked.getImageWidth();
        this.height = CheckBox.checked.getImageHeight();
    }

    public CheckBox(String name, int x, int y, boolean canEdit) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = CheckBox.checked.getImageWidth();
        this.height = CheckBox.checked.getImageHeight();
        this.isEditable = canEdit;
    }

    public CheckBox(String name, int x, int y, double scale) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = (int) (CheckBox.checked.getImageWidth() * scale);
        this.height = (int) (CheckBox.checked.getImageHeight() * scale);
    }

    private boolean isClicked() {
        boolean clicked = Mouse.leftClick() && this.mouseOver();
        if (clicked) {
            source.play(sound);
        }
        return clicked;
    }

    public void update() {
        if (this.isEditable && this.isClicked()) {
            this.isChecked = !this.isChecked;
        }
    }

    public boolean mouseOver() {
        return Mouse.getX() >= this.x && Mouse.getX() < this.x + this.width
                && Mouse.getY() >= this.y
                && Mouse.getY() < this.y + this.height;
    }

    public void draw() {
        if (this.isChecked) {
            if (this.mouseOver()) {
                if (this.isEditable) {
                    drawQuadTex(CheckBox.hoverChecked, this.x, this.y,
                            this.width, this.height);
                } else {
                    drawQuadTex(CheckBox.checked, this.x, this.y, this.width,
                            this.height);
                }
            } else {
                drawQuadTex(CheckBox.checked, this.x, this.y, this.width,
                        this.height);
            }
        } else {
            if (this.mouseOver()) {
                if (this.isEditable) {
                    drawQuadTex(CheckBox.hoverUnchecked, this.x, this.y,
                            this.width, this.height);
                } else {
                    drawQuadTex(CheckBox.unchecked, this.x, this.y, this.width,
                            this.height);
                }
            } else {
                drawQuadTex(CheckBox.unchecked, this.x, this.y, this.width,
                        this.height);
            }
        }
    }

}
