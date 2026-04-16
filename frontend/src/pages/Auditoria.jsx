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
    r.numeroTicket?.includes(filtro) ||
    r.nombreDonante?.toLowerCase().includes(filtro.toLowerCase()) ||
    r.nombreReceptor?.toLowerCase().includes(filtro.toLowerCase()) ||
    r.usuario?.toLowerCase().includes(filtro.toLowerCase()) ||
    r.tipoRecurso?.toLowerCase().includes(filtro.toLowerCase())
  );

  return (
    <div className="crud-container">
      <div className="crud-header">
        <h2>📋 Auditoría - Registro de Acciones</h2>
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
          placeholder="🔍 Buscar por ticket, usuario, recurso..."
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
                <th>👤 Usuario</th>
                <th>👥 Rol</th>
                <th>📦 Recurso</th>
                <th>🔎 ID Recurso</th>
                <th>⚙️ Acción</th>
                <th>🏷️ Donante</th>
                <th>🏷️ Receptor</th>
                <th>📍 Centro</th>
                <th>📅 Fecha/Hora</th>
              </tr>
            </thead>
            <tbody>
              {registrosFiltrados.map((r) => (
                <tr key={r.id} className="audit-row">
                  <td>
                    <code className="ticket-code">{r.numeroTicket}</code>
                  </td>
                  <td>
                    <strong>{r.usuario}</strong>
                  </td>
                  <td>{r.rol}</td>
                  <td>{r.tipoRecurso}</td>
                  <td>{r.idRecurso}</td>
                  <td>{r.accion}</td>
                  <td>{r.nombreDonante}</td>
                  <td>{r.nombreReceptor}</td>
                  <td>{r.centroDonacion}</td>
                  <td>
                    {r.timestamp ? new Date(r.timestamp).toLocaleString('es-CL') : ''}
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
          <li>✅ Cada acción genera trazabilidad completa de quién hizo qué y cuándo</li>
          <li>✅ Registros de cambios con detalles antes/después cuando aplica</li>
          <li>✅ Timestamps precisos para cada operación</li>
        </ul>
      </div>
    </div>
  );
}
