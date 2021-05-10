package model;

import view.MusicOrganizerWindow;
import java.util.List;

public class AddCommand implements Command{
    private Album album;
    private List<SoundClip> soundClips;
    private Album parent;
    private MusicOrganizerWindow view;

    public AddCommand(Album album, List<SoundClip> soundClips, MusicOrganizerWindow view) {
        this.album = album;
        this.soundClips = soundClips;
        this.view = view;
    }

    public AddCommand(Album album, MusicOrganizerWindow view){
        this.album = album;
        this.parent = album.getParent();
        this.view = view;
    }

    public void undo(){
        if(soundClips == null){
            parent.removeSubAlbum(album);
            view.onAlbumRemoved(album);
        } else{
            for(SoundClip clip : soundClips){
                album.removeSoundClip(clip);
                view.onClipsUpdated();
            }
        }
    }

    public void redo(){
        if(soundClips == null){
            parent.addSubAlbum(album);
            view.onAlbumAdded(parent, album);
        } else {
            for(SoundClip clip : soundClips){
                album.addSoundClip(clip);
                view.onClipsUpdated();
            }
        }
    }
}
