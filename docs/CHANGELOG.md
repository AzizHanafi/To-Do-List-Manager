# Changelog

Semua perubahan signifikan pada project TaskFlow dicatat di sini.
Format mengacu pada [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

---

## [1.1.0] — 2026-04-14

### Added
- **Category Cards (2×2 Grid)** — Tampilan kategori di sidebar diubah dari flat list menjadi card grid dengan warna per kategori, emoji, dan angka count yang ditampilkan prominently
- **Priority Chips (horizontal)** — Tiga chip prioritas sejajar horizontal dengan color coding merah/kuning/hijau, menggantikan flat nav list sebelumnya
- **Active state per warna** — Saat card/chip dipilih, border tebal + colored drop shadow muncul sesuai warna kategori/prioritas masing-masing
- **Hover animation** — Card dan chip naik sedikit (`translateY -2`) saat di-hover untuk feedback visual

### Changed
- Lebar sidebar dari `234px` menjadi `250px` untuk memberikan ruang lebih pada card grid
- `SIDEBAR_WIDTH` constant di `MainController` diperbarui mengikuti perubahan lebar

### Technical
- Tipe field `navKuliah/navKerja/navPersonal/navLainnya` di controller berubah dari `HBox` → `VBox`
- Tipe field `navHigh/navMedium/navLow` berubah dari `HBox` → `VBox`, direname menjadi `chipHigh/chipMedium/chipLow`
- Style class aktif kategori berubah: `nav-item-active` → `cat-card-active`
- Style class aktif prioritas berubah: `nav-item-active` → `prio-chip-active`
- Ditambahkan ~100 baris CSS baru untuk `.cat-card-*` dan `.prio-chip-*`

---

## [1.0.0] — 2026-04-14

### Added
- **Task Management** — Tambah, edit, hapus task melalui form dialog modal
- **Category System** — Empat kategori: Kuliah, Kerja, Personal, Lainnya
- **Priority System** — Tiga level prioritas: Tinggi (High), Sedang (Medium), Rendah (Low)
- **Deadline Tracking** — DatePicker dengan validasi (tidak bisa pilih tanggal lampau), indikator overdue berwarna merah
- **Dual Filter** — Filter task berdasarkan kategori DAN/ATAU prioritas secara bersamaan (logika AND)
- **Real-time Search** — Pencarian instan berdasarkan judul dan deskripsi task
- **Progress Card** — Progress bar, persentase selesai, dan pesan motivasi dinamis
- **Sidebar Navigation** — Sidebar collapsible dengan animasi slide (270ms, EASE_BOTH interpolation)
- **Task Card** — Tampilan task dengan checkbox, badge kategori, badge prioritas, deadline, tombol edit & hapus
- **Section Grouping** — Task dikelompokkan menjadi "Belum Selesai" dan "Selesai", diurutkan berdasarkan prioritas
- **JSON Persistence** — Semua task otomatis disimpan ke `data/tasks.json` menggunakan Gson
- **Empty State** — Tampilan kosong yang informatif saat tidak ada task atau hasil pencarian kosong

### Technical
- Arsitektur MVC dengan package `model`, `service`, `controller`, `util`
- `TaskService` menggunakan Singleton pattern
- `FileManager` dengan custom Gson serializer untuk `LocalDate` dan `LocalDateTime`
- JavaFX FXML + CSS terpisah (Material Design 3 × Notion-inspired)
