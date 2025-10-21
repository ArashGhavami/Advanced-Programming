import java.util.ArrayList;

public class Playlist {

    private String name;
    private User owner;
    private ArrayList<Track> tracks = new ArrayList<>();
    private static ArrayList<Playlist> playlists = new ArrayList<>();
    private static Playlist currentPlaylist;

    public Playlist(String name, User owner) {
        this.name = name;
        this.owner = owner;
    }

    public void addTrack(Track track) {
        tracks.add(track);
    }
    
    public void removeTrack(Track track) {
        ArrayList<Track> newTracks = new ArrayList<>();
        for (Track track2 : tracks) {
            if (!track2.getName().equals(track.getName())) {
                newTracks.add(track2);
            }
        }
        tracks = newTracks;
    }
    
    public User getOwner() {
        return this.owner;
    }
    
    public String getName() {
        return this.name;
    }
    
    public static void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
    }
    
    public static void removePlaylist(Playlist playlist) {
        ArrayList<Playlist> newPlaylists = new ArrayList<>();
        for (Playlist playlist2 : playlists) {
            if (!playlist2.getName().equals(playlist.getName())) {
                newPlaylists.add(playlist2);
            }
        }
        playlists = newPlaylists;
    }
    
    public static Playlist getPlaylistByName(String name) {
        for (Playlist playlist : playlists) {
            if (playlist.getName().equals(name)) {
                return playlist;
            }
        }
        return null;
    }
    
    public static Playlist getCurrentPlaylist() {
        return currentPlaylist;
    }
    
    public static void setCurrentPlaylist(Playlist playlist) {
        Playlist.currentPlaylist = playlist;
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }
}