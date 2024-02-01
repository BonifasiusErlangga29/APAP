import 'detail_resep.dart';

class DetailAppointment {
  String kode;
  String namaDokter;
  bool isDone;
  DateTime waktu;
  DetailResep resep;

  DetailAppointment(
      {required this.kode,
      required this.namaDokter,
      required this.isDone,
      required this.waktu,
      required this.resep
      });

  factory DetailAppointment.fromJson(Map<String, dynamic> json) {
    DetailResep baru = DetailResep(id: json['resep']['id'], isDone: json['resep']['is_done'], createdAt: json['resep']['created_at'], usernameApoteker: json['resep']['username_apoteker'], namaApoteker: json['resep']['nama_apoteker'], namaPasien: json['resep']['nama_pasien'], listObat: Map.from(json['resep']['list_obat']));

    return DetailAppointment(
        kode: json['kode'],
        namaDokter: json['nama_dokter'],
        isDone: json['is_done'],
        waktu: DateTime.parse(json['waktu_awal']),
        resep: baru
        );
  }
}
