package apap.tk.rumahsehat.restcontroller;

import apap.tk.rumahsehat.model.ApotekerModel;
import apap.tk.rumahsehat.model.ObatModel;
import apap.tk.rumahsehat.service.ObatService;
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

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@ExtendWith(SpringExtension.class)
@WebMvcTest(ObatRestController.class)
class ObatRestControllerTest {

    @MockBean
    ObatService obatService;

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {}

    @Test
    @WithMockUser(username = "uchi", roles = "Apoteker")
    void listObat() throws Exception {
        ObatModel obatModel1 = new ObatModel(
                "123", "Panadol", 10000, 100, Collections.emptyList()
        );
        List<ObatModel> obats = List.of(obatModel1);

        ApotekerModel apotekerModel = new ApotekerModel();
        apotekerModel.setNama("uchi");

        Mockito.when(obatService.list()).thenReturn(obats);
        Mockito.when(userService.getUserByUsername(any()))
                .thenReturn(apotekerModel);

        mockMvc.perform(get("/api/v1/obat/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is("123")));
    }

    @Test
    @WithMockUser(username = "uchi", roles = "Apoteker")
    void update() throws Exception {
        ObatModel obatModel2 = new ObatModel(
                "123", "Antangin", 15000, 100, Collections.emptyList()
        );

        ApotekerModel apotekerModel = new ApotekerModel();
        apotekerModel.setNama("uchi");

        Mockito.when(userService.getUserByUsername(any()))
                .thenReturn(apotekerModel);
        Mockito.when(obatService.update(eq("123"), any()))
                .thenReturn(obatModel2);

        mockMvc.perform(put("/api/v1/obat/update/{idObat}", "123")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(obatModel2))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is("123")))
                .andExpect(jsonPath("$.nama", Matchers.is("Antangin")))
                .andExpect(jsonPath("$.harga", Matchers.is(15000)));
    }

}
