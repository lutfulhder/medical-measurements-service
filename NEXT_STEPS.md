# Future Roadmap & Architectural Improvements

This project provides a solid foundation for collecting medical data. To prepare this for a production environment with heavy traffic (thousands of medical robots), I would implement the following architectural upgrades.

## 1. Scalability: Streaming Architecture (Kafka/Kinesis)
**Problem:** Writing high-frequency sensor data directly to the database (`Synchronous`) creates a bottleneck.
**Solution:** Decouple ingestion using **Data Streaming**.

* **Architecture:** `Robot` -> `API Gateway` -> **`Amazon Kinesis` (or `Kafka`)** -> `Consumer Service` -> `PostgreSQL`.
* **Why Streaming?**
* Replayability: If DB is down for maintenance, data is not lost.

* Ordering: Time-series data stays in correct sequence.

* Backpressure handling: Stream absorbs bursts in traffic.

* Horizontal scaling: Consumers scale independently.
## 2. Security: Device Authentication
**Problem:** The API is currently open. We need to ensure only authorized robots can write data.
**Solution:**
* API Keys per device (rotatable, revocable)
Key must be sent via:
 * X-API-KEY: <device-key>
* Rate Limiting: Prevent malfunctioning robots from spamming.
* Audit Logs: Track per-device throughput and health.
* Transport Encryption: TLS 1.2+ everywhere.

## 3. Data Management
**Problem:** Sensor data accumulates rapidly (millions of rows per month).
**Solution:**
* **Retention Policy:** Implement a scheduled job to move raw data older than **1 year** to cold storage (Amazon S3) for long-term archiving and audit compliance.

## 4. Analytics: AI & Anomaly Detection
**Opportunity:** With thousands of devices streaming telemetry, we have a rich dataset to predict hardware failures or patient health risks before they happen.

**Proposed Solution:**
* **Real-Time Anomaly Detection:** Attach a Kafka Consumer (Python) to the data stream to detect outliers in real-time (e.g., "Heart rate spike" or "Dispenser motor voltage irregularity").
* **Predictive Maintenance:** Train a regression model on historical sensor logs to predict when a robot's battery or mechanical parts will fail, allowing for proactive service calls.
* Notify clinicians or robots automatically (email/SMS/webhook integration).
## 5. Deployment & Reliability
* App Runner / ECS autoscaling
* Multi-AZ PostgreSQL (failover)
* CI/CD pipeline (GitHub Actions → ECR → App Runner)