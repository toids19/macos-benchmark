//MOLDOVANU_TUDOR_MAC_BENCHMARK//
package org.example.view;

public class CPUBenchmarkView {

    //method to display the output in the terminal
    public void displayOutput(String output) {
        System.out.println();
        System.out.println("**********CPU Benchmark***********");
        System.out.println(output);
        System.out.println("**********************************");
        System.out.println();

    }

    //method to display an error message
    public void displayError(String errorMessage) {
        System.out.println("Error: " + errorMessage);
    }
}
