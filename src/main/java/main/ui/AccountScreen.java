package main.ui;

import com.formdev.flatlaf.FlatClientProperties;
import main.connection.Patient;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class AccountScreen extends ScreenWithParent {
    Toolbar toolbar;
    JPanel mainPanel;
    AccountPanel accountPanel;


    public AccountScreen(App parentFrame) {
        super(parentFrame);
        this.setLayout(new BorderLayout());
        this.parentFrame = parentFrame;
        setUp();
    }
    private void setUp() {
        toolbar = new Toolbar(this, Screens.OPTIONS);
        accountPanel = new AccountPanel();
        mainPanel = new JPanel(new GridBagLayout());

        mainPanel.add(accountPanel);
        add(toolbar,BorderLayout.NORTH);
        add(mainPanel,BorderLayout.CENTER);
    }
    class AccountPanel extends JScrollPane {
        JPanel contentPanel;

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
        JTextField birthField;

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

        JButton confirmEditButton;
        JLabel messageLabel;

        AccountPanel() {
            super();
            setUpPanel();
            createPanelElements();
            addElementsToPanel();
        }
        private void setUpPanel() {
            MigLayout layout = new MigLayout("wrap 2, fillx, align center, insets 20 50 20 50", "35[40%!,fill]20[40%!,fill]35", "");
            contentPanel = new JPanel(layout);
            setViewportView(contentPanel);
            setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            putClientProperty(FlatClientProperties.STYLE, "arc:10");
            Dimension dim = new Dimension(1000, 700);
            CommonMethods.setDimension(this,1000, 700);
            getVerticalScrollBar().setUnitIncrement(15);
            contentPanel.setBackground(Color.WHITE);
        }
        private void createPanelElements() {
            Patient patient = getParentFrame().getPatient();

            logoLabel = CommonMethods.getLogoLabel(200,200);
            titleLabel = new JLabel("Cuenta");
            titleLabel.putClientProperty(FlatClientProperties.STYLE, "font: 24");

            dniLabel = new JLabel("DNI");
            dniField = new JTextField();
            dniField.setText(patient.getDni());
            dniField.setEditable(false);

            nameLabel = new JLabel("Nombre");
            nameField = new JTextField();
            nameField.setText(patient.getName());
            nameField.setEditable(false);

            surname1Label = new JLabel("Primer apellido");
            surname1Field = new JTextField();
            surname1Field.setText(patient.getSurname1());
            surname1Field.setEditable(false);

            surname2Label = new JLabel("Segundo apellido");
            surname2Field = new JTextField();
            surname2Field.setText(patient.getSurname2());
            surname2Field.setEditable(false);

            birthLabel = new JLabel("Fecha de Nacimiento");
            birthField = new JTextField();
            birthField.setText(CommonMethods.getDateString(patient.getBirthDate()));
            birthField.setEditable(false);

            mailLabel = new JLabel("Email");
            mailField = new JTextField();
            mailField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "ejemplo@correo.com");
            mailField.setText(patient.getEmail());

            telephoneLabel = new JLabel("Telefono");
            telephoneField = new JTextField();
            telephoneField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "9 caracteres numéricos");
            telephoneField.setText(patient.getTelephone());

            cityLabel = new JLabel("Ciudad");
            cityField = new JTextField();
            cityField.setText(patient.getCity());

            postalCodeLabel = new JLabel("Código postal");
            postalCodeField = new JTextField();
            postalCodeField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "5 caracteres numéricos");
            postalCodeField.setText(patient.getPostalCode());

            streetLabel = new JLabel("Calle");
            streetField = new JTextField();
            streetField.setText(patient.getStreet());

            numberLabel = new JLabel("Número");
            numberField = new JTextField();
            numberField.setText(patient.getNumber());

            passwordLabel = new JLabel("Contraseña");
            passwordField = new JPasswordField();
            passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "8-16 caracteres");
            addListenerToPasswordField();

            confirmEditButton = new JButton("Cambiar datos");
            confirmEditButton.addActionListener(_ -> attemptChange());
            createMessageLabel();
        }
        private void addElementsToPanel() {
            contentPanel.add(logoLabel,"span");
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

            contentPanel.add(titleLabel,"span");
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);


            contentPanel.add(new JSeparator(), "span, gapy 15 30");

            JLabel personalDataLabel = new JLabel("Datos personales");
            contentPanel.add(personalDataLabel,"span");

            contentPanel.add(new JSeparator(), "span, gapy 10 5");

            contentPanel.add(dniLabel,"span");
            contentPanel.add(dniField,"span, gapBottom 15");

            contentPanel.add(nameLabel);
            contentPanel.add(surname1Label);

            contentPanel.add(nameField, "gapBottom 15");
            contentPanel.add(surname1Field, "gapBottom 15");

            contentPanel.add(surname2Label);
            contentPanel.add(birthLabel);

            contentPanel.add(surname2Field, "gapBottom 15");
            contentPanel.add(birthField, "gapBottom 15");


            JLabel contactLabel = new JLabel("Datos de contacto");
            contentPanel.add(contactLabel,"span");

            contentPanel.add(new JSeparator(), "span, gapy 10 5");

            contentPanel.add(mailLabel);
            contentPanel.add(telephoneLabel);

            contentPanel.add(mailField, "gapBottom 15");
            contentPanel.add(telephoneField, "gapBottom 15");

            JLabel addressLabel = new JLabel("Dirección");
            contentPanel.add(addressLabel,"span");

            contentPanel.add(new JSeparator(), "span, gapy 10 5");

            contentPanel.add(cityLabel);
            contentPanel.add(postalCodeLabel);

            contentPanel.add(cityField, "gapBottom 15");
            contentPanel.add(postalCodeField,"gapBottom 15");

            contentPanel.add(streetLabel);
            contentPanel.add(numberLabel);

            contentPanel.add(streetField, "gapBottom 15");
            contentPanel.add(numberField, "gapBottom 15");

            contentPanel.add(new JSeparator(), "span, gapy 10 5");

            contentPanel.add(passwordLabel, "span");

            contentPanel.add(passwordField,"span, gapBottom 15");

            contentPanel.add(confirmEditButton, "span, gapBottom 15");

            contentPanel.add(messageLabel, "span");

            contentPanel.add(new JSeparator(), "gapy 15 15, span");
        }
        private void createMessageLabel() {
            messageLabel = new JLabel("Message");
            messageLabel.putClientProperty(FlatClientProperties.STYLE, "arc:6");
            messageLabel.setOpaque(true);
            messageLabel.setVisible(false);
            Dimension dim = new Dimension(contentPanel.getPreferredSize().width - 100, 40);
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
                                                      attemptChange();
                                                  }
                                              }

                                              @Override
                                              public void keyReleased(KeyEvent e) {

                                              }
                                          }
            );
        }
        private void attemptChange() {
            JTextComponent[] fieldsToValidate = {
                    mailField,
                    telephoneField,
                    cityField,
                    postalCodeField,
                    streetField,
                    numberField,
                    passwordField,
            };

            for (JTextComponent field : fieldsToValidate) {
                field.setBackground(Color.white);
            }

            String password = new String(passwordField.getPassword());

            boolean[] validations = {
                    DataCheck.validateEmail(mailField.getText()),
                    DataCheck.validateTelephone(telephoneField.getText()),
                    DataCheck.validateVarchar50(cityField.getText()),
                    DataCheck.validatePostalCode(postalCodeField.getText()),
                    DataCheck.validateVarchar50(streetField.getText()),
                    DataCheck.validateNumber(numberField.getText()),
                    DataCheck.validatePassword(password)
            };
            int errors = 0;

            for (int i = 0; i < validations.length; i++) {
                if (!validations[i]) {
                    fieldsToValidate[i].setBackground(COLORS.RED_LIGHT);
                    errors++;
                }
            }

            if (password.isEmpty()) {
                errors--;
                passwordField.setBackground(Color.WHITE);
            }
            if (errors > 0) {
                showErrorMessage("Datos erróneos. Revisa los campos en rojo.");
            } else {
                Patient newPatient = new Patient();
                newPatient.setEmail(mailField.getText());
                newPatient.setTelephone(telephoneField.getText());
                newPatient.setCity(cityField.getText());
                newPatient.setPostalCode(postalCodeField.getText());
                newPatient.setStreet(streetField.getText());
                newPatient.setNumber(numberField.getText());
                newPatient.setPassword(password);
                getParentFrame().getDbConnection().editPatient(getParentFrame().getPatient(), newPatient);
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
