# HealthBankApi
Use Mongodb as database
## User Data Foramt
```
  {
    personId: String,
    password: String,
    personalInfo:[
      Name: String,
      personId: String
    ],
    hospitalInfo: [
      {
        id: Int,
        diseaseId : String,
        diseaseName: String,
        medicalDate: Int,
        hospitalId: String,
        hospitalName: String
      }
      ...
    ],
    westernMedicine: [
      {
        id: Int,
        diseaseId: String,
        medicaldate: Int,
        hospitalId: String,
        hospitalName: String
      },
      ...
    ],
    medicineUsage: [
      {
        id: Int,
        medId: String,
        medName: String,
        amount: float,
        days: Int,
        medicalDate: Int
      }
      ...
    ],
  }
```
## Install required module
```
$ npm install
```
## Start Server
```
$ npm start
```
