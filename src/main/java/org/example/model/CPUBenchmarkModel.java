//MOLDOVANU_TUDOR_MAC_BENCHMARK//
package org.example.model;

import java.io.*;


public class CPUBenchmarkModel {

    //method to run a pre-compiled C program and capture the output
    public String runCProgram(String executablePath) {
        try {
            // Run the pre-compiled executable
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

    //method to fetch and display detailed CPU information for macOS
    public String getCPUInfo() {
        StringBuilder cpuInfo = new StringBuilder();
        try {
            cpuInfo.append("\n");
            cpuInfo.append("**********CPU Info**********\n");

            //fetch CPU Model
            String cpuModel = getSysctlInfo("machdep.cpu.brand_string");
            cpuInfo.append(String.format("CPU Model: %-30s\n", cpuModel));

            //fetch number of physical cores
            String physicalCores = getSysctlInfo("machdep.cpu.core_count");
            cpuInfo.append(String.format("Physical Cores: %-15s\n", physicalCores));

            //fetch logical CPU count (threads)
            String logicalCPUs = getSysctlInfo("machdep.cpu.thread_count");
            cpuInfo.append(String.format("Logical CPUs (Threads): %-5s\n", logicalCPUs));


            //fetch CPU Architecture
            String architecture = getSysctlInfo("hw.machine");
            cpuInfo.append(String.format("CPU Architecture: %-15s\n", architecture));

            //end of the benchmark info
            cpuInfo.append("****************************\n");

        } catch (Exception e) {
            cpuInfo.append("Error fetching CPU info: ").append(e.getMessage()).append("\n");
        }

        return cpuInfo.toString();
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
