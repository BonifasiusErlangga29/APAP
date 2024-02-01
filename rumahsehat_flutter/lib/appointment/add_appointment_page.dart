import 'dart:convert';
import 'package:intl/intl.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:date_format/date_format.dart';
import 'package:rumahsehat_flutter/appointment/list_appointment_page.dart';
import 'package:rumahsehat_flutter/common/cookie_request.dart';
import 'package:provider/provider.dart';

class AddAppointmentPage extends StatefulWidget {
  const AddAppointmentPage({Key? key}) : super(key: key);

  @override
  State<AddAppointmentPage> createState() => _AddAppointmentPageState();
}

class _AddAppointmentPageState extends State<AddAppointmentPage> {
  String _dokter = "Alifiyah";
  double _height = 0;
  double _width = 0;

  String _setTime = "", _setDate = "";

  String _hour = "", _minute = "", _time = "";

  String dateTime = "";

  DateTime selectedDate = DateTime.now();

  TimeOfDay selectedTime = TimeOfDay(hour: 00, minute: 00);

  final TextEditingController _dateController = TextEditingController();
  final TextEditingController _timeController = TextEditingController();

  List data = <dynamic>[]; //edited line

  Future<void> _selectDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
        context: context,
        initialDate: selectedDate,
        initialDatePickerMode: DatePickerMode.day,
        firstDate: DateTime(2015),
        lastDate: DateTime(2101));
    if (picked != null) {
      setState(() {
        selectedDate = picked;
        _dateController.text = DateFormat.yMd().format(selectedDate);
      });
    }
  }

  Future<String> fetchListDokter(CookieRequest request) async {
    const host =
        String.fromEnvironment('host', defaultValue: "http://localhost:8080");
    final response = await request.get(host + '/api/v1/dokter/list');

    if (response.statusCode == 200) {
      var resBody = json.decode(response.body);
      setState(() {
        data = resBody;
        _dokter = data[0]["username"];
      });

      return "Success";
    } else {
      throw Exception('Failed to load detail appointment');
    }
  }

  Future<void> _selectTime(BuildContext context) async {
    final TimeOfDay? picked = await showTimePicker(
      context: context,
      initialTime: selectedTime,
    );
    if (picked != null) {

      setState(() {
        selectedTime = picked;
        _hour = selectedTime.hour.toString();
        _minute = selectedTime.minute.toString();
        _time = _hour + ' : ' + _minute;
        _timeController.text = _time;
        _timeController.text = formatDate(
            DateTime(2019, 08, 1, selectedTime.hour, selectedTime.minute),
            [hh, ':', nn, " ", am]).toString();
      });
    }
  }

  void displayDialog(context, title, text) => showDialog(
        context: context,
        builder: (context) =>
            AlertDialog(title: Text(title), content: Text(text)),
      );


  Future<http.Response> attemptAddAppointment(
      CookieRequest request, String date, String time, String dokter) async {
    String day = date.split("/")[1];
    String month = date.split("/")[0];
    String year = date.split("/")[2];

    if (day.length == 1) {
      day = "0" + day;
    }
    if (month.length == 1) {
      month = "0" + month;
    }

    String hourmin = time.split(" ")[0];
    String am = time.split(" ")[1];

    String hour = hourmin.split(":")[0];
    String min = hourmin.split(":")[1];

    if (am == "PM") {
      if (hour != "12") {
        hour = (int.parse(hour) + 12).toString();
      }
    }

    if (hour.length == 1) {
      hour = "0" + hour;
    }
    if (min.length == 1) {
      min = "0" + min;
    }

    hourmin = hour + ":" + min;

    String dateTime = year + "-" + month + "-" + day + "T" + hourmin + ":00";

    var body = jsonEncode(
        <String, String>{"waktu_awal": dateTime, "username_dokter": dokter});
    const host =
        String.fromEnvironment('host', defaultValue: "http://localhost:8080");

    http.Response res =
        await request.post(host + '/api/v1/appointment/add', body);

    return res;
  }

  final ButtonStyle style = ElevatedButton.styleFrom(
      padding: const EdgeInsets.all(20),
      textStyle: const TextStyle(color: Colors.white),
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(20),
      ));

  @override
  void initState() {
    _dateController.text = DateFormat.yMd().format(DateTime.now());

    _timeController.text = formatDate(
        DateTime(2019, 08, 1, DateTime.now().hour, DateTime.now().minute),
        [hh, ':', nn, " ", am]).toString();
    final request = context.read<CookieRequest>();
    fetchListDokter(request);

    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    final request = context.read<CookieRequest>();

    _height = MediaQuery.of(context).size.height;
    _width = MediaQuery.of(context).size.width;
    dateTime = DateFormat.yMd().format(DateTime.now());
    return Scaffold(
        appBar: AppBar(
          title: const Text("Membuat Appointment"),
        ),
        body: Padding(
          padding: const EdgeInsets.all(3.0),
          child: Column(
            children: [
              const SizedBox(
                height: 35,
              ),
              Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 20),
                  child: Container(
                      alignment: Alignment.center,
                      padding: const EdgeInsets.all(10),
                      child: const Text(
                        'Buat Appointment',
                        style: TextStyle(
                            fontSize: 20, fontWeight: FontWeight.bold),
                      ))),
              const SizedBox(
                height: 20,
              ),
              const Text(
                'Tanggal Mulai',
                style: TextStyle(
                    fontStyle: FontStyle.italic,
                    fontWeight: FontWeight.w600,
                    letterSpacing: 0.5),
              ),

              InkWell(
                onTap: () {
                  _selectDate(context);
                },
                child: Container(
                  width: _width / 1.7,
                  height: _height / 9,
                  alignment: Alignment.center,
                  decoration: BoxDecoration(color: Colors.grey[200]),
                  child: TextFormField(
                    style: const TextStyle(fontSize: 40),
                    textAlign: TextAlign.center,
                    enabled: false,
                    keyboardType: TextInputType.text,
                    controller: _dateController,
                    onSaved: (String? val) {
                      _setDate = val!;
                    },
                    decoration: const InputDecoration(
                        disabledBorder:
                            UnderlineInputBorder(borderSide: BorderSide.none),
                        contentPadding: EdgeInsets.only(top: 0.0)),
                  ),
                ),
              ),
              const SizedBox(
                height: 20,
              ),
              const Text(
                'Waktu Mulai',
                style: TextStyle(
                    fontStyle: FontStyle.italic,
                    fontWeight: FontWeight.w600,
                    letterSpacing: 0.5),
              ),
              InkWell(
                onTap: () {
                  _selectTime(context);
                },
                child: Container(
                  width: _width / 1.7,
                  height: _height / 9,
                  alignment: Alignment.center,
                  decoration: BoxDecoration(color: Colors.grey[200]),
                  child: TextFormField(
                    style: const TextStyle(fontSize: 40),
                    textAlign: TextAlign.center,
                    onSaved: (String? val) {
                      _setTime = val!;
                    },
                    enabled: false,
                    keyboardType: TextInputType.text,
                    controller: _timeController,
                    decoration: const InputDecoration(
                        disabledBorder:
                            UnderlineInputBorder(borderSide: BorderSide.none),
                        contentPadding: EdgeInsets.all(5)),
                  ),
                ),
              ),

              const SizedBox(
                height: 20,
              ),

              const Text(
                'Dokter',
                style: TextStyle(
                    fontStyle: FontStyle.italic,
                    fontWeight: FontWeight.w600,
                    letterSpacing: 0.5),
              ),

              DropdownButton<String>(
                dropdownColor: Colors.grey,
                icon: const Icon(Icons.arrow_drop_down),
                iconSize: 24,
                isExpanded: false,
                underline: const SizedBox(),
                style: const TextStyle(color: Colors.black, fontSize: 16),
                value: _dokter,
                items: data.map((item) {
                  return DropdownMenuItem(
                    child: Text(item['nama'] + " - " + item['tarif'].toString()),
                    value: item['username'].toString(),
                  );
                }).toList(),
                onChanged: (String? newValue) {
                  setState(() {
                    _dokter = newValue!;
                  });
                },
              ),

              Padding(
                padding: const EdgeInsets.symmetric(vertical: 16.0),
                child: ElevatedButton(
                  style: style,
                  onPressed: () async {
                    var response = await attemptAddAppointment(request,
                        _dateController.text, _timeController.text, _dokter);
                    if (response.statusCode == 200) {
                      displayDialog(context, "Add Appointment success",
                          "Add Appointment success");
                      Navigator.of(context).pushReplacement(MaterialPageRoute(
                            builder: (context) => const ListAppointmentPage(),
                          ));
                    } else if (response.statusCode == 400) {
                      displayDialog(context, "An Error Occurred",
                          "Terdapat jadwal appointment yang bertabrakan pada dokter yang dipilih.");
                    } else if (response.statusCode == 403) {
                      displayDialog(context, "An Error Occurred", "Forbidden");
                    } else {
                      displayDialog(context, "An Error Occurred",
                          "No account was found matching that username and password");
                    }
                  },
                  child: const Text(
                    'Submit',
                  ),
                ),
              ),
            ],
          ),
        ));
  }
}
