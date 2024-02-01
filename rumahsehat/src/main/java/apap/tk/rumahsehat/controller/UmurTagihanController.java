package apap.tk.rumahsehat.controller;


import apap.tk.rumahsehat.dto.BarChartDto;
import apap.tk.rumahsehat.dto.BarChartTagihanDto;
import apap.tk.rumahsehat.dto.UmurTagihanDto;
import apap.tk.rumahsehat.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import apap.tk.rumahsehat.service.BarChartService;
import apap.tk.rumahsehat.service.UmurTagihanService;
import apap.tk.rumahsehat.service.UserService;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/chart")
public class UmurTagihanController {
    String barChartStr = "barChart";
    String listTagihanStr = "listTagihan";
    String formBarChartTagihan = "form-barchart-tagihan";

    @Autowired
    private BarChartService barChartService;

    @Autowired
    private UmurTagihanService umurTagihanService;

    @Autowired
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(UmurTagihanController.class);


    @GetMapping("/defaultChartTagihan")
    public String chartTagihan(Principal principal) {
        UserModel user = userService.getUserByUsername(principal.getName());

        logger.info("Admin {} mengakses halaman chart tagihan (default)", user.getNama());
        return "defaultchart-tagihan";
    }

    @GetMapping("/lineChartTagihan")
    public String lineChartTagihan(Principal principal) {
        UserModel user = userService.getUserByUsername(principal.getName());

        logger.info("Admin {} mengakses halaman chart tagihan (line)", user.getNama());
        return "linechart-tagihan";
    }


    @GetMapping("/barChartTagihan")
    public String chartTagihanForm(Model model, Principal principal) {
        UserModel user = userService.getUserByUsername(principal.getName());

        logger.info("Admin {} mengakses halaman chart tagihan (bar)", user.getNama());
        var newBarChart = new BarChartDto();

        List<BarChartTagihanDto> newBarChartTagihan = new ArrayList<>();
        newBarChart.setListBarChartTagihan(newBarChartTagihan);

        newBarChart.getListBarChartTagihan().add(new BarChartTagihanDto());


        List<UmurTagihanModel> listUmurTagihan = umurTagihanService.getListUmurTagihan();
        List<UmurTagihanDto> umurTagihanDtoList = new ArrayList<>();
        for (UmurTagihanModel umurTagihanModel : listUmurTagihan) {
            var dto = new UmurTagihanDto();
            dto.setId(umurTagihanModel.getId());
            dto.setNama(umurTagihanModel.getNama());
            umurTagihanDtoList.add(dto);
        }

        newBarChart.setListBarChartTagihan(new ArrayList<>());
        model.addAttribute(barChartStr, newBarChart);
        model.addAttribute("listBarChart", newBarChartTagihan);
        model.addAttribute(listTagihanStr, listUmurTagihan);
        return formBarChartTagihan;
    }


    @PostMapping("/barChartTagihan")
    public String chartTagihanSubmit(@ModelAttribute BarChartDto barChart, Model model) {
        if (barChart.getListBarChartTagihan() == null) {
            barChart.setListBarChartTagihan(new ArrayList<>());
        }
        for (var i=0; i< barChart.getListBarChartTagihan().size();i++) {
            barChart.getListBarChartTagihan().get(i).setBarChartId(barChart.getId());
        }

        var barChartModel = new BarChartModel();
        barChartModel.setIsBarChartKuantitas(barChart.isBarChartKuantitas());
        List<BarChartTagihanModel> barChartTagihanModels = new ArrayList<>();
        for (var i = 0; i < barChart.getListBarChartTagihan().size(); i++) {
            BarChartTagihanDto dto = barChart.getListBarChartTagihan().get(i);
            var barChartTagihanModel = new BarChartTagihanModel();
            barChartTagihanModel.setIdBarChartTagihan(dto.getId());
            barChartTagihanModel.setTagihanSelected(umurTagihanService.getUmurTagihanById(dto.getTagihanSelectedId()));
            barChartTagihanModel.setBarChart(barChartService.get(dto.getBarChartId()));
            barChartTagihanModels.add(barChartTagihanModel);
        }
        barChartModel.setListBarChartTagihan(barChartTagihanModels);
        barChartService.addBarChart(barChartModel);
        Map<String, Integer> data = new LinkedHashMap<>();
        var jenisBarChart = "";
        if (barChart.isBarChartKuantitas()) {
            jenisBarChart = "Kuantitas";
            for (BarChartTagihanModel barChartTagihanModel : barChartTagihanModels) {
                UmurTagihanModel umurTagihan = umurTagihanService.getUmurTagihanById(barChartTagihanModel.getTagihanSelected().getId());
                data.put(umurTagihan.getNama(), umurTagihan.getKuantitasTagihan());
            }
        }
        else {
            jenisBarChart = "Total Tagihan";
            for (BarChartTagihanModel barChartTagihanModel : barChartTagihanModels) {
                UmurTagihanModel umurTagihan = umurTagihanService.getUmurTagihanById(barChartTagihanModel.getTagihanSelected().getId());
                data.put(umurTagihan.getNama(), umurTagihan.getTotalTagihanTerbayar());
            }
        }
        model.addAttribute("data", data);
        model.addAttribute("jenisBarChart", jenisBarChart);
        return "barchart-tagihan";
    }


    @PostMapping(value = "/barChartTagihan", params = {"addRowChart"})
    public String addRowTagihanMultiple(
            @ModelAttribute BarChartDto barChart,
            Model model
    ) {
        List<UmurTagihanModel> listUmurTagihan = umurTagihanService.getListUmurTagihan();
        if (barChart.getListBarChartTagihan() == null || barChart.getListBarChartTagihan().isEmpty()) {
            barChart.setListBarChartTagihan(new ArrayList<>());
            barChart.getListBarChartTagihan().add(new BarChartTagihanDto());
            model.addAttribute(barChartStr, barChart);
            model.addAttribute(listTagihanStr, listUmurTagihan);
            return formBarChartTagihan;
        }
        else if (barChart.getListBarChartTagihan().size() < 5) {
            barChart.getListBarChartTagihan().add(new BarChartTagihanDto());
            model.addAttribute(barChartStr, barChart);
            model.addAttribute(listTagihanStr, listUmurTagihan);
            return formBarChartTagihan;
        }
        else {
            model.addAttribute(barChartStr, barChart);
            model.addAttribute(listTagihanStr, listUmurTagihan);
            model.addAttribute("message", "Maaf, anda sudah mencapai jumlah maksimal range umur yang dapat ditampilkan");
            return formBarChartTagihan;
        }

    }

    @PostMapping(value = "/barChartTagihan", params = {"deleteRowChart"})
    public String deleteRowObatMultiple(
            @ModelAttribute BarChartDto barChart,
            @RequestParam("deleteRowChart") Integer row,
            Model model
    ) {
        List<UmurTagihanModel> listUmurTagihan = umurTagihanService.getListUmurTagihan();
        barChart.getListBarChartTagihan().remove(row.intValue());
        model.addAttribute(barChartStr, barChart);
        model.addAttribute(listTagihanStr, listUmurTagihan);
        return formBarChartTagihan;
    }
}
