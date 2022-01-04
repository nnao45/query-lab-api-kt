package com.nnao45.infra.repo

object FibonacchiRepoImpl {
    fun exec(number: Long): Long {
        var f0 = 0L
        var f1 = 1L
        var fn = 0L
        for (i in 2..number) {
            fn = f0 + f1;
            f0 = f1
            f1 = fn
        }
        return fn
    }
}