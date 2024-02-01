import 'package:flutter/material.dart';
import 'package:rumahsehat_flutter/appointment/detail_appointment.dart';

import 'detail_resep.dart';

class DetailResepPage extends StatelessWidget {
  final DetailResep detailResep;
  final DetailAppointment detailAppointment;

  const DetailResepPage({Key? key, required this.detailResep, required this.detailAppointment}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Detail Resep'),
        leading: BackButton(
          onPressed: () => Navigator.pop(context),
        ),
      ),

      body: Center(
        /** Card Widget **/
        child: Card(
          elevation: 50,
          shadowColor: Colors.black,
          color: Colors.white,
          child: SizedBox(
            width: 400,
            height: 300,
            child: Padding(
              padding: const EdgeInsets.all(20.0),
              child: Column(
                children: [
                  const Text(
                    'Detail Resep',
                    style: TextStyle(
                      fontSize: 30,
                      color: Colors.blue,
                      fontWeight: FontWeight.w700,
                      fontFamily: "Verdana",
                    ), //Textstyle
                  ), //Text
                  const SizedBox(
                    height: 30,
                  ),
                  Text(
                    'ID Resep: ${detailResep.id}',
                    style: const TextStyle(
                      fontSize: 15,
                      color: Colors.blue,
                      fontFamily: "Verdana",
                    ), //Textstyle
                  ), //Text
                  const SizedBox(
                    height: 10,
                  ),
                  Text(
                    'Nama Dokter: ${detailAppointment.namaDokter}',
                    style: const TextStyle(
                      fontSize: 15,
                      color: Colors.blue,
                      fontFamily: "Verdana",
                    ), //Textstyle
                  ), //Text
                  const SizedBox(
                    height: 30,
                  ),
                  Text(
                    'Nama Pasien: ${detailResep.namaPasien}',
                    style: const TextStyle(
                      fontSize: 15,
                      color: Colors.blue,
                      fontFamily: "Verdana",
                    ), //Textstyle
                  ),
                  const SizedBox(
                    height: 10,
                  ), //SizedBox

                  if(detailResep.isDone) ...[
                    const Text("Status Resep: Sudah Selesai",
                        style: TextStyle(
                            fontSize: 15,
                            color: Colors.blue,
                            fontFamily: "Verdana")),

                  ]else ...[
                    const Text("Status Resep: Belum Selesai",
                        style: TextStyle(
                            fontSize: 15,
                            color: Colors.blue,
                            fontFamily: "Verdana")),
                  ],
                  const SizedBox(
                    height: 10,
                  ), //SizedBox
                  if(detailResep.isDone) ...[
                    Text('Nama Apoteker (yang mengonfirmasi): ${detailResep.namaApoteker}',
                        style: const TextStyle(
                            fontSize: 15,
                            color: Colors.blue,
                            fontFamily: "Verdana")),

                  ]else ...[
                    const Text('Nama Apoteker (yang mengonfirmasi): -',
                        style: TextStyle(
                            fontSize: 15,
                            color: Colors.blue,
                            fontFamily: "Verdana")),

                  ],//Text
                ],
              ), //Column
            ), //Padding
          ), //SizedBox
        ), //Card
      ), //Center
    );
  }
}
