# Pretest - Database, SQL, JPA, Hibernate, Flyway & Query Relationship

## Objective

Pretest ini digunakan untuk mengukur pemahaman awal peserta tentang database, SQL, JPA, Hibernate, Flyway, dan relationship antar table.

## Instructions

- Jawab dengan singkat dan jelas.
- Tidak perlu membuka dokumentasi.
- Tidak dinilai hanya dari benar atau salah, tetapi dari cara berpikir.
- Estimasi waktu: 20-30 menit.

## Section A - Database Basic

1. Apa itu database?

Jawaban:

```text
Basis data itu ibarat kontainer dari suatu data.
```

2. Apa perbedaan menyimpan data di Map dan menyimpan data di database?

Jawaban:

```text
Di map itu dia static, kalo database itu untuk di deploy.
```

3. Apa itu table?

Jawaban:

```text
table itu adalah struktur basis data yang berisi 1 primary key, x foreign key, unique key, dan keys lainnya.
```

4. Apa itu row?

Jawaban:

```text
row itu baris pada table
```

5. Apa itu column?

Jawaban:

```text
column itu kolom pada table
```

6. Apa itu primary key?

Jawaban:

```text
field utama dari table tersebut
```

7. Apa itu foreign key?

Jawaban:

```text
field utama dari table lain yg dipanggil ke table tertentu
```

8. Kenapa aplikasi backend membutuhkan database driver?

Jawaban:

```text
untuk sebagai media penyimpanan data pasca deploy online
```

## Section B - SQL Basic

9. Apa fungsi SELECT?

Jawaban:

```text
mirip GET, mengambil data
```

10. Apa fungsi INSERT?

Jawaban:

```text
mirip POST, mengunggah data
```

11. Apa fungsi UPDATE?

Jawaban:

```text
mirip PUT/PATCH, memperbarui data
```

12. Apa fungsi DELETE?

Jawaban:

```text
mirip DELETE di API, menghapus data
```

13. Apa fungsi WHERE?

Jawaban:

```text
Mirip fungsi if else, kayak misal SELECT nama, umur,
FROM KK1
WHERE umur>15
```

14. Apa perbedaan LIKE dan ILIKE di PostgreSQL?

Jawaban:

```text
Dua2nya fitur pencarian pencocokan kata, LIKE itu sensitif, ILIKE tidak sensitif ```

15. Apa fungsi ORDER BY?

Jawaban:

```text
Mengurutkan data
```

16. Apa fungsi LIMIT?

Jawaban:

```text
ngebatesin data yang muncul dan terlihat
```

17. Apa itu JOIN?

Jawaban:

```text
me-merge 2 table
```

18. Apa perbedaan INNER JOIN dan LEFT JOIN?

Jawaban:

```text
inner join = merge dan view baris data yang cocok (kalau di venn diagram dia yg beririsan kedua bulatan)
left join = merge dan view all data tabel kiri + bila data tabel kiri tidak ada di tabel kanan, dia akan null
```

## Section C - JPA & Hibernate

19. Apa itu JPA?

Jawaban:

```text
Java/Jakarta Persistence API, untuk mengelola dan mengakses data antar objek java dan SQL. @Entity, @Id, @Table
```

20. Apa itu Hibernate?

Jawaban:

```text
pustaka alat ORM untuk mengubah java jadi sql otomatis
```

21. Apa perbedaan JPA dan Hibernate?

Jawaban:

```text
JPA itu blueprint aturan dan annotation, hibernate itu implementasi nyata```

22. Apa itu Entity?

Jawaban:

```text
class java yang merepresentasi table di db```

23. Apa fungsi @Entity?

Jawaban:

```text
anotasi agar suatu kelas java itu dapat dikenali oleh JPA/hibernate ```

24. Apa fungsi @Table?

Jawaban:

```text
anotaasi yang menandakan suatu kode itu adalah table, mengatur detail db juga spt nama nya```

25. Apa fungsi @Id?

Jawaban:

```text
berfungsi menandai field sebagai PK
```

26. Apa fungsi @GeneratedValue?

Jawaban:

```text
mengatur pembuatan nilai PK otomatis, mirip auto_increment```

27. Apa itu Repository?

Jawaban:

```text
repository itu ya kayak container program sbg data storage gitu
```

28. Apa fungsi JpaRepository?

Jawaban:

```text
menyediakan fungsi DB standar, bisa CRUD(create, read, update, delete), bawaan Spring```

29. Pada Spring Boot 3, kenapa import JPA menggunakan jakarta.persistence, bukan javax.persistence?

Jawaban:

