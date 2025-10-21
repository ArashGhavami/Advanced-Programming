import java.text.DecimalFormat;
import java.util.*;

public class TerrorMask {

    public static double time_finder(double[] place, double[] rightVelocity, double[] leftVelocity, double[] wastedTime, double currentPlace) {
        double maxResult = 0;
        for (int i = 0; i < place.length; i++) {
            if (currentPlace > place[i]) {
                double temp = (currentPlace - place[i]) / rightVelocity[i];
                temp += wastedTime[i];
                if (temp > maxResult) {
                    maxResult = temp;
                }
            } else {
                double temp = (place[i] - currentPlace) / leftVelocity[i];
                temp += wastedTime[i];
                if (temp > maxResult) {
                    maxResult = temp;
                }
            }
        }
        return maxResult;
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numberOfCoworkers = scanner.nextInt();
        double[] place = new double[numberOfCoworkers];
        for (int i = 0; i < numberOfCoworkers; i++) {
            place[i] = scanner.nextDouble();
        }
        double[] rightVelocity = new double[numberOfCoworkers];
        for (int i = 0; i < numberOfCoworkers; i++) {
            rightVelocity[i] = scanner.nextDouble();
        }
        double[] leftVelocity = new double[numberOfCoworkers];
        for (int i = 0; i < numberOfCoworkers; i++) {
            leftVelocity[i] = scanner.nextDouble();
        }
        double[] wastedTime = new double[numberOfCoworkers];
        for (int i = 0; i < numberOfCoworkers; i++) {
            wastedTime[i] = scanner.nextDouble();
        }
        double minPlace = place[0];
        double maxPlace = place[0];
        for (int i = 0; i < place.length; i++) {
            if (place[i] > maxPlace) {
                maxPlace = place[i];
            }
            if (place[i] < minPlace) {
                minPlace = place[i];
            }
        }
        double currentPlace = minPlace;
        double distance = (maxPlace - minPlace) / 900;
        double minResult = time_finder(place, rightVelocity, leftVelocity, wastedTime, currentPlace);
        for (int i = 0; i < 900; i++) {
            currentPlace += distance;
            double temp = time_finder(place, rightVelocity, leftVelocity, wastedTime, currentPlace);
            if (temp < minResult) {
                minResult = temp;
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String roundNumber = decimalFormat.format(minResult);
        System.out.println(roundNumber);
    }
}