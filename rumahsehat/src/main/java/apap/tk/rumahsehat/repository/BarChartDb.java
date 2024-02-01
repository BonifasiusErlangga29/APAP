package apap.tk.rumahsehat.repository;

import apap.tk.rumahsehat.model.BarChartModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarChartDb extends JpaRepository<BarChartModel, Long> {
}

