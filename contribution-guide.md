# Contribution Guide

Panduan ini menjelaskan workflow GitHub. Peserta akan melakukan fork repository, mengerjakan tugas di repository masing-masing, lalu mengirim hasil pekerjaan melalui Pull Request ke repository utama.

Repository utama:

```text
https://github.com/khalidalhabibie/hephaestus-backend-forge
```

Branch utama:

```text
master
```

## 1. Fork Repository

1. Buka repository utama:

```text
https://github.com/khalidalhabibie/hephaestus-backend-forge
```

2. Klik tombol **Fork**.
3. Pilih akun GitHub kamu.
4. Setelah selesai, kamu akan memiliki salinan repository di akun sendiri.

Contoh:

```text
Repository utama:
https://github.com/khalidalhabibie/hephaestus-backend-forge

Repository fork:
https://github.com/username/hephaestus-backend-forge
```

Ganti `username` dengan username GitHub kamu.

## 2. Clone Repository Fork

Clone repository dari akun GitHub kamu sendiri.

```bash
git clone https://github.com/username/hephaestus-backend-forge.git
cd hephaestus-backend-forge
```

## 3. Tambahkan Upstream

Tambahkan repository utama sebagai remote bernama `upstream`.

```bash
git remote add upstream https://github.com/khalidalhabibie/hephaestus-backend-forge.git
```

Cek remote:

```bash
git remote -v
```

Keterangan:

```text
origin   = repository fork milik kamu
upstream = repository utama
```

## 4. Ambil Update dari Upstream Master

Sebelum mulai mengerjakan, pastikan branch `master` di repository kamu sudah mengikuti repository utama.

```bash
git checkout master
git fetch upstream
git merge upstream/master
git push origin master
```

## 5. Buat Branch dari Master

Buat branch baru dari `master`.

Format branch:

```text
exercise/day-01-nama
```

Contoh:

```bash
git checkout -b exercise/day-01-budi
```

## 6. Kerjakan Pretest, Exercise, dan Posttest

Masuk ke folder day yang sedang dikerjakan.

```bash
cd day-01-java-and-rest-api
```

Kerjakan file berikut sesuai instruksi:

- `pretest.md`
- `exercise.md`
- `posttest.md`

## 7. Cek Perubahan

Gunakan perintah berikut untuk melihat file yang berubah:

```bash
git status
```

Jika ingin melihat detail perubahan:

```bash
git diff
```

## 8. Commit Perubahan

Tambahkan file yang sudah dikerjakan:

```bash
git add .
```

Buat commit dengan pesan yang jelas:

```bash
git commit -m "Complete day 01 exercise"
```

Contoh commit message lain:

```bash
git commit -m "Add day 01 pretest answer"
git commit -m "Complete customer REST API exercise"
git commit -m "Add day 01 posttest answer"
```

## 9. Push ke Repository Fork

Push branch kamu ke repository fork.

```bash
git push origin exercise/day-01-budi
```

## 10. Buat Pull Request

Setelah push berhasil:

1. Buka repository fork kamu di GitHub.
2. Klik **Compare & pull request**.
3. Pastikan target Pull Request adalah:

```text
khalidalhabibie/hephaestus-backend-forge:master
```

4. Pastikan compare branch adalah branch kamu, misalnya:

```text
username/hephaestus-backend-forge:exercise/day-01-budi
```

5. Isi template Pull Request.
6. Klik **Create pull request**.

Pastikan semua pekerjaan dikirim ke branch `master` repository utama, bukan `main`.
