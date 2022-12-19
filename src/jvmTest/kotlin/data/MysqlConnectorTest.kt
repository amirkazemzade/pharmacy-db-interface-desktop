package data

import data.model.Med
import org.junit.Test
import java.sql.Date
import kotlin.test.assertEquals
import kotlin.test.assertTrue


internal class MysqlConnectorTest {

    @Test
    fun getMeds() {
        MysqlConnector.getMeds()
    }

    @Test
    fun insertMed() {
        val insertingMed = Med(
            id = 0,
            pharmId = 1,
            compId = 2,
            inv = 50,
            price = 100,
            expirationDate = Date(System.currentTimeMillis()),
            medName = "testName"
        )
        MysqlConnector.insertMed(insertingMed)
        val meds = MysqlConnector.getMeds()
        val isInserted = meds.any { it.parametricValues() == insertingMed.parametricValues() }
        assertTrue(isInserted)
    }

    @Test
    fun deleteMed() {
        var meds = MysqlConnector.getMeds()
        val deletingMed = meds.last()
        MysqlConnector.deleteMed(deletingMed.id)
        meds = MysqlConnector.getMeds()
        val hasDeleted = meds.none { it.id == deletingMed.id }
        assertTrue(hasDeleted)
    }

    @Test
    fun updateMed() {
        var meds = MysqlConnector.getMeds()
        val updatingMed = meds.last().copy(
            pharmId = 2,
            compId = 1,
            inv = 100,
            price = 50,
            expirationDate = Date(System.currentTimeMillis()),
            medName = "newTestName"
        )
        MysqlConnector.updateMed(updatingMed)
        meds = MysqlConnector.getMeds()
        val updatedMed = meds.find { it.id == updatingMed.id }
        assertTrue(updatedMed != null)
        assertEquals(updatedMed.parametricValues(), updatingMed.parametricValues())
    }
}