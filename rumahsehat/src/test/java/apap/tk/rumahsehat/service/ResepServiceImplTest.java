package apap.tk.rumahsehat.service;

import apap.tk.rumahsehat.model.*;
import apap.tk.rumahsehat.repository.ResepDb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ResepServiceImplTest {

    @Mock
    private ResepDb resepDb;

    @Mock
    private UserService userService;

    @InjectMocks
    private ResepService resepService = new ResepServiceImpl();

    @BeforeEach
    public void setUp() {
    }

    @Test
    void getResepModel() {
        List<ResepModel> resepModels = new ArrayList<>();

        List<JumlahModel> jumlah = new ArrayList<>();
        jumlah.add(new JumlahModel());
        ResepModel resepModel1 = new ResepModel(Long.parseLong("1"), false, LocalDateTime.now(), new ApotekerModel(), jumlah, new AppointmentModel());
        ResepModel resepModel2 = new ResepModel(Long.parseLong("2"), true, LocalDateTime.now(), new ApotekerModel(), jumlah, new AppointmentModel());

        resepModels.add(resepModel1);
        resepModels.add(resepModel2);

        Mockito.when(resepDb.findAll())
                .thenReturn(resepModels);

        List<ResepModel> resepFromService = resepService.getResepModel();
        assertEquals(2, resepFromService.size());
        for (int i = 0; i < resepModels.size(); i++) {
            assertEquals(resepModels.get(0).getId(), resepFromService.get(0).getId());
        }
    }
}
