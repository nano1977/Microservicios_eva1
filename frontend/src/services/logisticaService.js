import api from './api';

export const logisticaService = {
  // VEHÍCULOS
  getVehiculos: () => api.get('/api/logistica/vehiculos'),
  crearVehiculo: (data) => api.post('/api/logistica/vehiculos', data),
  actualizarVehiculo: (id, data) => api.put(`/api/logistica/vehiculos/${id}`, data),
  eliminarVehiculo: (id) => api.delete(`/api/logistica/vehiculos/${id}`),

  // CENTROS DE ACOPIO
  getCentros: () => api.get('/api/logistica/centros'),
  crearCentro: (data) => api.post('/api/logistica/centros', data),
  actualizarCentro: (id, data) => api.put(`/api/logistica/centros/${id}`, data),
  eliminarCentro: (id) => api.delete(`/api/logistica/centros/${id}`),

  // INVENTARIO
  getInventario: () => api.get('/api/logistica/inventario'),
  crearInventario: (data) => api.post('/api/logistica/inventario', data),
  actualizarInventario: (id, data) =>
    api.put(`/api/logistica/inventario/${id}`, data),
  eliminarInventario: (id) => api.delete(`/api/logistica/inventario/${id}`),

  // AUDITORÍA
  getAuditoria: () => api.get('/api/auditoria'),
};
