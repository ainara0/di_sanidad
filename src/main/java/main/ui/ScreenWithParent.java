package main.ui;

import javax.swing.*;

public abstract class ScreenWithParent extends JPanel {
    App parentFrame;

    public ScreenWithParent(App parentFrame) {
        super();
        this.parentFrame = parentFrame;
    }
    public App getParentFrame() {
        return parentFrame;
    }
}
