import 'dart:convert';
import 'package:rumahsehat_flutter/common/cookie_request.dart';

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:rumahsehat_flutter/home.dart';

import '../date_format.dart';
import '../home.dart';
import 'detail_appointment_page.dart';
import 'detail_appointment.dart';

class ListAppointmentPage extends StatefulWidget {
  const ListAppointmentPage({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => ListAppointmentState();
}

class ListAppointmentState extends State<ListAppointmentPage> {
  late Future<List<DetailAppointment>> listAppointment;

  @override
  void initState() {
    super.initState();
    final request = context.read<CookieRequest>();
    listAppointment = fetchListDetailAppointment(request);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text('List Appointment'),
          leading: BackButton(
            onPressed: () =>Navigator.of(context).pushReplacement(MaterialPageRoute(
                            builder: (context) => const MyMainPage(),
                          ))
          ),
        ),
        body: FutureBuilder<List<DetailAppointment>>(
          future: listAppointment,
          builder: (context, snapshot) {
            if (snapshot.hasData && snapshot.data != null) {
              if (snapshot.data!.isEmpty) {
                return const Center(
                  child: Text("Tidak ada appointment saat ini",
                      style: TextStyle(
                        color: Colors.black,
                        fontSize: 20.0,
                        fontWeight: FontWeight.normal,
                      )),
                );
              }
              return ListView(
                padding: const EdgeInsets.all(8),
                children: <Widget>[
                  for (var i in snapshot.data!)
                    Card(
                      child: ListTile(
                        title: Text(i.namaDokter),
                        subtitle: i.isDone
                            ? const Text('Sudah Selesai')
                            : const Text('Belum Selesai'),
                        trailing: Text(CustomDateFormat.format(i.waktu)),
                        onTap: () {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                                builder: (context) => DetailAppointmentPage(
                                      detailAppointment: i,
                                    )),
                          );
                        },
                      ),
                    )
                ],
              );
            } else if (snapshot.hasError) {
              return Text('${snapshot.error}');
            }
            return const CircularProgressIndicator();
          },
        ));
  }
}

Future<List<DetailAppointment>> fetchListDetailAppointment(
    CookieRequest request) async {
  const host =
      String.fromEnvironment('host', defaultValue: "http://localhost:8080");
  final response = await request.get(host + '/api/v1/list-appointment');

  if (response.statusCode == 200) {
    Iterable l = json.decode(response.body);

    return List<DetailAppointment>.from(
        l.map((json) => DetailAppointment.fromJson(json)));
  } else {
    throw Exception('Failed to load detail appointment');
  }
}
