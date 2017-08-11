package main.editor;

import static helpers.Handler.*;

import java.util.*;

import helpers.*;
import helpers.Keyboard.*;
import helpers.StateManager.*;
import main.*;
import serialization.*;
import ui.*;

public class Editor {
    public static TileGrid grid;
    public static UI ui;
    public static TileType type;
    public static Texture background = new Texture("null");
    private static ArrayList<Panel> panels = new ArrayList<Panel>();
    private float xOffset = 0;
    private float yOffset = 28 * PIXELS;
    public int currentPanel = 0;
    private Button save = new Button("save", 0, 0, 4);

    public Editor(TileGrid map) {
        grid = map;
        type = TileType.Grass;
        ui = new UI();
        this.createUI();
    }

    /**
     * Updates All Aspects of Editor, Draws the Background
     */
    public void update() {
        // Handle UI Buttons First, Then Palette, Then Draw Tile To Screen
        // ORDER IS IMPORTANT
        ui.panelList.clear();
        ui.panelList.add(Editor.panels.get(this.currentPanel));

        if (Keyboard.isClicked(Key.ESCAPE)) {
            StateManager.changeState(GameState.EDITORPAUSED);
        } else if (this.buttonClicked() != null) {
            this.handleButtonClick();
        } else if (Keyboard.isClicked(Key.E)) {
            type = TileType.Null;
        } else if (this.save.isClicked()) {
            this.saveCurrentMap();
        } else if (Mouse.leftClick() && this.isEditable()) {
            if (this.isInGrid()) {
                grid.setTile(((int) Mouse.getX() + grid.xOffset) / PIXELS,
                        ((int) Mouse.getY() + grid.yOffset) / PIXELS, type);
            }
        } else if (Mouse.rightClick() && this.isEditable()) {
            if (this.isInGrid()) {
                type = grid.getTile(((int) Mouse.getX() + grid.xOffset),
                        ((int) Mouse.getY() + grid.yOffset)).type;
            }
        }
        if (Keyboard.isPressed(Key.W)) {
            this.yOffset -= .3 * Boot.dt;
        }
        if (Keyboard.isPressed(Key.S)) {
            this.yOffset += .3 * Boot.dt;
        }
        if (Keyboard.isPressed(Key.A)) {
            this.xOffset -= .3 * Boot.dt;
        }
        if (Keyboard.isPressed(Key.D)) {
            this.xOffset += .3 * Boot.dt;
        }
    }

    private void saveCurrentMap() {
        TPDatabase mapData = new TPDatabase("map");
        Editor.grid.serialize(mapData);
        mapData.serializeToFile("mapData.tp");
    }

    private void handleButtonClick() {
        if (this.currentPanel == 0) {
            switch (this.buttonClicked().name) {
                case "grass1":
                    type = TileType.Grass1;
                    break;
                case "grass":
                    type = TileType.Grass;
                    break;
                case "bottom":
                    type = TileType.Bottom;
                    break;
                case "trigger":
                    type = TileType.Trigger;
                    break;
                case "null":
                    type = TileType.Null;
                    break;
                case "tree":
                    type = TileType.Tree;
                    break;
                case "tree1":
                    type = TileType.Tree1;
                    break;
                case "treeStump":
                    type = TileType.TreeStump;
                    break;
                case "branchLeft":
                    type = TileType.BranchLeft;
                    break;
                case "branchRight":
                    type = TileType.BranchRight;
                    break;
                case "treeBranchLeft":
                    type = TileType.TreeBranchLeft;
                    break;
                case "treeBranchRight":
                    type = TileType.TreeBranchRight;
                    break;
                case "left":
                    this.currentPanel = this.currentPanel - 1 >= 0
                            ? this.currentPanel - 1 : 0;
                    break;
                case "right":
                    this.currentPanel = (this.currentPanel + 1)
                            % Editor.panels.size();
                    break;
            }
        } else if (this.currentPanel == 1) {
            switch (this.buttonClicked().name) {
                case "waterfall01":
                    type = TileType.WaterFall0;
                    break;
                case "waterfall11":
                    type = TileType.WaterFall1;
                    break;
                case "waterfall2":
                    type = TileType.WaterFall2;
                    break;
                case "river":
                    type = TileType.River;
                    break;
                case "lilypad":
                    type = TileType.LilyPad;
                    break;
                case "sticks":
                    type = TileType.Sticks;
                    break;
                case "beehive":
                    type = TileType.BeeHive;
                    break;
                case "beehive1":
                    type = TileType.BeeHive1;
                    break;
                case "bees":
                    type = TileType.Bees;
                    break;
                case "treeHole":
                    type = TileType.TreeHole;
                    break;
                case "bird3":
                    type = TileType.Bird;
                    break;
                case "treeBird":
                    type = TileType.TreeBird;
                    break;
                case "left":
                    this.currentPanel = this.currentPanel - 1 >= 0
                            ? this.currentPanel - 1 : 0;
                    break;
                case "right":
                    this.currentPanel = (this.currentPanel + 1)
                            % Editor.panels.size();
                    break;
            }
        } else if (this.currentPanel == 2) {
            switch (this.buttonClicked().name) {
                case "leaves":
                    type = TileType.Leaves;
                    break;
                case "leaves2":
                    type = TileType.Leaves2;
                    break;
                case "leaves3":
                    type = TileType.Leaves3;
                    break;
                case "leaves5":
                    type = TileType.Leaves5;
                    break;
                case "leaves6":
                    type = TileType.Leaves6;
                    break;
                case "leaves8":
                    type = TileType.Leaves8;
                    break;
                case "leaves1":
                    type = TileType.Leaves1;
                    break;
                case "leaves4":
                    type = TileType.Leaves4;
                    break;
                case "leaves7":
                    type = TileType.Leaves7;
                    break;
                case "left":
                    this.currentPanel = this.currentPanel - 1 >= 0
                            ? this.currentPanel - 1 : 0;
                    break;
                case "right":
                    this.currentPanel = (this.currentPanel + 1)
                            % Editor.panels.size();
                    break;
            }
        }
    }

