package main.game.Bombed;

import static helpers.Handler.*;

import helpers.*;
import helpers.Keyboard.*;
import main.*;
import main.game.*;
import serialization.*;

public class ClientBombedPlayer extends BombedPlayer {

    private Timer bombTimer = new Timer(.1);
    public boolean canBomb = this.bombTimer.canUpdate();

    public ClientBombedPlayer(Player player) {
        super(player);
    }

    public void serialize(TPObject object) {
        object.setName("BombedPlayer");
        object.addField(TPField.Integer("ID", this.player.userID));
        object.addField(TPField.Integer("x", this.x));
        object.addField(TPField.Short("State", this.state.id));
        object.addField(TPField.Boolean("canBomb", this.canBomb));
    }

    public void deserialize(TPObject object) {
        assert (object.getName().equals("BombedPlayer"));
        this.x = object.findField("x").getInt();
        this.state = PlayerState.create(object.findField("State").getShort());
    }

    public void update() {
        this.canBomb = this.bombTimer.canUpdate();
        int dx = (int) (.25 * Boot.dt);
        if (this.state != PlayerState.DEAD) {
            if (!Bombed.bomb.isExploding()) {
                if (Keyboard.isPressed(Key.D)) {
                    this.state = PlayerState.RIGHT;
                    this.x += dx;
                    if (this.stepTimer.canUpdate()) {
                        source.play(step);
                    }
                } else if (Keyboard.isPressed(Key.A)) {
                    this.state = PlayerState.LEFT;
                    this.x -= dx;
                    if (this.stepTimer.canUpdate()) {
                        source.play(step);
                    }
                } else {
                    this.state = PlayerState.STANDING;
                }
            } else {
                this.state = PlayerState.STANDING;
            }
        }
    }

    @Override
    public void reset() {
        this.x = WIDTH / 4 + (int) (WIDTH / 2 * Math.random());
        this.y = HEIGHT - 64 - 64 - 64;
        this.state = PlayerState.STANDING;
        this.bombTimer.canUpdate();
    }

}
