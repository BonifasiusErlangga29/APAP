package apap.tk.rumahsehat.service;

import apap.tk.rumahsehat.model.ObatModel;
import apap.tk.rumahsehat.repository.ObatDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ObatServiceImpl implements ObatService {
    @Autowired
    ObatDb obatDb;

    @Override
    public List<ObatModel> list() {
        return obatDb.findAll();
    }

    @Override
    public ObatModel update(String idObat, ObatModel obatModel) {
        ObatModel obatDariDb = obatDb.getReferenceById(idObat);
        obatDariDb.setHarga(obatModel.getHarga());
        obatDariDb.setNama(obatModel.getNama());
        obatDariDb.setStok(obatModel.getStok());
        return obatDb.save(obatDariDb);
    }

    @Override
    public ObatModel updateObat(ObatModel obat) {
        obatDb.save(obat);
        return obat;
    }

    @Override
    public ObatModel findById(String id) {
        Optional<ObatModel> obat = obatDb.findById(id);
        if (obat.isPresent()) {
            return obat.get();
        } else {
            return null;
        }
    }
}
