package apap.tk.rumahsehat.restcontroller;

import apap.tk.rumahsehat.model.*;
import apap.tk.rumahsehat.service.AppointmentService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AppointmentRestController.class)
class AppointmentRestControllerTest {

    @MockBean
    AppointmentService appointmentService;

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {}

    @Test
    @WithMockUser(username = "uchi", roles = "Pasien")
    void listAppointment() throws Exception {
        PasienModel pasienModel = new PasienModel();
        pasienModel.setNama("uchi");
        pasienModel.setUsername("uchi");
        AppointmentModel appointmentModel1 = new AppointmentModel(
                "APT-01", LocalDateTime.now(), false, pasienModel, new DokterModel(),
                new TagihanModel(), null
        );

        Mockito.when(userService.getUserByUsername(any()))
                .thenReturn(pasienModel);
        Mockito.when(appointmentService.listByPasien(pasienModel))
                .thenReturn(Collections.singletonList(appointmentModel1));

        mockMvc.perform(get("/api/v1/list-appointment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].kode", Matchers.is("APT-01")))
                .andExpect(jsonPath("$[0].username_pasien", Matchers.is("uchi")));
    }
}
