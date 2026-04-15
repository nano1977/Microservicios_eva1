import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Dashboard from './pages/Dashboard';
import Vehiculos from './pages/Vehiculos';
import Centros from './pages/Centros';
import Inventario from './pages/Inventario';
import Auditoria from './pages/Auditoria';
import './styles/Common.css';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/vehiculos" element={<Vehiculos />} />
        <Route path="/centros" element={<Centros />} />
        <Route path="/inventario" element={<Inventario />} />
        <Route path="/auditoria" element={<Auditoria />} />
      </Routes>
    </Router>
  );
}

export default App;
