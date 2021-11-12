package info;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to store a tag key with its value and the list of all tag keys
 * @author Saikarthik Tirumalareddy
 * @author Tianqi Li
 */
public class Tag implements Serializable{
	
	/**
	 * ID for Serialization
	 */
	private static final long serialVersionUID = 64845134L;
	
	/**
	 * The directory for saving the tags
	 */
	public static final String storeDir = "data";
	
	/**
	 * The file the tags are saved in
	 */
	public static final String storeFile = "tags.ser";	
	
	/**
	 * The list of all keys
	 */
	public static ArrayList<String> keys = new ArrayList<String>();
	
	/**
	 * The key of the current tag
	 */
	private String key;
	
	/**
	 * The value of the current tag
	 */
	private String value;
	
	/**
	 * Constructor
	 * @param key	tag key
	 * @param value	tag value
	 */
	public Tag(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	/**
	 * Copies keys to copyList
	 * @param copyList the list we want to copy all the current keys to
	 */
	public static void copyKeys(List<String> copyList) {
		for(String key : keys) {
			copyList.add(key);
		}
	}
	
	/**
	 * Adds a key to the list of key
	 * @param key the new key
	 */
	public static void addHandle(String key) {
		keys.add(key);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null)
			return false;
		Tag t = (Tag)o;
		return key.equals(t.getKey()) && value.equals(t.getValue());
	}
	
	/**
	 * Retuns the handle
	 * @return the handle
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * Returns the value
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	public String toString() {
		return key + ":" + value;
	}
	
	/**
	 * Writes the handles to a file
	 */
	public static void writeSerial() {
		keys = new ArrayList<String>();
		for(User u : DataManager.users)
			for(Photo p : u.photos)
				for(Tag t : p.tags)
					if(!keys.contains(t.key))
						keys.add(t.key);
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(
					new FileOutputStream (storeDir + File.separator + storeFile));
			oos.writeObject(keys);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads the handles from a file
	 */
	@SuppressWarnings("unchecked")
	public static void readSerial() {
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(
					new FileInputStream(storeDir + File.separator + storeFile));
			keys = (ArrayList<String>)ois.readObject();
			ois.close();
		} catch (Exception e) {
			DataManager.error=1;
		}
		
	}
}

