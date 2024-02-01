package apap.tk.rumahsehat.service;
import apap.tk.rumahsehat.model.BarChartModel;
import apap.tk.rumahsehat.repository.BarChartDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class BarChartServiceImpl implements BarChartService {

    @Autowired
    BarChartDb barChartDb;

    @Override
    public BarChartModel get(long id) {
        return barChartDb.getReferenceById(id);
    }

    public void addBarChart(BarChartModel barChart) {
        barChartDb.save(barChart);
    }

}

