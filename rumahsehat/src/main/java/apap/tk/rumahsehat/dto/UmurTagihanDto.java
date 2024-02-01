package apap.tk.rumahsehat.dto;

import apap.tk.rumahsehat.model.TagihanModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UmurTagihanDto {

    @JsonProperty("id")
    private int id;

    @JsonProperty("nama")
    private String nama;

    @JsonProperty("kuantitas_tagihan")
    private int kuantitasTagihan;

    @JsonProperty("total_terbayar_tagihan")
    private int totalTagihanTerbayar;

    @JsonProperty("tagihan")
    private List<TagihanModel> tagihan;
}
