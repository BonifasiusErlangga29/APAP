package apap.tk.rumahsehat.repository;

import apap.tk.rumahsehat.model.BarChartTagihanModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarChartTagihanDb extends JpaRepository<BarChartTagihanModel, String> {
}
