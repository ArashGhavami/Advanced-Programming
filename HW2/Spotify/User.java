import java.util.ArrayList;

public class User {
    
    private String username;
    private String password;
    private int followers;
    {
        followers = 0;
    }
    private int following;
    {
        following = 0;
    }
    private ArrayList<Track> queue = new ArrayList<>();
    private ArrayList<Track> likedTracks = new ArrayList<>();
    private ArrayList<Playlist> playlists = new ArrayList<>();
    private static User loggedInUser;
    private static ArrayList<User> users = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
    }
    
    public void removePlaylist(Playlist playlist) {
        ArrayList<Playlist> newPlaylists = new ArrayList<>();
        for (Playlist playlist2 : playlists) {
            if (!playlist2.getName().equals(playlist.getName())) {
                newPlaylists.add(playlist2);
            }
        }
        playlists = newPlaylists;
    }
    
    public void addToQueue(Track track) {
        queue.add(track);
    }
     
    public void removeFromQueue(Track track) {
        ArrayList<Track> newQueue = new ArrayList<>();
        for (Track track2 : queue) {
            if (!track2.getName().equals(track.getName())) {
                newQueue.add(track2);
            }
        }
        this.queue = newQueue;
        System.out.println("track removed from queue successfully");
    }
    
    public void addLikedTrack(Track track) {
        likedTracks.add(track);
    }
    
    public void removeLikedTrack(Track track) {
        ArrayList<Track> newLikedTracks = new ArrayList<>();
        for (Track track2 : likedTracks) {
            if (!track2.getName().equals(track.getName())) {
                newLikedTracks.add(track2);
            }
        }
        this.likedTracks = newLikedTracks;
        System.out.println("track removed from liked tracks successfully");
    }
    
    public static void addUser(User user) {
        users.add(user);
    }
    
    public static User getUserByUsername(String username) {
        if (User.users == null)
            return null;
        for (User user : User.users) {
            if (user.getUsername().equals(username) == true) {
                return user;
            }
        }
        return null;
    }
    
    public ArrayList<Track> getQueue() {
        return this.queue;
    }
    
    public void setQueue(ArrayList<Track> queue) {
        this.queue = queue;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public ArrayList<Playlist> getPlaylists() {
        return this.playlists;
    }
    
    public int getFollowers() {
        return this.followers;
    }
    
    public void setFollowers(int followers) {
        this.followers = followers;
    }
    
    public int getFollowings() {
        return following;
    }
    
    public void setFollowings(int following) {
        this.following = following;
    }
    
    public ArrayList<Track> getLikedTracks() {
        return this.likedTracks;
    }
    
    public String getPassord() {
        return this.password;
    }
    
    public static User getLoggedInUser() {
        return User.loggedInUser;
    }
    
    public static void setLoggedInUser(User user) {
        User.loggedInUser = user;
    }

    public static ArrayList<User> getUsers() {
        return User.users;
    }
}
