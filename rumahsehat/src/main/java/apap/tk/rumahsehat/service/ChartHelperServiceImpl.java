package apap.tk.rumahsehat.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import apap.tk.rumahsehat.model.ChartHelperModel;
import apap.tk.rumahsehat.repository.ChartHelperDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChartHelperServiceImpl implements ChartHelperService {

    @Autowired
    ChartHelperDb chartHelperDb;

    @Override
    public ChartHelperModel getHelperById(Long id) {
        Optional<ChartHelperModel> helper = chartHelperDb.findById(id);
        return helper.orElse(null);
    }

    @Override
    public List<ChartHelperModel> getAll() {
        return chartHelperDb.findAll();
    }

    @Override
    public void add(ChartHelperModel helper) {
        chartHelperDb.save(helper);
    }

    @Override
    public ChartHelperModel getHelperByTanggal(LocalDate tanggal) {
        Optional<ChartHelperModel> helper = chartHelperDb.findByTanggal(tanggal);
        return helper.orElse(null);
    }

    @Override
    public void deleteAll() {
        chartHelperDb.deleteAll();
    }

    @Override
    public int getRange(int umurPasien) {
        if (umurPasien <= 15) {
            return 1;
        } else if (umurPasien >= 16 && umurPasien <= 30) {
            return 2;
        } else if (umurPasien >= 31 && umurPasien <= 45) {
            return 3;
        } else if (umurPasien >= 46 && umurPasien <= 60) {
            return 4;
        } else {
            return 5;
        }
    }

    public int getTotalTagihanPerRange(ChartHelperModel helper, int range, int jumlahTagihanPasien){
        if (range == 1) {
            return helper.getRange1() + jumlahTagihanPasien;
        } else if (range == 2) {
            return helper.getRange2() + jumlahTagihanPasien;
        } else if (range == 3) {
            return helper.getRange3() + jumlahTagihanPasien;
        } else if (range == 4) {
            return helper.getRange4() + jumlahTagihanPasien;
        } else {
            return helper.getRange5() + jumlahTagihanPasien;
        }
    }


}
