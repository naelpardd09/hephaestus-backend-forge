
# Hephaestus Backend Forge

Repository ini digunakan sebagai learning workspace untuk kelas backend Java Spring.

Peserta akan mengerjakan materi secara bertahap melalui:

- Pretest
- Exercise
- Posttest
- Solution / revisi jika dibutuhkan

Repository utama:


https://github.com/khalidalhabibie/hephaestus-backend-forge


---

## Tujuan Repository

Repository ini dibuat untuk membantu peserta belajar backend development dengan praktik langsung.

Fokus pembelajaran:

* Java Fundamental
* REST API
* Spring Framework
* Validation & Error Handling
* API Contract & DTO Mapping
* Database & Transaction
* Messaging / Async Process
* Performance
* Security
* Deployment

---

## Course Outline

| Day   | Topic                                |
| ----- | ------------------------------------ |
| Day 1 | Java Fundamental & REST API          |
| Day 2 | Validation & Error Handling          |
| Day 3 | API Contract & DTO Mapping           |
| Day 4 | Transaction & Database Handling      |
| Day 5 | Messaging / Async Process            |
| Day 6 | Performance, Redis & Connection Pool |
| Day 7 | Security & Authentication            |
| Day 8 | Deployment & Observability           |

---

## Struktur Repository

```text
hephaestus-backend-forge/
├── README.md
├── requirements.md
├── docker-compose.yml
├── day-01-java-and-rest-api/
│   ├── README.md
│   ├── pretest.md
│   ├── exercise.md
│   ├── posttest.md
│   └── solution/
├── day-02-validation-error-handling/
│   ├── README.md
│   ├── pretest.md
│   ├── exercise.md
│   ├── posttest.md
│   └── solution/
├── day-03-api-contract-dto-mapping/
├── day-04-transaction-database/
├── day-05-messaging-async-process/
├── day-06-performance-redis-connection-pool/
├── day-07-security-authentication/
├── day-08-deployment-observability/
└── final-project/
```

---

## Requirement

Tools yang digunakan:

| Tool            | Version                   |
| --------------- | ------------------------- |
| Java            | Java 8                    |
| Spring          | Spring 5.3.24             |
| Oracle Database | Oracle 19c                |
| Oracle JDBC     | OJDBC8                    |
| HikariCP        | 5.0.1                     |
| Redis           | 5.0.3                     |
| Docker          | 28.0.4                    |
| Docker Compose  | Latest compatible version |

---

# Panduan Peserta

Peserta tidak perlu push langsung ke repository utama.

Flow yang digunakan:

```text
Fork repo utama
↓
Clone repo fork milik peserta
↓
Tambahkan upstream ke repo utama
↓
Ambil update terbaru dari upstream/master
↓
Buat branch exercise
↓
Kerjakan pretest / exercise / posttest
↓
Commit
↓
Push ke repo fork peserta
```

---

## 1. Fork Repository

Buka repository utama:

```text
https://github.com/khalidalhabibie/hephaestus-backend-forge
```

Lalu klik tombol **Fork** di kanan atas GitHub.

Setelah fork selesai, kamu akan punya repository copy di akun GitHub kamu sendiri.

Contoh:

```text
Repository utama:
https://github.com/khalidalhabibie/hephaestus-backend-forge

Repository hasil fork:
https://github.com/username/hephaestus-backend-forge
```

Ganti `username` dengan username GitHub kamu.

---

## 2. Clone Repository Fork

Clone repository dari akun GitHub kamu sendiri, bukan dari repository utama.

```bash
git clone https://github.com/username/hephaestus-backend-forge.git
cd hephaestus-backend-forge
```

Ganti `username` dengan username GitHub kamu.

Contoh:

```bash
git clone https://github.com/budi/hephaestus-backend-forge.git
cd hephaestus-backend-forge
```

---

## 3. Cek Branch Utama

Repository ini menggunakan branch utama:

```text
master
```

Cek branch aktif:

```bash
git branch
```

Pastikan kamu berada di branch `master`.

Jika belum, jalankan:

```bash
git checkout master
```

---

## 4. Tambahkan Remote Upstream

Remote `origin` adalah repository fork milik kamu.

Remote `upstream` adalah repository utama milik mentor.

Tambahkan upstream:

```bash
git remote add upstream https://github.com/khalidalhabibie/hephaestus-backend-forge.git
```

