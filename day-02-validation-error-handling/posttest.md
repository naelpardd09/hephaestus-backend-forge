# Posttest - Validation & Error Handling

## Objective

Posttest ini digunakan untuk mengukur pemahaman peserta setelah mempelajari Validation & Error Handling.

### 1. Apa itu validasi request?

Jawaban:

```text
untuk memvalidasi request yang diminta```

### 2. Kenapa backend tetap perlu validasi walaupun frontend sudah validasi?

Jawaban:

```text
menjaga keamanan```

### 3. Apa fungsi @Valid?

Jawaban:

```text
untuk memvalidasi request body```

### 4. Apa fungsi @NotBlank?

Jawaban:

```text
untuk agar tidak null```

### 5. Apa perbedaan @NotBlank dan @NotNull?

Jawaban:

```text
notnull itu wajib diisi```

### 6. Apa fungsi @Email?

Jawaban:

```text
@email itu untuk validasi email nya apakah well-written atau tidak```

### 7. Apa fungsi @Size?

Jawaban:

```text
untuk mengatur minimum dan maximum char```

### 8. Apa yang terjadi jika request gagal validasi?

Jawaban:

```text
ada error response```

### 9. Apa itu MethodArgumentNotValidException?

Jawaban:

```text
args nya tidak valid```

### 10. Apa itu standard error response?

Jawaban:

```text
400,500```

### 11. Kenapa error response perlu konsisten?

Jawaban:

```text
agar memberikan jawaban yang konsisten bagi client```

### 12. Apa itu field-level error?

Jawaban:

```text
error pada level fieldnya seperti fullname dkk```

### 13. Apa itu custom exception?

Jawaban:

```text
exception yg di custom response exceptionnya```

### 14. Kenapa CustomerNotFoundException lebih baik daripada RuntimeException biasa?

Jawaban:

```text
lebih spesifik daripada bawaan```

### 15. Apa fungsi @ControllerAdvice?

Jawaban:

```text
Tulis jawaban di sini.
```

### 16. Apa fungsi @ExceptionHandler?

Jawaban:

```text
untuk menghandle exception```

### 17. Kenapa error handling sebaiknya centralized?

Jawaban:

```text
supaya tidak redundan```

### 18. Kapan menggunakan 400 Bad Request?

Jawaban:

```text
apabila inputannya gak sesuai tipe data```

### 19. Kapan menggunakan 404 Not Found?

Jawaban:

```text
ketika GET tidak ada yang diinput```

### 20. Kapan menggunakan 500 Internal Server Error?

Jawaban:

```text
ketika ada salah pada code```

### 21. Kenapa stack trace tidak boleh dikirim ke client?

Jawaban:

```text
Tulis jawaban di sini.
```

### 22. Jelaskan flow ketika POST /api/v1/customers menerima email invalid.

Jawaban:

```text
biasanya ada error seperti 500```

### 23. Jelaskan flow ketika GET /api/v1/customers/999 tidak menemukan data.

Jawaban:

```text
kalau tidak ada data 404 not found```

### 24. Apa perbedaan validation error, business error, dan system error?

Jawaban:

```text
valid error itu ketika disebabkan oleh validitas, business itu logis nya, sistem itu kdoe```

### 25. Bagian mana yang paling sulit dari exercise Day 2?

Jawaban:

```text
semuanyaaaa```

## Reflection

Apa 3 hal utama yang kamu pahami hari ini?

```text
1. error
2. exception
3. handling
```

Apa 2 hal yang masih membingungkan?

```text
1. error handling
2. exception handling
```

Apa 1 pertanyaan untuk mentor?

```text
sabar-sabar pak menghadapi saya.```
