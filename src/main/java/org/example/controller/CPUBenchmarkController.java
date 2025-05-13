//MOLDOVANU_TUDOR_MAC_BENCHMARK//
package org.example.controller;

import org.example.model.CPUBenchmarkModel;
import org.example.view.CPUBenchmarkView;

public class CPUBenchmarkController {

    public double CPUBenchmarkOverallAverage = 0;

    public double getCPUBenchmarkOverallAverage() {
        return CPUBenchmarkOverallAverage;
    }

    public void setCPUBenchmarkOverallAverage(double CPUBenchmarkOverallAverage) {
        this.CPUBenchmarkOverallAverage = CPUBenchmarkOverallAverage;
    }

    private final CPUBenchmarkModel model;

    private final CPUBenchmarkView view;

    //store the last 10 execution times
    private final double[] matrixMultiplicationTimes = new double[10];
    private final double[] aesDecryptionEncryptionTimes = new double[10];
    private int runCount = 0;

    public CPUBenchmarkController(CPUBenchmarkModel model, CPUBenchmarkView view) {
        this.model = model;
        this.view = view;
    }
    //method to run both pre-compiled C programs sequentially 10 times
    public void run(String matrixMultiplicationExecutablePath, String aesDecryptionEncryptionExecutablePath) {
        //loop for running both matrix multiplication and AES decryption/encryption 10 times
        for (int i = 0; i < 10; i++) {
            //calculate the progress as percentage
            int progressPercentage = (i + 1) * 10;

            //run the matrix multiplication program and store the time
            System.out.println("Benchmarking CPU: " + progressPercentage + "% (Matrix Multiplication | AES Decryption/Encryption)...");
            String matrixMultiplicationResult = model.runCProgram(matrixMultiplicationExecutablePath);
            double matrixMultiplicationTime = extractMatrixMultiplicationTime(matrixMultiplicationResult);
            if (matrixMultiplicationTime != -1) {
                storeMatrixMultiplicationTime(matrixMultiplicationTime);
            }

            //run the AES decryption/encryption program and store the time
            String aesDecryptionEncryptionResult = model.runCProgram(aesDecryptionEncryptionExecutablePath);
            double aesDecryptionEncryptionTime = extractAesDecryptionEncryptionTime(aesDecryptionEncryptionResult);
            if (aesDecryptionEncryptionTime != -1) {
                storeAesDecryptionEncryptionTime(aesDecryptionEncryptionTime);
            }
        }

        //calculate averages for both programs
        double matrixMultiplicationAverage = calculateAverage(matrixMultiplicationTimes);
        double aesDecryptionEncryptionAverage = calculateAverage(aesDecryptionEncryptionTimes);

        //calculate the overall average time
        CPUBenchmarkOverallAverage = (matrixMultiplicationAverage + aesDecryptionEncryptionAverage) / 2;

        //display the calculated averages
        view.displayOutput("Overall Average Time: " + String.format("%.6f ms", CPUBenchmarkOverallAverage));


    }



    //store the execution time for matrix multiplication
    private void storeMatrixMultiplicationTime(double time) {
        matrixMultiplicationTimes[runCount % 10] = time;
        runCount++;
    }

    //store the execution time for AES decryption/encryption
    private void storeAesDecryptionEncryptionTime(double time) {
        aesDecryptionEncryptionTimes[runCount % 10] = time;
        runCount++;
    }

    //calculate the average of the last 10 run times
    private double calculateAverage(double[] times) {
        double sum = 0;
        int count = Math.min(runCount, 10);

        for (int i = 0; i < count; i++) {
            sum += times[i];
        }

        if (count > 0) {
            return sum / count;
        } else {
            return 0;
        }

    }


    //helper method to extract matrix multiplication time from the C program's output
    private double extractMatrixMultiplicationTime(String output) {

        String keyword = "Matrix multiplication completed in";
        if (output.contains(keyword)) {
            try {
                //extract the part of the output after the keyword and before "seconds"
                String timeSubstring = output.split(keyword)[1].split("seconds")[0].trim();

                //convert the extracted substring to a double
                double timeInSeconds = Double.parseDouble(timeSubstring);

                //convert to milliseconds
                return timeInSeconds * 1000; // Convert seconds to milliseconds
            } catch (Exception e) {
                System.err.println("Error parsing matrix multiplication time from output: " + output);
                return -1.0;
            }
        } else {
            //if the keyword is not found, handle it accordingly (return error value)
            System.err.println("Expected time format not found in matrix multiplication output.");
            return -1.0;
        }
    }

    //helper method to extract AES decryption/encryption time from the C program's output
    private double extractAesDecryptionEncryptionTime(String output) {

        String keyword = "Total encryption and decryption time:";
        if (output.contains(keyword)) {
            try {
                //extract the part of the output after the keyword and before "seconds"
                String timeSubstring = output.split(keyword)[1].split("seconds")[0].trim();

                //convert the extracted substring to a double
                double timeInSeconds = Double.parseDouble(timeSubstring);

                //convert to milliseconds
                return timeInSeconds * 1000; // Convert seconds to milliseconds
            } catch (Exception e) {
                System.err.println("Error parsing AES decryption time from output: " + output);
                return -1.0;
            }
        } else {
            //if the keyword is not found, handle it accordingly (return error value)
            System.err.println("Expected time format not found in AES output.");
            return -1.0;
        }
    }
}
