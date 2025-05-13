//MOLDOVANU_TUDOR_MAC_BENCHMARK//
package org.example.model;

import java.io.*;

public class RAMBenchmarkModel {

    //method to run a pre-compiled C program for RAM benchmarking and capture the output
    public String runCProgram(String executablePath) {
        try {
            //run the pre-compiled executable
            Process runProcess = new ProcessBuilder(executablePath).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            int exitCode = runProcess.waitFor();
            if (exitCode == 0) {
                return output.toString(); //return program output if successful
            } else {
                return "Error running program";
            }
        } catch (IOException | InterruptedException e) {
            return "Error during execution: " + e.getMessage();
        }
    }

    //method to fetch and display detailed RAM information for macOS
    public String getRAMInfo() {
        StringBuilder ramInfo = new StringBuilder();
        try {
            ramInfo.append("**********RAM Info**********\n");

            //fetch total RAM size in bytes and convert to GB
            String totalRAMBytes = getSysctlInfo("hw.memsize");
            long totalRAM = Long.parseLong(totalRAMBytes);  //convert String to long
            double totalRAMGB = totalRAM / (1024.0 * 1024.0 * 1024.0);  //convert bytes to GB

            //fetch RAM page size
            String pageSize = getSysctlInfo("hw.pagesize");
            long pageSizeBytes = Long.parseLong(pageSize); //convert page size to long


            //fetch number of physical CPUs (used as a proxy for RAM slots)
            String numRAMSlots = getSysctlInfo("hw.physicalcpu");

            //format the RAM info output
            ramInfo.append(String.format("Total RAM Size: %-5.2f GB\n", totalRAMGB));
            ramInfo.append(String.format("RAM Page Size: %-5s bytes\n", pageSize));
            ramInfo.append(String.format("Number of RAM Slots: %-5s\n", numRAMSlots));

            //display the RAM information
            ramInfo.append("****************************\n");

        } catch (Exception e) {
            ramInfo.append("Error fetching RAM info: ").append(e.getMessage()).append("\n");
        }

        return ramInfo.toString();
    }

    //helper method to get sysctl information for a given key
    private String getSysctlInfo(String sysctlKey) {
        String result = "N/A";  //default fallback value if we can't retrieve the information
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("sysctl", sysctlKey);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if (line != null && line.contains(":")) {
                result = line.split(":")[1].trim(); //extract the value after the colon
            }
            process.waitFor();
        } catch (Exception e) {
            result = "N/A"; //return "N/A" if the key cannot be found
        }
        return result;
    }
}
