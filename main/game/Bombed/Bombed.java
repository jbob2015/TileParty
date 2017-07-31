package main.game.Bombed;

import static helpers.Handler.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.*;
import java.util.concurrent.*;

import helpers.*;
import helpers.StateManager.*;
import main.*;
import main.game.*;
import main.game.Bombed.BombedPlayer.*;
import serialization.*;

public class Bombed {
    public static ClientBombedPlayer player = new ClientBombedPlayer(
            StateManager.game.player);
    private static ArrayList<NetBombedPlayer> bombedPlayers = new ArrayList<>();
    private static Texture background = new Texture("sky");
    private static int backgroundOffset = 0;

    public static Queue<TPDatabase> packets = new ConcurrentLinkedQueue<TPDatabase>();

    public static Bomb bomb = new Bomb(WIDTH / 2, 16);

    public Bombed() {
        for (NetPlayer p : StateManager.game.playerList) {
            Bombed.bombedPlayers.add(new NetBombedPlayer(p));
        }
    }

    public void update() {

        this.updatePackets();

        Bombed.player.update();
        Bombed.bomb.update();

        if (!this.roundOver() && Bombed.bomb.bomb.getFrame() == 7) {
            Bombed.player.kill(Bombed.bomb.side);
        } else if (this.roundOver() && !Bombed.bomb.isExploding()) {
            StateManager.changeState(GameState.GAME);
        }
        Bombed.backgroundOffset -= .3 * Boot.dt;
    }

    private void updatePackets() {
        for (TPDatabase database : packets) {
            assert (database.getName().equals("BombedState"));
            TPObject object = database.objects.get(0);
            if (object.getName().equals("BombedPlayer")) {
                int tempID = object.findField("ID").getInt();
                if (StateManager.game.hasPlayer(tempID)) {
                    for (NetBombedPlayer p : Bombed.bombedPlayers) {
                        if (p.player.userID == tempID) {
                            p.x = object.findField("x").getInt();
                            p.state = PlayerState.create(
                                    object.findField("State").getShort());
                        }
                    }
                } else {
                    Bombed.bombedPlayers
                            .add(NetBombedPlayer.deserialize(object));
                }
            }
        }
        packets.clear();
    }

    public void draw() {
        glTranslatef(Bombed.backgroundOffset, 0, 0);
        drawQuadTex(background, 0, 0, WIDTH * 10, HEIGHT);
        if (Bombed.backgroundOffset > WIDTH * 10) {
            Bombed.backgroundOffset = 0;
        }
        drawQuadTex(new Texture("panel"), WIDTH / 2 - 2, 0, 4, HEIGHT);
        Bombed.bomb.draw();
        for (NetBombedPlayer p : Bombed.bombedPlayers) {
            if (!p.isAlive()) {
                p.draw();
            }
        }
        for (NetBombedPlayer p : Bombed.bombedPlayers) {
            if (p.isAlive()) {
                p.draw();
            }
        }
        Bombed.player.draw();
    }

    public boolean roundOver() {
        for (NetBombedPlayer p : Bombed.bombedPlayers) {
            if (p.isAlive()) {
                return false;
            }
        }
        return !Bombed.player.isAlive();
    }

    public Bomb getBomb() {
        return Bombed.bomb;
    }

    public void reset() {
        player.reset();
        Bombed.backgroundOffset = 0;
    }

    public static void serialize(TPDatabase database) {
        TPObject temp = new TPObject("BombedPlayer");
        Bombed.player.serialize(temp);
        database.addObject(temp);
    }

    public static void removePlayer(int id) {
        for (int i = 0; i < bombedPlayers.size(); i++) {
            NetBombedPlayer p = bombedPlayers.get(i);
            if (p.player.userID == id) {
                bombedPlayers.remove(p);
                break;
            }
        }
    }
}
