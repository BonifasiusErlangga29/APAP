package apap.tk.rumahsehat.service;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.tk.rumahsehat.model.PasienModel;
import apap.tk.rumahsehat.model.TagihanModel;
import apap.tk.rumahsehat.model.UmurTagihanModel;
import apap.tk.rumahsehat.repository.TagihanDb;
import javax.transaction.Transactional;

@Service
@Transactional
public class TagihanServiceImpl implements TagihanService{

    @Autowired
    TagihanDb tagihanDb;

    @Autowired
    UmurTagihanService umurTagihanService;

    @Override
    public void addTagihan(TagihanModel tagihan) {
        tagihanDb.save(tagihan);
    }

    @Override
    public TagihanModel updateTagihan(TagihanModel tagihan) {
        tagihanDb.save(tagihan);
        return tagihan;
    }


    @Override
    public TagihanModel getTagihanByKode(String kode) {
        Optional<TagihanModel> tagihan = tagihanDb.findByKode(kode);
        if (tagihan.isPresent()) {
            return tagihan.get();
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public List<TagihanModel> getListTagihan() {
        return tagihanDb.findAll();
    }

    @Override
    public List<TagihanModel> listByPasien(PasienModel pasienModel) {
        return tagihanDb.findAllByPasien(pasienModel);
    }

    @Override
    public List<String> setIdNamaUmur(int umurPasien) {
        ArrayList<String> result = new ArrayList<>();
        var idUmur = "0";
        var namaUmur = "";
        if (umurPasien >= 0 && umurPasien <= 15) {
            idUmur = "1";
            namaUmur = "[0 - 15]";
        } else if (umurPasien >= 16 && umurPasien <= 30) {
            idUmur = "2";
            namaUmur = "[16 - 30]";
        } else if (umurPasien >= 31 && umurPasien <= 45) {
            idUmur = "3";
            namaUmur = "[31 - 45]";
        } else if (umurPasien >= 45 && umurPasien <= 60) {
            idUmur = "4";
            namaUmur = "[46 - 60]";
        } else if (umurPasien >= 61) {
            idUmur = "5";
            namaUmur = ">60";
        }
        result.add(idUmur);
        result.add(namaUmur);
        return result;

    }

    public void addTagihanToChart(TagihanModel tagihan, int totalTagihan) {
        var idUmur = 0;
            var namaUmur = "";
            int umurPasien = tagihan.getAppointment().getPasien().getUmur();
            idUmur = Integer.parseInt(setIdNamaUmur(umurPasien).get(0));
            namaUmur = setIdNamaUmur(umurPasien).get(1);

            if (umurTagihanService.listAllIdUmurTagihan().contains(idUmur)) {
                var umurTagihan = umurTagihanService.getUmurTagihanById(idUmur);
                int kuantitasTagihan = umurTagihan.getKuantitasTagihan() + 1;
                int totalTagihanTerbayar = umurTagihan.getTotalTagihanTerbayar() + totalTagihan;
                umurTagihan.getTagihan().add(tagihan);
                umurTagihan.setKuantitasTagihan(kuantitasTagihan);
                umurTagihan.setTotalTagihanTerbayar(totalTagihanTerbayar);
                umurTagihanService.updateUmurTagihan(umurTagihan);
            } else {
                var umurTagihanNew = new UmurTagihanModel();
                umurTagihanNew.setId(idUmur);
                umurTagihanNew.setNama(namaUmur);
                umurTagihanNew.setKuantitasTagihan(1);
                umurTagihanNew.setTotalTagihanTerbayar(totalTagihan);
                List<TagihanModel> newList = new ArrayList<>();
                umurTagihanNew.setTagihan(newList);
                umurTagihanNew.getTagihan().add(tagihan);
                umurTagihanService.addUmurTagihan(umurTagihanNew);
            }
    }


}
