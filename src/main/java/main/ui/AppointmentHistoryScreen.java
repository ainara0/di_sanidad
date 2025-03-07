package main.ui;

import com.formdev.flatlaf.FlatClientProperties;
import main.connection.Appointment;
import main.connection.Doctor;
import main.connection.State;
import net.miginfocom.swing.MigLayout;
import raven.datetime.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class AppointmentHistoryScreen extends ScreenWithParent {
    Toolbar toolbar;
    JPanel mainPanel;
    AppointmentHistoryPanel appointmentHistoryPanel;


    public AppointmentHistoryScreen(App parentFrame) {
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

        JPanel filtersPanel;
        JPanel appointmentsPanel;

        JButton openFiltersButton;

        JLabel titleLabel;

        JLabel stateLabel;
        JComboBox stateComboBox;

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
        List<Appointment> appointments;
        int stateIndex;
        int doctorIndex;
        LocalDate start;
        LocalDate end;
        State state;

        AppointmentHistoryPanel() {
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

            MigLayout appointmentsPanelLayout = new MigLayout("wrap 2, fillx", "[200, fill]100[200, fill]", "[fill]");
            appointmentsPanel = new JPanel(appointmentsPanelLayout);
            appointmentsPanel.setBackground(Color.WHITE);

            titleLabel = new JLabel("Historial de pruebas");
            titleLabel.putClientProperty(FlatClientProperties.STYLE,"font: 30");

            openFiltersButton = new JButton("Mostrar filtros");
            CommonMethods.setDimension(openFiltersButton,300,60);
            openFiltersButton.addActionListener(_ -> {
                if (filtersOpen) {
                    removeFilterElements();
                } else {
                    addFilterElements();
                }
            });

            createFilterElements();

            pdfButton = new JButton("PDF");
            CommonMethods.setDimension(pdfButton,300, 60);
            pdfButton.addActionListener(_ -> {
                //TODO: print pdf
                System.out.println("Clock");
            });
        }
        private void createFilterElements() {
            stateLabel = new JLabel("Estado");
            String[] types = {
                    "-",
                    "Programada",
                    "Terminada",
                    "Cancelada"
            };
            stateComboBox = new JComboBox(types);

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

            dateEndLabel = new JLabel("Hasta");

            dateEndPicker = new DatePicker();
            dateEndField = new JFormattedTextField();
            dateEndPicker.setEditor(dateEndField);

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

            contentPanel.add(appointmentsPanel, "span");

            contentPanel.add(pdfButton, "span, center");

            addAppointmentElements();
        }
        private void addAppointmentElements() {
            appointments = filterAppointments();
            if (appointments.isEmpty()) {
                JLabel noAppointmentsLabel = new JLabel("No se han encontrado citas");
                noAppointmentsLabel.setHorizontalAlignment(SwingConstants.CENTER);
                appointmentsPanel.add(noAppointmentsLabel, "span, center, gapy 5 10");
                pdfButton.setVisible(false);
                openFiltersButton.setVisible(false);
            } else {
                for (Appointment appointment : appointments) {
                    addAppointmentToPanel(appointment);
                }
            }
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
                    new JTextField(CommonMethods.getDateString(appointment.getDate())),
                    new JTextField(CommonMethods.getTimeString12H(appointment.getTime())),
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

            appointmentsPanel.add(titles[0]);
            appointmentsPanel.add(titles[1]);

            appointmentsPanel.add(data[0], "gapy 10 15");
            appointmentsPanel.add(data[1], "gapy 10 15");

            appointmentsPanel.add(titles[2]);
            appointmentsPanel.add(titles[3]);

            appointmentsPanel.add(data[2], "gapy 10 15");
            appointmentsPanel.add(data[3], "gapy 10 15");

            appointmentsPanel.add(new JSeparator(), "gapy 10 20, span");
        }
        private void addFilterElements() {
            filtersOpen = true;
            openFiltersButton.setText("Ocultar filtros");
            filtersPanel.add(stateLabel);
            filtersPanel.add(doctorLabel);
            filtersPanel.add(stateComboBox);
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
            appointmentsPanel.removeAll();
            addAppointmentElements();
            revalidate();
            repaint();
        }
        private void removeFilters() {
            stateComboBox.setSelectedIndex(0);
            doctorComboBox.setSelectedIndex(0);
            dateStartField.setValue(null);
            dateEndField.setValue(null);
            applyFilters();
        }
        public List<Appointment> filterAppointments() {
            stateIndex = stateComboBox.getSelectedIndex() - 1;
            doctorIndex = doctorComboBox.getSelectedIndex() - 1;
            start = dateStartPicker.getSelectedDate();
            end = dateEndPicker.getSelectedDate();

            state = switch (stateIndex) {
                case 0 -> State.SCHEDULED;
                case 1 -> State.FINISHED;
                case 2 -> State.CANCELLED;
                default -> null;
            };
            Doctor doctor = null;
            if (doctorIndex > -1) {
                doctor = doctors.get(doctorIndex);
                System.out.println(doctor.getFullName());
            }
            return getParentFrame().getDbConnection().getAppointmentsFiltered(getParentFrame().getPatient(), doctor, state, start, end);
        }
    }
}
