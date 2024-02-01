package apap.tk.rumahsehat.service;

import apap.tk.rumahsehat.model.TagihanModel;

import java.util.List;

import apap.tk.rumahsehat.model.PasienModel;

public interface TagihanService {
    void addTagihan(TagihanModel tagihan);
    TagihanModel updateTagihan(TagihanModel tagihan);

    List<TagihanModel> getListTagihan();
    List<TagihanModel> listByPasien(PasienModel pasienModel);
    TagihanModel getTagihanByKode(String kode);
    List<String> setIdNamaUmur(int umurPasien);
    void addTagihanToChart(TagihanModel tagihan, int totalTagihan);
}
