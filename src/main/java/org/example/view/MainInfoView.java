//MOLDOVANU_TUDOR_MAC_BENCHMARK//
package org.example.view;
import org.example.model.*;

public class MainInfoView {
    CPUBenchmarkModel CPUmodel = new CPUBenchmarkModel();
    RAMBenchmarkModel RAMmodel = new RAMBenchmarkModel();
    DISKBenchmarkModel DiskModel = new DISKBenchmarkModel();
    NETWORKBenchmarkModel NetworkModel = new NETWORKBenchmarkModel();
    SYSTEMRESPONSIVENESSBenchmarkModel SystemResponsivenessModel = new SYSTEMRESPONSIVENESSBenchmarkModel();
    BATTERYBenchmarkModel BatteryModel = new BATTERYBenchmarkModel();
    public void showInfo() {
        System.out.println("**********System Information***********");
        System.out.println(CPUmodel.getCPUInfo());
        System.out.println();
        System.out.println(RAMmodel.getRAMInfo());
        System.out.println();
        System.out.println(DiskModel.getDiskInfo());
        System.out.println(NetworkModel.getNetworkInfo());
        System.out.println();
        System.out.println(SystemResponsivenessModel.getSystemResponsivenessInfo());
        System.out.println(BatteryModel.getBatteryInfo());
        System.out.println();
        System.out.println("***************************************");
        System.out.println();


    }
}
