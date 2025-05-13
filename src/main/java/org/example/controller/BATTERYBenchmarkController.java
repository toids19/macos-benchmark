//MOLDOVANU_TUDOR_MAC_BENCHMARK//
package org.example.controller;

import org.example.model.BATTERYBenchmarkModel;
import org.example.view.BATTERYBenchmarkView;

public class BATTERYBenchmarkController {

    private final BATTERYBenchmarkModel model;
    private final BATTERYBenchmarkView view;

    public BATTERYBenchmarkController(BATTERYBenchmarkModel model, BATTERYBenchmarkView view) {
        this.model = model;
        this.view = view;
    }

    public void displayDetailedBatteryInfo() {
        String batteryInfo = model.getDetailedBatteryInfo();
        if (batteryInfo.startsWith("Error")) {
            view.displayError(batteryInfo);
        } else {
            view.displayOutput(batteryInfo);
        }
    }
}
