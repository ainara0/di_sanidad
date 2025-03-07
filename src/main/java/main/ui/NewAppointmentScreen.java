package main.ui;

import com.formdev.flatlaf.FlatClientProperties;
import main.connection.Appointment;
import main.connection.Doctor;
import main.connection.Specialty;
import main.connection.State;
import net.miginfocom.swing.MigLayout;
import raven.datetime.DatePicker;
import raven.datetime.TimePicker;
import raven.datetime.TimeSelectionAble;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static main.ui.CommonMethods.setDimension;

public class NewAppointmentScreen extends ScreenWithParent {
    Toolbar toolbar;
    AppointmentPanel appointmentPanel;

    public NewAppointmentScreen(App parentFrame) {
        super(parentFrame);
        this.setLayout(new BorderLayout());
        this.parentFrame = parentFrame;
        setUp();
    }

    private void setUp() {
        toolbar = new Toolbar(this, Screens.APPOINTMENTS);
        add(toolbar,BorderLayout.NORTH);
        appointmentPanel = new AppointmentPanel();
        add(appointmentPanel,BorderLayout.CENTER);
    }
    class AppointmentPanel extends JPanel {
        List<Doctor> doctors;
        List<Specialty> specialties;

        JPanel contentPanel;

        JLabel titleLabel;

        JLabel dateLabel;
        JLabel timeLabel;
        JFormattedTextField dateField;
        JFormattedTextField timeField;
        JLabel specialtyLabel;
        JLabel doctorLabel;
        JComboBox specialtyComboBox;
        JComboBox doctorComboBox;
        JLabel messageLabel;
        JButton confirmAppointmentButton;

        public AppointmentPanel() {
            super(new GridBagLayout());
            setUp();
            add(contentPanel);
        }

        private void setUp() {
            setUpPanel();
            createElements();
            addElements();
        }

        private void setUpPanel() {
            MigLayout panelLayout = new MigLayout("wrap 2, fillx, insets 30 50 0 50", "[500, fill]100[500, fill]", "[fill]");
            contentPanel = new JPanel();
            contentPanel.setLayout(panelLayout);
            Dimension dim = new Dimension(1200, 600);
            contentPanel.setPreferredSize(dim);
            contentPanel.setMinimumSize(dim);
            contentPanel.setMaximumSize(dim);
            contentPanel.setBackground(Color.WHITE);
            contentPanel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        }

        private void createElements() {
            titleLabel = new JLabel("Nueva cita");
            titleLabel.putClientProperty(FlatClientProperties.STYLE, "font: 20");

            dateLabel = new JLabel("Fecha");

            timeLabel = new JLabel("Hora");

            DatePicker datePicker = new DatePicker();
            dateField = new JFormattedTextField();
            datePicker.setEditor(dateField);
            datePicker.setDateSelectionAble(localDate -> localDate.isAfter(LocalDate.now()));

            TimePicker timePicker = new TimePicker();
            timeField = new JFormattedTextField();
            timePicker.setEditor(timeField);
            timePicker.setTimeSelectionAble(new TimeSelectionAble() {
                @Override
                public boolean isTimeSelectedAble(LocalTime localTime, boolean b) {
                    return localTime.isAfter(LocalTime.of(8,0)) && localTime.isBefore(LocalTime.of(20,0));
                }
            });

            specialtyLabel = new JLabel("Especialidad");

            doctorLabel = new JLabel("Doctor");

            specialties = getParentFrame().getDbConnection().getSpecialties();

            String[] specialtyNames = new String[specialties.size() + 1];

            specialtyNames[0] = "-";
            for (int i = 0; i < specialties.size(); i++) {
                Specialty specialty = specialties.get(i);
                specialtyNames[i + 1] = specialty.getName();
            }

            specialtyComboBox = new JComboBox(specialtyNames);

            specialtyComboBox.addActionListener(_ -> refreshDoctorComboBox());

            String[] doctorNames = {"-"};
            doctorComboBox = new JComboBox(doctorNames);

            createMessageLabel();

            confirmAppointmentButton = new JButton("Confirmar");
            setDimension(confirmAppointmentButton, 300, 50);
            confirmAppointmentButton.addActionListener(e -> {
                attemptAddAppointment(datePicker, timePicker);
            });

        }

