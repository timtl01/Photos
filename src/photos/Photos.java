package photos;

import java.io.IOException;

import info.DataManager;
import info.Tag;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import view.LogInController;
import view.SceneManager;

/**
 * This class is the main class used to start application and initialize all the variables
 * @author Saikarthik Tirumalareddy
 * @author Tianqi Li
 */
public class Photos extends Application{
	
	/**
	 * Launches application
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Starts Application
	 * @param primaryStage application is on
	 */
	@Override
	public void start(Stage primaryStage){		
		try {
			initialize(primaryStage);
		} catch (IOException e) {
			Alert err = new Alert(AlertType.ERROR);
			err.setTitle("Error Dialog");
			err.setHeaderText(null);
			err.setContentText("Error Reading Fxml files");
			err.showAndWait();
		}
		DataManager.readSerial();	
		Tag.readSerial();
		primaryStage.setScene(SceneManager.logIn);
		primaryStage.setTitle("Photo Viewing Application");
		primaryStage.setResizable(false);
		primaryStage.show(); 
		LogInController.checkError();
	}
	
	/**
	 * Initializes all the Scenes
	 * @param primaryStage the primary stage the application is run on
	 * @throws IOException Error retrieving fxml files
	 */
	public void initialize(Stage primaryStage) throws IOException {
		SceneManager.stage=primaryStage;
		FXMLLoader loader=new FXMLLoader(getClass().getResource("/view/User Log in.fxml"));
		Parent root = loader.load();
		SceneManager.logInController = loader.getController();
		SceneManager.logIn = new Scene(root,250,169);
		SceneManager.logInController.start(primaryStage);
		
		loader = new FXMLLoader(getClass().getResource("/view/Admin Screen.fxml"));
		root = loader.load();
		SceneManager.adminController = loader.getController();
		SceneManager.admin = new Scene(root,370,582);
		SceneManager.adminController.start(primaryStage);
		
		loader = new FXMLLoader(getClass().getResource("/view/User Initial Screen.fxml"));
		root = loader.load();
		SceneManager.userScreenController = loader.getController();
		SceneManager.userScreen = new Scene(root,550,582);
		SceneManager.userScreenController.start(primaryStage);
		
		loader = new FXMLLoader(getClass().getResource("/view/OpenAlbum.fxml"));
		root = loader.load();
		SceneManager.openAlbumController = loader.getController();
		SceneManager.openAlbum = new Scene(root,600,582);
		SceneManager.openAlbumController.start(primaryStage);
		
		loader = new FXMLLoader(getClass().getResource("/view/DisplayPhoto.fxml"));
		root = loader.load();
		SceneManager.displayController = loader.getController();
		SceneManager.displayPhoto = new Scene(root,640,360);
		SceneManager.displayController.start(primaryStage);
		
		loader = new FXMLLoader(getClass().getResource("/view/PhotoSlideshow.fxml"));
		root = loader.load();
		SceneManager.slideshowController = loader.getController();
		SceneManager.photoSlideshow = new Scene(root,640,360);
		SceneManager.slideshowController.start(primaryStage);
		
		loader = new FXMLLoader(getClass().getResource("/view/Search For Photos.fxml"));
		root = loader.load();
		SceneManager.searchController = loader.getController();
		SceneManager.search = new Scene(root,600,750);
		SceneManager.searchController.start(primaryStage);
	}

}
