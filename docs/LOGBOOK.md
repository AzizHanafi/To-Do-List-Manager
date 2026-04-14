# Development Logbook — TaskFlow

Log pengerjaan harian project To-Do List Manager.

---

## Sesi 14 April 2026 (v1.0 → v1.1)

**Scope:** UI redesign sidebar — category cards & priority chips

**Dikerjakan:**
- Analisis struktur sidebar yang ada (HBox flat list untuk kategori dan prioritas)
- Rancang ulang tampilan: kategori menjadi card grid 2×2, prioritas menjadi horizontal chip row
- Implementasi `GridPane` dengan `ColumnConstraints percentWidth="50"` untuk layout card yang equal di FXML
- Redesign CSS: tambah class `.cat-card`, `.cat-card-kuliah/kerja/personal/lainnya`, `.cat-card-active`
- Redesign CSS: tambah class `.prio-chip`, `.prio-chip-high/medium/low`, `.prio-chip-active`
- Refactor controller: ubah tipe field dari `HBox` ke `VBox`, rename field names, update style class logic
- Sidebar diperlebar ke 250px
- Buat README.md

**Kendala:**
- Compound CSS selector (`.cat-card-kuliah.cat-card-active`) perlu urutan deklarasi yang tepat agar specificity tidak bentrok dengan `:hover`
- GridPane `percentWidth` di FXML membutuhkan `maxWidth="Infinity"` pada child VBox agar fill column dengan benar

**Solusi:**
- Active state CSS ditempatkan setelah hover state → specificity sama, cascade menang
- Tambahkan `-fx-translate-y: 0` di active state untuk override hover lift

---

## Sesi 14 April 2026 (Inisialisasi v1.0)

**Scope:** Setup project dan implementasi fitur lengkap

**Dikerjakan:**
- Setup Maven project dengan dependency JavaFX 21.0.2 dan Gson 2.10.1
- Buat model `Task.java` dengan field: id (UUID), title, description, category, priority, deadline, completed, createdAt
- Buat enum `Category` (KULIAH, KERJA, PERSONAL, LAINNYA) dan `Priority` (HIGH, MEDIUM, LOW) dengan display name Bahasa Indonesia
- Implementasi `TaskService` dengan Singleton pattern — CRUD operations + filter helper
- Implementasi `FileManager` — custom Gson serializer untuk `LocalDate` dan `LocalDateTime`, load/save ke JSON
- Desain `main.fxml` — layout HBox (sidebar + main content), top bar, progress card, scrollable task list
- Desain `task_form.fxml` — dialog modal untuk add/edit task
- Implementasi `MainController` — filter logic, task card builder, progress updater, sidebar animation
- Implementasi `TaskFormController` — form validation, date picker constraint (disable past dates), edit mode
- Tulis `style.css` — Material Design 3 color palette, card components, badge system, dialog styling

**Keputusan desain:**
- Gunakan Singleton untuk `TaskService` karena hanya butuh satu instance sepanjang lifecycle app
- Filter menggunakan stream chain (category → priority → search) dengan logika AND
- Task incomplete diurutkan HIGH → MEDIUM → LOW menggunakan `Comparable` order dari enum ordinal
- Sidebar animation menggunakan `Timeline` + `KeyValue` untuk animasi properti width secara smooth
