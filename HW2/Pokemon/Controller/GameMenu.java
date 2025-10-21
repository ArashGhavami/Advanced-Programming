package Controller;

import Model.Card;
import Model.User;

import java.util.Scanner;

public class GameMenu {
    public static void run(Scanner scanner, User first, User second) {
        first.setHand(first.getDeck());
        second.setHand(second.getDeck());
        int round = 1;

        while (true) {

            EachRound.playerTurn(scanner, first, second, round);
            endRound(first, second);
            endRound(second, first);
            if (first.getActive() == null) {
                System.out.println("Winner: " + second.getUsername());
                break;
            }
            System.out.println(second.getUsername() + "'s turn");
            EachRound.playerTurn(scanner, second, first, round);
            endRound(second,first);
            endRound(first,second);
            if (second.getActive() == null) {
                System.out.println("Winner: " + first.getUsername());
                break;
            }
            System.out.println(first.getUsername() + "'s turn");

            round++;
        }
        setCoins(first, second);
        setCoins(second, first);
        setExperience(first,second);
        setExperience(second,first);

        endgame(first);
        endgame(second);

    }

    public static void setCoins(User first, User second) {
        double totalHitpointNow = 0;
        Card card = second.getActive();
        if (card != null)
            totalHitpointNow += card.getHitpoint();
        for (Card card1 : second.getBench()) {
            if (card1 != null)
                totalHitpointNow += card1.getHitpoint();
        }
        for (Card card1 : second.getHand()) {
            if (card1 != null) {
                totalHitpointNow += card1.getHitpoint();
            }
        }
        double reduce = second.getFirstHandTotalHitpoints() - totalHitpointNow;
        first.setCoin(first.getNumberOfCoins() + reduce / 10);
    }

    public static void setExperience(User first, User second){
        int kills = 12;
        kills -= first.getDeck().size();
        if(first.getActive() != null){
            kills -= 1;
            if(first.getActive().getEnergy2Name()!= null){
                kills -=1;
            }
            if(first.getActive().getEnergy1Name()!= null){
                kills -=1;
            }
        }
        Card[] benchs = first.getBench();
        for(int i=0; i<3; i++){
            if(benchs[i] != null){
                kills--;
                if(benchs[i].getEnergy1Name()!= null){
                    kills--;
                }
                if(benchs[i].getEnergy2Name()!= null){
                    kills--;
                }
            }
        }
        second.setExperience(second.getExperience() + kills*10);
    }

    public static void endgame(User user) {
        user.setGetKilled(0);
        user.setKills(0);
        user.setActive(null);
        user.setBench(null, 0);
        user.setBench(null, 1);
        user.setBench(null, 2);
        user.setHand(null);
        user.setFirstHandTotalHitpoints(0);
    }

    public static void endRound(User user, User rival) {
        if (user.getActive() != null) {
            if (user.getActive().getHitpoint() <= 0) {
                rival.setKills(rival.getKills() + 1);
                if(user.getActive().getEnergy1Name() != null){
                    rival.setKills(rival.getKills() + 1);
                }
                if(user.getActive().getEnergy2Name() != null){
                    rival.setKills(rival.getKills() + 1);
                }
                user.setActive(null);
            }
        }
        Card[] bench = user.getBench();
        if (bench != null) {
            int kills = 0;
            for (int i = 0; i < 3; i++) {
                if (bench[i] != null) {
                    if (bench[i].getHitpoint() <= 0) {
                        kills++;
                        if(bench[i].getEnergy1Name() != null){
                            kills++;
                        }
                        if(bench[i].getEnergy2Name() != null){
                            kills++;
                        }
                        bench[i] = null;
                    }
                }
            }
            rival.setKills(rival.getKills()  + kills);
        }
    }
}


