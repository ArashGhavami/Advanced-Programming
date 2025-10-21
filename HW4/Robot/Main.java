import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        View.runProgram(scanner);
    }

}

class View {

    public static void runProgram(Scanner scanner) {
        String regex;
        String input;
        Matcher matcher;

        while (true) {
            input = scanner.nextLine();

            regex = "\\s*create security robot (?<id>\\d+) (?<power>\\d+) (?<live>\\d+)\\s*";
            matcher = getCommandMatcher(regex, input);
            if (matcher.matches()) {
                Controller.createSecurityRobot(matcher);
                continue;
            }

            regex = "\\s*create delivery robot (?<id>\\d+) (?<vehicle>\\S+)\\s*";
            matcher = getCommandMatcher(regex, input);
            if (matcher.matches()) {
                Controller.createDeliveryRobot(matcher);
                continue;
            }

            regex = "\\s*create cleaning robot (?<id>\\d+) (?<numTasks>\\d+) (?<areas>(\\d+)( ((\\d+\\s)+)?(\\d+))?)\\s*";
            matcher = getCommandMatcher(regex, input);
            if (matcher.matches()) {
                Controller.createCleaningRobot(matcher);
                continue;
            }

            regex = "\\s*deliver robot (?<id>\\d+)\\s*";
            matcher = getCommandMatcher(regex, input);
            if (matcher.matches()) {
                printDeliverRobot(matcher);
                continue;
            }

            regex = "\\s*clean robot (?<id>\\d+) (?<area>\\d+)\\s*";
            matcher = getCommandMatcher(regex, input);
            if (matcher.matches()) {
                printCleanRobot(matcher);
                continue;
            }

            regex = "\\s*perform task robot (?<id>\\d+)\\s*";
            matcher = getCommandMatcher(regex, input);
            if (matcher.matches()) {
                printPerformRobot(matcher);
                continue;
            }


            regex = "\\s*perform tast robot (?<id>\\d+)\\s*";
            matcher = getCommandMatcher(regex, input);
            if (matcher.matches()) {
                printPerformRobot(matcher);
                continue;
            }


            regex = "\\s*attack robot (?<id>\\d+) (?<enemyPower>\\d+)\\s*";
            matcher = getCommandMatcher(regex, input);
            if (matcher.matches()) {
                printAttack(matcher);
                continue;
            }

            regex = "\\s*get all robots\\s*";
            matcher = getCommandMatcher(regex, input);
            if (matcher.matches()) {
                printallRobots(matcher);
                continue;
            }

            regex = "\\s*end\\s*";
            matcher = getCommandMatcher(regex, input);
            if (matcher.matches()) {
                break;
            }


            System.out.println("unknown command");

        }
    }

    private static Matcher getCommandMatcher(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }

    private static void printDeliverRobot(Matcher matcher) {
        System.out.println(Controller.deliverRobot(matcher));
    }

    private static void printCleanRobot(Matcher matcher) {
        System.out.println(Controller.cleanRobot(matcher));
    }

    private static void printPerformRobot(Matcher matcher) {
        System.out.println(Controller.performRobot(matcher));
    }

    private static void printAttack(Matcher matcher) {
        System.out.println(Controller.attackRobot(matcher));
    }

    private static void printallRobots(Matcher matcher) {
        System.out.println(Controller.getAllRobots(matcher));
    }

}

abstract class Factory {

    public abstract Robot createRobot(Object... params);

    public void doAction(Object... params) {
        Robot robot = createRobot(params);
        robot.action();
    }
}

interface Robot {

    ArrayList<Robot> allRobots = new ArrayList<>();

    static void addRobot(Robot robot) {
        allRobots.add(robot);
    }

    static ArrayList<Robot> getRobots() {
        return allRobots;
    }

    String action();

}

class CleaningRobot implements Robot {

    private int id;
    private int numTask;
    private ArrayList<Integer> areas;

    private static ArrayList<CleaningRobot> cleaningRobots = new ArrayList<>();

    public CleaningRobot(int id, int numTask, ArrayList<Integer> areas) {
        this.id = id;
        this.numTask = numTask;
        this.areas = areas;
    }

