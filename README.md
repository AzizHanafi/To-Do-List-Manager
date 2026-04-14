# TaskFlow

A desktop to-do list application built with JavaFX. Clean, minimal UI inspired by Notion and Material Design 3 — with category filtering, priority levels, deadline tracking, and real-time search.

---

## Features

- **Add, edit, delete** tasks with title, description, deadline, category, and priority
- **Categories** — Kuliah, Kerja, Personal, Lainnya
- **Priority levels** — Tinggi, Sedang, Rendah (with color coding)
- **Filter** tasks by category or priority (or both at once)
- **Search** across task title and description in real time
- **Progress card** showing completion percentage and count
- **Overdue indicator** for past-deadline tasks
- **Collapsible sidebar** with smooth animation
- **Persistent storage** — tasks saved to JSON automatically

---

## Tech Stack

| | |
|---|---|
| Language | Java 21 |
| UI Framework | JavaFX 21.0.2 |
| Build Tool | Maven 3.x |
| JSON Library | Gson 2.10.1 |

---

## Getting Started

### Prerequisites

- JDK 21 or higher
- Maven 3.6+

### Run

```bash
mvn javafx:run
```

### Build JAR

```bash
mvn clean package
```

---

## Project Structure

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

## License

MIT
