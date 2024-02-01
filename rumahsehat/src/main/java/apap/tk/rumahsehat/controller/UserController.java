package apap.tk.rumahsehat.controller;

import apap.tk.rumahsehat.dto.ApotekerDto;
import apap.tk.rumahsehat.dto.DokterDto;
import apap.tk.rumahsehat.model.ApotekerModel;
import apap.tk.rumahsehat.model.DokterModel;
import apap.tk.rumahsehat.model.PasienModel;
import apap.tk.rumahsehat.model.UserModel;
import apap.tk.rumahsehat.service.ApotekerService;
import apap.tk.rumahsehat.service.DokterService;
import apap.tk.rumahsehat.service.PasienService;
import apap.tk.rumahsehat.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    String dokterStr = "dokter";
    String formAddDokter = "form-add-dokter";
    String duplicateUsn = "duplicate_username";
    String aptStr = "apoteker";
    String formAddApoteker = "form-add-apoteker";
    String duplicateEmail = "duplicate_email";
    String msg = "User dengan username ";
    String msg2 = " telah terdaftar";
    String msg3 = "User dengan email ";

    @Autowired
    private ApotekerService apotekerService;

    @Autowired
    private DokterService dokterService;

    @Autowired
    private PasienService pasienService;

    @Autowired
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(UserController.class);


    @GetMapping("")
    public String manajemenUser(Principal principal) {
        UserModel user = userService.getUserByUsername(principal.getName());

        logger.info("Admin {} mengakses halaman manajemen user", user.getNama());
        return "manajemen-user";
    }

    @GetMapping("/add-dokter")
    public String addDokterFormPage(Model model, Principal principal){
        UserModel user = userService.getUserByUsername(principal.getName());

        logger.info("Admin {} mengakses halaman tambah dokter", user.getNama());
        var dokter = new DokterModel();
        model.addAttribute(dokterStr, dokter);
        return formAddDokter;
    }

    @PostMapping(value = "/add-dokter")
    public String addDokterSubmit(@ModelAttribute DokterDto dokterDto, Model model){
        var dokter = new DokterModel();
        dokter.setEmail(dokterDto.getEmail());
        dokter.setNama(dokterDto.getNama());
        dokter.setPassword(dokterDto.getPassword());
        dokter.setTarif(dokterDto.getTarif());
        dokter.setUsername(dokterDto.getUsername());
        dokter.setRole("Dokter");
        UserModel userByUsername = userService.getUserByUsername(dokter.getUsername());
        UserModel userByEmail = userService.getUserByEmail(dokter.getEmail());

        if (userByUsername != null && userByEmail != null) {
            model.addAttribute(dokterStr, dokter);
            model.addAttribute(duplicateUsn, msg + userByUsername.getUsername() + msg2);
            model.addAttribute(duplicateEmail, msg3 + userByEmail.getEmail() + msg2);
            return formAddDokter;
        } else if (userByUsername != null) {
            model.addAttribute(dokterStr, dokter);
            model.addAttribute(duplicateUsn, msg + userByUsername.getUsername() + msg2);
            return formAddDokter;
        } else if (userByEmail != null) {
            model.addAttribute(dokterStr, dokter);
            model.addAttribute(duplicateEmail, msg3 + userByEmail.getEmail() + msg2);
            return formAddDokter;
        }

        dokterService.addDokter(dokter);
        model.addAttribute(dokterStr, dokter);
        return "add-dokter";
    }

    @GetMapping("/add-apoteker")
    public String addApotekerFormPage(Model model, Principal principal){
        UserModel user = userService.getUserByUsername(principal.getName());

        logger.info("Admin {} mengakses halaman tambah apoteker", user.getNama());
        var apoteker = new ApotekerModel();
        model.addAttribute(aptStr, apoteker);
        return formAddApoteker;
    }

    @PostMapping(value = "/add-apoteker")
    public String addApotekerSubmit(@ModelAttribute ApotekerDto apotekerDto, Model model){
        var apoteker = new ApotekerModel();
        apoteker.setEmail(apotekerDto.getEmail());
        apoteker.setNama(apotekerDto.getNama());
        apoteker.setPassword(apotekerDto.getPassword());
        apoteker.setUsername(apotekerDto.getUsername());
        apoteker.setRole("Apoteker");
        UserModel userByUsername = userService.getUserByUsername(apoteker.getUsername());
        UserModel userByEmail = userService.getUserByEmail(apoteker.getEmail());

        if (userByUsername != null && userByEmail != null) {
            model.addAttribute(aptStr, apoteker);
            model.addAttribute(duplicateUsn, msg + userByUsername.getUsername() + msg2);
            model.addAttribute(duplicateEmail, msg3 + userByEmail.getEmail() + msg2);
            return formAddApoteker;
        } else if (userByUsername != null) {
            model.addAttribute(aptStr, apoteker);
            model.addAttribute(duplicateUsn, msg + userByUsername.getUsername() + msg2);
            return formAddApoteker;
        } else if (userByEmail != null) {
            model.addAttribute(aptStr, apoteker);
            model.addAttribute(duplicateEmail, msg3 + userByEmail.getEmail() + msg2);
            return formAddApoteker;
        }

        apotekerService.addApoteker(apoteker);
        model.addAttribute(aptStr, apoteker);
        return "add-apoteker";
    }

    @GetMapping("/viewallapoteker")
    public String listApoteker(Model model, Principal principal) {
        UserModel user = userService.getUserByUsername(principal.getName());

        logger.info("Admin {} mengakses halaman list apoteker", user.getNama());
        List<ApotekerModel> listApoteker = apotekerService.getlistApoteker();
        model.addAttribute("listApoteker", listApoteker);
        return "viewallapoteker";
    }

    @GetMapping("/viewalldokter")
    public String listDokter(Model model, Principal principal) {
        UserModel user = userService.getUserByUsername(principal.getName());

        logger.info("Admin {} mengakses halaman list dokter", user.getNama());
        List<DokterModel> listDokter = dokterService.getlistDokter();
        model.addAttribute("listDokter", listDokter);
        return "viewalldokter";
    }

    @GetMapping("/viewallpasien")
    public String listPasien(Model model, Principal principal) {
        UserModel user = userService.getUserByUsername(principal.getName());

        logger.info("Admin {} mengakses halaman list pasien", user.getNama());
        List<PasienModel> listPasien = pasienService.getlistPasien();
        model.addAttribute("listPasien", listPasien);
        return "viewallpasien";
    }

}
