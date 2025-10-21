import java.util.Scanner;

public class Zipper {
    
    public static int result_finder(int[] chosen, int number) {
        int[] distances = new int[chosen.length];
        for (int i = 0; i < chosen.length; i++) {
            distances[i] = number - chosen[i];
            if (distances[i] < 0)
                distances[i] = -distances[i];
        }
        int min = distances[distances.length - 1];
        int minIndex = distances.length-1;
        for (int i = distances.length - 1; i >= 0; i--) {
            if (min > distances[i]) {
                min = distances[i];
                minIndex = i;
            }
        }
        return minIndex;
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int number = scanner.nextInt();
        int[] intensity = new int[number * number];
        for (int i = 0; i < intensity.length; i++) {
            intensity[i] = scanner.nextInt();
        }
        int[][] differentNumbers = new int[number * number][2];
        for (int i = 0; i < number * number; i++) {
            for (int j = 0; j < 2; j++) {
                differentNumbers[i][j] = -1;
            }
        }
        int line= -1;
        for (int i = 0; i < intensity.length; i++) {
            boolean notRepetetive = true;
            for (int j = line; j >= 0; j--) {
                if (differentNumbers[j][0] == intensity[i]) {
                    notRepetetive = false;
                }
            }
            if (notRepetetive == false) {
                continue;
            }
            line++;
            differentNumbers[line][0] = intensity[i];
            int howManyRepeated = 0;
            for (int j = 0; j < intensity.length; j++) {
                if (intensity[i] == intensity[j]) {
                    howManyRepeated++;
                }
            }
            differentNumbers[line][1] = howManyRepeated;
        }
        line++;
        //line = number of rows which is full in the matrix
        //sorting differentNumbers:
        for (int i = 0; i < line; i++) {
            for (int j = i; j < line; j++) {
                if (differentNumbers[j][1] > differentNumbers[i][1]) {
                    int temp = differentNumbers[j][1];
                    differentNumbers[j][1] = differentNumbers[i][1];
                    differentNumbers[i][1] = temp;

                    temp = differentNumbers[j][0];
                    differentNumbers[j][0] = differentNumbers[i][0];
                    differentNumbers[i][0] = temp;

                }
                if (differentNumbers[j][1] == differentNumbers[i][1]) {
                    if (differentNumbers[j][0] > differentNumbers[i][0]) {
                        int temp = differentNumbers[j][0];
                        differentNumbers[j][0] = differentNumbers[i][0];
                        differentNumbers[i][0] = temp;

                        temp = differentNumbers[j][1];
                        differentNumbers[j][1] = differentNumbers[i][1];
                        differentNumbers[i][1] = temp;
                    }
                }
            }
        }
        int chosen = scanner.nextInt();
        int[] chosenNumbers = new int[chosen];
        for (int i = 0; i < chosen; i++) {
            chosenNumbers[i] = differentNumbers[i][0];
        }
        for (int i = 0; i < chosenNumbers.length; i++) {
            for (int j = i; j < chosenNumbers.length; j++) {
                if (chosenNumbers[j] < chosenNumbers[i]) {
                    int temp = chosenNumbers[i];
                    chosenNumbers[i] = chosenNumbers[j];
                    chosenNumbers[j] = temp;
                }
            }
        }
        int counter = 0;
        for (int i = 0; i < number * number; i++) {
            System.out.print(result_finder(chosenNumbers, intensity[i]) + " ");
            counter++;
            if (counter == number) {
                System.out.println();
                counter = 0;
            }
        }

    }
}
