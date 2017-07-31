package main.game.Bubbles;

import main.*;
import main.game.*;

public class BubblesPlayer {
    public boolean clicked = false;
    public int popCount = 0;
    public Player player;
    public boolean popped = false;
    public boolean winner = false;

    public Texture bar;

    public BubblesPlayer(Player player) {
        this.player = player;
        this.bar = new Texture("bar" + this.player.userID);
    }

}
