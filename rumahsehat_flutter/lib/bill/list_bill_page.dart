import 'dart:convert';
import 'package:rumahsehat_flutter/common/cookie_request.dart';

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../date_format.dart';
import 'detail_bill_page.dart';
import 'detail_bill.dart';
import 'package:rumahsehat_flutter/home.dart';


class ListBillPage extends StatefulWidget {
  const ListBillPage({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => ListBillState();
}

class ListBillState extends State<ListBillPage> {
  late Future<List<DetailBill>> listBill;

  @override
  void initState() {
    super.initState();
    final request = context.read<CookieRequest>();
    listBill = fetchListDetailBill(request);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text('List Bill'),
          leading: BackButton(
            onPressed: () => Navigator.of(context).pushReplacement(MaterialPageRoute(
                            builder: (context) => const MyMainPage(),
                          ))
          ),
        ),
        body: FutureBuilder<List<DetailBill>>(
          future: listBill,
          builder: (context, snapshot) {
            if (snapshot.hasData && snapshot.data != null) {
              if (snapshot.data!.isEmpty) {
                return const Center(
                  child: Text("Tidak ada tagihan saat ini",
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
                        title: Text(i.kode),
                        subtitle: i.isPaid
                            ? const Text('Sudah Dibayar')
                            : const Text('Belum Dibayar'),
                        trailing: Text(CustomDateFormat.format(i.tanggalTerbuat)),
                        onTap: () {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                                builder: (context) => DetailBillPage(
                                      detailBill: i,
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

Future<List<DetailBill>> fetchListDetailBill(
    CookieRequest request) async {
  const host =
      String.fromEnvironment('host', defaultValue: "http://localhost:8080");
  final response = await request.get(host + '/api/v1/bill/list');

  if (response.statusCode == 200) {
    Iterable l = json.decode(response.body);
    return List<DetailBill>.from(
        l.map((json) => DetailBill.fromJson(json)));
  } else {
    throw Exception('Failed to load detail appointment');
  }
}
