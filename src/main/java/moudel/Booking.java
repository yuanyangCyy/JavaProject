package moudel;

import java.time.LocalDate;

public class Booking {
    private String bookingId;
    private Customer customer;
    private VehicleType vehicleType;
    private LocalDate startDate;
    private int durationDays;
    private double totalPrice;

    /**
     * Default constructor.
     */
    public Booking() {
    }

    /**
     * Parameterized constructor.
     *
     * @param bookingId    The unique ID for this booking.
     * @param customer     The Customer object associated with this booking.
     * @param vehicleType  The VehicleType object associated with this booking.
     * @param startDate    The start date of the rental.
     * @param durationDays The number of days for the rental.
     * @param totalPrice   The calculated total price.
     */
    public Booking(String bookingId, Customer customer, VehicleType vehicleType, LocalDate startDate, int durationDays, double totalPrice) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.vehicleType = vehicleType;
        this.startDate = startDate;
        this.durationDays = durationDays;
        this.totalPrice = totalPrice;
    }

    // --- Getters and Setters ---

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
