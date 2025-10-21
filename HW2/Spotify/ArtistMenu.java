import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArtistMenu {

    public static void run(Scanner scanner) {
        outerloop:
        while (true) {
            String input = scanner.nextLine();
            String regex = "";
            Matcher matcher;

            //show tracks:
            regex = "show tracks\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                showTracks();
                continue outerloop;
            }

            //show songs:
            regex = "show songs\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                showSongs();
                continue outerloop;
            }

            //show podcasts:
            regex = "show podcasts\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                showPodcasts();
                continue outerloop;
            }

            //releasing tracks:
            regex = "release -n (?<trackname>[a-zA-Z0-9 ]+) -t (?<type>song|podcast) -d (?<duration>\\d+) -r ((?<year>\\d{4})/(?<month>\\d{2})/(?<day>\\d{2}))\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                releaseTracks(matcher);
                continue outerloop;
            }

            //how many followers:
            regex = "num of followers\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                getNumberOfFollowers();
                continue outerloop;
            }

            //artist rank:
            regex = "get rank\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                getRank();
                continue outerloop;
            }

            //back:
            regex = "back\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                break outerloop;
            }

            //show menu name
            regex = "show menu name\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                System.out.println("artist menu");
                continue outerloop;
            }

            System.out.println("invalid command");
        }
    }
    
    private static Matcher getCommandMatcher(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }

    private static void showTracks() {
        Artist artist = Artist.getLoggedInArtist();
        Track[] artistTracks = new Track[artist.getTracks().size()];
        int index = 0;
        for (Track track : artist.getTracks()) {
            artistTracks[index] = track;
            index++;
        }
        for (int i = 0; i < artistTracks.length; i++) {
            for (int j = i; j < artistTracks.length; j++) {
                Date date1 = artistTracks[i].getReleaseDate();
                Date date2 = artistTracks[j].getReleaseDate();
                int comparison = date1.compareTo(date2);
                if (comparison < 0) {
                    Track temp = artistTracks[i];
                    artistTracks[i] = artistTracks[j];
                    artistTracks[j] = temp;
                }
                else if (comparison == 0) {
                    String name1 = artistTracks[i].getName();
                    String name2 = artistTracks[j].getName();
                    int result = name1.compareTo(name2);
                    if (result > 0) {
                        Track temp = artistTracks[i];
                        artistTracks[i] = artistTracks[j];
                        artistTracks[j] = temp;
                    }
                }
            }
        }
        for (int i = 0; i < artistTracks.length; i++) {
            System.out.println(artistTracks[i].getName());
        }
    }
    
    private static void showSongs() {
        Artist artist = Artist.getLoggedInArtist();
        ArrayList<Track> artistTracks = artist.getTracks();
        int howManySongs = 0;
        for (Track track : artistTracks) {
            if (track.getType() == false) {
                howManySongs++;
            }
        }
        Track songs[] = new Track[howManySongs];
        int index = 0;
        for (Track track : artistTracks) {
            if (track.getType() == false) {
                songs[index] = track;
                index++;
            }
        }
        for (int i = 0; i < songs.length; i++) {
            for (int j = i; j < songs.length; j++) {
                Date date1 = songs[i].getReleaseDate();
                Date date2 = songs[j].getReleaseDate();
                int comparison = date1.compareTo(date2);
                if (comparison < 0) {
                    Track temp = songs[i];
                    songs[i] = songs[j];
                    songs[j] = temp;
                }
                else if (comparison == 0) {
                    String name1 = songs[i].getName();
                    String name2 = songs[j].getName();
                    int result = name1.compareTo(name2);
                    if (result > 0) {
                        Track temp = songs[i];
                        songs[i] = songs[j];
                        songs[j] = temp;
                    }
                }
            }
        }
        for (int i = 0; i < songs.length; i++) {
            System.out.println(songs[i].getName());
        }
    }
    
    private static void showPodcasts() {
        Artist artist = Artist.getLoggedInArtist();
        ArrayList<Track> artistTracks = artist.getTracks();
        int howManySongs = 0;
        for (Track track : artistTracks) {
            if (track.getType() == true) {
                howManySongs++;
            }
        }
        Track songs[] = new Track[howManySongs];
        int index = 0;
        for (Track track : artistTracks) {
            if (track.getType() == true) {
                songs[index] = track;
                index++;
            }
        }
        for (int i = 0; i < songs.length; i++) {
            for (int j = i; j < songs.length; j++) {
                Date date1 = songs[i].getReleaseDate();
                Date date2 = songs[j].getReleaseDate();
                int comparison = date1.compareTo(date2);
                if (comparison < 0) {
                    Track temp = songs[i];
                    songs[i] = songs[j];
                    songs[j] = temp;
                }
                else if (comparison == 0) {
                    String name1 = songs[i].getName();
                    String name2 = songs[j].getName();
                    int result = name1.compareTo(name2);
                    if (result > 0) {
                        Track temp = songs[i];
                        songs[i] = songs[j];
                        songs[j] = temp;
                    }
                }
            }
        }
        for (int i = 0; i < songs.length; i++) {
            System.out.println(songs[i].getName());
        }
    }
    
    private static void releaseTracks(Matcher matcher) {
        String trackName = matcher.group("trackname");
        boolean type;
        if (matcher.group("type").equals("song"))
            type = false;
        else
            type = true;
        int duration = Integer.parseInt(matcher.group("duration"));
        int year = Integer.parseInt(matcher.group("year"));
        int month = Integer.parseInt(matcher.group("month"));
        int day = Integer.parseInt(matcher.group("day"));
        String date = year + "/" + month + "/" + day;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date realDate = null;
        try {
            java.util.Date utilDate = sdf.parse(date);
            long timeInMillis = utilDate.getTime();
            realDate = new Date(timeInMillis);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        Track track = Track.getTrackByName(trackName);
        if (track == null) {
            System.out.println("track released successfully");
            track = new Track(realDate, Artist.getLoggedInArtist(), type, duration, trackName);
            Track.addTrack(track);
            Artist.getLoggedInArtist().addToTracks(track);
        }
        else {
            System.out.println("track name already exists");
            return;
        }
    }

    private static void getRank() {
        Artist artist = Artist.getLoggedInArtist();
        System.out.println(artist.getRank());
    }
    
    private static void getNumberOfFollowers() {
        Artist artist = Artist.getLoggedInArtist();
        System.out.println(artist.getFollowers());
    }
    
}