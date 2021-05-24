package controller;

import java.util.*;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import model.Album;

import model.AddCommand;
import model.RemoveCommand;
import model.SoundClip;
import model.SoundClipBlockingQueue;
import model.SoundClipLoader;
import model.SoundClipPlayer;
import view.MusicOrganizerWindow;
import model.Command;

public class MusicOrganizerController {

	private MusicOrganizerWindow view;
	private SoundClipBlockingQueue queue;
	private Album root;
	private String name;
	private Album newTreeNode;
	private LinkedList<Command> undoStack = new LinkedList<>();
	private LinkedList<Command> redoStack = new LinkedList<>();
	private Album great;
	private Album flagged;
	
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

	public void addSearchAlbums(){
		great = new Album("Great SoundClips", root);
		flagged = new Album("Flagged SoundClips", root);
		root.addSubAlbum(great);
		view.onAlbumAdded(root, great);
		root.addSubAlbum(flagged);
		view.onAlbumAdded(root, flagged);
	}
	
	/**
	 * Adds an album to the Music Organizer
	 */
	public void addNewAlbum(Album album){ //TODO Update parameters if needed - e.g. you might want to give the currently selected album as parameter
		// TODO: Add your code here
		Album x;
		if(album == null){
			return;
		}
		while(true) {
			x = new Album(view.promptForAlbumName(), album);
			if(!(x.toString().equals(great.toString())) && !(x.toString().equals(flagged.toString()))){
				break;
			}
		}
		album.addSubAlbum(x);
		view.onAlbumAdded(album, x);
		newAdd(x, null);
		redoStack.clear();
	}
	
	/**
	 * Removes an album from the Music Organizer
	 */
	public void deleteAlbum(Album album){
		//TODO Update parameters if needed
		// TODO: Add your code here
		newRemove(album,null);
		if(album == root) {
			return;
		}
		album.getParent().removeSubAlbum(album);
		view.onAlbumRemoved(album);
		redoStack.clear();
	}
	
	/**
	 * Adds sound clips to an album
	 */
	public void addSoundClips(Album album, List<SoundClip> clep){ //TODO Update parameters if needed
		newAdd(album, clep);
		Album temp = album.getParent();
		for (SoundClip clip: clep) {
			album.addSoundClip(clip);
			while (temp != root) {
				temp.addSoundClip(clip);
				temp = temp.getParent();
			}
			temp = album.getParent();
		}
		redoStack.clear();
	}
	
	/**
	 * Removes sound clips from an album
	 */
	public void removeSoundClips(Album album, List<SoundClip> clips){ //TODO Update parameters if needed

		newRemove(album, clips);

		if(album.getSubAlbums() != null) {
			Iterator itr = album.getSubAlbums().iterator();
			while(itr.hasNext()){
				removeSoundClips((Album) itr.next(), clips);
			}
		}
		// TODO: Add your code here
		for(SoundClip clip : clips){
			album.removeSoundClip(clip);

		}
		view.onClipsUpdated();
		redoStack.clear();
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

	public void newRemove(Album album, List<SoundClip> clips){
		RemoveCommand rc;
		if(clips == null) {
			rc = new RemoveCommand(album, view);
		} else{
			rc = new RemoveCommand(album, clips, view);
		}
		undoStack.push(rc);
	}

	public void newAdd(Album album, List<SoundClip> clips){
		AddCommand ac;
		if(clips == null) {
			ac = new AddCommand(album, view);
		} else{
			ac = new AddCommand(album, clips, view);
		}
		undoStack.push(ac);
	}

	public void undo(){
		if(undoStack.isEmpty()){
			return;
		}
		Command comm = undoStack.pop();
		redoStack.push(comm);
		comm.undo();
	}

	public void redo(){
		if(redoStack.isEmpty()){
			return;
		}
		Command comm = redoStack.pop();
		undoStack.push(comm);
		comm.redo();
	}

	public boolean undoIsEmpty(){
		return undoStack.isEmpty();
	}

	public boolean redoIsEmpty(){
		return redoStack.isEmpty();
	}

	public void flag(List<SoundClip> clips){
		List<SoundClip> temp = new LinkedList<>();
		for(SoundClip clip : clips){
			temp.clear();
			clip.flag();
			if(clip.isFlagged()){
				temp.add(clip);
				addSoundClips(flagged, temp);
			} else{
				temp.add(clip);
				removeSoundClips(flagged, temp);
			}
			view.onClipsUpdated(flagged);
		}
	}


	public void grade(List<SoundClip> clips) {
		int grade;
		List<SoundClip> temp = new LinkedList<>();
		while(true){
			grade = view.promptForGrade();
			if(grade >= 0 && grade <= 5){
				break;
			}
		}
		for(SoundClip clip : clips){
			clip.grade(grade);
			temp.add(clip);
			if(clip.getGrade() >= 4){
				addSoundClips(great, temp);
			} else if(clip.isGraded()){
				removeSoundClips(great, temp);
			}
		}
		view.onClipsUpdated(great);
	}
}

