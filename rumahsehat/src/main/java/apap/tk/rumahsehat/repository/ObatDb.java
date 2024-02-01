package apap.tk.rumahsehat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import apap.tk.rumahsehat.model.ObatModel;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ObatDb extends JpaRepository<ObatModel, String> {
    @Query("select c from ObatModel c order by c.nama")
    public List<ObatModel> getAllData();
}
