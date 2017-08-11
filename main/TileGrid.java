package main;

import static helpers.Handler.*;
import static org.lwjgl.opengl.GL11.*;

import helpers.*;
import serialization.*;

public class TileGrid {

    public Tile[][][] map;
    public int width;
    public int height;
    public int offset;
    public int xOffset;
    public int yOffset;
    public final static int LAYERS = 5;

    public TileGrid(int x, int y) {
        this.map = new Tile[x][y][TileGrid.LAYERS];
        this.width = x;
        this.height = y;
        System.out.println(this.width + ", " + this.height);
//        for (int i = 0; i < this.width; i++) {
//            for (int j = 0; j < this.height; j++) {
//                this.setTile(i, j, TileType.Null);
//                System.out.println(i + "," + j);
//            }
//        }
//
//        this.setTile(0, 33, TileType.Grass);
//        this.setTile(0, 34, TileType.Bottom);
//
//        this.setTile(1, 33, TileType.Grass);
//        this.setTile(1, 34, TileType.Bottom);
//
//        this.setWaterFall(2, 33);
//        this.setTile(2, 29, TileType.Grass1);
//        this.setTile(2, 30, TileType.Bottom);
//        this.setTile(2, 33, TileType.Leaves);
//        this.setTile(2, 34, TileType.Leaves3);
//        this.setTile(2, 35, TileType.Leaves6);
//        this.setTile(2, 35, TileType.BranchLeft);
//
//        this.setTile(3, 33, TileType.Grass);
//        this.setTile(3, 29, TileType.Grass1);
//        this.setTile(3, 29, TileType.Sticks);
//        this.setTile(3, 30, TileType.Grass1);
//        this.setTile(3, 31, TileType.Bottom);
//        this.setTile(3, 33, TileType.Leaves1);
//        this.setTile(3, 34, TileType.Leaves4);
//        this.setTile(3, 35, TileType.Leaves7);
//        this.setTile(3, 35, TileType.TreeBranchLeft);
//        this.setTile(3, 36, TileType.TreeBranchRight);
//        this.setTile(3, 37, TileType.Tree);
//        this.setTile(3, 38, TileType.Bottom);
//
//        this.setTile(4, 30, TileType.TreeStump);
//        this.setTile(4, 31, TileType.Bottom);
//        this.setTile(4, 33, TileType.Grass);
//        this.setTile(4, 34, TileType.Bottom);
//        this.setTile(4, 33, TileType.Leaves2);
//        this.setTile(4, 34, TileType.Leaves5);
//        this.setTile(4, 35, TileType.Leaves8);
//        this.setTile(4, 36, TileType.BranchRight);
//
//        this.setTile(5, 33, TileType.Trigger);
//        this.setTile(5, 34, TileType.Bottom);
//
//        this.setTile(6, 24, TileType.Leaves);
//        this.setTile(6, 25, TileType.Leaves3);
//        this.setTile(6, 26, TileType.Leaves6);
//        this.setTile(6, 27, TileType.BranchLeft);
//        this.setTile(6, 32, TileType.Grass);
//        this.setTile(6, 33, TileType.Grass);
//        this.setTile(6, 34, TileType.Grass);
//        this.setTile(6, 35, TileType.Grass);
//        this.setTile(6, 36, TileType.Bottom);
//
//        this.setTile(7, 24, TileType.Leaves1);
//        this.setTile(7, 25, TileType.Leaves4);
//        this.setTile(7, 26, TileType.Leaves7);
//        this.setTile(7, 26, TileType.Tree1);
//        this.setTile(7, 27, TileType.TreeBranchLeft);
//        this.setTile(7, 28, TileType.Grass1);
//        this.setTile(7, 28, TileType.TreeBranchRight);
//        this.setTile(7, 29, TileType.Tree);
//        this.setTile(7, 30, TileType.Bottom);
//        this.setWaterFall(7, 32);
//        this.setTile(7, 35, TileType.Grass);
//        this.setTile(7, 36, TileType.Bottom);
//
//        this.setTile(8, 24, TileType.Leaves2);
//        this.setTile(8, 25, TileType.Leaves5);
//        this.setTile(8, 26, TileType.Leaves8);
//        this.setTile(8, 28, TileType.BranchRight);
//        this.setTile(8, 28, TileType.BeeHive);
//        this.setWaterFall(8, 28);
//        this.setTile(8, 29, TileType.BeeHive1);
//        this.setTile(8, 29, TileType.Bees);
//        this.setTile(8, 31, TileType.Grass);
//        this.setTile(8, 32, TileType.Grass);
//        this.setTile(8, 33, TileType.Bottom);
//        this.setTile(8, 35, TileType.Grass);
//        this.setTile(8, 36, TileType.Bottom);
//
//        this.setTile(9, 31, TileType.Grass);
//        this.setTile(9, 32, TileType.Bottom);
//        this.setTile(9, 35, TileType.Trigger);
//        this.setTile(9, 36, TileType.Bottom);
//        this.setTile(9, 37, TileType.TreeStump);
//        this.setTile(9, 38, TileType.Bottom);
//
//        this.setTile(10, 31, TileType.Grass);
//        this.setTile(10, 32, TileType.Grass);
//        this.setTile(10, 33, TileType.Grass);
//        this.setTile(10, 34, TileType.Bottom);
//        this.setTile(14, 35, TileType.LilyPad);
//        this.setWaterFall(10, 35);
//
//        this.setTile(11, 26, TileType.Leaves);
//        this.setTile(11, 27, TileType.Leaves3);
//        this.setTile(11, 28, TileType.Leaves6);
//        this.setTile(11, 29, TileType.BranchLeft);
//        this.setTile(11, 33, TileType.Grass);
//        this.setTile(11, 34, TileType.Grass);
//        this.setTile(11, 35, TileType.Grass);
//        this.setTile(11, 36, TileType.Bottom);
//
//        this.setTile(12, 26, TileType.Leaves1);
//        this.setTile(12, 27, TileType.Leaves4);
//        this.setTile(12, 28, TileType.Leaves7);
//        this.setTile(12, 28, TileType.TreeBranchRight);
//        this.setTile(12, 29, TileType.TreeBranchLeft);
//        this.setTile(12, 29, TileType.Grass1);
//        this.setTile(12, 30, TileType.Tree);
//        this.setTile(12, 31, TileType.Bottom);
//        this.setTile(12, 33, TileType.Grass);
//        this.setTile(12, 34, TileType.Bottom);
//
//        this.setTile(13, 26, TileType.Leaves2);
//        this.setTile(13, 27, TileType.Leaves5);
//        this.setTile(13, 28, TileType.Leaves8);
//        this.setTile(13, 28, TileType.BranchRight);
//        this.setTile(13, 29, TileType.Grass1);
//        this.setTile(13, 30, TileType.Bottom);
//        this.setTile(13, 33, TileType.Trigger);
//        this.setTile(13, 34, TileType.Bottom);
//        this.setTile(13, 38, TileType.Grass1);
//        this.setTile(13, 39, TileType.Grass1);
//        this.setTile(13, 40, TileType.Bottom);
//
//        this.setTile(14, 28, TileType.River);
//        this.setTile(14, 28, TileType.LilyPad);
//        this.setWaterFall(14, 29);
//        this.setTile(14, 33, TileType.Grass);
//        this.setTile(14, 33, TileType.Leaves);
//        this.setTile(14, 34, TileType.Bottom);
//        this.setTile(14, 34, TileType.Leaves3);
//        this.setTile(14, 35, TileType.Leaves6);
//        this.setTile(14, 35, TileType.BranchLeft);
//        this.setTile(14, 35, TileType.BeeHive);
//        this.setTile(14, 36, TileType.BeeHive1);
//        this.setTile(14, 36, TileType.Bees);
//        this.setTile(14, 38, TileType.BranchLeft);
//        this.setTile(14, 39, TileType.Sticks);
//        this.setTile(14, 39, TileType.Grass1);
//        this.setTile(14, 40, TileType.Bottom);
//
//        this.setTile(15, 33, TileType.Grass);
//        this.setTile(15, 33, TileType.Leaves1);
//        this.setTile(15, 34, TileType.Leaves4);
//        this.setTile(15, 34, TileType.Bottom);
//        this.setTile(15, 35, TileType.Leaves7);
//        this.setTile(15, 35, TileType.TreeBranchLeft);
//        this.setTile(15, 36, TileType.TreeBranchRight);
//        this.setTile(15, 37, TileType.TreeBird);
//        this.setTile(15, 38, TileType.TreeBranchLeft);
//        this.setTile(15, 39, TileType.Tree);
//        this.setTile(15, 40, TileType.Bottom);
//
//        this.setTile(16, 33, TileType.Grass);
//        this.setTile(16, 33, TileType.Leaves2);
//        this.setTile(16, 34, TileType.Bottom);
//        this.setTile(16, 34, TileType.Leaves5);
//        this.setTile(16, 35, TileType.Leaves8);
//        this.setTile(16, 36, TileType.BranchRight);
//        this.setTile(16, 37, TileType.Bird);
//        this.setTile(16, 39, TileType.Grass1);
//        this.setTile(16, 40, TileType.Bottom);
//
//        this.setTile(17, 33, TileType.Grass);
//        this.setTile(17, 34, TileType.Bottom);
//
//        for (int i = 18; i < T_WIDTH; i++) {
//            this.setTile(i, 33, TileType.Grass);
//            this.setTile(i, 34, TileType.Bottom);
//        }
    }

