package apap.tk.rumahsehat.repository;

import apap.tk.rumahsehat.model.TagihanModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import apap.tk.rumahsehat.model.PasienModel;

@Repository
public interface TagihanDb extends JpaRepository<TagihanModel, String> {
    @Query("SELECT c FROM TagihanModel c WHERE c.appointment IN (SELECT d from AppointmentModel d WHERE d.pasien = :pasienModel)")
    List<TagihanModel> findAllByPasien(@Param("pasienModel") PasienModel pasienModel);

    Optional<TagihanModel> findByKode(String kode);

}
