package model;

/**
 * Enum defining the hardcoded vehicle types, rates, and image URLs.
 * This acts as the "Source of Truth" for database initialization.
 */
public enum VehicleCategory {
    SMALL_SEDAN("Small Sedan", 40.0, "https://media.ed.edmunds-media.com/honda/civic/2026/oem/2026_honda_civic_sedan_si_fq_oem_1_815.jpg"),
    MEDIUM_SEDAN("Medium Sedan", 50.0, "https://hips.hearstapps.com/hmg-prod/images/2025-toyota-camry-xse-awd-123-66993cc94cc40.jpg?crop=0.580xw:0.489xh;0.137xw,0.397xh&resize=1200:*"),
    SUV("SUV", 60.0, "https://www.chevrolet.com/content/dam/chevrolet/na/us/english/index/vehicle-groups/suv/visid/01-images/small-suvs/2025-small-suvs-trax.png?imwidth=3000");

    private final String label;
    private final double rate;
    private final String imageUrl;

    VehicleCategory(String label, double rate, String imageUrl) {
        this.label = label;
        this.rate = rate;
        this.imageUrl = imageUrl;
    }

    public String getLabel() { return label; }
    public double getRate() { return rate; }
    public String getImageUrl() { return imageUrl; }

    @Override
    public String toString() { return label; }
}