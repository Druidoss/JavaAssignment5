/**
 * The Bank class represents a collection of customer bank accounts.
 * It uses a LinkedBlockingQueue to maintain a collection of bank
 * account transactions to be processed.
 *
 * @Daniel_Szuflicki
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Bank {
    private Map<Integer, Account> bankAccounts;
    private LinkedBlockingQueue<Transaction> transactionQueue;

    /**
     * Constructs a Bank object with an empty collection of bank accounts
     * and initializes the transaction queue.
     */
    public Bank() {
        this.bankAccounts = new HashMap<>();
        this.transactionQueue = new LinkedBlockingQueue<>();
    }

    /**
     * Adds an account to the bank's collection.
     *
     * @param account The Account object to be added to the bank.
     */
    public void addAccount(Account account) {
        bankAccounts.put(account.getAccountNumber(), account);
    }

    /**
     * Retrieves an account based on its account number.
     *
     * @param id The account number used to identify the account.
     * @return The Account object associated with the specified account number.
     */
    public Account getAccount(int id) {
        return bankAccounts.get(id);
    }

    /**
     * Submits a transaction to the bank's transaction queue for processing.
     * The method is synchronized to handle concurrent submissions.
     *
     * @param transaction The Transaction object to be processed.
     */
    public synchronized void submitTransaction(Transaction transaction) {
        transactionQueue.add(transaction);
    }

    /**
     * Retrieves the next transaction from the bank's transaction queue.
     * This method waits 5 seconds unless a transaction is available.
     *
     * @return The next Transaction to be processed.
     */
    public Transaction getNextTransaction() throws InterruptedException {
        return transactionQueue.poll(5, TimeUnit.SECONDS);
    }

    /**
     * Prints the details of an account.
     *
     * @param account The Account whose details will be printed.
     */
    public void printAccountDetails(Account account) {
        System.out.println(account);
    }

    /**
     * Retrieves a collection of account numbers related to the bank's accounts.
     *
     * @return A List containing the account numbers of all accounts.
     */
    public List<Integer> getAccountNumbers() {
        return new ArrayList<>(bankAccounts.keySet());
    }
}
