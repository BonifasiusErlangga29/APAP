package apap.tk.rumahsehat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PasienDto {

    @JsonProperty("nama")
    private String nama;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("saldo")
    private int saldo;

    @JsonProperty("password")
    private String password;

    @JsonProperty("umur")
    private int umur;
}
