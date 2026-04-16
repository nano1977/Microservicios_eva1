import { useState, useEffect } from 'react';
import { logisticaService } from '../services/logisticaService';
import '../styles/CRUD.css';

export default function Vehiculos() {
  const [vehiculos, setVehiculos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [formData, setFormData] = useState({
    patente: '',
    modelo: '',
    chofer: '',
    capacidadCarga: '',
    estado: 'Disponible',
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    loadVehiculos();
  }, []);

  const loadVehiculos = async () => {
    try {
      const response = await logisticaService.getVehiculos();
      setVehiculos(response.data);
      setError('');
    } catch (err) {
      setError('Error cargando vehículos');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    try {
      if (editingId) {
        await logisticaService.actualizarVehiculo(editingId, formData);
        setSuccess('✅ Vehículo actualizado');
      } else {
        await logisticaService.crearVehiculo(formData);
        setSuccess('✅ Vehículo creado');
      }

      setFormData({
        patente: '',
        modelo: '',
        chofer: '',
        capacidadCarga: '',
        estado: 'Disponible',
      });
      setShowForm(false);
      setEditingId(null);
      loadVehiculos();
      setTimeout(() => setSuccess(''), 3000);
    } catch (err) {
      setError(err.response?.data?.message || 'Error guardando vehículo');
    }
  };

  const handleEdit = (vehiculo) => {
    setFormData(vehiculo);
    setEditingId(vehiculo.id);
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('¿Eliminar este vehículo?')) {
      try {
        await logisticaService.eliminarVehiculo(id);
        setSuccess('✅ Vehículo eliminado');
        loadVehiculos();
        setTimeout(() => setSuccess(''), 3000);
      } catch (err) {
        setError('Error eliminando vehículo');
      }
    }
  };

  return (
    <div className="crud-container">
      <div className="crud-header">
        <h2>🚗 Gestión de Vehículos</h2>
        <button
          onClick={() => {
            setShowForm(!showForm);
            setEditingId(null);
            setFormData({
              patente: '',
              modelo: '',
              chofer: '',
              capacidadCarga: '',
              estado: 'Disponible',
            });
          }}
          className="btn-primary"
        >
          {showForm ? 'Cancelar' : '+ Nuevo Vehículo'}
        </button>
      </div>

      {error && <div className="error-message">{error}</div>}
      {success && <div className="success-message">{success}</div>}

      {showForm && (
        <form onSubmit={handleSubmit} className="form-card">
          <h3>{editingId ? 'Editar Vehículo' : 'Nuevo Vehículo'}</h3>

          <div className="form-row">
            <div className="form-group">
              <label>Patente *</label>
              <input
                type="text"
                value={formData.patente}
                onChange={(e) =>
                  setFormData({ ...formData, patente: e.target.value })
                }
                required
              />
            </div>
            <div className="form-group">
              <label>Modelo *</label>
              <input
                type="text"
                value={formData.modelo}
                onChange={(e) =>
                  setFormData({ ...formData, modelo: e.target.value })
                }
                required
              />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>Chofer *</label>
              <input
                type="text"
                value={formData.chofer}
                onChange={(e) =>
                  setFormData({ ...formData, chofer: e.target.value })
                }
                required
              />
            </div>
            <div className="form-group">
              <label>Capacidad (kg) *</label>
              <input
                type="number"
                value={formData.capacidadCarga}
                onChange={(e) =>
                  setFormData({ ...formData, capacidadCarga: e.target.value })
                }
                required
              />
            </div>
          </div>

          <div className="form-group">
            <label>Estado *</label>
            <select
              value={formData.estado}
              onChange={(e) =>
                setFormData({ ...formData, estado: e.target.value })
              }
            >
              <option>Disponible</option>
              <option>En Ruta</option>
              <option>Mantenimiento</option>
            </select>
          </div>

          <button type="submit" className="btn-primary">
            {editingId ? 'Actualizar' : 'Crear'}
          </button>
        </form>
      )}

      {loading ? (
        <p>Cargando vehículos...</p>
      ) : (
        <div className="table-responsive">
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Patente</th>
                <th>Modelo</th>
                <th>Chofer</th>
                <th>Capacidad (kg)</th>
                <th>Estado</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {vehiculos.map((v) => (
                <tr key={v.id}>
                  <td>{v.id}</td>
                  <td>
                    <strong>{v.patente}</strong>
                  </td>
                  <td>{v.modelo}</td>
                  <td>{v.chofer}</td>
                  <td>{v.capacidadCarga}</td>
                  <td>
                    <span className={`status ${(v.estado || "").toLowerCase()}`}>
                      {v.estado || "Sin estado"}
                    </span>
                  </td>
                  <td>
                    <button
                      onClick={() => handleEdit(v)}
                      className="btn-small btn-edit"
                    >
                      ✏️ Editar
                    </button>
                    <button
                      onClick={() => handleDelete(v.id)}
                      className="btn-small btn-delete"
                    >
                      🗑️ Eliminar
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
