package main.lobby;

import static helpers.Handler.*;

import java.util.*;
import java.util.concurrent.*;

import main.*;
import net.*;
import serialization.*;
import ui.*;

public class Lobby {
    private static Texture background = new Texture("tileparty");
    public static Queue<TPDatabase> packets = new ConcurrentLinkedQueue<TPDatabase>();
    public List<LobbyClient> clients = new ArrayList<LobbyClient>();
    public List<Texture> sprites = new ArrayList<Texture>();
    public List<Texture> dice = new ArrayList<Texture>();

    public LobbyClient me;

    private static UI ui = new UI();

    public Lobby(byte id) {
        for (byte i = 1; i <= Client.MAX_PLAYERS; i++) {
            this.clients.add(new LobbyClient(i));
            this.sprites.add(new Texture("standing" + i + "-1"));
            if (i != id) {
                ui.addCheckBox(i + "",
                        (int) (WIDTH * ((float) i) / (Client.MAX_PLAYERS + 1)),
                        2 * HEIGHT / 3, false);
            } else {
                ui.addCheckBox(i + "",
                        (int) (WIDTH * ((float) i) / (Client.MAX_PLAYERS + 1)),
                        2 * HEIGHT / 3);
            }
            this.dice.add(new Texture("dice0"));
        }
        this.me = this.clients.get(id - 1);
        packets.clear();
    }

    /**
     * Updates the moving background and UI, draws UI
     */
    public void update() {

        this.updatePackets();

        if (ui.getCheckBox(this.me.userID + "").isChecked) {
            this.me.ready = true;
            ui.getCheckBox(this.me.userID + "").isEditable = false;
            this.dice.remove(this.me.userID - 1);
            this.dice.add(this.me.userID - 1,
                    new Texture("dice" + this.me.roll));
        }
    }

    /**
     * Sets up the UI
     */
    private void updatePackets() {
        for (TPDatabase database : Lobby.packets) {
            TPObject temp = database.objects.get(0);
            int id = temp.findField("ID").getInt();
            int roll = temp.findField("roll").getByte();
            ui.getCheckBox(id + "").isChecked = temp.findField("ready")
                    .getBoolean();
            if (this.dice.get(id - 1).name.equals("dice0")
                    && temp.findField("ready").getBoolean()) {
                this.dice.remove(id - 1);
                this.dice.add(id - 1, new Texture("dice" + roll));
            }
        }
    }

    public TPDatabase serialize() {
        return this.me.serialize();
    }

    public void removePlayer(int id) {
        for (int i = 0; i < this.clients.size(); i++) {
            LobbyClient client = this.clients.get(i);
            if (client.userID == id) {
                this.clients.remove(id);
                return;
            }
        }

    }

    public void draw() {

        drawQuadTex(background, 0, 0, WIDTH, HEIGHT);

        for (int i = 1; i <= Client.MAX_PLAYERS; i++) {
            Texture tex = this.sprites.get(i - 1);
            drawQuadTex(tex,
                    (int) (WIDTH * ((float) i) / (Client.MAX_PLAYERS + 1)) - 32,
                    2 * HEIGHT / 3, 32, 32);
            drawQuadTex(this.dice.get(i - 1),
                    (int) (WIDTH * ((float) i) / (Client.MAX_PLAYERS + 1)),
                    2 * HEIGHT / 3 - 32, 32, 32);
        }
        ui.draw();
    }
}
