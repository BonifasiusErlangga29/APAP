package apap.tk.rumahsehat.repository;

import apap.tk.rumahsehat.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDb extends JpaRepository<UserModel, String> {
    UserModel findByUsername(String username);
    UserModel findByEmail(String email);
}
