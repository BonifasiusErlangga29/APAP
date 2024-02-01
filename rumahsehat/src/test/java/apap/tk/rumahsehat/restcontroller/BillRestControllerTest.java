package apap.tk.rumahsehat.restcontroller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import apap.tk.rumahsehat.config.JwtTokenUtil;
import apap.tk.rumahsehat.model.AppointmentModel;
import apap.tk.rumahsehat.model.PasienModel;
import apap.tk.rumahsehat.model.TagihanModel;
import apap.tk.rumahsehat.service.PasienService;
import apap.tk.rumahsehat.service.TagihanService;
import apap.tk.rumahsehat.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BillRestController.class)
class BillRestControllerTest {

    @MockBean
    UserService userService;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    PasienService pasienService;

    @MockBean
    TagihanService tagihanService;

    @MockBean
    JwtTokenUtil jwtTokenUtil;

    @MockBean
    PasswordEncoder encoder;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {}

    @Test
    @WithMockUser(username = "al", roles = "Pasien")
    void getTagihan() throws Exception {
        PasienModel pasienModel = new PasienModel();
        pasienModel.setNama("al");
        pasienModel.setUsername("al");

        AppointmentModel appointment = new AppointmentModel();
        appointment.setPasien(pasienModel);
        TagihanModel tagihanModel = new TagihanModel("BILL-123", LocalDateTime.now(), LocalDateTime.now(), false, 100000, appointment, null);

        Mockito.when(userService.getUserByUsername(any()))
                .thenReturn(pasienModel);
        Mockito.when(tagihanService.getTagihanByKode(any()))
                .thenReturn(tagihanModel);

        mockMvc.perform(get("/api/v1/bill/{kode}", "BILL-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kode", Matchers.is("BILL-123")))
                ;
    }

    @Test
    @WithMockUser(username = "al", roles = "Pasien")
    void bayar() throws Exception {
        PasienModel pasienModel = new PasienModel();
        pasienModel.setNama("al");
        pasienModel.setUsername("al");
        pasienModel.setSaldo(100000);

        AppointmentModel appointment = new AppointmentModel();
        appointment.setPasien(pasienModel);
        TagihanModel tagihanModel = new TagihanModel("BILL-123", LocalDateTime.now(), LocalDateTime.now(), false, 100000, appointment, null);
        TagihanModel tagihanModel2 = new TagihanModel("BILL-456", LocalDateTime.now(), LocalDateTime.now(), false, 100000, appointment, null);
        

        Mockito.when(userService.getUserByUsername(any()))
                .thenReturn(pasienModel);
        Mockito.when(tagihanService.updateTagihan(tagihanModel))
                .thenReturn(tagihanModel);
        Mockito.when(tagihanService.getTagihanByKode(any()))
                .thenReturn(tagihanModel);
                
        mockMvc.perform(post("/api/v1/bill/bayar/{kode}", "BILL-123")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(tagihanModel).replace("jumlahTagihan", "jumlah_tagihan"))
                .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.kode", Matchers.is("BILL-123")));

        Mockito.when(tagihanService.getTagihanByKode(any()))
                .thenReturn(tagihanModel2);
        mockMvc.perform(post("/api/v1/bill/bayar/{kode}", "BILL-456")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(tagihanModel2).replace("jumlahTagihan", "jumlah_tagihan"))
                .with(csrf()))
        .andExpect(status().isBadRequest());
    }

}