Cek remote:

```bash
git remote -v
```

Expected output:

```text
origin    https://github.com/username/hephaestus-backend-forge.git (fetch)
origin    https://github.com/username/hephaestus-backend-forge.git (push)
upstream  https://github.com/khalidalhabibie/hephaestus-backend-forge.git (fetch)
upstream  https://github.com/khalidalhabibie/hephaestus-backend-forge.git (push)
```

Keterangan:

```text
origin   = repository fork milik peserta
upstream = repository utama milik mentor
```

---

## 5. Ambil Update Terbaru dari Repository Utama

Sebelum mulai mengerjakan tugas baru, selalu ambil update terbaru dari repository utama.

```bash
git checkout master
git fetch upstream
git merge upstream/master
git push origin master
```

Penjelasan:

```text
git checkout master        = pindah ke branch master
git fetch upstream         = mengambil update terbaru dari repo utama
git merge upstream/master  = memasukkan update ke master lokal
git push origin master     = mengirim update ke repo fork kamu
```

---

## 6. Buat Branch untuk Mengerjakan Tugas

Jangan mengerjakan langsung di branch `master`.

Buat branch baru untuk setiap tugas.

Format branch:

```text
exercise/day-xx-nama
```

Contoh:

```bash
git checkout -b exercise/day-01-budi
```

Contoh lain:

```bash
git checkout -b exercise/day-01-siti
git checkout -b exercise/day-02-andra
git checkout -b exercise/day-03-rani
```

---

## 7. Kerjakan Pretest / Exercise / Posttest

Masuk ke folder day yang sedang dikerjakan.

Contoh Day 1:

```bash
cd day-01-java-and-rest-api
```

File yang biasanya dikerjakan:

```text
pretest.md
exercise.md
posttest.md
solution/
```

Kamu boleh membuat Pull Request untuk salah satu bagian saja.

Contoh:

```text
PR 1: hanya pretest
PR 2: hanya exercise
PR 3: hanya posttest
PR 4: revisi jawaban
```

Tidak harus semua dikumpulkan dalam satu Pull Request.

---

## 8. Cek Perubahan

Setelah selesai mengerjakan, cek file yang berubah:

```bash
git status
```

Jika ingin melihat detail perubahan:

```bash
git diff
```

---

## 9. Commit Perubahan

Tambahkan file yang sudah dikerjakan:

```bash
git add .
```

Commit dengan message yang jelas:

```bash
git commit -m "Complete day 01 pretest"
```

Contoh commit message:

```bash
git commit -m "Complete day 01 pretest"
git commit -m "Complete day 01 exercise"
git commit -m "Complete day 01 posttest"
git commit -m "Fix day 02 exercise answer"
```

Hindari commit message seperti:

```text
update
fix
done
test
wip
```

---

## 10. Push Branch ke Repository Fork

Push branch kamu ke repository fork milik kamu.

```bash
git push origin exercise/day-01-budi
```

Ganti nama branch sesuai branch yang kamu buat.

---

## 11. Buat Pull Request ke Repository Utama

Setelah push berhasil:

1. Buka repository fork kamu di GitHub.
2. Klik tombol **Compare & pull request**.
3. Pastikan target Pull Request adalah repository utama.
4. Pastikan base branch adalah `master`.
5. Pastikan compare branch adalah branch yang kamu push.

Format yang benar:

```text
base repository : khalidalhabibie/hephaestus-backend-forge
base branch     : master

head repository : username/hephaestus-backend-forge
compare branch  : exercise/day-01-budi
```

---

# Format Pull Request

Gunakan format Pull Request yang simple.

```markdown
## Apa yang dikerjakan?

Saya mengerjakan:

- [ ] Pretest
- [ ] Exercise
- [ ] Posttest
- [ ] Revisi / perbaikan

Day:

Topic:

---

## Untuk apa?

PR ini dibuat untuk:



---

## Catatan

<!-- Isi kalau ada kendala atau bagian yang ingin ditanyakan. Kalau tidak ada, tulis "-" -->



---

## Checklist

- [ ] Saya sudah mengerjakan sesuai instruksi
- [ ] Saya sudah cek perubahan dengan `git status`
- [ ] Saya sudah push dari branch sendiri
```

---

## Contoh Pull Request Pretest

