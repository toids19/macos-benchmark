//MOLDOVANU_TUDOR_MAC_BENCHMARK//
package org.example.controller;

import org.example.model.DISKBenchmarkModel;
import org.example.view.DISKBenchmarkView;

public class DISKBenchmarkController {



    public double DISKBenchmarkOverallAverage = 0;
    public double getDISKBenchmarkOverallAverage() {
        return DISKBenchmarkOverallAverage;
    }

    public void setDISKBenchmarkOverallAverage(double DISKBenchmarkOverallAverage) {
        this.DISKBenchmarkOverallAverage = DISKBenchmarkOverallAverage;
    }
    private final DISKBenchmarkModel model;
    private final DISKBenchmarkView view;

    //store the last 10 execution times for write and read speeds
    private final double[] writeSpeeds = new double[10];
    private final double[] readSpeeds = new double[10];
    private int runCount = 0;

    public DISKBenchmarkController(DISKBenchmarkModel model, DISKBenchmarkView view) {
        this.model = model;
        this.view = view;
    }

    //method to run the disk benchmark 10 times
    public void run(String executablePath) {
        for (int i = 0; i < 10; i++) {
            int progressPercentage = (i + 1) * 10;

            //run the disk benchmark program (write test)
            System.out.println("Benchmarking SSD: " + progressPercentage + "% (Write test | Read test)...");

            String result = model.runCProgram(executablePath);
            double writeSpeed = extractWriteSpeed(result);
            if (writeSpeed != -1) {
                storeWriteSpeed(writeSpeed);
            }

            //run the disk benchmark program (read test)
            result = model.runCProgram(executablePath);
            double readSpeed = extractReadSpeed(result);
            if (readSpeed != -1) {
                storeReadSpeed(readSpeed);
            }
        }

        //calculate averages for write and read speeds
        double writeSpeedAvg = calculateAverage(writeSpeeds);
        double readSpeedAvg = calculateAverage(readSpeeds);

        //calculate the overall average speed
        DISKBenchmarkOverallAverage = (writeSpeedAvg + readSpeedAvg) / 2;

        //display the calculated averages
        view.displayOutput("Overall Average Time: " + String.format("%.6f GB/s", DISKBenchmarkOverallAverage));
    }

    //store the execution time for write speed
    private void storeWriteSpeed(double speed) {
        writeSpeeds[runCount % 10] = speed;
        runCount++;
    }

    //store the execution time for read speed
    private void storeReadSpeed(double speed) {
        readSpeeds[runCount % 10] = speed;
        runCount++;
    }

    //calculate the average of the last 10 run speeds
    private double calculateAverage(double[] speeds) {
        double sum = 0;
        int count = Math.min(runCount, 10); // Only use up to 10 runs

        for (int i = 0; i < count; i++) {
            sum += speeds[i];
        }

        return count > 0 ? sum / count : 0;
    }

    //helper method to extract the write speed from the benchmark output
    private double extractWriteSpeed(String output) {
        String keyword = "Write Speed:";
        if (output.contains(keyword)) {
            try {
                String timeSubstring = output.split(keyword)[1].split("GB/s")[0].trim();
                return Double.parseDouble(timeSubstring); //return the extracted write speed
            } catch (Exception e) {
                System.err.println("Error parsing write speed from output: " + output);
                return -1.0;
            }
        }
        return -1.0;
    }

    //helper method to extract the read speed from the benchmark output
    private double extractReadSpeed(String output) {
        String keyword = "Read Speed:";
        if (output.contains(keyword)) {
            try {
                String timeSubstring = output.split(keyword)[1].split("GB/s")[0].trim();
                return Double.parseDouble(timeSubstring); //return the extracted read speed
            } catch (Exception e) {
                System.err.println("Error parsing read speed from output: " + output);
                return -1.0;
            }
        }
        return -1.0;
    }
}
