# Pretest - API Contract, API Testing & Swagger

## Objective

Pretest ini digunakan untuk mengukur pemahaman awal peserta tentang API contract, API testing, dan Swagger.

## Instructions

- Jawab dengan singkat dan jelas.
- Tidak perlu membuka dokumentasi.
- Tidak dinilai hanya dari benar atau salah, tetapi dari cara berpikir.
- Estimasi waktu: 20-30 menit.

## Section A - API Contract

### 1. Apa itu API contract?

Jawaban:

```text
Kontrak API adalah kontrak yang dibuat untuk mendeclare fungsi api dan batasannya sampe mana endpoint dan starting pointnya
```

### 2. Kenapa API contract penting?

Jawaban:

```text
karena agar api itu tidak 
```

### 3. Apa saja isi API contract?

Jawaban:

```text
REQUIREMENT, CONTRACT DESIGN, IMPLEMENTATION, TESTING, SWAGGER VISIBILITY```

### 4. Apa itu endpoint?

Jawaban:

```text
titik akhir```

### 5. Apa itu HTTP method?

Jawaban:

```text
metode menggunakan http```

### 6. Apa itu request body?

Jawaban:

```text
isi body request berdasarkan field```

### 7. Apa itu response body?

Jawaban:

```text
body berisi respon dari proses rqeuest yang dieskekusi```

### 8. Apa itu HTTP status code?

Jawaban:

```text
tampilan angka dan penjelasan error/success yang disederhanakan agar mudah dimengerti user sebab nya```

### 9. Kenapa request dan response perlu ditulis jelas?

Jawaban:

```text
biar user tau sebab akibat suatu proses```

### 10. Apa risiko jika API contract tidak jelas?

Jawaban:

```text
api gada limitasi yang jelas```

## Section B - DTO

### 11. Apa itu DTO?

Jawaban:

```text
data transfer object```

### 12. Apa itu request DTO?

Jawaban:

```text
request yang dikirim services```

### 13. Apa itu response DTO?

Jawaban:

```text
isi respon dari dto```

### 14. Kenapa DTO dan model sebaiknya dipisah?

Jawaban:

```text
agar tidak tumpang tindih```

### 15. Kenapa JSON biasanya menggunakan snake_case sedangkan Java menggunakan camelCase?

Jawaban:

```text
udah aturan mainnya```

### 16. Apa fungsi @JsonProperty?

Jawaban:

```text
untuk menambahkan standar untuk variable field```

## Section C - HTTP Method

### 17. Apa fungsi POST?

Jawaban:

```text
mengupload data baru```

### 18. Apa fungsi GET?

Jawaban:

```text
mengambil data yang ada```

### 19. Apa fungsi PUT?

Jawaban:

```text
mengubah data yang ada semua varibale dalam 1 objhect```

### 20. Apa fungsi PATCH?

Jawaban:

```text
mengubah data per variable dalam 1 object```

### 21. Apa perbedaan PUT dan PATCH?

Jawaban:

```text
lingkup request bodynya kalo put harus semua var kalo patch bisa per var```

### 22. Kapan menggunakan 201 Created?

Jawaban:

```text
ketika post berhasil di eksekusi```

### 23. Kapan menggunakan 200 OK?

Jawaban:

```text
ketika GET berhasil mendapatkan data yang dimau```

### 24. Kapan menggunakan 400 Bad Request?

Jawaban:

```text
invalid input```

### 25. Kapan menggunakan 404 Not Found?

Jawaban:

```text
data nya gak ditemukan```

## Section D - API Testing

### 26. Apa itu API testing?

Jawaban:

```text
melakukan pengujian pada efektivitas api```

### 27. Kenapa API perlu dites?

Jawaban:

```text
mengetes request dan response body, http status code, menguji url api dkk```

### 28. Tool apa yang biasa digunakan untuk API testing?

Jawaban:

```text
postman```

### 29. Apa yang perlu dicek saat melakukan API testing?

Jawaban:

```text
directory eksekusi, get/put/post/patch nya, framework springnya dkk```

### 30. Apa itu expected response?

Jawaban:

```text
respon yang diekspektasikan```

## Section E - Swagger

### 31. Apa itu Swagger?

Jawaban:

```text
kayak postman```

### 32. Apa itu OpenAPI?

Jawaban:

```text
induk nya API```

### 33. Apa manfaat Swagger UI?

Jawaban:

```text
mirip postman```

### 34. Apa bedanya Postman dan Swagger UI?

Jawaban:

```text
postman buat testing api, swagger buat dokumentasi api```

### 35. Menurut kamu, apakah Swagger bisa menggantikan dokumentasi API manual? Jelaskan.

Jawaban:

```text
tergantung kebutuhan dan kapasitas```

## Self Assessment

| Area | Score 1-5 |
| --- | --- |
| API contract | |
| DTO | |
| HTTP method | |
| API testing | |
| Swagger UI | |
| OpenAPI | |

## Notes

```text
logic terkadang butuh pendalaman```
