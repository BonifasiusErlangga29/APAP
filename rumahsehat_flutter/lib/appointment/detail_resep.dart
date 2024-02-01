class DetailResep {
  int id;
  bool isDone;
  String createdAt;
  String usernameApoteker;
  String namaApoteker;
  String namaPasien;
  Map<String, int> listObat;

  DetailResep(
      {required this.id,
        required this.isDone,
        required this.createdAt,
        required this.usernameApoteker,
        required this.namaApoteker,
        required this.namaPasien,
        required this.listObat
        });

  factory DetailResep.fromJson(Map<String, dynamic> json) {
    return DetailResep(
        id: json['id'],
        isDone: json['is_done'],
        createdAt: json['created_at'],
        usernameApoteker: json['username_apoteker'],
        namaApoteker: json['nama_apoteker'],
        namaPasien: json['nama_pasien'],
        listObat: json['listObat'],);
  }
}
