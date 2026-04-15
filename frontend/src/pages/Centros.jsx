import { useState, useEffect } from 'react';
import { logisticaService } from '../services/logisticaService';
import '../styles/CRUD.css';

export default function Centros() {
  const [centros, setCentros] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [formData, setFormData] = useState({
    nombre: '',
    ubicacion: '',
    emailResponsable: '',
    telefono: '',
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    loadCentros();
  }, []);

  const loadCentros = async () => {
    try {
      const response = await logisticaService.getCentros();
      setCentros(response.data);
      setError('');
    } catch (err) {
      setError('Error cargando centros');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editingId) {
        await logisticaService.actualizarCentro(editingId, formData);
        setSuccess('✅ Centro actualizado');
      } else {
        await logisticaService.crearCentro(formData);
        setSuccess('✅ Centro creado');
      }
      setFormData({ nombre: '', ubicacion: '', emailResponsable: '', telefono: '' });
      setShowForm(false);
      setEditingId(null);
      loadCentros();
      setTimeout(() => setSuccess(''), 3000);
    } catch (err) {
      setError(err.response?.data?.message || 'Error guardando centro');
    }
  };

  const handleEdit = (centro) => {
    setFormData(centro);
    setEditingId(centro.id);
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('¿Eliminar este centro?')) {
      try {
        await logisticaService.eliminarCentro(id);
        setSuccess('✅ Centro eliminado');
        loadCentros();
      } catch (err) {
        setError('Error eliminando centro');
      }
    }
  };

  return (
    <div className="crud-container">
      <div className="crud-header">
        <h2>📦 Centros de Acopio</h2>
        <button
          onClick={() => {
            setShowForm(!showForm);
            setEditingId(null);
            setFormData({ nombre: '', ubicacion: '', emailResponsable: '', telefono: '' });
          }}
          className="btn-primary"
        >
          {showForm ? 'Cancelar' : '+ Nuevo Centro'}
        </button>
      </div>

      {error && <div className="error-message">{error}</div>}
      {success && <div className="success-message">{success}</div>}

      {showForm && (
        <form onSubmit={handleSubmit} className="form-card">
          <h3>{editingId ? 'Editar Centro' : 'Nuevo Centro'}</h3>

          <div className="form-group">
            <label>Nombre *</label>
            <input
              type="text"
              value={formData.nombre}
              onChange={(e) => setFormData({ ...formData, nombre: e.target.value })}
              required
            />
          </div>

          <div className="form-group">
            <label>Ubicación *</label>
            <input
              type="text"
              value={formData.ubicacion}
              onChange={(e) => setFormData({ ...formData, ubicacion: e.target.value })}
              required
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label>Email Responsable *</label>
              <input
                type="email"
                value={formData.emailResponsable}
                onChange={(e) =>
                  setFormData({ ...formData, emailResponsable: e.target.value })
                }
                required
              />
            </div>
            <div className="form-group">
              <label>Teléfono *</label>
              <input
                type="tel"
                value={formData.telefono}
                onChange={(e) =>
                  setFormData({ ...formData, telefono: e.target.value })
                }
                required
              />
            </div>
          </div>

          <button type="submit" className="btn-primary">
            {editingId ? 'Actualizar' : 'Crear'}
          </button>
        </form>
      )}

      {loading ? (
        <p>Cargando centros...</p>
      ) : (
        <div className="table-responsive">
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Ubicación</th>
                <th>Email</th>
                <th>Teléfono</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {centros.map((c) => (
                <tr key={c.id}>
                  <td>{c.id}</td>
                  <td>
                    <strong>{c.nombre}</strong>
                  </td>
                  <td>{c.ubicacion}</td>
                  <td>{c.emailResponsable}</td>
                  <td>{c.telefono}</td>
                  <td>
                    <button
                      onClick={() => handleEdit(c)}
                      className="btn-small btn-edit"
                    >
                      ✏️ Editar
                    </button>
                    <button
                      onClick={() => handleDelete(c.id)}
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
