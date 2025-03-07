package main.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OptionScreen extends ScreenWithParent {
    Toolbar toolbar;
    OptionPanel optionPanel;


    public OptionScreen(App parentFrame) {
        super(parentFrame);
        this.setLayout(new BorderLayout());
        this.parentFrame = parentFrame;
        setUp();
    }

    private void setUp() {
        toolbar = new Toolbar(this);
        add(toolbar,BorderLayout.NORTH);
        optionPanel = new OptionPanel();
        add(optionPanel,BorderLayout.CENTER);
    }

    private OptionScreen getOptionScreen() {
        return this;
    }

    class OptionPanel extends JScrollPane {
        JButton appointmentButton;
        JButton prescriptionHistoryButton;
        JButton testResultsButton;
        JButton messagesButton;
        JPanel contentPanel;

        public OptionPanel() {
            super();
            setUp();
        }

        private void setUp() {
            contentPanel = new JPanel(new MigLayout("wrap 3, fillx", "[300, fill]", "[300, fill]"));
            this.setViewportView(contentPanel);
            setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            getVerticalScrollBar().setUnitIncrement(15);
            createButtons();
            setActions();
            ArrayList<JButton> buttons = createButtonsList();
            configureButtons(buttons);
        }

        private void createButtons() {
            FlatSVGIcon appointmentIcon = new FlatSVGIcon("icons/appointment.svg",80,80);
            FlatSVGIcon prescriptionHistoryIcon = new FlatSVGIcon("icons/prescription.svg",80,80);
            FlatSVGIcon resultsIcon = new FlatSVGIcon("icons/tests.svg",80,80);
            FlatSVGIcon messagesIcon = new FlatSVGIcon("icons/message.svg",80,80);
            appointmentButton = new JButton("Citas", appointmentIcon);
            prescriptionHistoryButton = new JButton("Historial de recetas", prescriptionHistoryIcon);
            testResultsButton = new JButton("Resultados de pruebas", resultsIcon);
            messagesButton = new JButton("Mensajes", messagesIcon);
        }
        private void setActions() {
            appointmentButton.addActionListener(e -> getOptionScreen().getParentFrame().showScreen(Screens.APPOINTMENTS));
            prescriptionHistoryButton.addActionListener(e -> getOptionScreen().getParentFrame().showScreen(Screens.PRESCRIPTIONS));
            testResultsButton.addActionListener(e -> getOptionScreen().getParentFrame().showScreen(Screens.TESTS));
            messagesButton.addActionListener(e -> getOptionScreen().getParentFrame().showScreen(Screens.MESSAGES));
        }
        private ArrayList<JButton> createButtonsList() {
            ArrayList<JButton> buttons = new ArrayList<>();
            buttons.add(appointmentButton);
            buttons.add(prescriptionHistoryButton);
            buttons.add(testResultsButton);
            buttons.add(messagesButton);
            return buttons;
        }
        private void configureButtons(ArrayList<JButton> buttons) {
            for (JButton button : buttons) {
                button.putClientProperty(FlatClientProperties.STYLE, "arc: 15");
                button.setBackground(Color.WHITE);
                button.setFont(UIManager.getFont("bold.font").deriveFont(Font.PLAIN, 20));
                contentPanel.add(button);
            }
        }
    }
}
