package main.ui;

import com.formdev.flatlaf.FlatClientProperties;
import main.connection.*;
import main.reports.PatientReport;
import main.reports.TestsReport;
import net.miginfocom.swing.MigLayout;
import raven.datetime.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestResultsScreen extends ScreenWithParent {
    Toolbar toolbar;
    JPanel mainPanel;
    TestResultsPanel testResultsPanel;


    public TestResultsScreen(App parentFrame) {
        super(parentFrame);
        this.setLayout(new BorderLayout());
        this.parentFrame = parentFrame;
        setUp();
    }

    private void setUp() {
        toolbar = new Toolbar(this, Screens.OPTIONS);
        testResultsPanel = new TestResultsPanel();
        mainPanel = new JPanel(new GridBagLayout());

        mainPanel.add(testResultsPanel);
        add(toolbar,BorderLayout.NORTH);
        add(mainPanel,BorderLayout.CENTER);
    }
    class TestResultsPanel extends JScrollPane {
        JPanel contentPanel;

        JPanel filtersPanel;
        JPanel testsPanel;

        JButton openFiltersButton;

        JLabel titleLabel;

        JLabel typeLabel;
        JComboBox typeComboBox;

        JLabel dateStartLabel;
        JFormattedTextField dateStartField;
        DatePicker dateStartPicker;

        JLabel dateEndLabel;
        JFormattedTextField dateEndField;
        DatePicker dateEndPicker;

        JLabel doctorLabel;
        JComboBox doctorComboBox;

        JButton applyFiltersButton;
        JButton removeFiltersButton;

        JSeparator separator;

        JButton pdfButton;

        boolean filtersOpen;

        List<Doctor> doctors;
        List<Test> tests;
        int typeIndex;
        int doctorIndex;
        LocalDate start;
        LocalDate end;
        TestType type;

        TestResultsPanel() {
            super();
            setUp();
        }

        private void setUp() {
            createElements();
            this.setViewportView(contentPanel);
            setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            getVerticalScrollBar().setUnitIncrement(15);
            CommonMethods.setDimension(this,1200, 600);
            putClientProperty(FlatClientProperties.STYLE, "arc:10");
            contentPanel.setBackground(Color.WHITE);
            addElements();
        }

        private void createElements() {
            MigLayout contentPanelLayout = new MigLayout("wrap 2, fillx, insets 30 50 30 50", "[fill]", "[fill]");
            contentPanel = new JPanel(contentPanelLayout);

            MigLayout filtersPanelLayout = new MigLayout("wrap 2, fillx", "[200, fill]100[200, fill]", "[20,fill]");
            filtersPanel = new JPanel(filtersPanelLayout);
            filtersPanel.setBackground(Color.WHITE);

            MigLayout testsPanelLayout = new MigLayout("wrap 2, fillx", "[200, fill]100[200, fill]", "[fill]");
            testsPanel = new JPanel(testsPanelLayout);
            testsPanel.setBackground(Color.WHITE);

            titleLabel = new JLabel("Historial de pruebas");
            titleLabel.putClientProperty(FlatClientProperties.STYLE,"font: 30");

            openFiltersButton = new JButton("Mostrar filtros");
            CommonMethods.setDimension(openFiltersButton,100,60);
            openFiltersButton.addActionListener(_ -> {
                if (filtersOpen) {
                    removeFilterElements();
                } else {
                    addFilterElements();
                }
            });

            createFilterElements();

            pdfButton = new JButton("PDF");
            CommonMethods.setDimension(openFiltersButton,300, 60);
            pdfButton.addActionListener(_ -> {
                TestsReport testsReport = new TestsReport(getParentFrame().getPatient());
                testsReport.createDocument();
            });
        }
        private void createFilterElements() {
            typeLabel = new JLabel("Tipo");
            String[] types = {
                    "-",
                    "Hemograma",
                    "Hemocultivo",
                    "Test de anticuerpos",
                    "Otros tests"
            };
            typeComboBox = new JComboBox(types);

            doctorLabel = new JLabel("Doctor");

            doctors = getParentFrame().getDbConnection().getDoctors();
            String[] doctorNames = new String[doctors.size() + 1];
            doctorNames[0] = "-";
            for (int i = 0; i < doctors.size(); i++) {
                doctorNames[i + 1] = doctors.get(i).getFullName();
            }
            doctorComboBox = new JComboBox(doctorNames);

            dateStartLabel = new JLabel("Desde");
            dateStartPicker = new DatePicker();
            dateStartField = new JFormattedTextField();
            dateStartPicker.setEditor(dateStartField);
            dateStartPicker.setDateSelectionAble(localDate -> localDate.isBefore(LocalDate.now()));

            dateEndLabel = new JLabel("Hasta");

            dateEndPicker = new DatePicker();
            dateEndField = new JFormattedTextField();
            dateEndPicker.setEditor(dateEndField);
            dateEndPicker.setDateSelectionAble(localDate -> localDate.isBefore(LocalDate.now()));

            applyFiltersButton = new JButton("Aplicar filtros");
            CommonMethods.setDimension(applyFiltersButton,300, 60);
            applyFiltersButton.addActionListener(_ -> applyFilters());

            removeFiltersButton = new JButton("Eliminar filtros");
            CommonMethods.setDimension(removeFiltersButton,300, 60);
            removeFiltersButton.addActionListener(_ -> removeFilters());

            separator = new JSeparator();
        }
        private void addElements() {
            contentPanel.add(titleLabel);

            contentPanel.add(openFiltersButton, "right");

            contentPanel.add(new JSeparator(), "gapy 15 5, span");

            contentPanel.add(filtersPanel, "span");

            contentPanel.add(testsPanel, "span");

            contentPanel.add(pdfButton, "span, center");

            addTestElements();
        }
        private void addTestElements() {
            tests = filterTests();

            if (tests.isEmpty()) {
                JLabel noAppointmentsLabel = new JLabel("No se han encontrado tests");
                noAppointmentsLabel.setHorizontalAlignment(SwingConstants.CENTER);
                testsPanel.add(noAppointmentsLabel, "span, center, gapy 5 10");
                pdfButton.setVisible(false);
                openFiltersButton.setVisible(false);
            } else {
                for (Test test : tests) {
                    addTestToPanel(test);
                }
            }
        }
        private void addTestToPanel(Test test) {
            ArrayList<JLabel> titles = new ArrayList<>();
            titles.add(new JLabel("Doctor"));
            titles.add(new JLabel("Fecha"));
            titles.add(new JLabel("Notas"));


            ArrayList<JTextField> data = new ArrayList<>();
            data.add(new JTextField(test.getDoctor().getFullName()));
            data.add(new JTextField(CommonMethods.getDateString(test.getDate())));
            data.add(new JTextField(test.getNotes() == null ? "" : test.getNotes()));

            switch (test) {
                case BloodCountTest bloodCountTest -> {
                    titles.add(new JLabel("Glóbulos rojos"));
                    titles.add(new JLabel("Hemoglobina"));
                    titles.add(new JLabel("Plaquetas"));
                    titles.add(new JLabel("Hematocrito"));

                    String redBloodCell = bloodCountTest.getRedBloodCell() == null ? "-" : bloodCountTest.getRedBloodCell().toString();
                    String hemoglobin = bloodCountTest.getHemoglobin() == null ? "-" : bloodCountTest.getHemoglobin().toString();
                    String platet = bloodCountTest.getPlatet() == null ? "-" : bloodCountTest.getPlatet().toString();
                    String hematocrit = bloodCountTest.getHematocrit() == null ? "-" : bloodCountTest.getHematocrit().toString();


                    data.add(new JTextField(redBloodCell));
                    data.add(new JTextField(hemoglobin));
                    data.add(new JTextField(platet));
                    data.add(new JTextField(hematocrit));
                }
                case BloodCultureTest bloodCultureTest -> {
                    titles.add(new JLabel("Acinetobacter"));
                    titles.add(new JLabel("Citrobacter"));
                    titles.add(new JLabel("Escheriachia coli"));
                    titles.add(new JLabel("Salmonella"));


                    String acinetobacter = bloodCultureTest.getAcinetobacter() == null ? "-" : bloodCultureTest.getAcinetobacter().toString();
                    String citrobacter = bloodCultureTest.getCitrobacter() == null ? "-" : bloodCultureTest.getCitrobacter().toString();
                    String echerichiaColi = bloodCultureTest.getEscherichiaColi() == null ? "-" : bloodCultureTest.getEscherichiaColi().toString();
                    String salmonella = bloodCultureTest.getSalmonella() == null ? "-" : bloodCultureTest.getSalmonella().toString();

                    data.add(new JTextField(acinetobacter));
                    data.add(new JTextField(citrobacter));
                    data.add(new JTextField(echerichiaColi));
                    data.add(new JTextField(salmonella));
                }
                case AntibodyTest antibodyTest -> {
                    titles.add(new JLabel("Anti CCP"));
                    titles.add(new JLabel("Anti SRP"));
                    titles.add(new JLabel("Anti SSA"));
                    titles.add(new JLabel("Anti TTG"));

                    String antiCcp = antibodyTest.getAntiCcp() == null ? "-" : antibodyTest.getAntiCcp().toString();
                    String antiSrp = antibodyTest.getAntiSrp() == null ? "-" : antibodyTest.getAntiSrp().toString();
                    String antiSsa = antibodyTest.getAntiSsa() == null ? "-" : antibodyTest.getAntiSsa().toString();
                    String antiTtg = antibodyTest.getAntiTtg() == null ? "-" : antibodyTest.getAntiTtg().toString();

                    data.add(new JTextField(antiCcp));
                    data.add(new JTextField(antiSrp));
                    data.add(new JTextField(antiSsa));
                    data.add(new JTextField(antiTtg));

                }
                case OtherTest otherTest -> {
                    if (type == null || type == TestType.OTHER_TEST) {
                        titles.add(new JLabel("Tipo"));
                        titles.add(new JLabel("Resultados"));

                        data.add(new JTextField(otherTest.getType()));
                        data.add(new JTextField(otherTest.getResults()));
                    }
                }
                default -> {

                }
            }

            for (int i = 0; i < titles.size(); i++) {
                titles.get(i).putClientProperty(FlatClientProperties.STYLE,"font: 20");
                titles.get(i).setHorizontalAlignment(SwingConstants.CENTER);
                data.get(i).setHorizontalAlignment(SwingConstants.CENTER);
                data.get(i).putClientProperty(FlatClientProperties.STYLE,"arc: 5");
                data.get(i).setBackground(Color.WHITE);
                data.get(i).setEditable(false);
            }

            data.get(2).setHorizontalAlignment(SwingConstants.LEFT);

            testsPanel.add(titles.get(0));
            testsPanel.add(titles.get(1));

            testsPanel.add(data.get(0), "gapy 5 10");
            testsPanel.add(data.get(1), "gapy 5 10");

            testsPanel.add(titles.get(2), "span");
            testsPanel.add(data.get(2), "gapy 5 10, span");

            for (int i = 3; i < titles.size(); i++) {
                testsPanel.add(titles.get(i));
                testsPanel.add(data.get(i), "gapy 5 10");
            }

            testsPanel.add(new JSeparator(), "gapy 10 20, span");
        }
        private void addFilterElements() {
            filtersOpen = true;
            openFiltersButton.setText("Ocultar filtros");
            filtersPanel.add(typeLabel);
            filtersPanel.add(doctorLabel);
            filtersPanel.add(typeComboBox);
            filtersPanel.add(doctorComboBox);
            filtersPanel.add(dateStartLabel);
            filtersPanel.add(dateEndLabel);
            filtersPanel.add(dateStartField);
            filtersPanel.add(dateEndField);
            filtersPanel.add(applyFiltersButton,"gapy 10 20, center");
            filtersPanel.add(removeFiltersButton,"gapy 10 20, center");
            filtersPanel.add(separator, "gapy 5 5, span");
        }
        private void removeFilterElements() {
            filtersOpen = false;
            openFiltersButton.setText("Mostrar filtros");
            filtersPanel.removeAll();
        }
        private void applyFilters() {
            testsPanel.removeAll();
            addTestElements();
            revalidate();
            repaint();
        }
        private void removeFilters() {
            typeComboBox.setSelectedIndex(0);
            doctorComboBox.setSelectedIndex(0);
            dateStartField.setValue(null);
            dateEndField.setValue(null);
            applyFilters();
        }
        public List<Test> filterTests() {
            typeIndex = typeComboBox.getSelectedIndex() - 1;
            doctorIndex = doctorComboBox.getSelectedIndex() - 1;
            start = dateStartPicker.getSelectedDate();
            end = dateEndPicker.getSelectedDate();

            type = switch (typeIndex) {
                case 0 -> TestType.BLOOD_COUNT_TEST;
                case 1 -> TestType.BLOOD_CULTURE_TEST;
                case 2 -> TestType.ANTIBODY_TEST;
                case 3 -> TestType.OTHER_TEST;
                default -> null;
            };
            Doctor doctor = null;
            if (doctorIndex > -1) {
                doctor = doctors.get(doctorIndex);
                System.out.println(doctor.getFullName());
            }

            return getParentFrame().getDbConnection().getTestsFiltered(getParentFrame().getPatient(), doctor, type, start, end);
        }
    }
}
