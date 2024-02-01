package apap.tk.rumahsehat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "jumlah")
public class JumlahModel implements Serializable {
    @EmbeddedId
    JumlahKey id;

    @ManyToOne
    @MapsId("idObat")
    @JoinColumn(name = "obat")
    ObatModel obat;
    
    @JsonIgnore
    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "resep")
    ResepModel resep;

    @NotNull
    @Column(name = "kuantitas", nullable = false)
    private int kuantitas;
}

