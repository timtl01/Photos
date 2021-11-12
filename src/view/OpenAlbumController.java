package view;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import info.*;

/**
 * @author Saikarthik Tirumalareddy
 * @author Tianqi Li
 * <p>This class is the controller for the opened album scene
 * **/
public class OpenAlbumController {
	
	@FXML ListView<Photo> photos;
	@FXML TextField caption;
	@FXML ListView<Tag> tags;
	
	/**
	 * Tooltip for setting up the photo list with thumbnails
	 */
	private final Tooltip tooltip = new Tooltip();
	
	/**
	 * Starts the scene
	 * @param primaryStage primary stage
	 */
	public void start(Stage primaryStage) {
		caption.setTooltip(tooltip);
		Tooltip.install(caption, tooltip);
		
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
		            imageView.setFitHeight(75);
		            imageView.setFitWidth(150);
		            setText(null);
		            setGraphic(imageView);
		        }
		    }
		});
	}
	/**
	 * Sets the ListView of photos to the current albums photo list
	 */
	public void setPhotos() {
		photos.setItems(DataManager.currentAlbum.photos);
		if(!DataManager.currentAlbum.photos.isEmpty()) {
			photos.getSelectionModel().select(0);
			setPhoto(photos.getSelectionModel().getSelectedItem());
		}
	}
	
	/**
	 * Shows details for the photo
	 * @param e Event selected a photo from the list
	 */
	public void showPhoto(Event e) {
		if(!DataManager.currentAlbum.photos.isEmpty()) {
			Photo selected = photos.getSelectionModel().getSelectedItem();
			if(selected!=null)
				setPhoto(selected);
		}
	}
	
	/**
	 * Sets tags view and caption the currently selected photo
	 * @param selected the currently selected photo
	 */
	public void setPhoto(Photo selected) {
		if(selected == null) {
			return;
		}
		caption.setPromptText(selected.getCaption());
		tooltip.setText(selected.getCaption());
		caption.setTooltip(tooltip);
		tags.setItems(selected.tags);
		tags.refresh();
	}
	
	/**
	 * Adds a photo
	 * @param e Event clicked on add
	 */
	public void onAddPhoto(Event e) {
		FileChooser explorer;
		explorer = new FileChooser();
		explorer.getExtensionFilters().addAll(new ExtensionFilter("Image files", "*.jpg;*.png;*.PNG"));
		List<File> files = explorer.showOpenMultipleDialog(SceneManager.stage);
		if(files == null)
			return;
		
		for(File f : files) {
			if(f == null) {
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("File Error");
				error.setHeaderText(null);
				error.setContentText("Something went wrong with a file");
				error.showAndWait();
				continue;
			}
			
			Photo p;
			try {
				p = new Photo(UUID.randomUUID().toString(), f);
			} catch (IOException | URISyntaxException e1) {
				Alert error = new Alert(AlertType.ERROR);
				error.setTitle("File Error");
				error.setHeaderText(null);
				error.setContentText("Error loading file.");
				error.showAndWait();
				return;
			}
			
			DataManager.currentAlbum.addPhoto(p);
			photos.getSelectionModel().select(p);
			setPhoto(p);
			DataManager.activeAccount.addPhoto(p);
			DataManager.writeSerial();
		}
	}
	
	/**
	 * Removes selected photo
	 * @param e Event clicked on remove
	 */
	public void onRemovePhoto(Event e) {
		Photo p = photos.getSelectionModel().getSelectedItem();
		if(p == null)
			return;
		DataManager.currentAlbum.removePhoto(p);
		DataManager.currentAlbum.updateRange();
		if(!DataManager.currentAlbum.photos.isEmpty()) {
			photos.getSelectionModel().select(0);
			setPhoto(photos.getSelectionModel().getSelectedItem());
		}
		else {
			caption.clear();
			caption.setPromptText("Caption");
			tags.setItems(null);
		}
		DataManager.writeSerial();
	}
	
	/**
	 * Copies selected photo to another album 
	 * @param e Event clicked on copy
	 */
	public void onCopyPhoto(Event e) {
		Photo p = photos.getSelectionModel().getSelectedItem();
		
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Copy Photo");
		
		ButtonType photoButtonType = new ButtonType("Copy Photo", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(photoButtonType, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		ListView<Album> otherAlbums = new ListView<Album>();
		grid.add(new Label("Other Albums:"), 0, 0);
		grid.add(otherAlbums, 1, 0);
		ObservableList<Album> albslist2 = FXCollections.observableArrayList();
		otherAlbums.setItems(albslist2);
		for (Album i : DataManager.activeAccount.albums) {
			if (i != DataManager.currentAlbum) {
				albslist2.add(i);
			}
		}
		otherAlbums.getSelectionModel().select(0);
		
		dialog.getDialogPane().setContent(grid);

		Optional<ButtonType> confirm = dialog.showAndWait();
		
		if(confirm.get() == ButtonType.CANCEL)
			return;
		
		otherAlbums.getSelectionModel().getSelectedItem().addPhoto(p);
		otherAlbums.getSelectionModel().getSelectedItem().updateRange();
		DataManager.writeSerial();
	}
	
	/**
	 * Moves the selected photo to another album
	 * @param e Event clicked on move 
	 */
	public void onMovePhoto(Event e) {
		Photo p = photos.getSelectionModel().getSelectedItem();
		
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Move Photo");
		
		ButtonType photoButtonType = new ButtonType("Move Photo", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(photoButtonType, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		ListView<Album> otherAlbums2 = new ListView<>();
		grid.add(new Label("Other Albums:"), 0, 0);
		grid.add(otherAlbums2, 1, 0);
		ObservableList<Album> albslist3 = FXCollections.observableArrayList();
		otherAlbums2.setItems(albslist3);
		for (Album i : DataManager.activeAccount.albums) {
			if (i != DataManager.currentAlbum) {
				albslist3.add(i);
			}
		}
		
		dialog.getDialogPane().setContent(grid);

		Optional<ButtonType> confirm = dialog.showAndWait();
		
		if(confirm.get() == ButtonType.CANCEL)
			return;
		
		otherAlbums2.getSelectionModel().getSelectedItem().addPhoto(p);
		otherAlbums2.getSelectionModel().getSelectedItem().updateRange();
		DataManager.currentAlbum.removePhoto(p);
		DataManager.currentAlbum.updateRange();
		if(!DataManager.currentAlbum.photos.isEmpty())
			photos.getSelectionModel().select(0);
		DataManager.writeSerial();
	}
	
	/**
	 * Adds a tag
	 * @param e Event clicked on add tag
	 */
	public void onAddTag(Event e) {
		Photo p = photos.getSelectionModel().getSelectedItem();
		if(p == null)
			return;
		
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Add Tag");
		
		ButtonType photoButtonType = new ButtonType("Add Tag", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(photoButtonType, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		ComboBox<String> tagKey = new ComboBox<String>();
		tagKey.setEditable(true);
		Tag.copyKeys(tagKey.getItems());
		TextField tagValue = new TextField();
		grid.add(new Label("Tag Handle:"), 0, 0);
		grid.add(tagKey, 1, 0);
		grid.add(new Label(":"), 2, 0);
		grid.add(tagValue, 3, 0);
		
		dialog.getDialogPane().setContent(grid);

		Optional<ButtonType> confirm = dialog.showAndWait();
		
		if(confirm.get() == ButtonType.CANCEL)
			return;
		
		if(!Tag.keys.contains(tagKey.getValue()))
			Tag.addHandle(tagKey.getValue());
		
		Tag newTag = new Tag(tagKey.getValue(), tagValue.getText());
		p.addTag(newTag);
		if(!photos.getSelectionModel().getSelectedItem().tags.isEmpty())
			tags.getSelectionModel().select(0);
		Tag.writeSerial();
	}
	
	/**
	 * Removes selected tag
	 * @param e Event clicked on delete tag
	 */
	public void onDeleteTag(Event e){
		Tag tag = tags.getSelectionModel().getSelectedItem();
		if (tag == null)
			return;
		photos.getSelectionModel().getSelectedItem().deleteTag(tag);
		if(!photos.getSelectionModel().getSelectedItem().tags.isEmpty())
			tags.getSelectionModel().select(0);
		Tag.writeSerial();
	}
	
	/**
	 * Called when the caption of the current photo needs to be changed
	 * @param e Event click on change caption
	 */
	public void onChangeCaption(Event e) {
		if(!caption.getText().isBlank()) {
			Photo p = photos.getSelectionModel().getSelectedItem();
			if(p == null)
				return;
			p.editCaption(caption.getText());
			caption.clear();
			setPhoto(photos.getSelectionModel().getSelectedItem());
			photos.refresh();
		}
	}
	
	/**
	 * Goes back to the user screen to select another album to view or search
	 * @param e Event click on back
	 */
	public void onBack(Event e) {
		SceneManager.setScene(SceneManager.userScreen);
		caption.clear();
		tags.setItems(null);
		photos.setItems(null);
		DataManager.currentAlbum=null;
	}
	
	/**
	 * Goes to the Photo View scene
	 * @param e Event click on open
	 */
	public void onOpenPhoto(Event e) {
		if(!photos.getSelectionModel().isEmpty()) {
			SceneManager.setScene(SceneManager.displayPhoto);
			DataManager.currentPhoto=photos.getSelectionModel().getSelectedItem();
			SceneManager.displayController.setPhoto();
		}
	}
	
	/**
	 * Goes to the Slideshow View scene
	 * @param e Event click on slideshow
	 */
	public void onSlideshow(Event e) {
		if(!DataManager.currentAlbum.photos.isEmpty()) {
			SceneManager.setScene(SceneManager.photoSlideshow);
			photos.getSelectionModel().select(0);
			DataManager.currentPhoto = photos.getSelectionModel().getSelectedItem();
			SceneManager.slideshowController.setInitialPhoto();
		} else {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setHeaderText(null);
			error.setContentText("Can Not Slideshow Empty Album");
			error.showAndWait();
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
