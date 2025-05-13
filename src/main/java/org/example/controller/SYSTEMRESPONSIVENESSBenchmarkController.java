//MOLDOVANU_TUDOR_MAC_BENCHMARK//
package org.example.controller;

import org.example.model.SYSTEMRESPONSIVENESSBenchmarkModel;
import org.example.view.SYSTEMRESPONSIVENESSBenchmarkView;

public class SYSTEMRESPONSIVENESSBenchmarkController {

    public double SYSTEMRESPONSIVENESSBenchmarkOverallAverage=0;
    public double getSYSTEMRESPONSIVENESSBenchmarkOverallAverage() {
        return SYSTEMRESPONSIVENESSBenchmarkOverallAverage;
    }

    public void setSYSTEMRESPONSIVENESSBenchmarkOverallAverage(double SYSTEMRESPONSIVENESSBenchmarkOverallAverage) {
        this.SYSTEMRESPONSIVENESSBenchmarkOverallAverage = SYSTEMRESPONSIVENESSBenchmarkOverallAverage;
    }
    private final SYSTEMRESPONSIVENESSBenchmarkModel model;
    private final SYSTEMRESPONSIVENESSBenchmarkView view;

    public SYSTEMRESPONSIVENESSBenchmarkController(SYSTEMRESPONSIVENESSBenchmarkModel model, SYSTEMRESPONSIVENESSBenchmarkView view) {
        this.model = model;
        this.view = view;
    }

    //method to run the responsiveness benchmark
    public void run(String executablePath) {
        String result = model.runCProgram(executablePath);
        if (result.startsWith("Error")) {
            view.displayError(result);
        } else {
            String[] launches = result.split("\n");
            int numApps = launches.length;
            double totalLaunchTime = 0.0;

            for (int i = 0; i < numApps-1; i++) {
                int progressPercentage = (i + 1) * 100 / (numApps-1);  //calculate progress percentage
                System.out.println("Benchmarking SystemResponsiveness: " + progressPercentage + "% (Opening Applications)...");

                //display application launch information with a delay
                try {
                    Thread.sleep(200); // 0.2 second delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                totalLaunchTime += extractTime(launches[i]);
            }

            SYSTEMRESPONSIVENESSBenchmarkOverallAverage = totalLaunchTime / numApps;
            view.displayOutput("Overall Average Time: " + String.format("%.2f seconds", SYSTEMRESPONSIVENESSBenchmarkOverallAverage));
        }
    }

    //helper method to extract time from the output line
    private double extractTime(String launchInfo) {
        String[] parts = launchInfo.split(" ");
        return Double.parseDouble(parts[parts.length - 2]);
    }
}
