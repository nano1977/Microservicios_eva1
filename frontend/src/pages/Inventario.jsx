import { useState, useEffect } from 'react';
import { logisticaService } from '../services/logisticaService';
import '../styles/CRUD.css';

export default function Inventario() {
  const [inventarios, setInventarios] = useState([]);
  const [centros, setCentros] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [formData, setFormData] = useState({
    recurso: 'Alimento',
    cantidad: '',
    unidadMedida: 'Unidades',
    centroAcopioId: '',
    condicion: 'Nueva',
    publico: 'Adulto',
    tallaNino: 'RN',
    tipoAdulto: 'Pantalon',
    tallaAdulto: 'M',
    tipoAlimento: 'Perecible',
    descripcionAlimento: '',
    fechaVencimiento: '',
  });

  const tallasNino = ['RN', '2', '4', '6', '8', '10', '12'];
  const tiposAdulto = ['Pantalon', 'Chaleca', 'Poleron', 'Jean', 'Buzo'];
  const tallasAdulto = ['XS', 'S', 'M', 'L', 'XL', 'XXL', 'XXXL'];

  useEffect(() => {
    loadInventario();
    loadCentros();
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

  const loadCentros = async () => {
    try {
      const response = await logisticaService.getCentros();
      setCentros(response.data);
    } catch (err) {
      setError('Error cargando centros');
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const payload = {
        recurso: formData.recurso,
        cantidad: Number(formData.cantidad),
        unidadMedida: formData.unidadMedida,
        centroAcopio: {
          id: Number(formData.centroAcopioId),
        },
      };
      await logisticaService.crearInventario(payload);
      setSuccess('✅ Inventario creado');
      setFormData({
        recurso: 'Alimento',
        cantidad: '',
        unidadMedida: 'Unidades',
        centroAcopioId: '',
        condicion: 'Nueva',
        publico: 'Adulto',
        tallaNino: 'RN',
        tipoAdulto: 'Pantalon',
        tallaAdulto: 'M',
        tipoAlimento: 'Perecible',
        descripcionAlimento: '',
        fechaVencimiento: '',
      });
      setShowForm(false);
      loadInventario();
      setTimeout(() => setSuccess(''), 3000);
    } catch (err) {
      setError(err.response?.data?.mensaje || err.response?.data?.message || 'Error guardando inventario');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('¿Eliminar este item de inventario?')) {
      try {
        await logisticaService.eliminarInventario(id);
        setSuccess('✅ Inventario eliminado');
        loadInventario();
        setTimeout(() => setSuccess(''), 3000);
      } catch (err) {
        setError('Error eliminando inventario');
      }
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
                value={formData.recurso}
                onChange={(e) => setFormData({ ...formData, recurso: e.target.value })}
              >
                <option>Alimento</option>
                <option>Ropa</option>
                <option>Insumos Médicos</option>
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
              <label>Unidad de Medida *</label>
              <select
                value={formData.unidadMedida}
                onChange={(e) =>
                  setFormData({ ...formData, unidadMedida: e.target.value })
                }
              >
                <option>Unidades</option>
                <option>Kilos</option>
                <option>Cajas</option>
                <option>Paquetes</option>
              </select>
            </div>
          </div>

          {formData.recurso === 'Alimento' && (
            <>
              <div className="form-row">
                <div className="form-group">
                  <label>Tipo de Alimento *</label>
                  <select
                    value={formData.tipoAlimento}
                    onChange={(e) => setFormData({ ...formData, tipoAlimento: e.target.value })}
                  >
                    <option>Perecible</option>
                    <option>No perecible</option>
                  </select>
                </div>
                <div className="form-group">
                  <label>Descripcion *</label>
                  <input
                    type="text"
                    value={formData.descripcionAlimento}
                    onChange={(e) =>
                      setFormData({ ...formData, descripcionAlimento: e.target.value })
                    }
                    placeholder="Ej: arroz, latas de conserva"
                    required
                  />
                </div>
              </div>

              {formData.tipoAlimento === 'Perecible' && (
                <div className="form-row">
                  <div className="form-group">
                    <label>Fecha de Vencimiento *</label>
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
              )}
            </>
          )}

          {formData.recurso === 'Ropa' && (
            <>
              <div className="form-row">
                <div className="form-group">
                  <label>Condicion *</label>
                  <select
                    value={formData.condicion}
                    onChange={(e) => setFormData({ ...formData, condicion: e.target.value })}
                  >
                    <option>Nueva</option>
                    <option>Usada</option>
                  </select>
                </div>
                <div className="form-group">
                  <label>Publico *</label>
                  <select
                    value={formData.publico}
                    onChange={(e) => setFormData({ ...formData, publico: e.target.value })}
                  >
                    <option>Adulto</option>
                    <option>Nino</option>
                  </select>
                </div>
              </div>

              {formData.publico === 'Nino' ? (
                <div className="form-row">
                  <div className="form-group">
                    <label>Talla Nino *</label>
                    <select
                      value={formData.tallaNino}
                      onChange={(e) => setFormData({ ...formData, tallaNino: e.target.value })}
                    >
                      {tallasNino.map((t) => (
                        <option key={t} value={t}>{t}</option>
                      ))}
                    </select>
                  </div>
                </div>
              ) : (
                <div className="form-row">
                  <div className="form-group">
                    <label>Tipo *</label>
                    <select
                      value={formData.tipoAdulto}
                      onChange={(e) => setFormData({ ...formData, tipoAdulto: e.target.value })}
                    >
                      {tiposAdulto.map((t) => (
                        <option key={t} value={t}>{t}</option>
                      ))}
                    </select>
                  </div>
                  <div className="form-group">
                    <label>Talla *</label>
                    <select
                      value={formData.tallaAdulto}
                      onChange={(e) => setFormData({ ...formData, tallaAdulto: e.target.value })}
                    >
                      {tallasAdulto.map((t) => (
                        <option key={t} value={t}>{t}</option>
                      ))}
                    </select>
                  </div>
                </div>
              )}
            </>
          )}

          <div className="form-group">
            <label>Centro Destino *</label>
            <select
              value={formData.centroAcopioId}
              onChange={(e) =>
                setFormData({ ...formData, centroAcopioId: e.target.value })
              }
              required
            >
              <option value="">Selecciona un centro</option>
              {centros.map((centro) => (
                <option key={centro.id} value={centro.id}>
                  {centro.nombre}
                </option>
              ))}
            </select>
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
                <th>Unidad</th>
                <th>Centro</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {inventarios.map((inv) => (
                <tr key={inv.id}>
                  <td>{inv.id}</td>
                  <td>
                    <strong>{inv.recurso}</strong>
                  </td>
                  <td>{inv.cantidad}</td>
                  <td>{inv.unidadMedida}</td>
                  <td>{inv.centroAcopio?.nombre || inv.centroAcopio?.id || 'Sin centro'}</td>
                  <td>
                    <button
                      onClick={() => handleDelete(inv.id)}
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
