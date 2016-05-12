
# CMPE273 Project Submission

Android_Client/ - Contails the source dode for the Android App

Backend/ - Contains the source code for the Backend

Web_Client/ - Contains the sourcecode of the Web Client

Architecture.png - Architecture Diagram for the project

Each Folder has its own README file. Please use them for further reference.



# Project Requirements

## Automatic Class Attendance System

Automed time and attendance tracking system shall help save time and money by eliminating manual processes. With the system, teachers can more accurately and quickly see whether a student is in the class or not.

### Requirement

#### BLE Sensor
You need one of the hardware for the project.
* 1. [Arduino BLE breakout]
* 2. Raspberry Pi with [BLE Adapter]
* 3. [Raspberry Pi 3 with Bluetooth (BLE) built-in]


#### Mobile App (Android or iOS)
You will also need to write some code in either iOS or Android to communite with BLE chip and your backend server.

#### Backend Server
The backend server will handle the business logic of tracking student attendance and will also have a set of REST APIs for moible app or Raspberry Pi client to communicate.

The backend server must use either NOSQL including key-value store or RDBMS to store the data.

One single web page that shows all student attendance for teachers is required.

