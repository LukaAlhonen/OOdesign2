package model;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.Set;

/**
 * SoundClipLoader finds sound clip files on disk.
 */
public class SoundClipLoader {

	/**
	 * Returns a set of sound clips representing all .WAV files under a given 
	 * folder (including subfolders as well).
	 * @param path the string representing the path to the folder.
	 * If path does not correspond to an actual folder in the filesystem,
	 * returns an empty set.
	 */
	public static Set<SoundClip> loadSoundClips(String path) {
		assert path!=null && path!="";
		Set<SoundClip> set = new HashSet<SoundClip>();

		if (path == null) return set;

		File f = new File(path);
		if (!f.isDirectory()) return set;

		addSoundClipsToSet(f, set);
		return set;
	}

	// Find all WAV files in folder and its sub folders and add them to set.
	// Requires folder to be an actual folder on disk and set != null.
	private static void addSoundClipsToSet(File folder, Set<SoundClip> set) {
		for (File f : findWAVFiles(folder)) {
			System.out.println("Loading... " + f.getAbsolutePath());
			set.add(new SoundClip(f));
		}

		for (File g: findSubFolders(folder)) {
			addSoundClipsToSet(g, set);
		}
	}
	
	// Return all the WAV files that are immediate children of folder.
	// Requires folder to be an actual folder on disk.
	private static File[] findWAVFiles(File folder) {
		return folder.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				int i = name.lastIndexOf ('.');
				return "wav".equals(name.substring(i+1).toLowerCase());
			}
		});
	}
	
	// Return all the immediate sub folders of folder.
	// Requires folder to be an actual folder on disk.
	private static File[] findSubFolders(File folder) {
		return folder.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return (pathname.isDirectory()); 
			}
		});
	}
}
