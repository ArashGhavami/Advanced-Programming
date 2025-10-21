import java.util.ArrayList;

public class Bank {

    static private ArrayList<Bank> allBanks = new ArrayList<>();
    private String name;

    public Bank(String name) {
        this.name = name;
    }

    public static Bank getBankWithName(String name) {
        for (Bank bank : allBanks) {
            if (bank.name.equals(name)) {
                return bank;
            }
        }
        return null;
    }

    public static boolean isThereBankWithName(String name) {
        for (Bank bank : allBanks) {
            if (bank.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static int getAccountInterestFromName(String type) {
        if (type.equals("KOOTAH"))
            return 10;
        if (type.equals("BOLAN"))
            return 30;
        if (type.equals("VIZHE"))
            return 50;
        else
            return -1;
    }

    public String getName() {
        return this.name;
    }

    public static void addBankToAllBanks(Bank bank) {
        Bank.allBanks.add(bank);
    }

    public static void showAllBanks() {
        for (Bank bank : allBanks) {
            System.out.println(bank.name);
        }
    }
}
