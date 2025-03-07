package main.ui;

import com.formdev.flatlaf.FlatClientProperties;
import main.connection.Appointment;
import main.connection.Doctor;
import main.connection.Patient;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static main.ui.CommonMethods.setDimension;

public class EditAppointmentsScreen extends ScreenWithParent {
    Toolbar toolbar;
    JPanel mainPanel;
    FutureAppointmentsPanel futureAppointmentsPanel;


    public EditAppointmentsScreen(App parentFrame) {
        super(parentFrame);
        this.setLayout(new BorderLayout());
        this.parentFrame = parentFrame;
        setUp();
    }

    private void setUp() {
        toolbar = new Toolbar(this, Screens.APPOINTMENTS);
        futureAppointmentsPanel = new FutureAppointmentsPanel();
        mainPanel = new JPanel(new GridBagLayout());

        mainPanel.add(futureAppointmentsPanel);
        add(toolbar,BorderLayout.NORTH);
        add(mainPanel,BorderLayout.CENTER);
    }

    private EditAppointmentsScreen getAppointmentHistoryScreen() {
        return this;
    }

    class FutureAppointmentsPanel extends JScrollPane {
        JPanel contentPanel;

        JLabel titleLabel;
        JButton pdfButton;

        public FutureAppointmentsPanel() {
            super();
            setUp();
        }

        private void setUp() {
            contentPanel = new JPanel(new MigLayout("wrap 2, fillx", "[250, fill]30[250, fill]", "[30,fill]"));
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
            contentPanel.putClientProperty(FlatClientProperties.STYLE, "arc:10");

            createElements();
            addElements();
        }

        private void createElements() {
            titleLabel = new JLabel("Editar o cancelar citas");
            titleLabel.putClientProperty(FlatClientProperties.STYLE,"font: 30");

            pdfButton = new JButton("PDF");
            setDimension(pdfButton, 300, 50);
            pdfButton.addActionListener(e -> {
                System.out.println("Clock");
            });
        }
        private void addElements() {
            contentPanel.add(titleLabel, "span");
            contentPanel.add(new JSeparator(), "gapy 5 10, span");

            Patient patient = parentFrame.getPatient();

            List<Appointment> appointments = getParentFrame().getDbConnection().getFutureAppointmentsByPatient(patient);

            if (appointments.isEmpty()) {
                JLabel noAppointmentsLabel = new JLabel("No se han encontrado citas futuras");
                contentPanel.add(noAppointmentsLabel, "span, center, gapy 5 10");
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
                data[j].setEditable(false);
                setDimension(data[j], 400, 50);
            }

            contentPanel.add(titles[0], "center");
            contentPanel.add(titles[1], "center");

            contentPanel.add(data[0], "center");
            contentPanel.add(data[1], "center");

            contentPanel.add(titles[2], "center");
            contentPanel.add(titles[3], "center");

            contentPanel.add(data[2], "center");
            contentPanel.add(data[3], "center");

            JButton editButton = new JButton("Editar");
            editButton.addActionListener(_ -> {
                getParentFrame().showScreen(new EditAppointmentScreen(getParentFrame(), appointment));
            });

            setDimension(editButton, 300, 50);

            contentPanel.add(editButton, "center, span, gapy 10 10");

            contentPanel.add(new JSeparator(), "gapy 5 10, span");
        }
    }
}
