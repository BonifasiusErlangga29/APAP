package apap.tk.rumahsehat.restcontroller;

import apap.tk.rumahsehat.config.JwtTokenUtil;
import apap.tk.rumahsehat.model.PasienModel;
import apap.tk.rumahsehat.service.PasienService;
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

@ExtendWith(SpringExtension.class)
@WebMvcTest(PasienRestController.class)
class PasienRestControllerTest {

    @MockBean
    UserService userService;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    PasienService pasienService;

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
    @WithMockUser(username = "uchi", roles = "Pasien")
    void getLoggedInPasien() throws Exception {
        PasienModel pasienModel = new PasienModel();
        pasienModel.setNama("uchi");
        pasienModel.setUsername("uchi");

        Mockito.when(userService.getUserByUsername(any()))
                .thenReturn(pasienModel);

        mockMvc.perform(get("/api/v1/pasien/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nama", Matchers.is("uchi")))
                .andExpect(jsonPath("$.username", Matchers.is("uchi")));
    }

}
