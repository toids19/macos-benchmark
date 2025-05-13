//MOLDOVANU_TUDOR_MAC_BENCHMARK//
package org.example.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class SYSTEMRESPONSIVENESSBenchmarkModel {

    //executes the pre-compiled C program and captures its output
    public String runCProgram(String executablePath) {
        ProcessBuilder processBuilder = new ProcessBuilder(executablePath);
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            int exitCode = process.waitFor();
            reader.close();
            return exitCode == 0 ? output.toString() : "Error running program";
        } catch (IOException | InterruptedException e) {
            return "Error during execution: " + e.getMessage();
        }
    }
    //gets the number of installed applications and their total disk usage directly from the Applications folder
    public String getSystemResponsivenessInfo() {
        //use the absolute path to the Applications folder
        String directoryPath = "/Applications";
        //count all items (not just directories to match Finder's item count)
        String countCommand = "ls '" + directoryPath + "' | wc -l";
        //calculate total size and format it correctly to always show GB
        String sizeCommand = "du -sh '" + directoryPath + "' | awk '{if($1 ~ /G$/) print $1 \"B\"; else print $1 \" GB\";}'";

        StringBuilder result = new StringBuilder("**********SystemResponsiveness Info**********\n");

        try {
            //execute the count command
            Process countProcess = new ProcessBuilder("bash", "-c", countCommand).start();
            BufferedReader countReader = new BufferedReader(new InputStreamReader(countProcess.getInputStream()));
            String appCount = countReader.readLine(); //read count result
            int exitValue = countProcess.waitFor();
            countReader.close();

            if (exitValue != 0) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(countProcess.getErrorStream()));
                String errorMessage = errorReader.readLine();
                errorReader.close();
                return result.append("Error counting applications: ").append(errorMessage == null ? "Unknown error" : errorMessage).toString();
            }

            //display the number of installed applications
            result.append("Number of installed applications: ").append(appCount == null ? "0" : appCount.trim()).append("\n");

            //execute the size command
            Process sizeProcess = new ProcessBuilder("bash", "-c", sizeCommand).start();
            BufferedReader sizeReader = new BufferedReader(new InputStreamReader(sizeProcess.getInputStream()));
            String totalSize = sizeReader.readLine(); //read size result
            exitValue = sizeProcess.waitFor();
            sizeReader.close();

            if (exitValue != 0) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(sizeProcess.getErrorStream()));
                String errorMessage = errorReader.readLine();
                errorReader.close();
                return result.append("Error calculating size: ").append(errorMessage == null ? "Unknown error" : errorMessage).toString();
            }

            //display the total size, formatted as GB
            result.append("Total size of applications: ").append(totalSize == null ? "0 GB" : totalSize).append("\n");

        } catch (IOException | InterruptedException e) {
            return result.append("Error fetching system responsiveness info: ").append(e.getMessage()).toString();
        }

        result.append("*********************************************\n");
        return result.toString();
    }
}
