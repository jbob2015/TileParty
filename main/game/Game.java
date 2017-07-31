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
    public Texture background = new Texture("gamesky");
    public TileGrid map;
    public ArrayList<NetPlayer> playerList = new ArrayList<>();
    public float backgroundOffset;
    public ClientPlayer player;
    public Dice dice = new Dice(WIDTH / 2 - 64, 32);

    public static Queue<TPDatabase> packets = new ConcurrentLinkedQueue<TPDatabase>();
    public int turnCount, currentPlayer;
    public TurnState lastState = TurnState.WAITING;

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

        WAITING(0), DECIDING(1), ROLLING(2), MOVING(3), ENDING(4);

        public short id;

        TurnState(int id) {
            this.id = (short) id;
        }

        public static TurnState create(short id) {
            switch (id) {
                case 0:
                    return TurnState.WAITING;
                case 1:
                    return TurnState.DECIDING;
                case 2:
                    return TurnState.ROLLING;
                case 3:
                    return TurnState.MOVING;
                default:
                    return TurnState.WAITING;
            }
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
                case ENDING:
//                    System.out.println("We Here");
//                    this.handleTileAction(this.player);
                    break;
            }
        }

        this.player.update();
        this.backgroundOffset = this.player.x;
        for (NetPlayer p : this.playerList) {
            p.update();
        }
    }

    public void handleTileAction(Player player) {
        switch (player.currentTile) {
            case Tile1:
                if (!player.bomb.isExploding) {
                    if (!player.canKnockBack) {
                        player.bomb.explode();
                        player.canKnockBack = true;
                    } else {
                        player.canKnockBack = false;
                        player.x -= this.dice.roll * PIXELS;
                    }
                }
                player.bomb.update();
                glTranslatef(player.x - this.player.x, 0, 0);
                player.bomb.draw();
                break;
            case Air:
                break;
            case Dirt:
                break;
            case Grass:
                break;
            case Lava:
                if (!player.lastTile.equals(TileType.Lava)
                        && !player.lastTile.equals(TileType.Tile1)) {
                    StateManager.changeState(GameState.BOMBED);
                }
                break;
            case Null:
                break;
            case Stone:
                break;
            case Water:
                if (!player.lastTile.equals(TileType.Water)) {
                    StateManager.changeState(GameState.BUBBLES);
                }
                break;
            default:
                System.out.println("We are Default");
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
                            p.turnState = TurnState.create(
                                    player.findField("turnState").getShort());
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
        glTranslatef(-this.backgroundOffset / 8, 0, 0);
        drawQuadTex(this.background, 0, 0, WIDTH, HEIGHT);
        glTranslatef(-this.backgroundOffset / 8, 0, 0);
        drawQuadTex(this.background, WIDTH, 0, WIDTH, HEIGHT);
        glTranslatef(-this.backgroundOffset, 0, 0);
        if (this.backgroundOffset > WIDTH) {
            this.backgroundOffset = 0;
        }

        this.map.draw(this.player.x, 0);
        for (NetPlayer p : this.playerList) {
            glTranslatef(p.x - this.player.x, 0, 0);
            p.draw();
        }

        Player temp = this.getPlayer(this.currentPlayer);
        if (temp.turnState == TurnState.ROLLING
                || temp.turnState == TurnState.MOVING) {
            this.dice.update();
        }

        if (temp instanceof ClientPlayer
                && temp.turnState == TurnState.ENDING) {
            this.handleTileAction(temp);
        }

        this.player.draw();
        this.backgroundOffset -= .3 * Boot.dt;
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
        temp2.addField(TPField.Float("x", Mouse.getX()));
        temp2.addField(TPField.Float("y", Mouse.getY()));
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
