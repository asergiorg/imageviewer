package software.ulpgc.imageviewer.application.gui;

import software.ulpgc.imageviewer.architecture.Command;
import software.ulpgc.imageviewer.architecture.ImageToolBar;

import javax.swing.*;
import java.awt.*;
import java.security.DigestException;
import java.util.HashMap;
import java.util.Map;

import static java.awt.BorderLayout.*;
import static java.awt.FlowLayout.CENTER;

public class Desktop extends JFrame implements ImageToolBar {
    private final Map<String, Command> commands;
    private final JLabel imageName = new JLabel("", JLabel.CENTER);
    private final JLabel memorySize = new JLabel("", JLabel.CENTER);
    private final JLabel imageSize = new JLabel("", JLabel.CENTER);

    public static Desktop create(SwingImageDisplay imageDisplay) {
        return new Desktop(imageDisplay);
    }

    private Desktop(SwingImageDisplay imageDisplay) throws HeadlessException {
        this.commands = new HashMap<>();
        this.setTitle("Image Viewer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1150, 590);
        this.setLayout(new BorderLayout());
        this.createButtonbar();
        this.createUpperToolBar();
        this.createLowerToolBar();
        this.setLocationRelativeTo(null);
        this.getContentPane().add(imageDisplay);
    }

    private void createUpperToolBar() {
        this.getContentPane().add(upperToolBar(), NORTH);
    }

    private JPanel upperToolBar() {
        JPanel panel = new JPanel(new FlowLayout(CENTER));
        panel.add(button(new Button("\uD83D\uDDD1",
                "Suprimir",
                30,
                new Dimension(60, 60))));
        panel.add(button(new Button("↻", "Girar a la derecha", 30, new Dimension(60, 60))));
        panel.add(label(imageName, new Dimension(400, 25)));
        panel.setBackground(Color.GRAY);
        return panel;
    }

    private JLabel label(JLabel label, Dimension size) {
        label.setOpaque(false);
        label.setPreferredSize(size);
        label.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 16));
        return label;
    }

    private void createLowerToolBar() {
        this.getContentPane().add(lowerToolBar(), SOUTH);
    }

    private JPanel lowerToolBar() {
        JPanel panel = new JPanel(new FlowLayout(CENTER));
        panel.add(button(new Button("ⓘ",
                "Información del archivo",
                30,
                new Dimension(60, 60))));
        panel.add(label(imageSize, new Dimension(100, 25)));
        panel.add(label(memorySize, new Dimension(100, 25)));
        panel.setBackground(Color.GRAY);
        return panel;
    }

    private void createButtonbar() {
        this.getContentPane().add(butttonbar(new Button("⏴",
                "Anterior",
                32,
                new Dimension(67, 80))), WEST);
        this.getContentPane().add(butttonbar(new Button("⏵",
                "Siguiente",
                32,
                new Dimension(67, 80))), EAST);
    }

    private JPanel butttonbar(Button button) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(button(button), new GridBagConstraints());
        panel.setBackground(Color.GRAY);
        return panel;
    }

    private JButton button(Button buttonObject) {
        JButton button = new JButton(buttonObject.name());
        button.addActionListener(_ -> commands.get(buttonObject.name()).execute());
        designe(button, buttonObject);
        return button;
    }

    private void designe(JButton button, Button buttonObject) {
        configureVisibility(button, buttonObject);
        configureButtonStyle(button);
        addToolTip(button, buttonObject);
    }

    private void configureButtonStyle(JButton button) {
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
    }

    private void configureVisibility(JButton button, Button buttonObject) {
        button.setPreferredSize(buttonObject.size());
        button.setFont(new Font("Segoe UI Symbol", Font.PLAIN, buttonObject.font()));
    }

    private void addToolTip(JButton button, Button buttonObject) {
        button.setToolTipText(buttonObject.alias());
        ToolTipManager.sharedInstance().setInitialDelay(200);
    }

    public Desktop put(String name, Command command) {
        commands.put(name, command);
        return this;
    }

    @Override
    public void showImageName(String name) {
        imageName.setText(name);
    }

    @Override
    public void showImageSize(int width, int height) {
        imageSize.setText(width + "x" + height);
    }

    @Override
    public void showImageMemorySize(long size) {
        memorySize.setText("\uD83D\uDCBE  " + size + "KB" );
    }

    private record Button(String name, String alias, int font, Dimension size){}
}


