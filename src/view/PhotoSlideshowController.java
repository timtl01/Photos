package view;

import info.DataManager;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * @author Saikarthik Tirumalareddy
 * @author Tianqi Li
 * <p>This class is the controller for the slideshow scene
 * **/
public class PhotoSlideshowController {
	
	@FXML ImageView imageView;
	@FXML Button prev;
	@FXML Button next;
	@FXML Button goBack;
	
	/**
	 * Index of the current photo in the current album
	 */
	private int photoIndex = 0;
	
	/**
	 * Starts the scene
	 * @param primaryStage primary stage
	 */
	public void start(Stage primaryStage) {
		
	}
	
	/**
	 * Sets the ImageView with the first photo in the album (index of the photo is 0)
	 */
	public void setInitialPhoto() {
		photoIndex = 0;
		imageView.setImage(DataManager.currentPhoto.getImage());
	}
	
	/**
	 * Changes image to the previous photo in the album
	 * @param e Event click on &lt;&lt;&lt;
	 */
	public void onPrev(Event e) {
		if (photoIndex > 0) {
			photoIndex -= 1;
			DataManager.currentPhoto = DataManager.currentAlbum.photos.get(photoIndex);
			imageView.setImage(DataManager.currentPhoto.getImage());
		}
	}
	
	/**
	 * Changes image to the next photo in the album
	 * @param e Event click on &gt;&gt;&gt;
	 */
	public void onNext(Event e) {
		if (photoIndex < DataManager.currentAlbum.photos.size()-1) {
			photoIndex += 1;
			DataManager.currentPhoto = DataManager.currentAlbum.photos.get(photoIndex);
			imageView.setImage(DataManager.currentPhoto.getImage());
		}
	}
	
	/**
	 * Go back to the open album scene
	 * @param e Event click on go back
	 */
	public void onGoBack(Event e) {
		SceneManager.setScene(SceneManager.openAlbum);
		DataManager.currentPhoto = null;
	}
	
}
