package data

import data.model.*
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
        val categories = mutableListOf<Category>()
        while (result.next()) {
            categories.add(Category(result))
        }
        return categories
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

    fun getPatients(): List<Patient> {
        val query = "select * from patient"
        val statement = connection.createStatement()
        val result = statement.executeQuery(query)
        val patients = mutableListOf<Patient>()
        while (result.next()) {
            patients.add(Patient(result))
        }
        return patients
    }

    fun insertPatient(patient: Patient) {
        val query = "insert into patient values (${patient.values()})"
        val statement = connection.createStatement()
        statement.execute(query)
    }

    fun deletePatient(patientId: Int) {
        val query = "delete from patient where id=${patientId}"
        val statement = connection.createStatement()
        statement.execute(query)
    }

    fun updatePatient(patient: Patient) {
        val query = "update patient set ${patient.parametricValues()} where id=${patient.id}"
        val statement = connection.createStatement()
        statement.execute(query)
    }

    fun getPrescriptions(): List<Prescription> {
        val query = "select * from prescription"
        val statement = connection.createStatement()
        val result = statement.executeQuery(query)
        val prescriptions = mutableListOf<Prescription>()
        while (result.next()) {
            prescriptions.add(Prescription(result))
        }
        return prescriptions
    }

    fun insertPrescription(prescription: Prescription) {
        val query = "insert into prescription values (${prescription.values()})"
        val statement = connection.createStatement()
        statement.execute(query)
    }

    fun deletePrescription(prescriptionId: Int) {
        val query = "delete from prescription where id=${prescriptionId}"
        val statement = connection.createStatement()
        statement.execute(query)
    }

    fun updatePrescription(prescription: Prescription) {
        val query = "update prescription set ${prescription.parametricValues()} where id=${prescription.id}"
        val statement = connection.createStatement()
        statement.execute(query)
    }

    fun getDoctors(): List<Doctor> {
        val query = "select * from doctor"
        val statement = connection.createStatement()
        val result = statement.executeQuery(query)
        val doctors = mutableListOf<Doctor>()
        while (result.next()) {
            doctors.add(Doctor(result))
        }
        return doctors
    }

    fun insertDoctor(doctor: Doctor) {
        val query = "insert into doctor values (${doctor.values()})"
        val statement = connection.createStatement()
        statement.execute(query)
    }

    fun deleteDoctor(doctorId: Int) {
        val query = "delete from doctor where id=${doctorId}"
        val statement = connection.createStatement()
        statement.execute(query)
    }

    fun updateDoctor(doctor: Doctor) {
        val query = "update doctor set ${doctor.parametricValues()} where id=${doctor.id}"
        val statement = connection.createStatement()
        statement.execute(query)
    }
}