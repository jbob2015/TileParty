package main;

public enum TileType {

    Null("", 0), // Could potentially be buggy?
    Grass("grass16", 1),
    Water("water", 2),
    Lava("lava16", 3),
    Dirt("dirt16", 4),
    Air("air1", 5),
    Stone("stone16", 6),
    Tile1("tile1", 7);

    public String textureName;
    public short id;

    TileType(String textureName, int id) {
        if (textureName.length() > 0) {
            this.textureName = textureName;
        }
        this.id = (short) id;
    }

    public static TileType create(int id) {
        switch (id) {
            case 0:
                return TileType.Null;
            case 1:
                return TileType.Grass;
            case 2:
                return TileType.Water;
            case 3:
                return TileType.Lava;
            case 4:
                return TileType.Dirt;
            case 5:
                return TileType.Air;
            case 6:
                return TileType.Stone;
            case 7:
                return TileType.Tile1;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        if (this.equals(TileType.Null)) {
            return "null";
        }
        return this.textureName;
    }
}
