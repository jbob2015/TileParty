package helpers;

import static helpers.Handler.*;

import java.util.*;
import java.util.concurrent.*;

import main.*;
import main.editor.*;
import main.game.*;
import main.game.Game.*;
import main.game.Bombed.*;
import main.game.Bubbles.*;
import main.game.minitank.*;
import main.lobby.*;
import net.*;
import serialization.*;

public class StateManager {

    public static Queue<TPDatabase> packets = new ConcurrentLinkedQueue<TPDatabase>();

    public static enum GameState {
        TITLE,
        LOBBY,
        GAME,
        EDITOR,
        GAMEPAUSED,
        EDITORPAUSED,
        MINITANK,
        BOMBED,
        TANKPAUSED,
        BUBBLES
    }

    public static volatile GameState state = GameState.TITLE;
    // Create Map To Be Used in Game/Editor
    public static TileGrid map = //new TileGrid(T_WIDTH, T_HEIGHT);
            TileGrid.deserialize(TPDatabase.DeserializeFromFile("mapData.tp"));

//    public static Client client = new Client("68.0.212.220", 8192);
    public static Client client = new Client("localhost", 8192);
    public static TPDatabase database;

    public static ArrayList<Integer> sounds = new ArrayList<>();
    public static Source source = new Source();
    public static boolean changeSong = false;

    public static Title title;
    public static Lobby lobby;
    public static Game game;
    public static Editor editor;
    public static Bombed bombed;
    public static Bubbles bubbles;
    public static MiniTank minitank;

    public static GamePaused gamePaused;
    public static EditorPaused editorPaused;
    public static TankPaused tankPaused;

    public StateManager() {
        // Initialize Mouse Handler
        new Mouse();
        //Initialize Keyboard Handler
        new Keyboard();
        // Load all Sounds
        loadSounds();
    }

    /**
     * Loads all songs/sounds into the game
     */
    private static void loadSounds() {
        sounds.add(loadSound("fine"));
        sounds.add(loadSound("cecilia"));
        sounds.add(loadSound("crazy"));
        sounds.add(loadSound("ispy"));
    }

    /**
     * Deletes the source for music
     */
    protected static void deleteSource() {
        source.delete();
    }

    /**
     * Changes the GameState
     *
     * @param s
     *            state to change to
     */
    public static void changeState(GameState s) {
        if (state == GameState.GAME && s == GameState.BOMBED
                || s == GameState.BUBBLES) {
            //Will Have to Update For Other Minigames
            database = new TPDatabase("GameState");
            StateManager.game.serialize(database);
            client.send(database);
        }
        switch (s) {
            case BOMBED:
                break;
            case BUBBLES:
                break;
            case EDITOR:
                title = null;
                lobby = null;
                gamePaused = null;
                break;
            case EDITORPAUSED:
                break;
            case GAME:
                title = null;
                gamePaused = null;
                lobby = null;
                bombed = null;
                bubbles = null;
                Lobby.packets.clear();
                break;
            case GAMEPAUSED:
                break;
            case LOBBY:
                break;
            case TITLE:
                game = null;
                Game.packets.clear();
                bombed = null;
                //Bombed.packets.clear();
                bubbles = null;
                //Bubbles.packets.clear();
                break;
            default:
                break;

        }

        //TODO SWITCH FOR NULL STATES

        if (s != GameState.BOMBED) {
            Mouse.showCursor();
        } else {
            Mouse.hideCursor();
        }
        state = s;
        changeSong = true;
    }

    /**
     * Updates the program based on what state it is in
     */
    public void update() {
        Mouse.update();
        Keyboard.update();
        this.updatePackets();

        switch (state) {
            case TITLE:
                updateTitle();
                break;
            case LOBBY:
                updateLobby();
                break;
            case GAME:
                updateGame();
                break;
            case BOMBED:
                updateBombed();
                break;
            case BUBBLES:
                this.updateBubbles();
                break;
            case MINITANK:
                updateMiniTank();
                break;
            case EDITOR:
                updateEditor();
                break;
            case GAMEPAUSED:
                updateGamePaused();
                break;
            case EDITORPAUSED:
                updateEditorPaused();
                break;
            case TANKPAUSED:
                updateTankPaused();
                break;
            default:
                break;
        }
    }

    public void draw() {
        switch (state) {
            case TITLE:
                if (title != null) {
                    title.draw();
                }
                break;
            case LOBBY:
                if (lobby != null) {
                    lobby.draw();
                }
                break;
            case GAME:
                if (game != null) {
                    game.draw();
                }
                break;
            case BOMBED:
                if (bombed != null) {
                    bombed.draw(); //TODO Test
                }
                break;
            case BUBBLES:
                if (bubbles != null) {
                    bubbles.draw();
                }
                break;
            case EDITOR:
                if (editor != null) {
                    editor.draw();
                }
                break;
            case GAMEPAUSED:
                game.draw();
                if (gamePaused != null) {
                    gamePaused.draw();
                }
                break;
            case EDITORPAUSED:
                editor.draw();
                if (editorPaused != null) {
                    editorPaused.draw();
                }
                break;
            default:
                break;
        }
    }

