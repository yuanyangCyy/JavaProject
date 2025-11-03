package model;

public class Customer {
    private String customerId;
    private String name;
    private String phone;
    private String email;

    /**
     * Default constructor.
     */
    public Customer() {
    }

    /**
     * Parameterized constructor to create a new customer.
     *
     * @param customerId The unique ID of the customer.
     * @param name       The customer's full name.
     * @param phone      The customer's phone number.
     * @param email      The customer's email address.
     */
    public Customer(String customerId, String name, String phone, String email) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    // --- Getters and Setters ---

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return name; // Used by ComboBox or other UI elements
    }
}
