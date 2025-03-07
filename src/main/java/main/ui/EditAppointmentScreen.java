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

public class EditAppointmentScreen extends ScreenWithParent {
    Toolbar toolbar;
    AppointmentPanel appointmentPanel;
    Appointment appointment;

    public EditAppointmentScreen(App parentFrame, Appointment appointment) {
        super(parentFrame);
        this.setLayout(new BorderLayout());
        this.parentFrame = parentFrame;
        this.appointment = appointment;
        setUp();
    }

    private void setUp() {
        toolbar = new Toolbar(this, Screens.EDIT_APPOINTMENTS);
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
        JButton cancelAppointmentButton;

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
            MigLayout panelLayout = new MigLayout("wrap 2, fillx, insets 30 50 0 50", "[fill]100[fill]", "[fill]");
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
            titleLabel = new JLabel("Editar cita");
            titleLabel.putClientProperty(FlatClientProperties.STYLE, "font: 30");

            dateLabel = new JLabel("Fecha");

            timeLabel = new JLabel("Hora");

            DatePicker datePicker = new DatePicker();
            dateField = new JFormattedTextField();
            datePicker.setEditor(dateField);
            datePicker.setDateSelectionAble(localDate -> localDate.isAfter(LocalDate.now()));
            LocalDate appointmentDate = appointment.getDate();
            String dateString = CommonMethods.getDateString(appointmentDate);
            dateField.setValue(dateString);

            TimePicker timePicker = new TimePicker();
            timeField = new JFormattedTextField();
            timePicker.setEditor(timeField);
            timePicker.setTimeSelectionAble(new TimeSelectionAble() {
                @Override
                public boolean isTimeSelectedAble(LocalTime localTime, boolean b) {
                    return localTime.isAfter(LocalTime.of(8,0)) && localTime.isBefore(LocalTime.of(20,0));
                }
            });
            LocalTime appointmentTime = appointment.getTime();
            String timeString = CommonMethods.getTimeString12H(appointmentTime);
            timeField.setValue(timeString);

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

            Specialty specialty = appointment.getDoctor().getSpecialty();
            int specialtyIndex = specialties.indexOf(specialty);
            specialtyComboBox.setSelectedIndex(specialtyIndex + 1);

            String[] doctorNames = {"-"};
            doctorComboBox = new JComboBox(doctorNames);

            refreshDoctorComboBox();

            specialtyComboBox.addActionListener(_ -> refreshDoctorComboBox());

            Doctor doctor = appointment.getDoctor();
            int doctorIndex = doctors.indexOf(doctor);
            doctorComboBox.setSelectedIndex(doctorIndex + 1);

            createMessageLabel();

            confirmAppointmentButton = new JButton("Confirmar");
            setDimension(confirmAppointmentButton, 300, 50);
            confirmAppointmentButton.addActionListener(e -> {
                attemptEditAppointment(datePicker, timePicker);
            });

            cancelAppointmentButton = new JButton("Cancelar cita");
            setDimension(cancelAppointmentButton, 300, 50);
            cancelAppointmentButton.addActionListener(e -> {
                appointment.setState(State.CANCELLED);
                titleLabel.setText("Editar cita (cancelada)");
                appointmentDone();
            });

            if (appointment.getState() == State.CANCELLED) {
                titleLabel.setText("Editar cita (cancelada)");
                appointmentDone();
            }else if (appointment.getState() == State.FINISHED) {
                titleLabel.setText("Editar cita (terminada)");
                appointmentDone();
            }
        }
        private void appointmentDone() {
            dateField.setEnabled(false);
            timeField.setEnabled(false);
            specialtyComboBox.setEnabled(false);
            doctorComboBox.setEnabled(false);
            confirmAppointmentButton.setEnabled(false);
            cancelAppointmentButton.setEnabled(false);
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

            contentPanel.add(confirmAppointmentButton, "center");
            contentPanel.add(cancelAppointmentButton, "center");
        }
        private void createMessageLabel() {
            messageLabel = new JLabel("Message");
            messageLabel.putClientProperty(FlatClientProperties.STYLE, "arc:6");
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
                doctors = getParentFrame().getDbConnection().getDoctorsBySpecialty(specialty);
                doctorComboBox.removeAllItems();
                doctorComboBox.addItem("-");
                for (int i = 0; i < doctors.size(); i++) {
                    Doctor doctor = doctors.get(i);
                    String fullName = doctor.getName() + " " + doctor.getSurname1() + " " + (doctor.getSurname2() == null ? "" : doctor.getSurname2());
                    doctorComboBox.addItem(fullName);
                }
                doctorComboBox.revalidate();
                doctorComboBox.repaint();
            } else {
                doctorComboBox.removeAllItems();
            }
        }
        private void attemptEditAppointment(DatePicker datePicker, TimePicker timePicker) {
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
                Doctor doctor = doctors.get(doctorIndex - 1);

                Appointment newAppointment = new Appointment();
                newAppointment.setDoctor(doctor);
                newAppointment.setPatient(getParentFrame().getPatient());
                newAppointment.setDate(date);
                newAppointment.setTime(time);
                getParentFrame().getDbConnection().editAppointment(appointment, newAppointment);
                showSuccessMessage("Datos actualizados correctamente");
            }
        }

        private void showErrorMessage(String message) {
            messageLabel.putClientProperty(FlatClientProperties.STYLE, "background:#FF96A8FF; foreground:#B51D37FF");
            messageLabel.setText(message);
            messageLabel.setVisible(true);
        }
        private void showSuccessMessage(String message) {
            messageLabel.putClientProperty(FlatClientProperties.STYLE, "background:#9bc2b0; foreground:#049152");
            messageLabel.setText(message);
            messageLabel.setVisible(true);
        }

    }

}

