package main

import (
	"net/http"
	"strings"
)

var BaseUrl string
var DesignDocStudentList string
var DesignDocStudentProfile string
var DesignDocClassList string
var DesignDocBluetoothId string

func createStudentList(){
	//Create DB
	Url:= BaseUrl+"/studentlist"
	request, _ := http.NewRequest("PUT", Url, nil)
	client := &http.Client{}
	client.Do(request)

	//Insert Design Doc
	Url= BaseUrl+"/studentlist"+"/_design/getlistdata"
	request, _ = http.NewRequest("PUT", Url, strings.NewReader(DesignDocStudentList))
	client = &http.Client{}
	client.Do(request)
}


func createStudentProfile(){
	//Create DB
	Url:= BaseUrl+"/studentprofile"
	request, _ := http.NewRequest("PUT", Url, nil)
	client := &http.Client{}
	client.Do(request)

	//Insert Design Doc
	Url= BaseUrl+"/studentprofile"+"/_design/studentdetails"
	request, _ = http.NewRequest("PUT", Url, strings.NewReader(DesignDocStudentProfile))
	client = &http.Client{}
	client.Do(request)
}


func createClassList(){
	//Create DB
	Url:= BaseUrl+"/classlist"
	request, _ := http.NewRequest("PUT", Url, nil)
	client := &http.Client{}
	client.Do(request)

	//Insert Design Doc
	Url= BaseUrl+"/classlist"+"/_design/getclassdata"
	request, _ = http.NewRequest("PUT", Url, strings.NewReader(DesignDocClassList))
	client = &http.Client{}
	client.Do(request)
}

func createBluetoothId(){
	//Create DB
	Url := BaseUrl+"/bluetoothid"
	request, _ := http.NewRequest("PUT", Url, nil)
	client := &http.Client{}
	client.Do(request)

	//Insert Design Doc
	Url= BaseUrl+"/bluetoothid"+"/_design/getbluetoothid"
	request, _ = http.NewRequest("PUT", Url, strings.NewReader(DesignDocBluetoothId))
	client = &http.Client{}
	client.Do(request)
}

func main(){

	//BASE URL for CouchDB. Curling this URL should give a welcome message
	BaseUrl=""

	DesignDocStudentList=`{"_id": "_design/getlistdata","views": {"studentname": {"map": "function(doc){ emit(doc.studentid, doc.studentname)}"},"studentenrolled": {"map": "function(doc){ emit(doc.studentid, doc.regclasses)}"}}}`
	DesignDocStudentProfile=`{"_id": "_design/studentdetails","views": {"studentregistered": {"map": "function(doc){ emit(doc.studentid)}"},"studentpassword": {"map": "function(doc){ emit(doc.studentid, doc.password)}"}}}`
	DesignDocClassList=`{"_id": "_design/getclassdata","views": {"classexists": {"map": "function(doc){ emit(doc.classnumber, doc.classname)}"}}}`
	DesignDocBluetoothId=`{"_id": "_design/getbluetoothid","views": {"bluetoothid": {"map": "function(doc){ emit(doc.classid, doc.bluetoothid)}"}}}`

	//Setup CouchDB
	createStudentList()
	createStudentProfile()
	createClassList()
	createBluetoothId()

}
