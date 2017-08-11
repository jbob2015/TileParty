package main.game;

import static helpers.Handler.*;

import java.util.*;

import helpers.*;
import helpers.Keyboard.*;
import helpers.StateManager.*;
import main.*;
import main.game.Game.*;
import serialization.*;
import ui.*;

public class ClientPlayer extends Player {

    public UI ui = new UI();

    public byte spacesToGo = 0;
    public int distanceToGo = 0;

    public ClientPlayer(TileGrid map, int id) {
        super(map, id);
        this.currentTile = Player.map.getTile(this.boardPositionX,
                this.boardPositionY).type;
        this.updateTile();
        this.ui.addButton("roll", WIDTH / 2 - 64 * 2, HEIGHT / 4, 4);
        // this.bomb = new Bomb(-PIXELS / 2 + PIXELS * 15, this.y + PIXELS / 2);
    }

    /**
     * Updates All Aspects of Player
     */
    public void update() {
        if (Keyboard.isClicked(Key.ESCAPE) || Keyboard.isClicked(Key.E)) {
            StateManager.changeState(GameState.GAMEPAUSED);
        } else if (this.turnState != TurnState.WAITING) {
            this.playStep = false;
            this.boardPositionX = this.x + 24;
            this.boardPositionY = this.y + 96;
            this.updateTile();
            this.currentTile = Player.map.getTile(this.boardPositionX,
                    this.boardPositionY).type;
            this.updateInput();
            this.updateMotion();
        }
        this.idle.update();
    }

    /**
     * Updates the animation based on if the player is walking, and if the dice
     * is rolling
     */
    private void updateMotion() {
        if (this.turnState == TurnState.ROLLING
                && !StateManager.game.dice.isRolling) {
            this.turnState = TurnState.MOVING;
            this.handleDirection();
        }
        if (this.turnState == TurnState.MOVING) {
            this.rightWalking.update();
            this.upWalking.update();
            this.downWalking.update();
            if (this.distanceToGo > 0) {
                int v = (int) (.2 * Boot.dt);
                if (v > this.distanceToGo) {
                    switch (this.toDirection) {
                        case UP:
                            this.y -= this.distanceToGo;
                            this.fromDirection = Direction.DOWN;
                            break;
                        case DOWN:
                            this.y += this.distanceToGo;
                            this.fromDirection = Direction.UP;
                            break;
                        case RIGHT:
                            this.x += this.distanceToGo;
                            this.fromDirection = Direction.LEFT;
                            break;
                        default:
                            break;
                    }
                    this.distanceToGo = 0;
                } else {
                    switch (this.toDirection) {
                        case UP:
                            this.y -= v;
                            break;
                        case DOWN:
                            this.y += v;
                            break;
                        case RIGHT:
                            this.x += v;
                            break;
                        default:
                            break;
                    }
                    this.distanceToGo -= v;
                }
            } else if (this.spacesToGo > 0) {
                this.spacesToGo--;
                this.distanceToGo = PIXELS;
                this.playStep = true;
                if (this.playStep) {
                    source.play(step);
                }
                this.handleDirection();
            } else {
                this.turnState = TurnState.ENDING;
                StateManager.game.handleTileAction(this);
            }
        }

        if (this.turnState == TurnState.MOVE_DECISION) {
            boolean right = (this.fromDirection != Direction.RIGHT
                    && Player.map.getTile(this.boardPositionX + PIXELS,
                            this.boardPositionY).type.walkable);
            boolean up = (this.fromDirection != Direction.UP
                    && Player.map.getTile(this.boardPositionX,
                            this.boardPositionY - PIXELS).type.walkable);
            boolean down = (this.fromDirection != Direction.DOWN
                    && Player.map.getTile(this.boardPositionX,
                            this.boardPositionY + PIXELS).type.walkable);
            if (Keyboard.isClicked(Key.W) && up) {
                this.toDirection = Direction.UP;
                this.fromDirection = Direction.DOWN;
                this.turnState = TurnState.MOVING;
            } else if (Keyboard.isClicked(Key.D) && right) {
                this.toDirection = Direction.RIGHT;
                this.fromDirection = Direction.LEFT;
                this.turnState = TurnState.MOVING;
            } else if (Keyboard.isClicked(Key.S) && down) {
                this.toDirection = Direction.DOWN;
                this.fromDirection = Direction.UP;
                this.turnState = TurnState.MOVING;
            }
        }
    }

