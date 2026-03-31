interface CardProps {
  label: string;
  value: number | string;
  icon: string;
  iconColor?: "blue" | "green" | "red";
  valueColor?: "danger" | "success" | "warning";
}

export default function Card({ label, value, icon, iconColor = "blue", valueColor }: CardProps) {
  return (
    <div className="card">
      <div className={`card-icon ${iconColor}`}>{icon}</div>
      <div className="card-label">{label}</div>
      <div className={`card-value ${valueColor ?? ""}`}>{value}</div>
    </div>
  );
}
