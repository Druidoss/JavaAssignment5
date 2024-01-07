/**
 * The RandomTransactionGenerator class represents a thread that generates
 * random deposit and withdrawal transactions and submits them to a bank's
 * transaction queue for processing. The thread terminates
 * by submitting a "poison-pill" transaction to indicate closure.
 *
 * @Daniel_Szuflicki
 */

import java.util.concurrent.ThreadLocalRandom;

public class RandomTransactionGenerator extends Thread {
    private final String name;
    private final Bank bank;
    private static final int numOfTransactions = 10;

    /**
     * Constructs a new RandomTransactionGenerator object with bank and name.
     *
     * @param bank The Bank instance associated with the transaction generator.
     * @param name The name of the random transaction generator thread.
     */
    public RandomTransactionGenerator(Bank bank, String name) {
        this.name = name;
        this.bank = bank;
    }

    /**
     * Runs the random transaction generator thread. Generates random
     * transactions, submits them to the bank's transaction queue, and sleeps between
     * transactions. Terminates by submitting a "poison-pill" transaction.
     */
    @Override
    public void run() {
        try {
            for (int i = 0; i < numOfTransactions; i++) {
                generateAndSubmitRandomTransaction();
                Thread.sleep(ThreadLocalRandom.current().nextLong(1000));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.printf("Thread %s was interrupted!\n", name);
        } finally {
            bank.submitTransaction(new Transaction(0, 0));
        }
    }

    /**
     * Generates a random account number and amount, creates a transaction, and submits
     * it to the bank's transaction queue.
     */
    private void generateAndSubmitRandomTransaction() {
        int randomAccountNumber = getRandomAccountNumber();
        float randomAmount = getRandomTransactionAmount();
        Transaction transaction = new Transaction(randomAccountNumber, randomAmount);
        bank.submitTransaction(transaction);
    }

    /**
     * Gets a random account number.
     *
     * @return A random account number.
     */
    private int getRandomAccountNumber() {
        int randomIndex = ThreadLocalRandom.current().nextInt(0, bank.getAccountNumbers().size());
        return bank.getAccountNumbers().get(randomIndex);
    }

    /**
     * Generates a random transaction amount between -10000 and 10000.
     *
     * @return A randomly generated transaction amount.
     */
    private float getRandomTransactionAmount() {
        return ThreadLocalRandom.current().nextFloat(-10000.00f, 10000.00f);
    }
}
