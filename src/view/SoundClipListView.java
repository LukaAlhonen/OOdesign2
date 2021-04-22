package view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import model.Album;
import model.SoundClip;

public class SoundClipListView extends ListView<SoundClip> {

	private List<SoundClip> clips;
	
	public SoundClipListView() {
		super();
		clips = new ArrayList<>();
	}

	public SoundClipListView(ObservableList<SoundClip> arg0) {
		super(arg0);
		clips = new ArrayList<>();
	}
	
	/**
	 * Displays the contents of the specified album
	 * @param album - the album which contents are to be displayed
	 */
	public void display(Album album){
		this.getItems().clear();
		Iterator iterator = album.getSoundClips().iterator();
		while (iterator.hasNext()) {
			SoundClip s = (SoundClip) iterator.next();
			clips.add(s);

		}

		// Add all SoundClips contained in the parameter album to 
		// the list of SoundClips 'clips' (the instance variable)
		

		
		ObservableList<SoundClip> temp = FXCollections.observableList(clips);
		this.setItems(temp);
	}

	public List<SoundClip> getSelectedClips(){
		ObservableList<SoundClip> items = this.getSelectionModel().getSelectedItems();
		List<SoundClip> clips = new ArrayList<>(items);
		return clips;
	}
}
