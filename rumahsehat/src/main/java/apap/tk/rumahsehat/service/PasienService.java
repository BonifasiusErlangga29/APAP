package apap.tk.rumahsehat.service;

import apap.tk.rumahsehat.model.PasienModel;

import java.util.List;

public interface PasienService {

    PasienModel addPasien (PasienModel pasienModel);
    PasienModel updatePasien (PasienModel pasienModel);

    List<PasienModel> getlistPasien();

    PasienModel topUpSaldo (PasienModel pasien, int saldoBaru);

}
