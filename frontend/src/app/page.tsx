"use client";

import { useEffect, useState } from "react";
import Card from "@/components/Card";

const API = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

interface Metrics {
  totalPackets: number;
  totalFlows: number;
  blockedFlows: number;
}

export default function DashboardPage() {
  const [metrics, setMetrics] = useState<Metrics | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchMetrics = async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch(`${API}/api/metrics`);
      if (!res.ok) throw new Error(`Server returned ${res.status}`);
      const data: Metrics = await res.json();
      setMetrics(data);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Failed to fetch metrics");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMetrics();
    const interval = setInterval(fetchMetrics, 10000);
    return () => clearInterval(interval);
  }, []);

  return (
    <>
      <div className="page-header">
        <h1>Dashboard</h1>
        <p>Live network monitoring metrics from the DPI engine</p>
      </div>

      {error && (
        <div className="error-box">
          ⚠️ Could not reach backend: {error}. Make sure the Spring Boot server is running on port 8080.
        </div>
      )}

      {loading && !metrics && (
        <div className="loading">
          <span className="spinner" /> Fetching metrics…
        </div>
      )}

      {metrics && (
        <div className="cards-grid">
          <Card
            label="Total Packets"
            value={metrics.totalPackets.toLocaleString()}
            icon="📦"
            iconColor="blue"
          />
          <Card
            label="Total Flows"
            value={metrics.totalFlows.toLocaleString()}
            icon="🔀"
            iconColor="green"
            valueColor="success"
          />
          <Card
            label="Blocked Flows"
            value={metrics.blockedFlows.toLocaleString()}
            icon="🚫"
            iconColor="red"
            valueColor="danger"
          />
        </div>
      )}

      <div style={{ fontSize: "0.78rem", color: "var(--text-muted)", marginTop: 8 }}>
        Auto-refreshes every 10 seconds.{" "}
        <button
          onClick={fetchMetrics}
          style={{
            background: "none",
            border: "none",
            color: "var(--accent-light)",
            cursor: "pointer",
            fontSize: "inherit",
            padding: 0,
          }}
        >
          Refresh now ↻
        </button>
      </div>
    </>
  );
}
