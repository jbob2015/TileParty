package serialization;

import java.util.*;

public class Main {

    static Random random = new Random();

    static void printBytes(byte[] data) {
        for (int i = 0; i < data.length; i++) {
            System.out.printf("0x%x ", data[i]);
        }
    }

    public static void serializationTest() {
        int[] data = new int[50000];
        for (int i = 0; i < data.length; i++) {
            data[i] = random.nextInt();
        }

        TPDatabase database = new TPDatabase("Database");
        TPArray array = TPArray.Integer("RandomNumbers", data);
        TPField field = TPField.Integer("Integer", 8);
        TPField positionx = TPField.Short("xpos", (short) 2);
        TPField positiony = TPField.Short("ypos", (short) 43);

        TPObject object = new TPObject("Entity");
        object.addArray(array);
        object.addArray(TPArray.Char("String", "Hello World!".toCharArray()));
        object.addField(field);
        object.addField(positionx);
        object.addField(positiony);
        object.addString(TPString.Create("Example String",
                "Testing our RCString class!"));

        database.addObject(object);
        database.addObject(new TPObject("Cherno"));
        database.addObject(new TPObject("Cherno1"));
        TPObject c = new TPObject("Cherno2");
        c.addField(TPField.Boolean("a", false));
        database.addObject(c);
        database.addObject(new TPObject("Cherno3"));

        database.serializeToFile("test.rcd");
    }

    public static void deserializationTest() {
        TPDatabase database = TPDatabase.DeserializeFromFile("test.rcd");
        System.out.println("Database: " + database.getName());
        for (TPObject object : database.objects) {
            System.out.println("\t" + object.getName());
            for (TPField field : object.fields) {
                System.out.println("\t\t" + field.getName());
            }
            System.out.println();
            for (TPString string : object.strings) {
                System.out.println(
                        "\t\t" + string.getName() + " = " + string.getString());
            }
            System.out.println();
            for (TPArray array : object.arrays) {
                System.out.println("\t\t" + array.getName());
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        serializationTest();
        deserializationTest();
        Sandbox sandbox = new Sandbox();
        sandbox.play();
    }

}