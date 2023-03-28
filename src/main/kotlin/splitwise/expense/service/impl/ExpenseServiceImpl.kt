package splitwise.expense.service.impl

import splitwise.expense.api.Transaction
import splitwise.expense.api.TransactionPerUser
import splitwise.expense.api.TransactionPerUserState
import splitwise.expense.repository.ExpenseRepository
import splitwise.expense.service.ExpenseService
import java.time.Instant
import java.util.Date
import java.util.Random

class ExpenseServiceImpl(
    private val expenseRepository: ExpenseRepository
) : ExpenseService {
    override fun addExpense(
        amount: Double, paidBy: List<String>,
        paidFor: List<String>, createdBy: String
    ): String {
        // also need to check if person creating transaction is in it
        if (amount < 0) throw IllegalArgumentException("please enter positive amount")
        val participants = paidFor + paidBy
        val check = participants.filter { it == createdBy }
        if (check.isEmpty()) throw IllegalArgumentException("Can only enter transactions you are a part of ")

        val transaction = createTransaction(amount, createdBy)
        expenseRepository.createTransaction(transaction)

        val duePerPerson = amount / (paidFor.size * paidBy.size)

        for (payBy in paidBy) {
            for (payFor in paidFor) {
                val transactionPerUser = TransactionPerUser(
                    transactionPerUserID = getPublicId(),
                    transactionID = transaction.transactionID,
                    amountDue = duePerPerson,
                    paidBy = payBy,
                    paidFor = payFor,
                    state = TransactionPerUserState.unsettled,
                    updatedBy = createdBy
                )
                expenseRepository.createTransactionPerUser(transactionPerUser)
            }
        }

        return transaction.transactionID
    }

    override fun getUnsettledTransactions(
        currUser: String,
        targetUser: String
    ): List<TransactionPerUser> {
        return expenseRepository.findTransactionsByStateBetween(
            currUser,
            targetUser,
            TransactionPerUserState.unsettled
        )
    }

    override fun getUnsettledBalance(currUser: String, targetUser: String): Double {
        return getUnsettledBetween(currUser, targetUser)
    }

    override fun settleUp(amount: Double, paidBy: String, paidTo: String, createdBy: String): String {
        // check if settling up is valid
        if (amount < 0) throw IllegalArgumentException("please enter positive amount")
        val currUser = if (paidBy == createdBy) paidBy else paidTo
        val targetUser = if (paidBy != createdBy) paidBy else paidTo
        val currUserDue = getUnsettledBalance(currUser, targetUser)

        if (currUserDue < 0 && paidBy == currUser) throw IllegalArgumentException("Current user cannot pay dues")
        if (currUserDue > 0 && paidBy == targetUser) throw IllegalArgumentException("Current user cannot charge dues")

        val settleTransaction = createTransaction(amount, currUser)
        val transactionPerUser = TransactionPerUser(
            transactionPerUserID = getPublicId(),
            transactionID = settleTransaction.transactionID,
            amountDue = amount,
            paidBy = paidBy,
            paidFor = paidTo,
            state = TransactionPerUserState.unsettled,
            updatedBy = createdBy
        )
        expenseRepository.createTransaction(settleTransaction)
        expenseRepository.createTransactionPerUser(transactionPerUser)
        if (amount == kotlin.math.abs(currUserDue)) {
            getUnsettledTransactions(paidBy, paidTo).map {
                expenseRepository.updateTransactionPerUser(
                    it.transactionPerUserID,
                    createdBy,
                    TransactionPerUserState.settled
                )
            }
        }

        return settleTransaction.transactionID
    }

    override fun getAllBalances(forUser: String): Map<String, Double> {
        val unsettledTransactions = expenseRepository.findAllUnsettledTransactionsFor(forUser)

        val map = HashMap<String, Double>()

        unsettledTransactions.map {
            val targetUser = if (it.paidBy == forUser) it.paidFor else it.paidBy
            val amountWithSign = if (it.paidBy == forUser) it.amountDue * (-1) else it.amountDue

            val currBalance = map.getOrDefault(targetUser, 0.0)
            map.put(targetUser, amountWithSign + currBalance)
        }

        return map
    }

    private fun createTransaction(amount: Double, createdBy: String): Transaction {
        return Transaction(
            transactionID = getPublicId(),
            amount = amount,
            createdOn = getInstant(),
            createdBy = createdBy
        )
    }

    private fun getUnsettledBetween(currUser: String, targetUser: String): Double {

        val unsettledTransaction =
            expenseRepository.findTransactionsByStateBetween(
                currUser,
                targetUser,
                TransactionPerUserState.unsettled
            )

        var currUserDue = 0.0
        unsettledTransaction.map {
            if (it.paidBy == currUser) {
                currUserDue -= it.amountDue
            } else {
                currUserDue += it.amountDue
            }
        }
        // if this is negative, target owes them money, else they owe target money
        return currUserDue
    }

    private fun getPublicId(): String {
        return Random().nextInt().toString()
    }

    private fun getInstant(): Instant {
        return Date().toInstant()
    }
}