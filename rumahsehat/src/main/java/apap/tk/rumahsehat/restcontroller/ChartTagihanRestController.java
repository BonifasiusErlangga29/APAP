package apap.tk.rumahsehat.restcontroller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apap.tk.rumahsehat.model.AppointmentModel;
import apap.tk.rumahsehat.model.ChartHelperModel;
import apap.tk.rumahsehat.model.TagihanModel;
import apap.tk.rumahsehat.service.AppointmentService;
import apap.tk.rumahsehat.service.ChartHelperService;
import apap.tk.rumahsehat.service.TagihanService;

@RestController
@RequestMapping(value = "/api/chart")
public class ChartTagihanRestController {

    @Autowired
    TagihanService tagihanService;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    ChartHelperService chartHelperService;


    @GetMapping("")
    public List<TagihanModel> tagihanAll(Model model) {
        return tagihanService.getListTagihan();
    }


    @GetMapping("/lineChartTagihan")
    public List<ChartHelperModel> tagihanChart() {

        chartHelperService.deleteAll(); 
        for (TagihanModel tagihanEach: tagihanService.getListTagihan()) {
                AppointmentModel appointmentEach = tagihanEach.getAppointment();
                Integer umurPasien = appointmentEach.getPasien().getUmur();
                Integer jumlahTagihanPasien = tagihanEach.getJumlahTagihan();
                var localDate = tagihanEach.getTanggalTerbuat().toLocalDate();
                int range = chartHelperService.getRange(umurPasien);
                var total = 0;
                ChartHelperModel helper;
                if(chartHelperService.getHelperByTanggal(localDate) == null) {
                    helper = new ChartHelperModel();
                    helper.setTanggal(localDate);
                    total = jumlahTagihanPasien;
                    
                } else {
                    helper = chartHelperService.getHelperByTanggal(localDate);
                    total = chartHelperService.getTotalTagihanPerRange(helper, range, jumlahTagihanPasien);
                }

                if (range == 1) {
                    helper.setRange1(total);
                } else if (range == 2) {
                    helper.setRange2(total);
                } else if (range == 3) {
                    helper.setRange3(total);
                } else if (range == 4) {
                    helper.setRange4(total);
                } else {
                    helper.setRange5(total);
                }
                chartHelperService.add(helper);
        }

        return chartHelperService.getAll();
    }

    @GetMapping("/lineChartTagihan/{bulan}/{tahun}")
    public List<ChartHelperModel> tagihanChartBulan(
            @PathVariable Integer bulan,
            @PathVariable Integer tahun,
            Model model) {

        List<ChartHelperModel> oldList = this.tagihanChart();
        List<ChartHelperModel> finalList = new ArrayList<>();

        for(ChartHelperModel item : oldList) {
            if(item.getTanggal().getMonthValue() == bulan && item.getTanggal().getYear() == tahun) {
                finalList.add(item);
            }
        }
        return finalList;
    }
    @GetMapping("/lineChartTagihan/{tahun}")
    public List<ChartHelperModel> tagihanChartTahun(
            @PathVariable Integer tahun,
            Model model) {

        List<ChartHelperModel> oldList = this.tagihanChart();

        var jan = new ChartHelperModel();
        var feb = new ChartHelperModel();
        var mar = new ChartHelperModel();
        var apr = new ChartHelperModel();
        var mei = new ChartHelperModel();
        var jun = new ChartHelperModel();
        var jul = new ChartHelperModel();
        var aug = new ChartHelperModel();
        var sep = new ChartHelperModel();
        var okt = new ChartHelperModel();
        var nov = new ChartHelperModel();
        var des = new ChartHelperModel();

        jan.setTanggal(LocalDate.of(1, 1, 1));
        feb.setTanggal(LocalDate.of(1, 2, 1));
        mar.setTanggal(LocalDate.of(1, 3, 1));
        apr.setTanggal(LocalDate.of(1, 4, 1));
        mei.setTanggal(LocalDate.of(1, 5, 1));
        jun.setTanggal(LocalDate.of(1, 6, 1));
        jul.setTanggal(LocalDate.of(1, 7, 1));
        aug.setTanggal(LocalDate.of(1, 8, 1));
        sep.setTanggal(LocalDate.of(1, 9, 1));
        okt.setTanggal(LocalDate.of(1, 10, 1));
        nov.setTanggal(LocalDate.of(1, 11, 1));
        des.setTanggal(LocalDate.of(1, 12, 1));

        List<ChartHelperModel> finalList = new ArrayList<>(Arrays.asList(jan, feb, mar, apr, mei, jun, jul, aug, sep, okt, nov, des));

        for(ChartHelperModel helperOld : oldList) {
            if(helperOld.getTanggal().getYear() == tahun) {
                int bulan = helperOld.getTanggal().getMonthValue();
                for (ChartHelperModel helperNew : finalList) {
                    if (helperNew.getTanggal().getMonthValue() == bulan) {
                        helperNew.setRange1(helperNew.getRange1() + helperOld.getRange1());
                        helperNew.setRange2(helperNew.getRange2() + helperOld.getRange2());
                        helperNew.setRange3(helperNew.getRange3() + helperOld.getRange3());
                        helperNew.setRange4(helperNew.getRange4() + helperOld.getRange4());
                        helperNew.setRange5(helperNew.getRange5() + helperOld.getRange5());
                    }
                }
            }
        }

        return finalList;
    }

}
