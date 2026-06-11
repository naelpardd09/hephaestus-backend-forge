# Day 1 - Java Fundamental & Spring Boot REST API

Day 1 dibagi menjadi dua modul:

1. Java Fundamental
2. Spring Boot REST API

Java Fundamental perlu dipahami terlebih dahulu sebelum masuk ke Spring Boot. Spring Boot tetap menggunakan class, object, variable, method, collection, dan konsep OOP dari Java. Annotation seperti `@RestController` atau `@Service` hanya menambahkan perilaku Spring di atas class Java biasa.

Modul Spring Boot REST API akan menggunakan konsep yang sama dari exercise Java, yaitu pengelolaan data customer sederhana. Bedanya, program akan diakses melalui HTTP endpoint dan dites menggunakan Postman.

## 1. Goal

Setelah menyelesaikan Day 1, peserta diharapkan mampu:

- Membuat program customer management sederhana dengan plain Java.
- Membuat Customer REST API sederhana menggunakan Spring Boot.
- Memahami peran Controller, Service, DTO, dan Model.
- Memahami alur request-response pada REST API.

## 2. Why This Day Is Important

Backend engineer yang menggunakan Spring Boot tetap perlu memahami Java. Jika peserta belum paham variable, class, object, method, constructor, getter, setter, `List`, dan `Map`, maka kode Spring Boot akan terasa seperti kumpulan annotation tanpa dasar yang jelas.

Day ini dibuat untuk membangun dasar tersebut secara bertahap. Pertama, peserta membuat program Java biasa. Setelah itu, konsep yang sama dipakai lagi dalam bentuk REST API.

## 3. Module Structure

```text
day-01-java-and-rest-api/
├── README.md
├── 01-java-fundamental/
│   ├── pretest.md
│   ├── materi.md
│   ├── exercise.md
│   └── posttest.md
└── 02-springboot-rest-api/
    ├── pretest.md
    ├── materi.md
    ├── exercise.md
    └── posttest.md
```

## 4. Learning Flow

Ikuti urutan belajar berikut:

1. Kerjakan `01-java-fundamental/pretest.md`.
2. Baca `01-java-fundamental/materi.md`.
3. Kerjakan `01-java-fundamental/exercise.md`.
4. Kerjakan `01-java-fundamental/posttest.md`.
5. Kerjakan `02-springboot-rest-api/pretest.md`.
6. Baca `02-springboot-rest-api/materi.md`.
7. Kerjakan `02-springboot-rest-api/exercise.md`.
8. Kerjakan `02-springboot-rest-api/posttest.md`.

## 5. Expected Output

Di akhir Day 1, peserta diharapkan memiliki:

- Program plain Java untuk customer management.
- Spring Boot Customer REST API sederhana.
- Pemahaman dasar tentang Controller, Service, DTO, dan Model.
- Pemahaman alur request dari client sampai response kembali ke client.

## 6. What Is Not Covered Today

Day 1 belum membahas:

- Database
- Detail validation
- Detail error handling
- Security
- Redis
- Deployment

Semua data pada Day 1 disimpan sementara di memory menggunakan `Map` atau `List`.
