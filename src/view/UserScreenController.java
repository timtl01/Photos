package view;

import java.time.LocalDate;
import info.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

/**
 * @author Saikarthik Tirumalareddy
 * @author Tianqi Li
 * <p>This class is the controller for the opened user screen scene
 * **/
public class UserScreenController {
	
	@FXML ListView<Album> albums;
	@FXML TextField showName;
	@FXML TextField showNoOfPhotos;
	@FXML TextField showEarliestDate;
	@FXML TextField showLatestDate;
	@FXML TextField newName;
	@FXML TextField key1;
	@FXML TextField key2;
	@FXML TextField value1;
	@FXML TextField value2;
	@FXML DatePicker earlyDate;
	@FXML DatePicker lateDate;
	@FXML ChoiceBox<Option> andOr;
	
	enum Option {
		AND, OR
	}
	
	/**
	 * Starts the scene
	 * @param primaryStage primary stage
	 */
	public void start(Stage primaryStage){
		
	}
	
	/**
	 * Displays the albums the current user has
	 * @param albumList The current user's albums
	 */
	public void setAlbums(ObservableList<Album> albumList) {
		albums.setItems(albumList);
		andOr.getItems().add(Option.AND);
		andOr.getItems().add(Option.OR);
		andOr.setValue(Option.AND);
		if(!DataManager.activeAccount.getAlbums().isEmpty()) {
			albums.getSelectionModel().select(0);
			setAlbum(albums.getSelectionModel().getSelectedItem());
		}
	}
	
	/**
	 * Displays the info of the selected album
	 * @param e Click on an album on the list
	 */
	public void showAlbum(Event e) {
		if(!DataManager.activeAccount.albums.isEmpty()) {
			Album selected = albums.getSelectionModel().getSelectedItem();
			if(selected!=null)
				setAlbum(selected);
		}
	}
	
	/**
	 * Displays the info of the selected album
	 * @param selected The selected album
	 */
	public void setAlbum(Album selected) {
		showName.setText(selected.getName());
		showNoOfPhotos.setText(selected.getCount()+"");
		showEarliestDate.setText(selected.getEarliest());
		showLatestDate.setText(selected.getLatest());
	}
	
	/**
	 * Adds a new album
	 * @param e Event clicked on add album
	 */
	public void onAddAlbum(Event e) {
		if(!newName.getText().isBlank()) {
			Album newAlbum = new Album(newName.getText().trim());
			boolean dupe=false;
			for(Album a : DataManager.activeAccount.albums) {
				if(a.getName().equals(newAlbum.getName())) {
					dupe=true;
					Alert error = new Alert(AlertType.ERROR);
					error.setTitle("Error");
					error.setHeaderText(null);
					error.setContentText("Album already exists");
					error.showAndWait();
				}
			}
			if(!dupe) {
				DataManager.activeAccount.addAlbum(newAlbum);
				newName.clear();
				albums.getSelectionModel().select(newAlbum);
				setAlbum(newAlbum);
				albums.refresh();
				DataManager.writeSerial();
			}
		}
	}
	
	/**
	 * Renames selected album
	 * @param e Event clicked on rename album
	 */
	public void onRename(Event e) {
		if(!newName.getText().isBlank()) {
			String newAlbum = newName.getText().trim();
			boolean dupe=false;
			for(Album a : DataManager.activeAccount.albums) {
				if(a.getName().equals(newAlbum)) {
					dupe=true;
					Alert error = new Alert(AlertType.ERROR);
					error.setTitle("Error");
					error.setHeaderText(null);
					error.setContentText("Album already exists");
					error.showAndWait();
				}
			}
			if(!dupe) {
				int index = albums.getSelectionModel().getSelectedIndex();
				Album old = DataManager.activeAccount.albums.remove(index);
				DataManager.activeAccount.saveAlbums.remove(index);
				old.updateName(newAlbum);
				DataManager.activeAccount.albums.add(index, old);
				DataManager.activeAccount.saveAlbums.add(index, old);
				setAlbum(old);
				newName.clear();
				albums.refresh();
				DataManager.writeSerial();
			}
		}
	}
	
	/**
	 * Deletes selected album
	 * @param e Event clicked on delete album
	 */
	public void deleteAlbum(Event e){
		if(!albums.getSelectionModel().isEmpty()) {
			DataManager.activeAccount.albums.remove(albums.getSelectionModel().getSelectedIndex());
			albums.refresh();
			if(!DataManager.activeAccount.albums.isEmpty()) {
				albums.getSelectionModel().select(0);
				setAlbum(DataManager.activeAccount.albums.get(0));
			}else {
				showName.clear();
				showNoOfPhotos.clear();
				showEarliestDate.clear();
				showLatestDate.clear();
			}
			DataManager.writeSerial();
		}		
	}
	
