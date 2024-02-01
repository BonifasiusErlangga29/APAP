package apap.tk.rumahsehat.service;

import apap.tk.rumahsehat.model.DokterModel;

import java.util.List;

public interface DokterService {
    DokterModel addDokter(DokterModel dokter);
    List<DokterModel> getlistDokter();
}
