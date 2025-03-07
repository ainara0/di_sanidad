package main.ui;

import com.formdev.flatlaf.FlatLaf;
import main.connection.DBConnection;
import main.connection.Patient;
import theme.Theme1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static main.ui.CommonMethods.getFontFromPath;

public class App extends JFrame {

    private DBConnection dbConnection;
    private Patient patient;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::new);
    }

    public App() {
        super("Hospital Sa Ronda");
        dbConnection = new DBConnection();
        if (!dbConnection.openSession()) {
            System.out.println("Failed to connect to database");
            return;
        }
        setUpFrame();
        setStyles();
        showScreen(Screens.LOGIN);
        setVisible(true);
    }

    void setUpFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(COLORS.BACKGROUND);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (!dbConnection.close()) {
                    System.out.println("An error occurred while closing database connection.");
                } else {
                    System.out.println("Database connection closed.");
                }
            }
        });
    }
    private void setStyles() {
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(getFontFromPath(FONTS.MONTSERRAT_REGULAR));
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(getFontFromPath(FONTS.MONTSERRAT_BOLD));
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(getFontFromPath(FONTS.MONTSERRAT_LIGHT));
        FlatLaf.registerCustomDefaultsSource( "themes" );
        Theme1.setup();
    }
    public void showScreen(Screens screens) {
        getContentPane().removeAll();
        switch (screens) {
            case LOGIN:
                getContentPane().add(new LoginScreen(this));
                break;
            case SIGNUP:
                getContentPane().add(new SignUpScreen(this));
                break;
            case OPTIONS:
                getContentPane().add(new OptionScreen(this));
                break;
            case APPOINTMENTS:
                getContentPane().add(new AppointmentScreen(this));
                break;
            case NEW_APPOINTMENT:
                getContentPane().add(new NewAppointmentScreen(this));
                break;
            case EDIT_APPOINTMENTS:
                getContentPane().add(new EditAppointmentsScreen(this));
                break;
            case APPOINTMENT_HISTORY:
                getContentPane().add(new AppointmentHistoryScreen(this));
                break;
            case TESTS:
                getContentPane().add(new TestResultsScreen(this));
                break;
            case ACCOUNT:
                getContentPane().add(new AccountScreen(this));
                break;
            case PRESCRIPTIONS:
                getContentPane().add(new PrescriptionHistoryScreen(this));
        }
        revalidate();
        repaint();
    }

    public void showScreen(EditAppointmentScreen screen) {
        getContentPane().removeAll();
        getContentPane().add(screen);
        revalidate();
        repaint();
    }

    public DBConnection getDbConnection() {
        return dbConnection;
    }

    public Patient getPatient() {
        dbConnection.refresh(patient);
        return patient;
    }

    public void setDbConnection(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

}