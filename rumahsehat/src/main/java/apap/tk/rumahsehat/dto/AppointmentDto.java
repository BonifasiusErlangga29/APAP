package apap.tk.rumahsehat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentDto {

    @JsonProperty("kode")
    private String kode;

    @JsonProperty("waktu_awal")
    private LocalDateTime waktuAwal;

    @JsonProperty("is_done")
    private boolean isDone;

    @JsonProperty("nama_dokter")
    private String namaDokter;

    @JsonProperty("username_dokter")
    private String usernameDokter;
    
    @JsonProperty("username_pasien")
    private String usernamePasien;

    @JsonProperty("resep")
    private ResepDto resep;
}
