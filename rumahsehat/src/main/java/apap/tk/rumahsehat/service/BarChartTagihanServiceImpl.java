package apap.tk.rumahsehat.service;

import apap.tk.rumahsehat.model.BarChartTagihanModel;
import apap.tk.rumahsehat.repository.BarChartTagihanDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class BarChartTagihanServiceImpl implements BarChartTagihanService{

    @Autowired
    BarChartTagihanDb barChartTagihanDb;

    public void addBarChartTagihan(BarChartTagihanModel barChartTagihan) {
        barChartTagihanDb.save(barChartTagihan);
    }

}


