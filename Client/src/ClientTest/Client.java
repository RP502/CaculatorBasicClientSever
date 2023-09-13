package ClientTest;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class Client extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField aField;
    private JTextField bField;
    private JTextField operatorField;
    private JTextArea resultArea;
    private JButton calculateButton;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Client frame = new Client();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Client() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel inputPanel = new JPanel();
        contentPane.add(inputPanel, BorderLayout.NORTH);
        inputPanel.setLayout(new GridLayout(3, 2, 5, 5));

        inputPanel.add(new JLabel("Enter float a:"));
        aField = new JTextField();
        inputPanel.add(aField);
        aField.setColumns(10);

        inputPanel.add(new JLabel("Enter float b:"));
        bField = new JTextField();
        inputPanel.add(bField);
        bField.setColumns(10);

        inputPanel.add(new JLabel("Enter operator (+, -, *, /):"));
        operatorField = new JTextField();
        inputPanel.add(operatorField);
        operatorField.setColumns(10);

        calculateButton = new JButton("Calculate");
        inputPanel.add(calculateButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Establish the connection to the server
        try {
            socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculate();
            }
        });
    }

    private void calculate() {
        try {
            float a = Float.parseFloat(aField.getText());
            float b = Float.parseFloat(bField.getText());
            char operator = operatorField.getText().charAt(0);

            out.println(a);
            out.println(b);
            out.println(operator);

            String result = in.readLine();
            resultArea.setText("Result: " + result);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NumberFormatException nfe) {
            System.err.println("Invalid number format: " + nfe.getMessage());
        } catch (IndexOutOfBoundsException ioobe) {
            System.err.println("Invalid operator: " + ioobe.getMessage());
        }
    }


    @Override
    public void dispose() {
        try {
            // Close the socket and streams when the client is closed
            if (socket != null) {
                socket.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.dispose();
    }
}
