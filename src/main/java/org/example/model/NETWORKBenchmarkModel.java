//MOLDOVANU_TUDOR_MAC_BENCHMARK//
package org.example.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NETWORKBenchmarkModel {

    //runs a pre-compiled C program for network benchmarking and captures the output
    public String runCProgram(String executablePath) {
        ProcessBuilder processBuilder = new ProcessBuilder(executablePath);
        try {
            Process runProcess = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            int exitCode = runProcess.waitFor();
            reader.close();
            return exitCode == 0 ? output.toString() : "Error running program";
        } catch (IOException | InterruptedException e) {
            return "Error during execution: " + e.getMessage();
        }
    }

    //fetches network information including interfaces, default gateway, and DNS settings
    public String getNetworkInfo() {
        StringBuilder networkInfo = new StringBuilder();
        try {
            networkInfo.append("\n**********Network Info**********\n");

            String interfaces = runSystemCommand("ifconfig");
            networkInfo.append(formatIPv4Addresses(interfaces));

            String defaultGateway = runSystemCommand("netstat -nr | grep default");
            networkInfo.append("Default Gateway: ").append(parseDefaultGateway(defaultGateway)).append("\n");

            String dns = runSystemCommand("scutil --dns | grep 'nameserver\\['");
            networkInfo.append("DNS Servers: ").append(parseDNSServers(dns)).append("\n");

            networkInfo.append("********************************\n");
        } catch (Exception e) {
            networkInfo.append("Error fetching network info: ").append(e.getMessage()).append("\n");
        }
        return networkInfo.toString();
    }

    //formats IPv4 addresses for network interfaces
    private String formatIPv4Addresses(String ifconfigOutput) {
        StringBuilder formatted = new StringBuilder();
        for (String line : ifconfigOutput.split("\n")) {
            if (line.trim().startsWith("inet ") && !line.contains("127.0.0.1")) {
                String ipAddress = line.trim().replace("inet ", "").split(" ")[0];
                formatted.append("IP Address: ").append(ipAddress).append("\n");
            }
        }
        if (formatted.length() == 0) {
            formatted.append("  No active IPv4 addresses found.\n");
        }
        return formatted.toString();
    }

    //parses the default gateway information
    private String parseDefaultGateway(String netstatOutput) {
        for (String line : netstatOutput.split("\n")) {
            if (line.contains("default")) {
                return line.substring(line.indexOf("default") + 8).trim().split(" ")[0];
            }
        }
        return "Not available";
    }

    //parses DNS server details, limiting to the first two servers
    private String parseDNSServers(String scutilOutput) {
        StringBuilder dnsInfo = new StringBuilder();
        int count = 0; // Counter for DNS entries
        for (String line : scutilOutput.split("\n")) {
            if (line.trim().startsWith("nameserver") && count < 2) {
                if (dnsInfo.length() > 0) {
                    dnsInfo.append(", ");
                }
                dnsInfo.append(line.substring(line.indexOf(":") + 1).trim());
                count++; //increment DNS server count
            }
        }
        return dnsInfo.toString().trim();
    }

    //executes system commands and captures their output
    private String runSystemCommand(String command) {
        StringBuilder output = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            process.waitFor();
        } catch (Exception e) {
            output.append("Failed to execute command: ").append(command).append(" - ").append(e.getMessage()).append("\n");
        }
        return output.toString();
    }
}
