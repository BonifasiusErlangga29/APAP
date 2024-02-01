import 'package:flutter/material.dart';
import 'package:rumahsehat_flutter/appointment/add_appointment_page.dart';
import 'package:rumahsehat_flutter/bill/list_bill_page.dart';
import 'package:rumahsehat_flutter/profile/profil_page.dart';
import 'package:intl/date_symbol_data_local.dart';
import 'package:rumahsehat_flutter/common/cookie_request.dart';
import 'package:provider/provider.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:rumahsehat_flutter/login.dart';



import 'appointment/list_appointment_page.dart';

class MyMainPage extends StatelessWidget {
  const MyMainPage({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'RUMAH',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or simply save your changes to "hot reload" in a Flutter IDE).
        // Notice that the counter didn't reset back to zero; the application
        // is not restarted.
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(title: 'RUMAH SEHAT'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key, required this.title}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  @override
  Widget build(BuildContext context) {
    final request = context.watch<CookieRequest>();

    initializeDateFormatting();
    return MaterialApp(
      home: Scaffold(
          appBar: AppBar(
              title: const Text('RUMAH SEHAT'),
              leading: IconButton(
                icon: const Icon(Icons.logout),
                onPressed: () async {
                  SharedPreferences pref =
                      await SharedPreferences.getInstance();
                  setState(() {
                    pref.remove("isSignIn");
                    pref.remove("CookieRequest");
                  });
                  Navigator.pushAndRemoveUntil(
                    context,
                    MaterialPageRoute(
                      builder: (context) => const Login(),
                    ),
                    (route) => false,
                  );
                },
              )),

          body: Center(
              child: Column(children: <Widget>[
            const SizedBox(
              height: 75,
            ),
            Text("Selamat datang, " + request.username + "!",
                style: const TextStyle(
                  fontFamily: "Montserrat",
                  color: Colors.black,
                  fontSize: 20.0,
                  fontWeight: FontWeight.normal,
                )),
            const SizedBox(
              height: 35,
            ),
            Container(
              margin: const EdgeInsets.all(15),
              child: ElevatedButton(
                child: const Text('Buat Appointment'),
                style: ButtonStyle(
                    backgroundColor: MaterialStateProperty.all(Colors.blue),
                    padding: MaterialStateProperty.all(const EdgeInsets.all(15)),
                    textStyle:
                        MaterialStateProperty.all(const TextStyle(fontSize: 15))),
                onPressed: () async {
                  Navigator.of(context).push(MaterialPageRoute(
                    builder: (context) => const AddAppointmentPage(),
                  ));
                },
              ),
            ),
            Container(
              margin: const EdgeInsets.all(15),
              child: ElevatedButton(
                child: const Text('Melihat Daftar Appointment'),
                style: ButtonStyle(
                    backgroundColor: MaterialStateProperty.all(Colors.blue),
                    padding: MaterialStateProperty.all(const EdgeInsets.all(15)),
                    textStyle:
                        MaterialStateProperty.all(const TextStyle(fontSize: 15))),
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) => const ListAppointmentPage()),
                  );
                },
              ),
            ),

            Container(
              margin: const EdgeInsets.all(15),
              child: ElevatedButton(
                child: const Text('Melihat Profile'),
                style: ButtonStyle(
                    backgroundColor: MaterialStateProperty.all(Colors.blue),
                    padding: MaterialStateProperty.all(const EdgeInsets.all(15)),
                    textStyle:
                        MaterialStateProperty.all(const TextStyle(fontSize: 15))),
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (context) => const ProfilPage()),
                  );
                },
              ),
            ),

            Container(
              margin: const EdgeInsets.all(15),
              child: ElevatedButton(
                child: const Text('Melihat Daftar Tagihan'),
                style: ButtonStyle(
                    backgroundColor: MaterialStateProperty.all(Colors.blue),
                    padding: MaterialStateProperty.all(const EdgeInsets.all(15)),
                    textStyle:
                        MaterialStateProperty.all(const TextStyle(fontSize: 15))),
                    onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) => const ListBillPage()),
                  );
                },              ),
            ),

          ]
          )
          )
          ),
    );
  }
}
