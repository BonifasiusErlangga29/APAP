package apap.tk.rumahsehat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="umur_tagihan")
public class UmurTagihanModel implements Serializable {
    @Id
    @Column(name = "id_umur", nullable = false)
    private int id;

    @NotNull
    @Size(max = 30)
    @Column(name = "nama_umur", nullable = false)
    private String nama;

    @NotNull
    @Column(name = "kuantitas_tagihan", nullable = false)
    private int kuantitasTagihan;

    @NotNull
    @Column(name = "total_terbayar_tagihan", nullable = false)
    private int totalTagihanTerbayar;

    @OneToMany(mappedBy = "umur", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TagihanModel> tagihan;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_bar_chart_tagihan", referencedColumnName = "idBarChartTagihan", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BarChartTagihanModel chartTagihan;
}
