package splitwise.expense.api

import java.time.Instant

// Database and other services should interact with this one via api models, so that there is no
// strong linkage, service model should not be aware of db

data class User(
    val userID: String,
    val name: String,
    val email: String,
    val phone: Int
)

data class Transaction(
    val transactionID: String,
    val amount: Double,
    val createdOn: Instant,
    val createdBy: String,
)

data class TransactionPerUser(
    val transactionPerUserID: String,
    val transactionID: String,
    val amountDue: Double,
    var paidBy: String,
    val paidFor: String,
    var state: TransactionPerUserState,
    var updatedBy: String?
)

enum class TransactionPerUserState {
    settled,
    unsettled
}