    private void handleDirection() {
        boolean right = (this.fromDirection != Direction.RIGHT
                && Player.map.getTile(this.boardPositionX + PIXELS,
                        this.boardPositionY).type.walkable);
        boolean up = (this.fromDirection != Direction.UP
                && Player.map.getTile(this.boardPositionX,
                        this.boardPositionY - PIXELS).type.walkable);
        boolean down = (this.fromDirection != Direction.DOWN
                && Player.map.getTile(this.boardPositionX,
                        this.boardPositionY + PIXELS).type.walkable);

        List<Boolean> values = new ArrayList<Boolean>();
        values.add(right);
        values.add(up);
        values.add(down);
        int dirCount = (int) values.stream().filter(p -> p == true).count();

        if (dirCount > 1 && !right) {
            this.turnState = TurnState.MOVE_DECISION;
        } else {
            if (right) {
                this.toDirection = Direction.RIGHT;
            } else if (up) {
                this.toDirection = Direction.UP;
            } else if (down) {
                this.toDirection = Direction.DOWN;
            }
        }

        System.out.println(
                this.fromDirection.name() + " " + this.toDirection.name() + " "
                        + Player.map.getTile(this.boardPositionX,
                                this.boardPositionY).toString()
                        + " " + up + " " + down + " " + right);
    }

    /**
     * Updates All Possible Input (UI, Buttons, Mouse), Processes Actions
     */
    private void updateInput() {
        if (this.turnState == TurnState.DECIDING) {
            if (this.ui.getButton("roll").isClicked()) {
                this.spacesToGo = (byte) StateManager.game.roll();
                this.distanceToGo = PIXELS;
                this.handleDirection();
                this.turnState = TurnState.ROLLING;
            }
        } else if (this.spacesToGo <= 0) {
            if (this.turnState != TurnState.ENDING) { //TODO Only way Turns Work
                this.turnState = TurnState.ENDING;
                StateManager.game.handleTileAction(this);
            } else {
                this.turnState = TurnState.WAITING;
            }
        }

    }

    /**
     * Draws the player walking, or standing still. Also highlights the tile
     * below the player
     */
    public void draw() {
        if (this.turnState == TurnState.MOVING) {
            this.idle.restart();
            switch (this.toDirection) {
                case UP:
                    this.upWalking.draw(PIXELS * 5, PIXELS * 5);
                    break;
                case DOWN:
                    this.downWalking.draw(PIXELS * 5, PIXELS * 5);
                    break;
                case RIGHT:
                    this.rightWalking.draw(PIXELS * 5, PIXELS * 5);
                    break;
                default:
                    break;
            }
        } else {
            this.idle.draw(PIXELS * 5, PIXELS * 5);
            this.rightWalking.restart();
            this.upWalking.restart();
            this.downWalking.restart();
            this.updateTile();
        }
    }

    public void serialize(TPObject object) {
        object.setName("Player");
        object.addField(TPField.Integer("ID", this.userID));
        object.addField(TPField.Integer("x", this.x));
        object.addField(TPField.Integer("y", this.y));
        object.addField(TPField.Short("currentTile", this.currentTile.id));
        object.addField(TPField.Short("turnState", this.turnState.id));
        object.addField(
                TPField.Byte("roll", (byte) StateManager.game.dice.roll));
        object.addField(TPField.Boolean("playStep", this.playStep));
        object.addField(TPField.Short("direction", this.toDirection.id));
    }

}
