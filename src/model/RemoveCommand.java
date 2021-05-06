package model;

import controller.MusicOrganizerController;

import view.MusicOrganizerWindow;
import java.util.List;

public class RemoveCommand implements Command{
    private Album album;
    private List<SoundClip> soundClips;
    private Album parent;
    private MusicOrganizerWindow view;

    public RemoveCommand(Album album, List<SoundClip> soundClips, MusicOrganizerWindow view){
        this.album = album;
        this.soundClips = soundClips;
        this.view = view;
    }

    public RemoveCommand(Album album, Album parent, MusicOrganizerWindow view){
        this.album = album;
        this.parent = parent;
        this.view = view;
    }

    public void undo(){
        if(soundClips == null){
            parent.addSubAlbum(album);
            view.onAlbumAdded(parent, album);
        } else{
            for(SoundClip clip : soundClips){
                album.addSoundClip(clip);
                view.onClipsUpdated();
            }
        }
    }

    public void redo(){

    }
}
