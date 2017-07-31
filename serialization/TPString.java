package serialization;

import static serialization.SerializationHandler.*;

public class TPString extends TPBase {

    public static final byte CONTAINER_TYPE = StorageType.STRING;
    public int count;
    private char[] characters;

    private TPString() {
        this.size += 1 + 4;
    }

    public String getString() {
        return new String(this.characters);
    }

    private void updateSize() {
        this.size += this.getDataSize();
    }

    public int getBytes(byte[] dest, int pointer) {
        pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
        pointer = writeBytes(dest, pointer, this.nameLength);
        pointer = writeBytes(dest, pointer, this.name);
        pointer = writeBytes(dest, pointer, this.size);
        pointer = writeBytes(dest, pointer, this.count);
        pointer = writeBytes(dest, pointer, this.characters);
        return pointer;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    public int getDataSize() {
        return this.characters.length * Type.getSize(Type.CHAR);
    }

    public static TPString Create(String name, String data) {
        TPString string = new TPString();
        string.setName(name);
        string.count = data.length();
        string.characters = data.toCharArray();
        string.updateSize();
        return string;
    }

    public static TPString Deserialize(byte[] data, int pointer) {
        byte containerType = data[pointer++];
        assert (containerType == CONTAINER_TYPE);

        TPString result = new TPString();
        result.nameLength = readShort(data, pointer);
        pointer += 2;
        result.name = readString(data, pointer, result.nameLength).getBytes();
        pointer += result.nameLength;

        result.size = readInt(data, pointer);
        pointer += 4;

        result.count = readInt(data, pointer);
        pointer += 4;

        result.characters = new char[result.count];
        readChars(data, pointer, result.characters);

        pointer += result.count * Type.getSize(Type.CHAR);
        return result;
    }

}
