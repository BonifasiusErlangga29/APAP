
import 'package:flutter/material.dart';
import 'package:rumahsehat_flutter/patient/registration_patient_page.dart';
import 'package:intl/date_symbol_data_local.dart';
import 'common/cookie_request.dart';
import 'package:provider/provider.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:rumahsehat_flutter/home.dart';


class Login extends StatelessWidget {
  const Login({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'RumahSehat',
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
      home: const MyLoginPage(title: 'RUMAH SEHAT'),
    );
  }
}

class MyLoginPage extends StatefulWidget {
  
  const MyLoginPage({Key? key, required this.title}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  State<MyLoginPage> createState() => _MyLoginPage();
}

class _MyLoginPage extends State<MyLoginPage> {
  String username = "";
  String password = "";

  void displayDialog(context, title, text) => showDialog(
        context: context,
        builder: (context) =>
            AlertDialog(title: Text(title), content: Text(text)),
      );

  @override
  Widget build(BuildContext context) {
    final request = context.watch<CookieRequest>();
    initializeDateFormatting();
    return MaterialApp(
      home: Scaffold(
          appBar: AppBar(
            title: const Text('RUMAH SEHAT'),
          ),
          body: Container(
            decoration: const BoxDecoration(color: Color(0xFFE1F5FE)),
            child: Center(
                child: Column(children: <Widget>[
              const SizedBox(
                height: 150,
              ),
              Container(
                  constraints: const BoxConstraints(
                      maxWidth: 325,
                      maxHeight: 400,
                      minWidth: 325,
                      minHeight: 100),
                  decoration: BoxDecoration(
                    border: Border.all(
                        color: Colors.grey.shade300,
                        width: 1.0,
                        style: BorderStyle.solid), //Border.all

                    borderRadius: const BorderRadius.only(
                      topLeft: Radius.circular(10.0),
                      topRight: Radius.circular(10.0),
                      bottomLeft: Radius.circular(10.0),
                      bottomRight: Radius.circular(10.0),
                    ),

                    //BorderRadius.only
                    /************************************/
                    /* The BoxShadow widget  is here */
                    /************************************/
                    boxShadow: [
                      BoxShadow(
                        color: Colors.grey.shade300,
                        offset: const Offset(
                          0.0,
                          0.0,
                        ),
                        blurRadius: 5.0,
                        spreadRadius: 2.0,
                      ), //BoxShadow
                      const BoxShadow(
                        color: Colors.white,
                        offset: Offset(0.0, 0.0),
                        blurRadius: 0.0,
                        spreadRadius: 0.0,
                      ), //BoxShadow
                    ],

                    color: Colors.white,
                  ),
                
                  child: Column(children: <Widget>[
                    const SizedBox(
                      height: 25,
                    ),
                    Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 20),
                        child: Container(
                            alignment: Alignment.center,
                            padding: const EdgeInsets.all(10),
                            child: const Text(
                              'Log In Pasien',
                              style: TextStyle(
                                  fontSize: 20, fontWeight: FontWeight.bold),
                            ))),
                    const SizedBox(
                      height: 20,
                    ),
                    Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 20),
                      child: SizedBox(
                        height: 40,
                        child: TextFormField(
                          onChanged: (String? value) {
                            setState(() {
                              username = value!;
                            });
                          },
                          onSaved: (String? value) {
                            setState(() {
                              username = value!;
                            });
                          },
                          validator: (value) {
                            if (value!.isEmpty || value == "") {
                              return 'Jangan biarkan kosong!';
                            }
                            return null;
                          },
                          decoration: const InputDecoration(
                            label: Text('Username'),
                            contentPadding: EdgeInsets.symmetric(vertical: 0),
                          ),
                        ),
                      ),
                    ),
                    const SizedBox(
                      height: 35,
                    ),
                    Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 20),
                      child: SizedBox(
                        height: 40,
                        child: TextFormField(
                          obscureText: true,
                          onChanged: (String? value) {
                            setState(() {
                              password = value!;
                            });
                          },
                          onSaved: (String? value) {
                            setState(() {
                              password = value!;
                            });
                          },
                          validator: (value) {
                            if (value!.isEmpty || value == "") {
                              return 'Jangan biarkan kosong!';
                            }
                            return null;
                          },
                          decoration: const InputDecoration(
                            label: Text('Password'),
                            contentPadding: EdgeInsets.symmetric(vertical: 0),
                          ),
                        ),
                      ),
                    ),
                    const SizedBox(
                      height: 35,
                    ),
                    ElevatedButton(
                        style: ButtonStyle(
                            backgroundColor:
                                MaterialStateProperty.all(Colors.blue),
                            padding: MaterialStateProperty.all(
                                const EdgeInsets.all(15)),
                            textStyle: MaterialStateProperty.all(
                                const TextStyle(fontSize: 15))),
                          onPressed: () async {
                            var response = await login(request, username, password);

                            if (response.statusCode == 200) {
                                  Navigator.of(context).pushReplacement(MaterialPageRoute(
                                builder: (context) => const MyMainPage(),
                              ));
                            } else if (response.statusCode == 400) {
                              displayDialog(context, "An Error Occurred",
                                  "No account was found matching that username and password");
                            } else if (response.statusCode == 403) {
                              displayDialog(context, "An Error Occurred", "Forbidden");
                            } else {
                              displayDialog(context, "An Error Occurred",
                                  "No account was found matching that username and password");
                            }
                            
                          },
                          child: const Text(
                              "Sign In",
                            ),
                          
                        ),
                    const SizedBox(
                      height: 32,
                    ),
                    ElevatedButton(
                        child: const Text('Registrasi Pasien'),
                        style: ButtonStyle(
                            backgroundColor:
                                MaterialStateProperty.all(Colors.blue),
                            padding: MaterialStateProperty.all(
                                const EdgeInsets.all(15)),
                            textStyle: MaterialStateProperty.all(
                                const TextStyle(fontSize: 15))),
                                
                        onPressed: () async {
                          Navigator.of(context).push(MaterialPageRoute(
                            builder: (context) => const RegistrationPage(),
                          ));
                        },
                      ),
                    
                  ])),
            ])),
          )),
    );
  }
}

Future<http.Response> login(
    CookieRequest request, String username, String password) async {
  var body = jsonEncode(<String, String>{
    "username": username,
    "password": password,
  });

  const host =
      String.fromEnvironment('host', defaultValue: "http://localhost:8080");
  final http.Response res =
      await request.login(host + '/api/v1/pasien/login', body);
  return res;
}
