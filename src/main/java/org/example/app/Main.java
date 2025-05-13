//MOLDOVANU_TUDOR_MAC_BENCHMARK//
package org.example.app;

import org.example.view.MainBenchmarkView;
import org.example.view.MainInfoView;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {
    public static void main(String[] args) {
        //initialize the view that displays initial application information
        MainInfoView mainInfoView = new MainInfoView();

        try {
            //retrieve the user's home directory path to use for saving the output file
            String userHome = System.getProperty("user.home");
            //define the file object for storing benchmark results on the user's desktop
            File logFile = new File(userHome + "/Desktop/MacBenchmarkResults.txt");

            //set up a PrintStream to write to the file (overwrite existing file)
            PrintStream fileOut = new PrintStream(new FileOutputStream(logFile, false));

            //redirect standard output and standard error streams to the created file
            System.setOut(fileOut);
            System.setErr(fileOut);

            //display initial information in the application
            mainInfoView.showInfo();

            //log a message to the file about the application start
            System.out.println("Benchmark application starting...\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //schedule a job for the event-dispatching thread:
        //creating and showing the application's GUI
        SwingUtilities.invokeLater(() -> {
            //initialize and display the main benchmark application window
            MainBenchmarkView view = new MainBenchmarkView();
            //make the main application view visible
            view.setVisible(true);
        });
    }
}
