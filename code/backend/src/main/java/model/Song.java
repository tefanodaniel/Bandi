package model;

import java.util.Set;

public class Song {
    private String songId;
    private String songName;
    private String artistName;
    private String albumName;
    private Integer releaseYear;
    private Set<String> genres;

    public Song(String songId, String songName, String artistName, String albumName, Integer releaseYear, Set<String> genres) {
        this.songId = songId;
        this.songName = songName;
        this.artistName = artistName;
        this.albumName = albumName;
        this.releaseYear = releaseYear;
        this.genres = genres;
    }
    public String getSongId() {
        return this.songId;
    }

    public String getSongName() {
        return this.songName;
    }

    public void setSongName(String song_name) {
        this.songName = song_name;
    }

    public String getArtistName() {
        return this.artistName;
    }

    public void setArtistName(String artist_name) {
        this.artistName = artist_name;
    }

    public String getAlbumName() {
        return this.albumName;
    }

    public void setAlbumName(String album_name) {
        this.albumName = album_name;
    }

    public Integer getReleaseYear() {
        return this.releaseYear;
    }

    public void setReleaseYear(Integer release_year) {
        this.releaseYear = release_year;
    }

    public Set<String> getGenres() {
        return this.genres;
    }

    public void setGenres(Set<String> genre_set) {
        this.genres = genre_set;
    }

    @Override
    public String toString() {
        return "Song {"+
                ", songId = '" + this.songId + '\''+
                ", songName = '" + this.songName + '\''+
                ", artistName = '" + artistName + '\'' +
                ", albumName = '" + this.albumName + '\''+
                ", releaseYear = '" + this.releaseYear + '\''+
                ", genres =  '" + this.genres + '\''+
                '}';
    }




}
