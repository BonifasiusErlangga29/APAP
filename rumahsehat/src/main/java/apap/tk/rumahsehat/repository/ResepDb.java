package apap.tk.rumahsehat.repository;

import apap.tk.rumahsehat.model.ResepModel;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResepDb extends JpaRepository<ResepModel, String> {

    Optional<ResepModel> findById(Long id);
}