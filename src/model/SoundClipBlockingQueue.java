package model;
import java.util.LinkedList;
import java.util.List;

public class SoundClipBlockingQueue {

	private List<List<SoundClip>> queue = new LinkedList<>();
	
	// Add a list of SoundClips to the queue, and notify all
	// threads waiting for SoundClips on the queue
	public synchronized void enqueue(List<SoundClip> soundclips){
		queue.add(soundclips);
	    notifyAll();
	}
	
	// Remove the oldest list of SoundClips from the queue. 
	// If the queue is empty, the invoking thread will wait until
	// there is a list of SoundClips in the queue, and then remove it from
	// the queue.
	public synchronized List<SoundClip> dequeue() throws InterruptedException{
		while(queue.size() == 0){
			wait();
		}
	    return queue.remove(0);
	}
}
