## **Student Details**
- **Name: Mann Sharma**
- **Registration Number: [25BAI10379]**
- **Branch/Year: B.Tech Computer Science Engineering, 2nd Year**
- **University: VIT Bhopal University**



# Hospital Emergency Triage & Bed Allocation System

A multi-threaded, command-line Java application designed for real-time emergency room patient triage and dynamic bed allocation. The system prioritizes incoming patients by medical severity using a priority queue, manages bed occupancy safely across concurrent intake threads, logs all operations to a local audit file, and persists data using an embedded H2 database via JDBC.

---

## Course Module Alignment

* **Module 1 & 2: Java Platform & Object-Oriented Design**
  * Implements domain classes (`Patient`, `Bed`) using encapsulation, inheritance (`ICUBed` and `GeneralBed` extending abstract class `Bed`), and custom sorting logic via the `Comparable` interface.
* **Module 3: Exception Handling & File I/O**
  * Throws custom domain exceptions (`NoBedsAvailableException`) when bed capacity is reached.
  * Uses `BufferedWriter` and `FileWriter` in `AuditLogger` to record every intake, allocation, and discharge event with timestamps into `triage_audit.log`.
* **Module 4: Java Collections Framework**
  * Utilizes `PriorityQueue<Patient>` to automatically order patients by urgency score (1 = Critical, 5 = Non-urgent).
  * Uses `HashMap<Integer, Bed>` for O(1) memory lookup of bed statuses.
* **Module 5: Concurrency & JDBC Database Persistence**
  * Simulates parallel registration desks using multi-threaded execution (`Runnable`, `ExecutorService`).
  * Protects bed assignment logic against race conditions using `synchronized` blocks.
  * Connects to an embedded H2 SQL database using JDBC `PreparedStatement` transactions for persistent storage without requiring an external database server setup.

---

## Project Structure

```text
hospital-triage-system/
├── pom.xml
├── README.md
├── triage_audit.log (Generated at runtime)
└── src/
    └── main/
        └── java/
            └── com/
                └── hospital/
                    ├── Main.java
                    ├── config/
                    │   └── DatabaseConfig.java
                    ├── model/
                    │   ├── Bed.java
                    │   ├── ICUBed.java
                    │   ├── GeneralBed.java
                    │   └── Patient.java
                    ├── exception/
                    │   └── NoBedsAvailableException.java
                    ├── service/
                    │   ├── AuditLogger.java
                    │   └── TriageService.java
                    └── dao/
                        ├── BedDAO.java
                        └── PatientDAO.java
```

---

## Step-by-Step Setup & Run Instructions

### 1. Environment Setup
Before building and running the application, verify that your environment has Java JDK and Maven installed:

* **Java Development Kit (JDK):** Version 17 or higher required.
  ```bash
  java -version
  ```
* **Apache Maven:** Version 3.8+ required.
  ```bash
  mvn -version
  ```

### 2. Dependency Installation
Clone the repository and resolve all project dependencies defined in `pom.xml`:

```bash
git clone https://github.com/YOUR_USERNAME/hospital-triage-system.git
cd hospital-triage-system
mvn dependency:resolve
mvn compile
```

### 3. Configuration
This application is configured for **zero-setup embedded execution**:
* **Database URL:** `jdbc:h2:./triage_db;DB_CLOSE_DELAY=-1`
* **Storage Mode:** Local file-backed database (`triage_db.mv.db`) auto-created on first run.
* **Database Driver:** `org.h2.Driver` (managed via Maven dependencies).
* **Schema Initialization:** Tables (`beds`, `patients`) and baseline bed seeds are automatically executed by `DatabaseConfig.initializeDatabase()` at launch. No manual SQL script execution or database server installation is required.

### 4. Execution
Build the standalone executable JAR file and run it directly from your command terminal:

```bash
mvn clean package
java -jar target/hospital-triage-system-1.0-SNAPSHOT.jar
```

---

## Database Schema (Embedded H2)

```sql
CREATE TABLE IF NOT EXISTS beds (
    bed_id INT PRIMARY KEY,
    bed_type VARCHAR(20) NOT NULL,
    is_occupied BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS patients (
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    urgency_score INT NOT NULL,
    bed_id INT,
    FOREIGN KEY (bed_id) REFERENCES beds(bed_id)
);
```

---

## Sample CLI Execution Output

```text
==================================================
  HOSPITAL EMERGENCY TRIAGE & BED ALLOCATION SYSTEM  
==================================================
[INIT] Initializing H2 Embedded Database...
[INIT] Database initialized successfully.
[INIT] Seeded 4 beds (2 ICU, 2 General).

--- SIMULATING CONCURRENT INTAKE DESKS ---
[DESK-1] Registered: John Doe | Urgency: 1 (Critical)
[DESK-2] Registered: Jane Smith | Urgency: 3 (Moderate)
[DESK-1] Registered: Alex Brown | Urgency: 1 (Critical)

--- PROCESSING TRIAGE QUEUE ---
[TRIAGE] Processing highest priority: John Doe (Urgency: 1)
[SUCCESS] Allocated Bed #101 (ICU) to John Doe.
[AUDIT] Action logged to triage_audit.log.

[TRIAGE] Processing highest priority: Alex Brown (Urgency: 1)
[SUCCESS] Allocated Bed #102 (ICU) to Alex Brown.
[AUDIT] Action logged to triage_audit.log.

[TRIAGE] Processing highest priority: Jane Smith (Urgency: 3)
[SUCCESS] Allocated Bed #201 (GENERAL) to Jane Smith.
[AUDIT] Action logged to triage_audit.log.

==================================================
System execution finished cleanly.
==================================================
```
