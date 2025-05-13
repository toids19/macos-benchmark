//MOLDOVANU_TUDOR_MAC_BENCHMARK//
package org.example.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BATTERYBenchmarkModel {

    public String getDetailedBatteryInfo() {
        String[] command = {
                "/bin/sh",
                "-c",
                "system_profiler SPPowerDataType | sed -n '/Charge Information:/,/System Power Settings:/p' | sed '$d'"
        };

        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            return output.toString().isEmpty() ? "No detailed battery information available." : output.toString();
        } catch (IOException e) {
            return "Error retrieving detailed battery information: " + e.getMessage();
        }
    }

    //method to execute system commands and return output as a string
    private String runSystemCommand(String command) {
        StringBuilder output = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } catch (IOException e) {
            output.append("Error executing system command: ").append(e.getMessage()).append("\n");
        }
        return output.toString();
    }

    //method to get detailed battery information
    public String getBatteryInfo() {
        StringBuilder batteryInfo = new StringBuilder();
        try {
            batteryInfo.append("\n**********Battery Info**********\n");

            String pmsetOutput = runSystemCommand("pmset -g batt");
            batteryInfo.append(formatBatteryDetails(pmsetOutput));

            batteryInfo.append("********************************\n");
        } catch (Exception e) {
            batteryInfo.append("Error fetching battery info: ").append(e.getMessage()).append("\n");
        }
        return batteryInfo.toString();
    }

    //helper method to format battery details from pmset output
    private String formatBatteryDetails(String pmsetOutput) {
        StringBuilder formatted = new StringBuilder();
        String[] lines = pmsetOutput.split("\n");

        for (String line : lines) {
            if (line.contains("InternalBattery")) {
                // Extracting details using split
                String[] parts = line.trim().split("\\s+");

                if (parts.length >= 6) {
                    String batteryId = parts[1].replace("(", "").replace(")","");
                    String batteryPercentage = parts[2].replace(";", "");
                    String batteryStatus = parts[3].replace(";", "");
                    String batteryAutonomy = parts[4];

                    formatted
                            .append("Macbook InternalBattery (").append(batteryId).append(")\n")
                            .append("Battery Percentage: ").append(batteryPercentage).append("\n")
                            .append("Battery Status: ").append(batteryStatus).append("\n")
                            .append("Battery Autonomy Left: ").append(batteryAutonomy).append(" hours\n");
                } else {
                    formatted.append("Unexpected format for battery details.\n");
                }
            }
        }

        if (formatted.length() == 0) {
            formatted.append("No battery details found in the output.\n");
        }

        return formatted.toString();
    }
}
