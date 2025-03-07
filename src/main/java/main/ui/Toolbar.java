package main.ui;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;

public class Toolbar extends JToolBar {
    ScreenWithParent panel;
    Screens returnScreens = null;

    JPanel leftPanel = new JPanel();
    JPanel rightPanel = new JPanel();

    JButton logoutButton;
    JButton accountButton;

    public Toolbar(ScreenWithParent panel) {
        super();
        this.panel = panel;
        setUp();
    }

    public Toolbar(ScreenWithParent panel, Screens returnScreens) {
        super();
        this.panel = panel;
        this.returnScreens = returnScreens;
        setUp();
    }

    private void setUp() {
        setLayout(new BorderLayout());
        setBackground(COLORS.PRIMARY_DARK);

        leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        leftPanel.setBackground(COLORS.PRIMARY_DARK);
        rightPanel.setBackground(COLORS.PRIMARY_DARK);

        if (returnScreens != null) {
            setReturnButton();
        }
        setLogoutButton();
        setAccountIcon();

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    private void setLogoutButton() {
        FlatSVGIcon logoutIcon = new FlatSVGIcon("icons/logout.svg",20,20);
        logoutButton = new JButton(logoutIcon);
        logoutButton.setBackground(COLORS.BACKGROUND);
        logoutButton.addActionListener(e -> {
                    panel.getParentFrame().setPatient(null);
                    panel.getParentFrame().showScreen(Screens.LOGIN);
                });
        rightPanel.add(logoutButton);
    }

    private void setAccountIcon() {
        FlatSVGIcon accountIcon = new FlatSVGIcon("icons/account.svg",20,20);
        accountButton = new JButton(accountIcon);
        accountButton.setBackground(COLORS.BACKGROUND);
        accountButton.addActionListener(e -> panel.getParentFrame().showScreen(Screens.ACCOUNT));
        leftPanel.add(accountButton);
    }

    private void setReturnButton() {
        FlatSVGIcon returnIcon = new FlatSVGIcon("icons/back.svg",20,20);
        JButton returnButton = new JButton(returnIcon);
        returnButton.setBackground(COLORS.BACKGROUND);
        returnButton.addActionListener(e -> panel.getParentFrame().showScreen(returnScreens));
        leftPanel.add(returnButton);
    }
}