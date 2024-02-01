package apap.tk.rumahsehat.repository;

import apap.tk.rumahsehat.model.UmurTagihanModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UmurTagihanDb extends JpaRepository<UmurTagihanModel, String> {

    @Query("SELECT c FROM UmurTagihanModel c WHERE c.id = :id")
    Optional<UmurTagihanModel> findById(@Param("id") int id);

    @Query("SELECT id FROM UmurTagihanModel")
    List<Integer> findAllId();
}
