import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:rumahsehat_flutter/common/cookie_request.dart';
import 'package:rumahsehat_flutter/home.dart';
import 'package:rumahsehat_flutter/profile/pasien.dart';
import 'package:provider/provider.dart';
import 'package:rumahsehat_flutter/currency_format.dart';
import 'package:rumahsehat_flutter/profile/top_up.dart';

class ProfilPage extends StatefulWidget {
  const ProfilPage({Key? key}) : super(key: key);

  @override
  State<ProfilPage> createState() => _ProfilPageState();
}

class _ProfilPageState extends State<ProfilPage> {
  late Future<Pasien> profilFuture;

  @override
  void initState() {
    super.initState();
    var request = context.read<CookieRequest>();
    profilFuture = fetchPasien(request);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text('Profil'),
          leading: BackButton(
            onPressed: () => Navigator.of(context).pushReplacement(MaterialPageRoute(
                            builder: (context) => const MyMainPage(),
                          ))
          ),
        ),
        body: FutureBuilder<Pasien>(
          future: profilFuture,
          builder: (context, snapshot) {
            if (snapshot.hasData && snapshot.data != null) {
              return Container(
                padding: const EdgeInsets.all(32),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  mainAxisSize: MainAxisSize.min,
                  children: <Widget>[
                    Expanded(
                        child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: <Widget>[
                        Padding(
                          padding: const EdgeInsets.only(bottom: 12),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Padding(
                                padding: const EdgeInsets.only(bottom: 8),
                                child: ClipRRect(
                                  borderRadius: BorderRadius.circular(360),
                                  child: const Image(
                                      height: 90,
                                      image: AssetImage('assets/avatar.png')),
                                ),
                              ),
                              Text(
                                snapshot.data!.nama,
                                style: const TextStyle(
                                    fontSize: 24, fontWeight: FontWeight.bold),
                              ),
                              Text(
                                '@${snapshot.data!.username}',
                                style: TextStyle(
                                    fontSize: 18, color: Colors.grey[700]),
                              ),
                            ],
                          ),
                        ),
                        Text(snapshot.data!.email),
                        Text('Saldo: ' +
                            CurrencyFormat.convertToIdr(
                                snapshot.data!.saldo, 2)),
                        Padding(
                            padding: const EdgeInsets.only(top: 8),
                            child: ElevatedButton(
                              onPressed: () {
                                Navigator.push(
                                  context,
                                  MaterialPageRoute(
                                      builder: (context) =>
                                          TopUpSaldo(pasien: snapshot.data!)),
                                );
                              },
                              child: const Text('Top Up Saldo'),
                            ))
                      ],
                    ))
                  ],
                ),
              );
            } else if (snapshot.hasError) {
              return Text('${snapshot.error}');
            }
            return const CircularProgressIndicator();
          },
        ));
  }
}

Future<Pasien> fetchPasien(CookieRequest request) async {
  const host =
      String.fromEnvironment('host', defaultValue: "http://localhost:8080");
  final response = await request.get(host + '/api/v1/pasien/');
  if (response.statusCode == 200) {
    return Pasien.fromJson(jsonDecode(response.body));
  } else {
    throw Exception('Failed to load pasien');
  }
}
