import Model.Action;
import Model.Friend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        final String text;
        final int THREAD_COUNT;
        try {
            StringBuilder builder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            THREAD_COUNT = Integer.parseInt(bufferedReader.readLine());
            String line;
            while (true) {
                line = bufferedReader.readLine();
                if (line.equals("!end"))
                    break;
                builder.append(line).append("\n");
            }
            text = builder.substring(0, builder.length() - 1);
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final long startTime = System.nanoTime();

        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();

        for (int i = 0; i < 26; i++) {
            ArrayList<String> strings = new ArrayList<>();
            arrayLists.add(strings);
        }

        double hackCode = 0;
        for (int i = 0; i < text.length(); i++) {
            hackCode = hackCode * 0.99998 + text.charAt(i);
        }

        String formattedValue = String.format("%.3f", hackCode);

        ArrayList<String> words = getWords(text);

        ArrayList<Thread> threads = new ArrayList<>();

        Action action = new Action(arrayLists);

        int number = words.size() / THREAD_COUNT + 1;

        for (int i = 0; i < THREAD_COUNT; i++) {

            int firstIndex = number * i;
            int lastIndex = number * (i + 1);

            ArrayList<String> wordsToBeChecked = new ArrayList<>();

            for (int j = firstIndex; j < Math.min(lastIndex, words.size()); j++) {
                wordsToBeChecked.add(words.get(j));
            }

            Friend friend = new Friend(wordsToBeChecked, action);
            threads.add(friend);
        }

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads.get(i).start();
        }
        for (int i = 0; i < THREAD_COUNT; i++) threads.get(i).join();

        ArrayList<String> resultStrings = new ArrayList<>();
        ArrayList<Integer> resultRepetitions = new ArrayList<>();

        for (int i = 0; i < 26; i++) {
            checkNumbersRepetition(arrayLists.get(i), resultStrings, resultRepetitions);
        }

        int howManyWordsInTotal = 0;
        for (Integer integer : resultRepetitions) {
            howManyWordsInTotal += integer;
        }


        final long elapsedMillis = (System.nanoTime() - startTime) / 1000000;


        System.out.println("Word count: " + howManyWordsInTotal);
        int maxIndex = findIndexOfMaxWord(resultStrings);
        System.out.println("The longest word is \"" + resultStrings.get(maxIndex) + "\" with a length of " + resultStrings.get(maxIndex).length() + ".");
        maxIndex = findIndexOfMaxRepetition(resultRepetitions);
        System.out.println("The most frequent word is \"" + resultStrings.get(maxIndex).toLowerCase() + "\" with " + resultRepetitions.get(maxIndex) + " appearances.");

        System.out.println("HackCode: " + formattedValue);

    }


    public static ArrayList<String> getWords(String text) {

        ArrayList<String> strings = new ArrayList<>();

        StringBuilder word = null;
        for (int i = 0; i < text.length(); i++) {
            if (word == null) word = new StringBuilder();
            if (Character.isAlphabetic(text.charAt(i))) {
                word.append(text.charAt(i));
            } else {
                if (word != null && word.length() != 0) {
                    String string = word.toString();
                    strings.add(string);
                }
                word = null;
            }
        }

        if (word != null) {
            String string = word.toString();
            strings.add(string);
        }

        return strings;
    }


    public static void checkNumbersRepetition(ArrayList<String> target, ArrayList<String> resultStrings, ArrayList<Integer> resultRepetitions) {

        if (target.isEmpty()) return;

        for (int i = 0; i < target.size(); i++) {
            if (target.get(i) != null) {
                resultStrings.add(target.get(i));
                resultRepetitions.add(howManyOfThis(target, target.get(i)));
            }
        }

    }

    public static int howManyOfThis(ArrayList<String> target, String realTarget) {
        int result = 0;

        for (int i = 0; i < target.size(); i++) {
            if (target.get(i) == null) {
                continue;
            }
            if (target.get(i).equalsIgnoreCase(realTarget)) {
                result++;
                target.set(i, null);
            }
        }
        return result;
    }

    public static int findIndexOfMaxWord(ArrayList<String> target) {
        int max = 0;
        int index = 0;
        for (int i = 0; i < target.size(); i++) {
            if (target.get(i).length() > max) {
                max = target.get(i).length();
                index = i;
            }
        }
        return index;
    }

    public static int findIndexOfMaxRepetition(ArrayList<Integer> target) {
        int max = 0;
        int index = 0;

        for (int integer = 0; integer < target.size(); integer++) {
            if (target.get(integer) > max) {
                max = target.get(integer);
                index = integer;
            }
        }
        return index;
    }

}