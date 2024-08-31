import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class BasicStockTradingPlatform {
    private static final Map<String, Double> marketData = new HashMap<>();
    private static final Map<String, Integer> portfolio = new HashMap<>();
    private static double balance = 1000000.0; // Starting balance in Rupees
    private static final Random random = new Random();

    public static void main(String[] args) {
        // Initialize some stocks
        marketData.put("REL", 12500.0); // AAPL price in Rupees
        marketData.put("GOOG", 231000.0); // GOOG price in Rupees
        marketData.put("AMZN", 278000.0); // AMZN price in Rupees

        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Update stock prices randomly
            updatePrices();

            // Display available stocks
            System.out.println("Available Stocks:");
            for (Map.Entry<String, Double> entry : marketData.entrySet()) {
                System.out.println(entry.getKey() + ": ₹" + String.format("%.2f", entry.getValue()));
            }

            // Display portfolio
            displayPortfolio();

            // Command prompt
            System.out.println("\nEnter command (buy, sell, exit):");
            String command = scanner.nextLine();

            switch (command.toLowerCase()) {
                case "buy":
                    handleBuy(scanner);
                    break;
                case "sell":
                    handleSell(scanner);
                    break;
                case "exit":
                    System.out.println("Exiting platform.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid command.");
            }
        }
    }

    // Method to update stock prices randomly
    private static void updatePrices() {
        for (String symbol : marketData.keySet()) {
            double price = marketData.get(symbol);
            double change = (random.nextDouble() - 0.5) * 500; // Random change between -250 to 250 Rupees
            marketData.put(symbol, price + change);
        }
    }

    // Method to handle buying stocks
    private static void handleBuy(Scanner scanner) {
        System.out.println("Enter stock symbol and quantity to buy:");
        String[] input = scanner.nextLine().split(" ");
        String symbol = input[0];
        int quantity = Integer.parseInt(input[1]);

        if (!marketData.containsKey(symbol)) {
            System.out.println("Invalid stock symbol.");
            return;
        }

        double price = marketData.get(symbol);
        double cost = price * quantity;

        if (cost > balance) {
            System.out.println("Insufficient funds to buy " + quantity + " shares of " + symbol);
        } else {
            balance -= cost;
            portfolio.put(symbol, portfolio.getOrDefault(symbol, 0) + quantity);
            System.out.println("Bought " + quantity + " shares of " + symbol);
        }
    }

    // Method to handle selling stocks
    private static void handleSell(Scanner scanner) {
        System.out.println("Enter stock symbol and quantity to sell:");
        String[] input = scanner.nextLine().split(" ");
        String symbol = input[0];
        int quantity = Integer.parseInt(input[1]);

        if (!portfolio.containsKey(symbol) || portfolio.get(symbol) < quantity) {
            System.out.println("Not enough shares to sell.");
            return;
        }

        double price = marketData.get(symbol);
        double proceeds = price * quantity;
        balance += proceeds;
        portfolio.put(symbol, portfolio.get(symbol) - quantity);

        if (portfolio.get(symbol) == 0) {
            portfolio.remove(symbol);
        }

        System.out.println("Sold " + quantity + " shares of " + symbol);
    }

    // Method to display the portfolio
    private static void displayPortfolio() {
        System.out.println("\nCurrent Portfolio:");
        for (Map.Entry<String, Integer> entry : portfolio.entrySet()) {
            String symbol = entry.getKey();
            int quantity = entry.getValue();
            double price = marketData.get(symbol);
            System.out.println(symbol + " - " + quantity + " shares @ ₹" + String.format("%.2f", price));
        }
        System.out.println("Balance: ₹" + String.format("%.2f", balance));
    }
}
