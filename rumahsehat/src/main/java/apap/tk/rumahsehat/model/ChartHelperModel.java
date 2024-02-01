package apap.tk.rumahsehat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "helper_tagihan")
public class ChartHelperModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "tanggal")
    private LocalDate tanggal;

    @Column(name = "range_1")
    private Integer range1 = 0;

    @Column(name = "range_2")
    private Integer range2 = 0;

    @Column(name = "range_3")
    private Integer range3 = 0;

    @Column(name = "range_4")
    private Integer range4 = 0;

    @Column(name = "range_5")
    private Integer range5 = 0;

}