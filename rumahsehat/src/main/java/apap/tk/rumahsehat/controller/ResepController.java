package apap.tk.rumahsehat.controller;

import apap.tk.rumahsehat.dto.ResepDto;
import apap.tk.rumahsehat.model.*;
import apap.tk.rumahsehat.service.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import apap.tk.rumahsehat.repository.ObatDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/resep")
public class ResepController {
    String listObatStr = "listObat";
    String resepStr = "resep";
    String formAddResep = "form-add-resep";

    @Qualifier("resepServiceImpl")
    @Autowired
    private ResepService resepService;

    @Autowired
    ObatDb obatDb;

    @Autowired
    private ObatService obatService;

    @Autowired
    private JumlahService jumlahService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private TagihanService tagihanService;

    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(ResepController.class);


    @GetMapping("/viewAll")
    public String viewall(Model model, Principal principal){
        UserModel user = userService.getUserByUsername(principal.getName());

        logger.info("User {} mengakses halaman listResep", user.getNama());

        List<ResepModel> listResep = resepService.getResepModel();
        model.addAttribute("listResep", listResep);
        return "list-resep";
    }

    // Fitur 11 (Melihat Detail Resep)
    @GetMapping("/view")
    public String viewDetailResep(@RequestParam(value = "id") Long id, Model model, Principal principal) {
        UserModel user = userService.getUserByUsername(principal.getName());

        logger.info("User {} mengakses halaman detailResep", user.getNama());
        ResepModel resep = resepService.getResepByIdResep(id);
        List<JumlahModel> listJumlahObat = resep.getJumlah();
        List<ObatModel> listObat = new ArrayList<>();
        List<Integer> listKuantitas = new ArrayList<>();

        AppointmentModel appointment = resep.getAppointment();
        String dokter = appointment.getDokter().getNama();
        String pasien = appointment.getPasien().getNama();

        for (JumlahModel i: listJumlahObat) {
            listObat.add(i.getObat());
            listKuantitas.add(i.getKuantitas());
        }

        String status;
        String namaApoteker = null;
        if(resep.isDone()) {
            ApotekerModel apoteker = resep.getApoteker();
            namaApoteker = apoteker.getNama();
            status = "Sudah selesai";
        } else {
            status = "Belum selesai";
        }
        model.addAttribute("statusresep", status);
        model.addAttribute("apoteker", namaApoteker);

        if (userService.getUserByUsername(principal.getName()).getRole().equals("Apoteker")) {
            model.addAttribute("isApoteker", true);
        }

        model.addAttribute("dokter", dokter);
        model.addAttribute("pasien", pasien);
        model.addAttribute("listJumlahObat", listJumlahObat);
        model.addAttribute(listObatStr, listObat);
        model.addAttribute("listKuantitas", listKuantitas);
        model.addAttribute(resepStr, resep);
        model.addAttribute("dokter", resep.getAppointment().getDokter());
        model.addAttribute("pasien", resep.getAppointment().getPasien());
        return "detail-resep";

    }

    @GetMapping("/konfirmasi-resep/{id}")
    public String updateStatusResepFormPage(@PathVariable Long id, Model model, Principal principal) {
        ResepModel resep = resepService.getResepByIdResep(id);

        model.addAttribute(resepStr, resep);
        return "konfirmasi-resep";
    }

    @PostMapping("/konfirmasi-resep/{id}")
    public String updateStatusResepSubmitPage(@PathVariable Long id, Model model, Principal principal) {
        ResepModel resep = resepService.getResepByIdResep(id);

        if (!cekStokObat(resep.getJumlah())) {
            return "eror-konfirmasi-resep";
        }
        
        UserModel user = userService.getUserByUsername(principal.getName());
        ApotekerModel apoteker = (ApotekerModel) user;
        resepService.updateStatusResep(resep, apoteker);
        List<JumlahModel> listJumlahObat = resep.getJumlah();

        var time = LocalDateTime.now();


        if (resep.isDone()) {
            var tagihan = new TagihanModel();
            AppointmentModel appointment = resep.getAppointment();
            Integer tarifDokter = appointment.getDokter().getTarif();
            var totalHargaResep = 0;
            for (JumlahModel i: listJumlahObat) {
                totalHargaResep += (i.getObat().getHarga() * i.getKuantitas());
            }
            int totalTagihan = tarifDokter + totalHargaResep;

            appointment.setDone(true);
            tagihan.setAppointment(appointment);
            tagihan.setTanggalTerbuat(time);
            tagihan.setTanggalBayar(time);
            tagihan.setJumlahTagihan(totalTagihan);
            tagihan.setPaid(false);
            
            tagihanService.addTagihan(tagihan);
            appointment.setTagihan(tagihan);

            tagihanService.addTagihanToChart(tagihan, totalTagihan);

            model.addAttribute("tagihan", tagihan.getKode());
            return "konfirmasi-resep-submit";
        } 
        
        else {
            return "error";
        }

    }

