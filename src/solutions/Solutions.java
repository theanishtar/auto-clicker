package solutions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class Solutions extends JFrame {
    private JLabel statusLabel, author;
    private Timer timer;
    private long lastMouseActivityTime;
    private JButton startStopButton;
    private boolean isRunning;
    private int previousMouseX, previousMouseY;
    private int demNguoc =  60 * 1000; // 1 phút
    private int yenTinh =  60 * 60 * 1000; // 4h
    private JTextField yenTinhField, demNguocField;

    public Solutions() {
        URL urlIconMain = Solutions.class.getResource("/solutions/icon.png");
        Image img = Toolkit.getDefaultToolkit().createImage(urlIconMain);
        this.setIconImage(img);
        setTitle("Auto Click App");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1));
        author = new JLabel("Tran Huu Dang");
        add(author);

        JPanel yenTinhPanel = new JPanel(new FlowLayout());
        yenTinhPanel.add(new JLabel("Yen Tinh (ms): "));
        yenTinhField = new JTextField(10);
        yenTinhField.setText(Integer.toString(yenTinh));
        yenTinhPanel.add(yenTinhField);
        add(yenTinhPanel);

        JPanel demNguocPanel = new JPanel(new FlowLayout());
        demNguocPanel.add(new JLabel("Dem Nguoc (ms): "));
        demNguocField = new JTextField(10);
        demNguocField.setText(Integer.toString(demNguoc));
        demNguocPanel.add(demNguocField);
        add(demNguocPanel);
        
        statusLabel = new JLabel("No mouse activity.");
        add(statusLabel);

        startStopButton = new JButton("Start");
        startStopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isRunning) {
                    stopAutoClick();
                } else {
                    startAutoClick();
                }
            }
        });
        add(startStopButton);

        lastMouseActivityTime = System.currentTimeMillis();
        isRunning = false;

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {}

            @Override
            public void mouseMoved(MouseEvent e) {
                if (previousMouseX != e.getX() || previousMouseY != e.getY()) {
                    lastMouseActivityTime = System.currentTimeMillis();
                    previousMouseX = e.getX();
                    previousMouseY = e.getY();
                }
            }
        });
    }

    private void startAutoClick() {
        try {
            yenTinh = Integer.parseInt(yenTinhField.getText());
            demNguoc = Integer.parseInt(demNguocField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter integer values only.");
            return;
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastMouseActivityTime >= yenTinh) {
                    statusLabel.setText("No mouse activity detected for: "+yenTinh/1000+"s");
                    try {
                        Thread.sleep(demNguoc);
                        Robot robot = new Robot();
                        robot.mousePress(java.awt.event.InputEvent.BUTTON1_DOWN_MASK);
                        robot.mouseRelease(java.awt.event.InputEvent.BUTTON1_DOWN_MASK);
                        lastMouseActivityTime = System.currentTimeMillis();
                        statusLabel.setText("Auto-clicked at " + getCurrentTime());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }, 0, 1000); // Check every second
        startStopButton.setText("Stop");
        isRunning = true;
    }

    private void stopAutoClick() {
        if (timer != null) {
            timer.cancel();
        }
        startStopButton.setText("Start");
        isRunning = false;
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Solutions().setVisible(true);
            }
        });
    }
}
