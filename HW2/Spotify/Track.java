import java.sql.Date;
import java.util.ArrayList;

public class Track {
    
    private String name;
    private Artist artist;
    private int duration;
    private Date releaseDate;
    private boolean type;
    private static ArrayList<Track> tracks = new ArrayList<>();

    public Track(Date releaseDate, Artist artist, boolean type, int duration, String name) {
        this.releaseDate = releaseDate;
        this.artist = artist;
        this.type = type;
        this.duration = duration;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
    
    public Artist getArtist() {
        return this.artist;
    }
    
    public int getDuration() {
        return this.duration;
    }
    
    public Date getReleaseDate() {
        return this.releaseDate;
    }
    
    public boolean getType() {
        return this.type;
    }
    
    public static void addTrack(Track track) {
        tracks.add(track);
    }
    
    public static ArrayList<Track> getTracks() {
        return Track.tracks;
    }
    
    public static Track getTrackByName(String name) {
        for (Track track : tracks) {
            if (track.getName().equals(name)) {
                return track;
            }
        }
        return null;
    }
}
