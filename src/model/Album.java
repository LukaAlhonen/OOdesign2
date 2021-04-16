/*
* @Authors: Luka Alhonen, Amos Weckström
* Denna klass representerar ett album. Albumet kan innehålla både subalbum
* och ljudfiler
*/
package model;

import java.util.ArrayList;
import java.util.*;

public class Album {
    private final int MAX_SUBALBUMS = 500; // Max antal subalbum
    private int numSubAlbums; // Håller reda på mängden subalbum
    private String name;
    private List<Album> SubAlbum = new ArrayList<Album>(); // Arraylist för att representera SubAlbum
    private List<SoundClip> LjudFiler = new ArrayList<SoundClip>(); // Arraylist för att representera LjudFiler

    // Simpel construktor, om namnet är tomt får albumet ett default namn
    public Album(String name){
        if(name != "") {
            this.name = name;
        } else{
            this.name = "New Album";
        }
    }

    // Simpel construktor med default namn
    public Album(){
        this.name = "New Album";
    }
    
    // Getters
    // Returnerar albumets namn som en string
    public String toString(){
        assert invariant();
        return name;
    }

    // Returnerar ljudfilen vid indexet
    public SoundClip getSoundClip(int index){
        if(LjudFiler.size() == 0){
            return null;
        }
        assert invariant();
        return LjudFiler.get(index);
    }

    // Returnerar den sista ljudfilen
    public SoundClip getSoundClip(){
        if(LjudFiler.size() == 0){
            return null;
        }
        assert invariant();
        return LjudFiler.get(LjudFiler.size() - 1);
    }

    public int getNumSoundClips() {
        return LjudFiler.size();
    }
    // Returnerar subalbumet vid indexet
    public Album getSubAlbum(int index){
        if(SubAlbum.size() == 0){
            return null;
        }
        assert invariant();
        return SubAlbum.get(index);
    }

    // Returnerar det sista subalbumet
    public Album getSubAlbum(){
        if(SubAlbum.size() == 0){
            return null;
        }
        assert invariant();
        return SubAlbum.get(SubAlbum.size()-1);
    }

    // Returnerar mängden subalbum
    public int getNumSubAlbums(){
        assert invariant();
        return numSubAlbums;
    }

    // Setters
    // Lägger till en ljudfil, ingen gräns på mängden ljudfiler i album
    public void addSoundClip(SoundClip song){
        LjudFiler.add(song);
        assert invariant();
    }

    // Lägger till ett subalbum om det finns rum
    public void addSubAlbum(Album subalbum){
        if(numSubAlbums < MAX_SUBALBUMS) {
            SubAlbum.add(subalbum);
            numSubAlbums++;
        }
        assert invariant();
    }

    // Tar bort subalbumet vid indexet och returnerar det
    public Album removeSubAlbum(int index){
        if(SubAlbum.size() == 0){
            return null;
        }
        Album toReturn = SubAlbum.remove(index);
        assert invariant();
        return toReturn;
    }

    // Tar bort det sista subalbumet och returnerar det
    public Album removeSubAlbum(){
        if(SubAlbum.size() == 0){
            return null;
        }
        Album toReturn = SubAlbum.remove(SubAlbum.size() - 1);
        assert invariant();
        return toReturn;
    }

    // Tar bort ljudfilen vid indexet och returnerar den
    public SoundClip removeSoundClip(int index){
        if(LjudFiler.size() == 0){
            return null;
        }
        SoundClip toReturn =LjudFiler.remove(index);
        assert invariant();
        return toReturn;
    }

    // Tar bort den sista ljudfilen och returnerar den
    public SoundClip removeSoundClip(){
        if(LjudFiler.size() == 0){
            return null;
        }
        SoundClip toReturn = LjudFiler.remove(LjudFiler.size() - 1);
        assert invariant();
        return toReturn;
    }

    // Metod för att kolla klassinvarianten
    public boolean invariant(){
        return name != "" && SubAlbum.size() >= 0 && LjudFiler.size() >= 0 && SubAlbum.size() <= 500;
    }

    
}