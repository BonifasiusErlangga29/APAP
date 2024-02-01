package apap.tk.rumahsehat.service;

import apap.tk.rumahsehat.model.*;
import apap.tk.rumahsehat.repository.ObatDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ObatServiceImplTest {

    @Mock
    private ObatDb obatDb;

    @InjectMocks
    private ObatService obatService = new ObatServiceImpl();

    @BeforeEach
    public void setUp() {
    }

    @Test
    void list() {
        List<ObatModel> obatModels = new ArrayList<>();

        ObatModel obatModel1 = new ObatModel(
                "123", "Panadol", 10000, 100, Collections.emptyList()
        );

        ObatModel obatModel2 = new ObatModel(
                "234", "Sangobion", 10000, 100, Collections.emptyList()
        );

        obatModels.add(obatModel1);
        obatModels.add(obatModel2);

        Mockito.when(obatDb.findAll())
                .thenReturn(obatModels);

        List<ObatModel> obatFromService = obatService.list();
        assertEquals(2, obatFromService.size());
        for (int i = 0; i < obatFromService.size(); i++) {
            assertEquals(obatModels.get(0).getNama(), obatFromService.get(0).getNama());
        }
    }

    @Test
    void update() {
        ObatModel obatBefore = new ObatModel(
                "123", "Panadol", 10000, 100, Collections.emptyList()
        );

        ObatModel obatAfter = new ObatModel(
                "123", "Sangobion", 10000, 100, Collections.emptyList()
        );
        Mockito.when(obatDb.getReferenceById("123"))
                .thenReturn(obatBefore);

        Mockito.when(obatDb.save(obatBefore))
                .thenReturn(obatAfter);

        ObatModel obatFromService = obatService.update("123", obatBefore);
        assertEquals(obatFromService.getNama(), obatAfter.getNama());
    }

}
