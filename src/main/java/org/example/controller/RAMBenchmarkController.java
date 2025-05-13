//MOLDOVANU_TUDOR_MAC_BENCHMARK//
package org.example.controller;

import org.example.model.RAMBenchmarkModel;
import org.example.view.RAMBenchmarkView;

public class RAMBenchmarkController {



    public double RAMBenchmarkOverallAverage=0;
    public double getRAMBenchmarkOverallAverage() {
        return RAMBenchmarkOverallAverage;
    }

    public void setRAMBenchmarkOverallAverage(double RAMBenchmarkOverallAverage) {
        this.RAMBenchmarkOverallAverage = RAMBenchmarkOverallAverage;
    }
    private final RAMBenchmarkModel model;
    private final RAMBenchmarkView view;

    //store the last 10 execution times for write and read speeds
    private final double[] writeTimes = new double[10];
    private final double[] readTimes = new double[10];
    private int runCount = 0;

    public RAMBenchmarkController(RAMBenchmarkModel model, RAMBenchmarkView view) {
        this.model = model;
        this.view = view;
    }

    //method to run the memory benchmark 10 times
    public void run(String executablePath) {
        for (int i = 0; i < 10; i++) {
            int progressPercentage = (i + 1) * 10;

            //run the RAM benchmark program (write test)
            System.out.println("Benchmarking RAM: " + progressPercentage + "% (Write test | Read test)...");
            String result = model.runCProgram(executablePath);
            double writeSpeed = extractWriteSpeed(result);
            if (writeSpeed != -1) {
                storeWriteTime(writeSpeed);
            }

            //run the RAM benchmark program (read test)
            result = model.runCProgram(executablePath);
            double readSpeed = extractReadSpeed(result);
            if (readSpeed != -1) {
                storeReadTime(readSpeed);
            }
        }

        //calculate averages for write and read speeds
        double writeSpeedAvg = calculateAverage(writeTimes);
        double readSpeedAvg = calculateAverage(readTimes);

        //calculate the overall average speed
        RAMBenchmarkOverallAverage = (writeSpeedAvg + readSpeedAvg) / 2;

        //display the calculated averages
        view.displayOutput("Overall Average Time: " + String.format("%.6f GB/s", RAMBenchmarkOverallAverage));

    }

    //store the execution time for write operation
    private void storeWriteTime(double time) {
        writeTimes[runCount % 10] = time;
        runCount++;
    }

    //store the execution time for read operation
    private void storeReadTime(double time) {
        readTimes[runCount % 10] = time;
        runCount++;
    }

    //calculate the average of the last 10 run times
    private double calculateAverage(double[] times) {
        double sum = 0;
        int count = Math.min(runCount, 10);

        for (int i = 0; i < count; i++) {
            sum += times[i];
        }

        return count > 0 ? sum / count : 0;
    }

    //helper method to extract the write speed from the benchmark output
    private double extractWriteSpeed(String output) {
        String keyword = "Memory write speed:";
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
        String keyword = "Memory read speed:";
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
