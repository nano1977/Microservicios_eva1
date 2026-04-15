import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authService } from '../services/authService';
import '../styles/Login.css';

export default function Login() {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      // Simular login directamente
      // En caso de backend real, aquí iría el AuthService.login()
      if (email === 'man.alvarezg@duocuc.cl' && password === '12345678') {
        // Guardar token simulado
        sessionStorage.setItem('authToken', 'token_' + Math.random());
        setTimeout(() => navigate('/dashboard'), 500);
      } else {
        setError('❌ Email o contraseña incorrectos');
      }
    } catch (err) {
      setError(err.message || 'Error al iniciar sesión');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <h1>🔐 Microservicio de Donaciones</h1>

        <form onSubmit={handleLogin}>
          <h2>Inicia Sesión</h2>

          <div className="form-group">
            <label>Email</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="man.alvarezg@duocuc.cl"
              required
              autoComplete="email"
            />
          </div>

          <div className="form-group">
            <label>Contraseña</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••"
              required
              autoComplete="current-password"
            />
          </div>

          {error && <div className="error-message">{error}</div>}

          <button type="submit" disabled={loading} className="btn-primary">
            {loading ? 'Ingresando...' : 'Ingresar'}
          </button>
        </form>

        <div className="login-info">
          <p>
            <strong>ℹ️ Credenciales de Prueba:</strong>
          </p>
          <ul>
            <li>Email: <code>man.alvatrezg@duocuc.cl</code></li>
            <li>Contraseña: <code>12345678</code></li>
          </ul>
        </div>
      </div>
    </div>
  );
}