```text
versi upgrade nya udh jakarta
```

## Section D - Query

30. Apa itu derived query method di Spring Data JPA?

Jawaban:

```text
query method automation yang memperbolehkan spring data jpa yang bisa buat query sql otomatis hanya dengan menuliskan nama method di interface
```

31. Apa contoh query method untuk mencari customer berdasarkan email?

Jawaban:

```text
findByEmail(String email)
```

32. Apa fungsi @Query?

Jawaban:

```text
Anotasi untuk mendefinisikan custom query secara manual dalam sebuah method. 
```

33. Apa perbedaan JPQL dan native query?

Jawaban:

```text
Java Persistence Query Language adalah query orientasi pada objek, yang diakses pun adlh class Entity dan nama variable Java. Sedangkan native query SQL lgsg beriorientasi pada nama tabel dan kolom fisik db```

34. Kapan menggunakan native query?

Jawaban:

```text
digunakan ketika mengeksekusi fungsi spesifik dari suatu db yang gapake JPQL
```

## Section E - Flyway

35. Apa itu database migration?

Jawaban:

```text
proses perpindahan , perubahan, pembaruan struktur skema database secara bertaahp```

36. Apa itu Flyway?

Jawaban:

```text
open src lib Java yang berfungsi sebagai alat databse migration di berbagai tahapan```

37. Kenapa perubahan schema database perlu versioning?

Jawaban:

```text
agar ada dokumentasi jelas```

38. Apa maksud file V1__create_customers_table.sql?

Jawaban:

```text
ini file script migrasi flyway dengan skema penamaan standar : 'v1'```

39. Apa risiko jika struktur database diubah manual tanpa migration?

Jawaban:

```text
data redundancy, data incosistency, dkk```

## Section F - Relationship & Lazy Loading

40. Apa itu relationship antar table?

Jawaban:

```text
hubungan logis antara tabel, misal antara tabel product dan customer```

41. Apa itu one-to-many?

Jawaban:

```text
hubungan antara 1 baris data tabel a bisa terhubung dengan banyak baris data tabel b```

42. Apa itu many-to-one?

Jawaban:

```text
banyak baris data tabel b bisa terhubung/merujuk pada satu baris data tabel a```

43. Apa fungsi @ManyToOne?

Jawaban:

```text
anotasi JPA yang diletakkan di sisi kelas Entity  anak untuk mendefinisikan many-to1```
44. Apa fungsi @OneToMany?

Jawaban:

```text
anotasi JPA sisi kelas Entity induk untuk mendefinisikan hub one to many```

45. Apa itu lazy loading?

Jawaban:

```text
strategi loading data dari tabel relasi yg tdk akan diambil dari database sampai data tsb bener2 ke diakses/dipanggil```

46. Apa itu eager loading?

Jawaban:

```text
strategi loading data dari tabel relasi akan otomatis lgsg diambil dari database secara bersamaan pake JOIN```

47. Apa risiko lazy loading jika tidak dipahami?

Jawaban:

```text
memicu lazyinitializationexception jika data relasi diakses saat session database```

48. Apa itu N+1 query problem?

Jawaban:

```text
masalah performa dmn aplikasi mengeksekusi 1 query awal untuk get all list data
```

49. Apa itu join fetch?

Jawaban:

```text
solusi JPQL untuk mengatasi n+1 query problem, memaksa jpa untuk lsgs memuat data utama sekaligus relasinya secara instan dlm 1x eksekusi```

## Section G - Finance Case

50. Dalam sistem pinjaman, kenapa customer dan loan application sebaiknya dipisah ke table berbeda?

Jawaban:

```text
utk menjaga keamanan data leak
 ```

51. Dalam sistem cicilan, kenapa repayment schedule perlu table sendiri?

Jawaban:

```text
karena satu pinjaman akan dipecah jadi banyak tenor pembayaran```

52. Apa contoh query yang berguna untuk melihat loan berdasarkan status?

Jawaban:

```text
SELECT loanId, status
FROM Loan
WHERE status = "Completed"```

53. Apa contoh query yang berguna untuk melihat total pembayaran customer?

Jawaban:

```text
SELECT loanId, loanAmount, customerId
FROM Customer
W```

## Self Assessment

| Area | Score 1-5 |
| --- | --- |
| Database basic |4|
| SQL basic |4|
| JPA |1|
| Hibernate |1|
| Repository |2|
| Flyway |1|
| Relationship |4|
| Join query |4|
| Lazy loading |1|
| Finance data modeling |3|

## Notes

```text
JPA, Hibernate, Repository```