        private void attemptAddAppointment(DatePicker datePicker, TimePicker timePicker) {
            LocalDate date = datePicker.getSelectedDate();
            LocalTime time = timePicker.getSelectedTime();
            int specialtyIndex = specialtyComboBox.getSelectedIndex();
            int doctorIndex = doctorComboBox.getSelectedIndex();

            dateField.setBackground(Color.WHITE);
            timeField.setBackground(Color.WHITE);
            specialtyComboBox.setBackground(Color.WHITE);
            doctorComboBox.setBackground(Color.WHITE);

            int errors = 0;

            if (date == null) {
                dateField.setBackground(COLORS.RED_LIGHT);
                errors++;
            }
            if (time == null) {
                timeField.setBackground(COLORS.RED_LIGHT);
                errors++;
            }
            if (specialtyIndex == 0) {
                specialtyComboBox.setBackground(COLORS.RED_LIGHT);
                errors++;
            }
            if (doctorIndex == 0) {
                doctorComboBox.setBackground(COLORS.RED_LIGHT);
                errors++;
            }

            if (errors > 0) {
                showMessage("Datos erróneos. Revisa los campos en rojo.");
            } else {
                messageLabel.setVisible(false);
                Specialty specialty = specialties.get(specialtyIndex - 1);
                Doctor doctor = doctors.get(doctorIndex - 1);

                Appointment appointment = new Appointment();
                appointment.setDoctor(doctor);
                appointment.setPatient(getParentFrame().getPatient());
                appointment.setDate(date);
                appointment.setTime(time);
                appointment.setState(State.SCHEDULED);

                getParentFrame().getDbConnection().addAppointment(appointment);

                System.out.println("Date: " + date);
                System.out.println("Time: " + time);
                System.out.println("Specialty: " + specialty.getName());
                System.out.println("Doctor: " + doctor.getName());

                dateField.setValue(null);
                timeField.setValue(null);
                doctorComboBox.setSelectedIndex(0);
                specialtyComboBox.setSelectedIndex(0);
            }
        }

        private void addElements() {
            contentPanel.add(titleLabel, "span");

            contentPanel.add(new JSeparator(), "gapy 10 20, span");

            contentPanel.add(dateLabel);
            contentPanel.add(timeLabel);

            contentPanel.add(dateField,  "gapBottom 35");
            contentPanel.add(timeField,  "gapBottom 35");

            contentPanel.add(specialtyLabel);
            contentPanel.add(doctorLabel);

            contentPanel.add(specialtyComboBox,  "gapBottom 20");
            contentPanel.add(doctorComboBox,  "gapBottom 20");

            contentPanel.add(messageLabel, "span, gapy 0 20");

            contentPanel.add(confirmAppointmentButton, "span, center");
        }
        private void createMessageLabel() {
            messageLabel = new JLabel();
            messageLabel.putClientProperty(FlatClientProperties.STYLE, "background:#FF96A8FF; foreground:#B51D37FF; arc:6");
            messageLabel.setOpaque(true);
            messageLabel.setVisible(false);
            Dimension dim = new Dimension(500, 35);
            messageLabel.setMinimumSize(dim);
            messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        private void showMessage(String message) {
            messageLabel.setText(message);
            messageLabel.setVisible(true);
        }
        private void refreshDoctorComboBox() {
            int selected = specialtyComboBox.getSelectedIndex();
            if (selected > 0) {
                Specialty specialty = specialties.get(selected - 1);
                System.out.println("Specialty: " + specialty.getName());
                doctors = getParentFrame().getDbConnection().getDoctorsBySpecialty(specialty);
                System.out.println("Doctors: " + doctors.size());
                doctorComboBox.removeAllItems();
                doctorComboBox.addItem("-");
                for (int i = 0; i < doctors.size(); i++) {
                    Doctor doctor = doctors.get(i);
                    System.out.println("Doctor: " + doctor.getName());
                    String fullName = doctor.getName() + " " + doctor.getSurname1() + " " + (doctor.getSurname2() == null ? "" : doctor.getSurname2());
                    doctorComboBox.addItem(fullName);
                }
                doctorComboBox.revalidate();
                doctorComboBox.repaint();
            } else {
                doctorComboBox.removeAllItems();
                doctorComboBox.addItem("-");
            }
        }
    }

}

