package splitwise.expense.repository.impl

import splitwise.expense.api.Transaction
import splitwise.expense.api.TransactionPerUser
import splitwise.expense.api.TransactionPerUserState
import splitwise.expense.repository.ExpenseRepository

class ExpenseRepositoryImpl:ExpenseRepository {

    private val transactionMap=HashMap<String,Transaction>()
    private val transactionPerUserMap=HashMap<String,TransactionPerUser>()

    override fun createTransaction(transaction: Transaction): String {

        transactionMap[transaction.transactionID] = transaction
        return transaction.transactionID
    }

    override fun createTransactionPerUser(transactionPerUser: TransactionPerUser): String {
        transactionPerUserMap[transactionPerUser.transactionPerUserID]=transactionPerUser
        return transactionPerUser.transactionPerUserID
    }

    override fun updateTransactionPerUser(
        transactionPerUserId: String,
        updatedByUserId: String,
        state: TransactionPerUserState
    ): String {
        val transactionPerUser= transactionPerUserMap[transactionPerUserId]
            ?: throw NoSuchElementException("This transaction does not exist")
        transactionPerUser.state=state
        transactionPerUser.updatedBy=updatedByUserId
        transactionPerUserMap[transactionPerUserId]=transactionPerUser
        return transactionPerUserId
    }

    override fun findTransactionsByStateBetween(
        currUser: String,
        targetUser: String,
        state: TransactionPerUserState
    ): List<TransactionPerUser> {
        return transactionPerUserMap.values.filter {
            it.state==state
        }.filter { it->
            (it.paidBy == currUser && it.paidFor == targetUser)
                || it.paidBy == targetUser && it.paidFor == currUser
        }
    }

    override fun findAllUnsettledTransactionsFor(user: String): List<TransactionPerUser> {
        return transactionPerUserMap.values.filter {
            (it.paidBy == user || it.paidFor == user)
        }
    }
}