package apap.tk.rumahsehat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ObatDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("nama")
    private String nama;

    @JsonProperty("harga")
    private int harga;

    @JsonProperty("stok")
    private int stok;

}
