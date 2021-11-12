package view;

import info.DataManager;
import info.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * @author Saikarthik Tirumalareddy
 * @author Tianqi Li
 * <p>This class is the controller for the display photo scene
 * **/
public class DisplayPhotoController {
	
	@FXML TextField caption;
	@FXML ListView<Tag> tags;
	@FXML ImageView imageView;	
	
	/**
	 * Starts the scene
	 * @param primaryStage primary stage
	 */
	public void start(Stage primaryStage) {
		
	}
	
	/**
	 * Sets the image view, caption, and tags to the photo selected
	 */
	public void setPhoto() {
		imageView.setImage(DataManager.currentPhoto.getImage());
		caption.setText(DataManager.currentPhoto.getCaption());
		tags.setItems(DataManager.currentPhoto.tags);
	}
	
	/**
	 * Goes back to the starting screen
	 * @param e Event click on logout
	 */
	public void onLogout(Event e){
		DataManager.writeSerial();
		SceneManager.setScene(SceneManager.logIn);
	}
	
	/**
	 * Saves changes and closes application
	 * @param e Event click on quit
	 */
	public void onQuit(Event e){
		DataManager.writeSerial();
		System.exit(1);
	}
	
	/**
	 * Goes back to the user screen to select another album to view or search
	 * @param e Event click on back
	 */
	public void onBack(Event e) {
		SceneManager.setScene(SceneManager.openAlbum);
		caption.clear();
		tags.setItems(null);
		DataManager.currentPhoto=null;
	}
}
