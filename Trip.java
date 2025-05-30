import java.time.LocalDate;

public class Trip {
    private String name;
    private Destination destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private double price;
    private int availableSpots;

    public Trip(String name, Destination destination, LocalDate startDate, LocalDate endDate, double price, int availableSpots) {
        this.name = name;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.availableSpots = availableSpots;
    }

    public boolean isValid() {
        return !endDate.isBefore(startDate) && availableSpots >= 0;
    }

    public String getName() { return name; }
    public Destination getDestination() { return destination; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public double getPrice() { return price; }
    public int getAvailableSpots() { return availableSpots; }

    public void reserveSpot() {
        if (availableSpots > 0) availableSpots--;
    }

    @Override
    public String toString() {
        return name + " | " + destination + " | " + startDate + " - " + endDate + " | $" + price + " | Spots: " + availableSpots;
    }
}