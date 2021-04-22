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
    private Set<Album> SubAlbum = new HashSet<>(); // Arraylist för att representera SubAlbum
    private Set<SoundClip> LjudFiler = new HashSet<>(); // Arraylist för att representera LjudFiler
    private Album parent; // För att skapa en länk till albumets parent

    // Simpel construktor, om namnet är tomt får albumet ett default namn
    public Album(String name, Album parent) {
        if(name != "") {
            this.name = name;
        } else{
            this.name = "New Album";
        }
        this.parent = parent;
    }
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

    public Set getSubAlbums() {
        assert invariant();
        return SubAlbum;
    }

    public Set getSoundClips() {
        assert invariant();
        return LjudFiler;
    }


    // Returnerar albumets namn som en string

    public String toString(){
        assert invariant();
        return name;
    }


    public int getNumSoundClips() {
        return LjudFiler.size();
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


    // Tar bort det givna subalbumet
    public boolean removeSubAlbum(Album album){

        assert invariant();
        return SubAlbum.remove(album);

    }

    // Tar bort den givna ljudfilen
    public boolean removeSoundClip(SoundClip song){

        assert invariant();
        return LjudFiler.remove(song);
    }


    // Metod för att kolla klassinvarianten
    public boolean invariant(){
        return name != "" && SubAlbum.size() >= 0 && LjudFiler.size() >= 0 && SubAlbum.size() <= 500;
    }

    // Returnerar albumets parent
    public Album getParent() {

        assert invariant();
        return parent;
    }

    
}