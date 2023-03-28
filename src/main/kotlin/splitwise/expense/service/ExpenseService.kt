package splitwise.expense.service

import splitwise.expense.api.Transaction
import splitwise.expense.api.TransactionPerUser
import splitwise.expense.api.TransactionPerUserState

interface ExpenseService {

    /**
     * creates two entries, one [Transaction], and one [TransactionPerUser]
     * can also potentially update total balance between users
     */
    fun addExpense(
        amount: Double,

        paidBy: List<String>,
        paidFor: List<String>,
        createdBy: String
    ): String

    /**
     * get all [TransactionPerUser] where state is [TransactionPerUserState.unsettled]
     */
    fun getUnsettledTransactions(currUser: String, targetUser: String): List<TransactionPerUser>

    /**
     * Returns total balance between two users
     */
    fun getUnsettledBalance(currUser: String, targetUser: String): Double

    /**
     * creates new [Transaction], and settles [TransactionPerUser] if amount is equal to balance
     */
    fun settleUp(amount: Double, paidBy: String, paidTo: String,createdBy: String): String

    /**
     * Gets all pending balance for user
     * */
    fun getAllBalances(forUser: String): Map<String, Double>
}