    private void setWaterFall(int i, int j) {
        this.setTile(i, j, TileType.WaterFall0);
        this.setTile(i, j + 1, TileType.WaterFall1);
        this.setTile(i, j + 2, TileType.WaterFall2);
    }

    /**
     * Draws the Bottom Layer of the Tile Grid
     *
     * @param x
     *            offset
     * @param y
     *            offset
     */
    public void drawBottom(int x, int y) {
        this.xOffset = x;
        this.yOffset = y;
        for (int i = 0; i < this.map.length; i++) {
            for (int j = 0; j < this.map[i].length; j++) {
                if (!this.map[i][j][0].type.equals(TileType.Null)) {
                    if (this.onScreen(this.map[i][j][0])) {
                        glMatrixMode(GL_MODELVIEW);
                        glTranslatef(-this.xOffset, -this.yOffset, 0);
                        this.map[i][j][0].draw();
                    }
                }
            }
        }
    }

    /**
     * Draws the Top Layer(s) of the Tile Grid
     *
     * @param x
     *            offset
     * @param y
     *            offset
     */
    public void drawTop(int x, int y) {
        this.xOffset = x;
        this.yOffset = y;
        for (int i = 0; i < this.map.length; i++) {
            for (int j = 0; j < this.map[i].length; j++) {
                for (int k = 1; k < TileGrid.LAYERS; k++) {
                    if (!this.map[i][j][k].type.equals(TileType.Null)) {
                        if (this.onScreen(this.map[i][j][k])) {
                            glMatrixMode(GL_MODELVIEW);
                            glTranslatef(-this.xOffset, -this.yOffset, 0);
                            this.map[i][j][k].draw();
                        }
                    }
                }
            }
        }
    }

