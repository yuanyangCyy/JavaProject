package scenes;

import panes.AppConstants;
import panes.IntroPane;
import javafx.scene.Scene;

public class IntroScene extends Scene {
    public IntroScene() {
        super(new IntroPane(), AppConstants.SCREEN_WIDTH, AppConstants.SCREEN_HEIGHT);
    }
}