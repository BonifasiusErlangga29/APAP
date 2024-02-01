package apap.tk.rumahsehat.restcontroller;

import apap.tk.rumahsehat.dto.ObatDto;
import apap.tk.rumahsehat.model.ApotekerModel;
import apap.tk.rumahsehat.model.ObatModel;
import apap.tk.rumahsehat.service.ObatService;
import apap.tk.rumahsehat.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/obat")
public class ObatRestController {

    @Autowired
    ObatService obatService;

    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(ObatRestController.class);

    @GetMapping(value="/list")
    public List<ObatDto> listObat(Principal principal) {
        ApotekerModel apoteker = (ApotekerModel) userService.getUserByUsername(principal.getName());
        logger.info("Apoteker {} melihat list obat", apoteker.getNama());
        return obatService.list()
                .stream()
                .map(obat -> {
                    var dto = new ObatDto();
                    dto.setId(obat.getIdObat());
                    dto.setNama(obat.getNama());
                    dto.setHarga(obat.getHarga());
                    dto.setStok(obat.getStok());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @PutMapping(value="/update/{idObat}")
    public ObatDto update(@PathVariable String idObat, @RequestBody ObatDto obat, Principal principal) {
        ApotekerModel apoteker = (ApotekerModel) userService.getUserByUsername(principal.getName());
        logger.info("Apoteker {} melakukan update obat dengan id {}", apoteker.getNama(), idObat);
        var obatUpdate = new ObatModel();
        obatUpdate.setNama(obat.getNama());
        obatUpdate.setHarga(obat.getHarga());
        obatUpdate.setStok(obat.getStok());
        ObatModel obatAfterUpdate = obatService.update(idObat, obatUpdate);

        var dto = new ObatDto();
        dto.setId(obatAfterUpdate.getIdObat());
        dto.setNama(obatAfterUpdate.getNama());
        dto.setHarga(obatAfterUpdate.getHarga());
        dto.setStok(obatAfterUpdate.getStok());
        return dto;
    }
}
