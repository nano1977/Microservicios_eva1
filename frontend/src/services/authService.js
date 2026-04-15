import api from './api';
import Cookie from 'js-cookie';

export const authService = {
  // PASO 1: Login inicial
  login: async (email, password) => {
    try {
      const response = await api.post('/api/auth/login', {
        email,
        contraseña: password,
      });
      // Response contiene: requiresCode: true, tempToken: xxx
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // PASO 2: Verificar código 2FA
  verify2FA: async (email, code, tempToken) => {
    try {
      const response = await api.post('/api/auth/verificar-codigo-2fa', {
        email,
        codigo: code,
        tempToken,
      });
      // Response contiene: token (JWT)
      if (response.data.token) {
        Cookie.set('authToken', response.data.token, { expires: 7 });
      }
      return response.data;
    } catch (error) {
      throw error.response?.data || error;
    }
  },

  // Logout
  logout: async () => {
    Cookie.remove('authToken');
    await api.post('/api/auth/logout').catch(() => {});
  },

  // Verificar si está autenticado
  isAuthenticated: () => {
    return !!Cookie.get('authToken');
  },

  // Obtener token
  getToken: () => Cookie.get('authToken'),
};
