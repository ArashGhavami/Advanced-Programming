import java.util.Arrays;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserMenu {

    public static void run(Scanner scanner) {
        outerloop:
        while (true) {
            boolean isInvalidCommand = true;
            String input = scanner.nextLine();
            String regex = "";
            Matcher matcher;

            //show playlists:
            regex = "show playlists\\s*";
            matcher = getCommnadMatcherInput(input, regex);
            if (matcher.matches()) {
                showPlaylists();
                isInvalidCommand = false;
                continue outerloop;
            }

            //show liked tracks:
            regex = "show liked tracks\\s*";
            matcher = getCommnadMatcherInput(input, regex);
            if (matcher.matches()) {
                showLikedTracks();
                isInvalidCommand = false;
                continue outerloop;
            }

            //show queue:
            regex = "show queue\\s*";
            matcher = getCommnadMatcherInput(input, regex);
            if (matcher.matches()) {
                showQueue();
                isInvalidCommand = false;
                continue outerloop;
            }

            //add to queue:
            regex = "add -t (?<trackname>[a-zA-Z0-9 ]+) to queue\\s*";
            matcher = getCommnadMatcherInput(input, regex);
            if (matcher.matches()) {
                String trackname = matcher.group("trackname");
                Track track = Track.getTrackByName(trackname);
                isInvalidCommand = false;
                if (track == null) {
                    System.out.println("no such track");
                    continue outerloop;
                } else {
                    System.out.println("track added to queue successfully");
                    addTrackToQueue(matcher);
                    continue outerloop;
                }
            }

            //like a track:
            regex = "like track -t (?<trackname>[a-zA-Z0-9 ]+)\\s*";
            matcher = getCommnadMatcherInput(input, regex);
            if (matcher.matches()) {
                isInvalidCommand = false;
                addTrackToLikedTracks(matcher);
                continue outerloop;
            }

            //remove from queue:
            regex = "remove -t (?<trackname>[a-zA-Z0-9 ]+) from queue\\s*";
            matcher = getCommnadMatcherInput(input, regex);
            if (matcher.matches()) {
                removeFromQueue(matcher);
                isInvalidCommand = false;
                continue outerloop;
            }

            //remove from liked tracks:
            regex = "remove -t (?<trackname>[a-zA-Z0-9 ]+) from liked tracks\\s*";
            matcher = getCommnadMatcherInput(input, regex);
            if (matcher.matches()) {
                removeTrackFromLikedTracks(matcher);
                isInvalidCommand = false;
                continue outerloop;
            }

            //reverse queue:
            regex = "reverse order of queue from (?<start>.+) to (?<end>.+)\\s*";
            matcher = getCommnadMatcherInput(input, regex);
            if (matcher.matches()) {
                reverseOrderOfQueue(scanner, matcher);
                isInvalidCommand = false;
                continue outerloop;
            }

            //create playlist:
            regex = "create -p (?<playlistName>[a-zA-Z0-9 ]+)\\s*";
            matcher = getCommnadMatcherInput(input, regex);
            if (matcher.matches()) {
                createPlaylist(matcher);
                isInvalidCommand = false;
                continue outerloop;
            }

            //remove playlist:
            regex = "delete -p (?<playlistName>[a-zA-Z0-9 ]+)\\s*";
            matcher = getCommnadMatcherInput(input, regex);
            if (matcher.matches()) {
                removePlaylist(matcher);
                isInvalidCommand = false;
                continue outerloop;
            }

            //follow other users:
            regex = "follow user -u (?<username>[a-zA-Z0-9]+)\\s*";
            matcher = getCommnadMatcherInput(input, regex);
            if (matcher.matches()) {
                followUser(matcher);
                isInvalidCommand = false;
                continue outerloop;
            }

            //follow artist:
            regex = "follow artist -u (?<username>[a-zA-Z0-9]+)\\s*";
            matcher = getCommnadMatcherInput(input, regex);
            if (matcher.matches()) {
                followArtist(matcher);
                isInvalidCommand = false;
                continue outerloop;
            }

            //unfollow other users:
            regex = "unfollow user -u (?<username>[a-zA-Z0-9]+)\\s*";
            matcher = getCommnadMatcherInput(input, regex);
            if (matcher.matches()) {
                unfollowUser(matcher);
                isInvalidCommand = false;
                continue outerloop;
            }

            //unfollow artist
            regex = "unfollow artist -u (?<username>[a-zA-Z0-9]+)\\s*";
            matcher = getCommnadMatcherInput(input, regex);
            if (matcher.matches()) {
                unfollowArtist(matcher);
                isInvalidCommand = false;
                continue outerloop;
            }

            //going to playlist menu:
            regex = "go to playlist menu -p (?<playlistName>[a-zA-Z0-9 ]+)\\s*";
            matcher = getCommnadMatcherInput(input, regex);
            if (matcher.matches()) {
                goToPlaylistMenu(scanner, matcher);
                isInvalidCommand = false;
                continue outerloop;
            }

            //show track info:
            regex = "show track info -t (?<trackname>[a-zA-Z0-9 ]+)\\s*";
            matcher = getCommnadMatcherInput(input, regex);
            if (matcher.matches()) {
                showTrackInfo(matcher);
                isInvalidCommand = false;
                continue outerloop;
            }

            //show playlist info:
            regex = "show playlist info -p (?<playlistName>[a-zA-Z0-9 ]+)\\s*";
            matcher = getCommnadMatcherInput(input, regex);
            if (matcher.matches()) {
                showPlaylistInfo(matcher);
                isInvalidCommand = false;
                continue outerloop;
            }

            //show artist info:
            regex = "show artist info -u (?<username>[a-zA-Z0-9]+)\\s*";
            matcher = getCommnadMatcherInput(input, regex);
            if (matcher.matches()) {
                showArtistInfo(matcher);
                isInvalidCommand = false;
                continue outerloop;
            }

            //show user info:
            regex = "show user info -u (?<username>[a-zA-Z0-9]+)\\s*";
            matcher = getCommnadMatcherInput(input, regex);
            if (matcher.matches()) {
                showUserInfo(matcher);
                isInvalidCommand = false;
                continue outerloop;
            }

            //back:
            regex = "back\\s*";
            matcher = getCommnadMatcherInput(input, regex);
            if (matcher.matches()) {
                isInvalidCommand = false;
                break outerloop;
            }

            //show menu name
            regex = "show menu name\\s*";
            matcher = getCommnadMatcherInput(input, regex);
            if (matcher.matches()) {
                System.out.println("user menu");
                isInvalidCommand = false;
                continue outerloop;
            }

            if (isInvalidCommand) {
                System.out.println("invalid command");
            }
        }
    }
    
    private static Matcher getCommnadMatcherInput(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher;

    }
    
    private static void addTrackToQueue(Matcher matcher) {
        String trackname = matcher.group("trackname");
        Track track = Track.getTrackByName(trackname);
        User user = User.getLoggedInUser();
        user.addToQueue(track);
    }

    private static void addTrackToLikedTracks(Matcher matcher) {
        String trackname = matcher.group("trackname");
        Track track = Track.getTrackByName(trackname);
        if (track == null) {
            System.out.println("no such track");
            return;
        }
        else {
            boolean trackAddedBefore = false;
            User user = User.getLoggedInUser();
            ArrayList<Track> likedTracks = user.getLikedTracks();
            for (Track track2 : likedTracks) {
                if (track2.getName().equals(trackname)) {
                    trackAddedBefore = true;
                }
            }
            if (trackAddedBefore) {
                System.out.println("track is already liked");
                return;
            } else {
                user.addLikedTrack(track);
                System.out.println("liked track successfully");
                return;
            }
        } 
    }
    
    private static void removeFromQueue(Matcher matcher) {
        String trackname = matcher.group("trackname");
        User user = User.getLoggedInUser();
        Track track = null;
        for (Track track2 : user.getQueue()) {
            if (track2.getName().equals(trackname)) {
                track = track2;
            }
        }
        if (track == null) {
            System.out.println("no such track in queue");
            return;
        }
        user.removeFromQueue(track);
    }
    
    private static void removeTrackFromLikedTracks(Matcher matcher) {
        String trackname = matcher.group("trackname");
        User user = User.getLoggedInUser();
        Track track = null;
        for (Track track2 : user.getLikedTracks()) {
            if (track2.getName().equals(trackname)) {
                track = track2;
            }
        }
        if (track == null) {
            System.out.println("no such track in liked tracks");
            return;
        }
        
        user.removeLikedTrack(track);
    }
    
    private static void showPlaylists() {
        User user = User.getLoggedInUser();
        ArrayList<Playlist> playlists = user.getPlaylists();
        String[] orderedPlaylist = new String[200];
        int index = 0;
        for (Playlist playlist : playlists) {
            orderedPlaylist[index] = playlist.getName();
            index++;
        }
        String[] newOrderedList = new String[index];
        for (int i = 0; i < index; i++) {
            newOrderedList[i] = orderedPlaylist[i];
        }
        Arrays.sort(newOrderedList);
        for (int i = 0; i < index; i++) {
            System.out.println(newOrderedList[i]);
        }
    }
    
    private static void showLikedTracks() {
        User user = User.getLoggedInUser();
        ArrayList<Track> likedTracks = user.getLikedTracks();
        String[] orderedTracks = new String[200];
        int index = 0;
        for (Track track : likedTracks) {
            orderedTracks[index] = track.getName();
            index++;
        }
        String[] newOrderedTracks = new String[index];
        for (int i = 0; i < index; i++) {
            newOrderedTracks[i] = orderedTracks[i];
        }
        Arrays.sort(newOrderedTracks);
        for (int i = 0; i < index; i++) {
            System.out.println(newOrderedTracks[i]);
        }
    }
    
    private static void showUserInfo(Matcher matcher) {
        String username = matcher.group("username");
        User user = User.getUserByUsername(username);
        if (user == null) {
            System.out.println("no such user");
            return;
        }
        System.out.println(user.getUsername() + " " + user.getFollowers() + " " + user.getFollowings() + " " + user.getPlaylists().size());

    }
    
    private static void showTrackInfo(Matcher matcher) {
        String trackName = matcher.group("trackname");
        Track track = Track.getTrackByName(trackName);
        if (track == null) {
            System.out.println("no such track");
            return;
        }
        String type = "";
        if (track.getType() == false) {
            type = "song";
        }
        else {
            type = "podcast";
        }
        System.out.print(track.getName() + " " + type + " " + track.getDuration() + " ");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = sdf.format(track.getReleaseDate());
        System.out.print(formattedDate + " ");
        Artist artist = track.getArtist();
        System.out.println(artist.getNickname());
    }
    
    private static void showQueue() {
        User user = User.getLoggedInUser();
        ArrayList<Track> queue = user.getQueue();
        for (Track track : queue) {
            System.out.println(track.getName());
        }
    }
    
    private static void showArtistInfo(Matcher matcher) {
        String username = matcher.group("username");
        Artist artist = Artist.getArtistByUsername(username);
        if (artist == null) {
            System.out.println("no such artist ");
            return;
        }
        System.out.println(artist.getUsername() + " " + artist.getNickname() + " " + artist.getFollowers() + " " + artist.getRank());
    }

    private static void showPlaylistInfo(Matcher matcher) {
        String playlistName = matcher.group("playlistName");
        Playlist playlist = Playlist.getPlaylistByName(playlistName);
        if (playlist == null) {
            System.out.println("no such playlist");
            return;
        }
        System.out.print(playlistName + " " + playlist.getOwner().getUsername() + " ");
        int totalTime = 0;
        for (Track track : playlist.getTracks()) {
            totalTime += track.getDuration();
        }
        System.out.println(totalTime);
    }
    
    private static void followUser(Matcher matcher) {
        String username = matcher.group("username");
        User user = User.getLoggedInUser();
        if (user.getUsername().equals(username)) {
            System.out.println("you can't follow yourself");
            return;
        }
        boolean anyoneWithThisName = false;
        for (User user2 : User.getUsers()) {
            if (user2.getUsername().equals(username)) {
                anyoneWithThisName = true;
            }
        }
        if (anyoneWithThisName) {
            System.out.println("added user to followings");
            user.setFollowings(user.getFollowings() + 1);
            User user2 = User.getUserByUsername(username);
            user2.setFollowers(user2.getFollowers() + 1);
        }
        else {
            System.out.println("no such user");
            return;
        }
    }

    private static void unfollowUser(Matcher matcher) {
        String username = matcher.group("username");
        User user = User.getLoggedInUser();
        if (user.getUsername().equals(username)) {
            System.out.println("you can't unfollow yourself");
            return;
        }
        boolean anyoneWithThisName = false;
        for (User user2 : User.getUsers()) {
            if (user2.getUsername().equals(username)) {
                anyoneWithThisName = true;
            }
        }
        if (anyoneWithThisName) {
            System.out.println("user unfollowed successfully");
            user.setFollowings(user.getFollowings() - 1);
            User user2 = User.getUserByUsername(username);
            user2.setFollowers(user2.getFollowers() - 1);
        }
        else {
            System.out.println("no such user");
            return;
        }
    }

    private static void followArtist(Matcher matcher) {
        String username = matcher.group("username");
        Artist artist = Artist.getArtistByUsername(username);
        if (artist == null) {
            System.out.println("no such artist");
            return;
        }
        System.out.println("added artist to followings");
        User user = User.getLoggedInUser();
        user.setFollowings(user.getFollowings() + 1);
        artist.setFollowers(artist.getFollowers() + 1);
    }
    
    private static void unfollowArtist(Matcher matcher) {
        String username = matcher.group("username");
        Artist artist = Artist.getArtistByUsername(username);
        if (artist == null) {
            System.out.println("no such artist");
            return;
        }
        System.out.println("artist unfollowed successfully");
        User user = User.getLoggedInUser();
        user.setFollowings(user.getFollowings() - 1);
        artist.setFollowers(artist.getFollowers()-1);
    }
    
    private static void createPlaylist(Matcher matcher) {
        String playlistName = matcher.group("playlistName");
        Playlist playlist = Playlist.getPlaylistByName(playlistName);
        User user = User.getLoggedInUser();
        if (playlist == null) {
            Playlist newPlaylist = new Playlist(playlistName, user);
            Playlist.addPlaylist(newPlaylist);
            user.addPlaylist(newPlaylist);
            System.out.println("playlist created successfully");
        }
        else {
            System.out.println("playlist name already exists");
            return;
        }
    }
    
    private static void removePlaylist(Matcher matcher) {
        User user = User.getLoggedInUser();
        String playListName = matcher.group("playlistName");
        boolean doWeHaveThisPlaylist = false;
        for (Playlist playlist : user.getPlaylists()) {
            if (playlist.getName().equals(playListName)) {
                doWeHaveThisPlaylist = true;
            }
        }
        if (doWeHaveThisPlaylist) {
            Playlist playlist = Playlist.getPlaylistByName(playListName);
            user.removePlaylist(playlist);
            Playlist.removePlaylist(playlist);
            System.out.println("playlist deleted successfully");
            return;
        }
        else {
            System.out.println("user doesn't own such playlist");
            return;
        }
    }
    
    private static void goToPlaylistMenu(Scanner scanner, Matcher matcher) {
        String playlistName = matcher.group("playlistName");
        Playlist playlist = Playlist.getPlaylistByName(playlistName);
        if (playlist == null) {
            System.out.println("no such playlist");
            return;
        }
        Playlist.setCurrentPlaylist(playlist);
        System.out.println("entered playlist menu successfully");
        PlaylistMenu.run(scanner);
    }
    
    private static void reverseOrderOfQueue(Scanner scanner, Matcher matcher) {
        int start = Integer.parseInt(matcher.group("start"));
        int end = Integer.parseInt(matcher.group("end"));
        User user = User.getLoggedInUser();
        ArrayList<Track> oldQueue = user.getQueue();
        if (oldQueue.isEmpty()) {
            System.out.println("queue is empty");
            return;
        }
        if (start >= end) {
            System.out.println("invalid bounds");
            return;
        }
        if (end > oldQueue.size()) {
            System.out.println("invalid bounds");
            return;
        }
        if (start < 1) {
            System.out.println("invalid bounds");
            return;
        }
        ArrayList<Track> newQueue = new ArrayList<>();
        for (int i = 0; i < start - 1; i++) {
            newQueue.add(oldQueue.get(i));
        }
        for (int i = end - 1; i >= start - 1; i--) {
            newQueue.add(oldQueue.get(i));
        }
        for (int i = end; i < oldQueue.size(); i++) {
            newQueue.add(oldQueue.get(i));
        }
        user.setQueue(newQueue);
        System.out.println("order of queue reversed successfully");
    }

}