	/**
	 * Opens the selected album
	 * @param e Event clicked on open album
	 */
	public void onOpenAlbum(Event e) {
		if(!albums.getSelectionModel().isEmpty()) {
			SceneManager.setScene(SceneManager.openAlbum);
			DataManager.currentAlbum=albums.getSelectionModel().getSelectedItem();
			SceneManager.openAlbumController.setPhotos();
		}
	}
	
	/**
	 * Searches by Date
	 * @param e Event clicked on search by date
	 */
	public void onSearch(Event e) {
		if(earlyDate.getValue()==null || lateDate.getValue()==null) {
			searchAlert();
			return;
		}
		ObservableList<Photo> photoList = FXCollections.observableArrayList();
		LocalDate early = earlyDate.getValue();
		LocalDate late = lateDate.getValue();
		for(Photo p: DataManager.activeAccount.photos) {
			if((p.getDate().compareTo(early)>0) && (p.getDate().compareTo(late)<0)) {
				photoList.add(p);
			}
		}
		if(photoList.isEmpty()) {
			searchAlert();
			return;
		}
		key1.clear();
		value1.clear();
		key2.clear();
		value2.clear();
		SceneManager.searchController.setSearchPhotos(photoList);
		SceneManager.setScene(SceneManager.search);
	}
	
	/**
	 * Search by A Tag Combination or a Single Tag
	 * @param e Event click on search by tag
	 */
	public void onTagSearch(Event e) {
		if(key1.getText().isBlank() || value1.getText().isBlank()) {
			searchAlert();
			return;
		}
		if(key2.getText().isBlank() ^ value2.getText().isBlank()) {
			searchAlert();
			return;
		}
		ObservableList<Photo> photoList = FXCollections.observableArrayList();
		if(key2.getText().isBlank() && value2.getText().isBlank()) {
			if(!Tag.keys.contains(key1.getText())) {
				searchAlert();
				return;
			}
			Tag searchTag = new Tag(key1.getText().trim(),value1.getText().trim());
			for(Photo p: DataManager.activeAccount.photos) {
				if(p.tags.contains(searchTag)) {
					photoList.add(p);
				}
			}
		}else if(andOr.getValue()==Option.AND) {
			if(!Tag.keys.contains(key1.getText()) || !Tag.keys.contains(key2.getText())) {
				searchAlert();
				return;
			}
			Tag searchTag1 = new Tag(key1.getText().trim(),value1.getText().trim());
			Tag searchTag2 = new Tag(key2.getText().trim(),value2.getText().trim());
			for(Photo p: DataManager.activeAccount.photos) {
				if(p.tags.contains(searchTag1) && p.tags.contains(searchTag2)) {
					photoList.add(p);
				}
			}
		}else {
			if(!Tag.keys.contains(key1.getText()) && !Tag.keys.contains(key2.getText())) {
				searchAlert();
				return;
			}
			Tag searchTag1 = new Tag(key1.getText().trim(),value1.getText().trim());
			Tag searchTag2 = new Tag(key2.getText().trim(),value2.getText().trim());
			for(Photo p: DataManager.activeAccount.photos) {
				if(p.tags.contains(searchTag1) || p.tags.contains(searchTag2)) {
					photoList.add(p);
				}
			}
		}
		if(photoList.isEmpty()) {
			searchAlert();
			return;
		}
		key1.clear();
		value1.clear();
		key2.clear();
		value2.clear();
		SceneManager.searchController.setSearchPhotos(photoList);
		SceneManager.setScene(SceneManager.search);
	}
	
	/**
	 * Throws and alert notifying the user their search had no results
	 */
	public void searchAlert() {
		Alert error = new Alert(AlertType.ERROR);
		error.setTitle("Search Results");
		error.setHeaderText(null);
		error.setContentText("No Search Results With The Given Parameters");
		error.showAndWait();
	}
	/**
	 * Goes back to the starting screen
	 * @param e Event click on logout
	 */
	public void onLogout(Event e){
		key1.clear();
		value1.clear();
		key2.clear();
		value2.clear();
		showName.clear();
		showNoOfPhotos.clear();
		showEarliestDate.clear();
		showLatestDate.clear();
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
