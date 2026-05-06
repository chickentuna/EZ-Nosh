import { Link } from 'react-router-dom';

export default function Nav() {
  return (
    <header style={{ textAlign: 'center', marginBottom: '2rem' }}>
      <h1 style={{
        fontSize: '4rem',
        fontWeight: 900,
        color: '#166534',
        letterSpacing: '-0.03em',
        margin: 0,
        textShadow: '0 3px 12px rgba(0,0,0,0.1)',
        lineHeight: 1,
      }}>
        EZ-Nosh
      </h1>
      <nav style={{ display: 'flex', justifyContent: 'center', gap: '2rem', marginTop: '0.75rem' }}>
        <Link to="/" style={{ color: '#15803d', fontWeight: 600, textDecoration: 'none', fontSize: '1.1rem' }}>Menu</Link>
        <Link to="/recipes" style={{ color: '#15803d', fontWeight: 600, textDecoration: 'none', fontSize: '1.1rem' }}>Recipes</Link>
      </nav>
    </header>
  );
}
