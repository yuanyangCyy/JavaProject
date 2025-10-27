package moudel;

public class VehicleType {
    private String type;
    private double dailyRate;
    private String imagePath; // NEW: Path to the vehicle image

    /**
     * Default constructor.
     */
    public VehicleType() {
    }

    /**
     * Parameterized constructor.
     *
     * @param type      The name of the vehicle type (e.g., "Small Sedan").
     * @param dailyRate The cost per day to rent this vehicle.
     * @param imagePath The resource path to the vehicle's image.
     */
    public VehicleType(String type, double dailyRate, String imagePath) {
        this.type = type;
        this.dailyRate = dailyRate;
        this.imagePath = imagePath;
    }

    // --- Getters and Setters ---

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * The string representation used in the ComboBox.
     *
     * @return The name of the vehicle type.
     */
    @Override
    public String toString() {
        return type;
    }
}
