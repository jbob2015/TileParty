package serialization;

import static serialization.SerializationHandler.*;

import java.util.ArrayList;
import java.util.List;

public class TPObject extends TPBase {

	public static final byte CONTAINER_TYPE = StorageType.OBJECT;
	private short fieldCount;
	public List<TPField> fields = new ArrayList<TPField>();
	private short stringCount;
	public List<TPString> strings = new ArrayList<TPString>();
	private short arrayCount;
	public List<TPArray> arrays = new ArrayList<TPArray>();
	
	private TPObject() {
	}
	
	public TPObject(String name) {
		size += 1 + 2 + 2 + 2;
		setName(name);
	}
	
	public void addField(TPField field) {
		fields.add(field);
		size += field.getSize();
		
		fieldCount = (short)fields.size();
	}
	
	public void addString(TPString string) {
		strings.add(string);
		size += string.getSize();
		
		stringCount = (short)strings.size();
	}

	public void addArray(TPArray array) {
		arrays.add(array);
		size += array.getSize();
		
		arrayCount = (short)arrays.size();
	}
	
	public int getSize() {
		return size;
	}
	
	public TPField findField(String name) {
		for (TPField field : fields) {
			if (field.getName().equals(name))
				return field;
		}
		return null;
	}
	
	public TPString findString(String name) {
		for (TPString string : strings) {
			if (string.getName().equals(name))
				return string;
		}
		return null;
	}

	public TPArray findArray(String name) {
		for (TPArray array : arrays) {
			if (array.getName().equals(name))
				return array;
		}
		return null;
	}
	
	public int getBytes(byte[] dest, int pointer) {
		pointer = writeBytes(dest, pointer, CONTAINER_TYPE);
		pointer = writeBytes(dest, pointer, nameLength);
		pointer = writeBytes(dest, pointer, name);
		pointer = writeBytes(dest, pointer, size);
		
		pointer = writeBytes(dest, pointer, fieldCount);
		for (TPField field : fields)
			pointer = field.getBytes(dest, pointer);
		
		pointer = writeBytes(dest, pointer, stringCount);
		for (TPString string : strings)
			pointer = string.getBytes(dest, pointer);
		
		pointer = writeBytes(dest, pointer, arrayCount);
		for (TPArray array : arrays)
			pointer = array.getBytes(dest, pointer);
		
		return pointer;
	}
	
	public static TPObject Deserialize(byte[] data, int pointer) {
		byte containerType = data[pointer++];
		assert(containerType == CONTAINER_TYPE);
		
		TPObject result = new TPObject();
		result.nameLength = readShort(data, pointer);
		pointer += 2;
		result.name = readString(data, pointer, result.nameLength).getBytes();
		pointer += result.nameLength;
		
		result.size = readInt(data, pointer);
		pointer += 4;
		
		// Early-out: pointer += result.size - sizeOffset - result.nameLength;
		
		result.fieldCount = readShort(data, pointer);
		pointer += 2;
		
		for (int i = 0; i < result.fieldCount; i++) {
			TPField field = TPField.Deserialize(data, pointer);
			result.fields.add(field);
			pointer += field.getSize();
		}
		
		result.stringCount = readShort(data, pointer);
		pointer += 2;

		for (int i = 0; i < result.stringCount; i++) {
			TPString string = TPString.Deserialize(data, pointer);
			result.strings.add(string);
			pointer += string.getSize();
		}

		result.arrayCount = readShort(data, pointer);
		pointer += 2;

		for (int i = 0; i < result.arrayCount; i++) {
			TPArray array = TPArray.Deserialize(data, pointer);
			result.arrays.add(array);
			pointer += array.getSize();
		}
		
		return result;
	}
	
}
