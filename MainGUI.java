import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;

public class MainGUI extends JFrame {
    private TravelManager manager;
    private DefaultListModel<String> tripListModel;
    private JList<String> tripList;

    public MainGUI() {
        manager = new TravelManager();
        setTitle("\"Travel system\"");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tripListModel = new DefaultListModel<>();
        tripList = new JList<>(tripListModel);
        JScrollPane scrollPane = new JScrollPane(tripList);

        JButton loadButton = new JButton("Load from file");
        JButton saveButton = new JButton("Save in file");
        JButton addButton = new JButton("Add offer");
        JButton searchButton = new JButton("Search by country");
        JButton reserveButton = new JButton("Reserve");

        JPanel panel = new JPanel();
        panel.add(loadButton);
        panel.add(saveButton);
        panel.add(addButton);
        panel.add(searchButton);
        panel.add(reserveButton);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadButton.addActionListener(e -> {
            try {
                manager.loadFromFile("trips.txt");
                refreshList(manager.getTrips());
                showMessage("The offers are loaded");
            } catch (Exception ex) {
                showMessage("Loading error");
            }
        });

        saveButton.addActionListener(e -> {
            try {
                manager.saveToFile("trips.txt");
                showMessage("Saved successfully");
            } catch (Exception ex) {
                showMessage("Write error");
            }
        });

        addButton.addActionListener(e -> {
            JTextField name = new JTextField();
            JTextField country = new JTextField();
            JTextField city = new JTextField();
            JTextField desc = new JTextField();
            JTextField start = new JTextField("2025-07-01");
            JTextField end = new JTextField("2025-07-10");
            JTextField price = new JTextField("1000");
            JTextField spots = new JTextField("5");

            JPanel inputPanel = new JPanel(new GridLayout(0, 1));
            inputPanel.add(new JLabel("Name:")); inputPanel.add(name);
            inputPanel.add(new JLabel("Country:")); inputPanel.add(country);
            inputPanel.add(new JLabel("City:")); inputPanel.add(city);
            inputPanel.add(new JLabel("Description:")); inputPanel.add(desc);
            inputPanel.add(new JLabel("Starting date:")); inputPanel.add(start);
            inputPanel.add(new JLabel("End date:")); inputPanel.add(end);
            inputPanel.add(new JLabel("Price:")); inputPanel.add(price);
            inputPanel.add(new JLabel("Free spots:")); inputPanel.add(spots);

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "Add offer", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    Destination d = new Destination(country.getText(), city.getText(), desc.getText());
                    Trip t = new Trip(name.getText(), d,
                            LocalDate.parse(start.getText()),
                            LocalDate.parse(end.getText()),
                            Double.parseDouble(price.getText()),
                            Integer.parseInt(spots.getText()));
                    manager.addTrip(t);
                    refreshList(manager.getTrips());
                } catch (Exception ex) {
                    showMessage("Invalid data");
                }
            }
        });

        searchButton.addActionListener(e -> {
            String country = JOptionPane.showInputDialog(this, "Enter country:");
            if (country != null) {
                List<Trip> found = manager.searchByCountry(country);
                refreshList(found);
            }
        });

        reserveButton.addActionListener(e -> {
            String selected = tripList.getSelectedValue();
            if (selected == null) {
                showMessage("Please, choose an offer.");
                return;
            }
            for (Trip t : manager.getTrips()) {
                if (selected.startsWith(t.getName())) {
                    if (t.getAvailableSpots() > 0) {
                        t.reserveSpot();
                        refreshList(manager.getTrips());
                        showMessage("The reservation is successful");
                    } else {
                        showMessage("No more spots left.");
                    }
                    break;
                }
            }
        });
    }

    private void refreshList(List<Trip> trips) {
        tripListModel.clear();
        for (Trip t : trips) {
            tripListModel.addElement(t.toString());
        }
    }

    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI().setVisible(true));
    }
}
