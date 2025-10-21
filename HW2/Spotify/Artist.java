import java.util.ArrayList;

public class Artist {

    private String username;
    private String password;
    private String nickname;
    private int followers;
    private ArrayList<Track> tracks = new ArrayList<>();
    private static Artist loggedInArtist;
    private static ArrayList<Artist> artists = new ArrayList<>();
    
    public Artist(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    public static Artist getArtistByUsername(String username) {
        for (Artist artist : Artist.artists) {
            if (artist.getUsername().equals(username)) {
                return artist;
            }
        }
        return null;
    }
    
    public static void addArtist(Artist artist) {
        Artist.artists.add(artist);
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public String getNickname() {
        return this.nickname;
    }
    
    public int getFollowers() {
        return this.followers;
    }
    
    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public ArrayList<Track> getTracks() {
        return this.tracks;
    }
    
    public void addToTracks(Track track) {
        this.tracks.add(track);
    }
    
    public static Artist getLoggedInArtist() {
        return Artist.loggedInArtist;
    }
    
    public static void setLoggedInArtist(Artist artist) {
        Artist.loggedInArtist = artist;
    }

    public int getRank() {
        int[] countOfFollowers = new int[artists.size()];
        int index = 0;
        for (Artist artist : artists) {
            countOfFollowers[index] = artist.getFollowers();
            index++;
        }
        for (int i = 0; i < artists.size(); i++) {
            for (int j = i; j < artists.size(); j++) {
                if (countOfFollowers[i] < countOfFollowers[j]) {
                    int temp = countOfFollowers[i];
                    countOfFollowers[i] = countOfFollowers[j];
                    countOfFollowers[j] = temp;
                }
            }
        }
        for (int i = 0; i < countOfFollowers.length; i++) {
            if (this.followers == countOfFollowers[i]) {
                return i + 1;
            }
        }
        return -1;
    }

}
