package apap.tk.rumahsehat.repository;
import apap.tk.rumahsehat.model.PasienModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasienDb extends JpaRepository<PasienModel, String>{
    PasienModel findByUsername(String username);
}
