package main.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class AppointmentScreen extends ScreenWithParent {
    Toolbar toolbar;
    AppointmentPanel appointmentPanel;

    public AppointmentScreen(App parentFrame) {
        super(parentFrame);
        this.setLayout(new BorderLayout());
        this.parentFrame = parentFrame;
        setUp();
    }

    private void setUp() {
        toolbar = new Toolbar(this, Screens.OPTIONS);
        add(toolbar,BorderLayout.NORTH);
        appointmentPanel = new AppointmentPanel();
        add(appointmentPanel,BorderLayout.CENTER);
    }
    private AppointmentScreen getAppointmentScreen() {
        return this;
    }
    class AppointmentPanel extends JPanel {
        JPanel contentPanel;

        JLabel titleLabel;

        JButton newAppointmentButton;
        JButton editAppointmentsButton;
        JButton pastAppointmentsButton;


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
            MigLayout panelLayout = new MigLayout("wrap 2, fillx, insets 30 50 0 50", "[fill]50[fill]", "[fill]20[fill]");
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
            titleLabel = new JLabel("Menú de citas");
            titleLabel.putClientProperty(FlatClientProperties.STYLE, "font: 24");

            FlatSVGIcon newAppointmentIcon = new FlatSVGIcon("icons/newAppointment.svg",80,80);
            FlatSVGIcon editAppointmentsIcon = new FlatSVGIcon("icons/editAppointment.svg",80,80);
            FlatSVGIcon pastAppointmentsIcon = new FlatSVGIcon("icons/history.svg",80,80);
            FlatSVGIcon futureAppointmentsIcon = new FlatSVGIcon("icons/pending.svg",80,80);

            newAppointmentButton = new JButton("Nueva cita",newAppointmentIcon);
            editAppointmentsButton = new JButton("Editar o eliminar citas",editAppointmentsIcon);
            pastAppointmentsButton = new JButton("Historial de citas",pastAppointmentsIcon);


            newAppointmentButton.setBackground(COLORS.PRIMARY_LIGHT);
            newAppointmentButton.putClientProperty(FlatClientProperties.STYLE, "arc: 15");
            newAppointmentButton.setFont(UIManager.getFont("bold.font").deriveFont(Font.PLAIN, 20));
            CommonMethods.setDimension(newAppointmentButton, 800, 150);

            editAppointmentsButton.setBackground(COLORS.PRIMARY_LIGHT);
            editAppointmentsButton.putClientProperty(FlatClientProperties.STYLE, "arc: 15");
            editAppointmentsButton.setFont(UIManager.getFont("bold.font").deriveFont(Font.PLAIN, 20));
            CommonMethods.setDimension(editAppointmentsButton, 500, 150);

            pastAppointmentsButton.setBackground(COLORS.PRIMARY_LIGHT);
            pastAppointmentsButton.putClientProperty(FlatClientProperties.STYLE, "arc: 15");
            pastAppointmentsButton.setFont(UIManager.getFont("bold.font").deriveFont(Font.PLAIN, 20));
            CommonMethods.setDimension(pastAppointmentsButton, 500, 150);

            newAppointmentButton.addActionListener(_ -> {getParentFrame().showScreen(Screens.NEW_APPOINTMENT);});
            editAppointmentsButton.addActionListener(_ -> {getParentFrame().showScreen(Screens.EDIT_APPOINTMENTS);});
            pastAppointmentsButton.addActionListener(_ -> {getParentFrame().showScreen(Screens.APPOINTMENT_HISTORY);});
        }


        private void addElements() {
            contentPanel.add(titleLabel, "span");

            contentPanel.add(new JSeparator(), "gapy 10 20, span");

            contentPanel.add(newAppointmentButton, "span, center");
            contentPanel.add(editAppointmentsButton, "center");
            contentPanel.add(pastAppointmentsButton, "center");
        }
    }
}

