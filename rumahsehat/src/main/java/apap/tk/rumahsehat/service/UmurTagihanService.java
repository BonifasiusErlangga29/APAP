package apap.tk.rumahsehat.service;
import apap.tk.rumahsehat.model.UmurTagihanModel;

import java.util.List;

public interface UmurTagihanService {
    void addUmurTagihan(UmurTagihanModel umurTagihan);
    UmurTagihanModel getUmurTagihanById(int id);
    List<Integer> listAllIdUmurTagihan();
    List<UmurTagihanModel> getListUmurTagihan();
    UmurTagihanModel updateUmurTagihan(UmurTagihanModel umurTagihan);
    List<UmurTagihanModel> list();
}
