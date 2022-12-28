package data

import data.model.Med
import data.model.Pharm
import java.sql.DriverManager

object MysqlConnector {
    private val connection = DriverManager
        .getConnection("jdbc:mysql://localhost", "ui-connector", "123456789")

    init {
        connection.prepareStatement("use pharmacy;").execute()
    }

    fun getMeds(): List<Med> {
        val query = "select * from med"
        val statement = connection.createStatement()
        val result = statement.executeQuery(query)
        val meds = mutableListOf<Med>()
        while (result.next()) {
            meds.add(Med(result))
        }
        return meds
    }

    fun insertMed(med: Med) {
        val query = "insert into med values (${med.values()})"
        val statement = connection.createStatement()
        statement.execute(query)
    }

    fun deleteMed(medId: Int) {
        val query = "delete from med where id=$medId"
        val statement = connection.createStatement()
        statement.execute(query)
    }

    fun updateMed(med: Med) {
        val query = "update med set ${med.parametricValues()} where id=${med.id}"
        val statement = connection.createStatement()
        statement.execute(query)
    }

    fun getPharms(): List<Pharm> {
            val query = "select * from pharm"
            val statement = connection.createStatement()
            val result = statement.executeQuery(query)
            val pharms = mutableListOf<Pharm>()
            while (result.next()) {
                pharms.add(Pharm(result))
            }
            return pharms
        }

        fun insertPharm(pharm: Pharm) {
            val query = "insert into pharm values (${pharm.values()})"
            val statement = connection.createStatement()
            statement.execute(query)
        }

        fun deletePharm(pharmId: Int) {
            val query = "delete from pharm where id=$pharmId"
            val statement = connection.createStatement()
            statement.execute(query)
        }

        fun updatePharm(pharm: Pharm) {
            val query = "update pharm set ${pharm.parametricValues()} where id=${pharm.id}"
            val statement = connection.createStatement()
            statement.execute(query)
        }
}