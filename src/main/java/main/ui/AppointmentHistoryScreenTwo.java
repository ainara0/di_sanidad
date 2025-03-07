package main.ui;

import com.formdev.flatlaf.FlatClientProperties;
import main.connection.Appointment;
import main.connection.Doctor;
import main.connection.Patient;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AppointmentHistoryScreenTwo extends ScreenWithParent {
    Toolbar toolbar;
    JPanel mainPanel;
    AppointmentHistoryPanel appointmentHistoryPanel;


    public AppointmentHistoryScreenTwo(App parentFrame) {
        super(parentFrame);
        this.setLayout(new BorderLayout());
        this.parentFrame = parentFrame;
        setUp();
    }

    private void setUp() {
        toolbar = new Toolbar(this, Screens.APPOINTMENTS);
        appointmentHistoryPanel = new AppointmentHistoryPanel();
        mainPanel = new JPanel(new GridBagLayout());

        mainPanel.add(appointmentHistoryPanel);
        add(toolbar,BorderLayout.NORTH);
        add(mainPanel,BorderLayout.CENTER);
    }

    class AppointmentHistoryPanel extends JScrollPane {
        JPanel contentPanel;

        JLabel titleLabel;
        JButton pdfButton;

        public AppointmentHistoryPanel() {
            super();
            setUp();
        }

        private void setUp() {
            MigLayout panelLayout = new MigLayout("wrap 2, fillx, insets 30 50 30 50", "[250, fill]100[250, fill]", "[fill]");
            contentPanel = new JPanel(panelLayout);

            this.setViewportView(contentPanel);
            setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            getVerticalScrollBar().setUnitIncrement(15);
            Dimension dim = new Dimension(1200, 600);
            setPreferredSize(dim);
            setMinimumSize(dim);
            setMaximumSize(dim);
            putClientProperty(FlatClientProperties.STYLE, "arc:10");
            contentPanel.setBackground(Color.WHITE);
            createElements();
            addElements();
        }

        private void createElements() {
            titleLabel = new JLabel("Historial de citas");
            titleLabel.putClientProperty(FlatClientProperties.STYLE,"font: 30");

            pdfButton = new JButton("PDF");
            CommonMethods.setDimension(pdfButton,300, 50);
            pdfButton.addActionListener(_ -> {
                //TODO: print pdf
                System.out.println("Clock");
            });
        }
        private void addElements() {
            contentPanel.add(titleLabel, "span");
            contentPanel.add(new JSeparator(), "gapy 10 20, span");

            Patient patient = parentFrame.getPatient();

            List<Appointment> appointments = getParentFrame().getDbConnection().getPastAppointmentsByPatient(patient);

            if (appointments.isEmpty()) {
                JLabel noAppointmentsLabel = new JLabel("No se han encontrado citas pasadas");
                noAppointmentsLabel.setHorizontalAlignment(SwingConstants.CENTER);
                contentPanel.add(noAppointmentsLabel, "span, center, gapy 5 10");
                pdfButton.setVisible(false);
            } else {
                for (Appointment appointment : appointments) {
                    addAppointmentToPanel(appointment);
                }
            }
            contentPanel.add(pdfButton, "span, center");
        }

        private void addAppointmentToPanel(Appointment appointment) {
            JLabel[] titles = {
                    new JLabel("Fecha"),
                    new JLabel("Hora"),
                    new JLabel("Doctor"),
                    new JLabel("Estado")
            };

            Doctor doctor = appointment.getDoctor();
            String doctorFullName = doctor.getName() + " " + doctor.getSurname1() + " " + (doctor.getSurname2() == null ? "" : doctor.getSurname2());

            JTextField[] data = {
                    new JTextField(appointment.getDate().toString()),
                    new JTextField(appointment.getTime().toString()),
                    new JTextField(doctorFullName),
                    switch (appointment.getState().toString()) {
                        case "SCHEDULED" -> new JTextField("Programada");
                        case "CANCELLED" -> new JTextField("Cancelada");
                        case "FINISHED" -> new JTextField("Terminada");
                        default -> new JTextField("Valor inválido");
                    }
            };

            for (int j = 0; j < 4; j++) {
                titles[j].putClientProperty(FlatClientProperties.STYLE,"font: 20");
                titles[j].setHorizontalAlignment(SwingConstants.CENTER);
                data[j].setHorizontalAlignment(SwingConstants.CENTER);
                data[j].putClientProperty(FlatClientProperties.STYLE,"arc: 5");
                data[j].setBackground(Color.WHITE);
                data[j].setEditable(false);
            }

            contentPanel.add(titles[0]);
            contentPanel.add(titles[1]);

            contentPanel.add(data[0], "gapy 5 10");
            contentPanel.add(data[1], "gapy 5 10");

            contentPanel.add(titles[2]);
            contentPanel.add(titles[3]);

            contentPanel.add(data[2], "gapy 5 10");
            contentPanel.add(data[3], "gapy 5 10");

            contentPanel.add(new JSeparator(), "gapy 10 20, span");
        }
    }
}
