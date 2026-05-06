import { useState, useEffect } from 'react';
import type { Recipe, MealState, Phase } from '../types';

type Props = {
  meal: string;
  state: MealState;
  categories: string[];
  recipes: Recipe[];
  phase: Phase;
  onUpdate: (update: Partial<MealState>) => void;
};

export default function MealRow({ meal, state, categories, recipes, phase, onUpdate }: Props) {
  const activeRecipe = recipes.find(r => r.id === state.recipeId);
  const step = activeRecipe?.servings ?? 1;

  const [inputValue, setInputValue] = useState(activeRecipe?.name ?? '');
  const [open, setOpen] = useState(false);

  useEffect(() => {
    setInputValue(activeRecipe?.name ?? '');
  }, [state.recipeId]); // eslint-disable-line react-hooks/exhaustive-deps

  const suggestions = recipes.filter(r =>
    r.name.toLowerCase().includes(inputValue.toLowerCase())
  );

  function selectRecipe(r: Recipe) {
    const update: Partial<MealState> = { recipeId: r.id, category: r.category };
    if (state.people > 0 && state.people % r.servings !== 0) {
      update.people = Math.ceil(state.people / r.servings) * r.servings;
    }
    onUpdate(update);
    setInputValue(r.name);
    setOpen(false);
  }

  return (
    <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', marginBottom: '0.25rem' }}>
      <span style={{ width: '80px' }}>{meal}</span>

      {phase === 'categories' ? (
        <select value={state.category} onChange={e => onUpdate({ category: e.target.value })}>
          {categories.map(cat => (
            <option key={cat} value={cat}>{cat}</option>
          ))}
        </select>
      ) : (
        <>
          <span style={{ width: '80px', fontSize: '0.8em', color: '#555', fontStyle: 'italic' }}>
            {activeRecipe?.category ?? state.category}
          </span>
          <div style={{ position: 'relative' }}>
            <input
              type="text"
              value={inputValue}
              onChange={e => { setInputValue(e.target.value); setOpen(true); }}
              onFocus={() => setOpen(true)}
              onBlur={() => setTimeout(() => setOpen(false), 150)}
              style={{ width: '180px' }}
            />
            {open && suggestions.length > 0 && (
              <ul style={{
                position: 'absolute',
                top: '100%',
                left: 0,
                background: 'white',
                border: '1px solid #ccc',
                borderRadius: '4px',
                boxShadow: '0 4px 12px rgba(0,0,0,0.15)',
                listStyle: 'none',
                margin: '2px 0 0',
                padding: 0,
                zIndex: 10,
                maxHeight: '200px',
                overflowY: 'auto',
                minWidth: '220px',
              }}>
                {suggestions.map(r => (
                  <li
                    key={r.id}
                    onMouseDown={() => selectRecipe(r)}
                    style={{
                      padding: '0.3rem 0.6rem',
                      cursor: 'pointer',
                      display: 'flex',
                      justifyContent: 'space-between',
                      gap: '1rem',
                    }}
                  >
                    <span>{r.name}</span>
                    <span style={{ fontSize: '0.8em', color: '#888' }}>{r.category}</span>
                  </li>
                ))}
              </ul>
            )}
          </div>
        </>
      )}

      <label style={{ display: 'flex', alignItems: 'center', gap: '0.25rem' }}>
        <span>People:</span>
        <input
          type="number"
          min={0}
          step={step}
          value={state.people}
          style={{ width: '60px' }}
          onChange={e => onUpdate({ people: Math.max(0, parseInt(e.target.value, 10) || 0) })}
        />
      </label>

      {phase === 'recipes' && activeRecipe?.laVeille && (
        <span title="la veille — prepare the day before" style={{ fontSize: '1.2em' }}>🎑</span>
      )}
    </div>
  );
}
