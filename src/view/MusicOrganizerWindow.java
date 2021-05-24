package view;

import controller.MusicOrganizerController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Album;
import model.SoundClip;

import java.util.List;
import java.util.Optional;


public class MusicOrganizerWindow extends Application {
	
	private BorderPane bord;
	private static MusicOrganizerController controller;
	private TreeItem<Album> rootNode;
	private TreeView<Album> tree;
	private ButtonPaneHBox buttons;
	private SoundClipListView soundClipTable;
	private TextArea messages;
	
	
	public static void main(String[] args) {
		controller = new MusicOrganizerController();
		if (args.length == 0) {
			controller.loadSoundClips("sample-sound");
		} else if (args.length == 1) {
			controller.loadSoundClips(args[0]);
		} else {
			System.err.println("too many command-line arguments");
			System.exit(0);
		}
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
				
		try {
			controller.registerView(this);
			primaryStage.setTitle("Music Organizer");
			
			bord = new BorderPane();
			
			// Create buttons in the top of the GUI
			buttons = new ButtonPaneHBox(controller, this);
			bord.setTop(buttons);

			// Create the tree in the left of the GUI
			tree = createTreeView();
			bord.setLeft(tree);
			
			// Create the list in the right of the GUI
			soundClipTable = createSoundClipListView();
			bord.setCenter(soundClipTable);
						
			// Create the text area in the bottom of the GUI
			bord.setBottom(createBottomTextArea());
			
			Scene scene = new Scene(bord);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.sizeToScene();
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent arg0) {
					Platform.exit();
					System.exit(0);
					
				}
				
			});

			primaryStage.show();
			controller.addSearchAlbums();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private TreeView<Album> createTreeView(){
		rootNode = new TreeItem<>(controller.getRootAlbum());
		TreeView<Album> v = new TreeView<>(rootNode);
		//Fråga kristian om hur man sätter en ny nod/album till treeview
		v.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				if(e.getClickCount()==2) {
					// This code gets invoked whenever the user double clicks in the TreeView
					// TODO: ADD YOUR CODE HERE
					soundClipTable.display(getSelectedAlbum());
					
				}
				
			}
			
		});

		
		return v;
	}
	
	private SoundClipListView createSoundClipListView() {
		SoundClipListView v = new SoundClipListView();
		v.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		v.display(controller.getRootAlbum());
		
		v.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				if(e.getClickCount() == 2) {
					// This code gets invoked whenever the user double clicks in the sound clip table
					// TODO: ADD YOUR CODE HERE
					controller.playSoundClips();
					
				}
				
			}
			
		});
		
		return v;
	}
	
	private ScrollPane createBottomTextArea() {
		messages = new TextArea();
		messages.setPrefRowCount(3);
		messages.setWrapText(true);
		messages.prefWidthProperty().bind(bord.widthProperty());
		messages.setEditable(false); // don't allow user to edit this area
		
		// Wrap the TextArea in a ScrollPane, so that the user can scroll the 
		// text area up and down
		ScrollPane sp = new ScrollPane(messages);
		sp.setHbarPolicy(ScrollBarPolicy.NEVER);
		sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		
		return sp;
	}
	
	/**
	 * Displays the message in the text area at the bottom of the GUI
	 * @param message the message to display
	 */
	public void displayMessage(String message) {
		messages.appendText(message + "\n");
	}
	
	public Album getSelectedAlbum() {
		TreeItem<Album> selectedItem = getSelectedTreeItem();
		return selectedItem == null ? null : selectedItem.getValue();
	}
	
	private TreeItem<Album> getSelectedTreeItem(){
		return tree.getSelectionModel().getSelectedItem();
	}
	
	
	
	/**
	 * Pop up a dialog box prompting the user for a name for a new album.
	 * Returns the name, or null if the user pressed Cancel
	 */
	public String promptForAlbumName() {
		TextInputDialog dialog = new TextInputDialog();
		
		dialog.setTitle("Enter album name");
		dialog.setHeaderText(null);
		dialog.setContentText("Please enter the name for the album");
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {
			return result.get();
		} else {
			return null;
		}
	}

	public int promptForGrade() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Enter grade for soundclip");
		dialog.setHeaderText("GRADE");
		dialog.setContentText("Enter the grade or be doomed in hell for eternity.");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			if(isInteger(result.get())){
				return Integer.parseInt(result.get());
			}
		}
		return -1;
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch(NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * Return all the sound clips currently selected in the clip table.
	 */
	public List<SoundClip> getSelectedSoundClips(){
		return soundClipTable.getSelectedClips();
	}
	
	
	
	/**
	 * *****************************************************************
	 * Methods to be called in response to events in the Music Organizer
	 * *****************************************************************
	 */	
	
	
	
	/**
	 * Updates the album hierarchy with a new album
	 * @param newAlbum
	 */
	public void onAlbumAdded(Album parent, Album newAlbum){

		TreeItem<Album> root = tree.getRoot();
		TreeItem<Album> parentNode = findAlbumNode(parent, root);

		parentNode.getChildren().add(new TreeItem<>(newAlbum));
		parentNode.setExpanded(true); // automatically expand the parent node in the tree

	}
	/**
	 * Updates the album hierarchy by removing an album from it
	 */
	public void onAlbumRemoved(Album toRemove) {

		TreeItem<Album> root = tree.getRoot();

		TreeItem<Album> nodeToRemove = findAlbumNode(toRemove, root);
		nodeToRemove.getParent().getChildren().remove(nodeToRemove);
	}

	private TreeItem<Album> findAlbumNode(Album albumToFind, TreeItem<Album> root) {

		// recursive method to locate a node that contains a specific album in the TreeView

		if(root.getValue().equals(albumToFind)) {
			return root;
		}

		for(TreeItem<Album> node : root.getChildren()) {
			TreeItem<Album> item = findAlbumNode(albumToFind, node);
			if(item != null)
				return item;
		}

		return null;
	}
	
	/**
	 * Refreshes the clipTable in response to the event that clips have
	 * been modified in an album
	 */
	public void onClipsUpdated(){
		Album a = getSelectedAlbum();
		soundClipTable.display(a);
	}

	public void onClipsUpdated(Album album){
		soundClipTable.display(album);
	}
	
}
