package scenes;

import panes.AppConstants;
import panes.ConfigPane;
import javafx.scene.Scene;

public class ConfigScene extends Scene {
    public ConfigScene() {
        super(new ConfigPane(), AppConstants.SCREEN_WIDTH, AppConstants.SCREEN_HEIGHT);
    }
}