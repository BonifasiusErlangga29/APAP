package apap.tk.rumahsehat.controller;

import apap.tk.rumahsehat.model.AppointmentModel;
import apap.tk.rumahsehat.model.DokterModel;
import apap.tk.rumahsehat.model.ResepModel;
import apap.tk.rumahsehat.model.TagihanModel;
import apap.tk.rumahsehat.model.UserModel;
import apap.tk.rumahsehat.service.AppointmentService;
import apap.tk.rumahsehat.service.TagihanService;
import apap.tk.rumahsehat.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;


@Controller
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    private TagihanService tagihanService;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(AppointmentController.class);

    @GetMapping("/list")
    public String listAppointment(Model model, Principal principal) {
        UserModel user = userService.getUserByUsername(principal.getName());
        if (user.getRole().equals("Admin")) {
            logger.info("Admin {} mengakses halaman listAppointment", user.getNama());
            model.addAttribute("listAppointment", appointmentService.listAll());
            model.addAttribute("isDokter", false);
        } else if (user.getRole().equals("Dokter")) {
            logger.info("Dokter {} mengakses halaman listAppointment", user.getNama());
            model.addAttribute("listAppointment", appointmentService.listByDokter((DokterModel) user));
            model.addAttribute("isDokter", true);
        }
        else {
            logger.warn("User {} mengakses halaman listAppointment, tetapi tidak diizinkan", user.getNama());
            return "redirect:/";
        }
        return "list-appointment";
    }

    // Fitur 8 Melihat Detail Appointment (Admin, Dokter, Pasien)
    @GetMapping("/detail")
    public String viewDetailAppointmentPage(@RequestParam(value = "kode") String kode, Model model, Principal principal) {
        UserModel user = userService.getUserByUsername(principal.getName());

        logger.info("User {} mengakses halaman detailAppointment", user.getNama());

        AppointmentModel appointment = appointmentService.getAppointmentByKodeAppointment(kode);

        ResepModel listResep = appointment.getResep();
        model.addAttribute("appointment", appointment);
        model.addAttribute("listResep", listResep);

        return "detail-appointment";
    }

    // Fitur 8 Melihat Detail Appointment (Admin, Dokter, Pasien) - UPDATE STATUS
    @GetMapping("/update-status-appointment")
    public String updateStatusAppointment(@RequestParam(value = "kode") String kode, Model model, Principal principal) {
        UserModel user = userService.getUserByUsername(principal.getName());

        AppointmentModel appointment = appointmentService.getAppointmentByKodeAppointment(kode);
        
        if (appointment.getResep() != null && !appointment.getResep().isDone()) {
            logger.info("User {} mengakses halaman update status appointment, namun resep masih belum selesai", user.getNama());
            return "error-resep-belum-selesai";
        }
        
        if (appointment.getResep() != null && appointment.getResep().getApoteker() == null) {
            logger.info("User {} mengakses halaman update status appointment, namun resep masih belum dikonfirmasi", user.getNama());
            return "error-resep-belum-dikonfirmasi";
        }
        
        appointment.setDone(true);
        appointmentService.updateStatusAppointment(appointment);
        logger.info("User {} mengakses halaman update status appointment", user.getNama());
        
        if(appointment.isDone()){
            var tagihan = new TagihanModel();

            int totalTagihan = appointment.getDokter().getTarif();

            tagihan.setAppointment(appointment);
            tagihan.setTanggalTerbuat(LocalDateTime.now());
            tagihan.setTanggalBayar(LocalDateTime.now());
            tagihan.setJumlahTagihan(totalTagihan);
            tagihan.setPaid(false);
            tagihanService.addTagihan(tagihan);
            appointment.setTagihan(tagihan);

            tagihanService.addTagihanToChart(tagihan, totalTagihan);

        }
        model.addAttribute("appointment", appointment);

        return "home";
    }
}
