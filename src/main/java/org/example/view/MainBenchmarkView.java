//MOLDOVANU_TUDOR_MAC_BENCHMARK//
package org.example.view;

import com.formdev.flatlaf.FlatDarkLaf;
import org.example.controller.*;
import org.example.model.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class MainBenchmarkView extends JFrame {
    private double[] OverallAverages = new double[6];
    private JProgressBar progressBar;
    private JButton startButton;
    private JLabel gradeLabel;

    private CPUBenchmarkModel cpuModel;
    private RAMBenchmarkModel ramModel;

    private DISKBenchmarkModel diskModel;

    private NETWORKBenchmarkModel networkModel;

    private BATTERYBenchmarkModel batteryModel;

    private SYSTEMRESPONSIVENESSBenchmarkModel systemresponsivenessModel;

    private CPUBenchmarkController cpuBenchmarkController;
    private RAMBenchmarkController ramBenchmarkController;

    private DISKBenchmarkController diskBenchmarkController;

    private NETWORKBenchmarkController networkBenchmarkController;

    private BATTERYBenchmarkController batteryBenchmarkController;

    private SYSTEMRESPONSIVENESSBenchmarkController systemresponsivenessBenchmarkController;

    private CPUBenchmarkView cpuBenchmarkView;
    private RAMBenchmarkView ramBenchmarkView;

    private DISKBenchmarkView diskBenchmarkView;

    private NETWORKBenchmarkView networkBenchmarkView;

    private BATTERYBenchmarkView batteryBenchmarkView;

    private SYSTEMRESPONSIVENESSBenchmarkView systemresponsivenessBenchmarkView;

    public MainBenchmarkView() {
        //set FlatLaf theme
        FlatDarkLaf.setup();
        cpuModel = new CPUBenchmarkModel(); //initialize the CPU model
        ramModel = new RAMBenchmarkModel();
        diskModel = new DISKBenchmarkModel();
        networkModel = new NETWORKBenchmarkModel();
        batteryModel = new BATTERYBenchmarkModel();
        systemresponsivenessModel = new SYSTEMRESPONSIVENESSBenchmarkModel();
        cpuBenchmarkView = new CPUBenchmarkView();
        ramBenchmarkView = new RAMBenchmarkView();
        diskBenchmarkView = new DISKBenchmarkView();
        networkBenchmarkView = new NETWORKBenchmarkView();
        batteryBenchmarkView = new BATTERYBenchmarkView();
        systemresponsivenessBenchmarkView = new SYSTEMRESPONSIVENESSBenchmarkView();
        cpuBenchmarkController = new CPUBenchmarkController(cpuModel,cpuBenchmarkView);
        ramBenchmarkController = new RAMBenchmarkController(ramModel,ramBenchmarkView);
        diskBenchmarkController = new DISKBenchmarkController(diskModel,diskBenchmarkView);
        networkBenchmarkController = new NETWORKBenchmarkController(networkModel,networkBenchmarkView);
        batteryBenchmarkController = new BATTERYBenchmarkController(batteryModel,batteryBenchmarkView);
        systemresponsivenessBenchmarkController = new SYSTEMRESPONSIVENESSBenchmarkController(systemresponsivenessModel,systemresponsivenessBenchmarkView);
        //configure JFrame
        setTitle("MacBenchmark");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        setIconImage(new ImageIcon(getClass().getResource("/logo.png")).getImage());

        //greeting Screen
        JLabel greetingLabel = new JLabel("Welcome to MacBenchmark!", SwingConstants.CENTER);
        greetingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        greetingLabel.setForeground(Color.WHITE);
        getContentPane().setBackground(Color.BLACK);
        add(greetingLabel, BorderLayout.CENTER);

        //show greeting for 2 seconds, then display the main view
        Timer timer = new Timer(2000, e -> {
            remove(greetingLabel);
            initializeMainView();
            revalidate();
            repaint();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void initializeMainView() {
        //main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(6, 1, 10, 10));
        mainPanel.setBackground(Color.BLACK);

        //add benchmark sections with functionality
        mainPanel.add(createInfoSection("CPU Info", () -> displayInfo(cpuModel.getCPUInfo())));
        mainPanel.add(createInfoSection("RAM Info", () -> displayInfo(ramModel.getRAMInfo())));
        mainPanel.add(createInfoSection("Disk Info",() -> displayInfo(diskModel.getDiskInfo())));
        mainPanel.add(createInfoSection("Network Info",() -> displayInfo(networkModel.getNetworkInfo())));
        mainPanel.add(createInfoSection("System Responsiveness Info",() -> displayInfo(systemresponsivenessModel.getSystemResponsivenessInfo())));
        mainPanel.add(createInfoSection("Battery Info",() -> displayInfo(batteryModel.getBatteryInfo())));

        //bottom Panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.BLACK);

        //progress Bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        bottomPanel.add(progressBar, BorderLayout.CENTER);

        //start Button
        startButton = new JButton("Start Benchmark");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.addActionListener(e -> startBenchmark());
        bottomPanel.add(startButton, BorderLayout.EAST);

        //grade Label
        gradeLabel = new JLabel("Grade: N/A", SwingConstants.CENTER);
        gradeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gradeLabel.setForeground(Color.WHITE);
        bottomPanel.add(gradeLabel, BorderLayout.SOUTH);

        //add components to the frame
        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createInfoSection(String title, Runnable action) {
        JPanel panel = createSectionPanel(title);
        JButton infoButton = new JButton("Show Info");
        infoButton.addActionListener(e -> action.run());
        panel.add(infoButton, BorderLayout.EAST);
        return panel;
    }

    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.DARK_GRAY);
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }

    private void displayInfo(String info) {
        JTextArea textArea = new JTextArea(info);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(this, scrollPane, "System Info", JOptionPane.INFORMATION_MESSAGE);
    }



    private void startBenchmark() {
        try {
            //resolve paths dynamically
            File tempDir = new File(System.getProperty("java.io.tmpdir"), "benchmark_executables");
            if (!tempDir.exists()) tempDir.mkdirs();

            //extract executables
            String matrixMultiplicationPath = extractExecutable("c_algorithms/compiled/matrix_multiply", tempDir);
            String aesDecryptionEncryptionExecutablePath = extractExecutable("c_algorithms/compiled/aes_decryption_ecryption", tempDir);
            String memorySpeedPath = extractExecutable("c_algorithms/compiled/memory_speed_test", tempDir);
            String diskSpeedPath = extractExecutable("c_algorithms/compiled/disk_speed_test", tempDir);
            String networkSpeedPath = extractExecutable("c_algorithms/compiled/network_test", tempDir);
            String responsivenessPath = extractExecutable("c_algorithms/compiled/system_responsiveness_test", tempDir);

            //run benchmarks in a separate thread
            new Thread(() -> {
                try {
                    int completedTasks = 0;

                    //update progress for each task
                    progressBar.setValue(0);

                    //CPU Benchmark
                    cpuBenchmarkController.run(matrixMultiplicationPath, aesDecryptionEncryptionExecutablePath);
                    completedTasks++;
                    progressBar.setValue(completedTasks * 17);
                    OverallAverages[1] = cpuBenchmarkController.getCPUBenchmarkOverallAverage();

                    //RAM Benchmark
                    ramBenchmarkController.run(memorySpeedPath);
                    completedTasks++;
                    progressBar.setValue(completedTasks * 17);
                    OverallAverages[2] = ramBenchmarkController.getRAMBenchmarkOverallAverage();

                    //Disk Benchmark
                    diskBenchmarkController.run(diskSpeedPath);
                    completedTasks++;
                    progressBar.setValue(completedTasks * 17);
                    OverallAverages[3] = diskBenchmarkController.getDISKBenchmarkOverallAverage();

                    //Network Benchmark
                    networkBenchmarkController.run(networkSpeedPath);
                    completedTasks++;
                    progressBar.setValue(completedTasks * 17);
                    OverallAverages[4] = networkBenchmarkController.getNETWORKBenchmarkOverallAverage();

                    //System Responsiveness Benchmark
                    systemresponsivenessBenchmarkController.run(responsivenessPath);
                    completedTasks++;
                    progressBar.setValue(completedTasks * 17);
                    OverallAverages[5] = systemresponsivenessBenchmarkController.getSYSTEMRESPONSIVENESSBenchmarkOverallAverage();

                    //Battery Info
                    batteryBenchmarkController.displayDetailedBatteryInfo();
                    completedTasks++;
                    progressBar.setValue(completedTasks * 17);

                    //calculate the arithmetic mean
                    int sum = 0;
                    for (double overallAverage : OverallAverages) {
                        sum += overallAverage;
                    }

                    //calculate the average score
                    double rawAverage = sum / OverallAverages.length;
                    double benchmarkMax = 220; // Define the benchmark maximum score
                    int finalGrade = (int) ((rawAverage / benchmarkMax) * 100);

                    //display the grade
                    SwingUtilities.invokeLater(() -> {
                        progressBar.setValue(100);
                        gradeLabel.setText("Grade: " + finalGrade + "%");
                        System.out.println("Final System Grade: " + finalGrade + "%");
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Benchmark failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to start benchmarks: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String extractExecutable(String resourcePath, File tempDir) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new FileNotFoundException("Resource not found: " + resourcePath);
        }

        File outputFile = new File(tempDir, new File(resourcePath).getName());
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        //make the file executable
        outputFile.setExecutable(true);
        return outputFile.getAbsolutePath();
    }


}
