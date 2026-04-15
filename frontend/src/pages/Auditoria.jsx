import { useState, useEffect } from 'react';
import { logisticaService } from '../services/logisticaService';
import '../styles/CRUD.css';

export default function Auditoria() {
  const [registros, setRegistros] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filtro, setFiltro] = useState('');

  useEffect(() => {
    loadAuditoria();
  }, []);

  const loadAuditoria = async () => {
    try {
      const response = await logisticaService.getAuditoria();
      setRegistros(response.data);
    } catch (err) {
      setError('Error cargando auditoría');
    } finally {
      setLoading(false);
    }
  };

  const registrosFiltrados = registros.filter((r) =>
    filtro === '' ||
    r.ticket?.includes(filtro) ||
    r.donante?.toLowerCase().includes(filtro.toLowerCase()) ||
    r.receptor?.toLowerCase().includes(filtro.toLowerCase())
  );

  return (
    <div className="crud-container">
      <div className="crud-header">
        <h2>📋 Auditoría - Registro de Donaciones</h2>
        {registros.length > 0 && (
          <p className="record-count">
            Total: {registros.length} registros inmutables
          </p>
        )}
      </div>

      {error && <div className="error-message">{error}</div>}

      <div className="form-group" style={{ marginBottom: '20px' }}>
        <input
          type="text"
          placeholder="🔍 Buscar por ticket, donante o receptor..."
          value={filtro}
          onChange={(e) => setFiltro(e.target.value)}
          className="search-input"
        />
      </div>

      {loading ? (
        <p>Cargando auditoría...</p>
      ) : registros.length === 0 ? (
        <p>No hay registros de auditoría</p>
      ) : (
        <div className="table-responsive">
          <table className="data-table audit-table">
            <thead>
              <tr>
                <th>🎫 Ticket</th>
                <th>👤 Donante</th>
                <th>📧 Email</th>
                <th>📦 Receptor</th>
                <th>📊 Recurso</th>
                <th>⚖️ Cantidad</th>
                <th>🚗 Vehículo</th>
                <th>📅 Fecha/Hora</th>
              </tr>
            </thead>
            <tbody>
              {registrosFiltrados.map((r) => (
                <tr key={r.id} className="audit-row">
                  <td>
                    <code className="ticket-code">{r.ticket}</code>
                  </td>
                  <td>
                    <strong>{r.donante}</strong>
                  </td>
                  <td>{r.email}</td>
                  <td>{r.receptor}</td>
                  <td>{r.recurso}</td>
                  <td>{r.cantidad}</td>
                  <td>{r.vehiculo}</td>
                  <td>
                    {new Date(r.fechaHora).toLocaleString('es-CL')}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {registrosFiltrados.length === 0 && filtro && (
            <p className="no-results">No se encontraron resultados para "{filtro}"</p>
          )}
        </div>
      )}

      <div className="audit-info">
        <h3>🔒 Información de Auditoría</h3>
        <ul>
          <li>✅ Todos los registros son <strong>inmutables</strong> (no se pueden modificar)</li>
          <li>✅ Cada donación genera un <strong>ticket único</strong> formato DON-YYYYMMDD-XXXXX</li>
          <li>✅ Trazabilidad 100% del donante al receptor final</li>
          <li>✅ Registros de vehículos asignados automáticamente</li>
          <li>✅ Timestamps precisos para cada operación</li>
          <li>✅ Integración con notificaciones por email/SMS</li>
        </ul>
      </div>
    </div>
  );
}
