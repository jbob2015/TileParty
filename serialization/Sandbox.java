package serialization;

import java.util.*;

public class Sandbox {

    static class Level {

        private String name;
        private String path;
        private int width, height;
        private List<Entity> entities = new ArrayList<Entity>();

        private Level() {
        }

        public Level(String path) {
            this.name = "Level One";
            this.path = path;
            this.width = 64;
            this.height = 128;
        }

        public void add(Entity e) {
            e.init(this);
            this.entities.add(e);
        }

        public void update() {
        }

        public void render() {
        }

        public void save(String path) {
            TPDatabase database = new TPDatabase(this.name);
            TPObject object = new TPObject("LevelData");
            object.addString(TPString.Create("name", this.name));
            object.addString(TPString.Create("path", this.path));
            object.addField(TPField.Integer("width", this.width));
            object.addField(TPField.Integer("height", this.height));
            object.addField(
                    TPField.Integer("entityCount", this.entities.size()));
            database.addObject(object);
            for (int i = 0; i < this.entities.size(); i++) {
                Entity e = this.entities.get(i);

                TPObject entity = new TPObject("E" + i);
                byte type = 0;
                if (e instanceof Player) {
                    type = 1;
                }
                entity.addField(TPField.Byte("type", type));
                entity.addField(TPField.Integer("arrayIndex", i));
                e.serialize(entity);
                database.addObject(entity);
            }

            database.serializeToFile(path);
        }

        public static Level load(String path) {
            TPDatabase database = TPDatabase.DeserializeFromFile(path);
            TPObject levelData = database.findObject("LevelData");

            Level result = new Level();
            result.name = levelData.findString("name").getString();
            result.path = levelData.findString("path").getString();
            result.width = levelData.findField("width").getInt();
            result.height = levelData.findField("height").getInt();
            int entityCount = levelData.findField("entityCount").getInt();

            for (int i = 0; i < entityCount; i++) {
                TPObject entity = database.findObject("E" + i);
                Entity e;
                if (entity.findField("type").getByte() == 1) {
                    e = Player.deserialize(entity);
                } else {
                    e = Entity.deserialize(entity);
                }
                result.add(e);
            }
            return result;
        }

    }

    static class Entity {

        protected int x, y;
        protected boolean removed = false;
        protected Level level;

        public Entity() {
            this.x = 0;
            this.y = 0;
        }

        public void init(Level level) {
            this.level = level;
        }

        public void serialize(TPObject object) {
            object.addField(TPField.Integer("x", this.x));
            object.addField(TPField.Integer("y", this.y));
            object.addField(TPField.Boolean("removed", this.removed));
        }

        public static Entity deserialize(TPObject object) {
            Entity result = new Entity();
            result.x = object.findField("x").getInt();
            result.y = object.findField("y").getInt();
            result.removed = object.findField("removed").getBoolean();
            return result;
        }

    }

    static class Player extends Entity {

        private String name;
        private String avatarPath;

        private Player() {
        }

        public Player(String name, int x, int y) {
            this.x = x;
            this.y = y;

            this.name = name;
            this.avatarPath = "res/avatar.png";
        }

        @Override
        public void serialize(TPObject object) {
            super.serialize(object);
            object.addString(TPString.Create("name", this.name));
            object.addString(TPString.Create("avatarPath", this.avatarPath));
        }

        public static Player deserialize(TPObject object) {
            Entity e = Entity.deserialize(object);

            Player result = new Player();
            result.x = e.x;
            result.y = e.y;
            result.removed = e.removed;

            result.name = object.findString("name").getString();
            result.avatarPath = object.findString("avatarPath").getString();

            return result;
        }

    }

    public void play() {
        {
            Entity mob = new Entity();
            Player player = new Player("Cherno", 40, 28);

            Level level = new Level("res/level.lvl");
            level.add(mob);
            level.add(player);

            level.save("level.rcd");
        }
        {
            Level level = Level.load("level.rcd");
            System.out.println();
        }
    }

}