    private boolean onScreen(Tile tile) {
        return tile.x * PIXELS >= this.xOffset - 2 * PIXELS
                && tile.x * PIXELS <= (this.xOffset + Handler.WIDTH)
                && tile.y * PIXELS >= this.yOffset - 2 * PIXELS
                && tile.y * PIXELS <= (Handler.HEIGHT + this.yOffset);
    }

    /**
     * Sets a tile on the grid at (x, y)
     *
     * @param x
     *            x coordinate on the grid
     * @param y
     *            y coordinate on the grid
     * @param type
     *            TileType to be set
     */
    public void setTile(int x, int y, TileType type) {
        if (type == TileType.Null) {
            this.map[x][y][0] = new Tile(x, y, type);
            this.map[x][y][1] = new Tile(x, y, type);
            this.map[x][y][2] = new Tile(x, y, type);
            this.map[x][y][3] = new Tile(x, y, type);
            this.map[x][y][4] = new Tile(x, y, type);
        } else {
            this.map[x][y][type.layer] = new Tile(x, y, type);
        }
    }

    /**
     * Gets the tile at (x,y)
     *
     * @param x
     *            x coordinate in space
     * @param y
     *            y coordinate in space
     * @return
     */
    public Tile getTile(int x, int y) {
        return this.map[x / PIXELS][y / PIXELS][0]; //TODO MULTIPLE LAYERS
    }

    public void serialize(TPDatabase database) {
        database.setName("map");
        TPObject width = new TPObject("width");
        width.addField(TPField.Integer("width", this.width));
        TPObject height = new TPObject("height");
        height.addField(TPField.Integer("height", this.height));
        database.addObject(width);
        database.addObject(height);

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                TPObject tempObject = new TPObject(
                        Integer.toString(i) + "," + Integer.toString(j));
                boolean hasContent = false;
                for (int k = 0; k < TileGrid.LAYERS; k++) {
                    if (this.map[i][j][k].type != TileType.Null) {
                        TPField tempField = TPField.Short(Integer.toString(k),
                                this.map[i][j][k].type.id);
                        tempObject.addField(tempField);
                        hasContent = true;
                    }
                }
                if (hasContent) {
                    database.addObject(tempObject);
                }
            }
        }
    }

    public static TileGrid deserialize(TPDatabase database) {
        assert (database.getName().equals("map"));

        System.out.println("Loading Map...");

        TileGrid map = new TileGrid(
                database.objects.get(0).findField("width").getInt(),
                database.objects.get(1).findField("height").getInt());

        for (int i = 0; i < map.width; i++) {
            for (int j = 0; j < map.height; j++) {
                TPObject tempObject = database.findObject(
                        Integer.toString(i) + "," + Integer.toString(j));
                if (tempObject != null) {
                    for (int k = 0; k < LAYERS; k++) {
                        TPField tempField = tempObject
                                .findField(Integer.toString(k));
                        if (tempField != null) {
                            short id = tempField.getShort();
                            map.map[i][j][k] = new Tile(i, j,
                                    TileType.create(id));
                        } else {
                            map.map[i][j][k] = new Tile(i, j, TileType.Null);
                        }
                    }
                } else {
                    map.setTile(i, j, TileType.Null);
                }
            }
            System.out.printf("%2.2f%%\n",
                    ((double) (i + 1) / (double) T_WIDTH) * 100);
        }

        return map;
    }

}
