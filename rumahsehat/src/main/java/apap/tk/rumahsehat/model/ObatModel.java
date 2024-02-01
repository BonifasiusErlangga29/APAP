package apap.tk.rumahsehat.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="obat")
public class ObatModel implements Serializable {
    @Id
    @Size(max = 50)
    @Column(name = "id_obat", nullable = false)
    private String idObat;

    @NotNull
    @Column(name = "nama_obat", nullable = false)
    private String nama;

    @NotNull
    @Column(name = "harga", nullable = false)
    private int harga;

    @NotNull
    @Column(name = "stok", nullable = false, columnDefinition = "integer default 100")
    private Integer stok;

    @JsonIgnore
    @OneToMany(mappedBy = "obat")
    private List<JumlahModel> jumlah;
}
