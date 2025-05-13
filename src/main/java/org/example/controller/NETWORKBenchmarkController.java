//MOLDOVANU_TUDOR_MAC_BENCHMARK//
package org.example.controller;

import org.example.model.NETWORKBenchmarkModel;
import org.example.view.NETWORKBenchmarkView;

public class NETWORKBenchmarkController {

    public double NETWORKBenchmarkOverallAverage=0;

    public double getNETWORKBenchmarkOverallAverage() {
        return NETWORKBenchmarkOverallAverage;
    }

    public void setNETWORKBenchmarkOverallAverage(double NETWORKBenchmarkOverallAverage) {
        this.NETWORKBenchmarkOverallAverage = NETWORKBenchmarkOverallAverage;
    }

    private final NETWORKBenchmarkModel model;
    private final NETWORKBenchmarkView view;
    private final long[] transferTimes = new long[10];

    public NETWORKBenchmarkController(NETWORKBenchmarkModel model, NETWORKBenchmarkView view) {
        this.model = model;
        this.view = view;
    }

    //method to run the network benchmark 10 times and compute the average
    public void run(String executablePath) {
        int validRuns = 0; //counts only successful runs where a time is parsed

        for (int i = 0; i < 10; i++) {
            int progressPercentage = (i + 1) * 10;
            System.out.println("Benchmarking Network: " + progressPercentage + "% (Transfer test)...");

            String result = model.runCProgram(executablePath);
            if (result.startsWith("Error")) {
                view.displayError(result);
            } else {
                boolean timeRecorded = false;
                //assuming the output includes a line "Transfer time: XXXX microseconds"
                String[] lines = result.split("\n");
                for (String line : lines) {
                    if (line.contains("Transfer time")) {
                        try {
                            long transferTime = Long.parseLong(line.split(":")[1].trim().split(" ")[0]);
                            if (!timeRecorded) {  //ensure only the first valid time per run is recorded
                                transferTimes[validRuns++] = transferTime;
                                timeRecorded = true;
                            }
                        } catch (NumberFormatException e) {
                            view.displayError("Failed to parse transfer time from output.");
                        }
                    }
                }
            }

            if (validRuns == 10) break; //stop if we already have 10 valid runs
        }

        if (validRuns > 0) {
            NETWORKBenchmarkOverallAverage = calculateAverage(transferTimes, validRuns);
            view.displayOutput("Overall Average Time: " + String.format("%.6f ms", NETWORKBenchmarkOverallAverage));
        } else {
            view.displayError("No valid benchmark results to compute an average.");
        }
    }

    //helper method to calculate the average of the collected transfer times
    private long calculateAverage(long[] times, int count) {
        long sum = 0;
        for (int i = 0; i < count; i++) {
            sum += times[i];
        }
        return sum / count;
    }
}
