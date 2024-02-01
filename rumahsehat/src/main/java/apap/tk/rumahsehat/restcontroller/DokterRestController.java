package apap.tk.rumahsehat.restcontroller;

import apap.tk.rumahsehat.dto.DokterDto;
import apap.tk.rumahsehat.model.DokterModel;
import apap.tk.rumahsehat.service.DokterService;
import apap.tk.rumahsehat.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/dokter")
public class DokterRestController {

    @Autowired
    DokterService dokterService;

    @Autowired
    UserService userService;


    @GetMapping(value="/list")
    public List<DokterDto> listDokter() {
       
        List<DokterModel> dokter = dokterService.getlistDokter();

        return dokter
                .stream()
                .map(dokterModel -> {
                    var dto = new DokterDto();
                    dto.setEmail(dokterModel.getEmail());
                    dto.setUsername(dokterModel.getUsername());
                    dto.setTarif(dokterModel.getTarif());
                    dto.setNama(dokterModel.getNama());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
