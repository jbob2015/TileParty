package main.game;

import static helpers.Handler.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.*;
import java.util.concurrent.*;

import helpers.*;
import helpers.StateManager.*;
import main.*;
import net.*;
import serialization.*;

public class Game {
    public Texture background = new Texture("null");
    public TileGrid map;
    public ArrayList<NetPlayer> playerList = new ArrayList<>();
    public ClientPlayer player;
    public Dice dice = new Dice(WIDTH / 2 - 64, 32);

    public static Queue<TPDatabase> packets = new ConcurrentLinkedQueue<TPDatabase>();
    public int turnCount, currentPlayer;
    public TurnState lastState = TurnState.WAITING;

    public enum Direction {
        UP(0), DOWN(1), LEFT(2), RIGHT(3);

        public short id;

        Direction(int id) {
            this.id = (short) id;
        }

        public static Direction create(int id) {
            for (Direction d : Direction.values()) {
                if (d.id == id) {
                    return d;
                }
            }
            return null;
        }
    }

    public Game(TileGrid map, int id) {
        this.map = map;
        this.player = new ClientPlayer(map, id);
        //TODO ADD NET PLAYERS HERE, ADD PLAYERS TO TURN QUEUE
        for (int i = 1; i < Client.MAX_PLAYERS; i++) {
            if (i != id) {
                this.playerList.add(new NetPlayer(i));
            }
        }
    }

    public enum TurnState {

        WAITING(0),
        DECIDING(1),
        ROLLING(2),
        MOVING(3),
        MOVE_DECISION(4),
        ENDING(5);

        public short id;

        TurnState(int id) {
            this.id = (short) id;
        }

        public static TurnState create(short id) {
            for (TurnState t : TurnState.values()) {
                if (t.id == id) {
                    return t;
                }
            }
            return null;
        }

    }

    /**
     * Updates Player 1 and Draws Background, Player1, and Map
     */
    public void update() {
        this.updatePackets();

        if (this.currentPlayer == this.player.userID) {
            switch (this.player.turnState) {
                case WAITING:
                    this.player.turnState = TurnState.DECIDING;
                    break;
                case DECIDING:
                    break;
                case ROLLING:
                    break;
                case MOVING:
                    break;
                case MOVE_DECISION:
                    break;
                case ENDING:
                    this.handleTileAction(this.player);
                    break;
            }
        }

        this.player.update();
        for (NetPlayer p : this.playerList) {
            p.update();
        }
    }

    public void handleTileAction(Player player) {
        switch (player.currentTile) {
            case Bottom:
                break;
            case Grass:
                break;
            case Trigger:
                //if (player.lastTile != TileType.Trigger) {
                StateManager.changeState(GameState.BOMBED);
                //}
                break;
            case Null:
                break;
            case WaterFall0:
                //if (player.lastTile != TileType.WaterFall0) {
                StateManager.changeState(GameState.BUBBLES);
                //}
                break;
            default:
                break;
        }
    }

    private void updatePackets() {
        for (TPDatabase database : packets) {
            assert (database.getName().equals("GameState"));
            TPObject player = database.objects.get(0);
            TPObject mouse = database.objects.get(1);
            if (player.getName().equals("Player")) {
                int tempID = player.findField("ID").getInt();
                if (this.hasPlayer(tempID)) {
                    for (NetPlayer p : this.playerList) {
                        if (p.userID == tempID) {
                            p.x = player.findField("x").getInt();
                            p.y = player.findField("y").getInt();

                            TurnState tempState = TurnState.create(
                                    player.findField("turnState").getShort());

                            if (p.userID == this.currentPlayer) {
                                if (tempState == TurnState.ROLLING
                                        && p.turnState != TurnState.ROLLING) {
                                    this.dice.roll();
                                } else if (tempState == TurnState.MOVING
                                        || tempState == TurnState.MOVE_DECISION) {
                                    this.dice.roll = player.findField("roll")
                                            .getByte();
                                }
                            }
                            p.turnState = TurnState.create(
                                    player.findField("turnState").getShort());
                            p.toDirection = Direction.create(
                                    (player.findField("direction").getShort()));
                            p.playStep = player.findField("playStep")
                                    .getBoolean();

                            p.cursor.x = mouse.findField("x").getInt();
                            p.cursor.y = mouse.findField("y").getInt();
                        }
                    }
                } else {
                    this.playerList.add(NetPlayer.deserialize(player, mouse));
                }
            }
        }
        packets.clear();
    }