    @Override
    public String action() {
        return "cleaning robot " + this.getId() + " cleaned the area";
    }

    public int getId() {
        return id;
    }

    public int getNumTask() {
        return numTask;
    }

    public ArrayList<Integer> getAreas() {
        return areas;
    }

    public static ArrayList<CleaningRobot> getCleaningRobots() {
        return cleaningRobots;
    }

    public static void addRobot(CleaningRobot cleaningRobot) {
        cleaningRobots.add(cleaningRobot);
    }

    public boolean isValidArea(int area) {
        for (int integers : areas) {
            if (integers == area) {
                return true;
            }
        }
        return false;
    }

    public void setNumTask(int numTask) {
        this.numTask = numTask;
    }
}

class DeliveryRobot implements Robot {

    private int id;
    private String vehicle;

    private static ArrayList<DeliveryRobot> deliveryRobots = new ArrayList<>();


    public DeliveryRobot(int id, String vehicle) {
        this.id = id;
        this.vehicle = vehicle;
    }

    @Override
    public String action() {
        return "delivery robot " + this.getId() + " sent the pocket by " + this.getVehicle();
    }

    public int getId() {
        return id;
    }

    public String getVehicle() {
        return vehicle;
    }

    public static ArrayList<DeliveryRobot> getDeliveryRobots() {
        return deliveryRobots;
    }

    public static ArrayList<DeliveryRobot> getSecurityRobots() {
        return deliveryRobots;
    }

    public static void addRobot(DeliveryRobot deliveryRobot) {
        deliveryRobots.add(deliveryRobot);
    }
}

class SecurityRobot implements Robot {

    private int id;
    private int power;
    private int live;

    private static ArrayList<SecurityRobot> allSecurityRobots = new ArrayList<>();

    public SecurityRobot(int id, int power, int live) {
        this.id = id;
        this.power = power;
        this.live = live;
    }

    @Override
    public String action() {
        return "security robot is monitoring";
    }

    public static ArrayList<SecurityRobot> getAllSecurityRobots() {
        return allSecurityRobots;
    }

    public static void addRobot(SecurityRobot securityRobot) {
        allSecurityRobots.add(securityRobot);
    }

    public int getId() {
        return id;
    }

    public int getPower() {
        return power;
    }

    public int getLive() {
        return live;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setLive(int live) {
        this.live = live;
    }

    public static void setAllSecurityRobots(ArrayList<SecurityRobot> allSecurityRobots) {
        SecurityRobot.allSecurityRobots = allSecurityRobots;
    }
}

class DeliveryFactory extends Factory {

    @Override
    public Robot createRobot(Object... params) {
        return new DeliveryRobot((Integer) params[0], (String) params[1]);
    }
}

class CleaningFactory extends Factory {
    @Override
    public Robot createRobot(Object... params) {

        return new CleaningRobot((Integer) params[0], (Integer) params[1], (ArrayList<Integer>) params[2]);
    }
}

class Controller {

    private static SecurityRobot getSecurityRobotById(int id) {
        for (SecurityRobot securityRobot : SecurityRobot.getAllSecurityRobots()) {
            if (securityRobot.getId() == id) return securityRobot;
        }
        return null;
    }

    private static CleaningRobot getCleaningRobotById(int id) {
        for (CleaningRobot cleaningRobot : CleaningRobot.getCleaningRobots()) {
            if (cleaningRobot.getId() == id) return cleaningRobot;
        }
        return null;
    }

    private static DeliveryRobot getDeliveryRobotById(int id) {
        for (DeliveryRobot deliveryRobot : DeliveryRobot.getDeliveryRobots()) {
            if (deliveryRobot.getId() == id) return deliveryRobot;
        }
        return null;
    }

    public static void createSecurityRobot(Matcher matcher) {
        int live = Integer.parseInt(matcher.group("live"));
        int power = Integer.parseInt(matcher.group("power"));
        int id = Integer.parseInt(matcher.group("id"));

        Factory factory = new SecurityFactory();
        SecurityRobot securityRobot = (SecurityRobot) factory.createRobot(id, power, live);
        Robot.addRobot(securityRobot);
        SecurityRobot.addRobot(securityRobot);
    }

