package info;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class is used to store an album of photos
 * @author Saikarthik Tirumalareddy
 * @author Tianqi Li
 */
public class Album implements Serializable{
	
	/**
	 * ID for Serialization
	 */
	private static final long serialVersionUID = 61485402;
	
	/**
	 * Name of the Album
	 */
	public String name;
	
	/**
	 * Date of the photo that was made the earliest
	 */
	private LocalDate earliestDate;
	
	/**
	 * Date of the photo that was made the latest
	 */
	private LocalDate latestDate;
	
	/**
	 * List of Photos in the Album
	 */
	public transient ObservableList<Photo> photos;
	
	/**
	 * List of Photos in the album used to save when Serializing
	 */
	public ArrayList<Photo> savePhotos=new ArrayList<Photo>();
	
	/**
	 * Constructor
	 * @param name Name of the album
	 */
	public Album(String name) {
		this.name=name;
		photos=FXCollections.observableArrayList();
	}
	
	/**
	 * Constructor
	 * @param name Name of the album
	 * @param photoList ArrayList of photos we want the user to have
	 */
	public Album(String name, ObservableList<Photo> photoList) {
		this.name=name;
		photos=FXCollections.observableArrayList();
		photos.addAll(photoList);
		savePhotos.addAll(photoList);
	}
	
	/**
	 * Returns the name of the album
	 * @return the name of the album
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Renames the album
	 * @param name Desired new name
	 */
	public void updateName(String name) {
		this.name=name;
	}
	
	/**
	 * Returns the number of photos
	 * @return the number of photos
	 */
	public int getCount() {
		return photos.size();
	}

	/**
	 * Returns the the date of the earliest photo
	 * @return Returns the the date of the earliest photo
	 */
	public String getEarliest() {
		return (earliestDate == null)? "" : earliestDate.toString();
	}
	
	/**
	 * Returns the the date of the latest photo
	 * @return Returns the the date of the latest photo
	 */
	public String getLatest() {
		return (latestDate == null)? "" : latestDate.toString();
	}
	
	/**
	 * Adds a photo to the album
	 * @param merge photo to merge into the album
	 */
	public void addPhoto(Photo merge) {
		photos.add(merge);
		savePhotos.add(merge);
		updateRange();
	}
	
	/**
	 * Adds a list of photos to the album
	 * @param photoList list of photos to be merged into the album
	 */
	public void addPhotos(ObservableList<Photo> photoList) {
		photos.addAll(photoList);
		savePhotos.addAll(photoList);
		updateRange();
	}
	
	/**
	 * Removes a photos to the album
	 * @param remove Photo to be removed from the album
	 */
	public void removePhoto(Photo remove) {
		photos.remove(remove);
		savePhotos.remove(remove);
	}
	
	/**
	 * Recalculates the date range of the album
	 */
	public void updateRange() {
		if (photos.size() == 0) {
			earliestDate = null;
			latestDate = null;
			return;
		}
		earliestDate = photos.get(0).getDate();
		latestDate = photos.get(0).getDate();
		for (Photo i : photos) {
			if (i.getDate().compareTo(earliestDate) < 0)
				earliestDate = i.getDate();
			else if (i.getDate().compareTo(latestDate) > 0)
				latestDate = i.getDate();
		}
	}
	
	/**
	 * Returns desplay version of album
	 * @return toString of album
	 */
	public String toString() {
		return name;
	}
	
}
