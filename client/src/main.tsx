import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import App from './App';
import RecipeEditor from './pages/RecipeEditor';
import tomato from './assets/tomato.png';
import './index.css';

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <BrowserRouter basename={import.meta.env.BASE_URL}>
      <img
        src={tomato}
        alt=""
        aria-hidden="true"
        style={{
          position: 'fixed',
          bottom: '-40px',
          right: '-40px',
          width: '300px',
          opacity: 1,
          zIndex: 0,
          pointerEvents: 'none',
          transform: 'rotate(12deg)',
        }}
      />
      <Routes>
        <Route path="/" element={<App />} />
        <Route path="/recipes" element={<RecipeEditor />} />
      </Routes>
    </BrowserRouter>
  </React.StrictMode>
);
