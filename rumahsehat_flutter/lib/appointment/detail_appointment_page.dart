import 'package:flutter/material.dart';
import 'package:rumahsehat_flutter/appointment/detail_resep_page.dart';
import 'detail_appointment.dart';

class DetailAppointmentPage extends StatefulWidget {
    final DetailAppointment detailAppointment;

    const DetailAppointmentPage({Key? key, required this.detailAppointment}) : super(key: key);
  // const DetailBillPage({Key? key, required this.detailBill}) : super(key: key);

  @override
  State<DetailAppointmentPage> createState() => _DetailAppointmentState();
}

class _DetailAppointmentState extends State<DetailAppointmentPage> {

  @override
  void initState() {
    super.initState();
  }


   Widget _buildDetailResepButton() {
    return widget.detailAppointment.resep.id != 0
        ? ElevatedButton(
                      child: const Text('Detail Resep'),
                      style: ButtonStyle(
                          backgroundColor:
                              MaterialStateProperty.all(Colors.blue),
                          padding: MaterialStateProperty.all(
                              const EdgeInsets.all(15)),
                          textStyle: MaterialStateProperty.all(
                              const TextStyle(fontSize: 15))),
                      onPressed: () {
                        Navigator.push(
                            context,
                            MaterialPageRoute(
                                builder: (context) => DetailResepPage(
                                      detailResep: widget.detailAppointment.resep, detailAppointment: widget.detailAppointment,
                                    )),
                          );
                      },
                    )
        : const Text("Belum ada resep untuk appointment ini");
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Detail Appointment'),
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
                    'Detail Appointment',
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
                    'Kode Appointment: ${widget.detailAppointment.kode}',
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
                    'Nama Dokter: ${widget.detailAppointment.namaDokter}',
                    style: const TextStyle(
                      fontSize: 15,
                      color: Colors.blue,
                      fontFamily: "Verdana",
                    ), //Textstyle
                  ), //Text
                  const SizedBox(
                    height: 10,
                  ), //SizedBox
                  
                  if(widget.detailAppointment.isDone) ...[
                    const Text("Status Appointment: Sudah Selesai", 
                    style: TextStyle(
                      fontSize: 15,
                      color: Colors.blue,
                      fontFamily: "Verdana")),
                    
                  ]else ...[
                    const Text("Status Appointment: Belum Selesai",
                    style: TextStyle(
                      fontSize: 15,
                      color: Colors.blue,
                      fontFamily: "Verdana")),
                  ],
                  const SizedBox(
                    height: 10,
                  ), //SizedBox
                  Text(
                    'Waktu Mulai Appointment: ${widget.detailAppointment.waktu}',
                    style: const TextStyle(
                      fontSize: 15,
                      color: Colors.blue,
                      fontFamily: "Verdana",
                    ), //Textstyle
                  ), //Text
                  const SizedBox(
                    height: 30,
                  ), //SizedBox
                  const SizedBox(
                    width: 120,
                  ),
                  _buildDetailResepButton()
                ],
              ), //Column
            ), //Padding
          ), //SizedBox
        ), //Card
      ), //Center
    );
  }
}

