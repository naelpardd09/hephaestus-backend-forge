# Posttest - Backend Quality: Testing, Peer Code Review & Observability

## Objective

Posttest ini digunakan untuk mengukur pemahaman peserta setelah mempelajari testing mindset, unit testing, peer code review, structured logging, correlation ID, dan PII safety.

## Instructions

- Jawab dengan singkat dan jelas.
- Total pertanyaan: 10.

1. Kenapa testing disebut risk reduction?

Jawaban:

```text
karena testing ini menemukan bug, error, dll dan bisa mengurangi risiko kedepannya
```

2. Apa perbedaan working code dan trusted code?

Jawaban:

```text
working code itu kode yg udh run tp blm di tes, trusted code ini kode yg udh run, udh diuji dan verified```

3. Jelaskan pola Given-When-Then.

Jawaban:

```text
Cara nyeritain test: Given (kondisi awal apa), When (apa yang dilakukan), Then (hasil yang diharapkan). Contoh: Given customer ada, When create loan, Then loan tersimpan dengan status SUBMITTED.
Tulis jawaban di sini.
```
4. Kenapa service layer cocok untuk unit test?

Jawaban:

```text
karena service layer gakonek db
```

5. Apa peran JUnit 5 dan Mockito dalam unit test?

Jawaban:

```text
Mockito adalah alat bantu untuk isolasi unit test dari dependency eksternal. Framework Java untuk nulis dan jalanin test. Bisa mark method sebagai test, assert hasil, setup/teardown, parameterize test, dll.
```

6. Sebutkan 3 test case penting untuk `LoanApplicationService`.

Jawaban:

```text
Create Loan — Customer Valid → Success
Create Loan — Customer Not Found → Throw Exception
Create Loan — Role Unauthorized → Throw Exception
```

7. Apa tujuan peer code review?

Jawaban:

```text
Bukan cari salah, tapi: (1) bagi knowledge, (2) cek apakah kode mudah dipahami orang lain, (3) tangkap bug/edge case yang terlewat, (4) pastikan konsisten standar tim.
```

8. Apa itu structured logging dan kenapa penting?

Jawaban:

```text
Log yang format-nya rapi dan bisa di-parse mesin (biasanya JSON). Bisa difilter, dicari, di-aggregate. Beda sama log teks bebas yang susah dianalisis.
```

9. Apa fungsi `correlation_id` pada log dan error response?

Jawaban:

```text
correlation_id = ID unik yang dibawa sepanjang perjalanan satu request, error response untuk mengirim pesan error
```

10. Sebutkan minimal 5 data yang tidak boleh ditulis mentah di log.

Jawaban:

```text
NIK, riwayat medis, nama ibu, Password / token / API key, KARTU KREDIT
```

## Reflection

Apa 3 hal utama yang kamu pahami hari ini?

```text
1. tentang risk
2. tentang masalah yang biasa muncul di backend
3. tentang testing
```

Apa 2 hal yang masih membingungkan?

```text
1. flow automation testing
2.flow integration testing
```

Apa 1 hal yang akan kamu cek saat melakukan code review?

```text
backend
```
