package main;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.*;
import java.util.*;

import javax.swing.*;
import javax.swing.JSpinner.*;

public class TilePartyServer implements ActionListener {

    public static final int port = 8192;
    public static JFrame window = new JFrame("Tile Party Server");
    public static JTextPane print = new JTextPane();
    public static JScrollPane temp = new JScrollPane(print);
    public static JButton start = new JButton("Start Server");
    public static SpinnerModel model = new SpinnerNumberModel(1, 1, 6, 1);
    public static JSpinner spinner = new JSpinner(model);

    public static JMenuBar menuBar = new JMenuBar();
    public static JMenu file = new JMenu("File");
    public static JMenuItem save = new JMenuItem("Save Server Log");
    public static JMenuItem exit = new JMenuItem("Exit");

    public static ArrayList<String> backlog = new ArrayList<String>();

    private static Server server = new Server(port);

    public TilePartyServer() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        temp.getVerticalScrollBar()
                .addAdjustmentListener(new AdjustmentListener() {
                    @Override
                    public void adjustmentValueChanged(AdjustmentEvent e) {
                        e.getAdjustable()
                                .setValue(e.getAdjustable().getMaximum());
                    }
                });

        JPanel content = new JPanel();
        content.setLayout(new GridLayout(2, 1));

        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.setResizable(false);
        window.setSize(800, 600);
        window.add(content);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (!TilePartyServer.server.listening) {
                    int result = JOptionPane.showConfirmDialog(window,
                            "Are you sure you want to close the application?",
                            "Close Application", JOptionPane.YES_NO_OPTION);

                    if (result == JOptionPane.YES_OPTION) {
                        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    }
                } else {
                    int result = JOptionPane.showConfirmDialog(window,
                            "Are you sure you want to close the application and end the server?",
                            "Close Application", JOptionPane.YES_NO_OPTION);

                    if (result == JOptionPane.YES_OPTION) {
                        TilePartyServer.server.end();
                        TilePartyServer.server = new Server(port);
                        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    }
                }
            }
        });

        save.addActionListener(this);
        save.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        exit.addActionListener(this);
        exit.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        file.add(save);
        file.add(exit);
        menuBar.add(file);
        window.setJMenuBar(menuBar);

        JPanel top = new JPanel();
        top.add(temp);
        top.setLayout(new GridLayout(1, 1));
        top.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 8));

        content.add(top);

        print.setEditable(false);
        print.setFont(new Font("courier new", Font.BOLD, 16));
        print.setForeground(Color.WHITE);
        print.setBackground(Color.BLACK);

        start.setFont(new Font("courier new", Font.BOLD, 36));
        start.addActionListener(this);
//        start.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createRaisedBevelBorder(),
//                BorderFactory.createLoweredBevelBorder()));

        ((DefaultEditor) spinner.getEditor()).getTextField().setEditable(false);
        ((DefaultEditor) spinner.getEditor()).getTextField()
                .setFont(new Font("courier new", Font.BOLD, 16));

        JPanel bot = new JPanel();
        bot.setLayout(new GridLayout(1, 2));
        bot.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 8));
        content.add(bot);

        JPanel botLeft = new JPanel();
        botLeft.setLayout(new GridLayout(2, 2));
//        botLeft.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createRaisedBevelBorder(),
//                BorderFactory.createLoweredBevelBorder()));

        JPanel botRight = new JPanel();
        botRight.setLayout(new GridLayout(2, 1));
//        botRight.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createRaisedBevelBorder(),
//                BorderFactory.createLoweredBevelBorder()));

        bot.add(botLeft);
        bot.add(botRight);

        JPanel botTopRight = new JPanel();

        JTextPane players = new JTextPane();
        players.setText("Players: ");
        players.setFont(new Font("courier new", Font.BOLD, 14));
        players.setEditable(false);
        players.setOpaque(false);

        botTopRight.setLayout(new GridLayout(2, 2));
        botTopRight.setBorder(
                BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 8));
        botTopRight.add(new JPanel());
        botTopRight.add(new JPanel());
        botTopRight.add(new JPanel());
        JPanel row4 = new JPanel();
        spinner.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createRaisedBevelBorder(),
                                BorderFactory.createLoweredBevelBorder()),
                        "Players"));

        row4.setLayout(new GridLayout(0, 2));
        row4.add(new JPanel());
        row4.add(spinner);
        botTopRight.add(row4);

        botRight.add(botTopRight);
        botRight.add(start);

        botLeft.add(new JPanel());
        botLeft.add(new JPanel());
        botLeft.add(new JPanel());
        botLeft.add(new JPanel());

        temp.setSize(600, 400);
        window.setVisible(true);
    }

    public static void print(String message) {
        if (print.getText().length() + message.length() < Integer.MAX_VALUE) {
            print.setText(print.getText() + message);
        } else {
            String temp = print.getText().substring(0,
                    print.getText().length() / 2);
            backlog.add(temp);
            print.setText(
                    print.getText().substring(print.getText().length() / 2));
        }
    }

    public static void println(String message) {
        print(LocalTime.now().toString().substring(0, 5) + ":\t" + message
                + "\n");
    }

    public static void main(String[] args) {
        new TilePartyServer();
    }

    @Override
    public void actionPerformed(ActionEvent action) {
        if (action.getSource().equals(start)) {
            if (TilePartyServer.start.getText().equals("Start Server")) {
                if (TilePartyServer.server
                        .start((int) TilePartyServer.model.getValue())) {
                    TilePartyServer.print.setForeground(Color.WHITE);
                    TilePartyServer.spinner.setEnabled(false);
                    TilePartyServer.start.setText("End Server");
                }
            } else if (TilePartyServer.start.getText().equals("End Server")) {
                int result = JOptionPane.showConfirmDialog(window,
                        "Are you sure you want to end the server?",
                        "End Server", JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION) {
                    TilePartyServer.server.end();
                    TilePartyServer.server = new Server(port);

                    int result2 = JOptionPane.showConfirmDialog(window,
                            "Would you like to save the server log?",
                            "Save Log", JOptionPane.YES_NO_OPTION);

                    if (result2 == JOptionPane.YES_OPTION) {
                        TilePartyServer.saveLog();
                    }
                    TilePartyServer.print.setText("");
                    TilePartyServer.spinner.setEnabled(true);
                    TilePartyServer.start.setText("Start Server");
                }
            }
        } else if (action.getSource().equals(save)) {
            TilePartyServer.saveLog();
        } else if (action.getSource().equals(exit)) {
            window.dispatchEvent(
                    new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
        }
    }

    private static void saveLog() {
        try {
            PrintWriter writer = new PrintWriter(
                    "log_" + LocalDate.now().toString()
                            + "_" + LocalTime.now().toString()
                                    .replaceAll(":", "-").substring(0, 8),
                    "UTF-8");
            for (String s : backlog) {
                writer.write(s);
            }
            writer.write(print.getText());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
