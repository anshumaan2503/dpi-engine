"use client";

import { useState } from "react";

const API = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

interface RuleFormProps {
  onRuleAdded: () => void;
}

export default function RuleForm({ onRuleAdded }: RuleFormProps) {
  const [form, setForm] = useState({
    sourceIp: "",
    destinationIp: "",
    destinationPort: "",
    action: "BLOCK",
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
    setError(null);
    setSuccess(false);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSuccess(false);

    try {
      const body: Record<string, unknown> = {
        action: form.action,
      };
      if (form.sourceIp.trim()) body.sourceIp = form.sourceIp.trim();
      if (form.destinationIp.trim()) body.destinationIp = form.destinationIp.trim();
      if (form.destinationPort.trim()) body.destinationPort = parseInt(form.destinationPort.trim(), 10);

      const res = await fetch(`${API}/api/rules`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body),
      });

      if (!res.ok) {
        const msg = await res.text();
        throw new Error(msg || `HTTP ${res.status}`);
      }

      setForm({ sourceIp: "", destinationIp: "", destinationPort: "", action: "BLOCK" });
      setSuccess(true);
      onRuleAdded();
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Failed to create rule");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="section">
      <div className="section-title">➕ Add New Rule</div>
      <form onSubmit={handleSubmit}>
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="sourceIp">Source IP</label>
            <input
              id="sourceIp"
              name="sourceIp"
              type="text"
              placeholder="e.g. 192.168.1.1"
              value={form.sourceIp}
              onChange={handleChange}
            />
          </div>
          <div className="form-group">
            <label htmlFor="destinationIp">Destination IP</label>
            <input
              id="destinationIp"
              name="destinationIp"
              type="text"
              placeholder="e.g. 10.0.0.1"
              value={form.destinationIp}
              onChange={handleChange}
            />
          </div>
          <div className="form-group">
            <label htmlFor="destinationPort">Destination Port</label>
            <input
              id="destinationPort"
              name="destinationPort"
              type="number"
              placeholder="e.g. 443"
              value={form.destinationPort}
              onChange={handleChange}
              min={0}
              max={65535}
            />
          </div>
          <div className="form-group">
            <label htmlFor="action">Action</label>
            <select id="action" name="action" value={form.action} onChange={handleChange}>
              <option value="BLOCK">BLOCK</option>
              <option value="ALLOW">ALLOW</option>
            </select>
          </div>
          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? <><span className="spinner" /> Adding…</> : "Add Rule"}
          </button>
        </div>
      </form>

      {error && <div className="error-box">⚠️ {error}</div>}
      {success && (
        <div style={{ marginTop: 12, color: "var(--success)", fontSize: "0.875rem" }}>
          ✅ Rule created successfully.
        </div>
      )}
    </div>
  );
}
