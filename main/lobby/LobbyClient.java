package main.lobby;

import serialization.*;

public class LobbyClient {
    public byte userID;
    public boolean ready = false;
    public byte roll = (byte) (1 + 5 * Math.random());

    public LobbyClient(byte id) {
        this.userID = id;
    }

    public TPDatabase serialize() {
        TPDatabase temp = new TPDatabase("LobbyState");
        TPObject object = new TPObject("Client");
        object.addField(TPField.Boolean("ready", this.ready));
        object.addField(TPField.Integer("ID", this.userID));
        object.addField(TPField.Byte("roll", this.roll));
        temp.addObject(object);
        return temp;
    }
}
