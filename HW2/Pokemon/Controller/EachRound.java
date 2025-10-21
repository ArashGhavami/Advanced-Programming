package Controller;

import Model.Card;
import Model.User;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EachRound {

    private static boolean didIPutEnergy;

    {
        didIPutEnergy = false;
    }


    public static void playerTurn(Scanner scanner, User player, User rival, int round) {
        String regex = "";
        String input = "";
        Matcher matcher = null;

        if (player.getActive() != null) {
            if (player.getActive().getType().equals("plant")) {
                player.getActive().setShieldHitpoint(15);
            }
        }

        Card[] bench1 = player.getBench();
        for (int i = 0; i < 3; i++) {
            if (bench1[i] != null) {
                if (bench1[i].getType().equals("plant")) {
                    bench1[i].setShieldHitpoint(15);
                }
            }
        }
        player.setBenchFull(bench1);

        while (true) {
            input = scanner.nextLine();

            //show table:
            regex = "\\s*show table\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                showTable(player, rival, round);
                continue;
            }

            //show place info:
            regex = "\\s*show my info (?<num>\\S+)\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                showPlaceInfo(matcher, player);
                continue;
            }
            regex = "\\s*show enemy info (?<num>\\S+)\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                showPlaceInfo(matcher, rival);
                continue;
            }

            //put card from hand:
            regex = "\\s*put card (?<cardname>\\S+) to (?<placenumber>\\S+)\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                putCard(matcher, player);
                continue;
            }

            //replace card:
            regex = "\\s*substitute active card with bench (?<num>\\S+)";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                replaceCard(matcher, player);
                continue;
            }

            //end turn:
            regex = "\\s*end turn\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                endTurn(player);

                didIPutEnergy = false;
                break;
            }


            //show current menu:
            regex = "\\s*show current menu\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                System.out.println("game menu");
                continue;
            }


            //execute action:
            regex = "\\s*execute action(?<t> -t (?<target>\\S+))?\\s*";
            matcher = getCommandMatcher(input, regex);
            if (matcher.matches()) {
                if (executeAction(player, rival, matcher)) {
                    didIPutEnergy = false;
                    break;
                } else continue;
            }



            //invalid:
            System.out.println("invalid command");
            continue;

        }


    }

    public static Matcher getCommandMatcher(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }

    public static void showTable(User first, User second, int round) {
        System.out.println("round " + round);
        System.out.println("your active card:");
        Card.printCardEnergiesWithActive(first.getActive());
        System.out.println();
        System.out.println("your hand:");
        for (int i = 0; i < first.getHand().size(); i++) {
            System.out.println((i + 1) + "." + first.getHand().get(i).getName());
        }
        System.out.println();
        System.out.println("your bench:");
        Card[] firstBench = first.getBench();
        for (int i = 0; i < 3; i++) {
            System.out.print((i + 1) + ".");
            Card.printCardEnergiesWithActive(firstBench[i]);
        }
        System.out.println();
        System.out.println(second.getUsername() + "'s active card:");
        Card.printCardEnergiesWithActive(second.getActive());
        System.out.println();
        System.out.println(second.getUsername() + "'s bench:");
        Card[] secondBench = second.getBench();
        for (int i = 0; i < 3; i++) {
            System.out.print((i + 1) + ".");
            Card.printCardEnergiesWithActive(secondBench[i]);
        }

    }

    public static void showPlaceInfo(Matcher matcher, User player) {
        int number = Integer.parseInt(matcher.group("num"));
        if (number > 3 || number < 0) {
            System.out.println("invalid place number");
            return;
        }
        if (number == 0) {
            Card card = player.getActive();
            if (card == null) {
                System.out.println("no pokemon in the selected place");
                return;
            } else {
                Card.printCardData(card);
                return;
            }
        } else {
            Card[] bench = player.getBench();
            number--;
            if (bench[number] == null) {
                System.out.println("no pokemon in the selected place");
                return;
            } else {
                Card.printCardData(bench[number]);
                return;
            }

        }
    }

    public static void putCard(Matcher matcher, User player) {
        String cardname = matcher.group("cardname");
        int placenum = Integer.parseInt(matcher.group("placenumber"));
        if (!Card.isThisNameValid(cardname)) {
            System.out.println("card name is invalid");
            return;
        }
        ArrayList<Card> hand = player.getHand();
        boolean doWeHaveTheCard = false;
        Card relatedCard = null;
        for (Card card : hand) {
            if (card.getName().equals(cardname)) {
                doWeHaveTheCard = true;
                relatedCard = card;
                break;
            }
        }
        if (!doWeHaveTheCard) {
            System.out.println("you don't have the selected card");
            return;
        }
        if (placenum > 3 || placenum < 0) {
            System.out.println("invalid place number");
            return;
        }
        if (relatedCard.getType().equals("fire") || relatedCard.getType().equals("water") || relatedCard.getType().equals("plant")) {
            if (placenum == 0) {
                Card card = player.getActive();
                if (card != null) {
                    System.out.println("a pokemon already exists there");
                    return;
                }
            } else {
                Card[] bench = player.getBench();
                if (bench[placenum - 1] != null) {
                    System.out.println("a pokemon already exists there");
                    return;
                }
            }
        }
        if (relatedCard.getType().equals("energy")) {
            if (placenum == 0) {
                Card card = player.getActive();
                if (card == null) {
                    System.out.println("no pokemon in the selected place");
                    return;
                } else {
                    if (card.getEnergy1Name() != null && card.getEnergy2Name() != null) {
                        System.out.println("pokemon already has 2 energies");
                        return;
                    }
                }
            } else {
                Card[] bench = player.getBench();
                if (bench[placenum - 1] == null) {
                    System.out.println("no pokemon in the selected place");
                    return;
                } else {
                    if (bench[placenum - 1].getEnergy2Name() != null && bench[placenum - 1].getEnergy1Name() != null) {
                        System.out.println("pokemon already has 2 energies");
                        return;
                    }
                }
            }
            if (didIPutEnergy) {
                System.out.println("you have already played an energy card in this turn");
                return;
            }
        }


        System.out.println("card put successful");

        if (relatedCard.getType().equals("fire") || relatedCard.getType().equals("water") || relatedCard.getType().equals("plant")) {
            if (placenum == 0) {
                player.setActive(relatedCard);
            } else {
                player.setBench(relatedCard, placenum - 1);
            }
        }
        if (relatedCard.getType().equals("energy")) {
            if (placenum == 0) {
                Card card = player.getActive();
                if (card.getEnergy1Name() == null) {
                    card.setEnergy1(Card.getEnergyByEnergyCardAndMainCard(relatedCard.getName(), card.getType()));
                    card.setEnergy1Name(relatedCard.getName());
                } else {
                    card.setEnergy2(Card.getEnergyByEnergyCardAndMainCard(relatedCard.getName(), card.getType()));
                    card.setEnergy2Name(relatedCard.getName());
                }
                player.setActive(card);
            } else {
                Card[] bench = player.getBench();
                Card card = bench[placenum - 1];
                if (card.getEnergy1Name() == null) {
                    card.setEnergy1(Card.getEnergyByEnergyCardAndMainCard(relatedCard.getName(), card.getType()));
                    card.setEnergy1Name(relatedCard.getName());
                } else {
                    card.setEnergy2(Card.getEnergyByEnergyCardAndMainCard(relatedCard.getName(), card.getType()));
                    card.setEnergy2Name(relatedCard.getName());
                }
                player.setBench(card, placenum - 1);
            }
            didIPutEnergy = true;
        }
        ArrayList<Card> newHand = player.getHand();
        newHand.remove(relatedCard);
        player.setHand(newHand);
        player.removeFromAllCards(relatedCard);
        ArrayList<Card> mainCards = User.getLoggedInUser().getCards();
        for (Card card : mainCards) {
            if (card.getName().equals(cardname)) {
                mainCards.remove(card);
                break;
            }
        }
        User.getLoggedInUser().setCards(mainCards);
    }


    public static void replaceCard(Matcher matcher, User player) {
        int benchNumber = Integer.parseInt(matcher.group("num"));
        if (benchNumber > 3 || benchNumber < 1) {
            System.out.println("invalid bench number");
            return;
        }
        Card[] bench = player.getBench();
        Card active = player.getActive();
        if (bench[benchNumber - 1] == null) {
            System.out.println("no pokemon in the selected place");
            return;
        }
        if (active != null) {
            if (active.isItSlept()) {
                System.out.println("active pokemon is sleeping");
                return;
            }
        }
        System.out.println("substitution successful");
        Card activeTemp = active;
        active = bench[benchNumber - 1];
        bench[benchNumber - 1] = activeTemp;
        player.setBench(bench[benchNumber - 1], benchNumber - 1);
        player.setActive(active);
    }

    public static boolean executeAction(User first, User second, Matcher matcher) {
        if (first.getActive() == null) {
            System.out.println("no active pokemon");
            return false;
        }
        if (first.getActive().getName().equals("ducklett") || first.getActive().getName().equals("rowlet")) {
            String t = matcher.group("t");
            if (t == null) {
                System.out.println("invalid action");
                return false;
            }
            int target = 0;
            target = Integer.parseInt(matcher.group("target"));
            if (first.getActive().getName().equals("ducklett")) {
                if (target > 3 || target < 0) {
                    System.out.println("invalid target number");
                    return false;
                }
            } else {
                if (target > 3 || target < 1) {
                    System.out.println("invalid target number");
                    return false;
                }
            }

            if (target == 0) {
                Card card = null;
                if (first.getActive().getName().equals("ducklett"))
                    card = second.getActive();
                else
                    card = first.getActive();
                if (card == null) {
                    System.out.println("no pokemon in the selected place");
                    return false;
                }
            } else {
                Card[] bench = null;
                if (first.getActive().getName().equals("ducklett"))
                    bench = second.getBench();
                else
                    bench = first.getBench();

                if (bench[target - 1] == null) {
                    System.out.println("no pokemon in the selected place");
                    return false;
                }
            }
        } else {
            String t = matcher.group("t");
            if (t != null) {
                System.out.println("invalid action");
                return false;
            }
            if (second.getActive() == null) {
                System.out.println("no pokemon in the selected place");
                return false;
            }
        }


        if (first.getActive().isItSlept()) {
            System.out.println("active pokemon is sleeping");
            return false;
        }
        Card[] bench = first.getBench();


        System.out.println("action executed successfully");
        if (first.getActive().getName().equals("dragonite")) {
            dragoniteAction(first, second);
        }
        if (first.getActive().getName().equals("tepig")) {
            tepigAction(first, second);
        }
        if (first.getActive().getName().equals("lugia")) {
            lugiaAction(first, second);
        }
        if (first.getActive().getName().equals("ducklett")) {
            ducklettAction(first, second, matcher);
        }
        if (first.getActive().getName().equals("pineco")) {
            pinecoAction(first, second);
        }
        if (first.getActive().getName().equals("rowlet")) {
            rowletAction(first, second, matcher);
        }
        Card card = first.getActive();
        if (card != null) {
            if (card.getHitpoint() > card.getMaxHitpoint()) {
                card.setHitpoint(card.getMaxHitpoint());
                first.setActive(card);
            }
            if (card.getHitpoint() <= 0) {
                first.setActive(null);
                first.setGetKilled(first.getGetKilled() + 1);
            }
            if (card.getType().equals("plant")) {
                card.setShieldHitpoint(15);
            }
        }
        first.setActive(card);
        bench = first.getBench();
        for (int i = 0; i < 3; i++) {
            if (bench != null) {
                if (bench[i] != null) {
                    if (bench[i].getHitpoint() > bench[i].getMaxHitpoint()) {
                        bench[i].setHitpoint(bench[i].getMaxHitpoint());
                    }
                    if (bench[i].getHitpoint() <= 0) {
                        bench[i] = null;
                        first.setGetKilled(first.getGetKilled() + 1);
                    }
                    if (bench[i] != null) {
                        if (bench[i].getType().equals("plant")) {
                            bench[i].setShieldHitpoint(15);
                        }
                    }
                }
            }
        }
        first.setBenchFull(bench);
        endRound(first);
        endRound(second);

        int kill = 0;
        if (card != null) {
            if (card.getHitpoint() < 0) {
                kill++;
                if (card.getEnergy2Name() != null) {
                    kill++;
                }
                if (card.getEnergy1Name() != null) {
                    kill++;
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            if (bench[i] != null) {
                if (bench[i].getHitpoint() < 0) {
                    kill++;
                }
                if (bench[i].getEnergy1Name() != null) {
                    kill++;
                }
                if (bench[i].getEnergy2Name() != null) {
                    kill++;
                }
            }
        }
        second.setKills(kill + second.getKills());

        Card card2 = second.getActive();
        Card[] bench2 = second.getBench();
        kill = 0;
        if (card2 != null) {
            if (card2.getHitpoint() < 0) {
                if (card2 != null) {
                    kill++;
                    if (card2.getEnergy2Name() != null) {
                        kill++;
                    }
                    if (card2.getEnergy1Name() != null) {
                        kill++;
                    }
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            if (bench2[i] != null) {
                if (bench2[i].getHitpoint() < 0) {
                    kill++;
                }
                if (bench2[i].getEnergy1Name() != null) {
                    kill++;
                }
                if (bench2[i].getEnergy2Name() != null) {
                    kill++;
                }
            }
        }
        first.setKills(first.getKills() + kill);
        kill = 0;


        card.setEnergy1Name(card.getEnergy2Name());
        card.setEnergy1(card.getEnergy2());
        card.setEnergy2(1);
        card.setEnergy2Name(null);


        if (first.getActive() != null) {
            if (first.getActive().isItFired()) {
                if (first.getActive().getShieldHitpoint() > 10) {
                    first.getActive().setShieldHitpoint(first.getActive().getShieldHitpoint() - 10);
                } else {
                    first.getActive().setHitpoint(first.getActive().getHitpoint() - (10 - first.getActive().getShieldHitpoint()));
                    first.getActive().setShieldHitpoint(0);
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            if (bench[i] != null) {
                if (bench[i].isItFired()) {
                    if (bench[i].getShieldHitpoint() > 10) {
                        bench[i].setShieldHitpoint(bench[i].getShieldHitpoint() - 10);
                    } else {
                        bench[i].setHitpoint(bench[i].getHitpoint() - (10 - bench[i].getShieldHitpoint()));
                        bench[i].setShieldHitpoint(0);
                    }
                }
            }
        }
        if (first.getActive() != null) {
            first.getActive().setItFired(false);
            first.getActive().setItSlept(false);
        }
        for (int i = 0; i < 3; i++) {
            if (bench[i] != null) {
                bench[i].setItSlept(false);
            }
        }
        for (int i = 0; i < 3; i++) {
            if (bench[i] != null) bench[i].setItFired(false);
        }
        first.setBenchFull(bench);
        if (first.getActive() != null) {
            if (first.getActive().getType().equals("plant")) {
                first.getActive().setShieldHitpoint(15);
            }
        }

        Card[] bench1 = first.getBench();
        for (int i = 0; i < 3; i++) {
            if (bench1[i] != null) {
                if (bench1[i].getType().equals("plant")) {
                    bench1[i].setShieldHitpoint(15);
                }
            }
        }
        first.setBenchFull(bench1);

        return true;
    }

    public static void dragoniteAction(User first, User second) {
        Card activeAttacker = first.getActive();
        double damage = activeAttacker.getDamageOrHeal(Card.getWeakness(second.getActive().getName(), activeAttacker.getType()), second.getActive().getResistence());
        Card activeDefender = second.getActive();

        if (damage > activeDefender.getShieldHitpoint()) {
            double realDamage = damage - activeDefender.getShieldHitpoint();
            activeDefender.setHitpoint(activeDefender.getHitpoint() - realDamage);

            activeDefender.setShieldHitpoint(0);
        } else {
            activeDefender.setShieldHitpoint(activeDefender.getShieldHitpoint() - damage);

        }
        if (!second.getActive().getType().equals("water")) {
            activeDefender.setItFired(true);
        }
        second.setActive(activeDefender);
    }

    public static void tepigAction(User first, User second) {
        Card activeAttacker = first.getActive();
        double damage = activeAttacker.getDamageOrHeal(Card.getWeakness(second.getActive().getName(), activeAttacker.getType()), second.getActive().getResistence());
        Card activeDefender = second.getActive();
        if (!activeDefender.getType().equals("water")) {
            activeDefender.setItFired(true);
        }
        if (damage > activeDefender.getShieldHitpoint()) {
            double realDamage = damage - activeDefender.getShieldHitpoint();
            activeDefender.setHitpoint(activeDefender.getHitpoint() - realDamage);
            activeDefender.setShieldHitpoint(0);
        } else {
            activeDefender.setShieldHitpoint(activeDefender.getShieldHitpoint() - damage);
        }
        Card[] benchDefender = second.getBench();
        for (int i = 0; i < 3; i++) {
            if (benchDefender[i] != null) {
                damage = 0.2 * activeAttacker.getDamageOrHeal(Card.getWeakness(benchDefender[i].getName(), activeAttacker.getType()), benchDefender[i].getResistence());
                if (benchDefender[i].getShieldHitpoint() < damage) {
                    double realDamage = damage - benchDefender[i].getShieldHitpoint();


                    benchDefender[i].setHitpoint(benchDefender[i].getHitpoint() - realDamage);
                }
                if (!benchDefender[i].getType().equals("water")) {
                    benchDefender[i].setItFired(true);
                }
            }
        }
        second.setBenchFull(benchDefender);
        second.setActive(activeDefender);
    }

    public static void lugiaAction(User first, User second) {
        second.getActive().setItSlept(true);
        Card activeAttacker = first.getActive();
        double damage = activeAttacker.getDamageOrHeal(Card.getWeakness(second.getActive().getName(), activeAttacker.getType()), second.getActive().getResistence());
        Card activeDefender = second.getActive();
        if (damage > activeDefender.getShieldHitpoint()) {
            double realDamage = damage - activeDefender.getShieldHitpoint();
            activeDefender.setHitpoint(activeDefender.getHitpoint() - realDamage);
            activeDefender.setShieldHitpoint(0);
        } else {
            activeDefender.setShieldHitpoint(activeDefender.getShieldHitpoint() - damage);
        }
        second.setActive(activeDefender);
    }

    public static void ducklettAction(User first, User second, Matcher matcher) {
        int target = Integer.parseInt(matcher.group("target"));
        if (target == 0) {
            Card card = second.getActive();
            card.setEnergy1Name(null);
            card.setEnergy2Name(null);
            card.setEnergy1(1);
            card.setEnergy2(1);


            Card activeAttacker = first.getActive();
            double damage = activeAttacker.getDamageOrHeal(Card.getWeakness(second.getActive().getName(), activeAttacker.getType()), second.getActive().getResistence());
            Card activeDefender = second.getActive();
            if (damage > activeDefender.getShieldHitpoint()) {
                double realDamage = damage - activeDefender.getShieldHitpoint();
                activeDefender.setHitpoint(activeDefender.getHitpoint() - realDamage);
                activeDefender.setShieldHitpoint(0);
            } else {
                activeDefender.setShieldHitpoint(activeDefender.getShieldHitpoint() - damage);
            }

            second.setActive(card);
        } else {
            target--;
            Card[] bench = second.getBench();
            bench[target].setEnergy1(1);
            bench[target].setEnergy2(1);
            bench[target].setEnergy2Name(null);
            bench[target].setEnergy1Name(null);
            double damage = 0;
            Card activeAttacker = first.getActive();
            damage = activeAttacker.getDamageOrHeal(Card.getWeakness(bench[target].getName(), activeAttacker.getType()), bench[target].getResistence());
            if (bench[target].getShieldHitpoint() < damage) {
                double realDamage = damage - bench[target].getShieldHitpoint();
                bench[target].setHitpoint(bench[target].getHitpoint() - realDamage);
                bench[target].setShieldHitpoint(0);
            } else {
                bench[target].setShieldHitpoint(bench[target].getShieldHitpoint() - damage);
            }
            second.setBenchFull(bench);
        }
    }

    public static void pinecoAction(User first, User second) {
        double heal = first.getActive().getDamageOrHeal(1, first.getActive().getResistence());
        Card card = first.getActive();
        card.setHitpoint(first.getActive().getHitpoint() + heal);
        first.setActive(card);
    }

    public static void rowletAction(User first, User second, Matcher matcher) {
        int target = Integer.parseInt(matcher.group("target"));
        Card[] bench = first.getBench();
        target--;
        double heal = first.getActive().getDamageOrHeal(Card.getWeakness(bench[target].getName(), "plant"), bench[target].getResistence());
        if(bench[target].isItFired() == true){
            bench[target].setHitpoint(bench[target].getHitpoint() - 10);
            bench[target].setItFired(false);
        }
        bench[target].setHitpoint(bench[target].getHitpoint() + heal);
        first.setBenchFull(bench);
    }

    public static void endRound(User user) {
        if (user.getActive() != null) {
            if (user.getActive().getHitpoint() <= 0) {
                user.setActive(null);
            }
        }
        Card[] bench = user.getBench();
        if (bench != null) {
            for (int i = 0; i < 3; i++) {
                if (bench[i] != null) {
                    if (bench[i].getHitpoint() <= 0) {
                        bench[i] = null;
                    }
                }
            }
        }
    }

    public static void endTurn(User first) {
        if (first.getActive() != null) {
            if (first.getActive().isItFired()) {
                first.getActive().setItFired(false);
                if (first.getActive().getShieldHitpoint() > 10) {
                    first.getActive().setShieldHitpoint(first.getActive().getShieldHitpoint() - 10);
                } else {
                    first.getActive().setHitpoint(first.getActive().getHitpoint() - (10 - first.getActive().getShieldHitpoint()));
                    first.getActive().setShieldHitpoint(0);
                }
            }
            if (first.getActive().isItSlept()) {
                first.getActive().setItSlept(false);
            }
        }
        if (first.getActive() != null) {
            if (first.getActive().getType().equals("plant")) {
                first.getActive().setShieldHitpoint(15);
            }
        }
        Card[] bench = first.getBench();
        if (bench != null) {
            for (int i = 0; i < 3; i++) {
                if (bench[i] != null) {
                    if (bench[i].isItFired()) {
                        bench[i].setItFired(false);
                        if (bench[i].getShieldHitpoint() > 10) {
                            bench[i].setShieldHitpoint(bench[i].getShieldHitpoint() - 10);
                        } else {
                            bench[i].setHitpoint(bench[i].getHitpoint() - (10 - bench[i].getShieldHitpoint()));
                            bench[i].setShieldHitpoint(0);
                        }
                    }
                    if (bench[i].isItSlept()) {
                        bench[i].setItSlept(false);
                    }
                    if (bench[i].getType().equals("plant")) {
                        bench[i].setShieldHitpoint(15);
                    }

                }
            }
        }
        first.setBenchFull(bench);
    }

}
