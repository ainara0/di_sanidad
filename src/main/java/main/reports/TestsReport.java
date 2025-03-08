package main.reports;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import main.connection.*;
import main.ui.CommonMethods;

import java.util.List;

public class TestsReport extends Report {

    public TestsReport(Patient patient) {
        super(patient);
        filename = "pruebas_" + filename;
        title = "Informe de pruebas";
    }

    @Override
    public void addContents() throws DocumentException {
        addDetailsPage();
    }

    private void addDetailsPage() throws DocumentException {
        Paragraph details = new Paragraph();

        addEmptyLine(details, 1);
        details.add(new Paragraph("Pruebas", font22b));
        addEmptyLine(details, 4);

        details.add(new Paragraph("Blood count tests", font16b));
        addEmptyLine(details, 1);
        details.add(createBloodCountTestTable());
        addEmptyLine(details, 3);

        details.add(new Paragraph("Blood culture tests", font16b));
        addEmptyLine(details, 1);
        details.add(createBloodCultureTestTable());
        addEmptyLine(details, 3);

        details.add(new Paragraph("Antibody tests", font16b));
        addEmptyLine(details, 1);
        details.add(createAntibodyTestTable());
        addEmptyLine(details, 3);

        details.add(new Paragraph("Other tests", font16b));
        addEmptyLine(details, 1);
        details.add(createOtherTestTable());
        addEmptyLine(details, 3);

        document.add(details);
    }


    private PdfPTable createBloodCountTestTable() throws BadElementException {
        String[] titles =  {
                "Doctor",
                "Fecha",
                "Notas",
                "Glóbulos rojos",
                "Hemoglobina",
                "Plaquetas",
                "Hematocrito"
        };

        List <BloodCountTest> bloodCountTests = db.getBloodCountTestsByPatient(patient);
        String[][] values = new String[bloodCountTests.size()][];
        for (int i = 0; i < bloodCountTests.size(); i++) {
            BloodCountTest bloodCountTest = bloodCountTests.get(i);
            String[] testValues = {
                    bloodCountTest.getDoctor().getFullName(),
                    CommonMethods.getDateString(bloodCountTest.getDate()),
                    bloodCountTest.getNotes(),
                    Integer.toString(bloodCountTest.getRedBloodCell()),
                    Integer.toString(bloodCountTest.getHemoglobin()),
                    Integer.toString(bloodCountTest.getPlatet()),
                    Integer.toString(bloodCountTest.getHematocrit()),
            };
            values[i] = testValues;
        }
        return createTable(titles,values);
    }

    private PdfPTable createBloodCultureTestTable() throws BadElementException {
        String[] titles =  {
                "Doctor",
                "Fecha",
                "Notas",
                "Acinetobacter",
                "Citrobacter",
                "Escheriachia coli",
                "Salmonella"
        };

        List <BloodCultureTest> bloodCultureTests = db.getBloodCultureTestsByPatient(patient);
        String[][] values = new String[bloodCultureTests.size()][];
        for (int i = 0; i < bloodCultureTests.size(); i++) {
            BloodCultureTest bloodCultureTest = bloodCultureTests.get(i);
            String[] testValues = {
                    bloodCultureTest.getDoctor().getFullName(),
                    CommonMethods.getDateString(bloodCultureTest.getDate()),
                    bloodCultureTest.getNotes(),
                    Integer.toString(bloodCultureTest.getAcinetobacter()),
                    Integer.toString(bloodCultureTest.getCitrobacter()),
                    Integer.toString(bloodCultureTest.getEscherichiaColi()),
                    Integer.toString(bloodCultureTest.getSalmonella()),
            };
            values[i] = testValues;
        }
        return createTable(titles,values);
    }

    private PdfPTable createAntibodyTestTable() throws BadElementException {
        String[] titles =  {
                "Doctor",
                "Fecha",
                "Notas",
                "Anti CCP",
                "Anti SRP",
                "Anti SSA",
                "Anti TTG"
        };

        List <AntibodyTest> antibodyTests = db.getAntibodyTestsByPatient(patient);
        String[][] values = new String[antibodyTests.size()][];
        for (int i = 0; i < antibodyTests.size(); i++) {
            AntibodyTest antibodyTest = antibodyTests.get(i);
            String[] testValues = {
                    antibodyTest.getDoctor().getFullName(),
                    CommonMethods.getDateString(antibodyTest.getDate()),
                    antibodyTest.getNotes(),
                    Integer.toString(antibodyTest.getAntiCcp()),
                    Integer.toString(antibodyTest.getAntiSrp()),
                    Integer.toString(antibodyTest.getAntiSsa()),
                    Integer.toString(antibodyTest.getAntiTtg()),
            };
            values[i] = testValues;
        }
        return createTable(titles,values);
    }

    private PdfPTable createOtherTestTable() throws BadElementException {
        String[] titles =  {
                "Doctor",
                "Fecha",
                "Notas",
                "Tipo",
                "Resultados"
        };

        List <OtherTest> otherTests = db.getOtherTestsByPatient(patient);
        String[][] values = new String[otherTests.size()][];
        for (int i = 0; i < otherTests.size(); i++) {
            OtherTest otherTest = otherTests.get(i);
            String[] testValues = {
                    otherTest.getDoctor().getFullName(),
                    CommonMethods.getDateString(otherTest.getDate()),
                    otherTest.getNotes(),
                    otherTest.getType(),
                    otherTest.getResults()
            };
            values[i] = testValues;
        }
        return createTable(titles,values);
    }


    private PdfPTable createTable(String[] titles, String[][] values) throws BadElementException {
        PdfPTable table = new PdfPTable(titles.length);
        table.setHeaderRows(1);
        for (String title : titles) {
            PdfPCell cell = new PdfPCell(new Phrase(title, font16b));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
        for (String[] test : values) {
            for (String value : test) {
                PdfPCell cell = new PdfPCell(new Phrase(value, font12r));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
        }
        return table;
    }

}
