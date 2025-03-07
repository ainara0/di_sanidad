package main.ui;

import com.formdev.flatlaf.FlatClientProperties;
import main.connection.Patient;
import net.miginfocom.swing.MigLayout;
import raven.datetime.DatePicker;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalDate;


public class SignUpScreen extends ScreenWithParent {
    JPanel panel;
    JScrollPane scrollPane;
    JLabel logoLabel;

    JLabel titleLabel;

    JLabel dniLabel;
    JTextField dniField;

    JLabel nameLabel;
    JTextField nameField;

    JLabel surname1Label;
    JTextField surname1Field;

    JLabel surname2Label;
    JTextField surname2Field;

    JLabel birthLabel;
    DatePicker datePicker;
    JFormattedTextField birthField;

    JLabel mailLabel;
    JTextField mailField;

    JLabel telephoneLabel;
    JTextField telephoneField;

    JTextField cityField;
    JLabel cityLabel;

    JTextField postalCodeField;
    JLabel postalCodeLabel;

    JTextField streetField;
    JLabel streetLabel;

    JTextField numberField;
    JLabel numberLabel;

    JLabel passwordLabel;
    JPasswordField passwordField;

    JButton signUpButton;
    JLabel messageLabel;
    JButton loginButton;

    SignUpScreen(App parentFrame) {
        super(parentFrame);
        this.setLayout(new GridBagLayout());
        setUpPanel();
        createElements();
        addElementsToPanel();
        add(scrollPane);
        addListenerToSignUpButton();
        setVisible(true);
    }
    private void setUpPanel() {
        setLayout(new GridBagLayout());
        panel = new JPanel();
        MigLayout layout = new MigLayout("wrap 2, fillx, align center, insets 20 50 20 50", "35[40%!,fill]20[40%!,fill]35", "");
        panel.setLayout(layout);
        scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        Dimension dim = new Dimension(900, 700);
        scrollPane.setPreferredSize(dim);
        scrollPane.setMinimumSize(dim);
        scrollPane.setMaximumSize(dim);
        scrollPane.getVerticalScrollBar().setUnitIncrement(15);
        panel.setBackground(Color.WHITE);
    }
    private void addElementsToPanel() {
        panel.add(logoLabel,"span");
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(titleLabel,"span");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);


        panel.add(new JSeparator(), "span, gapy 15 30");

        JLabel personalDataLabel = new JLabel("Datos personales");
        panel.add(personalDataLabel,"span");

        panel.add(new JSeparator(), "span, gapy 10 5");

        panel.add(dniLabel,"span");
        panel.add(dniField,"span, gapBottom 15");

        panel.add(nameLabel);
        panel.add(surname1Label);

        panel.add(nameField, "gapBottom 15");
        panel.add(surname1Field, "gapBottom 15");

        panel.add(surname2Label);
        panel.add(birthLabel);

        panel.add(surname2Field, "gapBottom 15");
        panel.add(birthField, "gapBottom 15");


        JLabel contactLabel = new JLabel("Datos de contacto");
        panel.add(contactLabel,"span");

        panel.add(new JSeparator(), "span, gapy 10 5");

        panel.add(mailLabel);
        panel.add(telephoneLabel);

        panel.add(mailField, "gapBottom 15");
        panel.add(telephoneField, "gapBottom 15");

        JLabel addressLabel = new JLabel("Dirección");
        panel.add(addressLabel,"span");

        panel.add(new JSeparator(), "span, gapy 10 5");

        panel.add(cityLabel);
        panel.add(postalCodeLabel);

        panel.add(cityField, "gapBottom 15");
        panel.add(postalCodeField,"gapBottom 15");

        panel.add(streetLabel);
        panel.add(numberLabel);

        panel.add(streetField, "gapBottom 15");
        panel.add(numberField, "gapBottom 15");

        panel.add(new JSeparator(), "span, gapy 10 5");

        panel.add(passwordLabel, "span");

        panel.add(passwordField,"span, gapBottom 15");

        panel.add(signUpButton, "span, gapBottom 15");

        panel.add(messageLabel, "span");

        panel.add(new JSeparator(), "gapy 15 15, span");

