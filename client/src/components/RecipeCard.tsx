import { createPortal } from 'react-dom';
import Markdown from 'react-markdown';
import remarkBreaks from 'remark-breaks';
import type { Recipe, Ingredient } from '../types';

type Props = {
  recipe: Recipe;
  ingredients: Ingredient[];
  people?: number;
  onClose: () => void;
};

function formatQuantity(qty: number): string {
  return String(Math.round(qty * 100) / 100);
}

export default function RecipeCard({ recipe, ingredients, people, onClose }: Props) {
  const scaleTo = people && people > 0 ? people : recipe.servings;
  const multiplier = scaleTo / recipe.servings;

  return createPortal(
    <div
      onClick={onClose}
      style={{
        position: 'fixed',
        inset: 0,
        background: 'rgba(0,0,0,0.35)',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        zIndex: 1000,
        padding: '1rem',
      }}
    >
      <div
        onClick={e => e.stopPropagation()}
        style={{
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 8px 30px rgba(0,0,0,0.25)',
          padding: '1.25rem 1.5rem',
          maxWidth: '560px',
          width: '100%',
          maxHeight: '85vh',
          overflowY: 'auto',
        }}
      >
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', gap: '1rem' }}>
          <h3 style={{ marginTop: 0 }}>{recipe.name}</h3>
          <button onClick={onClose}>✕</button>
        </div>
        {recipe.toValidate && (
          <p style={{ margin: '0 0 0.5rem', padding: '0.3rem 0.5rem', background: '#fef3c7', color: '#92400e', borderRadius: '4px', fontSize: '0.9em' }}>
            ⚠️ À valider — preparation drafted by Claude, not checked yet
          </p>
        )}
        <p style={{ margin: '0 0 0.75rem', color: '#666', fontSize: '0.9em' }}>
          {recipe.category} — for {scaleTo} people
          {recipe.laVeille && ' — 🎑 la veille (prepare the day before)'}
        </p>

        {recipe.ingredients.length > 0 && (
          <>
            <strong>Ingredients</strong>
            <ul style={{ margin: '0.3rem 0 0.75rem', paddingLeft: '1.2rem' }}>
              {recipe.ingredients.map(ri => {
                const ing = ingredients.find(i => i.id === ri.ingredientId);
                if (!ing) return null;
                return (
                  <li key={ri.ingredientId}>
                    {ing.name}: {formatQuantity(ri.quantity * multiplier)} {ing.unit}
                  </li>
                );
              })}
            </ul>
          </>
        )}

        <strong>Preparation</strong>
        {recipe.instructions?.trim() ? (
          <div style={{ marginTop: '0.3rem', lineHeight: 1.5 }}>
            <Markdown remarkPlugins={[remarkBreaks]}>{recipe.instructions}</Markdown>
          </div>
        ) : (
          <p style={{ marginTop: '0.3rem', color: '#888', fontStyle: 'italic' }}>
            No preparation steps yet — add them in the recipe editor.
          </p>
        )}
      </div>
    </div>,
    document.body
  );
}
