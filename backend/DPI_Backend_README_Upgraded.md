# 🚀 Deep Packet Inspection (DPI) Engine — Production-Style Backend

## 📌 Project Summary

This project is a **high-performance, real-time Deep Packet Inspection (DPI) backend system** built using **Java (Spring Boot)**.

It simulates how real-world network security systems (like IDS/IPS) operate by:
- Parsing raw network traffic (`.pcap`)
- Aggregating packets into flows
- Applying dynamic security rules
- Detecting malicious behavior (e.g., port scans)
- Persisting results asynchronously for performance

👉 This is a **systems + backend engineering project**, not just CRUD.

---

## 🎯 What Problem This Solves

Traditional systems struggle with:
- High-throughput packet processing
- Real-time threat detection
- Avoiding DB bottlenecks

This project solves it using:
- Async pipeline architecture
- Flow-based processing (not packet-by-packet)
- In-memory detection + persistence separation

---

## 🧠 Core Concepts (Interview Critical)

### 🔹 FiveTuple (Flow Identity)
A flow is uniquely identified using:
- Source IP
- Destination IP
- Source Port
- Destination Port
- Protocol

👉 This is how real networking systems track communication.

---

### 🔹 Packet vs Flow

| Packet | Flow |
|------|------|
| Raw data unit | Aggregated session |
| Stateless | Stateful |
| High volume | Optimized analysis |

👉 DPI operates on **flows**, not raw packets.

---

## 🏗️ Architecture (Explain This Clearly in Interviews)

```
PCAP → Parser → Queue → Worker → FlowTracker → RuleEngine → Threat Detection → Async DB → PostgreSQL
```

---

## 🔥 Key Components

### 1. PcapReader
- Reads raw `.pcap` files
- No external libraries used
- Pushes packets to queue

---

### 2. PacketParser
Extracts:
- IPs, Ports, Protocol
- Packet size
- TLS SNI (domain)

---

### 3. PacketWorker
- Converts Packet → Flow
- Builds FiveTuple
- Sends to FlowTracker

---

### 4. FlowTracker (CORE ENGINE)
Handles:
- Flow aggregation
- Rule evaluation
- Threat detection
- Async dispatch

---

### 5. RuleEngine (Dynamic)
- Rules stored in DB
- Supports:
  - field + operator + value
- Actions:
  - ALLOW
  - BLOCK
  - ALERT

👉 No hardcoding → dynamic control

---

### 6. Threat Detection

#### 🚨 Port Scan Detection
- Tracks unique destination ports per IP
- Time window: 10 seconds
- Threshold: >20 ports

👉 If triggered:
- Flow blocked
- IP blacklisted

---

### 7. Attacker Persistence
- In-memory blacklist
- Future packets auto-blocked

👉 Converts detection → prevention

---

### 8. Async Processing (MOST IMPORTANT)
- Queues decouple ingestion from DB
- No blocking operations in pipeline

👉 This is the **real engineering strength**

---

### 9. Metrics API

```
GET /api/metrics
```

Returns:
- Total packets
- Total flows
- Blocked flows

---

## ⚙️ Tech Stack

- Java 17  
- Spring Boot  
- PostgreSQL  
- JPA / Hibernate  
- Multithreading (Queue + Workers)

---

## 🧪 What Makes This Project Strong

- Real-time system design
- Concurrency handling
- Networking fundamentals
- Clean architecture (decoupled layers)
- Production-like pipeline

---

## ⚠️ Honest Limitations 

- Blacklist is in-memory (not persistent)
- Single-node (no distributed scaling)
- DB writes not fully batched
- No UI layer

👉 This shows **engineering maturity**

---

## 💡 Future Improvements

- Redis for distributed blacklist
- Kafka for scalable ingestion
- Batch DB writes
- WebSocket dashboard
- ML-based anomaly detection

---

