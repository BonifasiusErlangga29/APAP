package apap.tk.rumahsehat.service;
import apap.tk.rumahsehat.model.DokterModel;
import apap.tk.rumahsehat.repository.DokterDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DokterServiceImpl implements DokterService{
    @Autowired
    DokterDb dokterDb;

    @Autowired
    UserService userService;

    @Override
    public DokterModel addDokter(DokterModel dokter){
        String pass = userService.encrypt(dokter.getPassword());
        dokter.setPassword(pass);
        return dokterDb.save(dokter);
    }

    @Override
    public List<DokterModel> getlistDokter(){
        return dokterDb.findAll();
    }


}
