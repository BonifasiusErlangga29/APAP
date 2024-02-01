package apap.tk.rumahsehat.restcontroller;

import apap.tk.rumahsehat.dto.AppointmentDto;
import apap.tk.rumahsehat.dto.ResepDto;
import apap.tk.rumahsehat.model.AppointmentModel;
import apap.tk.rumahsehat.model.DokterModel;
import apap.tk.rumahsehat.model.JumlahModel;
import apap.tk.rumahsehat.model.PasienModel;
import apap.tk.rumahsehat.service.AppointmentService;
import apap.tk.rumahsehat.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AppointmentRestController {

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private UserService userService;


    Logger logger = LoggerFactory.getLogger(AppointmentRestController.class);

    @GetMapping(value="/list-appointment")
    public List<AppointmentDto> listAppointment(Principal principal) {

        PasienModel pasien = (PasienModel) userService.getUserByUsername(principal.getName());
        logger.info("Pasien {} mengakses halaman listAppointment", pasien.getNama());
        return appointmentService.listByPasien(pasien)
                .stream()
                .map(appointmentModel -> {
                    var dto = new AppointmentDto();
                    dto.setKode(appointmentModel.getKode());
                    dto.setDone(appointmentModel.isDone());
                    dto.setWaktuAwal(appointmentModel.getWaktuAwal());
                    dto.setNamaDokter(appointmentModel.getDokter().getNama());
                    dto.setUsernameDokter(appointmentModel.getDokter().getUsername());
                    dto.setUsernamePasien(appointmentModel.getPasien().getUsername());

                    var resepDto = new ResepDto();

                    if (appointmentModel.getResep() != null) {

                        resepDto.setId(appointmentModel.getResep().getId());
                        resepDto.setDone(appointmentModel.getResep().isDone());
                        resepDto.setCreatedAt(appointmentModel.getResep().getCreatedAt());
    
                        if (appointmentModel.getResep().getApoteker() != null) {
                            resepDto.setUsernameApoteker(appointmentModel.getResep().getApoteker().getUsername());
                            resepDto.setNamaApoteker(appointmentModel.getResep().getApoteker().getNama());
                        }
                        else {
                            resepDto.setUsernameApoteker("");
                            resepDto.setNamaApoteker("");
            
                        }
                        resepDto.setNamaPasien(appointmentModel.getPasien().getNama());
    
                        Map<String, Integer> listObat = new HashMap<>();
                        for (JumlahModel i : appointmentModel.getResep().getJumlah()){
                            listObat.put(i.getObat().getNama(), i.getKuantitas());
                        }
    
                        resepDto.setListObat(listObat);
                        dto.setResep(resepDto);
                    } else {
                        resepDto.setId(Long.parseLong("0"));
                        resepDto.setDone(false);
                        resepDto.setCreatedAt(LocalDateTime.now());
                        resepDto.setUsernameApoteker("");
                        resepDto.setNamaApoteker("");
                        resepDto.setNamaPasien("");
                        Map<String, Integer> a = new HashMap<>();
                        a.put("", 0);
                        resepDto.setListObat(a);
                        dto.setResep(resepDto);
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @PostMapping(value="appointment/add")
    public AppointmentModel createPasien(@Valid @RequestBody AppointmentDto appointmentDto, BindingResult bindingResult, Principal principal) {
        PasienModel pasien = (PasienModel) userService.getUserByUsername(principal.getName());
        logger.info("Pasien {} mengakses halaman add appointment", pasien.getNama());

        if (bindingResult.hasFieldErrors()) {
            logger.error("Pasien {} gagal menambahkan appoointment", pasien.getNama());

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field."
            );
        } else {
            appointmentDto.setUsernamePasien(principal.getName());
            DokterModel dokter = (DokterModel) userService.getUserByUsername(appointmentDto.getUsernameDokter());

            boolean kosong = appointmentService.checkWaktu(appointmentDto);

            if (!kosong) {
                logger.error("Pasien {} gagal menambahkan appointment", pasien.getNama());

                throw new ResponseStatusException(
                    HttpStatus.valueOf(400), "Jadwal tabrakan"
                );            
            }

            logger.info("Pasien {} berhasil menambahkan appoointment", pasien.getNama());

            var appointmentModel = new AppointmentModel();
            appointmentModel.setDokter(dokter);
            appointmentModel.setDone(false);
            appointmentModel.setPasien(pasien);
            appointmentModel.setWaktuAwal(appointmentDto.getWaktuAwal());
            appointmentService.addAppointment(appointmentModel);
            return appointmentModel;
        }
    }
}
