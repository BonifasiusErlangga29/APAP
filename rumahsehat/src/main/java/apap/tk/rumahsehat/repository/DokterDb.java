package apap.tk.rumahsehat.repository;

import apap.tk.rumahsehat.model.DokterModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DokterDb extends JpaRepository<DokterModel, String>{
}
