package com.example.simplechat.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.springframework.transaction.ReactiveTransaction
import org.springframework.transaction.reactive.TransactionContextManager
import org.springframework.transaction.reactive.TransactionalOperator

suspend fun <T : Any> TransactionalOperator.executeAndAwaitNotNull(block: suspend (ReactiveTransaction) -> T): T =
    execute { transaction ->
        mono(Dispatchers.Unconfined) { block(transaction) }
    }.awaitSingle()

suspend fun <T> transactionRequired(block: suspend () -> T): T {
    TransactionContextManager.currentContext().awaitSingle()
    return block()
}

//suspend fun <T> withTransaction(block: suspend (transaction: TransactionContext) -> T): T {
//    val transaction = TransactionContextManager.currentContext().awaitSingle()
//    return block(transaction)
//}