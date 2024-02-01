package apap.tk.rumahsehat.service;

import apap.tk.rumahsehat.model.UserModel;
import apap.tk.rumahsehat.repository.UserDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    private UserDb userDb;

    @InjectMocks
    private UserService userService = new UserServiceImpl();

    @BeforeEach
    public void setUp() {}

    @Test
    void getUserByUsername() {
        String username = "username";
        UserModel userModel = new UserModel();
        userModel.setUsername(username);

        Mockito.when(userDb.findByUsername(username))
                .thenReturn(userModel);

        UserModel userFromService = userService.getUserByUsername(username);
        assertEquals(username, userFromService.getUsername());
    }


}
