package apap.tk.rumahsehat.repository;

import apap.tk.rumahsehat.model.AppointmentModel;

import java.util.Optional;

import apap.tk.rumahsehat.model.DokterModel;
import apap.tk.rumahsehat.model.PasienModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentDb extends JpaRepository<AppointmentModel, String> {
    Optional<AppointmentModel> findByKode(String kode);

    List<AppointmentModel> findAllByDokter(DokterModel dokterModel);

    List<AppointmentModel> findAllByPasien(PasienModel pasienModel);

}
