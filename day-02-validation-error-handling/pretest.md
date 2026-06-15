# Pretest - Validation & Error Handling

## Objective

Pretest ini digunakan untuk mengukur pemahaman awal peserta tentang validasi request dan error handling pada REST API.

## Instructions

- Jawab dengan singkat dan jelas.
- Tidak perlu membuka dokumentasi.
- Tidak dinilai hanya dari benar atau salah, tetapi dari cara berpikir.
- Estimasi waktu: 20-30 menit.

## Section A - Validation Basic

### 1. Apa itu validasi request?

Jawaban:

```text
Untuk memvalidasi request dari client ke server
```

### 2. Kenapa backend tetap perlu melakukan validasi walaupun frontend sudah melakukan validasi?

Jawaban:

```text
Untuk memperketat security
```

### 3. Apa risiko jika API menerima data kosong atau format data yang salah?

Jawaban:

```text
Kemungkinan ngehit berkali2 namun sia2
```

### 4. Sebutkan contoh validasi untuk field full_name.

Jawaban:

```text
kalau setau saya di java kayak mismatch exception jadi misal variable fieldnya adalah String tapi
inputannya int dia bakal error
```

### 5. Sebutkan contoh validasi untuk field email.

Jawaban:

```text
misal tanpa @ bakal invalid
```

### 6. Sebutkan contoh validasi untuk field phone_number.

Jawaban:

```text
misalkan client masukinnya text semua
```

### 7. Apa perbedaan validasi teknis dan validasi bisnis?

Jawaban:

```text
menurut sya validasi teknis ini lebih ke backend nya dan bisnis ini lebih ke money nya
```

## Section B - Bean Validation

### 8. Apa fungsi annotation @Valid?

Jawaban:

```text
untuk validasi
```

### 9. Apa fungsi annotation @NotBlank?

Jawaban:

```text
memastikan inputan tidak koosong```

### 10. Apa fungsi annotation @NotNull?

Jawaban:

```text
untuk memvalidasi inputan tidak boleh kosong
```

### 11. Apa fungsi annotation @Email?

Jawaban:

```text
untuk memberikan validasi email
```

### 12. Apa fungsi annotation @Size?

Jawaban:

```text
untuk memastikan jumlah character```

### 13. Apa perbedaan @NotBlank dan @NotNull?

Jawaban:

```text
notblank itu supaya tidak kosong, notnull supaya tidak null```

### 14. Di Spring Boot, validasi biasanya diletakkan di object apa?

Jawaban:

```text
Field
```

## Section C - Error Handling

### 15. Apa itu error handling?

Jawaban:

```text
Mengelola error```

### 16. Kenapa error response perlu dibuat konsisten?

Jawaban:

```text
Agar transparan dengan user```

### 17. Apa risiko jika stack trace dikirim ke client?

Jawaban:

```text
Client tidak tahu log errornya dmn```

### 18. Apa perbedaan HTTP status 400, 404, dan 500?

Jawaban:

```text
kepala 4 itu bad request, kepala 5 itu internal server```

### 19. Kapan menggunakan 400 Bad Request?

Jawaban:

```text
ketika inputan invalid```

### 20. Kapan menggunakan 404 Not Found?

Jawaban:

```text
data nya gaada```

### 21. Kapan menggunakan 500 Internal Server Error?

Jawaban:

```text
buat handle except```

## Section D - Exception

### 22. Apa itu exception?

Jawaban:

```text
try catch, throw exception itu pengeculian error```

### 23. Apa itu RuntimeException?

Jawaban:

```text
jenis exception 
```

### 24. Apa itu custom exception?

Jawaban:

```text
exception yang diatur oleh client```

### 25. Kenapa kita perlu membuat CustomerNotFoundException?

Jawaban:

```text
membuat pengeculian kalau customer tidak ditemukan```

### 26. Apa perbedaan validation error, business error, dan system error?

Jawaban:

```text
penyebab error, karena validitas tidak valid, karena logika bisnis, dan karena kode/sistem```

## Section E - Global Exception Handler

### 27. Apa itu @ControllerAdvice?

Jawaban:

```text
membuat fungsi pada Controller
```

### 28. Apa itu @ExceptionHandler?

Jawaban:

```text
mengelola exception```

### 29. Kenapa error handling sebaiknya tidak ditulis berulang di setiap Controller?

Jawaban:

```text
supaya tidak redundan
```

### 30. Apa manfaat centralized error handling?

Jawaban:

```text
tidak redundan dan lebih mudah di kelola```

## Self Assessment

| Area | Score 1-5 |
| --- | --- |
| Request validation | |
| Bean Validation | |
| HTTP status code | |
| Exception | |
| Custom exception | |
| Global error handling | |
| Standard error response | |

## Notes

```text
error handling dan exception in api```
