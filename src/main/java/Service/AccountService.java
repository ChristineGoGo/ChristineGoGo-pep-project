package Service;
import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    AccountDAO accountDAO;


    /**
     * no-args constructor for a flightService that instantiates a plain accountDAO
     */
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    /**
     * constructor for an accountService when a flightDAO is provided
     * this would allow for testing of AccountService independently of AccountDAO
     * @param accountDAO
     */
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }


    /** use accountDAO to add a user to the db
     * @param account
     * @return newly created account user
     */
    public Account addAccount(Account account) {
        Account newAccount = accountDAO.addAccount(account);
        Integer generatedAccountId = newAccount == null ? 0 : newAccount.getAccount_id();

        if (generatedAccountId > 0) {
            return newAccount;
        }
        return null;
    }
    
    /**
     * use accountDAO to verify a user/login
     * @param username
     * @param password
     * @param account
     * @return account
     */
    public Account getAccount(String username,String password) {
        Account matchedAccount = accountDAO.getAccount(username, password);
        int account_id = matchedAccount == null ? 0 : matchedAccount.getAccount_id();
        if (account_id > 0) {
            return matchedAccount;
        }
        return null;
    }

    /**
     * obtain an account using its account_id through the AccountDAO
     * @param account_id
     * @return account 
     */
    public Account getAccountById(int account_id) {
        Account matchedAccount = accountDAO.getAccountById(account_id);
        int matchedAccountId = matchedAccount == null ? 0 : matchedAccount.getAccount_id();
        if (matchedAccountId > 0) {
            return matchedAccount;
        }
        return null;
    }
}
