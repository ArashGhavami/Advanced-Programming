import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StoreServer extends Thread {
    private static Map<String, Integer> inventory = new HashMap<>();
    private static Map<String, Customer> customers = new HashMap<>();
    private Socket socket;
    private Customer currentCustomer;

    public StoreServer(Socket socket) {
        this.currentCustomer = null;
        this.socket = socket;
    }

    @Override
    public void run() {
        DataInputStream dataInputStream;
        DataOutputStream dataOutputStream;
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            try {
                handleCommads(dataOutputStream, dataInputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void handleCommads(DataOutputStream dataOutputStream, DataInputStream dataInputStream) throws IOException {
        String input = "";
        try {
            input = dataInputStream.readUTF();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String regex = "";
        Matcher matcher = null;
        //register:
        regex = "\\s*register:(?<id>[a-zA-Z0-9_]+):(?<name>[a-zA-Z0-9_]+):(?<money>[0-9]*\\.?[0-9]+)\\s*";
        matcher = getMatcher(regex, input);
        if (matcher.matches()) {
            registerChecker(matcher, dataOutputStream);
            return;
        }
        //login:
        regex = "\\s*login:(?<id>[a-zA-Z0-9_]+)\\s*";
        matcher = getMatcher(regex, input);
        if (matcher.matches()) {
            login(matcher, dataOutputStream);
            return;
        }
        //logout:
        regex = "\\s*logout\\s*";
        matcher = getMatcher(regex, input);
        if (matcher.matches()) {
            logout(dataOutputStream);
            return;
        }
        //getPrice:
        regex = "\\s*get price:(?<shoeName>\\S+)\\s*";
        matcher = getMatcher(regex, input);
        if (matcher.matches()) {
            priceChecker(matcher, dataOutputStream);
            return;
        }
        //getQuantity:
        regex = "\\s*get quantity:(?<shoeName>\\S+)\\s*";
        matcher = getMatcher(regex, input);
        if (matcher.matches()) {
            quantityChecker(matcher, dataOutputStream);
            return;
        }
        //get money:
        regex = "\\s*get money\\s*";
        matcher = getMatcher(regex, input);
        if (matcher.matches()) {
            moneyChecker(matcher, dataOutputStream);
            return;
        }
        //charge:
        regex = "\\S*charge:(?<money>[0-9]+)";
        matcher = getMatcher(regex, input);
        if (matcher.matches()) {
            chargeChecker(matcher, dataOutputStream);
            return;
        }
        //purchase:
        regex = "purchase:(?<shoeName>[0-9A-Za-z]+):(?<quantity>[0-9]+)";
        matcher = getMatcher(regex, input);
        if (matcher.matches()) {
            purchaseChecker(matcher, dataOutputStream);
            return;
        }
        writeInBuffer(dataOutputStream, "invalid command");
    }

    private void purchaseChecker(Matcher matcher, DataOutputStream dataOutputStream) {
        String name = matcher.group("shoeName");
        int quantity = Integer.parseInt(matcher.group("quantity"));
        if (currentCustomer == null) {
            writeInBuffer(dataOutputStream, "you should login as a user first!");
        }
        try {
            purchaseProduct(name, quantity, dataOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chargeChecker(Matcher matcher, DataOutputStream dataOutputStream) {
        int money = Integer.parseInt(matcher.group("money"));
        if (currentCustomer == null) {
            writeInBuffer(dataOutputStream, "there is no logged in user!");
            return;
        }

        try {
            chargeCustomer(money, dataOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void moneyChecker(Matcher matcher, DataOutputStream dataOutputStream) {
        if (currentCustomer == null) {
            writeInBuffer(dataOutputStream, "there is no current user!");
            return;
        }
        Integer money = currentCustomer.getMoney();
        writeInBuffer(dataOutputStream, money.toString());
    }

    private void quantityChecker(Matcher matcher, DataOutputStream dataOutputStream) {
        String shoeName = matcher.group("shoeName");
        Integer quantity = getQuantity(shoeName);
        if (getQuantity(shoeName) == -1) {
            writeInBuffer(dataOutputStream, "there is no product with this name!");
        } else {
            writeInBuffer(dataOutputStream, quantity.toString());
        }
    }

    private void priceChecker(Matcher matcher, DataOutputStream dataOutputStream) {
        String shoeName = matcher.group("shoeName");
        Integer price = getPrice(shoeName);
        String priceStr = price.toString();
        if (price == -1) {
            writeInBuffer(dataOutputStream, "there is no product with this name!");
        } else {
            writeInBuffer(dataOutputStream, priceStr);
        }
    }

    private void registerChecker(Matcher matcher, DataOutputStream dataOutputStream) {
        String id = matcher.group("id");
        String name = matcher.group("name");
        int money = Integer.parseInt(matcher.group("money"));
        register(name, id, money, dataOutputStream);
    }

    private Matcher getMatcher(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }

    private void register(String name, String id, int money, DataOutputStream dataOutputStream) {
        if (!isValidId(id)) {
            writeInBuffer(dataOutputStream, "this id has been already taken!");
            return;
        }
        Customer customer = new Customer(name, id, money);
        customers.put(id, customer);
        writeInBuffer(dataOutputStream, "register successful!");
    }

    private void login(Matcher matcher, DataOutputStream dataOutputStream) {
        String id = matcher.group("id");
        Customer customer = getCustomerById(id);
        if (customer == null) {
            writeInBuffer(dataOutputStream, "this id does not exist!");
            return;
        }
        if (currentCustomer != null) {
            writeInBuffer(dataOutputStream, "you should logout first to be able to login as another account!");
        }
        currentCustomer = customer;
        writeInBuffer(dataOutputStream, "login successful!");
    }

    private Customer getCustomerById(String id) {
        for (Map.Entry<String, Customer> entry : customers.entrySet()) {
            if (entry.getKey().equals(id)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private void logout(DataOutputStream dataOutputStream) {
        if (currentCustomer == null) {
            writeInBuffer(dataOutputStream, "there is no registered account to be logged out!");
            return;
        }
        currentCustomer = null;
        writeInBuffer(dataOutputStream, "logged out successfully!");
    }

    private boolean isValidId(String id) {
        for (Map.Entry<String, Customer> customerEntry : customers.entrySet()) {
            if (customerEntry.getKey().equals(id)) {
                return false;
            }
        }
        return true;
    }

    private void writeInBuffer(DataOutputStream dataOutputStream, String data) {
        try {
            dataOutputStream.writeUTF(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidMoney(int money) {
        if (currentCustomer.getMoney() >= money) {
            return true;
        }
        return false;
    }

    private boolean isValidProductName(String productName) {
        if (productName.equals("shoe1")) return true;
        if (productName.equals("shoe2")) return true;
        if (productName.equals("shoe3")) return true;
        return false;
    }

    private boolean isValidQuantity(int quantity, String productName) {
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            if (entry.getKey().equals(productName)) {
                if (quantity > entry.getValue()) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    private void chargeCustomer(int chargeAmount, DataOutputStream dataOutputStream) throws IOException {
        currentCustomer.setMoney(currentCustomer.getMoney() + chargeAmount);
        writeInBuffer(dataOutputStream, "process successful");
    }

    private int getPrice(String productName) {
        if (!isValidProductName(productName)) {
            return -1;
        }
        if (productName.equals("shoe1")) return 5;
        if (productName.equals("shoe2")) return 10;
        if (productName.equals("shoe3")) return 15;
        return -1;
    }

    private int getQuantity(String productName) {
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            if (entry.getKey().equals(productName)) return entry.getValue();
        }
        return -1;
    }

    private void purchaseProduct(String productName, int quantity, DataOutputStream dataOutputStream) throws IOException {
        synchronized (this) {
            if (!isValidProductName(productName)) {
                writeInBuffer(dataOutputStream, "this product does not exist!");
                return;
            }
            int totalPrice = getPrice(productName) * quantity;
            if (!isValidMoney(totalPrice)) {
                writeInBuffer(dataOutputStream, "you don't have enough money!");
                return;
            }
            if (!isValidQuantity(quantity, productName)) {
                writeInBuffer(dataOutputStream, "there is not enough products in inventory!");
                return;
            }
            currentCustomer.setMoney(currentCustomer.getMoney() - totalPrice);
            int inventoryQuantity = -1;
            for (Map.Entry<String, Integer> map : inventory.entrySet()) {
                if (map.getKey().equals(productName)) {
                    inventoryQuantity = map.getValue() - quantity;
                }
            }
            inventory.put(productName, inventoryQuantity);
            writeInBuffer(dataOutputStream, "purchased successfully!");
        }
    }

    public static void main(String[] args) {
        inventory.put("shoe1", 5);
        inventory.put("shoe2", 5);
        inventory.put("shoe3", 5);

        try {
            ServerSocket serverSocket = new ServerSocket(5001);
            Socket socket;
            while (true) {
                socket = serverSocket.accept();
                StoreServer storeServer = new StoreServer(socket);
                storeServer.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

class Customer {
    private String name;
    private String id;
    private int money;

    public Customer(String name, String id, int money) {
        this.name = name;
        this.id = id;
        this.money = money;
    }


    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}