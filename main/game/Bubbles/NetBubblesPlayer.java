package main.game.Bubbles;

import static helpers.Handler.*;

import helpers.*;
import main.game.*;
import serialization.*;

public class NetBubblesPlayer extends BubblesPlayer {

    public Cursor cursor;

    public NetBubblesPlayer(NetPlayer player) {
        super(player);
        if (player instanceof NetPlayer) {
            this.cursor = player.cursor;
        }
    }

    public void draw() {
        float ratio = this.popCount / 50.0f;
        drawQuadTex(this.bar, (this.player.userID - 1) * 32,
                (int) (HEIGHT - HEIGHT * ratio), 32, (int) (HEIGHT * ratio));
        this.cursor.draw();
    }

    public static NetBubblesPlayer deserialize(TPObject object,
            TPObject mouse) {
        assert (object.getName().equals("BubblesPlayer"));

        NetBubblesPlayer temp = new NetBubblesPlayer(
                (NetPlayer) StateManager.game
                        .getPlayer(object.findField("ID").getInt())); //TODO

        temp.popCount = object.findField("popCount").getInt();
        temp.cursor.x = mouse.findField("x").getInt();
        temp.cursor.y = mouse.findField("y").getInt();
        return temp;
    }
}