        panel.add(loginButton, "span");
    }
    private void createElements() {
        logoLabel = CommonMethods.getImageLabel("src/main/resources/icons/logo.png",200,200);
        titleLabel = new JLabel("Crear cuenta");
        titleLabel.putClientProperty(FlatClientProperties.STYLE, "font: 24");

        dniLabel = new JLabel("DNI");
        dniField = new JTextField();

        nameLabel = new JLabel("Nombre");
        nameField = new JTextField();

        surname1Label = new JLabel("Primer apellido");
        surname1Field = new JTextField();

        surname2Label = new JLabel("Segundo apellido");
        surname2Field = new JTextField();

        birthLabel = new JLabel("Fecha de Nacimiento");
        datePicker = new DatePicker();
        birthField = new JFormattedTextField();
        datePicker.setEditor(birthField);
        datePicker.setDateSelectionAble(localDate -> localDate.isBefore(LocalDate.now()));

        mailLabel = new JLabel("Email");
        mailField = new JTextField();
        mailField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "ejemplo@correo.com");

        telephoneLabel = new JLabel("Telefono");
        telephoneField = new JTextField();
        telephoneField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "9 caracteres numéricos");

        cityLabel = new JLabel("Ciudad");
        cityField = new JTextField();

        postalCodeLabel = new JLabel("Código postal");
        postalCodeField = new JTextField();
        postalCodeField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "5 caracteres numéricos");

        streetLabel = new JLabel("Calle");
        streetField = new JTextField();

        numberLabel = new JLabel("Número");
        numberField = new JTextField();

        passwordLabel = new JLabel("Contraseña");
        passwordField = new JPasswordField();
        passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "8-16 caracteres");
        addListenerToPasswordField();

        signUpButton = new JButton("Sign Up");
        addListenerToSignUpButton();

        createMessageLabel();

        loginButton = new JButton("Log in");
        loginButton.putClientProperty(FlatClientProperties.STYLE,
                "margin:1,5,1,5;" +
                        "borderWidth:0;" +
                        "focusWidth:0;" +
                        "innerFocusWidth:0;" +
                        "foreground:$Component.accentColor;" +
                        "background:null;");
        addListenerToLogInButton();
    }
    private void createMessageLabel() {
        messageLabel = new JLabel("Error");
        messageLabel.putClientProperty(FlatClientProperties.STYLE, "background:#FF96A8FF; foreground:#B51D37FF; arc:6");
        messageLabel.setOpaque(true);
        messageLabel.setVisible(false);
        Dimension dim = new Dimension(panel.getPreferredSize().width - 100, 40);
        messageLabel.setMinimumSize(dim);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }
    private void addListenerToPasswordField() {
        passwordField.addKeyListener( new KeyListener() {
                                          @Override
                                          public void keyTyped(KeyEvent e) {

                                          }

                                          @Override
                                          public void keyPressed(KeyEvent e) {
                                              if(e.getKeyChar() == KeyEvent.VK_ENTER){
                                                  attemptSignUp();
                                              }
                                          }

                                          @Override
                                          public void keyReleased(KeyEvent e) {

                                          }
                                      }
        );
    }
    private void addListenerToSignUpButton() {
        signUpButton.addActionListener(_ -> attemptSignUp());
    }
    private void addListenerToLogInButton() {
        loginButton.addActionListener(_ -> parentFrame.showScreen(Screens.LOGIN));
    }
    private void attemptSignUp() {
        JTextComponent[] fields = {
                dniField,
                nameField,
                surname1Field,
                surname2Field,
                birthField,
                mailField,
                telephoneField,
                cityField,
                postalCodeField,
                streetField,
                numberField,
                passwordField,
        };

        for (int i = 0; i < fields.length; i++) {
            fields[i].setBackground(Color.white);
        }

        String dni = dniField.getText().toUpperCase();
        if (getParentFrame().getDbConnection().getPatient(dni) != null) {
            dniField.setBackground(COLORS.RED_LIGHT);
            showMessage("Patient already exists. Log in or input a different DNI.");
            return;
        }
        Patient patient = new Patient();
        patient.setDni(dni);
        patient.setName(nameField.getText());
        patient.setSurname1(surname1Field.getText());
        patient.setSurname2(surname2Field.getText());
        patient.setBirthDate(datePicker.getSelectedDate());
        patient.setEmail(mailField.getText());
        patient.setTelephone(telephoneField.getText());
        patient.setCity(cityField.getText());
        patient.setPostalCode(postalCodeField.getText());
        patient.setStreet(streetField.getText());
        patient.setNumber(numberField.getText());
        String password = new String(passwordField.getPassword());
        patient.setPassword(password);

        boolean[] correctData = DataCheck.checkPatientData(patient);

        int errors = 0;

        for (int i = 0; i < correctData.length; i++) {
            if (!correctData[i]) {
                fields[i].setBackground(COLORS.RED_LIGHT);
                errors++;
            }
        }
        if (errors > 0) {
            showMessage("Datos erróneos. Revisa los campos en rojo.");
        } else {
            getParentFrame().getDbConnection().addPatient(patient);
            patient = getParentFrame().getDbConnection().getPatient(patient.getDni());
            getParentFrame().setPatient(patient);
            parentFrame.showScreen(Screens.OPTIONS);
        }
    }
    private void showMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }
}
