package apap.tk.rumahsehat;

import apap.tk.rumahsehat.controller.PageController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class RumahsehatApplicationTests {

	@Autowired
	PageController pageController;

	@Test
	void contextLoads() {
		assertNotNull(pageController);
	}

}
