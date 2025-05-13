//MOLDOVANU_TUDOR_MAC_BENCHMARK//
package org.example.view;

public class BATTERYBenchmarkView {

    public void displayOutput(String output) {
        System.out.println();
        System.out.println("**********Battery Benchmark***********");
        String[] lines = output.split("\n");
        for (String line : lines) {
            System.out.println(line.trim());  //trims leading and trailing white spaces
        }
        System.out.println("**************************************");
        System.out.println();
    }

    public void displayError(String errorMessage) {
        System.out.println("Error: " + errorMessage);
    }
}
