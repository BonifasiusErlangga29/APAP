package apap.tk.rumahsehat.service;
import apap.tk.rumahsehat.model.ApotekerModel;
import apap.tk.rumahsehat.repository.ApotekerDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApotekerServiceImpl implements ApotekerService{
    @Autowired
    private ApotekerDb apotekerDb;

    @Autowired
    private UserService userService;

    @Override
    public ApotekerModel addApoteker(ApotekerModel apoteker){
        String pass = userService.encrypt(apoteker.getPassword());
        apoteker.setPassword(pass);
        return apotekerDb.save(apoteker);
    }

    @Override
    public List<ApotekerModel> getlistApoteker(){
        return apotekerDb.findAll();
    }

}
