package main.connection;

import main.ui.CommonMethods;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.SelectionQuery;

import java.time.LocalDate;
import java.util.List;

public class DBConnection {
    private SessionFactory sf;
    private Session session;
    public DBConnection() {

    }
    public boolean openSession() {
        try {
            sf = new Configuration().configure().buildSessionFactory();
            session = sf.openSession();
            java.util.logging.Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.SEVERE);

        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public boolean close() {
        try {
            session.close();
            sf.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void refresh(Object obj) {
        session.refresh(obj);
    }
    public void flush() {
        session.flush();
    }
    public Patient getPatient(String dni) {
        return session.find(Patient.class,dni);
    }
    public Patient getPatient(String dni, String password) {
        SelectionQuery<Patient> query = session.createSelectionQuery("from Patient where dni = :dni and password = :password", Patient.class);
        query.setParameter("dni", dni);
        query.setParameter("password", password);
        return query.uniqueResult();
    }
    public Doctor getDoctor(String dni) {
        return session.find(Doctor.class,dni);
    }
    public Specialty getSpecialty(int id) {
        return session.find(Specialty.class,id);
    }
    public Appointment getAppointment(int id) {
        return session.find(Appointment.class,id);
    }
    public Visit getVisit(int id) {
        return session.find(Visit.class,id);
    }
    public Prescription getPrescription(int id) {
        return session.find(Prescription.class,id);
    }
    public Diagnosis getDiagnosis(int id) {
        return session.find(Diagnosis.class,id);
    }
    public BloodCountTest getBloodCountTest(int id) {
        return session.find(BloodCountTest.class,id);
    }
    public BloodCultureTest getBloodCultureTest(int id) {
        return session.find(BloodCultureTest.class,id);
    }
    public AntibodyTest getAntibodyTest(int id) {
        return session.find(AntibodyTest.class,id);
    }
    public OtherTest getOtherTest(int id) {
        return session.find(OtherTest.class,id);
    }

    public List<Appointment> getFutureAppointmentsByPatient(Patient patient) {
        SelectionQuery<Appointment> query = session.createSelectionQuery("from Appointment where patient = :patient  and date > current_date order by date asc", Appointment.class);
        query.setParameter("patient", patient);
        return query.getResultList();
    }
    public List<Appointment> getPastAppointmentsByPatient(Patient patient) {
        SelectionQuery<Appointment> query = session.createSelectionQuery("from Appointment where patient = :patient and date < current_date  order by date desc", Appointment.class);
        query.setParameter("patient", patient);
        return query.getResultList();
    }
    public List<Visit> getVisitsByPatient(Patient patient) {
        SelectionQuery<Visit> query = session.createSelectionQuery("from Visit where patient = :patient order by date desc", Visit.class);
        query.setParameter("patient", patient);
        return query.getResultList();
    }
    public List<Prescription> getPrescriptionsByPatient(Patient patient) {
        SelectionQuery<Prescription> query = session.createSelectionQuery("from Prescription where patient = :patient order by start desc", Prescription.class);
        query.setParameter("patient", patient);
        return query.getResultList();
    }
    public List<Prescription> getPrescriptionsByPatient(Patient patient, String description) {
        SelectionQuery<Prescription> query = session.createSelectionQuery("from Prescription where patient = :patient and lower(description) like :description order by start desc", Prescription.class);
        query.setParameter("patient", patient);
        query.setParameter("description", "%" + description.toLowerCase() + "%");
        return query.getResultList();
    }
    public List<Diagnosis> getDiagnosisByPatient(Patient patient) {
        SelectionQuery<Diagnosis> query = session.createSelectionQuery("from Diagnosis where patient = :patient order by start desc", Diagnosis.class);
        query.setParameter("patient", patient);
        return query.getResultList();
    }
    public List<BloodCountTest> getBloodCountTestsByPatient(Patient patient) {
        SelectionQuery<BloodCountTest> query = session.createSelectionQuery("from BloodCountTest where patient = :patient", BloodCountTest.class);
        query.setParameter("patient", patient);
        return query.getResultList();
    }
    public List<BloodCultureTest> getBloodCultureTestsByPatient(Patient patient) {
        SelectionQuery<BloodCultureTest> query = session.createSelectionQuery("from BloodCultureTest where patient = :patient", BloodCultureTest.class);
        query.setParameter("patient", patient);
        return query.getResultList();
    }
    public List<AntibodyTest> getAntibodyTestsByPatient(Patient patient) {
        SelectionQuery<AntibodyTest> query = session.createSelectionQuery("from AntibodyTest where patient = :patient", AntibodyTest.class);
        query.setParameter("patient", patient);
        return query.getResultList();
    }
    public List<OtherTest> getOtherTestsByPatient(Patient patient) {
        SelectionQuery<OtherTest> query = session.createSelectionQuery("from OtherTest where patient = :patient", OtherTest.class);
        query.setParameter("patient", patient);
        return query.getResultList();
    }
    public Specialty getSpecialtyByName(String name) {
        SelectionQuery<Specialty> query = session.createSelectionQuery("from Specialty where name = :name", Specialty.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }
    public List<Doctor> getDoctorsBySpecialty(Specialty specialty) {
        SelectionQuery<Doctor> query = session.createSelectionQuery("from Doctor where specialty = :specialty", Doctor.class);
        query.setParameter("specialty", specialty);
        return query.getResultList();
    }
    public List<Test> getTestsByPatient(Patient patient) {
        SelectionQuery<Test> query = session.createSelectionQuery("from Test where patient = : patient order by date desc", Test.class);
        query.setParameter("patient", patient);
        return query.getResultList();
    }
    public List<Test> getTestsFiltered(Patient patient, Doctor doctor, TestType type, LocalDate start, LocalDate end) {
        SelectionQuery<Test> query;
        String queryString = "from ";
        switch (type) {
            case BLOOD_COUNT_TEST -> queryString += "BloodCountTest ";
            case BLOOD_CULTURE_TEST -> queryString += "BloodCultureTest ";
            case ANTIBODY_TEST -> queryString += "AntibodyTest ";
            case OTHER_TEST -> queryString += "OtherTest ";
            case null, default -> queryString += "Test ";
        }
        queryString += " where patient = :patient";
        if (doctor != null) {
            queryString += " and doctor = :doctor";
        }
        if (start != null) {
            queryString += " and date >= :start";
        }
        if (end != null) {
            queryString += " and date <= :end";
        }
        queryString += " order by date desc";
        query = session.createSelectionQuery(queryString, Test.class);
        query.setParameter("patient", patient);
        if (doctor != null) {
            query.setParameter("doctor", doctor);
        }
        if (start != null) {
            query.setParameter("start", start);
        }
        if (end != null) {
            query.setParameter("end", end);
        }
        return query.getResultList();
    }

    public List<Appointment> getAppointmentsFiltered(Patient patient, Doctor doctor, State state, LocalDate start, LocalDate end) {
        SelectionQuery<Appointment> query;
        String queryString = "from Appointment where patient = :patient";
        if (state != null) {
            switch (state) {
                case SCHEDULED -> queryString += " and state = 'SCHEDULED'";
                case FINISHED -> queryString += " and state = 'FINISHED'";
                case CANCELLED -> queryString += " and state = 'CANCELLED'";
            }
        }
        if (doctor != null) {
            queryString += " and doctor = :doctor";
        }
        if (start != null) {
            queryString += " and date >= :start";
        }
        if (end != null) {
            queryString += " and date <= :end";
        }
        queryString += " order by date desc";
        System.out.println(queryString);
        query = session.createSelectionQuery(queryString, Appointment.class);
        query.setParameter("patient", patient);
        if (doctor != null) {
            query.setParameter("doctor", doctor);
        }
        if (start != null) {
            query.setParameter("start", start);
        }
        if (end != null) {
            query.setParameter("end", end);
        }
        return query.getResultList();
    }

    public List<Specialty> getSpecialties() {
        SelectionQuery<Specialty> query = session.createSelectionQuery("from Specialty", Specialty.class);
        return query.getResultList();
    }
    public List<Doctor> getDoctors() {
        SelectionQuery<Doctor> query = session.createSelectionQuery("from Doctor", Doctor.class);
        return query.getResultList();
    }

    public boolean addPatient(Patient patient) {
        Transaction transaction = session.beginTransaction();
        if (getPatient(patient.getDni()) == null) {
            String hashedPassword = CommonMethods.hash(patient.getPassword());
            patient.setPassword(hashedPassword);
            session.persist(patient);
            transaction.commit();
            return true;
        } else {
            transaction.rollback();
            return false;
        }
    }
    public boolean addDoctor(Doctor doctor) {
        Transaction transaction = session.beginTransaction();
        if (getDoctor(doctor.getDni()) == null) {
            session.persist(doctor);
            transaction.commit();
            return true;
        } else {
            transaction.rollback();
            return false;
        }
    }
    public boolean addSpecialty(Specialty specialty) {
        Transaction transaction = session.beginTransaction();
        if (getSpecialty(specialty.getId()) == null) {
            session.persist(specialty);
            transaction.commit();
            return true;
        } else {
            transaction.rollback();
            return false;
        }
    }
    public boolean addAppointment(Appointment appointment) {
        Transaction transaction = session.beginTransaction();
        if (getAppointment(appointment.getId()) == null) {
            session.persist(appointment);
            transaction.commit();
            return true;
        } else {
            transaction.rollback();
            return false;
        }
    }
    public boolean addVisit(Visit visit) {
        Transaction transaction = session.beginTransaction();
        if (getVisit(visit.getId()) == null) {
            session.persist(visit);
            transaction.commit();
            return true;
        } else {
            transaction.rollback();
            return false;
        }
    }
    public boolean addPrescription(Prescription prescription) {
        Transaction transaction = session.beginTransaction();
        if (getPrescription(prescription.getId()) == null) {
            session.persist(prescription);
            transaction.commit();
            return true;
        } else {
            transaction.rollback();
            return false;
        }
    }
    public boolean addDiagnosis(Diagnosis diagnosis) {
        Transaction transaction = session.beginTransaction();
        if (getDiagnosis(diagnosis.getId()) == null) {
            session.persist(diagnosis);
            transaction.commit();
            return true;
        } else {
            transaction.rollback();
            return false;
        }
    }
    public boolean addBloodCountTest(BloodCountTest bloodCountTest) {
        Transaction transaction = session.beginTransaction();
        if (getBloodCountTest(bloodCountTest.getId()) == null) {
            session.persist(bloodCountTest);
            transaction.commit();
            return true;
        } else {
            transaction.rollback();
            return false;
        }
    }
    public boolean addBloodCultureTest(BloodCultureTest bloodCultureTest) {
        Transaction transaction = session.beginTransaction();
        if (getBloodCultureTest(bloodCultureTest.getId()) == null) {
            session.persist(bloodCultureTest);
            transaction.commit();
            return true;
        } else {
            transaction.rollback();
            return false;
        }
    }
    public boolean addAntibodyTest(AntibodyTest antibodyTest) {
        Transaction transaction = session.beginTransaction();
        if (getAntibodyTest(antibodyTest.getId()) == null) {
            session.persist(antibodyTest);
            transaction.commit();
            return true;
        } else {
            transaction.rollback();
            return false;
        }
    }
    public boolean addOtherTest(OtherTest otherTest) {
        Transaction transaction = session.beginTransaction();
        if (getOtherTest(otherTest.getId()) == null) {
            session.persist(otherTest);
            transaction.commit();
            return true;
        } else {
            transaction.rollback();
            return false;
        }
    }

    public void editPatient(Patient patient, Patient newPatient) {
        Transaction transaction = session.beginTransaction();
        session.refresh(patient);
        patient.setEmail(newPatient.getEmail());
        patient.setTelephone(newPatient.getTelephone());
        patient.setCity(newPatient.getCity());
        patient.setPostalCode(newPatient.getPostalCode());
        patient.setStreet(newPatient.getStreet());
        patient.setNumber(newPatient.getNumber());
        String hashedPassword;
        if (newPatient.getPassword().isEmpty()) {
            hashedPassword = patient.getPassword();
        } else {
            hashedPassword = CommonMethods.hash(newPatient.getPassword());
        }
        patient.setPassword(hashedPassword);
        transaction.commit();
    }
    public void editAppointment(Appointment appointment, Appointment newAppointment) {
        Transaction transaction = session.beginTransaction();
        session.refresh(appointment);
        appointment.setDate(newAppointment.getDate());
        appointment.setTime(newAppointment.getTime());
        appointment.setPatient(newAppointment.getPatient());
        appointment.setDoctor(newAppointment.getDoctor());
        appointment.setState(newAppointment.getState());
        transaction.commit();
    }
}
