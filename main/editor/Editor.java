package main.editor;

import static helpers.Handler.*;

import helpers.*;
import helpers.Keyboard.*;
import helpers.StateManager.*;
import main.*;
import ui.*;

public class Editor {
    public static TileGrid grid;
    public static UI ui;
    public static TileType type;
    public static Texture sky = new Texture("air1");
    private float xOffset = 0;
    private float yOffset = 0;

    public Editor(TileGrid map) {
        grid = map;
        type = TileType.Grass;
        ui = new UI();
        createUI();
    }

    /**
     * Updates All Aspects of Editor, Draws the Background
     */
    public void update() {
        // Handle UI Buttons First, Then Palette, Then Draw Tile To Screen
        // ORDER IS IMPORTANT
        if (Keyboard.isClicked(Key.ESCAPE) || Keyboard.isClicked(Key.E)) {
            StateManager.changeState(GameState.EDITORPAUSED);
        } else if (ui.isClicked("grass16")) {
            type = TileType.Grass;
        } else if (ui.isClicked("water")) {
            type = TileType.Water;
        } else if (ui.isClicked("dirt16")) {
            type = TileType.Dirt;
        } else if (ui.isClicked("lava16")) {
            type = TileType.Lava;
        } else if (ui.isClicked("air")) {
            type = TileType.Air;
        } else if (ui.isClicked("stone16")) {
            type = TileType.Stone;
        } else if (ui.isClicked("tile1")) {
            type = TileType.Tile1;
        } else if (ui.isClicked("null")) {
            type = TileType.Null;
        } else if (Mouse.leftDrag() && isEditable()) {
            if (this.isInGrid()) {
                grid.setTile(((int) Mouse.getX() + grid.xOffset) / PIXELS,
                        ((int) Mouse.getY() + grid.yOffset) / PIXELS, type);
            }
        } else if (Mouse.rightDrag() && isEditable()) {
            if (this.isInGrid()) {
                grid.setTile(((int) Mouse.getX() + grid.xOffset) / PIXELS,
                        ((int) Mouse.getY() + grid.yOffset) / PIXELS,
                        TileType.Null);
            }
        }
        if (Keyboard.isPressed(Key.W)) {
            this.yOffset -= .2 * Boot.dt;
        }
        if (Keyboard.isPressed(Key.S)) {
            this.yOffset += .2 * Boot.dt;
        }
        if (Keyboard.isPressed(Key.A)) {
            this.xOffset -= .2 * Boot.dt;
        }
        if (Keyboard.isPressed(Key.D)) {
            this.xOffset += .2 * Boot.dt;
        }
    }

    private boolean isInGrid() {
        int x = (int) (Mouse.getX() + grid.xOffset);
        int y = (int) (Mouse.getY() + grid.yOffset);
        return x >= 0 && x < grid.width * PIXELS && y >= 0
                && y < grid.height * PIXELS;
    }

    public void draw() {
        drawQuadTex(sky, 0, 0, WIDTH, HEIGHT);
        grid.draw((int) this.xOffset, (int) this.yOffset);
        ui.draw();
    }

    /**
     * Creates UI for Editor
     */
    private static void createUI() {
        ui.addPanel("palette", WIDTH - PIXELS * 2 - 8 * 3, 0, 2, 4); //2, 4
        ui.getPanel("palette").addButton("grass16", 0, 0, 2);
        ui.getPanel("palette").addButton("water", 1, 0);
        ui.getPanel("palette").addButton("dirt16", 0, 1, 2);
        ui.getPanel("palette").addButton("lava16", 1, 1, 2);
        ui.getPanel("palette").addButton("air", 0, 2);
        ui.getPanel("palette").addButton("stone16", 1, 2, 2);
        ui.getPanel("palette").addButton("tile1", 0, 3);
        ui.getPanel("palette").addButton("null", 1, 3);
    }

    /**
     * Checks if the tile can be edited
     *
     * @return true if the tile is not under the palette
     */
    private static boolean isEditable() {
        return Mouse.getX() < ui.getPanel("palette").x
                || Mouse.getY() > ui.getPanel("palette").y
                        + ui.getPanel("palette").height;
    }
}
