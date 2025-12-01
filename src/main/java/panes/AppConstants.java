package panes;

import javafx.geometry.Insets;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class AppConstants {

    // Application Info
    public static final String APP_NAME = "Car Rental System";
    public static final String BRANCH_LOCATION = "Windsor, Ontario";

    // Screen Dimensions
    public static final int SCREEN_WIDTH = 1024;
    public static final int SCREEN_HEIGHT = 768;
    public static final int BOOKING_SCREEN_WIDTH = 600;
    public static final int BOOKING_SCREEN_HEIGHT = 700;

    // Button Labels
    public static final String BTN_BOOK = "Book a Car";
    public static final String BTN_MANAGE = "Manage Bookings";
    public static final String BTN_ADMIN = "Admin Dashboard";
    public static final String BTN_BACK = "Back";

    // Colors
    public static final Color PRIMARY_COLOR = Color.rgb(128, 0, 0);
    public static final Color SECONDARY_COLOR = Color.rgb(165, 42, 42);
    public static final Color TERTIARY_COLOR = Color.rgb(255, 240, 245);
    public static final Color WHITE_COLOR = Color.WHITE;
    public static final Color BLACK_COLOR = Color.BLACK;
    public static final Color GRAY_COLOR = Color.GRAY;
    public static final Color SUCCESS_COLOR = Color.rgb(56, 176, 0);
    public static final Color WARNING_COLOR = Color.rgb(247, 119, 0);

    // Background Fills
    public static final BackgroundFill primaryColorFill = new BackgroundFill(
            PRIMARY_COLOR, new CornerRadii(0), new Insets(0)
    );
    public static final BackgroundFill secondaryColorRadii = new BackgroundFill(
            SECONDARY_COLOR, new CornerRadii(5), new Insets(0)
    );
    public static final BackgroundFill tertiaryColorRadii = new BackgroundFill(
            TERTIARY_COLOR, new CornerRadii(5), new Insets(0)
    );
    public static final BackgroundFill backgroundColor = new BackgroundFill(
            Color.rgb(247, 247, 248), new CornerRadii(0), new Insets(0)
    );
    public static final BackgroundFill backgroundWhiteColorRadii = new BackgroundFill(
            Color.rgb(247, 247, 248), new CornerRadii(5), new Insets(0)
    );

    // Font Settings
    private static String FONT_FAMILY = "Tahoma";

    public static void setFontFamily(String fontFamily) {
        FONT_FAMILY = fontFamily;
    }

    public static String getFontFamily() {
        return FONT_FAMILY;
    }
}