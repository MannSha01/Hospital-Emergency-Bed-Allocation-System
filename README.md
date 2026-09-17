## **Student Details**
- **Name: Mann Sharma**
- **Registration Number: [25BAI10379]**
- **Branch/Year: B.Tech Computer Science Engineering, 2nd Year**
- **University: VIT Bhopal University**



# Hospital Emergency Triage & Bed Allocation System

A multi-threaded, command-line Java application desiged for real-time emergency room patient triage and dynamic bed allocation. The system prioritizes incoming patients by medical severity using a priority queue, manages bed occupancy safely across concurrent intake threads, logs all operations to a local audit file, and persists data using an embedded H2 database via JDBC.

---

## Course Module Alignment

* **Module 1 & 2: Java latform & Object-Oriented Design**
  * Implements doman classes (`Patient`, `Bed`) using encapsulation, inheritance (`ICUBed` and `GeneralBed` extending abstract class `Bed`), and custom sorting logic via the `Comparable` interface.
* **Module 3: Exception Handling & File I/O**
  * Throws custom domain exeptions (`NoBedsAvailableException`) when bed capacity is reached.
  * Uses `BufferedWriter` and `FileWriter` in `AuditLogger` to record every intake, allocation, and discharge event with timestamps into `triage_audit.log`.
* **Module 4: Java Collections Framework**
  * Utilizes `PriorityQueue<Patient>` to automatcally order patients by urgency score (1 = Critical, 5 = Non-urgent).
  * Uses `HashMap<Integer, Bed>` for O(1) memory lookup of bed statuses.
* **Module 5: Concurrency & JDBC atabase Persistence**
  * Simulates parallel registration desks using multi-threaded execution (`Runnable`, `ExecutorService`).
  * Protects bed assignmentlogic against race conditions using `synchronized` blocks.
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
