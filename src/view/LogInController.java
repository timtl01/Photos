package view;

import info.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * @author Saikarthik Tirumalareddy
 * @author Tianqi Li
 * <p>This class is the controller for the log in scene
 * **/
public class LogInController {
	
	@FXML TextField username;
	
	/**
	 * Starts the scene
	 * @param primaryStage primary stage
	 */
	public void start(Stage primaryStage) {
		
	}
	
	/**
	 * Checks for any file errors before proceeding with the application
	 */
	public static void checkError(){
		if(DataManager.error==1) {
			Alert err = new Alert(AlertType.ERROR);
			err.setTitle("Error Dialog");
			err.setHeaderText(null);
			err.setContentText("Error Reading File");
			err.showAndWait();
		}
	}
	
	/**
	 * Opens the profile of the user entered in text.
	 * If the user does not exist, display an error.
	 * @param e Event click on log in
	 */
	public void onLogIn(Event e){
		if(username.getText().equals("admin")) {
			SceneManager.setScene(SceneManager.admin);
			username.clear();
		} else {
			if(!DataManager.makeActive(username.getText())) {
				Alert err = new Alert(AlertType.ERROR);
				err.setTitle("Error Dialog");
				err.setHeaderText(null);
				err.setContentText("No Such User");
				err.showAndWait();
			}else {
				SceneManager.setScene(SceneManager.userScreen);	
				username.clear();
			}
		}
	}
	
	/**
	 * Saves changes and closes application
	 * @param e Event click on quit
	 */
	public void onQuit(Event e){
		System.exit(1);
	}
}
