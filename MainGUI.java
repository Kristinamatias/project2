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
        setTitle("\"������������ �������\"");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tripListModel = new DefaultListModel<>();
        tripList = new JList<>(tripListModel);
        JScrollPane scrollPane = new JScrollPane(tripList);

        JButton loadButton = new JButton("������ �� ����");
        JButton saveButton = new JButton("������ ��� ����");
        JButton addButton = new JButton("������ ������");
        JButton searchButton = new JButton("����� �� �������");
        JButton reserveButton = new JButton("����������");

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
                showMessage("�������� �� ��������.");
            } catch (Exception ex) {
                showMessage("������ ��� ���������.");
            }
        });

        saveButton.addActionListener(e -> {
            try {
                manager.saveToFile("trips.txt");
                showMessage("�������� �������.");
            } catch (Exception ex) {
                showMessage("������ ��� �����.");
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
            inputPanel.add(new JLabel("���:")); inputPanel.add(name);
            inputPanel.add(new JLabel("�������:")); inputPanel.add(country);
            inputPanel.add(new JLabel("����:")); inputPanel.add(city);
            inputPanel.add(new JLabel("��������:")); inputPanel.add(desc);
            inputPanel.add(new JLabel("������� ����:")); inputPanel.add(start);
            inputPanel.add(new JLabel("������ ����:")); inputPanel.add(end);
            inputPanel.add(new JLabel("����:")); inputPanel.add(price);
            inputPanel.add(new JLabel("�������� �����:")); inputPanel.add(spots);

            int result = JOptionPane.showConfirmDialog(this, inputPanel, "�������� �� ������", JOptionPane.OK_CANCEL_OPTION);
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
                    showMessage("��������� �����.");
                }
            }
        });

        searchButton.addActionListener(e -> {
            String country = JOptionPane.showInputDialog(this, "������ �������:");
            if (country != null) {
                List<Trip> found = manager.searchByCountry(country);
                refreshList(found);
            }
        });

        reserveButton.addActionListener(e -> {
            String selected = tripList.getSelectedValue();
            if (selected == null) {
                showMessage("����, ������ ������.");
                return;
            }
            for (Trip t : manager.getTrips()) {
                if (selected.startsWith(t.getName())) {
                    if (t.getAvailableSpots() > 0) {
                        t.reserveSpot();
                        refreshList(manager.getTrips());
                        showMessage("������������ � �������.");
                    } else {
                        showMessage("���� �������� �����.");
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