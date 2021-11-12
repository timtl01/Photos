package info;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class is used to store User with their photos and their albums
 * @author Saikarthik Tirumalareddy
 * @author Tianqi Li
 */
public class User implements Serializable{
	
	/**
	 * ID for Serialization
	 */
	private static final long serialVersionUID = 1536413731;
	
	/**
	 * The name of the user
	 */
	private String username;
	
	/**
	 * The List of photos used to save
	 */
	public ArrayList<Photo> savePhotos = new ArrayList<Photo>();
	
	/**
	 * The list of albums used to display
	 */
	public transient ObservableList<Album> albums;
	
	/**
	 * The list of photos used to display
	 */
	public transient ObservableList<Photo> photos; 
	
	/**
	 * The list of albums used to save
	 */
	public ArrayList<Album> saveAlbums = new ArrayList<Album>();
	
	/**
	 * The id of the first stock photo
	 */
	public static String stockPhoto1 = "stockPhoto1";
	
	/**
	 * The id of the second stock photo
	 */
	public static String stockPhoto2 = "stockPhoto2";
	
	/**
	 * The id of the third stock photo
	 */
	public static String stockPhoto3 = "stockPhoto3";
	
	/**
	 * The id of the fourth stock photo
	 */
	public static String stockPhoto4 = "stockPhoto4";
	
	/**
	 * The id of the fifth stock photo
	 */
	public static String stockPhoto5 = "stockPhoto5";
	
	
	/**
	 * Constructor
	 * @param name Name of the user
	 */
	public User(String name) {
		albums = FXCollections.observableArrayList();
		photos = FXCollections.observableArrayList();
		this.username=name;
	}
	
	/**
	 * Constructor
	 * @param name name of the user
	 * @param photos List of photos the user has
	 */
	public User(String name, ObservableList<Photo> photos) {
		albums = FXCollections.observableArrayList();
		photos = FXCollections.observableArrayList();
		this.username=name;
		this.photos=photos;
	}
	
	/**
	 * Gets the name of the user
	 * @return username of the current user
	 */
	public String getUsername(){
		return username;
	}
	
	/**
	 * Gets the list of photos to display
	 * @return list of photos to display
	 */
	public ObservableList<Photo> getPhotos(){
		return photos;
	}
	
	/**
	 * Checks if the current user is the "stock" user
	 * If they are then give them the stock photos
	 */
	public void checkIfStock() {
		if(this.username.equals("stock")){
			ObservableList<Photo> stockPhotos = FXCollections.observableArrayList();
			try {
				stockPhotos.add(new Photo(stockPhoto1,new File(System.getProperty("user.dir")+"/data/StockPhoto1.jpg")));
				stockPhotos.add(new Photo(stockPhoto2,new File(System.getProperty("user.dir")+"/data/StockPhoto2.jpg")));
				stockPhotos.add(new Photo(stockPhoto3,new File(System.getProperty("user.dir")+"/data/StockPhoto3.png")));
				stockPhotos.add(new Photo(stockPhoto4,new File(System.getProperty("user.dir")+"/data/StockPhoto4.PNG")));
				stockPhotos.add(new Photo(stockPhoto5,new File(System.getProperty("user.dir")+"/data/StockPhoto5.PNG")));
			} catch (IOException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Album stockAlbum= new Album("Stock Photos");
			stockAlbum.addPhotos(stockPhotos);
			this.addPhotos(stockPhotos);
			this.addAlbum(stockAlbum);
		}
	}
	
	/**
	 * Adds a photo to the album
	 * @param merge photo to merge into the album
	 */
	public void addPhoto(Photo merge) {
		photos.add(merge);
		savePhotos.add(merge);
	}
	
	/**
	 * Adds a photo to the album
	 * @param merge photo to merge into the album
	 */
	public void addPhotos(ObservableList<Photo> merge) {
		photos.addAll(merge);
		savePhotos.addAll(merge);
	}
	
	/**
	 * Adds a Album to the album list
	 * @param merge album to merge into the album list
	 */
	public void addAlbum(Album merge) {
		albums.add(merge);
		saveAlbums.add(merge);
	}
	
	
	/**
	 * Gets the list of albums to display
	 * @return list of albums to display
	 */
	public ObservableList<Album> getAlbums(){
		return albums;
	}
	
	@Override
	public String toString() {
		return username;
	}
	
    @Override
    public boolean equals(Object o) {
    	if(!(o instanceof User)) return false;
    	User other = (User) o;
    	return this.username.equals(other.getUsername());
    }
}
