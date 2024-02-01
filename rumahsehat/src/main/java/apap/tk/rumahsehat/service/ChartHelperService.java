package apap.tk.rumahsehat.service;

import java.time.LocalDate;
import java.util.List;

import apap.tk.rumahsehat.model.ChartHelperModel;
import org.springframework.stereotype.Service;

@Service
public interface ChartHelperService {
    public ChartHelperModel getHelperById(Long id);
    public ChartHelperModel getHelperByTanggal(LocalDate tanggal);
    public List<ChartHelperModel> getAll();
    public void deleteAll();
    public void add(ChartHelperModel tagihan);
    public int getRange(int umurPasien);
    public int getTotalTagihanPerRange(ChartHelperModel helper, int range, int jumlahTagihanPasien);
}