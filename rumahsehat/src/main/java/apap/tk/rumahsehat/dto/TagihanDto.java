package apap.tk.rumahsehat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TagihanDto {

    @JsonProperty("kode")
    private String kode;

    @JsonProperty("tanggal_terbuat")
    private LocalDateTime tanggalTerbuat;

    @JsonProperty("tanggal_terbayar")
    private LocalDateTime tanggalBayar;

    @JsonProperty("is_paid")
    private boolean isPaid;

    @JsonProperty("jumlah_tagihan")
    private int jumlahTagihan;

    @JsonProperty("kode_appointment")
    private String kodeAppointment;
    
    @JsonProperty("username_pasien")
    private String usernamePasien;

    @JsonProperty("umur_pasien")
    private int umurPasien;
}
