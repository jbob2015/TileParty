package main;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Queue;

import serialization.*;

public class Server {

    private int port;
    private Thread listenThread, manageThread;
    public volatile boolean listening = false;
    private DatagramSocket socket;
    public static final int MAX_PACKET_SIZE = 1024;
    private byte[] recievedDateBuffer = new byte[MAX_PACKET_SIZE * 40];
    public static final int MAX_ATTEMPTS = 5;
    public static final int MAX_LOST_ATTEMPTS = 30;
    public static int MAX_PLAYERS;
    private byte[] turnOrder;

    private java.util.List<ServerClient> clients = Collections
            .synchronizedList(new ArrayList<ServerClient>());
    private java.util.List<ServerClient> lostClients = Collections
            .synchronizedList(new ArrayList<ServerClient>());
    private java.util.List<Integer> clientResponse = Collections
            .synchronizedList(new ArrayList<Integer>());
    private volatile GameState state = GameState.GAME;

    private boolean gameStarted = false;
    public Queue<Byte> turnQueue = new LinkedList<Byte>();
    public byte currentPlayer;
    private int turnCount = 1;
    private byte[] startMessage = "GAMESTART".getBytes();

    public static enum GameState {
        GAME, MINITANK, BOMBED, BUBBLES, LOBBY
    }

    public enum TurnState {

        WAITING(0),
        DECIDING(1),
        ROLLING(2),
        MOVING(3),
        MOVE_DECISION(4),
        ENDING(5);

        public short id;

        TurnState(int id) {
            this.id = (short) id;
        }

        public static TurnState create(short id) {
            for (TurnState t : TurnState.values()) {
                if (t.id == id) {
                    return t;
                }
            }
            return null;
        }

    }

    public Server(int port) {
        this.port = port;
    }

    public boolean start(int playerCount) {
        Server.MAX_PLAYERS = playerCount;
        ServerClient.init(Server.MAX_PLAYERS);
        this.turnOrder = new byte[MAX_PLAYERS];
        try {
            this.socket = new DatagramSocket(this.port);
        } catch (BindException e) {
            TilePartyServer.println("Server already opened on port 8192");
            return false;
        } catch (SocketException e) {
            TilePartyServer.println(getStackTrace(e));
            return false;
        }

        this.listening = true;

        this.listenThread = new Thread(() -> this.listen(),
                "TPServer ListenThread");

        this.listenThread.start();
        return true;
    }

    //TODO
    public void end() {
        this.listening = false;
        this.manageThread.interrupt();
        this.listenThread.interrupt();
        this.recievedDateBuffer = new byte[MAX_PACKET_SIZE * 40];
        byte[] message = { '~' };
        for (ServerClient client : this.clients) {
            this.send(message, client.address, client.port);
        }
        this.clients.clear();
        this.lostClients.clear();
        TilePartyServer.println("All Clients Disconnected");
        this.socket.close();
        TilePartyServer.println("Server ended");
        System.out.println("Server ended");
    }

