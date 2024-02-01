package apap.tk.rumahsehat.service;
import apap.tk.rumahsehat.model.JumlahModel;
import apap.tk.rumahsehat.repository.JumlahDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JumlahServiceImpl implements JumlahService{
    @Autowired
    JumlahDb jumlahDb;

    @Override
    public JumlahModel addJumlah(JumlahModel jumlah){

        return jumlahDb.save(jumlah);
    }


}
