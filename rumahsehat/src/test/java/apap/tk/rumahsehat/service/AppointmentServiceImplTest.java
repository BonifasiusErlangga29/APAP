package apap.tk.rumahsehat.service;

import apap.tk.rumahsehat.model.*;
import apap.tk.rumahsehat.repository.AppointmentDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AppointmentServiceImplTest {

    @Mock
    private AppointmentDb appointmentDb;

    @Mock
    private UserService userService;

    @InjectMocks
    private AppointmentService appointmentService = new AppointmentServiceImpl();

    @BeforeEach
    public void setUp() {
    }

    @Test
    void listAll() {
        List<AppointmentModel> appointmentModels = new ArrayList<>();

        AppointmentModel appointmentModel1 = new AppointmentModel(
                "APT-01", LocalDateTime.now(), false, new PasienModel(), new DokterModel(),
                new TagihanModel(), new ResepModel()
        );

        AppointmentModel appointmentModel2 = new AppointmentModel(
                "APT-02", LocalDateTime.now(), true, new PasienModel(), new DokterModel(),
                new TagihanModel(), new ResepModel()
        );

        appointmentModels.add(appointmentModel1);
        appointmentModels.add(appointmentModel2);

        Mockito.when(appointmentDb.findAll())
                .thenReturn(appointmentModels);

        List<AppointmentModel> aptFromService = appointmentService.listAll();
        assertEquals(2, aptFromService.size());
        for (int i = 0; i < appointmentModels.size(); i++) {
            assertEquals(appointmentModels.get(0).getKode(), aptFromService.get(0).getKode());
        }
    }

    @Test
    void listByDokter() {
        DokterModel dokter = new DokterModel();
        dokter.setNama("dr");

        AppointmentModel appointmentModel1 = new AppointmentModel(
                "APT-01", LocalDateTime.now(), false, new PasienModel(), dokter,
                new TagihanModel(), new ResepModel()
        );

        Mockito.when(appointmentDb.findAllByDokter(dokter))
                .thenReturn(Collections.singletonList(appointmentModel1));

        List<AppointmentModel> aptFromService = appointmentService.listByDokter(dokter);
        assertEquals(1, aptFromService.size());
        assertEquals(appointmentModel1.getKode(), aptFromService.get(0).getKode());
        assertEquals(dokter.getNama(), aptFromService.get(0).getDokter().getNama());
    }

    @Test
    void listByPasien() {
        PasienModel pasien = new PasienModel();
        pasien.setNama("pasien");

        AppointmentModel appointmentModel1 = new AppointmentModel(
                "APT-01", LocalDateTime.now(), false, pasien, new DokterModel(),
                new TagihanModel(), new ResepModel()
        );

        Mockito.when(appointmentDb.findAllByPasien(pasien))
                .thenReturn(Collections.singletonList(appointmentModel1));

        List<AppointmentModel> aptFromService = appointmentService.listByPasien(pasien);
        assertEquals(1, aptFromService.size());
        assertEquals(appointmentModel1.getKode(), aptFromService.get(0).getKode());
        assertEquals(pasien.getNama(), aptFromService.get(0).getPasien().getNama());
    }

}
