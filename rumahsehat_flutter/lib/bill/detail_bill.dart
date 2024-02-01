class DetailBill {
  String kode;
  DateTime tanggalTerbuat;
  DateTime tanggalBayar;
  bool isPaid;
  int jumlahTagihan;
  String kodeAppointment;
  String usernamePasien;

  DetailBill(
      {required this.kode,
      required this.tanggalTerbuat,
      required this.tanggalBayar,
      required this.isPaid, 
      required this.jumlahTagihan, 
      required this.kodeAppointment,
      required this.usernamePasien
      });

  factory DetailBill.fromJson(Map<String, dynamic> json) {
    return DetailBill(
        kode: json['kode'],
        tanggalTerbuat:  DateTime.parse(json['tanggal_terbuat']),
        tanggalBayar:  DateTime.parse(json['tanggal_terbayar']),
        isPaid: json['is_paid'], 
        jumlahTagihan: json['jumlah_tagihan'], 
        kodeAppointment: json['kode_appointment'], 
        usernamePasien: json['username_pasien']
    );
  }
}
