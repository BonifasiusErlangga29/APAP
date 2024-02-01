package apap.tk.rumahsehat.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import apap.tk.rumahsehat.model.ChartHelperModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface ChartHelperDb extends JpaRepository<ChartHelperModel, Long> {
    Optional<ChartHelperModel> findById(Long id);
    Optional<ChartHelperModel> findByTanggal(LocalDate tanggal);

    @Query(value = "SELECT * FROM helper_tagihan ORDER BY tanggal;", nativeQuery = true)
    List<ChartHelperModel> findAll();

}

