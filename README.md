# DPI Engine (Deep Packet Inspection System)

## Overview
This project is a full-stack Deep Packet Inspection (DPI) system built using Java (Spring Boot) for the backend and Next.js for the frontend. It processes network traffic from PCAP files, converts packets into flows using the FiveTuple model, applies dynamic rules, detects threats like port scanning, and visualizes everything through a dashboard.

---

## Architecture
PCAP → Parser → Packet Queue → Worker → FlowTracker → RuleEngine → Threat Detection → Async DB → PostgreSQL → Frontend Dashboard

---

## Backend (Spring Boot)
- Custom PCAP parser (no external libs)
- Flow tracking using FiveTuple (srcIP, dstIP, srcPort, dstPort, protocol)
- Dynamic rule engine (DB-driven rules)
- Real-time threat detection (port scan detection)
- Async pipeline (no DB blocking)
- Metrics API (/api/metrics)
- Flow APIs (/dpi/flows, /dpi/stats)

---

## Frontend (Next.js)
- Dashboard: shows total packets, flows, blocked flows
- Flows page: displays all network flows
- Rules page: manage rules (create/delete)
- Real-time refresh
- Clean UI with error handling

---

## Problem Solved
Raw network traffic is unstructured and massive. This system converts packets into structured flows, enabling real-time monitoring, threat detection, and efficient analysis without performance bottlenecks.

---

## Key Features
- Real-time DPI processing
- Rule-based traffic filtering
- Port scan detection
- Async architecture
- Full-stack visualization

---

## Tech Stack
Backend:
- Java, Spring Boot, PostgreSQL

Frontend:
- Next.js, React, TypeScript

---

## How to Run

### Backend
cd backend
./mvnw spring-boot:run

### Frontend
cd frontend
npm install
npm run dev

---

## Future Improvements
- WebSocket real-time streaming
- Redis-based distributed blacklist
- Kafka integration
- ML-based anomaly detection

---

## Author
Anshuman Tiwari
