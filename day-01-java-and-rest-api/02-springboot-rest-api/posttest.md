# Posttest - Spring Boot REST API

Jawab pertanyaan berikut setelah membaca materi dan membuat Customer REST API.

### 1. Apa itu Spring Boot?

Jawaban:

```text
Framework java untuk membuat backend apalagi dengan API```

### 2. Apa fungsi Spring Initializr?

Jawaban:

```text
Website Pembuat project spring boot yang otomatis dan bisa add dependency seperti swagger postman dkk```

### 3. Saat membuat project, kenapa memilih Maven?

Jawaban:

```text
karena diajarinnya pake maven dan lebih amateur-friendly mnrt saya.```

### 4. Apa fungsi dependency Spring Web?

Jawaban:

```text
Ibarat nya basis yang membolehkan rest api kita berjalan seperti Rest Controller, Mapping, dll```

### 5. Kenapa Day 1 belum menambahkan dependency database?

Jawaban:

```text
Karena masih memahami konsep```

### 6. Apa fungsi file pom.xml?

Jawaban:

```text
Semacam konfig file maven berisi library. Mirip2 build.gradle.kts```

### 7. Apa fungsi TrainingApplication.java?

Jawaban:

```text
Letak class main untuk running apk```

### 8. Apa fungsi @SpringBootApplication?

Jawaban:

```
Declare class sebagai main config
```

### 9. Bagaimana cara menjalankan Spring Boot dari IDE?

Jawaban:

```text
pake mvn clean:compiler lalu mvn jetty:run```

### 10. Bagaimana cara menjalankan Spring Boot dari terminal?

Jawaban:

```text
Tulis jawaban di sini.
```

### 11. Jika membuka http://localhost:8080 dan hasilnya 404, apakah selalu error? Jelaskan.

Jawaban:

```text
bisa jadi endpoint blm ada```

### 12. Apa itu REST API?

Jawaban:

```text
cara komunikasi antara klien menggunakan protokol http```

### 13. Apa itu endpoint?

Jawaban:

```text
url spesifik untuk menerima request, cth. api/v3/customers```

### 14. Apa perbedaan request dan response?

Jawaban:

```text
request itu data dari client -> server, response kembalian server -> client```

### 15. Kenapa JSON menggunakan snake_case, sedangkan Java menggunakan camelCase?

Jawaban:

```text
standarnya seperti itu
```

### 16. Apa fungsi @JsonProperty?

Jawaban:

```text
untuk memetakan nama field di java```

### 17. Apa tugas Controller?

Jawaban:

```text
menerima request http dari client```

### 18. Apa tugas Service?

Jawaban:

```text
menjalankan logika bisnis```

### 19. Apa itu DTO?

Jawaban:

```text
data transfer object untuk membawa data antar layer client dan server```

### 20. Apa itu Model?

Jawaban:

```text
represetnasi struktur data seperti id, fullname, dkk```

### 21. Kenapa request body tidak langsung menggunakan model?

Jawaban:

```text
karena ada dto dlu```

### 22. Kenapa response tidak langsung menggunakan model?

Jawaban:

```text
karena ada field model yang harus di filter dlu```

### 23. Kenapa business logic tidak boleh ditaruh di Controller?

Jawaban:

```text
porsi controller sebagai pintu masuk saja```

### 24. Kapan menggunakan 200 OK?

Jawaban:

```text
request dan response berhasil```

### 25. Kapan menggunakan 201 Created?

Jawaban:

```text
user craeted successfully```

### 26. Apa bedanya path parameter dan query parameter?

Jawaban:

```text
path itu bagian dari url kayak api/v3/customers/5, angka 5 itu pathnya. untuk identifikasi resource```

### 27. Jelaskan flow POST /api/v1/customers dari request sampai response.

Jawaban:

```text
client ngirim http post ke api/v1/customers dengan json, request masuk ke customercontroller, method createcustomer menerima, controller memanggil customerService.createCustomer(), service create new object Customer, simpan ke customerStroage, lalu loading object CustomerResponse dari data yang tersimpan, CustomerResponse lalu di return ke Controller dalam bentuk response, Controller nge-wrap si response dengan 201 Created nya ke client, lalu at the end client nerima response dalam bentuk JSON berisi data cust baru```

### 28. Bagian mana yang paling sulit saat membuat Spring Boot REST API?

Jawaban:

```text
pelajarin logic sih jujur```

### 29. Error apa yang kamu temui dan bagaimana cara menyelesaikannya?

Jawaban:

```text
kemaren ketika run springnya error sampe ada 35 commands saya buat baru akhirnya```

### 30. Apa yang ingin kamu pelajari berikutnya?

Jawaban:

```text
masih mau perdalam rest api sama spring```
