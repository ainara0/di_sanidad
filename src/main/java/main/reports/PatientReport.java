package main.reports;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import main.connection.Patient;

public class PatientReport extends Report {

    public PatientReport(Patient patient) {
        super(patient);
        filename = "paciente_" + filename;
        title = "Informe del paciente";
    }

    @Override
    public void addContents() throws DocumentException {
        addDetailsPage();
    }

    private void addDetailsPage() throws DocumentException {
        Paragraph details = new Paragraph();
        addEmptyLine(details, 1);
        details.add(new Paragraph("Datos personales", font22b));
        addEmptyLine(details, 6);
        details.add(new Paragraph("DNI: " + patient.getDni(), font12b));
        addEmptyLine(details, 1);
        details.add(new Paragraph("Nombre: " + patient.getFullName(), font12b));
        addEmptyLine(details, 1);
        details.add(new Paragraph("Email: " + patient.getEmail(), font12b));
        addEmptyLine(details, 1);
        details.add(new Paragraph("Telephone: " + patient.getTelephone(), font12b));
        addEmptyLine(details, 1);
        details.add(new Paragraph("Address: " + patient.getFullAddress(), font12b));
        document.add(details);
    }
}
