class Pasien {
  final String nama;
  final String username;
  final String email;
  final int saldo;
  final int umur;

  const Pasien(
      {required this.nama,
      required this.username,
      required this.email,
      required this.saldo,
      required this.umur});

  factory Pasien.fromJson(Map<String, dynamic> json) {
    return Pasien(
        nama: json['nama'],
        username: json['username'],
        email: json['email'],
        saldo: json['saldo'],
        umur: json['umur']);
  }
}
