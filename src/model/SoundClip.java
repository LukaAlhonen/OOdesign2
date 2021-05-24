package model;
import java.io.File;

/**
 * SoundClip is a class representing a digital
 * sound clip file on disk.
 */
public class SoundClip {

	private boolean flag;
	private int grade;
	private final File file;
	private boolean isGraded;
	
	/**
	 * Make a SoundClip from a file.
	 * Requires file != null.
	 */
	public SoundClip(File file) {
		assert file != null;
		this.file = file;
	}

	/**
	 * @return the file containing this sound clip.
	 */
	public File getFile() {
		return file;
	}
	
	public String toString(){
		String name = file.getName();
		if(isFlagged()) {
			name = name + " (f)";
		}
		if(isGraded){
			name = name + " (" + String.valueOf(grade) + ")";
		}
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		return 
			obj instanceof SoundClip
			&& ((SoundClip)obj).file.equals(file);
	}
	
	@Override
	public int hashCode() {
		return file.hashCode();
	}

	public boolean isFlagged(){
		return flag;
	}

	public void flag(){
		if(flag){
			flag = false;
			File flagged = new File(getFile().getName() + " F");
			getFile().renameTo(flagged);
		} else{
			flag = true;
		}
	}

	public void grade(int num){
		grade = num;
		isGraded = true;
	}

	public int getGrade(){
		return grade;
	}

	public boolean isGraded(){ return isGraded;}
}