    private void updatePackets() {
        for (TPDatabase database : StateManager.packets) {
            switch (database.getName()) {
                case "GameState":
                    Game.packets.add(database);
                    break;
                case "BombedState":
                    Bombed.packets.add(database);
                    break;
                case "BubblesState":
                    Bubbles.packets.add(database);
                case "LobbyState":
                    Lobby.packets.add(database);
                    break;
                case "DC":
                    StateManager.game.removePlayer(
                            database.objects.get(0).findField("ID").getInt());
                    if (StateManager.bombed != null) {
                        Bombed.removePlayer(database.objects.get(0)
                                .findField("ID").getInt());
                    }
                    if (StateManager.bubbles != null) {
                        Bubbles.removePlayer(database.objects.get(0)
                                .findField("ID").getInt());
                    }
                    break;
                case "G":
                    StateManager.changeState(GameState.GAME);
                    break;
                case "TurnInfo":
                    if (game != null) {
                        game.turnCount = database.objects.get(0)
                                .findField("turnCount").getInt();

                        int temp = database.objects.get(0)
                                .findField("currentPlayer").getInt();
                        if (game.currentPlayer != 0
                                && game.currentPlayer != temp) {
                            System.out.println(
                                    "Current Player is: Player " + temp);
                            game.handleTileAction(
                                    game.getPlayer(game.currentPlayer));
                        }

                        game.currentPlayer = temp; //TODO
                        if (game.currentPlayer != client.userID
                                && game.player.turnState != TurnState.WAITING) {
                            game.player.turnState = TurnState.WAITING;
                        }
                    }
                    break;

            }
        }
        StateManager.packets.clear();

    }

    /**
     * Changes the current song
     *
     * @param song
     *            index of song in StateManager.sounds (-1 to stop all)
     */
    private static void changeSong(int song) {
        for (int s : sounds) {
            if (song == -1 || s != sounds.get(song)) {
                source.stop(s);
            }
        }
        if (song != -1) {
            source.play(sounds.get(song));
        }
    }

    /**
     * Updates Title
     */
    private static void updateTitle() {
        //Song = 0
        if (title == null) {
            title = new Title();
            source.play(sounds.get(0));
        }
        if (changeSong) {
            changeSong = !changeSong;
            changeSong(0);
        }
        title.update();
    }

    /**
     * Updates Editor
     */
    private static void updateEditor() {
        //Song = 1
        if (editor == null) {
            editor = new Editor(map);
        }
        if (changeSong) {
            changeSong = !changeSong;
            changeSong(1);
        }
        editor.update();
    }

    /**
     * Updates Game
     */
    private static void updateLobby() {
        //Song = 2
        if (lobby == null) {
            client.connect();
            if (client.connected) {
                lobby = new Lobby(client.userID);
                Handler.changeCursor(client.userID);
            } else {
                StateManager.changeState(GameState.TITLE);
                return;
            }
        }
        if (changeSong) {
            changeSong = !changeSong;
            changeSong(-1);
        }
        client.send(lobby.serialize());
        lobby.update();
    }

    /**
     * Updates Game
     */
    private static void updateGame() {
        //Song = 2
        if (game == null) {
            if (client.connected) {
                Handler.changeCursor(client.userID);
                game = new Game(map, client.userID);
            } else {
                StateManager.changeState(GameState.TITLE);
                return;
            }
        }
        if (changeSong) {
            changeSong = !changeSong;
            changeSong(-1);
        }
        game.update();
        database = new TPDatabase("GameState");
        StateManager.game.serialize(database);
        client.send(database);
    }

    /**
     * Updates GamePause
     */
    private static void updateGamePaused() {
        //Song = 3
        if (gamePaused == null) {
            gamePaused = new GamePaused();
        }
        if (changeSong) {
            changeSong = !changeSong;
            changeSong(3);
        }
        game.update();
        database = new TPDatabase("GameState");
        StateManager.game.serialize(database);
        client.send(database);
        gamePaused.update();
    }

    private static void updateMiniTank() {
    }

    private static void updateBombed() {
        //Song = 2
        if (bombed == null) {
            bombed = new Bombed();
        }
        if (changeSong) {
            changeSong = !changeSong;
            changeSong(-1);
            bombed.reset();
        }
        bombed.update();
        database = new TPDatabase("BombedState");
        Bombed.serialize(database);
        client.send(database);
    }

    private void updateBubbles() {
        //Song = 2
        if (bubbles == null) {
            bubbles = new Bubbles();
        }
        if (changeSong) {
            changeSong = !changeSong;
            changeSong(-1);
        }
        bubbles.update();
        database = new TPDatabase("BubblesState");
        Bubbles.serialize(database);
        client.send(database);
    }

    /**
     * Updates EditorPaused
     */
    private static void updateEditorPaused() {
        //Song = ?
        if (editorPaused == null) {
            editorPaused = new EditorPaused();
        }
        if (changeSong) {
            changeSong = !changeSong;
            changeSong(-1);
        }
        editor.update();
        editorPaused.update();
    }

    private static void updateTankPaused() {
        if (tankPaused == null) {
            tankPaused = new TankPaused();
        }
        tankPaused.update();
    }
}
