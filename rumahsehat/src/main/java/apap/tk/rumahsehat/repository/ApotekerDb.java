package apap.tk.rumahsehat.repository;

import apap.tk.rumahsehat.model.ApotekerModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApotekerDb extends JpaRepository<ApotekerModel, String>{
}
