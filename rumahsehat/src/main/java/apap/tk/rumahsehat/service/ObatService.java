package apap.tk.rumahsehat.service;

import apap.tk.rumahsehat.model.ObatModel;

import java.util.List;

public interface ObatService {

    // Fitur 13 [Web] Melihat Daftar Obat (Admin, Apoteker)
    List<ObatModel> list();

    /**
     * Melakukan update pada obat, dengan id obat tertentu
     * @param idObat id obat
     * @param obatModel obat
     * @return obat yang sudah di-update
     */
    ObatModel update(String idObat, ObatModel obatModel);

    ObatModel updateObat(ObatModel obat);
    ObatModel findById(String id);
}
