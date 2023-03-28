package splitwise.expense.repository

import splitwise.expense.api.Transaction
import splitwise.expense.api.TransactionPerUser
import splitwise.expense.api.TransactionPerUserState

interface ExpenseRepository {

    fun createTransaction(transaction: Transaction): String

    fun createTransactionPerUser(transactionPerUser: TransactionPerUser): String

    fun updateTransactionPerUser(
        transactionPerUserId: String, updatedByUserId: String,
        state: TransactionPerUserState
    ): String

    fun findTransactionsByStateBetween(
        currUser: String,
        targetUser: String,
        state:TransactionPerUserState
    ): List<TransactionPerUser>

    fun findAllUnsettledTransactionsFor(user: String): List<TransactionPerUser>
}