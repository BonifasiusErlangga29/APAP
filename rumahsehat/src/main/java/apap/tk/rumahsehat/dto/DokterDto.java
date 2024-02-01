package apap.tk.rumahsehat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DokterDto {

    @JsonProperty("nama")
    private String nama;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("tarif")
    private int tarif;

    @JsonProperty("password")
    private String password;
}
