package model;

import java.net.MalformedURLException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundClipPlayer implements Runnable {

	private SoundClipBlockingQueue queue;
	private List<SoundClip> clips; // The list of SoundClips to play with the media player
	private static final int SLEEP_DELAY = 500; // Sleep time in ms between sound clips when playing several in a row
	private boolean playing = false; // True when playing SoundClips, false otherwise
	
	public SoundClipPlayer(SoundClipBlockingQueue queue){
		this.queue = queue;
		clips = new ArrayList<>();
	}
	
	// Main loop for the SoundClipPlayer thread
	//
	// The thread will dequeue a list of SoundClips from the
	// SoundClipBlockingQueue and play add them to the list
	// of SoundClips to play. If the queue is empty, the thread 
	// will go to sleep until there is a SoundClip to retrieve 
	// from the queue.
	
	@Override
	public void run(){
		List<SoundClip> sc = null;
		while (true) {
			try {
				
				// Retrieve the list of SoundClips from the blocking queue
				sc = queue.dequeue();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(sc != null) {
				
				// Add the retrieved SoundClips to the list of SoundClips to play
				clips.addAll(sc);
				
				// If not currently playing SoundClips, play the SoundClips
				// Note: if already playing, the newly retrieved SoundClips are
				// already added to the list that is being played. Therefore,
				// we don't need to invoke playClips again in that case
				
				if(!playing)
					playClips();
			}
		}
	}
	
	// Plays the SoundClips
	private synchronized void playClips(){
		if(clips == null || clips.size() == 0) {
			// Finished playing all the SoundClips
			playing = false;
			return;
		}
		try {
			
			playing = true;
			
			SoundClip sc = clips.remove(0); // Remove the first SoundClip from the list
			String name = sc.getFile().toString();
			System.out.println("Now playing " + name);
			
			// Setup the MediaPlayer
			Path audioPath = FileSystems.getDefault().getPath(name);
			
			Media media = new Media(audioPath.toUri().toURL().toExternalForm());
			MediaPlayer player = new MediaPlayer(media);
		
			// Setup media event handler, which will recursively call this method
			// when the play back of the current SoundClip has ended.
			//
			// Note: In each recursion, the size of the list of SoundClips is reduced by 1.
			// Therefore, it will eventually become empty, and play back will stop
			
			player.setOnEndOfMedia(new Runnable(){

				@Override
				public void run() {
					
					playClips();
				}
				
			});
			
			// Play the SoundClip
			player.play();
			
			// Put the current thread to sleep for a short while, in order to introduce a short
			// pause in between the play back of consecutive SoundClips
			
			Thread.sleep(SLEEP_DELAY);

		} catch (MalformedURLException e) {
			System.out.println("MalformedURLException in SoundClipPlayer");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		
	}

}
