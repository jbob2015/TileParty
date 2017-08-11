package main;

public enum TileType {

    //Basic Tiles
    Null("", 0, 0, false, false), // Could potentially be buggy?
    Grass("grass", 1, 0, true, false),
    Grass1("grass1", 2, 0, false, false),
    Bottom("bottom", 3, 0, false, false),
    Trigger("trigger", 4, 0, true, false),
    //Water Biome?
    WaterFall0("waterfall0", 100, 0, true, true),
    WaterFall1("waterfall1", 101, 0, false, true),
    WaterFall2("waterfall2", 102, 0, false, false),
    River("river", 103, 0, false, false),
    LilyPad("lilypad", 104, 1, false, false),
    //Little Animations
    Bees("bees", 200, 3, false, true),
    BeeHive("beehive", 202, 1, false, false),
    BeeHive1("beehive1", 203, 1, false, false),
    TreeBird("treeBird", 204, 2, false, true),
    Bird("bird", 205, 2, false, true),
    //Trees, Big Details
    BranchLeft("branchLeft", 300, 2, false, false),
    BranchRight("branchRight", 301, 2, false, false),
    TreeBranchLeft("treeBranchLeft", 302, 2, false, false),
    TreeBranchRight("treeBranchRight", 303, 2, false, false),
    Tree("tree", 304, 0, false, false),
    Tree1("tree1", 305, 2, false, false),
    TreeStump("treeStump", 306, 0, false, false),
    Leaves("leaves", 307, 3, false, false),
    Leaves1("leaves1", 308, 3, false, false),
    Leaves2("leaves2", 309, 3, false, false),
    Leaves3("leaves3", 310, 3, false, false),
    Leaves4("leaves4", 311, 3, false, false),
    Leaves5("leaves5", 312, 3, false, false),
    Leaves6("leaves6", 313, 3, false, false),
    Leaves7("leaves7", 314, 3, false, false),
    Leaves8("leaves8", 315, 3, false, false),
    Sticks("sticks", 315, 1, false, false),
    TreeHole("treeHole", 316, 1, false, false);

    public String textureName;
    public short id;
    public boolean animated = false;
    public byte layer = 0;
    public boolean walkable = false;

    TileType(String textureName, int id, int layer, boolean walkable,
            boolean animated) {
        if (textureName.length() > 0) {
            this.textureName = textureName;
        }
        this.id = (short) id;
        this.layer = (byte) layer;
        this.walkable = walkable;
        this.animated = animated;
    }

    public static TileType create(int id) {
        for (TileType t : TileType.values()) {
            if (t.id == id) {
                return t;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        if (this.equals(TileType.Null)) {
            return "null";
        }
        return this.textureName;
    }
}
