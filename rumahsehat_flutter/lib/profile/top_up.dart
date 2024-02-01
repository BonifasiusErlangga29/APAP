import 'dart:convert';
import 'package:http/http.dart' as http;


import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:rumahsehat_flutter/common/cookie_request.dart';
import 'package:rumahsehat_flutter/profile/pasien.dart';
import 'package:provider/provider.dart';
import 'package:rumahsehat_flutter/profile/profil_page.dart';

class TopUpSaldo extends StatefulWidget {
  final Pasien pasien;

  const TopUpSaldo({Key? key, required this.pasien}) : super(key: key);

  @override
  State<TopUpSaldo> createState() => _TopUpSaldoState();
}

class _TopUpSaldoState extends State<TopUpSaldo> {
  final TextEditingController _saldoController = TextEditingController();

  @override
  void initState() {
    super.initState();
  }

    void displayDialog(context, title, text) => showDialog(
        context: context,
        builder: (context) =>
            AlertDialog(title: Text(title), content: Text(text)),
      );

  @override
  Widget build(BuildContext context) {
    var request = context.read<CookieRequest>();

    return Scaffold(
      appBar: AppBar(
        title: const Text('TopUp Saldo'),
        leading: BackButton(
          onPressed: () => Navigator.pop(context),
        ),
      ),
      body: Container(
          padding: const EdgeInsets.all(40.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              TextField(
                decoration: const InputDecoration(
                    labelText: "Jumlah saldo yang di top up"),
                keyboardType: TextInputType.number,
                controller: _saldoController,
                inputFormatters: <TextInputFormatter>[
                  FilteringTextInputFormatter.digitsOnly
                ], // Only numbers can be entered
              ),
              Padding(
                  padding: const EdgeInsets.only(top: 8),
                  child: ElevatedButton(
                    onPressed: () async {
                      var response = await topUp(
                          request, widget.pasien, _saldoController.text);

                       if (response.statusCode == 200) {
                      displayDialog(
                          context, "Success", "Berhasil top up saldo sebesar " + _saldoController.text);
                          Navigator.of(context).pushReplacement(MaterialPageRoute(
                            builder: (context) => const ProfilPage(),
                          ));

                    } else if (response.statusCode == 400) {
                      displayDialog(context, "An Error Occurred",
                          "Request body has invalid type or missing field.");
                    } else if (response.statusCode == 403) {
                      displayDialog(context, "An Error Occurred", "Forbidden");
                    } else {
                      displayDialog(context, "An Error Occurred",
                          "");
                    }
                    },
                    child: const Text('Top Up Saldo'),
                  ))
            ],
          )),
    );
  }
}

Future<http.Response> topUp(
    CookieRequest request, Pasien pasien, String saldoBaru) async {
  var body = jsonEncode({
    "nama": pasien.nama,
    "username": pasien.username,
    "email": pasien.email,
    "saldo": pasien.saldo + int.parse(saldoBaru),
    "umur": pasien.umur
  });
  const host =
      String.fromEnvironment('host', defaultValue: "http://localhost:8080");
  final response = await request.post(
      host + '/api/v1/pasien/topUpSaldo/' + request.username, body);
  return response;
}
