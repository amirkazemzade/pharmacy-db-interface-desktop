package data

import java.sql.DriverManager

object MysqlConnector {
    private val connection = DriverManager
        .getConnection("jdbc:mysql://localhost", "ui-connector", "123456789")

    init {
        connection.prepareStatement("use pharmacy;").execute()
    }

    fun medCount(): Int {
        val query = "select count(*) from med"
        val statement = connection.createStatement()
        val result = statement.executeQuery(query)
        if (result.next()) {
            return result.getInt(1)
        }
        return 0
    }
}