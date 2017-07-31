package main;

import static helpers.Handler.*;
import static org.lwjgl.opengl.GL11.*;

import helpers.*;
import serialization.*;

public class TileGrid {

    public Tile[][] map;
    public int width;
    public int height;
    public int offset;
    public int xOffset;
    public int yOffset;

    public TileGrid(int x, int y) {
        this.map = new Tile[x][y];
        this.width = x;
        this.height = y;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < y; j++) {
                if (j >= T_HEIGHT - 1) {
                    this.map[i][j] = new Tile(i, j, TileType.Dirt);
                } else if (j >= T_HEIGHT - 2) {
                    this.map[i][j] = new Tile(i, j, TileType.Grass);
                } else {
                    this.map[i][j] = new Tile(i, j, TileType.Null);
                }
            }
        }
        for (int i = 20; i < 30; i++) {
            for (int j = 0; j < y; j++) {
                if (j >= T_HEIGHT - 1) {
                    this.map[i][j] = new Tile(i, j, TileType.Stone);
                } else if (j >= T_HEIGHT - 2) {
                    this.map[i][j] = new Tile(i, j, TileType.Water);
                } else {
                    this.map[i][j] = new Tile(i, j, TileType.Null);
                }
            }
        }
        for (int i = 30; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (j == T_HEIGHT - 2) {
//                    if (Math.random() < .08) {
//                        this.map[i][j] = new Tile(i, j, TileType.Tile1);
//                        this.map[i - 1][j] = new Tile(i - 1, j, TileType.Lava);
//                        this.map[i - 2][j] = new Tile(i - 2, j, TileType.Lava);
//                    } else {
//                        this.map[i][j] = new Tile(i, j, TileType.Stone);
//                    }if (Math.random() < .08) {
                    if (i == 33 || i == 50) {
                        this.map[i][j] = new Tile(i, j, TileType.Tile1);
                        this.map[i - 1][j] = new Tile(i - 1, j, TileType.Lava);
                        this.map[i - 2][j] = new Tile(i - 2, j, TileType.Lava);
                    } else {
                        this.map[i][j] = new Tile(i, j, TileType.Stone);
                    }
                } else if (j > T_HEIGHT - 2) {
                    this.map[i][j] = new Tile(i, j, TileType.Stone);
                } else {
                    this.map[i][j] = new Tile(i, j, TileType.Null);
                }
            }
        }
    }

    /**
     * Draws the Entire Tile Grid
     *
     * @param x
     *            offset
     * @param y
     *            offset
     */
    public void draw(int x, int y) {
        this.xOffset = x;
        this.yOffset = y;
        for (int i = 0; i < this.map.length; i++) {
            for (int j = 0; j < this.map[i].length; j++) {
                if (!this.map[i][j].type.equals(TileType.Null)) {
                    if (this.onScreen(this.map[i][j])) {
                        glMatrixMode(GL_MODELVIEW);
                        glTranslatef(-this.xOffset, -this.yOffset, 0);
                        this.map[i][j].draw();
                    }
                }
            }
        }
    }

    private boolean onScreen(Tile tile) {
        return tile.x * PIXELS >= this.xOffset - PIXELS
                && tile.x * PIXELS <= (this.xOffset + Handler.WIDTH)
                && tile.y * PIXELS >= this.yOffset - PIXELS
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
        if (this.map[x][y].editable) {
            this.map[x][y] = new Tile(x, y, type);
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
        return this.map[x / PIXELS][y / PIXELS];
    }

    public void serialize(TPObject object) {
        object.setName("map");
        object.addField(TPField.Integer("width", this.width));
        object.addField(TPField.Integer("height", this.height));
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                TPField temp = TPField.Short(
                        Integer.toString(i) + "," + Integer.toString(j),
                        this.getTile(i, j).type.id);
                object.addField(temp);
            }
        }
    }

    public static TileGrid deserialize(TPObject object) {
        assert (object.getName().equals("map"));

        TileGrid map = new TileGrid(object.findField("width").getInt(),
                object.findField("height").getInt());

        for (int i = 0; i < map.width; i++) {
            for (int j = 0; j < map.height; j++) {
                TPField temp = object.findField(
                        Integer.toString(i) + "," + Integer.toString(j));
                if (temp != null) {
                    short id = temp.getShort();
                    map.setTile(i, j, TileType.create(id));
                }
            }
        }

        return map;
    }

}