    private boolean cekStokObat(List<JumlahModel> listResepObat) {
        for (JumlahModel resep: listResepObat) {
            ObatModel obat = obatService.findById(resep.getObat().getIdObat());
            if (resep.getKuantitas() > obat.getStok()) {
                return false;
            }
        }

        for (JumlahModel resep: listResepObat) {
            ObatModel obat = obatService.findById(resep.getObat().getIdObat());
            int jumlahObat = obat.getStok() - resep.getKuantitas();
            obat.setStok(jumlahObat);
            obatService.updateObat(obat);
        }

        return true;
    }
    

    @GetMapping("/add")
    public String addResepFormPage(@RequestParam(value = "kode") String kode, Model model, Principal principal) {
        UserModel user = userService.getUserByUsername(principal.getName());

        logger.info("User {} mengakses halaman tambahResep", user.getNama());

        var resep = new ResepDto();
        List<ObatModel> dataObat = obatDb.getAllData();
        List<JumlahModel> listJumlah = new ArrayList<>();

        resep.setJumlah(listJumlah);
        resep.getJumlah().add(new JumlahModel());

        model.addAttribute(resepStr, resep);
        model.addAttribute(listObatStr, dataObat);
        model.addAttribute("kode", kode);

        return formAddResep;
    }

    @PostMapping(value = "/add", params = {"addRow"})
    public String addRowObatMultiple(@RequestParam(value = "kode") String kode, @ModelAttribute ResepDto resep,
                                      Model model
    ) {
        if (resep.getJumlah() == null || resep.getJumlah().isEmpty()) {
            resep.setJumlah(new ArrayList<>());
        }

        resep.getJumlah().add(new JumlahModel());
        List<ObatModel> listObat = obatDb.getAllData();

        model.addAttribute(resepStr, resep);
        model.addAttribute(listObatStr, listObat);       
        model.addAttribute("kode", kode);


        return formAddResep;
    }

    @PostMapping(value = "/add", params = {"deleteRow"})
    public String deleteRowObat(
            @ModelAttribute ResepDto resep,
            @RequestParam("deleteRow") Integer row,
            Model model, @RequestParam(value = "kode") String kode
    ) {
        resep.getJumlah().remove(row.intValue());

        List<ObatModel> listObat = obatDb.getAllData();

        model.addAttribute(resepStr, resep);
        model.addAttribute(listObatStr, listObat);
        model.addAttribute("kode", kode);


        return formAddResep;
    }

    @PostMapping("/add")
    public String addResepSubmitPage(@ModelAttribute ResepDto resep, @RequestParam(value = "kode") String kode, Model model) { 
        var resepNew = new ResepModel();
        AppointmentModel apt = appointmentService.getAppointmentByKodeAppointment(kode);
        var time = LocalDateTime.now();
        resepNew.setApoteker(null);
        resepNew.setCreatedAt(time);
        resepNew.setDone(false);
        resepNew.setAppointment(apt);

        List<JumlahModel> listJumlah = resep.getJumlah();
        if(listJumlah == null)  {
            listJumlah = new ArrayList<>();
        }

        resepService.addResep(resepNew);
        for (JumlahModel jumlah : listJumlah) {
            var key = new JumlahKey(resepNew.getId(), jumlah.getObat().getIdObat());
            jumlah.setResep(resepNew);
            jumlah.setId(key);
            jumlahService.addJumlah(jumlah);
        }
    
        AppointmentModel appointment = appointmentService.getAppointmentByKodeAppointment(kode);

        appointment.setResep(resepNew);
        resepNew.setAppointment(appointment);

        resepService.addResep(resepNew);
        appointmentService.addAppointment(appointment);


        resep.setJumlah(listJumlah);

        return "add-resep";
    }

}
