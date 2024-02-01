package apap.tk.rumahsehat.model;

import java.util.List;

public class AuthResponse {
    private String token;
    private List<String> roles;
    private PasienModel pasien;
  
    public String getToken() {
      return token;
    }
  
    public void setToken(String token) {
      this.token = token;
    }
  
    public List<String> getRoles() {
      return roles;
    }
  
    public void setRoles(List<String> roles) {
      this.roles = roles;
    } 

    public PasienModel getPasien() {
      return pasien;
    }

    public void setPasien(PasienModel pasien) {
      this.pasien = pasien;
    }  
  }