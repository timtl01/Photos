package info;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

/**
 * This class is used to store an photo with its tags and its file location
 * @author Saikarthik Tirumalareddy
 * @author Tianqi Li
 */
public class Photo implements Serializable{
	
	/**
	 * ID for Serialization
	 */
	private static final long serialVersionUID = 746815311;
	
	/**
	 * The date the photo was made
	 */
	private LocalDate date;
	
	/**
	 * The caption of the photo
	 */
	private String caption;
	
	/**
	 * the link to the photo
	 */
	private String link;
	
	/**
	 * The id of the photo
	 */
	public String id;
	
	/**
	 * The list of tags on a photo used to save the tags
	 */
	public ArrayList<Tag> saveTags = new ArrayList<Tag>();
	
	/**
	 * The list of tags on a photo used to display the tags
	 */
	public transient ObservableList<Tag> tags;
	
	/**
	 * The photo image
	 */
	private transient Image image;
	
	/**
	 * Constructor
	 * @param id ID of photo
	 * @param file	Photo File
	 * @throws IOException Error finding file
	 * @throws URISyntaxException Error finding file
	 */
	public Photo(String id, File file) throws IOException, URISyntaxException {
		tags=FXCollections.observableArrayList();
		link = file.toURI().toURL().toExternalForm();
		this.id=id;
		date=LocalDate.ofEpochDay(file.lastModified()/86400000);
		loadImage();
	} 
	
	/**
	 * Retrives the image from the link specified
	 */
	public void loadImage() {
		image = new Image(link);
	}
	
	/**
	 * Changes Caption
	 * @param caption desired new caption
	 */
	public void editCaption(String caption) {
		this.caption = caption;
	}
	
	/**
	 * Changes link
	 * @param file desired new file
	 * @throws MalformedURLException Error reading file
	 */
	public void editLink(File file) throws MalformedURLException {
		link = file.toURI().toURL().toExternalForm();
	}
	
	/**
	 * gets the id
	 * @return id of photo
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * gets the link
	 * @return link of photo
	 */
	public String getLink() {
		return link;
	}
	
	/**
	 * gets caption
	 * @return	caption
	 */
	public String getCaption() {
		return caption;
	}
	
	/**
	 * gets late modified date
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}
    
    /**
     * gets the image
     * @return the image file
     */
    public Image getImage() {
    	return image;
    }
    
    /**
     * Adds a tag to this photo's list of tags
     * @param t tag to add
     */
    public void addTag(Tag t) {
    	tags.add(t);
    	saveTags.add(t);
    }
    
    /**
     * Removes a tag from this photo's list of tags
     * @param t the tag to remove
     */
    public void deleteTag(Tag t) {
    	tags.remove(t);
    	saveTags.remove(t);
    }
    
    /**
     * copies tags to tagList
     * @param tagList list of tags we want to copy to
     */
	public void loadTags(List<Tag> tagList) {
		for(Tag t : tags) {
			tagList.add(t);
		}
	}
}
