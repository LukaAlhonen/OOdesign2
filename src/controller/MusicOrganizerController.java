package controller;

import java.util.*;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import model.Album;


import model.SoundClip;
import model.SoundClipBlockingQueue;
import model.SoundClipLoader;
import model.SoundClipPlayer;
import view.MusicOrganizerWindow;

public class MusicOrganizerController {

	private MusicOrganizerWindow view;
	private SoundClipBlockingQueue queue;
	private Album root;
	private String name;
	private Album newTreeNode;
	
	public MusicOrganizerController() {

		// TODO: Create the root album for all sound clips
		root = new Album("All Sound Clips");
		
		// Create the blocking queue
		queue = new SoundClipBlockingQueue();
				
		// Create a separate thread for the sound clip player and start it
		
		(new Thread(new SoundClipPlayer(queue))).start();
	}
	
	/**
	 * Load the sound clips found in all subfolders of a path on disk. If path is not
	 * an actual folder on disk, has no effect.
	 */
	public Set<SoundClip> loadSoundClips(String path) {
		Set<SoundClip> clips = SoundClipLoader.loadSoundClips(path);
		// TODO: Add the loaded sound clips to the root album
		Iterator<SoundClip> it = clips.iterator();
		while (it.hasNext()) {
			root.addSoundClip(it.next());

		}



		return clips;
	}
	
	public void registerView(MusicOrganizerWindow view) {
		this.view = view;
	}
	
	/**
	 * Returns the root album
	 */
	public Album getRootAlbum(){
		return root;
	}
	
	/**
	 * Adds an album to the Music Organizer
	 */
	public void addNewAlbum(Album album){ //TODO Update parameters if needed - e.g. you might want to give the currently selected album as parameter
		// TODO: Add your code here
		Album x = new Album(view.promptForAlbumName(), album);
		if (album == null) {
			getRootAlbum().addSubAlbum(x);
			view.onAlbumAdded(x);
		} else {
			album.addSubAlbum(x);
			view.onAlbumAdded(x);
		}



		
	}
	
	/**
	 * Removes an album from the Music Organizer
	 */
	public void deleteAlbum(Album album){
		if(album == root) {
			return;
		}
		album.getParent().removeSubAlbum(album);
		view.onAlbumRemoved();

		//TODO Update parameters if needed
		// TODO: Add your code here
	}
	
	/**
	 * Adds sound clips to an album
	 */
	public void addSoundClips(Album album, List<SoundClip> clep){ //TODO Update parameters if needed
		for (SoundClip clip: clep) {
			album.addSoundClip(clip);

		}
	}
	
	/**
	 * Removes sound clips from an album
	 */
	public void removeSoundClips(Album album, SoundClip clip){ //TODO Update parameters if needed

		// TODO: Add your code here
		
	}
	
	/**
	 * Puts the selected sound clips on the queue and lets
	 * the sound clip player thread play them. Essentially, when
	 * this method is called, the selected sound clips in the 
	 * SoundClipTable are played.
	 */
	public void playSoundClips(){
		List<SoundClip> l = view.getSelectedSoundClips();
		queue.enqueue(l);
		for(int i=0;i<l.size();i++) {
			view.displayMessage("Playing " + l.get(i));
		}
	}
}
