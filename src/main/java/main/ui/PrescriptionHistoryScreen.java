package main.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.icons.FlatSearchIcon;
import main.connection.Doctor;
import main.connection.Patient;
import main.connection.Prescription;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class PrescriptionHistoryScreen extends ScreenWithParent {
    Toolbar toolbar;
    JPanel mainPanel;
    PrescriptionHistoryPanel prescriptionHistoryPanel;


    public PrescriptionHistoryScreen(App parentFrame) {
        super(parentFrame);
        this.setLayout(new BorderLayout());
        this.parentFrame = parentFrame;
        setUp();
    }

    private void setUp() {
        toolbar = new Toolbar(this, Screens.OPTIONS);
        prescriptionHistoryPanel = new PrescriptionHistoryPanel();
        mainPanel = new JPanel(new GridBagLayout());

        mainPanel.add(prescriptionHistoryPanel);
        add(toolbar,BorderLayout.NORTH);
        add(mainPanel,BorderLayout.CENTER);
    }

    class PrescriptionHistoryPanel extends JScrollPane {
        JPanel contentPanel;
        JPanel prescriptionsPanel;
        JTextField searchField;

        JLabel titleLabel;
        JButton pdfButton;

        public PrescriptionHistoryPanel() {
            super();
            setUp();
        }

        private void setUp() {
            MigLayout panelLayout = new MigLayout("wrap, fillx, insets 30 50 30 50", "[fill]", "[fill]");
            contentPanel = new JPanel(panelLayout);

            MigLayout prescriptionsPanelLayout = new MigLayout("wrap 2, fillx", "[250, fill]100[250, fill]", "[fill]");
            prescriptionsPanel = new JPanel(prescriptionsPanelLayout);

            this.setViewportView(contentPanel);
            setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            getVerticalScrollBar().setUnitIncrement(15);
            CommonMethods.setDimension(this, 1200, 600);
            putClientProperty(FlatClientProperties.STYLE, "arc:10");
            contentPanel.setBackground(Color.WHITE);
            prescriptionsPanel.setBackground(Color.WHITE);
            createElements();
            addElements();
        }

        private void createElements() {
            titleLabel = new JLabel("Historial de recetas");
            titleLabel.putClientProperty(FlatClientProperties.STYLE,"font: 30");
            pdfButton = new JButton("PDF");
            CommonMethods.setDimension(pdfButton, 300, 50);
            searchField = new JTextField();
            searchField.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON, new FlatSearchIcon());
            searchField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Descripción");
            searchField.setHorizontalAlignment(SwingConstants.CENTER);
            CommonMethods.setDimension(searchField, 400, 60);
            searchField.addActionListener(_ -> {
                removePrescriptions();
                addPrescriptions();
            });
            searchField.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    removePrescriptions();
                    addPrescriptions();
                }
            });
            pdfButton.addActionListener(_ -> {
                //TODO: print pdf
                System.out.println("Clock");
            });
        }
        private void addElements() {
            contentPanel.add(titleLabel);
            contentPanel.add(new JSeparator(), "gapy 10 20");
            contentPanel.add(searchField, "center, gapy 10 20");
            contentPanel.add(prescriptionsPanel);
            addPrescriptions();
            contentPanel.add(pdfButton, "center");
        }

        private void addPrescriptions() {
            Patient patient = parentFrame.getPatient();
            String description = searchField.getText();
            List<Prescription> prescriptions;
            if (description != null && !description.isEmpty()) {
                prescriptions = getParentFrame().getDbConnection().getPrescriptionsByPatient(patient,description);
            } else {
                prescriptions = getParentFrame().getDbConnection().getPrescriptionsByPatient(patient);
            }
            if (prescriptions.isEmpty()) {
                JLabel noPrescriptionsLabel = new JLabel("No se han encontrado recetas");
                noPrescriptionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
                prescriptionsPanel.add(noPrescriptionsLabel, "span, center, gapy 5 10");
                pdfButton.setVisible(false);
            } else {
                for (Prescription prescription : prescriptions) {
                    addPrescriptionToPanel(prescription);
                }
            }
        }
        private void removePrescriptions() {
            prescriptionsPanel.removeAll();
            pdfButton.setVisible(true);
        }

        private void addPrescriptionToPanel(Prescription prescription) {
            JLabel[] titles = {
                    new JLabel("Doctor"),
                    new JLabel("Start date"),
                    new JLabel("End date"),
                    new JLabel("Description"),
            };

            Doctor doctor = prescription.getDoctor();
            String doctorFullName = doctor.getName() + " " + doctor.getSurname1() + " " + (doctor.getSurname2() == null ? "" : doctor.getSurname2());


            JTextField[] data = {
                    new JTextField(doctorFullName),
                    new JTextField(CommonMethods.getDateString(prescription.getStart())),
                    new JTextField(CommonMethods.getDateString(prescription.getEnd())),
                    new JTextField(prescription.getDescription()),
            };

            for (int j = 0; j < 4; j++) {
                titles[j].putClientProperty(FlatClientProperties.STYLE,"font: 20");
                titles[j].setHorizontalAlignment(SwingConstants.CENTER);
                data[j].putClientProperty(FlatClientProperties.STYLE,"arc: 5");
                data[j].setBackground(Color.WHITE);
                data[j].setHorizontalAlignment(SwingConstants.CENTER);
                data[j].setEditable(false);
            }
            data[3].setHorizontalAlignment(SwingConstants.LEFT);

            prescriptionsPanel.add(titles[0], "span");
            prescriptionsPanel.add(data[0], "span, gapy 5 10");

            prescriptionsPanel.add(titles[1]);
            prescriptionsPanel.add(titles[2]);

            prescriptionsPanel.add(data[1], "gapy 5 10");
            prescriptionsPanel.add(data[2], "gapy 5 10");

            prescriptionsPanel.add(titles[3], "span");
            prescriptionsPanel.add(data[3], "span, gapy 5 10");

            prescriptionsPanel.add(new JSeparator(), "gapy 10 20, span");
        }
    }
}
