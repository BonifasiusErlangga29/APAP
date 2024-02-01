package apap.tk.rumahsehat.restcontroller;

import apap.tk.rumahsehat.dto.TagihanDto;

import apap.tk.rumahsehat.model.PasienModel;
import apap.tk.rumahsehat.model.TagihanModel;
import apap.tk.rumahsehat.service.PasienService;
import apap.tk.rumahsehat.service.TagihanService;
import apap.tk.rumahsehat.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/v1/bill")
public class BillRestController {

    @Autowired
    private TagihanService tagihanService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasienService pasienService;

    Logger logger = LoggerFactory.getLogger(BillRestController.class);

    @GetMapping("/{kode}")
    public TagihanDto getTagihan(@PathVariable String kode, Principal principal) {
        PasienModel pasienn = (PasienModel) userService.getUserByUsername(principal.getName());
        logger.info("Pasien {} mengakses halaman detail tagihan", pasienn.getNama());

        var tagihanModel = tagihanService.getTagihanByKode(kode);
        var dto = new TagihanDto();
                    dto.setKode(tagihanModel.getKode());
                    dto.setJumlahTagihan(tagihanModel.getJumlahTagihan());
                    dto.setKodeAppointment(tagihanModel.getAppointment().getKode());
                    dto.setPaid(tagihanModel.isPaid());
                    dto.setTanggalBayar(tagihanModel.getTanggalBayar());
                    dto.setTanggalTerbuat(tagihanModel.getTanggalTerbuat());
                    dto.setUsernamePasien(tagihanModel.getAppointment().getPasien().getUsername());
        return dto;
    }

    @GetMapping(value="/list")
    public List<TagihanDto> listTagihan(Principal principal) {

        PasienModel pasien = (PasienModel) userService.getUserByUsername(principal.getName());
        logger.info("Pasien {} mengakses halaman listTagihan", pasien.getNama());
        return tagihanService.listByPasien(pasien)
                .stream()
                .map(tagihanModel -> {
                    var dto = new TagihanDto();
                    dto.setKode(tagihanModel.getKode());
                    dto.setJumlahTagihan(tagihanModel.getJumlahTagihan());
                    dto.setKodeAppointment(tagihanModel.getAppointment().getKode());
                    dto.setPaid(tagihanModel.isPaid());
                    dto.setTanggalBayar(tagihanModel.getTanggalBayar());
                    dto.setTanggalTerbuat(tagihanModel.getTanggalTerbuat());
                    dto.setUsernamePasien(tagihanModel.getAppointment().getPasien().getUsername());
                    return dto;
                })
                .collect(Collectors.toList());
        
    }

    @PostMapping(value="/bayar/{kode}")
    public TagihanModel bayar(@PathVariable String kode, @Valid @RequestBody TagihanDto tagihanDto, Principal principal) {
        PasienModel pasien = (PasienModel) userService.getUserByUsername(principal.getName()); 
        
        if (tagihanDto.getJumlahTagihan() <= pasien.getSaldo()) {
            logger.info("Pasien {} berhasil membayar tagihan", pasien.getNama());
            
            pasien.setSaldo(pasien.getSaldo() - tagihanDto.getJumlahTagihan());
            pasienService.updatePasien(pasien);
            TagihanModel tagihan = tagihanService.getTagihanByKode(tagihanDto.getKode());
            tagihan.setPaid(true);
            tagihan.setTanggalBayar(LocalDateTime.now());
            tagihanService.updateTagihan(tagihan);
            return tagihan;
        }
        
        logger.info("Pasien {} gagal membayar tagihan", pasien.getNama());
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Saldo tidak cukup"
        );
    }
}
