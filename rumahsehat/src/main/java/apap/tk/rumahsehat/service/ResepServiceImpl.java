package apap.tk.rumahsehat.service;

import apap.tk.rumahsehat.model.ApotekerModel;
import apap.tk.rumahsehat.model.ResepModel;
import apap.tk.rumahsehat.repository.ResepDb;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Transactional
public class ResepServiceImpl implements ResepService {
    @Autowired
    ResepDb resepDb;

    @Override
    public List<ResepModel> getResepModel() {
        return resepDb.findAll();
    }
    @Override
    public void addResep(ResepModel resep) {
        resepDb.save(resep);
    }

    @Override
    public ResepModel getResepByIdResep(Long id) {
        Optional<ResepModel> resep = resepDb.findById(id);
        if (resep.isPresent()) {
            return resep.get();
        }
        return null;
    }

    @Override
    public ResepModel updateStatusResep(ResepModel resep, ApotekerModel apoteker) {
        resep.setApoteker(apoteker);
        resep.setDone(true);
        return resep;
    }
}