    private void manageClients() {
        this.manageThread = new Thread("TPServer ManageThread") {
            @Override
            public void run() {
                while (Server.this.listening) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                    }
                    for (int i = 0; i < Server.this.clients.size(); i++) {
                        ServerClient c = Server.this.clients.get(i);
                        if (!Server.this.clientResponse
                                .contains(new Integer(c.userID))) {
                            if (c.attempt >= MAX_ATTEMPTS) {
                                Server.this.disconnect(c.userID, false);
                                c.attempt = 0;
                            } else {
                                c.attempt++;
                                TilePartyServer.println("Player " + c.userID
                                        + " did not respond: " + c.attempt * 2
                                        + "s");
                            }
                        } else {
                            Server.this.clientResponse
                                    .remove(new Integer(c.userID));
                            c.attempt = 0;
                        }
                    }
                    for (int i = 0; i < Server.this.lostClients.size(); i++) {
                        ServerClient c = Server.this.lostClients.get(i);
                        if (!Server.this.clientResponse
                                .contains(new Integer(c.userID))) {
                            if (c.attempt >= MAX_LOST_ATTEMPTS) {
                                Server.this.lostClients.remove(i);
                                TilePartyServer.println("Player " + c.userID
                                        + " disconnected for the remainder of the game");
                                byte[] disconnect = { 'D', 'C',
                                        (byte) c.userID };

                                for (int j = 0; j < Server.this.clients
                                        .size(); j++) {
                                    ServerClient temp = Server.this.clients
                                            .get(j);
                                    Server.this.send(disconnect, temp.address,
                                            temp.port);
                                }
                            } else {
                                //TODO
                                boolean print = false;
                                if (c.attempt == 0) {
                                    print = true;
                                } else if (c.attempt == 15) {
                                    print = true;
                                } else if (c.attempt >= 25) {
                                    print = true;
                                }
                                if (print) {
                                    TilePartyServer.println("Player " + c.userID
                                            + " has "
                                            + (MAX_LOST_ATTEMPTS - c.attempt)
                                                    * 2
                                            + " seconds to reconnect");
                                }
                                c.attempt++;
                            }
                        } else {
                            Server.this.clientResponse
                                    .remove(new Integer(c.userID));
                            c.attempt = 0;
                        }
                    }
                }
            }
        };
        this.manageThread.start();
    }

    private void disconnect(int id, boolean status) {
        ServerClient c = null;
        boolean existed = false;
        for (int i = 0; i < this.clients.size(); i++) {
            if (this.clients.get(i).userID == id) {
                c = this.clients.get(i);
                this.clients.remove(i);
                this.lostClients.add(c);
                existed = true;
                break;
            }
        }
        if (!existed) {
            return;
        }
        String message = "Client " + c.userID + " - ("
                + c.address.toString().substring(1) + ":" + c.port + ")";
        if (status) {
            message += " disconnected.";
        } else {
            message += " timed out.";
        }
        TilePartyServer.println(message);
    }

    private void process(DatagramPacket packet) {
        byte[] data = packet.getData();

        if (new String(data, 0, 4).equals("TPDB")) {
            TPDatabase database = TPDatabase.Deserialize(data);
            this.process(database);
        } else {
            switch (data[0]) {
                case 1:
                    //Connection packet
                    this.handleConnection(packet, data);
                    break;
            }
        }
    }

    private void process(TPDatabase database) {
        switch (database.getName()) {
            case "GameState":
                if (database.objects.get(0).findField("ID")
                        .getInt() == this.currentPlayer) {
                    if (database.objects.get(0).findField("turnState")
                            .getShort() == TurnState.ENDING.id) {
                        if (this.turnQueue.size() == 0) {
                            this.initTurnQueue();
                            this.turnCount++;
                        }
                        this.currentPlayer = this.turnQueue.poll();
                    }
                } else {
                    if (database.objects.get(0).findField("turnState")
                            .getShort() != TurnState.WAITING.id) {
                        System.out
                                .println("Player "
                                        + database.objects.get(0)
                                                .findField("ID").getInt()
                                        + " is wrong");
                    }
                }

                TPDatabase currentTurnInfo = new TPDatabase("TurnInfo");
                TPObject object = new TPObject("TurnInfo");
                object.addField(
                        TPField.Integer("currentPlayer", this.currentPlayer));
                object.addField(TPField.Integer("turnCount", this.turnCount));
                currentTurnInfo.addObject(object);
                for (ServerClient client : this.clients) {
                    this.send(currentTurnInfo, client.address, client.port);
                }

                this.state = GameState.GAME;
                break;
            case "BombedState":
                if (database.objects.get(0).findField("canBomb").getBoolean()) {
                    byte side = (byte) ((Math.random() > .5) ? 0 : 1);
                    byte[] message = { 'S', 'I', 'D', 'E', side };
                    for (ServerClient client : this.clients) {
                        this.send(message, client.address, client.port);
                    }
                }
                this.state = GameState.BOMBED;
                break;
            case "BubblesState":
                this.state = GameState.BUBBLES;
                break;
            case "LobbyState":
                //TODO can crash if game isn't started...
                if (!this.gameStarted) {
                    this.state = GameState.LOBBY;
                    ServerClient currentClient = this.getClient(
                            database.objects.get(0).findField("ID").getInt());
                    currentClient.ready = database.objects.get(0)
                            .findField("ready").getBoolean();
                    currentClient.roll = database.objects.get(0)
                            .findField("roll").getByte();
                    if (this.allReady()) {
                        for (int i = 0; i < this.turnOrder.length; i++) {
                            this.turnOrder[i] = (byte) this.clients
                                    .get(i).userID;
                        }
                        for (int i = 1; i < this.turnOrder.length; i++) {
                            for (int j = i; j > 0; j--) {
                                ServerClient client1 = this
                                        .getClient(this.turnOrder[j]);
                                ServerClient client2 = this
                                        .getClient(this.turnOrder[j - 1]);
                                if (client1.roll > client2.roll) {
                                    byte tempByte = this.turnOrder[j];
                                    this.turnOrder[j] = this.turnOrder[j - 1];
                                    this.turnOrder[j - 1] = tempByte;
                                }
                            }
                        }
                        for (ServerClient client : this.clients) {
                            this.send(this.startMessage, client.address,
                                    client.port);
                        }
                        System.out.println(Arrays.toString(this.turnOrder));
                        this.gameStarted = true;
                        this.initTurnQueue();
                        this.currentPlayer = this.turnQueue.poll();
                        this.state = GameState.GAME;
                    }
                } else {
                    ServerClient client = this.getClient(
                            database.objects.get(0).findField("ID").getInt());
                    this.send(this.startMessage, client.address, client.port);
                }
        }

        int temp = database.objects.get(0).findField("ID").getInt();
        for (ServerClient client : this.clients) {
            if (temp != client.userID) {
                this.send(database, client.address, client.port);
            }
        }
        if (!this.clientResponse.contains(new Integer(temp))) {
            this.clientResponse.add(temp);
        }
    }

    private void initTurnQueue() {
        for (int i = 0; i < MAX_PLAYERS; i++) {
            byte temp = this.turnOrder[i];
            this.turnQueue.add(temp);
        }
    }

    private boolean allReady() {
        for (ServerClient client : this.clients) {
            if (!client.ready) {
                return false;
            }
        }
        return this.clients.size() == MAX_PLAYERS && !this.gameStarted;
    }

    private boolean hasClient(int userID) {
        for (int i = 0; i < this.clients.size(); i++) {
            ServerClient client = this.clients.get(i);
            if (client.userID == userID) {
                return true;
            }
        }
        return false;
    }

    private ServerClient getClient(int userID) {
        for (int i = 0; i < this.clients.size(); i++) {
            ServerClient client = this.clients.get(i);
            if (client.userID == userID) {
                return client;
            }
        }
        return null;
    }

    public int getClientID(InetAddress address) {
        for (int i = 0; i < this.clients.size(); i++) {
            ServerClient client = this.clients.get(i);
            if (client.address.equals(address)) {
                return client.userID;
            }
        }
        return 0;
    }

    private boolean hadClient(InetAddress inetAddress) {
        for (int i = 0; i < this.lostClients.size(); i++) {
            ServerClient client = this.lostClients.get(i);
            if (client.address.equals(inetAddress)) {
                return true;
            }
        }
        return false;
    }

    private ServerClient getOldClient(InetAddress inetAddress) {
        for (int i = 0; i < this.lostClients.size(); i++) {
            ServerClient client = this.lostClients.get(i);
            if (client.address.equals(inetAddress)) {
                return this.lostClients.remove(i);
            }
        }
        return null;
    }

    public void send(byte[] data, InetAddress address, int port) {
        assert (this.socket.isConnected());

        DatagramPacket packet = new DatagramPacket(data, data.length, address,
                port);
        try {
            this.socket.send(packet);
        } catch (IOException e) {
            TilePartyServer.println(getStackTrace(e));
        }
    }

    public void send(TPDatabase database, InetAddress address, int port) {
        assert (this.socket.isConnected());
        byte[] data = new byte[database.getSize()];
        database.getBytes(data, 0);
        this.send(data, address, port);
    }

    public static String getStackTrace(Exception e) {
        e.printStackTrace();

        TilePartyServer.print.setForeground(Color.RED);
        return e.getMessage();
    }

    private void dump(DatagramPacket packet) {
        byte[] data = packet.getData();
        InetAddress address = packet.getAddress();
        int port = packet.getPort();

        TilePartyServer.println("------------------------------------------");
        TilePartyServer.print("PACKET: " + address.toString() + ":" + port);
        TilePartyServer.println("\n\t" + new String(data));
        TilePartyServer.println("------------------------------------------");
    }

    private void dump(TPDatabase database) {
        TilePartyServer.println("------------------------------------------");
        TilePartyServer.println("----------------TPDatabase----------------");
        TilePartyServer.println("------------------------------------------");
        TilePartyServer.println("Name: " + database.getName());
        TilePartyServer.println("Size: " + database.getSize());
        TilePartyServer.println("Object Count: " + database.objects.size());
        for (TPObject o : database.objects) {
            TilePartyServer.println("\tObject");
            TilePartyServer.println("\t\tName: " + o.getName());
            TilePartyServer.println("\t\tSize: " + o.getSize());
            TilePartyServer.println("\t\tField Count: " + o.fields.size());
            TilePartyServer.println("\t\tString Count: " + o.strings.size());
            TilePartyServer.println("\t\tArray Count: " + o.arrays.size());
        }
        TilePartyServer.println("------------------------------------------");
    }

    private void handleConnection(DatagramPacket packet, byte[] data) {
        if (ServerClient.canConnect() || this.lostClients.size() > 0) {
            ServerClient temp = null;
            if (!this.hadClient(packet.getAddress())) {
                temp = new ServerClient(packet.getAddress(), packet.getPort());
                this.clients.add(temp);
                this.send(
                        new byte[] { 'A', 'O', 'K', (byte) temp.userID,
                                (byte) MAX_PLAYERS },
                        packet.getAddress(), packet.getPort());
                TilePartyServer
                        .println("Client #" + temp.userID + " connected. ("
                                + temp.userID + "/" + Server.MAX_PLAYERS + ")");
            } else {
                temp = this.getOldClient(packet.getAddress());
                temp.port = packet.getPort();
                TilePartyServer
                        .println("Client #" + temp.userID + " reconnected. ("
                                + temp.userID + "/" + Server.MAX_PLAYERS + ")");
                this.clients.add(temp);
                this.send(
                        new byte[] { 'A', 'O', 'K', (byte) temp.userID,
                                (byte) MAX_PLAYERS },
                        packet.getAddress(), packet.getPort());
                if (this.gameStarted) {
                    this.send(this.startMessage, packet.getAddress(),
                            packet.getPort());
                }
            }
        } else {
            this.send(new byte[] { 'F', 'U', 'L', 'L' }, packet.getAddress(),
                    packet.getPort());
        }
    }

    private void listen() {
        TilePartyServer.println("Server Started Successfully");
        this.manageClients();
        while (this.listening) {
            DatagramPacket packet = new DatagramPacket(this.recievedDateBuffer,
                    MAX_PACKET_SIZE);
            try {
                this.socket.receive(packet);
            } catch (SocketException e) {
            } catch (IOException e) {
                TilePartyServer.println(getStackTrace(e));
            }
            this.process(packet);
        }
    }
}
