package apap.tk.rumahsehat.service;
import apap.tk.rumahsehat.model.UserModel;

import java.util.List;

public interface UserService {
    UserModel addUser(UserModel user);
    public String encrypt(String password);
    List<UserModel> getListUser();
    UserModel getUserByUsername(String username);
    UserModel getUserByEmail(String email);
}
