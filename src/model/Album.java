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
    private TreeSet<Album> SubAlbum = new TreeSet<Album>(); // Arraylist för att representera SubAlbum
    private TreeSet<SoundClip> LjudFiler = new TreeSet<SoundClip>(); // Arraylist för att representera LjudFiler
    private Album parent;

    // Simpel construktor, om namnet är tomt får albumet ett default namn
    public Album(String name, Album parent) {
        if(name != "") {
            this.name = name;
        } else{
            this.name = "New Album";
        }
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
        return SubAlbum;
    }

    public Set getSoundClips() {
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


    // Tar bort det sista subalbumet och returnerar det
    public boolean removeSubAlbum(Album album){

        assert invariant();
        return SubAlbum.remove(album);

    }

    // Tar bort ljudfilen vid indexet och returnerar den
    public boolean removeSoundClip(SoundClip song){

        assert invariant();
        return LjudFiler.remove(song);
    }


    // Metod för att kolla klassinvarianten
    public boolean invariant(){
        return name != "" && SubAlbum.size() >= 0 && LjudFiler.size() >= 0 && SubAlbum.size() <= 500;
    }

    public Album getParent() {
        return parent;
    }

    
}