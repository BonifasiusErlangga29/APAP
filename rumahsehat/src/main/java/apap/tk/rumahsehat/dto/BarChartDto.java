package apap.tk.rumahsehat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BarChartDto {

    @JsonProperty("id")
    private long id;

    @JsonProperty("is_barchart_kuantitas")
    private boolean barChartKuantitas;

    @JsonProperty("list_barchart_tagihan")
    private List<BarChartTagihanDto> listBarChartTagihan;

}
