package apap.tk.rumahsehat.service;

import apap.tk.rumahsehat.dto.AppointmentDto;
import apap.tk.rumahsehat.model.AppointmentModel;
import apap.tk.rumahsehat.model.DokterModel;
import apap.tk.rumahsehat.model.PasienModel;
import apap.tk.rumahsehat.repository.AppointmentDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements  AppointmentService {

    @Autowired
    AppointmentDb appointmentDb;
    @Autowired
    UserService userService;


    @Override
    public AppointmentModel addAppointment(AppointmentModel apt) {
        return appointmentDb.save(apt);

    }

    @Override
    public List<AppointmentModel> listAll() {
        return appointmentDb.findAll();
    }

    @Override
    public AppointmentModel getAppointmentByKodeAppointment(String kode) {
        Optional<AppointmentModel> appointment = appointmentDb.findByKode(kode);
        if (appointment.isPresent()) {
            return appointment.get();
        } else return null;
    }

    @Override
    public AppointmentModel updateStatusAppointment(AppointmentModel appointment) {
        appointmentDb.save(appointment);
        return appointment;
    }
    
    @Override
    public List<AppointmentModel> listByDokter(DokterModel dokterModel) {
        return appointmentDb.findAllByDokter(dokterModel);
    }

    @Override
    public List<AppointmentModel> listByPasien(PasienModel pasienModel) {
        return appointmentDb.findAllByPasien(pasienModel);
    }

    @Override
    public boolean checkWaktu(AppointmentDto appointment){
        LocalDateTime waktuApt = appointment.getWaktuAwal();

        DokterModel dokter = (DokterModel) userService.getUserByUsername(appointment.getUsernameDokter());
        
        for (AppointmentModel apt: dokter.getListAppointment()) {
            if (Math.abs(Duration.between(waktuApt, apt.getWaktuAwal()).toMinutes()) < 60) {
                return false;
            }
        }
        return true;
    }
}
