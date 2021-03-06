package view;


import controller.MusicOrganizerController;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

public class ButtonPaneHBox extends HBox {

	private MusicOrganizerController controller;
	private MusicOrganizerWindow view;

	private Button newAlbumButton;
	private Button deleteAlbumButton;
	private Button addSoundClipsButton;
	private Button removeSoundClipsButton;
	private Button playButton;
	private Button undoButton;
	private Button redoButton;
	private Button flagButton;
	private Button gradeButton;
	public static final int BUTTON_MIN_WIDTH = 150;



	public ButtonPaneHBox(MusicOrganizerController contr, MusicOrganizerWindow view) {
		super();
		this.controller = contr;
		this.view = view;

		newAlbumButton = createNewAlbumButton();
		this.getChildren().add(newAlbumButton);

		deleteAlbumButton = createDeleteAlbumButton();
		this.getChildren().add(deleteAlbumButton);

		addSoundClipsButton = createAddSoundClipsButton();
		this.getChildren().add(addSoundClipsButton);

		removeSoundClipsButton = createRemoveSoundClipsButton();
		this.getChildren().add(removeSoundClipsButton);

		playButton = createPlaySoundClipsButton();
		this.getChildren().add(playButton);

		undoButton = createUndoButton();
		this.getChildren().add(undoButton);

		redoButton = createRedoButton();
		this.getChildren().add(redoButton);

		flagButton = createFlagButton();
		this.getChildren().add(flagButton);

		gradeButton = createGradeButton();
		this.getChildren().add(gradeButton);

	}
	
	/*
	 * Each method below creates a single button. The buttons are also linked
	 * with event handlers, so that they react to the user clicking on the buttons
	 * in the user interface
	 */

	private Button createNewAlbumButton() {
		Button button = new Button("New Album");
		button.setTooltip(new Tooltip("Create new sub-album to selected album"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				controller.addNewAlbum(view.getSelectedAlbum());
				enableUndo();
			}
			
		});
		return button;
	}
	
	private Button createDeleteAlbumButton() {
		Button button = new Button("Remove Album");
		button.setTooltip(new Tooltip("Remove selected album"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				controller.deleteAlbum(view.getSelectedAlbum());
				enableUndo();
			}
			
		});
		return button;
	}
	
	private Button createAddSoundClipsButton() {
		Button button = new Button("Add Sound Clips");
		button.setTooltip(new Tooltip("Add selected sound clips to selected album"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				controller.addSoundClips(view.getSelectedAlbum(),view.getSelectedSoundClips());
				enableUndo();
			}
			
		});
		return button;
	}
	
	private Button createRemoveSoundClipsButton() {
		Button button = new Button("Remove Sound Clips");
		button.setTooltip(new Tooltip("Remove selected sound clips from selected album"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				controller.removeSoundClips(view.getSelectedAlbum(), view.getSelectedSoundClips());
				enableUndo();
			}
			
		});
		return button;
	}
	
	private Button createPlaySoundClipsButton() {
		Button button = new Button("Play Sound Clips");
		button.setTooltip(new Tooltip("Play selected sound clips"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				
				controller.playSoundClips();
			}
			
		});
		return button;
	}

	private Button createUndoButton(){
		Button button = new Button("Undo");
		button.setTooltip(new Tooltip("Undo last operation"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setDisable(true);
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				controller.undo();
				// Enable redoButton
				if(controller.undoIsEmpty()){
					button.setDisable(true);
				}
				if(redoButton.isDisabled()){
					redoButton.setDisable(false);
				}
			}
		});
		return button;
	}

	private Button createRedoButton(){
		Button button = new Button("Redo");
		button.setTooltip(new Tooltip("Redo last operation"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setDisable(true);
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				controller.redo();
				if(controller.redoIsEmpty()){
					button.setDisable(true);
				}
				enableUndo();
			}
		});

		return button;
	}

	public void enableUndo(){
		if(controller.redoIsEmpty()) {
			redoButton.setDisable(true);
		}
		if(undoButton.isDisabled()){
			undoButton.setDisable(false);
		}
	}

	private Button createFlagButton(){
		Button button = new Button("Flag");
		button.setTooltip(new Tooltip("Flag soundclip"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				controller.flag(view.getSelectedSoundClips());
			}
		});

		return button;
	}

	private Button createGradeButton(){
		Button button = new Button("Grade");
		button.setTooltip(new Tooltip("Grade soundclip"));
		button.setMinWidth(BUTTON_MIN_WIDTH);
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				controller.grade(view.getSelectedSoundClips());
			}
		});

		return button;
	}

}
