package scenes;

import panes.AppConstants;
import panes.BookingPane;
import javafx.scene.Scene;

public class BookingScene extends Scene {
    public BookingScene() {
        super(new BookingPane(), AppConstants.SCREEN_WIDTH, AppConstants.SCREEN_HEIGHT);
    }
}