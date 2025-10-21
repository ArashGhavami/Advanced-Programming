package Model;

import java.util.ArrayList;

public class User {

    private int kills;
    {
        kills = 0;
    }
    private Card active;
    private Card[] bench = new Card[3];
    ArrayList<Card> hand = new ArrayList<>();
    private String username;
    private String password;
    private String email;
    private int experience;

    {
        experience = 0;
    }

    private double coins;

    {
        coins = 300;
    }

    private ArrayList<Card> cards = new ArrayList<>();
    private ArrayList<Card> deckCards = new ArrayList<>();
    private ArrayList<Card> allCards = new ArrayList<>();

    private static User loggedInUser = null;

    private int getKilled = 0;

    private double firstHandTotalHitpoints = 0;

    private static ArrayList<User> allUsers = new ArrayList<>();

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public static boolean doesThisUsernameAlreadyExists(String username) {
        for (User user : allUsers) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static void addUser(User user) {
        allUsers.add(user);
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static User getUserByUsername(String username) {
        for (User user : allUsers) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public int getNumberOfDecks() {
        return deckCards.size();
    }

    public double getNumberOfCoins() {
        return coins;
    }

    public int getExperience() {
        return experience;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public boolean doIHaveThisCard(String cardname) {
        if(cards == null){
            return false;
        }
        for (Card card : cards) {
            if (card.getName().equals(cardname)) {
                return true;
            }
        }
        return false;
    }

    public boolean doIHaveThisNameInMyDeck(String cardname) {
        for (Card card : deckCards) {
            if (card.getName().equals(cardname)) {
                return true;
            }
        }
        return false;
    }

    public void addCardToDeck(String cardname) {
        Card card = null;
        for (Card card2 : cards) {
            if (card2.getName().equals(cardname)) {
                card = card2;
                break;
            }
        }
        card.setItInDeck(true);
        cards.remove(card);
        deckCards.add(card);
        firstHandTotalHitpoints += card.getHitpoint();
    }

    public void removeCardFromDeck(String cardname) {
        Card card = null;
        for (Card card2 : deckCards) {
            if (card2.getName().equals(cardname)) {
                card = card2;
                break;
            }
        }
        card.setItInDeck(false);
        cards.add(card);
        deckCards.remove(card);
        firstHandTotalHitpoints -= card.getHitpoint();
    }

    public ArrayList<Card> getDeck() {
        return deckCards;
    }

    public static ArrayList<User> getUsers() {
        return allUsers;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void setCoin(double coins) {
        this.coins = coins;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public void setActive(Card active) {
        this.active = active;
    }

    public void setBench(Card card, int index) {
        bench[index] = card;
    }


    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public Card getActive() {
        return active;
    }

    public Card[] getBench() {
        return bench;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setBenchFull(Card[] bench) {
        this.bench = bench;
    }

    public void setGetKilled(int getKilled) {
        this.getKilled = getKilled;
    }

    public int getGetKilled() {
        return getKilled;
    }

    public double getFirstHandTotalHitpoints() {
        return firstHandTotalHitpoints;
    }

    public void setFirstHandTotalHitpoints(double firstHandTotalHitpoints) {
        this.firstHandTotalHitpoints = firstHandTotalHitpoints;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void addToAllCards(Card card){
        allCards.add(card);
    }

    public void removeFromAllCards(Card card){
        allCards.remove(card);
    }

    public ArrayList<Card> getAllCards(){
        return allCards;
    }


    public void setDeckCards(ArrayList<Card> deckCards){
        this.deckCards = deckCards;
    }

    public Card getFirstCardInList(String cardname){
        for(Card card : allCards){
            if(card.getName().equals(cardname)){
                return card;
            }
        }
        return null;
    }

    public void setAllCards(ArrayList<Card> newAllCards){
        this.allCards = newAllCards;
    }

    public int getKills(){
        return kills;
    }

    public void setKills(int kills){
        this.kills = kills;
    }

    public void setFirstHandTotalHitPoint(){
        double sum = 0;
        for(Card card: deckCards){
            if(!card.getType().equals("energy")){
                sum+= card.getMaxHitpoint();
            }
        }
        this.setFirstHandTotalHitpoints(sum);
    }
}