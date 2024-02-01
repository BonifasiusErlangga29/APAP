package apap.tk.rumahsehat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import apap.tk.rumahsehat.model.JumlahModel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class ResepDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("is_done")
    private boolean isDone;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("username_apoteker")
    private String usernameApoteker;
    
    @JsonProperty("nama_apoteker")
    private String namaApoteker;

    @JsonProperty("nama_pasien")
    private String namaPasien;

    @JsonProperty("list_obat")
    private Map<String, Integer> listObat;

    @JsonProperty("jumlah")
    private List<JumlahModel> jumlah;
}
