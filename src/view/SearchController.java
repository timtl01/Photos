package view;

import info.*;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * @author Saikarthik Tirumalareddy
 * @author Tianqi Li
 * <p>This class is the controller for the search scene
 * **/
public class SearchController {
	
	@FXML ListView<Photo> photos;
	
	/**
	 * Starts the scene
	 * @param primaryStage primary stage
	 */
	public void start(Stage primaryStage) {
		photos.setCellFactory(listView -> new ListCell<Photo>() {
		    private ImageView imageView = new ImageView();
		    @Override
		    public void updateItem(Photo item, boolean empty) {
		        super.updateItem(item, empty);
		        if (empty) {
		            setText(null);
		            setGraphic(null);
		        } else {
		            Image image = item.getImage();
		            imageView.setImage(image);
		            imageView.setFitHeight(200);
		            imageView.setFitWidth(400);
		            setText(null);
		            setGraphic(imageView);
		        }
		    }
		});
	}
	
	/**
	 * Displays the list of photos that match the search criteria
	 * @param photoList The photos that match the search criteria
	 */
	public void setSearchPhotos(ObservableList<Photo> photoList){
		photos.setItems(photoList);
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
		SceneManager.setScene(SceneManager.userScreen);
		DataManager.currentPhoto=null;
	}
}
