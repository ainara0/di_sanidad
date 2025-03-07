package main.ui;

import com.formdev.flatlaf.*;
import main.connection.Patient;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class LoginScreen extends ScreenWithParent {
    JPanel panel;
    JLabel logoLabel;
    JLabel titleLabel;
    JLabel dniLabel;
    JTextField dniField;
    JLabel passwordLabel;
    JPasswordField passwordField;
    JButton loginButton;
    JLabel messageLabel;
    JButton signUpButton;

    public LoginScreen(App parentFrame) {
        super(parentFrame);
        this.setLayout(new GridBagLayout());
        this.parentFrame = parentFrame;
        setUpPanel();
        createElements();
        addElementsToPanel();
        add(panel);
        setVisible(true);
    }

    private void addListenerToLoginButton() {
        loginButton.addActionListener(_ -> attemptLogIn());
    }
    private void attemptLogIn() {
        String dni = dniField.getText().toUpperCase();
        String password = new String(passwordField.getPassword());
        String hashedPassword = CommonMethods.hash(password);

        Patient patient = getParentFrame().getDbConnection().getPatient(dni, hashedPassword);


        if (patient == null) {
            if (getParentFrame().getDbConnection().getPatient(dni) == null) {
                showMessage("Patient does not exist");
            } else {
                showMessage("Wrong password");
            }
        } else {
            getParentFrame().setPatient(patient);
            getParentFrame().showScreen(Screens.OPTIONS);
        }
    }
    private void addListenerToSignUpButton() {
        signUpButton.addActionListener(_ -> parentFrame.showScreen(Screens.SIGNUP));
    }
    private void createElements() {
        logoLabel = CommonMethods.getImageLabel("src/main/resources/icons/logo.png", 170,170);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        titleLabel = new JLabel("Inicio de sesión");
        titleLabel.putClientProperty(FlatClientProperties.STYLE, "font: 20");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        dniLabel = new JLabel("DNI");
        dniField = new JTextField();

        passwordLabel = new JLabel("Contraseña");
        passwordField = new JPasswordField();
        addListenerToPasswordField();

        loginButton = new JButton("Login");
        addListenerToLoginButton();

        createMessageLabel();

        signUpButton = new JButton("Sign Up");
        signUpButton.putClientProperty(FlatClientProperties.STYLE,
                "margin:1,5,1,5;" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0;" +
                "foreground:$Component.accentColor;" +
                "background:null;");
        addListenerToSignUpButton();
    }
    private void addListenerToPasswordField() {
        passwordField.addKeyListener( new KeyListener() {
                             @Override
                             public void keyTyped(KeyEvent e) {

                             }

                             @Override
                             public void keyPressed(KeyEvent e) {
                                 if(e.getKeyChar() == KeyEvent.VK_ENTER){
                                     attemptLogIn();
                                 }
                             }

                             @Override
                             public void keyReleased(KeyEvent e) {

                             }
                         }
        );
    }
    private void setUpPanel() {
        panel = new JPanel();
        MigLayout layout = new MigLayout("wrap, fillx ,align center, insets 20 50 20 50", "[fill]", "");
        panel.setLayout(layout);
        Dimension dim = new Dimension(550, 700);
        panel.setPreferredSize(dim);
        panel.setMinimumSize(dim);
        panel.setMaximumSize(dim);
        panel.setBackground(Color.WHITE);
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
    }
    private void addElementsToPanel() {
        panel.add(logoLabel);

        panel.add(titleLabel);

        panel.add(new JSeparator(), "gapy 5 5");

        panel.add(dniLabel);
        panel.add(dniField, "gapBottom 10");

        panel.add(passwordLabel);
        panel.add(passwordField,"gapBottom 10");

        panel.add(loginButton, "gapBottom 5");

        panel.add(messageLabel);

        panel.add(new JSeparator(), "gapBottom 10");
        panel.add(signUpButton);
    }
    private void createMessageLabel() {
        messageLabel = new JLabel("Error");
        messageLabel.putClientProperty(FlatClientProperties.STYLE, "background:#FF96A8FF; foreground:#B51D37FF; arc:6");
        messageLabel.setOpaque(true);
        messageLabel.setVisible(false);
        //messageLabel.putClientProperty(FlatClientProperties.STYLE, "margin:10");
        Dimension dim = new Dimension(panel.getPreferredSize().width - 100, 30);
        messageLabel.setMinimumSize(dim);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }
    private void showMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }
}