    /**
     * Draws Background, Player1, and Map
     */
    public void draw() {
        drawQuadTex(this.background, 0, 0, WIDTH, HEIGHT);

        this.map.drawBottom(-PIXELS * 5 + this.player.x,
                -PIXELS * 5 + this.player.y);

        this.drawPlayers();

        this.map.drawTop(-PIXELS * 5 + this.player.x,
                -PIXELS * 5 + this.player.y);

        Player temp = this.getPlayer(this.currentPlayer);
        if (temp.turnState == TurnState.ROLLING
                || temp.turnState == TurnState.MOVING
                || temp.turnState == TurnState.MOVE_DECISION) {
            this.dice.update();
        }

        if (temp instanceof ClientPlayer
                && temp.turnState == TurnState.ENDING) {
            this.handleTileAction(temp);
        }

        if (this.player.turnState == TurnState.DECIDING) {
            this.player.ui.draw();
        }

        if (this.getPlayer(this.currentPlayer) instanceof NetPlayer) {
            ((NetPlayer) this.getPlayer(this.currentPlayer)).cursor.draw();
        }
    }

    public void drawPlayers() {
        ArrayList<Player> drawList = new ArrayList<Player>();

        boolean drawnPlayer = false;

        for (int i = 0; i < this.playerList.size() + 1; i++) {
            int tempMax = 0;
            NetPlayer tempPlayer = null;

            for (NetPlayer p : this.playerList) {
                if (!drawList.contains(p) && p.y > tempMax) {
                    tempPlayer = p;
                    tempMax = p.y;
                }
            }

            if (!drawnPlayer && this.player.y >= tempMax) {
                drawnPlayer = true;
                drawList.add(this.player);
            } else if (!drawList.contains(tempPlayer)) {
                drawList.add(tempPlayer);
            }
        }

        for (int i = drawList.size() - 1; i >= 0; i--) {
            Player drawPlayer = drawList.get(i);
            if (drawPlayer instanceof ClientPlayer) {
                this.player.draw();
            } else {
                glTranslatef(drawPlayer.x - this.player.x,
                        drawPlayer.y - this.player.y, 0);
                ((NetPlayer) drawPlayer).draw();
            }
        }

    }

    public int roll() {
        if (this.dice.isRolling) {
            return this.dice.roll;
        } else {
            return this.dice.roll();
        }
    }

    public void serialize(TPDatabase database) {
        TPObject temp = new TPObject("Player");
        this.player.serialize(temp);
        TPObject temp2 = new TPObject("Mouse");
        temp2.addField(TPField.Integer("x", (int) Mouse.getX()));
        temp2.addField(TPField.Integer("y", (int) Mouse.getY()));
        temp2.addField(TPField.Boolean("leftClick", Mouse.leftClick()));
        database.addObject(temp);
        database.addObject(temp2);

    }

    public boolean hasPlayer(int id) {
        for (NetPlayer p : this.playerList) {
            if (p.userID == id) {
                return true;
            }
        }
        return this.player.userID == id;
    }

    public Player getPlayer(int id) {
        assert (this.hasPlayer(id));
        for (NetPlayer p : this.playerList) {
            if (p.userID == id) {
                return p;
            }
        }
        return this.player;
    }

    public void removePlayer(int id) {
        for (int i = 0; i < this.playerList.size(); i++) {
            NetPlayer p = this.playerList.get(i);
            if (p.userID == id) {
                this.playerList.remove(p);
                break;
            }
        }
    }
}
