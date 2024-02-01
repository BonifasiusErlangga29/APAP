package apap.tk.rumahsehat.service;

import apap.tk.rumahsehat.model.BarChartModel;

public interface BarChartService {

    BarChartModel get(long id);
    void addBarChart(BarChartModel barChart);
}

