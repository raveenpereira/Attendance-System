##School Attendance System using Bluetooth Low Energy


The aim of the project is to design an attendance system for schools which utilizes a Raspberry Pi or BLE chip and the student's Mobile phone for authentication and marking attendance. 

###Components:

* Web UI – For School Administration
* Mobile Client – For the students
* Back End Server – The backend server provides a list of all APIs to be used by Web and Mobile client
* Couch DB – The database used by the Backend server to store database
* Raspberry Pi – This has an inbuilt BLE Sensor and will be fixed to the entrance of the classroom



###Flow :

####General - 

* Use InitializeCouchDB.go to initialize and set up your freshly installed CouchDb instance

* The Administration uses the web UI to create classes.
* The  Administration uses the web UI to Add students to the school

* The Student installs the mobile client app and registers into the app
* The Student will bring the mobile phone near the BLE sensor in front of the classroom
* This Student will get receive a notification asking him to mark himself present.

* Once the Student marks himself present, the Administration can use the web UI to view the class attendance.

* The Administration can add and delete class and students on the fly from the web UI


####Student Changes Mobile Device -

* Student has to be added by the Administration
* Student Registers using a password on the mobile device
* If the student wishes to change the device, there is an option on the mobile 	application where the student enters her password again and sends the delete request.
* The student acquires another device and registers from that device again.




###BackEnd Server 

The server consists of 4 files. 

* ClassAttendanceListAPI -
	This file contains the API to be used for School Administration.
* StudentListDatabaseAPI -
	This file contains the API to be used for School Administration  	
* StudentProfileDatabaseAPI
	This file contains the API to be used by the students on the mobile client
* InitializeCouchDB -
	This script completely initializes the newly installed Couch DB server with the required Database names and Structures. Hence you dont need to rely on a preprepared and pre setup  Database and you can convert any freshly installed CouchDB database to support your system.

###API Documentation

The Documentation consists of the following files:

* CouchDB API Documentation – The backend Couch Databases each have a design document with their own views and Map functions inserted into them when executing the InitializeCouchDB.go file. This document completely covers the structure of the database and documents all the APIs that the GO backend uses. 

* REST Api Documentation – This documents completely covers all the API endpoints exposed by the GO backend. These are the APIs the mobile client and the web UI should be using. This backend will use the CouchDB apis to provide functionality.

* SettingUpCouchDBandBackend – This document completely explains how to use InitializeCouchDB.go file to set up your CouchDB database.

* WorkFlow – This document explains the general workflow of using the application once deployed.



These documents will explain everything for you. You have to go through all of the documentation to completely understand the backend. The backend does a lot of automation by calling the APIs itself to make things easier for the mobile and Web Clients




###Demo

During the demo in class, we installed our GO servers and CouchDB servers in EC2 instances and load balanced between them. The instannces since then have been terminated and IPs are no longer valid.
