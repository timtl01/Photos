package view;

import java.io.File;
import java.util.List;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @author Saikarthik Tirumalareddy
 * @author Tianqi Li
 * <p>This class stores and manages all scenes of the application
 * **/
public abstract class SceneManager {
	
	/**
	 * The log in scene
	 */
	public static Scene logIn;
	
	/**
	 * The scene to display a photo
	 */
	public static Scene displayPhoto;
	
	/**
	 * The scene to open an album
	 */
	public static Scene openAlbum;
	
	/**
	 * The scene to slideshow photos in an album
	 */
	public static Scene photoSlideshow;
	
	/**
	 * The scene for admins
	 */
	public static Scene admin;
	
	/**
	 * The scene to search for photos
	 */
	public static Scene search;
	
	/**
	 * The scene for users
	 */
	public static Scene userScreen;
	
	/**
	 * The stage the application is on
	 */
	public static Stage stage;
	
	/**
	 * The controller for the log in scene
	 */
	public static LogInController logInController;
	
	/**
	 * The controller for the admin scene
	 */
	public static AdminController adminController;
	
	/**
	 * The controller for the open album scene
	 */
	public static OpenAlbumController openAlbumController;
	
	/**
	 * The controller for the user scene
	 */
	public static UserScreenController userScreenController;
	
	/**
	 * The controller for the display photo scene
	 */
	public static DisplayPhotoController displayController;
	
	/**
	 * The controller for the slideshow scene
	 */
	public static PhotoSlideshowController slideshowController;
	
	/**
	 * The controller for the search scene
	 */
	public static SearchController searchController;
	
	/**
	 * The file chooser to get files
	 */
	private FileChooser chooser;
	
	/**
	 * Opens an explorer for the user to select files to upload
	 * @return	a list files selected by the user for adding to an album
	 */
	public List<File> getFile() {
		return chooser.showOpenMultipleDialog(stage);
	}
	
	/**
	 * Changes current scene
	 * @param scene scene to change to
	 */
	public static void setScene(Scene scene) {
		stage.setScene(scene);
		stage.centerOnScreen();
	}
	
	/**
	 * Sets the primary stage to the parameter
	 * @param primaryStage The stage the application is on
	 */
	public void stage(Stage primaryStage) {
		stage = primaryStage;
	}
}

