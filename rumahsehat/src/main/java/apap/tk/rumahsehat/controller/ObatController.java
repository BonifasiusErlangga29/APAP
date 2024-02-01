package apap.tk.rumahsehat.controller;

import apap.tk.rumahsehat.setting.Setting;
import apap.tk.rumahsehat.model.ApotekerModel;
import apap.tk.rumahsehat.model.ObatModel;
import apap.tk.rumahsehat.model.UserModel;
import apap.tk.rumahsehat.service.ObatService;
import apap.tk.rumahsehat.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/obat")
public class ObatController {
    @Autowired
    ObatService obatService;
    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(ObatController.class);

    // Fitur 13 [Web] Melihat Daftar Obat (Admin, Apoteker)
    @GetMapping("/viewAll")
    public String viewall(Model model, Principal principal){
        UserModel user = userService.getUserByUsername(principal.getName());

        logger.info("User {} mengakses halaman listObat", user.getNama());

        List<ObatModel> listObat = obatService.list();
        model.addAttribute("listObat", listObat);
        if (userService.getUserByUsername(principal.getName()).getRole().equals("Apoteker")) {
            model.addAttribute("isApoteker", true);
        }
        return "list-obat";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAutoGrowCollectionLimit(20000);
    }

    // Fitur 12 (Mengubah Stok Obat)
    @GetMapping("/ubah-stok")
    public String ubahStokObatForm(Model model, Principal principal) {
        ApotekerModel apoteker = (ApotekerModel) userService.getUserByUsername(principal.getName());
        logger.info("Apoteker {} membuka halaman ubah stok obat", apoteker.getNama());
        model.addAttribute("host", Setting.CLIENT_BASE_URL);
        return "ubah-stok-obat";
    }

}