```markdown
## Apa yang dikerjakan?

Saya mengerjakan:

- [x] Pretest
- [ ] Exercise
- [ ] Posttest
- [ ] Revisi / perbaikan

Day: Day 1

Topic: Java Fundamental & REST API

---

## Untuk apa?

PR ini dibuat untuk mengumpulkan jawaban pretest Day 1 sebelum masuk materi Java dan REST API.

---

## Catatan

Masih bingung di bagian perbedaan interface dan abstract class.

---

## Checklist

- [x] Saya sudah mengerjakan sesuai instruksi
- [x] Saya sudah cek perubahan dengan `git status`
- [x] Saya sudah push dari branch sendiri
```

---

## Contoh Pull Request Exercise

```markdown
## Apa yang dikerjakan?

Saya mengerjakan:

- [ ] Pretest
- [x] Exercise
- [ ] Posttest
- [ ] Revisi / perbaikan

Day: Day 1

Topic: Java Fundamental & REST API

---

## Untuk apa?

PR ini dibuat untuk mengumpulkan exercise pembuatan REST API sederhana untuk customer.

---

## Catatan

API create customer sudah selesai, tetapi masih ingin direview bagian struktur Controller dan Service.

---

## Checklist

- [x] Saya sudah mengerjakan sesuai instruksi
- [x] Saya sudah cek perubahan dengan `git status`
- [x] Saya sudah push dari branch sendiri
```

---

## Contoh Pull Request Posttest

```markdown
## Apa yang dikerjakan?

Saya mengerjakan:

- [ ] Pretest
- [ ] Exercise
- [x] Posttest
- [ ] Revisi / perbaikan

Day: Day 1

Topic: Java Fundamental & REST API

---

## Untuk apa?

PR ini dibuat untuk mengumpulkan jawaban posttest setelah menyelesaikan materi Day 1.

---

## Catatan

-

---

## Checklist

- [x] Saya sudah mengerjakan sesuai instruksi
- [x] Saya sudah cek perubahan dengan `git status`
- [x] Saya sudah push dari branch sendiri
```

---

# Jika Ada Update Baru dari Mentor

Jika mentor menambahkan materi baru di repository utama, peserta perlu sync ulang.

Jalankan dari branch `master`:

```bash
git checkout master
git fetch upstream
git merge upstream/master
git push origin master
```

Setelah itu buat branch baru untuk tugas berikutnya:

```bash
git checkout -b exercise/day-02-budi
```

---

# Jika Sedang Mengerjakan Branch dan Ada Update Baru

Kalau kamu sedang berada di branch exercise, lalu repository utama ada update baru, jalankan:

```bash
git fetch upstream
git merge upstream/master
```

Jika tidak ada conflict, push ulang branch kamu:

```bash
git push origin exercise/day-01-budi
```

Jika ada conflict:

1. Buka file yang conflict.
2. Pilih perubahan yang benar.
3. Simpan file.
4. Jalankan command berikut:

```bash
git add .
git commit -m "Resolve conflict with upstream master"
git push origin exercise/day-01-budi
```

---

# Workflow Singkat

Gunakan command ini sebagai ringkasan.

```bash
# Clone repository fork
git clone https://github.com/username/hephaestus-backend-forge.git
cd hephaestus-backend-forge

# Tambahkan upstream repository utama
git remote add upstream https://github.com/khalidalhabibie/hephaestus-backend-forge.git

# Ambil update terbaru dari repository utama
git checkout master
git fetch upstream
git merge upstream/master
git push origin master

# Buat branch exercise
git checkout -b exercise/day-01-nama

# Kerjakan tugas
git status
git add .
git commit -m "Complete day 01 pretest"

# Push ke repository fork
git push origin exercise/day-01-nama
```

Setelah itu, buat Pull Request ke:

```text
khalidalhabibie/hephaestus-backend-forge:master
```

---

# Aturan Sederhana

* Jangan push langsung ke repository utama.
* Jangan mengerjakan langsung di branch `master`.
* Selalu buat branch baru untuk tugas.
* Selalu sync dari `upstream/master` sebelum mulai tugas baru.
* Pull Request boleh hanya berisi pretest, exercise, posttest, atau revisi.
* Gunakan commit message yang jelas.
* Jika bingung, tulis kendala di bagian `Catatan` saat membuat Pull Request.


