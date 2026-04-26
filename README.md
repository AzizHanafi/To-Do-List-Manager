# TaskFlow

Aplikasi to-do list desktop berbasis JavaFX. Tampilan bersih dan minimalis dengan Material Design 3<br>
dilengkapi filter kategori, tingkat prioritas, pelacak tenggat waktu, dan pencarian real-time.

![Preview](https://github.com/user-attachments/assets/7c19e547-6bc1-4757-bc3c-eb1d3b40e56b)

---

## Fitur

- **Tambah, edit, hapus** tugas dengan judul, deskripsi, tenggat waktu, kategori, dan prioritas
- **Kategori** — Kuliah, Kerja, Personal, Lainnya
- **Tingkat prioritas** — Tinggi, Sedang, Rendah (dengan kode warna)
- **Filter** tugas berdasarkan kategori atau prioritas (atau keduanya sekaligus)
- **Pencarian** di judul dan deskripsi tugas secara real-time
- **Kartu progres** yang menampilkan persentase penyelesaian dan jumlah tugas
- **Indikator keterlambatan** untuk tugas yang melewati tenggat waktu
- **Sidebar lipat** dengan animasi halus
- **Penyimpanan persisten** — tugas disimpan otomatis ke JSON

---

## Teknologi

| | |
|---|---|
| Bahasa | Java 21 |
| Framework UI | JavaFX 21.0.2 |
| Build Tool | Maven 3.x |
| Library JSON | Gson 2.10.1 |

---

## Cara Memulai

### Prasyarat

- JDK 21 atau lebih baru
- Maven 3.6+

### Menjalankan Aplikasi

```bash
mvn javafx:run
```

### Build JAR

```bash
mvn clean package
```

---

## Struktur Proyek

```
src/
└── main/
    ├── java/com/todolist/
    │   ├── Main.java
    │   ├── controller/
    │   │   ├── MainController.java
    │   │   └── TaskFormController.java
    │   ├── model/
    │   │   ├── Task.java
    │   │   ├── Category.java
    │   │   └── Priority.java
    │   ├── service/
    │   │   └── TaskService.java
    │   └── util/
    │       └── FileManager.java
    └── resources/com/todolist/
        ├── fxml/
        │   ├── main.fxml
        │   └── task_form.fxml
        └── css/
            └── style.css
data/
└── tasks.json
```

---

## Lisensi

Proyek ini menggunakan Lisensi MIT. Silakan lihat file [LICENSE](LICENSE) untuk informasi selengkapnya.
