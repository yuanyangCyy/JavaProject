package scenes;

import panes.AdminPane;
import panes.AppConstants;
import javafx.scene.Scene;

public class AdminScene extends Scene {
    public AdminScene() {
        super(new AdminPane(), AppConstants.SCREEN_WIDTH, AppConstants.SCREEN_HEIGHT);
    }
}