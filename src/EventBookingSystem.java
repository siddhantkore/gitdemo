import javax.swing.*;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class EventBookingSystem {
    public static JFrame frame;

    private static ButtonGroup buttonGroup = new ButtonGroup();

    private static int seatAvailability = 100;
    private static JLabel availabilityLabel;

    private static JLabel reservationLabel;

    private static double ticketPriceKids = 100;
    private static double ticketPriceAdults = 200;
    private static double ticketPriceElders = 150;

    private static JLabel totalPriceLabel;
    private static int currentReservationCount = 0;

    private static JRadioButton kidsRadioButton;
    private static JRadioButton adultsRadioButton;

    private static JRadioButton eldersRadioButton;
    private static JButton bookButton;
    private static JButton payButton;

    private static double totalPrice;
    // Replace customerDetailsPanel with JTable
    private static JTable customerTable;
    private static DefaultTableModel tableModel;

    // New JLabel for customer name and reservation details
    private static JLabel customerDetailsLabel;

    // New variables to store customer name and total seats booked
    private static String customerName = "";
    private static int totalSeatsBooked = 0;

    // ArrayList to store customer names
    private static ArrayList<String> customerList = new ArrayList<>();
    private static JPanel customerDetailsPanel;

    // Add JTextField for seat input
    private static JTextField seatInputField;

    public static void main(String[] args) {
        frame = new JFrame("Event Booking System");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));

        availabilityLabel = new JLabel("Available Seats: " + seatAvailability);
        panel.add(availabilityLabel);

        reservationLabel = new JLabel("Reserved Seats: 0");
        panel.add(reservationLabel);

        kidsRadioButton = new JRadioButton("Kids (Rs" + ticketPriceKids + ")");
        adultsRadioButton = new JRadioButton("Adults (Rs" + ticketPriceAdults + ")");
        eldersRadioButton = new JRadioButton("Elders (Rs" + ticketPriceElders + ")");


        buttonGroup.add(kidsRadioButton);
        buttonGroup.add(adultsRadioButton);
        buttonGroup.add(eldersRadioButton);

        JPanel radioButtonsPanel = new JPanel();
        radioButtonsPanel.setLayout(new FlowLayout());
        radioButtonsPanel.add(kidsRadioButton);
        radioButtonsPanel.add(adultsRadioButton);
        radioButtonsPanel.add(eldersRadioButton);
        panel.add(radioButtonsPanel);

        // Add seat input field to the panel
        JPanel seatInputPanel = new JPanel();
        seatInputPanel.setLayout(new FlowLayout());
        JLabel seatInputLabel = new JLabel("Number of Seats to Book:");
        seatInputField = new JTextField(5);
        seatInputPanel.add(seatInputLabel);
        seatInputPanel.add(seatInputField);
        panel.add(seatInputPanel);

        bookButton = new JButton("Book Seat");
        bookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bookSeat();
            }
        });
        bookButton.setBackground(new Color(135, 206, 235)); // Sky blue color
        panel.add(bookButton);

        JPanel payButtonPanel = new JPanel();
        payButtonPanel.setLayout(new FlowLayout());
        payButton = new JButton("Pay Now");
        payButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                payNow();
            }
        });
        payButton.setBackground(new Color(255, 105, 180));
        payButtonPanel.add(payButton);
        panel.add(payButtonPanel);

        customerTable = new JTable();
        customerTable.setEnabled(false); // Making table uneditable
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Customer Name");
        tableModel.addColumn("Reserved Seats");
        tableModel.addColumn("Paid Amount");
        customerTable.setModel(tableModel);
        panel.add(new JScrollPane(customerTable)); // Add the table to a scroll pane and add it to the panel

        totalPriceLabel = new JLabel("Total Price: Rs 0.00");
        panel.add(totalPriceLabel);

        frame.add(panel);
        frame.setVisible(true);
    }

    // Modify the updateCustomerDetailsLabel method
    private static void updateCustomerDetailsLabel() {
        String name = customerList.get(customerList.size() - 1);
        Object[] rowData = {name, currentReservationCount, totalPrice};
        tableModel.addRow(rowData);
    }

    private static void bookSeat() {
        currentReservationCount = 0;
        if (seatAvailability > 0) {
            String seatInputText = seatInputField.getText();

            if (seatInputText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter the number of seats to book!", "Reservation Unsuccessful...!!!", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int seatsToBook = Integer.parseInt(seatInputText);

                if (seatsToBook <= 0) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number of seats (greater than 0)!", "Reservation Unsuccessful...!!!", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (seatsToBook > seatAvailability) {
                    JOptionPane.showMessageDialog(null, "Not enough seats available!", "Reservation Unsuccessful...!!!", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                seatAvailability -= seatsToBook;
                currentReservationCount = seatsToBook;
                int reservedSeats = 100 - seatAvailability;
                availabilityLabel.setText("Available Seats: " + seatAvailability);
                reservationLabel.setText("Reserved Seats: " + reservedSeats);

                // Calculate total price based on selected ticket type
                double selectedTicketPrice = 0;


                if (kidsRadioButton.isSelected()) {
                    selectedTicketPrice = ticketPriceKids;
                } else if (adultsRadioButton.isSelected()) {
                    selectedTicketPrice = ticketPriceAdults;
                } else if (eldersRadioButton.isSelected()) {
                    selectedTicketPrice = ticketPriceElders;
                } else {
                    JOptionPane.showMessageDialog(null, "Select a ticket type!!!");
                    return;
                }

                totalPrice += selectedTicketPrice * seatsToBook;
                System.out.println(selectedTicketPrice + " " + seatsToBook);

                DecimalFormat df = new DecimalFormat("0.00");
                totalPriceLabel.setText("Total Price: Rs " + df.format(totalPrice));
                System.out.println(seatsToBook + " seats booked!");

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input! Please enter a valid number.", "Reservation Unsuccessful...!!!", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            System.out.println("No seats available!");
        }
    }

    private static void payNow() {
        if (currentReservationCount == 0) {
            JOptionPane.showMessageDialog(null, "Please reserve seat first", "Payment Unsuccessful...!!!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (seatAvailability < 100) {

            // Prompt the user to enter their name
            customerName = JOptionPane.showInputDialog(null, "Enter your name:");

            if (customerName != null && !customerName.trim().isEmpty()) { // Check if the customer entered a valid name
                int reservedSeats = 100 - seatAvailability;

                System.out.println("Payment successful! Total amount paid: Rs" + totalPrice);
                System.out.println("Registration successfully completed!");

                // Add the customer name to the list
                customerList.add(customerName);

                // Display the bill along with the customer's name
                String billText = "Customer Name: " + customerName + "\n" +
                        "Reserved Seats: " + currentReservationCount + "\n" +
                        "Total Price: Rs " + totalPrice;

                JOptionPane.showMessageDialog(null, billText, "Payment Successful", JOptionPane.INFORMATION_MESSAGE);

                // Reset the customer name and reservation count
                customerName = "";

                totalSeatsBooked += currentReservationCount;

                updateCustomerDetailsLabel();
                // Reset total price and update the display
                totalPrice = 0;
                totalPriceLabel.setText("Total Price: Rs " + (totalPrice));
                currentReservationCount = 0;
            } else {
                JOptionPane.showMessageDialog(null, "Invalid customer name!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No seat Available");
        }
    }
}