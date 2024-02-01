package apap.tk.rumahsehat.service;

import apap.tk.rumahsehat.model.UmurTagihanModel;
import apap.tk.rumahsehat.repository.UmurTagihanDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UmurTagihanServiceImpl implements UmurTagihanService{

    @Autowired
    UmurTagihanDb umurTagihanDb;

    @Override
    public void addUmurTagihan(UmurTagihanModel umurTagihan) {
        umurTagihanDb.save(umurTagihan);
    }

    @Override
    public UmurTagihanModel getUmurTagihanById(int id) {
        Optional<UmurTagihanModel> umurTagihan = umurTagihanDb.findById(id);
        if (umurTagihan.isPresent()) {
            return umurTagihan.get();
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public List<Integer> listAllIdUmurTagihan() {
        return umurTagihanDb.findAllId();
    }

    @Override
    public List<UmurTagihanModel> getListUmurTagihan() {
        return umurTagihanDb.findAll();
    }

    @Override
    public UmurTagihanModel updateUmurTagihan(UmurTagihanModel umurTagihan) {
        umurTagihanDb.save(umurTagihan);
        return umurTagihan;
    }

    @Override
    public List<UmurTagihanModel> list() {
        return umurTagihanDb.findAll();
    }
}
