import { useState, useEffect } from 'react';
import { logisticaService } from '../services/logisticaService';
import '../styles/CRUD.css';

export default function Inventario() {
  const [inventarios, setInventarios] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [formData, setFormData] = useState({
    tipo: 'Alimento',
    cantidad: '',
    peso: '',
    fechaVencimiento: '',
    centrDestino: '',
  });

  useEffect(() => {
    loadInventario();
  }, []);

  const loadInventario = async () => {
    try {
      const response = await logisticaService.getInventario();
      setInventarios(response.data);
    } catch (err) {
      setError('Error cargando inventario');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await logisticaService.crearInventario(formData);
      setSuccess('✅ Inventario creado');
      setFormData({ tipo: 'Alimento', cantidad: '', peso: '', fechaVencimiento: '', centrDestino: '' });
      setShowForm(false);
      loadInventario();
      setTimeout(() => setSuccess(''), 3000);
    } catch (err) {
      setError(err.response?.data?.message || 'Error guardando inventario');
    }
  };

  return (
    <div className="crud-container">
      <div className="crud-header">
        <h2>📊 Inventario</h2>
        <button
          onClick={() => setShowForm(!showForm)}
          className="btn-primary"
        >
          {showForm ? 'Cancelar' : '+ Registrar Recurso'}
        </button>
      </div>

      {error && <div className="error-message">{error}</div>}
      {success && <div className="success-message">{success}</div>}

      {showForm && (
        <form onSubmit={handleSubmit} className="form-card">
          <h3>Registrar Nuevo Recurso</h3>

          <div className="form-row">
            <div className="form-group">
              <label>Tipo de Recurso *</label>
              <select
                value={formData.tipo}
                onChange={(e) => setFormData({ ...formData, tipo: e.target.value })}
              >
                <option>Alimento</option>
                <option>Ropa</option>
                <option>Medicina</option>
              </select>
            </div>
            <div className="form-group">
              <label>Cantidad *</label>
              <input
                type="number"
                value={formData.cantidad}
                onChange={(e) => setFormData({ ...formData, cantidad: e.target.value })}
                required
              />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>Peso (kg) *</label>
              <input
                type="number"
                value={formData.peso}
                onChange={(e) => setFormData({ ...formData, peso: e.target.value })}
                required
              />
            </div>
            <div className="form-group">
              <label>Fecha Vencimiento *</label>
              <input
                type="date"
                value={formData.fechaVencimiento}
                onChange={(e) =>
                  setFormData({ ...formData, fechaVencimiento: e.target.value })
                }
                required
              />
            </div>
          </div>

          <div className="form-group">
            <label>Centro Destino *</label>
            <input
              type="text"
              value={formData.centrDestino}
              onChange={(e) =>
                setFormData({ ...formData, centrDestino: e.target.value })
              }
              placeholder="ID o nombre del centro"
              required
            />
          </div>

          <button type="submit" className="btn-primary">
            Registrar
          </button>
        </form>
      )}

      {loading ? (
        <p>Cargando inventario...</p>
      ) : (
        <div className="table-responsive">
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Tipo</th>
                <th>Cantidad</th>
                <th>Peso (kg)</th>
                <th>Vencimiento</th>
                <th>Centro</th>
              </tr>
            </thead>
            <tbody>
              {inventarios.map((inv) => (
                <tr key={inv.id}>
                  <td>{inv.id}</td>
                  <td>
                    <strong>{inv.tipo}</strong>
                  </td>
                  <td>{inv.cantidad}</td>
                  <td>{inv.peso}</td>
                  <td>{new Date(inv.fechaVencimiento).toLocaleDateString()}</td>
                  <td>{inv.centrDestino}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
