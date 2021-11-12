package info;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import view.SceneManager;
import java.io.*;

/**
 * This class is used to manage the data in the project mainly the list of users
 * @author Saikarthik Tirumalareddy
 * @author Tianqi Li
 */
public class DataManager {
	
	/**
	 * List of users in the system
	 */
	public static ObservableList<User> users = FXCollections.observableArrayList();
	
	/**
	 * Directory of the data
	 */
	public static final String storeDir = "data";
	
	/**
	 * File where users are stored
	 */
	public static final String storeFile = "users.ser";
	
	/**
	 * The account that is currently logged in
	 */
	public static User activeAccount;
	
	/**
	 * The album that is currently open
	 */
	public static Album currentAlbum;
	
	/**
	 * The photo that is currently open
	 */
	public static Photo currentPhoto;
	
	/**
	 * Error code to check if there was an error reading file
	 */
	public static int error=0;
	
	/**
	 * Validates the passed username
	 * Sets that account active if it exists
	 * @param username	The username
	 * @return False if the account doesn't exist. True otherwise
	 */
	public static boolean makeActive(String username) {
		User findAcc = new User(username);
		int result = users.indexOf(findAcc);
		if(result != -1) {
			activeAccount = users.get(result);
			SceneManager.userScreenController.setAlbums(activeAccount.albums);
		}
		return !(result==-1);
	}
	
	/**
	 * Makes the album selected album active
	 * @param newAlbum	The album that was opened by the user
	 */
	public static void makeCurrent(Album newAlbum) {
		currentAlbum = newAlbum;
	}
	
	/**
	 * Gets a requested account 
	 * @param username	The username of the requested account
	 * @return The account requested
	 */
	public static User fetchAccount(String username) {
		User fetch = new User(username);
		boolean fetchSuccess = false;
		for (User i : users) {
			if (i.getUsername() == fetch.getUsername()) {
				fetch = i; fetchSuccess = true;
				break;
			}
		}
		if (!fetchSuccess)
			return null;
		else
			return fetch;
	}
	
	/**
	 * Writes the accounts object to a file
	 */
	public static void writeSerial() {
		Tag.writeSerial();
		ArrayList<User> temp = new ArrayList<User>();
		for(User u: users) {
			u.saveAlbums.clear();
			u.saveAlbums.addAll(u.albums);
			u.savePhotos.clear();
			u.savePhotos.addAll(u.photos);
		}
		temp.addAll(users);
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream (storeDir + File.separator + storeFile));
			oos.writeObject(temp);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Reads in the accounts object from the file
	 */
	@SuppressWarnings("unchecked")
	public static void readSerial(){
		try {
		ObjectInputStream ois;
		ois = new ObjectInputStream(
				new FileInputStream(storeDir + File.separator + storeFile));
		users.addAll((ArrayList<User>)ois.readObject());
		for(User u: users) {
			u.albums = FXCollections.observableArrayList();
			u.photos = FXCollections.observableArrayList();
			u.albums.addAll(u.saveAlbums);
			u.photos.addAll(u.savePhotos);
			for(Photo p : u.photos) {
				p.tags=FXCollections.observableArrayList();
				p.tags.addAll(p.saveTags);
				if(p.id.equals(User.stockPhoto1)) 
					p.editLink(new File(System.getProperty("user.dir")+"/data/StockPhoto1.jpg"));
				else if(p.id.equals(User.stockPhoto2)) 
					p.editLink(new File(System.getProperty("user.dir")+"/data/StockPhoto2.jpg"));
				else if(p.id.equals(User.stockPhoto3)) 
					p.editLink(new File(System.getProperty("user.dir")+"/data/StockPhoto3.png"));
				else if(p.id.equals(User.stockPhoto4)) 
					p.editLink(new File(System.getProperty("user.dir")+"/data/StockPhoto4.PNG"));
				else if(p.id.equals(User.stockPhoto5)) 
					p.editLink(new File(System.getProperty("user.dir")+"/data/StockPhoto5.PNG"));
				p.loadImage();
			}
			for(Album a : u.albums) {
				a.photos=FXCollections.observableArrayList();
				a.photos.addAll(a.savePhotos);
				a.updateRange();
			}
		}
		ois.close();
		}catch(Exception e){
			error=1;
		}
	}

}

