import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.reflect.Array;
import java.sql.Date;

public class PlaylistMenu {

    public static void run(Scanner scanner) {
        outerloop:
        while (true) {
            String regex = "";
            Matcher matcher;
            String input = scanner.nextLine();
            boolean isInvalidCommand = true;

            //show tracks:
            regex = "show tracks\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                showTracks();
                isInvalidCommand = false;
                continue outerloop;
            }

            //show duration:
            regex = "show duration\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                showDuration();
                isInvalidCommand = false;
                continue outerloop;
            }

            //add track:
            regex = "add -t (?<trackname>[a-zA-Z0-9 ]+)\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                addTrack(matcher);
                isInvalidCommand = false;
                continue outerloop;
            }

            //remove track:
            regex = "remove -t (?<trackname>[a-zA-Z0-9 ]+)\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                removeTrack(matcher);
                isInvalidCommand = false;
                continue outerloop;
            }

            //back:
            regex = "back\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                isInvalidCommand = false;
                break outerloop;
            }

            //show menu name
            regex = "show menu name\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                System.out.println("playlist menu");
                isInvalidCommand = false;
                continue outerloop;
            }

            if (isInvalidCommand) {
                System.out.println("invalid command");
            }
        }
    }
    
    private static Matcher getCommandMatcher(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }
    
    private static void showTracks() {
        Playlist playlist = Playlist.getCurrentPlaylist();
        int index = playlist.getTracks().size();
        Track[] tracks = new Track[index];
        int whichIndex = 0;
        for (Track track : playlist.getTracks()) {
            tracks[whichIndex] = track;
            whichIndex++;
        }
        for (int i = 0; i < index; i++) {
            for (int j = i; j < index; j++) {
                Date date1 = tracks[i].getReleaseDate();
                Date date2 = tracks[j].getReleaseDate();
                int comparison = date1.compareTo(date2);
                if (comparison < 0) {
                    Track temp = tracks[i];
                    tracks[i] = tracks[j];
                    tracks[j] = temp;
                }
                else if (comparison == 0) {
                    String name1 = tracks[i].getName();
                    String name2 = tracks[j].getName();
                    int result = name1.compareTo(name2);
                    if (result > 0) {
                        Track temp = tracks[i];
                        tracks[i] = tracks[j];
                        tracks[j] = temp;
                    }
                }
            }
        }
        for (Track track : tracks) {
            System.out.println(track.getName());
        }
    }
    
    private static void showDuration() {
        Playlist playlist = Playlist.getCurrentPlaylist();
        ArrayList<Track> tracks = playlist.getTracks();
        int totalDuration = 0;
        for (Track track : tracks) {
            totalDuration += track.getDuration();
        }
        System.out.println(totalDuration);
    }
    
    private static void addTrack(Matcher matcher) {
        String trackname = matcher.group("trackname");
        User user = User.getLoggedInUser();
        Playlist playlist = Playlist.getCurrentPlaylist();
        boolean owningPlaylistByUserError = true;
        if (playlist.getOwner().getUsername().equals(user.getUsername())) {
            owningPlaylistByUserError = false;
        }
        boolean trackDoesNotExistError = true;
        for (Track track : Track.getTracks()) {
            if (track.getName().equals(trackname)) {
                trackDoesNotExistError = false;
            }
        }
        boolean trackAlreadyAddedError = false;
        for (Track track : playlist.getTracks()) {
            if (track.getName().equals(trackname)) {
                trackAlreadyAddedError = true;
            }
        }
        if (owningPlaylistByUserError == true) {
            System.out.println("user doesn't own this playlist");
            return;
        }
        if (trackDoesNotExistError == true) {
            System.out.println("no such track");
            return;
        }
        if (trackAlreadyAddedError) {
            System.out.println("track is already in the playlist");
            return;
        }
        System.out.println("track added to playlist successfully");
        Track track = Track.getTrackByName(trackname);
        playlist.addTrack(track);
    }
    
    private static void removeTrack(Matcher matcher) {
        String trackname = matcher.group("trackname");
        Playlist playlist = Playlist.getCurrentPlaylist();
        ArrayList<Track> tracks = playlist.getTracks();
        boolean doWeHaveThisTrack = false;
        for (Track track : tracks) {
            if (track.getName().equals(trackname)) {
                doWeHaveThisTrack = true;
            }
        }
        User user = User.getLoggedInUser();
        boolean owningThisPlaylist = false;
        if (playlist.getOwner().getUsername().equals(user.getUsername())) {
            owningThisPlaylist = true;
        }
        if (!owningThisPlaylist) {
            System.out.println("user doesn't own this playlist");
            return;
        }
        if (doWeHaveThisTrack) {
            Track track = Track.getTrackByName(trackname);
            playlist.removeTrack(track);
            System.out.println("track removed from playlist successfully");
        }
        else {
            System.out.println("no such track in playlist");
            return;
        }
    }

}
