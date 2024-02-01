package apap.tk.rumahsehat.restcontroller;

import apap.tk.rumahsehat.dto.TagihanDto;
import apap.tk.rumahsehat.model.UserModel;
import apap.tk.rumahsehat.service.TagihanService;
import apap.tk.rumahsehat.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tagihan")
public class TagihanRestController {

    @Autowired
    TagihanService tagihanService;

    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(TagihanRestController.class);

    @GetMapping(value="/list")
    public List<TagihanDto> listObat(Principal principal) {
        UserModel user = userService.getUserByUsername(principal.getName());
        logger.info("Admin {} mengakses API list tagihan", user.getNama());
        return tagihanService.getListTagihan()
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
                    dto.setUmurPasien(tagihanModel.getAppointment().getPasien().getUmur());
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
