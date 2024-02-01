package apap.tk.rumahsehat.service;

import apap.tk.rumahsehat.model.ApotekerModel;

import java.util.List;

public interface ApotekerService {
    ApotekerModel addApoteker(ApotekerModel apoteker);

    List<ApotekerModel> getlistApoteker();
}
