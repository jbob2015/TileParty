package main;

import java.net.*;
import java.util.*;

public class ServerClient {

    public InetAddress address;
    public int port;
    public boolean status = false;
    public int userID;
    public static ArrayList<Integer> freeIDs = new ArrayList<Integer>();
    public static int userIDCounter = 1;
    public int attempt = 0;
    public boolean ready = false;
    public byte roll = 0;

    public ServerClient(InetAddress address, int port) {
        if (freeIDs.size() > 0) {
            this.userID = freeIDs.remove(0);
        }
        this.address = address;
        this.port = port;
        this.status = true;
    }

    public static void init(int maxPlayers) {
        freeIDs = new ArrayList<Integer>();
        for (int i = 1; i <= maxPlayers; i++) {
            freeIDs.add(i);
        }
    }

    @Override
    public int hashCode() {
        return this.userID;
    }

    public static boolean canConnect() {
        return freeIDs.size() > 0;
    }

}