    public static void createCleaningRobot(Matcher matcher) {
        int id = Integer.parseInt(matcher.group("id"));
        int numTasks = Integer.parseInt(matcher.group("numTasks"));
        String[] areasString = matcher.group("areas").split(" ");
        ArrayList<Integer> areas = new ArrayList<>();
        for (String string : areasString) {
            areas.add(Integer.parseInt(string));
        }

        Factory factory = new CleaningFactory();
        CleaningRobot cleaningRobot = (CleaningRobot) factory.createRobot(id, numTasks, areas);
        Robot.addRobot(cleaningRobot);
        CleaningRobot.addRobot(cleaningRobot);
    }

    public static void createDeliveryRobot(Matcher matcher) {
        int id = Integer.parseInt(matcher.group("id"));
        String vehicle = matcher.group("vehicle");

        Factory factory = new DeliveryFactory();
        DeliveryRobot deliveryRobot = (DeliveryRobot) factory.createRobot(id, vehicle);
        Robot.addRobot(deliveryRobot);
        DeliveryRobot.addRobot(deliveryRobot);
    }

    public static String deliverRobot(Matcher matcher) {
        int id = Integer.parseInt(matcher.group("id"));
        DeliveryRobot deliveryRobot = getDeliveryRobotById(id);
        if (deliveryRobot == null) return "invalid robot id";
        return deliveryRobot.action();
    }

    public static String cleanRobot(Matcher matcher) {
        int id = Integer.parseInt(matcher.group("id"));
        int area = Integer.parseInt(matcher.group("area"));

        CleaningRobot cleaningRobot = getCleaningRobotById(id);


        if (cleaningRobot == null) return "invalid robot id";
        if (cleaningRobot.getNumTask() == 0) return "this robot is retired";
        if (!cleaningRobot.isValidArea(area)) return "invalid area";


        cleaningRobot.setNumTask(cleaningRobot.getNumTask() - 1);
        return cleaningRobot.action();
    }

    public static String performRobot(Matcher matcher) {
        int id = Integer.parseInt(matcher.group("id"));
        SecurityRobot securityRobot = getSecurityRobotById(id);
        if (securityRobot == null) return "invalid robot id";
        return securityRobot.action();
    }

    public static String attackRobot(Matcher matcher) {
        int id = Integer.parseInt(matcher.group("id"));
        int enemyPower = Integer.parseInt(matcher.group("enemyPower"));

        SecurityRobot securityRobot = getSecurityRobotById(id);
        if (securityRobot == null) return "invalid robot id";
        if (securityRobot.getPower() >= enemyPower) return "attack was successful";
        securityRobot.setLive(securityRobot.getLive() - 1);
        if (securityRobot.getLive() == 0) {
            SecurityRobot.getAllSecurityRobots().remove(securityRobot);
            Robot.getRobots().remove(securityRobot);
        }

        return "attack was unsuccessful";
    }

    public static StringBuilder getAllRobots(Matcher matcher) {
        ArrayList<Robot> robots = Robot.getRobots();
        StringBuilder result = new StringBuilder();

        for (Robot robot : robots) {
            try {
                SecurityRobot securityRobot = (SecurityRobot) robot;
                if (securityRobot.getLive() > 0) {
                    result.append("robot ");
                    result.append(securityRobot.getId());
                    result.append(" security\n");
                }
            } catch (ClassCastException e) {
                try {
                    DeliveryRobot deliveryRobot = (DeliveryRobot) robot;
                    result.append("robot ");
                    result.append(deliveryRobot.getId());
                    result.append(" delivery\n");
                } catch (ClassCastException e1) {
                    try {
                        CleaningRobot cleaningRobot = (CleaningRobot) robot;
                        result.append("robot ");
                        result.append(cleaningRobot.getId());
                        result.append(" cleaning\n");
                    } catch (Exception e2) {

                    }
                }
            }
        }

        return result;
    }


}

class SecurityFactory extends Factory {

    @Override
    public Robot createRobot(Object... params) {
        return new SecurityRobot((Integer) params[0], (Integer) params[1], (Integer) params[2]);
    }
}