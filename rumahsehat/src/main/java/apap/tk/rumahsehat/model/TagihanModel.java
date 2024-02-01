package apap.tk.rumahsehat.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;

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
@Table(name="tagihan")
public class TagihanModel implements Serializable {
    @Id
    @GeneratedValue(generator = "bill-generator")
    @GenericGenerator(name = "bill-generator", 
      parameters = @Parameter(name = "prefix", value = "BILL"), 
      strategy = "apap.tk.rumahsehat.generator.BillGenerator")
    private String kode;  

    @NotNull
    @Column(name = "tanggal_terbuat", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime tanggalTerbuat;
    
    @NotNull
    @Column(name = "tanggal_terbayar", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime tanggalBayar;

    @NotNull
    @Column(name = "is_paid")
    private boolean isPaid;

    @NotNull
    @Column(name = "jumlah_tagihan")
    private int jumlahTagihan;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name="kode_appointment")
    public AppointmentModel appointment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_umur", referencedColumnName = "id_umur")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UmurTagihanModel umur;

}


