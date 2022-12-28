package data

import data.model.Category
import data.model.Company
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

    fun getCategories(): List<Category> {
        val query = "select * from category"
        val statement = connection.createStatement()
        val result = statement.executeQuery(query)
        val categorys = mutableListOf<Category>()
        while (result.next()) {
            categorys.add(Category(result))
        }
        return categorys
    }

    fun insertCategory(category: Category) {
        val query = "insert into category values (${category.values()})"
        val statement = connection.createStatement()
        statement.execute(query)
    }

    fun deleteCategory(categoryId: Int) {
        val query = "delete from category where id=$categoryId"
        val statement = connection.createStatement()
        statement.execute(query)
    }

    fun updateCategory(category: Category) {
        val query = "update category set ${category.parametricValues()} where id=${category.id}"
        val statement = connection.createStatement()
        statement.execute(query)
    }

    fun getCompanies(): List<Company> {
        val query = "select * from company"
        val statement = connection.createStatement()
        val result = statement.executeQuery(query)
        val companies = mutableListOf<Company>()
        while (result.next()) {
            companies.add(Company(result))
        }
        return companies
    }

    fun insertCompany(company: Company) {
        val query = "insert into company values (${company.values()})"
        val statement = connection.createStatement()
        statement.execute(query)
    }

    fun deleteCompany(companyId: Int) {
        val query = "delete from company where id=${companyId}"
        val statement = connection.createStatement()
        statement.execute(query)
    }

    fun updateCompany(company: Company) {
        val query = "update company set ${company.parametricValues()} where id=${company.id}"
        val statement = connection.createStatement()
        statement.execute(query)
    }
}