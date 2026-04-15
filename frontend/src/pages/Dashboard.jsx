import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { authService } from '../services/authService';
import { logisticaService } from '../services/logisticaService';
import '../styles/Dashboard.css';

export default function Dashboard() {
  const navigate = useNavigate();
  const [stats, setStats] = useState({
    vehiculos: 0,
    centros: 0,
    inventario: 0,
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    try {
      const [vehiculos, centros, inventario] = await Promise.all([
        logisticaService.getVehiculos(),
        logisticaService.getCentros(),
        logisticaService.getInventario(),
      ]);

      setStats({
        vehiculos: vehiculos.data.length,
        centros: centros.data.length,
        inventario: inventario.data.length,
      });
    } catch (error) {
      console.error('Error cargando estadísticas:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = async () => {
    await authService.logout();
    navigate('/login');
  };

  return (
    <div className="dashboard-container">
      <header className="navbar">
        <div className="navbar-brand">
          <h1>🚚 Microservicio de Donaciones</h1>
        </div>
        <button onClick={handleLogout} className="btn-logout">
          Cerrar Sesión
        </button>
      </header>

      <nav className="sidebar">
        <ul>
          <li>
            <a href="/vehiculos" className="nav-link">
              🚗 Vehículos
            </a>
          </li>
          <li>
            <a href="/centros" className="nav-link">
              📦 Centros de Acopio
            </a>
          </li>
          <li>
            <a href="/inventario" className="nav-link">
              📊 Inventario
            </a>
          </li>
          <li>
            <a href="/auditoria" className="nav-link">
              📋 Auditoría
            </a>
          </li>
        </ul>
      </nav>

      <main className="dashboard-content">
        <h2>📊 Dashboard</h2>

        {loading ? (
          <p>Cargando estadísticas...</p>
        ) : (
          <div className="stats-grid">
            <div className="stat-card">
              <div className="stat-icon">🚗</div>
              <div className="stat-info">
                <h3>Vehículos</h3>
                <p className="stat-number">{stats.vehiculos}</p>
              </div>
              <a href="/vehiculos" className="stat-link">
                Ver →
              </a>
            </div>

            <div className="stat-card">
              <div className="stat-icon">📦</div>
              <div className="stat-info">
                <h3>Centros de Acopio</h3>
                <p className="stat-number">{stats.centros}</p>
              </div>
              <a href="/centros" className="stat-link">
                Ver →
              </a>
            </div>

            <div className="stat-card">
              <div className="stat-icon">📊</div>
              <div className="stat-info">
                <h3>Inventario</h3>
                <p className="stat-number">{stats.inventario}</p>
              </div>
              <a href="/inventario" className="stat-link">
                Ver →
              </a>
            </div>
          </div>
        )}

        <section className="info-section">
          <h3>ℹ️ Características del Sistema</h3>
          <ul className="features-list">
            <li>✅ Autenticación de 2 factores (Email + SMS)</li>
            <li>✅ Gestión de vehículos con validaciones</li>
            <li>✅ Administración de centros de acopio</li>
            <li>✅ Control de inventario</li>
            <li>✅ Auditoría completa con tickets únicos</li>
            <li>✅ Notificaciones automáticas</li>
            <li>✅ Patrón Strategy (21 validaciones)</li>
            <li>✅ Patrón Factory (creación segura de objetos)</li>
          </ul>
        </section>
      </main>
    </div>
  );
}
