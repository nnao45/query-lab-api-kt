package com.nnao45.migration

import com.nnao45.plugins.configureDDL
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    configureDDL()
}