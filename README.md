**Deskripsi Halaman Autentikasi dan Fitur Aplikasi**

---

### **1. Halaman Login**
- **Tujuan:** Halaman untuk autentikasi pengguna sebelum mengakses aplikasi.
- **Input yang Dibutuhkan:**
  - **Email:** Menggunakan `EditText` dengan ID `R.id.ed_login_email`.
  - **Password:** Menggunakan `EditText` dengan ID `R.id.ed_login_password`.
    - Password wajib disembunyikan (mode password).
    - Menampilkan pesan error langsung pada `EditText` jika jumlah karakter password kurang dari 8.

---

### **2. Halaman Register**
- **Tujuan:** Halaman untuk pendaftaran pengguna baru.
- **Input yang Dibutuhkan:**
  - **Nama:** Menggunakan `EditText` dengan ID `R.id.ed_register_name`.
  - **Email:** Menggunakan `EditText` dengan ID `R.id.ed_register_email`.
  - **Password:** Menggunakan `EditText` dengan ID `R.id.ed_register_password`.
    - Password wajib disembunyikan.
    - Menampilkan pesan error langsung pada `EditText` jika jumlah karakter password kurang dari 8.

---

### **3. Custom View untuk Password**
- **Tujuan:** Membuat Custom View berupa `EditText` yang digunakan pada halaman login dan register.
- **Ketentuan:**
  - Pesan error muncul langsung di `EditText` jika password kurang dari 8 karakter.
  - Pesan error muncul tanpa memindahkan fokus atau klik tombol terlebih dahulu.

---

### **4. Data Sesi dan Token**
- **Tujuan:** Mengelola sesi login pengguna.
- **Ketentuan:**
  - Sesi dan token disimpan di **SharedPreferences**.
  - Jika pengguna sudah login, langsung diarahkan ke halaman utama.
  - Jika pengguna belum login, diarahkan ke halaman login.
- **Fitur Logout:**
  - Logout dilakukan dengan tombol `R.id.action_logout` di halaman utama.
  - Ketika logout, informasi token dan sesi akan dihapus dari **SharedPreferences**.

---

### **5. Daftar Cerita (List Story)**
- **Tujuan:** Menampilkan daftar cerita dari API.
- **Minimal Informasi yang Ditampilkan:**
  - **Nama user:** Ditampilkan dengan ID `R.id.tv_item_name`.
  - **Foto user:** Ditampilkan dengan ID `R.id.iv_item_photo`.
- **Detail Cerita:**
  - Saat item cerita ditekan, muncul halaman detail dengan informasi berikut:
    - **Nama user:** Ditampilkan dengan ID `R.id.tv_detail_name`.
    - **Foto user:** Ditampilkan dengan ID `R.id.iv_detail_photo`.
    - **Deskripsi cerita:** Ditampilkan dengan ID `R.id.tv_detail_description`.

---

### **6. Tambah Cerita**
- **Tujuan:** Menambahkan cerita baru ke aplikasi.
- **Input Minimal yang Dibutuhkan:**
  - **File Foto:** Harus mendukung pengambilan dari galeri.
  - **Deskripsi Cerita:** Menggunakan `EditText` dengan ID `R.id.ed_add_description`.
- **Ketentuan Upload:**
  - Tombol untuk mengunggah data ke server memiliki ID `R.id.button_add`.
  - Setelah proses upload berhasil:
    - Kembali ke halaman daftar cerita.
    - Cerita terbaru muncul di urutan paling atas.

---

### **7. Menampilkan Animasi**
- **Tujuan:** Menambahkan animasi untuk meningkatkan pengalaman pengguna.
- **Jenis Animasi yang Digunakan:**
  - **Property Animation**, **Motion Animation**, atau **Shared Element**.
- **Lokasi:** Jenis dan lokasi animasi dicatat di **Student Note**.

---

### **8. Menampilkan Maps**
- **Tujuan:** Menampilkan cerita dengan lokasi di peta.
- **Ketentuan:**
  - Peta ditampilkan di halaman baru.
  - Marker atau icon berupa gambar ditampilkan sesuai data cerita dengan koordinat latitude dan longitude.
  - Data lokasi diambil melalui endpoint: 
    ```
    https://story-api.dicoding.dev/v1/stories?location=1
    ```

---

### **9. Paging List**
- **Tujuan:** Menampilkan daftar cerita menggunakan **Paging 3**.
- **Ketentuan:**
  - Daftar cerita harus dimuat dengan efisien menggunakan mekanisme Paging 3.

---

### **10. Testing**
- **Tujuan:** Menerapkan **unit test** pada fungsi ViewModel untuk mengambil data Paging.
- **Skenario Testing:**
  - Ketika data berhasil dimuat:
    - Memastikan data tidak `null`.
    - Memastikan jumlah data sesuai ekspektasi.
    - Memastikan data pertama sesuai dengan yang diharapkan.
  - Ketika tidak ada data:
    - Memastikan jumlah data yang dikembalikan adalah nol.

---
