package view;

import info.DataManager;
import info.User;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * @author Saikarthik Tirumalareddy
 * @author Tianqi Li
 * <p>This class is the controller for the admin scene
 * **/
public class AdminController {
	
	@FXML ListView<User> users;
	@FXML TextField userName;
	@FXML TextField NewName;
	
	/**
	 * Starts the scene
	 * @param primaryStage primary stage
	 */
	public void start(Stage primaryStage) {
		users.setItems(DataManager.users);
		if(!DataManager.users.isEmpty()) {
			users.getSelectionModel().select(0);
			setUser(DataManager.users.get(0));
		}
	}
	
	/**
	 * Sets the displayed user to the selected one
	 * @param e Event click on a user
	 */
	public void showName(Event e) {
		if(!DataManager.users.isEmpty()) {
			User selected = users.getSelectionModel().getSelectedItem();
			if(selected!=null)
				setUser(selected);
		}
	}
	
	/**
	 * Helper to show name, displays the details of a user
	 * @param selected desired user to display details
	 */
	public void setUser(User selected) {
		userName.setText(selected.getUsername());
	}
	
	/**
	 * Deletes selected user
	 * @param e Event clicked on delete user
	 */
	public void deleteUser(Event e){
		if(!users.getSelectionModel().isEmpty()) {
			DataManager.users.remove(users.getSelectionModel().getSelectedIndex());
			if(!DataManager.users.isEmpty()) {
				users.getSelectionModel().select(0);
				setUser(DataManager.users.get(0));
			}else {
				userName.clear();
			}
			DataManager.writeSerial();
		}		
	}
	
	/**
	 * Adds a new User
	 * @param e Event clicked on add user
	 */
	public void onAddUser(Event e) {
		if(!NewName.getText().isBlank()) {
			User newUser = new User(NewName.getText().trim());
			boolean dupe=false;
			for(User u : DataManager.users) {
				if(u.getUsername().equals(newUser.getUsername())) {
					dupe=true;
					Alert error = new Alert(AlertType.ERROR);
					error.setTitle("Error");
					error.setHeaderText(null);
					error.setContentText("User already exists");
					error.showAndWait();
				}
			}
			if(!dupe) {
				DataManager.users.add(newUser);
				newUser.checkIfStock();
				NewName.clear();
				users.getSelectionModel().select(newUser);
				setUser(newUser);
				users.refresh();
				DataManager.writeSerial();
			}
		}
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
	
}
