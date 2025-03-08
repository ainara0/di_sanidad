package main.reports;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import main.connection.DBConnection;
import main.connection.Patient;

import java.io.FileOutputStream;

public abstract class Report {
    Patient patient;
    String filename;
    Document document;
    String title;
    DBConnection db;
    static Font font12b = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    static Font font16b = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    static Font font20b = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
    static Font font22b = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD);
    static Font font30b = new Font(Font.FontFamily.TIMES_ROMAN, 30, Font.BOLD);

    static Font font12r = new Font(Font.FontFamily.TIMES_ROMAN, 12);
    static Font font16r = new Font(Font.FontFamily.TIMES_ROMAN, 16);
    static Font font18r = new Font(Font.FontFamily.TIMES_ROMAN, 18);

    public Report(Patient patient) {
        this.patient = patient;
        filename = "informe_" + patient.getDni() + "_" + System.currentTimeMillis() + ".pdf";
    }

    public boolean createDocument() {
        db = new DBConnection();
        if (!db.openSession()) {
            return false;
        }
        try {
            document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
            addFirstPage();
            addContents();
            document.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public abstract void addContents() throws DocumentException;

    private void addFirstPage() throws DocumentException {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        preface.add(new Paragraph(title, font30b));
        addEmptyLine(preface, 3);
        preface.add(new Paragraph("Generado por: " + System.getProperty("user.name"), font18r));
        addEmptyLine(preface, 3);
        preface.add(new Paragraph("DNI del paciente: " + patient.getDni(), font18r));
        addEmptyLine(preface, 3);
        document.add(preface);
        document.newPage();
    }

    static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}