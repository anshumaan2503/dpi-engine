"use client";

import { useEffect, useState } from "react";

const API = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

interface Flow {
  id?: number | string;
  sourceIp: string;
  destinationIp: string;
  destinationPort: number;
  blocked: boolean;
}

export default function FlowsPage() {
  const [flows, setFlows] = useState<Flow[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchFlows = async () => {
    setLoading(true);
    setError(null);
    try {
      // ✅ FIXED ENDPOINT
      const res = await fetch(`${API}/dpi/flows`);

      if (!res.ok) throw new Error(`Server returned ${res.status}`);

      const data: Flow[] = await res.json();
      setFlows(data);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Failed to fetch flows");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchFlows();
  }, []);

  return (
    <>
      <div className="page-header">
        <h1>Network Flows</h1>
        <p>All inspected network flows captured by the DPI engine</p>
      </div>

      {error && (
        <div className="error-box">
          ⚠️ Could not reach backend: {error}. Make sure the Spring Boot server is running on port 8080.
        </div>
      )}

      {loading ? (
        <div className="loading">
          <span className="spinner" /> Loading flows…
        </div>
      ) : (
        <div className="table-wrapper">
          <div className="table-title">
            Flows{" "}
            <span style={{ color: "var(--text-muted)", fontWeight: 400, fontSize: "0.8rem" }}>
              ({flows.length} total)
            </span>
            <button
              onClick={fetchFlows}
              style={{
                float: "right",
                background: "none",
                border: "none",
                color: "var(--accent-light)",
                cursor: "pointer",
                fontSize: "0.8rem",
              }}
            >
              ↻ Refresh
            </button>
          </div>

          {flows.length === 0 ? (
            <div className="empty">No flows recorded yet.</div>
          ) : (
            <table>
              <thead>
                <tr>
                  <th>Source IP</th>
                  <th>Destination IP</th>
                  <th>Dest. Port</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {flows.map((flow, i) => (
                  <tr key={flow.id ?? i}>
                    <td>
                      <code style={{ fontSize: "0.82rem" }}>{flow.sourceIp}</code>
                    </td>
                    <td>
                      <code style={{ fontSize: "0.82rem" }}>{flow.destinationIp}</code>
                    </td>
                    <td>{flow.destinationPort}</td>
                    <td>
                      {flow.blocked ? (
                        <span className="badge blocked">🚫 Blocked</span>
                      ) : (
                        <span className="badge allowed">✅ Allowed</span>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}
    </>
  );
}