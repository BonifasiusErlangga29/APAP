package apap.tk.rumahsehat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BarChartTagihanDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("barchart")
    private long barChartId;

    @JsonProperty("tagihan_selected")
    private int tagihanSelectedId;

}
