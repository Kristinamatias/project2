import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TravelManager {
    private List<Trip> trips = new ArrayList<>();
    private Map<String, List<Trip>> tripsByCountry = new HashMap<>();

    public void addTrip(Trip trip) {
        if (trip.isValid() && !isDuplicate(trip)) {
            trips.add(trip);
            tripsByCountry.computeIfAbsent(trip.getDestination().getCountry(), k -> new ArrayList<>()).add(trip);
        }
    }

    public boolean isDuplicate(Trip trip) {
        return trips.stream().anyMatch(t -> t.getName().equalsIgnoreCase(trip.getName()));
    }

    public List<Trip> searchByCountry(String country) {
        return tripsByCountry.getOrDefault(country, new ArrayList<>());
    }

    public List<Trip> searchByPrice(double min, double max) {
        return trips.stream().filter(t -> t.getPrice() >= min && t.getPrice() <= max).collect(Collectors.toList());
    }

    public List<Trip> searchByPeriod(LocalDate from, LocalDate to) {
        return trips.stream().filter(t -> !(t.getEndDate().isBefore(from) || t.getStartDate().isAfter(to))).collect(Collectors.toList());
    }

    public void sortByPrice() {
        insertionSort(trips, Comparator.comparingDouble(Trip::getPrice));
    }

    public void sortByDate() {
        insertionSort(trips, Comparator.comparing(Trip::getStartDate));
    }

    private void insertionSort(List<Trip> list, Comparator<Trip> comp) {
        for (int i = 1; i < list.size(); i++) {
            Trip key = list.get(i);
            int j = i - 1;
            while (j >= 0 && comp.compare(list.get(j), key) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }

    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Trip t : trips) {
                writer.write(t.getName() + "|" + t.getDestination().getCountry() + "|" + t.getDestination().getCity() + "|" +
                             t.getStartDate() + "|" + t.getEndDate() + "|" + t.getPrice() + "|" + t.getAvailableSpots() + "|" +
                             t.getDestination().getDescription());
                writer.newLine();
            }
        }
    }

    public void loadFromFile(String filename) throws IOException {
        trips.clear();
        tripsByCountry.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\"|");
                if (parts.length < 8) continue;

                String name = parts[0];
                String country = parts[1];
                String city = parts[2];
                LocalDate start = LocalDate.parse(parts[3]);
                LocalDate end = LocalDate.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                int spots = Integer.parseInt(parts[6]);
                String desc = parts[7];

                Destination dest = new Destination(country, city, desc);
                Trip trip = new Trip(name, dest, start, end, price, spots);
                addTrip(trip);
            }
        }
    }

    public List<Trip> getTrips() {
        return trips;
    }
}