package scenes;

import panes.AppConstants;
import panes.AdminPane;
import javafx.scene.Scene;

public class AdminScene extends Scene {
    public AdminScene() {
        super(new AdminPane(), AppConstants.SCREEN_WIDTH, AppConstants.SCREEN_HEIGHT);
    }
}