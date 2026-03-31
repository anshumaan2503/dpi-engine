"use client";

import { useEffect, useState, useCallback } from "react";
import RuleForm from "@/components/RuleForm";

const API = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

interface Rule {
  id: number | string;
  sourceIp?: string;
  destinationIp?: string;
  destinationPort?: number;
  action: string;
}

export default function RulesPage() {
  const [rules, setRules] = useState<Rule[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [deletingId, setDeletingId] = useState<number | string | null>(null);

  const fetchRules = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch(`${API}/api/rules`);
      if (!res.ok) throw new Error(`Server returned ${res.status}`);
      const data: Rule[] = await res.json();
      setRules(data);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Failed to fetch rules");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchRules();
  }, [fetchRules]);

  const handleDelete = async (id: number | string) => {
    if (!confirm("Delete this rule?")) return;
    setDeletingId(id);
    try {
      const res = await fetch(`${API}/api/rules/${id}`, { method: "DELETE" });
      if (!res.ok) throw new Error(`Server returned ${res.status}`);
      setRules((prev) => prev.filter((r) => r.id !== id));
    } catch (err: unknown) {
      alert(err instanceof Error ? err.message : "Failed to delete rule");
    } finally {
      setDeletingId(null);
    }
  };

  return (
    <>
      <div className="page-header">
        <h1>Firewall Rules</h1>
        <p>Manage DPI filtering rules — block or allow traffic by IP and port</p>
      </div>

      <RuleForm onRuleAdded={fetchRules} />

      {error && (
        <div className="error-box">
          ⚠️ Could not reach backend: {error}. Make sure the Spring Boot server is running on port 8080.
        </div>
      )}

      <div className="section">
        <div className="section-title">
          Active Rules{" "}
          <span style={{ color: "var(--text-muted)", fontWeight: 400, fontSize: "0.8rem" }}>
            ({rules.length})
          </span>
          <button
            onClick={fetchRules}
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

        {loading ? (
          <div className="loading">
            <span className="spinner" /> Loading rules…
          </div>
        ) : rules.length === 0 ? (
          <div className="empty">No rules configured. Add one above.</div>
        ) : (
          <div className="rule-list">
            {rules.map((rule) => (
              <div className="rule-item" key={rule.id}>
                <div className="rule-item-info">
                  <span>
                    Action:{" "}
                    <strong style={{ color: rule.action === "BLOCK" ? "var(--danger-light)" : "var(--success)" }}>
                      {rule.action}
                    </strong>
                  </span>
                  {rule.sourceIp && (
                    <span>
                      Src: <strong><code style={{ fontSize: "0.82rem" }}>{rule.sourceIp}</code></strong>
                    </span>
                  )}
                  {rule.destinationIp && (
                    <span>
                      Dst: <strong><code style={{ fontSize: "0.82rem" }}>{rule.destinationIp}</code></strong>
                    </span>
                  )}
                  {rule.destinationPort !== undefined && rule.destinationPort !== null && (
                    <span>
                      Port: <strong>{rule.destinationPort}</strong>
                    </span>
                  )}
                </div>
                <button
                  className="btn btn-danger"
                  onClick={() => handleDelete(rule.id)}
                  disabled={deletingId === rule.id}
                >
                  {deletingId === rule.id ? <><span className="spinner" /> Deleting…</> : "🗑 Delete"}
                </button>
              </div>
            ))}
          </div>
        )}
      </div>
    </>
  );
}
