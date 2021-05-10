package model;

import controller.MusicOrganizerController;

import view.MusicOrganizerWindow;

import java.util.Iterator;
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

    public RemoveCommand(Album album, MusicOrganizerWindow view){
        this.album = album;
        this.parent = album.getParent();
        this.view = view;
    }

    public void undo(){
        if(soundClips == null){
            parent.addSubAlbum(album);
            view.onAlbumAdded(parent, album);
            update(album);
        } else {
            for(SoundClip clip : soundClips){
                album.addSoundClip(clip);
                view.onClipsUpdated();
            }
        }
    }

    public void redo(){
        if(soundClips == null) {
            parent.removeSubAlbum(album);
            view.onAlbumRemoved(album);
        } else {
            for(SoundClip clip : soundClips){
                album.removeSoundClip(clip);
                view.onClipsUpdated();
            }
        }


    }

    public void update(Album album){
        Iterator it = album.getSubAlbums().iterator();
        while(it.hasNext()){
            Album a = (Album) it.next();
            view.onAlbumAdded(album, a);
            update(a);
        }
    }
}