    private Button buttonClicked() {
        Panel temp = Editor.panels.get(this.currentPanel);
        for (Button b : temp.getButtonList()) {
            if (b.isClicked()) {
                return b;
            }
        }
        return null;
    }

    private boolean isInGrid() {
        int x = (int) (Mouse.getX() + grid.xOffset);
        int y = (int) (Mouse.getY() + grid.yOffset);
        return x >= 0 && x < grid.width * PIXELS && y >= 0
                && y < grid.height * PIXELS;
    }

    public void draw() {
        drawQuadTex(background, 0, 0, WIDTH, HEIGHT);
        System.out.println(this.xOffset + " " + this.yOffset);
        grid.drawBottom((int) this.xOffset, (int) this.yOffset);
        grid.drawTop((int) this.xOffset, (int) this.yOffset);
        ui.draw();
    }

    /**
     * Creates UI for Editor
     */
    private void createUI() {
        ui.addButton(this.save);

        panels.add(
                new Panel("palette", WIDTH - 48 * 2 - 8 * 3, 0, 2, 7, 48, 48));
        panels.add(
                new Panel("palette", WIDTH - 48 * 2 - 8 * 3, 0, 2, 7, 48, 48));
        panels.add(
                new Panel("palette", WIDTH - 48 * 2 - 8 * 3, 0, 2, 7, 48, 48));

        panels.get(0).addButton("null", 0, 0, 3, true);
        panels.get(0).addButton("grass", 1, 0, 3, true);

        panels.get(0).addButton("trigger", 0, 1, 3, true);
        panels.get(0).addButton("bottom", 1, 1, 3, true);

        panels.get(0).addButton("tree", 0, 2, 3, true);
        panels.get(0).addButton("tree1", 1, 2, 3, true);

        panels.get(0).addButton("treeStump", 0, 3, 3, true);
        panels.get(0).addButton("grass1", 1, 3, 3, true);

        panels.get(0).addButton("branchLeft", 0, 4, 3, true);
        panels.get(0).addButton("branchRight", 1, 4, 3, true);

        panels.get(0).addButton("treeBranchLeft", 0, 5, 3, true);
        panels.get(0).addButton("treeBranchRight", 1, 5, 3, true);

        panels.get(0).addButton("left", 0, 6, 3, true);
        panels.get(0).addButton("right", 1, 6, 3, true);

        panels.get(1).addButton("waterfall01", 0, 0, 3, true);
        panels.get(1).addButton("waterfall11", 1, 0, 3, true);

        panels.get(1).addButton("waterfall2", 0, 1, 3, true);
        panels.get(1).addButton("river", 1, 1, 3, true);

        panels.get(1).addButton("lilypad", 0, 2, 3, true);
        panels.get(1).addButton("sticks", 1, 2, 3, true);

        panels.get(1).addButton("beehive", 0, 3, 3, true);
        panels.get(1).addButton("beehive1", 1, 3, 3, true);

        panels.get(1).addButton("bees", 0, 4, 3, true);
        panels.get(1).addButton("treeHole", 1, 4, 3, true);

        panels.get(1).addButton("treeBird", 0, 5, 3, true);
        panels.get(1).addButton("bird3", 1, 5, 3, true);

        panels.get(1).addButton("left", 0, 6, 3, true);
        panels.get(1).addButton("right", 1, 6, 3, true);

        panels.get(2).addButton("leaves", 0, 0, 3, true);
        panels.get(2).addButton("leaves2", 1, 0, 3, true);

        panels.get(2).addButton("leaves3", 0, 1, 3, true);
        panels.get(2).addButton("leaves5", 1, 1, 3, true);

        panels.get(2).addButton("leaves6", 0, 2, 3, true);
        panels.get(2).addButton("leaves8", 1, 2, 3, true);

        panels.get(2).addButton("leaves1", 0, 3, 3, true);
        panels.get(2).addButton("leaves7", 1, 3, 3, true);

        panels.get(2).addButton("leaves4", 0, 4, 3, true);

        panels.get(2).addButton("left", 0, 6, 3, true);
        panels.get(2).addButton("right", 1, 6, 3, true);
    }

    /**
     * Checks if the tile can be edited
     *
     * @return true if the tile is not under the palette
     */
    private boolean isEditable() {
        return Mouse.getX() < panels.get(this.currentPanel).x
                || Mouse.getY() > panels.get(this.currentPanel).y
                        + panels.get(this.currentPanel).height;
    }
}
