package apap.tk.rumahsehat.restcontroller;

import apap.tk.rumahsehat.config.JwtTokenUtil;
import apap.tk.rumahsehat.dto.PasienDto;
import apap.tk.rumahsehat.model.*;
import apap.tk.rumahsehat.service.PasienService;
import apap.tk.rumahsehat.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pasien")
public class PasienRestController {

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasienService pasienService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    PasswordEncoder encoder;

    Logger logger = LoggerFactory.getLogger(PasienRestController.class);
    
    @GetMapping(value = "/")
    public PasienDto getLoggedInPasien(Principal principal) {
        var pasienModel = (PasienModel) userService.getUserByUsername(principal.getName());
        logger.info("Pasien yang sedang login: {}", pasienModel.getNama());
        var dto = new PasienDto();
        dto.setNama(pasienModel.getNama());
        dto.setUsername(pasienModel.getUsername());
        dto.setEmail(pasienModel.getEmail());
        dto.setSaldo(pasienModel.getSaldo());
        dto.setUmur(pasienModel.getUmur());
        return dto;
    }

    @PostMapping(value="/add")
    public PasienModel createPasien(@Valid @RequestBody PasienDto pasien, BindingResult bindingResult) {
        var pasienBaru = new PasienModel();
        pasienBaru.setEmail(pasien.getEmail());
        pasienBaru.setNama(pasien.getNama());
        pasienBaru.setPassword(pasien.getPassword());
        pasienBaru.setRole("Pasien");
        pasienBaru.setSaldo(0);
        pasienBaru.setUmur(pasien.getUmur());
        pasienBaru.setUsername(pasien.getUsername());

        if (bindingResult.hasFieldErrors()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Request body has invalid type or missing field."
            );
        } else {
            return pasienService.addPasien(pasienBaru);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> userLogin(@Valid @RequestBody JwtRequest user) {
        try {
            var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenUtil.generateJwtToken();
            List<String> roles = new ArrayList<>();
            roles.add("Pasien");
            var authResponse = new AuthResponse();
            authResponse.setToken(token);
            authResponse.setRoles(roles);

            PasienModel pasien = (PasienModel)userService.getUserByUsername(user.getUsername());
            authResponse.setPasien(pasien);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Tidak ada akun yang sesuai"
            );        
        }
    }

    @PostMapping(value="/topUpSaldo/{username}")
    public PasienModel topUpSaldo(@PathVariable String username, @RequestBody PasienDto pasienDto, Principal principal) {
        PasienModel pasien = (PasienModel) userService.getUserByUsername(principal.getName());
        logger.info("Pasien {} melakukan top up saldo dengan username {}", pasien.getNama(), username);
        return pasienService.topUpSaldo(pasien, pasienDto.getSaldo());
    }
}
