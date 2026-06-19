# Posttest - Authentication, Authorization & RBAC

## Objective

Posttest ini digunakan untuk mengukur pemahaman peserta setelah mempelajari Authentication, Authorization, JWT, RBAC, dan resource-level authorization.

### 1. Apa itu authentication?

Jawaban:

```text
Proses mengenali suatu program
```

### 2. Apa itu authorization?

Jawaban:

```text
Proses memverifikasi/menolak suatu program
```

### 3. Apa perbedaan authentication dan authorization?

Jawaban:

```text
Authenticate itu terjadi sebelum authorization.
```

### 4. Kenapa user yang sudah login belum tentu boleh melakukan semua action?

Jawaban:

```text
Ada validasi role, apakah role tsb memenuhi semua atau action tertentu saja, itu harus di perjelas logicnya.
```

### 5. Apa itu token-based authentication?

Jawaban:

```text
Autentikasi berdasarkan token yang di-generate
```

### 6. Apa fungsi Authorization header?

Jawaban:

```text
Sebagai komponen request HTTP yang dipakai client utk mengirim kredensial
```

### 7. Apa arti Bearer token?

Jawaban:

```text
Jenis access token yang ada di Auth Header
```

### 8. Apa itu JWT?

Jawaban:

```text
JSON Web Token (JavaScript Object Notation Web Token) adalah standar terbuka yg digunakan utk mengirim informasi antara client dan server as JSON.
```

### 9. Apa itu claim pada JWT?

Jawaban:

```text
Informasi pada payload yg tiulis dalam bentuk key-value JSON```

### 10. Sebutkan 4 claim yang umum ada pada JWT.

Jawaban:

```text
Subject, expiration time, issued at, issuer```

### 11. Kenapa JWT payload tidak boleh dipercaya sebelum signature divalidasi?

Jawaban:

```text
untuk alasan keamanan data
```

### 12. Data apa saja yang tidak boleh disimpan di JWT?

Jawaban:

```text
password```

### 13. Kenapa token perlu expiry?

Jawaban:

```text
Supaya ga di eksploitasi dan bisa diakses sembarangan
```

### 14. Apa perbedaan access token dan refresh token?

Jawaban:

```text
access token dipakai utk akses API , sementara refresh token itu untuk minta access token baru yg sudah expired```

### 15. Apa itu RBAC?

Jawaban:

```text
role-based access control itu ya simplenya adalah pembatasan akses user berdasarkan role mereka. cth. user gabisa approve loan```

### 16. Apa perbedaan role dan permission?

Jawaban:

```text
role itu ya status penggunanya sebagai siapa, permission itu limitasi akses beliau```

### 17. Berikan contoh role dalam loan system.

Jawaban:

```text
approver, admin, user, manager```

### 18. Berikan contoh permission dalam loan system.

Jawaban:

```text
approvement, createLoan```

### 19. Kenapa role check saja tidak cukup?

Jawaban:

```text
tentunya tidak secure```

### 20. Apa itu resource-level authorization?

Jawaban:

```text
object-level access control atau mekanisme keamanan yang menentukan apakah user tsb diizinkan utk mengakses/memanipulasi data
```

### 21. Apa itu ownership check?

Jawaban:

```text
pengecekan kepemilikan adalah proses validasi di dalam kode aplikasi utk memastikan user login adalah user sah/tdk ```

### 22. Apa itu IDOR?

Jawaban:

```text
insecure direct object ref adalah jenis celah pada web/api yang terjadi ketika sistem lgsg memberikan akses ke suatu data based on ID tanpa verifikasi role```

### 23. Bagaimana cara mencegah customer melihat data customer lain?

Jawaban:

```text
RBAC, RLA, Ownership check```

### 24. Kapan menggunakan 401 Unauthorized?

Jawaban:

```text
rbac nya gagal```

### 25. Kapan menggunakan 403 Forbidden?

Jawaban:

```text
role tertentu melakukan pencobaan akses yang tidak di authorized```

### 26. Apa perbedaan 401 dan 403?

Jawaban:

```text
401 itu role nya tidak teruatoikasi, 403 itu dilarang ```

### 27. Kenapa error message security tidak boleh terlalu detail?

Jawaban:

```text
ya jangan sampai nampilin password dkk nya```

### 28. Apa itu principle of least privilege?

Jawaban:

```text
artinya tiap role itu hanya boleh diberikan hak akses seminimal mungkin
```

### 29. Kenapa access log penting dalam finance backend?

Jawaban:

```text
ya disitu ada data pribadi konsumen, bisa jd vulnerability yg berbahaya jika gaada keamanan```

### 30. Sebutkan field penting dalam access log.

Jawaban:

```text
password, token, data pribadi konsumen```

### 31. Bagaimana auth requirement ditulis di API contract?

Jawaban:

```text
di controller, service, dan dto```

### 32. Bagaimana Swagger/OpenAPI membantu dokumentasi endpoint yang protected?

Jawaban:

```text
dengan memvisualisasikan ```

### 33. Apa risiko jika role dikirim dari client lalu langsung dipercaya?

Jawaban:

```text
ya bisa jd client nakal dan diberikan overaccess```

### 34. Apa risiko token tanpa expiry?

Jawaban:

```text
token bisa diakses kapanpun dan rawan leak.```

### 35. Bagian mana yang paling sulit dari Day 4?

Jawaban:

```text
jujur SUSAH```

## Reflection

Apa 3 hal utama yang kamu pahami hari ini?

```text
1. Authorization is important
2. authorization memang ribet dan susah karena kalo ga begitu jd rawan disadap
3.
```

Apa 2 hal yang masih membingungkan?

```text
1. auth
2. auth
```

Apa 1 pertanyaan untuk mentor?

```text
bagaimana sih auth testing otomatis itu```
