package main.game.Bubbles;

import static helpers.Handler.*;

import java.util.*;
import java.util.concurrent.*;

import helpers.*;
import helpers.StateManager.*;
import main.*;
import serialization.*;

public class Bubbles {

    public static ClientBubblesPlayer player = new ClientBubblesPlayer(
            StateManager.game.player);
    private static ArrayList<NetBubblesPlayer> bubblesPlayers = new ArrayList<NetBubblesPlayer>();
    public static Queue<TPDatabase> packets = new ConcurrentLinkedQueue<TPDatabase>();

    public static ArrayList<Bubble> bubbles = new ArrayList<Bubble>();

    public static Texture background = new Texture("bubblesBackground");

    public Bubbles() {
        for (int i = 0; i < 50; i++) {
            int x = (int) (-Bubble.width + (WIDTH) * Math.random());
            int y = (int) (-Bubble.height + (HEIGHT) * Math.random());
            float dx = (float) ((Math.random() > .5 ? 1 : -1)
                    * (Math.random() * .09375f));
            float dy = -.09375f;
            Bubble temp = new Bubble(x, y, dx, dy);
            Bubbles.bubbles.add(temp);
        }
        Mouse.setHand(false);
    }

    public void drawBackground() {
        drawQuadTex(background, 0, 0, WIDTH * 2, HEIGHT);
    }

    public void update() {
        this.updatePackets();
        for (Bubble b : bubbles) {
            b.update();
        }
        player.update();
        if (player.popCount == bubbles.size() || this.netWinner()) {
            if (this.netWinner()) {
                this.getNetWinner().player.coins += 50;
            }
            StateManager.changeState(GameState.GAME);

        }

    }

    private boolean netWinner() {
        for (NetBubblesPlayer p : bubblesPlayers) {
            if (p.popCount == 50) {
                return true;
            }
        }
        return false;
    }

    private NetBubblesPlayer getNetWinner() {
        for (NetBubblesPlayer p : bubblesPlayers) {
            if (p.popCount == 50) {
                return p;
            }
        }
        return null;
    }

    public void draw() {
        this.drawBackground();
        for (NetBubblesPlayer p : bubblesPlayers) {
            p.draw();
        }

        player.draw();

        for (int i = 0; i < 50; i++) {
            Bubble b = bubbles.get(i);
            b.draw();
        }
    }

    public void updatePackets() {
        for (TPDatabase database : packets) {
            if (database.getName().equals("BubblesState")) {
                TPObject player = database.objects.get(0);
                TPObject mouse = database.objects.get(1);
                int tempID = player.findField("ID").getInt();
                if (Bubbles.hasPlayer(tempID)) {
                    for (NetBubblesPlayer p : Bubbles.bubblesPlayers) {
                        if (p.player.userID == tempID) {
                            p.cursor.x = mouse.findField("x").getInt();
                            p.cursor.y = mouse.findField("y").getInt();
                            p.popCount = player.findField("popCount").getInt();
                        }
                    }
                } else {
                    bubblesPlayers
                            .add(NetBubblesPlayer.deserialize(player, mouse));
                }
            }
        }
        packets.clear();

    }

    public static boolean hasPlayer(int id) {
        for (NetBubblesPlayer p : Bubbles.bubblesPlayers) {
            if (p.player.userID == id) {
                return true;
            }
        }
        return false;
    }

    public static void serialize(TPDatabase database) {
        database.setName("BubblesState");
        TPObject p = new TPObject("BubblesPlayer");
        TPObject mouse = new TPObject("Mouse");
        player.serialize(p);
        mouse.addField(TPField.Float("x", Mouse.getX()));
        mouse.addField(TPField.Float("y", Mouse.getY()));
        mouse.addField(TPField.Boolean("leftClick", Mouse.leftClick()));
        database.addObject(p);
        database.addObject(mouse);
    }

    public static void removePlayer(int id) {
        for (int i = 0; i < bubblesPlayers.size(); i++) {
            NetBubblesPlayer p = bubblesPlayers.get(i);
            if (p.player.userID == id) {
                bubblesPlayers.remove(p);
                break;
            }
        }
    }
}
