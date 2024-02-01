package apap.tk.rumahsehat.service;
import apap.tk.rumahsehat.model.PasienModel;
import apap.tk.rumahsehat.repository.PasienDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasienServiceImpl implements PasienService{
    @Autowired
    PasienDb pasienDb;

    @Autowired
    UserService userService;

    @Override
    public PasienModel addPasien(PasienModel pasien){
        String pass = userService.encrypt(pasien.getPassword());
        pasien.setPassword(pass);
        return pasienDb.save(pasien);
    }

    @Override
    public PasienModel updatePasien(PasienModel pasien){
        return pasienDb.save(pasien);
    }

    @Override
    public List<PasienModel> getlistPasien(){
        return pasienDb.findAll();
    }

    @Override
    public PasienModel topUpSaldo (PasienModel pasien, int saldoBaru){
        pasien.setSaldo(saldoBaru);
        return pasienDb.save(pasien);
    }

}
