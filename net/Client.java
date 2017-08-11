package net;

import java.io.*;
import java.net.*;

import helpers.*;
import helpers.StateManager.*;
import main.game.Bombed.*;
import main.game.Bombed.Bomb.*;
import serialization.*;

public class Client {
    public static final int MAX_PACKET_SIZE = 1028;
    private byte[] recievedDateBuffer = new byte[MAX_PACKET_SIZE * 10];
    private String ipAddress;
    private int port;

    private InetAddress serverAddress;
    private DatagramSocket socket;

    private Thread listenThread;
    public boolean listening = false;
    public volatile boolean connected = false;
    public byte userID;

    public static byte MAX_PLAYERS;

    public Client(String host) {
        this.ipAddress = host;
        String[] parts = host.split(":");
        if (parts.length != 2) {
            return;
        }
        this.ipAddress = parts[0];
        try {
            this.port = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        this.listening = true;
    }

    public Client(String host, int port) {
        this.ipAddress = host;
        this.port = port;

        this.listening = true;

    }

    public boolean connect() {

        this.listenThread = new Thread(() -> this.listen(),
                "Client ListenThread");

        try {
            this.serverAddress = InetAddress.getByName(this.ipAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }

        try {
            this.socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        }

        this.listenThread.start();

        this.sendConnectionPacket();

        long time = System.currentTimeMillis() + 2000;
        while (!this.connected) {
            if (time < System.currentTimeMillis()) {
                break;
            }
        }
        return true;
    }

    private void listen() {
        while (this.listening) {
            DatagramPacket packet = new DatagramPacket(this.recievedDateBuffer,
                    MAX_PACKET_SIZE);
            try {
                this.socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.process(packet);
        }
    }

    private void dump(DatagramPacket packet) {
        byte[] data = packet.getData();
        InetAddress address = packet.getAddress();
        int port = packet.getPort();

        System.out.println("------------------------------------------");
        System.out.print("PACKET: " + address.toString() + ":" + port);
        System.out.println("\n\t" + new String(data));
        System.out.println("------------------------------------------");
    }

    private void process(DatagramPacket packet) {
        byte[] data = packet.getData();

        if (new String(data, 0, 4).equals("TPDB")) {
            TPDatabase database = TPDatabase.Deserialize(data);
            this.process(database);
        } else {
            switch (data[0]) {
                case 'A':
                    //Connection packet
                    System.out.println("Confirmed Connection To Server");
                    this.connected = true;
                    this.userID = data[3];
                    Client.MAX_PLAYERS = data[4];
                    System.out.println("I am Player #" + this.userID + "/"
                            + Client.MAX_PLAYERS);
                    break;
                case 'S':
                    //Bomb Side packet
                    Bombed.bomb.explode(data[4] == 0 ? BombLocation.LEFT
                            : BombLocation.RIGHT);
                    break;
                case 'D':
                    //Disconnect packet
                    TPDatabase dbD = new TPDatabase("DC");
                    TPObject objD = new TPObject("DC");
                    System.out.println("Player " + data[2] + " disconnected");
                    objD.addField(TPField.Integer("ID", data[2]));
                    dbD.addObject(objD);
                    this.process(dbD);
                    break;
                case 'G':
                    TPDatabase dbG = new TPDatabase("G");
                    this.process(dbG);
                    break;
                case 'F':
                    //Server Full Packet
                    System.out.println("Server is Full");
                    StateManager.changeState(GameState.TITLE);
                    StateManager.game = null;
                    break;
                case '~':
                    //Final Server Packet
                    System.out.println("Server ended");
                    StateManager.changeState(GameState.TITLE);
                    StateManager.game = null;
                    break;
            }
        }
    }

    private void dump(TPDatabase database) {
        System.out.println("------------------------------------------");
        System.out.println("----------------TPDatabase----------------");
        System.out.println("------------------------------------------");
        System.out.println("Name: " + database.getName());
        System.out.println("Size: " + database.getSize());
        System.out.println("Object Count: " + database.objects.size());
        for (TPObject o : database.objects) {
            System.out.println("\tObject");
            System.out.println("\t\tName: " + o.getName());
            System.out.println("\t\tSize: " + o.getSize());
            System.out.println("\t\tField Count: " + o.fields.size());
            System.out.println("\t\tString Count: " + o.strings.size());
            System.out.println("\t\tArray Count: " + o.arrays.size());
        }
        System.out.println("------------------------------------------");
    }

    private void process(TPDatabase database) {
        StateManager.packets.add(database);

    }

    private void sendConnectionPacket() {
        byte[] data = { 1, (byte) (Byte.MAX_VALUE * Math.random()),
                (byte) (Byte.MAX_VALUE * Math.random()),
                (byte) (Byte.MAX_VALUE * Math.random()) };
        this.send(data);
    }

    public void send(byte[] data) {
        assert (this.socket.isConnected());
        DatagramPacket packet = new DatagramPacket(data, data.length,
                this.serverAddress, this.port);
        try {
            this.socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(TPDatabase database) {
        assert (this.socket.isConnected());
        byte[] data = new byte[database.getSize()];
        database.getBytes(data, 0);
        this.send(data);
    }

}
