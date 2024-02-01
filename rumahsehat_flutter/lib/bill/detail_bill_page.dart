
import 'package:rumahsehat_flutter/bill/list_bill_page.dart';

import 'detail_bill.dart';
import 'package:http/http.dart' as http;

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:rumahsehat_flutter/common/cookie_request.dart';
import 'package:provider/provider.dart';

class DetailBillPage extends StatefulWidget {
    final DetailBill detailBill;

  const DetailBillPage({Key? key, required this.detailBill}) : super(key: key);

  @override
  State<DetailBillPage> createState() => _DetailBillState();
}

class _DetailBillState extends State<DetailBillPage> {

  @override
  void initState() {
    super.initState();
  }

 void displayDialog(title, text) => showDialog(
        context: context,
        builder: (context) =>
            AlertDialog(title: Text(title), content: Text(text), actions: [
                ElevatedButton(                    
                  onPressed: () {
                    Navigator.of(context).pushReplacement(MaterialPageRoute(
                            builder: (context) => const ListBillPage(),
                          ));
                  },         
                  child: const Text('Oke'),
                ),]),
            
      );

  void _showDialog(BuildContext context) {
    var request = context.read<CookieRequest>();
  showDialog(
    context: context,
    builder: (BuildContext context) {
      return AlertDialog(
              title: const Text('Konfirmasi Bayar'),          
              content: Text('Apakah Anda yakin ingin membayar tagihan ' + widget.detailBill.kode + ' seharga ' + widget.detailBill.jumlahTagihan.toString() + '?'),  
                                              
                actions: [
                ElevatedButton(                    
                  onPressed: () {
                      Navigator.of(context).pop();

                  },            
                  child: const Text('Batal'),
                ),
                ElevatedButton(
                  onPressed: () async {
                    Navigator.of(context).pop();

                    var response = await attemptBayar(widget.detailBill,request);

                    if (response.statusCode == 200) {
                      displayDialog(
                          "Sukses", "Tagihan Berhasil Dibayar");

                      
                    } else if (response.statusCode == 400) {
                      displayDialog("Error",
                          "Saldo tidak mencukupi");
                    } else if (response.statusCode == 403) {
                      displayDialog("An Error Occurred", "Forbidden");
                    } else {
                      displayDialog("An Error Occurred",
                          "No account was found matching that username and password");
                    }
                  },
                  
                  child: const Text('Yakin'),
                ),
              ],
            );
    },
  );
}

  Widget _buildLoginButton() {
    return widget.detailBill.isPaid == false
        ? ElevatedButton(
            onPressed: () {
                  _showDialog(context);
            },
            child: const Text("Bayar"),
          )
        : Text("Telah dibayar pada " + generateDateTime(widget.detailBill.tanggalBayar.toString()));
  }

  String generateDateTime(String raw) {
    String year = raw.substring(0, 4);
    String month = raw.substring(5, 7);
    String day = raw.substring(8, 10);

    String time = raw.substring(11, 19);

    if (month == "01") {
      month = "Januari";
    } else if (month == "02") {
      month = "Februari";
    } else if (month == "03") {
      month = "Maret";
    } else if (month == "04") {
      month = "April";
    }else if (month == "05") {
      month = "Mei";
    }else if (month == "06") {
      month = "Juni";
    }else if (month == "07") {
      month = "Juli";
    }else if (month == "08") {
      month = "Agustus";
    }else if (month == "09") {
      month = "September";
    }else if (month == "10") {
      month = "Oktober";
    }else if (month == "11") {
      month = "November";
    }else if (month == "12") {
      month = "Desember";
    }
    return "dibuat pada " + day + " " + month + " " + year + " pukul " + time;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text('Tagihan'),
          leading: BackButton(
            onPressed: () => Navigator.pop(context),
          ),
        ),
        body: Center(
              child: Column(children: <Widget>[
                const SizedBox(
                  height: 75,
                ),
                 Text(widget.detailBill.kode,
                style: const TextStyle(
                  fontFamily: "Montserrat",
                  color: Colors.black,
                  fontSize: 20.0,
                  fontWeight: FontWeight.normal,
                )),
                const SizedBox(
                  height: 10,
                ),
                Text("dibuat pada " + generateDateTime(widget.detailBill.tanggalTerbuat.toString())),
                const SizedBox(
                  height: 20,
                ),
                const Text("Jumlah Tagihan: "),
                const SizedBox(
                  height: 5,
                ),
                Text(widget.detailBill.jumlahTagihan.toString(),
                style: const TextStyle(
                  fontFamily: "Montserrat",
                  color: Colors.black,
                  fontSize: 18.0,
                  fontWeight: FontWeight.normal,
                )),
                const SizedBox(
                  height: 10,
                ),
                _buildLoginButton(), 

              ]
            )
        )
    );
  }
  Future<http.Response> attemptBayar(DetailBill detailBill, CookieRequest request) async {
      var body = jsonEncode({
        "kode": detailBill.kode,
        "is_paid": detailBill.isPaid, 
        "jumlah_tagihan": detailBill.jumlahTagihan, 
        "kode_appointment": detailBill.kodeAppointment
      });

      const host =
          String.fromEnvironment('host', defaultValue: "http://localhost:8080");
      var res = await request.post(host + '/api/v1/bill/bayar/' + detailBill.kode, body);
      return res;
    }
}
