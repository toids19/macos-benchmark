//MOLDOVANU_TUDOR_MAC_BENCHMARK//
package org.example.model;

import java.io.*;

public class DISKBenchmarkModel {

    //method to run the pre-compiled C program for disk benchmarking and capture the output
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
            reader.close();
            if (exitCode == 0) {
                return output.toString(); //return program output if successful
            } else {
                return "Error running program";
            }
        } catch (IOException | InterruptedException e) {
            return "Error during execution: " + e.getMessage();
        }
    }
    //method to fetch and display basic disk information
    public String getDiskInfo() {
        StringBuilder diskInfo = new StringBuilder();
        try {
            diskInfo.append("**********Disk Info**********\n");

            //fetch disk info
            String diskInfoOutput = getDiskInfoCommand("df", "-P", "/");

            //split the output into individual lines
            String[] lines = diskInfoOutput.split("\n");

            if (lines.length > 1) {
                //the second line contains the actual disk space info
                String[] columns = lines[1].split("\\s+");  // Split by whitespace

                //extract values from the columns
                long totalDiskSpace = Long.parseLong(columns[1]); // Total disk space in 512-blocks
                long availableDiskSpace = Long.parseLong(columns[3]); // Available space in 512-blocks
                long usedDiskSpace = totalDiskSpace - availableDiskSpace; // Used space is the difference

                //convert from 512-blocks to bytes (1 block = 512 bytes)
                double totalDiskGB = totalDiskSpace * 512.0 / (1024.0 * 1024.0 * 1024.0);  // Convert blocks to GB
                double availableDiskGB = availableDiskSpace * 512.0 / (1024.0 * 1024.0 * 1024.0);  // Convert blocks to GB
                double usedDiskGB = usedDiskSpace * 512.0 / (1024.0 * 1024.0 * 1024.0);  // Convert blocks to GB

                //format the disk info output
                diskInfo.append(String.format("Total Disk Space: %-5.2f GB\n", totalDiskGB));
                diskInfo.append(String.format("Used Disk Space: %-5.2f GB\n", usedDiskGB));
                diskInfo.append(String.format("Available Disk Space: %-5.2f GB\n", availableDiskGB));
            } else {
                diskInfo.append("Error: Unable to fetch valid disk information.\n");
            }

            diskInfo.append("****************************\n");

        } catch (Exception e) {
            diskInfo.append("Error fetching disk info: ").append(e.getMessage()).append("\n");
        }

        return diskInfo.toString();
    }

    //helper method to get disk info using system command
    private String getDiskInfoCommand(String... command) {
        String result = "N/A";  //default fallback value if we can't retrieve the information
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n"); //capture the full output
            }
            process.waitFor();
            result = output.toString();  //capture the full output
        } catch (Exception e) {
            result = "N/A"; //return "N/A" if the command fails
        }
        return result;
    }
}


