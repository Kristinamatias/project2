import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.List;

public class MainSwing {
    private static TravelManager manager = new TravelManager();
    private static JTextArea outputArea = new JTextArea(15, 40);

    public static void main(String[] args) {
        JFrame frame = new JFrame("Туристическа система");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 5, 5));

        JButton loadButton = new JButton("Зареди от файл");
        JButton showAllButton = new JButton("Покажи всички");
        JButton searchButton = new JButton("Търси по държава");
        JButton sortPriceButton = new JButton("Сортирай по цена");
        JButton saveButton = new JButton("Запази във файл");

        loadButton.addActionListener((ActionEvent e) -> {
            try {
                manager.loadFromFile("trips.txt");
                outputArea.setText("Заредено успешно!");
            } catch (Exception ex) {
                outputArea.setText("Грешка при зареждане.");
            }
        });

        showAllButton.addActionListener((ActionEvent e) -> {
            outputArea.setText("");
            for (Trip t : manager.getTrips()) {
                outputArea.append(t.toString() + "\n");
            }
        });

        searchButton.addActionListener((ActionEvent e) -> {
            String country = JOptionPane.showInputDialog("Въведи държава:");
            List<Trip> results = manager.searchByCountry(country);
            outputArea.setText("");
            for (Trip t : results) {
                outputArea.append(t.toString() + "\n");
            }
        });

        sortPriceButton.addActionListener((ActionEvent e) -> {
            manager.sortByPrice();
            outputArea.setText("✔ Сортирано по цена.");
        });

        saveButton.addActionListener((ActionEvent e) -> {
            try {
                manager.saveToFile("trips.txt");
                outputArea.setText("Записано успешно.");
            } catch (Exception ex) {
                outputArea.setText("Грешка при запис.");
            }
        });

        panel.add(loadButton);
        panel.add(showAllButton);
        panel.add(searchButton);
        panel.add(sortPriceButton);
        panel.add(saveButton);

        frame.getContentPane().add(BorderLayout.CENTER, new JScrollPane(outputArea));
        frame.getContentPane().add(BorderLayout.EAST, panel);

        frame.setVisible(true);
    }
}
