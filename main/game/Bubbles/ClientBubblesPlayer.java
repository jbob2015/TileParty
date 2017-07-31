package main.game.Bubbles;

import static helpers.Handler.*;

import helpers.*;
import main.game.*;
import serialization.*;

public class ClientBubblesPlayer extends BubblesPlayer {

    public ClientBubblesPlayer(ClientPlayer player) {
        super(player);
    }

    public void update() {
        this.clicked = Mouse.leftClick();
        float x = Mouse.getX();
        float y = Mouse.getY();
        if (this.clicked) {
            for (int i = Bubbles.bubbles.size() - 1; i >= 0; i--) {
                Bubble bubble = Bubbles.bubbles.get(i);
                if (!bubble.popped) {
                    if (bubble.pop(x, y)) {
                        this.popCount++;
                        break;
                    }
                }
            }
        }
    }

    public void draw() {
        float ratio = this.popCount / 50.0f;
        drawQuadTex(this.bar, (this.player.userID - 1) * 32,
                (int) (HEIGHT - HEIGHT * ratio), 32, (int) (HEIGHT * ratio));
    }

    public void serialize(TPObject p) {
        p.setName("BubblesPlayer");
        p.addField(TPField.Integer("ID", StateManager.game.player.userID));
        p.addField(TPField.Integer("popCount", this.popCount));
    }

}
