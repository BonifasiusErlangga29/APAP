package apap.tk.rumahsehat.service;

import apap.tk.rumahsehat.dto.AppointmentDto;
import apap.tk.rumahsehat.model.AppointmentModel;
import apap.tk.rumahsehat.model.DokterModel;
import apap.tk.rumahsehat.model.PasienModel;

import java.util.List;

public interface AppointmentService {
    AppointmentModel addAppointment(AppointmentModel apt);

    // Fitur 8 Melihat Detail Appointment (Admin, Dokter, Pasien)
    AppointmentModel getAppointmentByKodeAppointment(String kode);

    // Fitur 8 Melihat Detail Appointment (Admin, Dokter, Pasien) - UPDATE STATUS
    AppointmentModel updateStatusAppointment (AppointmentModel appointment);

    /**
     * Mengembalikan semua appointment
     * @return list appointment
     */
    List<AppointmentModel> listAll();

    /**
     * Mengembalikan semua appointment dengan dokter tertentu
     * @param dokterModel dokter
     * @return list appointment
     */
    List<AppointmentModel> listByDokter(DokterModel dokterModel);

    /**
     * Mengembalikan semua appointment dengan pasien tertentu
     * @param pasienModel pasien
     * @return list appointment
     */
    List<AppointmentModel> listByPasien(PasienModel pasienModel);
    boolean checkWaktu(AppointmentDto appointment);
}
