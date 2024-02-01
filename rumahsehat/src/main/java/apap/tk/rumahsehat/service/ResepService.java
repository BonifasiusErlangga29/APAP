package apap.tk.rumahsehat.service;

import apap.tk.rumahsehat.model.ApotekerModel;
import apap.tk.rumahsehat.model.ResepModel;
import java.util.List;

public interface ResepService {
    List<ResepModel> getResepModel();
    void addResep(ResepModel resep);
    ResepModel getResepByIdResep(Long id);
    ResepModel updateStatusResep(ResepModel resep, ApotekerModel apoteker);

}
