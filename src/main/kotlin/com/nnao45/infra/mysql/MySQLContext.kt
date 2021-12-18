package com.nnao45.infra.mysql

import org.jetbrains.exposed.sql.Database

fun initMysqlClient() {
    val mysqlUser = System.getenv("MYSQL_USER") ?: "admin"
    val mysqlPassword = System.getenv("MYSQL_PASSWORD") ?: "mypwds"
    val mysqlHost = System.getenv("MYSQL_HOST") ?: "127.0.0.1"
    val mysqlDB = System.getenv("MYSQL_DB") ?: "jq_api"
    val mysqlPort = System.getenv("MYSQL_PORT") ?: "3306"
    val mysqlOpts = System.getenv("MYSQL_OPTS") ?: "useSSL=false&loc=Asia%2FTokyo"
    Database.connect("jdbc:mysql://$mysqlHost:$mysqlPort/$mysqlDB?$mysqlOpts", "com.mysql.cj.jdbc.Driver", mysqlUser, mysqlPassword)
}