# Problem Statement & Scope

## Problem Statement
Emergency departments (ED) in healthcare facilities globally experience sudden, unpredictable surges in patient volume[cite: 1]. In conventional emergency care settings, patient intake and bed distribution often rely on First-Come, First-Served (FCFS) queuing or manual, non-automated tracking workflows[cite: 1]. During critical surge periods, standard FCFS queuing fails severely ill patients, causing life-threatening delays in securing Intensive Care Unit (ICU) beds and immediate life-saving interventions[cite: 1].

Manual bed management is inherently prone to operational bottlenecks, double-booking race conditions across concurrent registration desks, and a lack of real-time visibility into ward utilization[cite: 1]. Furthermore, many complex hospital management enterprise software systems require heavy graphical interfaces, complex cloud deployments, or external database setups that are difficult to evaluate or run in lightweight environments[cite: 1]. There is a critical need for an automated, multi-threaded emergency triage system that prioritizes patients purely by medical urgency, automates smart bed allocation (with ICU-to-General bed fallbacks), and provides instant operational analytics without requiring complex graphical setups or external server deployments[cite: 1].

---

## Scope of the Project
The **Hospital Emergency Triage & Bed Allocation System** is a lightweight, high-performance Java CLI application designed to automate emergency room intake, severity prioritization, and bed assignment[cite: 1].

### In-Scope
* **Dynamic Priority Triage Engine:** Automatically sorting incoming patients using medical urgency scores ($1 = \text{Critical}$ to $5 = \text{Non-Urgent}$) powered by Java's thread-safe collections[cite: 1].
* **Smart Bed Routing & Fallback System:** Directing critical patients (Urgency 1 & 2) to ICU beds first, with automatic fallback routing to General Ward beds if ICU capacity is full[cite: 1].
* **Thread-Safe Multi-Desk Concurrency:** Simulating concurrent patient registration desks using multi-threaded execution with synchronized memory management to prevent double-booking race conditions[cite: 1].
* **Embedded Relational Persistence:** Persisting patient records, bed statuses, and allocation maps in a local, file-backed embedded H2 SQL database via JDBC transactions[cite: 1].
* **Custom Exception Handling:** Handling capacity exhaustion gracefully via a custom domain exception (`NoBedsAvailableException`), preventing system crashes during peak hospital loads[cite: 1].
* **Real-Time Analytics & Audit Logging:** Generating an ASCII-based terminal dashboard for capacity tracking and logging every action with precise timestamps to a local `triage_audit.log` file[cite: 1].
* **Zero-Setup Command-Line Execution:** Operating entirely headlessly via terminal commands (`mvn clean package && java -jar ...`) with zero GUI or external database server dependencies[cite: 1].

### Out-of-Scope
* Graphical User Interface (GUI), web frontend (HTML/JS), or mobile application interfaces[cite: 1].
* Long-term Electronic Health Record (EHR) or patient medical history tracking.
* Integration with external hospital billing, insurance processing, or pharmacy management systems.
* Multi-hospital network routing or ambulance GPS tracking.

---

## Target Users
1. **Emergency Room Triage Nurses & Intake Staff:** Staff members registering incoming emergency patients and needing instant, objective bed allocation based on clinical severity[cite: 1].
2. **Hospital Operations & Emergency Department Managers:** Administrators monitoring real-time ER bed occupancy percentages, waiting queue sizes, and ward capacity limits[cite: 1].
3. **Academic Evaluators & Software Auditors:** Technical reviewers looking for a fully command-line compliant, zero-setup Java demonstration showcasing OOD, Java Collections, Concurrency, and JDBC persistence[cite: 1].

---

## High-Level Features
* **Priority Queue Triage Engine:** Utilizes Java's `PriorityBlockingQueue` to ensure critical patients (Urgency 1) are prioritized over non-urgent ones regardless of arrival sequence[cite: 1].
* **Smart Bed Allocation & Fallback Handling:** Intelligently matches medical severity to bed types (`ICU` vs. `GENERAL`) and handles capacity shortage gracefully via a custom `NoBedsAvailableException`[cite: 1].
* **Embedded SQL Database Management:** Automatically initializes database tables (`beds`, `patients`) and seeds baseline bed inventory on launch using embedded H2 JDBC[cite: 1].
* **Real-Time ASCII Analytics Dashboard:** Computes and prints live ER bed utilization stats (e.g., `100% Capacity`) and waiting queue counts before and after allocation runs[cite: 1].
* **Thread-Safe Concurrent Operations:** Uses synchronized execution blocks to protect bed status transitions against race conditions across parallel intake threads[cite: 1].
* **Automated File Audit Logging:** Writes all registration, allocation, and capacity exhaustion events asynchronously into `triage_audit.log`[cite: 1].
* **Human-Readable Clinical Labeling:** Automatically maps integer urgency scores (1–5) to descriptive clinical status labels (`CRITICAL`, `SEVERE`, `MODERATE`, `LOW URGENCY`, `NON-URGENT`).
