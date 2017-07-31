package main.game.Bombed;

import helpers.*;
import main.game.*;
import serialization.*;

public class NetBombedPlayer extends BombedPlayer {

    public NetBombedPlayer(Player player) {
        super(player);
    }

    public static NetBombedPlayer deserialize(TPObject object) {
        assert (object.getName().equals("BombedPlayer"));

        NetBombedPlayer temp = new NetBombedPlayer(
                StateManager.game.getPlayer(object.findField("ID").getInt()));

        temp.x = object.findField("x").getInt();
        temp.state = PlayerState.create(object.findField("State").getShort());
        return temp;
    }

}
