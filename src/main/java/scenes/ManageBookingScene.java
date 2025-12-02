package scenes;

import panes.AppConstants;
import javafx.scene.Scene;
import panes.ManageBookingPane;

public class ManageBookingScene extends Scene {
    public ManageBookingScene() {
        super(new ManageBookingPane(), AppConstants.SCREEN_WIDTH, AppConstants.SCREEN_HEIGHT);
    }
}