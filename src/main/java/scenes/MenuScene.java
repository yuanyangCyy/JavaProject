package scenes;

import panes.AppConstants;
import panes.MenuPane;
import javafx.scene.Scene;

public class MenuScene extends Scene {
    public MenuScene() {
        super(new MenuPane(), AppConstants.SCREEN_WIDTH, AppConstants.SCREEN_HEIGHT);
    }
}