import { useState, useEffect, type FormEvent } from 'react';
import type { Recipe, Ingredient } from '../types';
import Nav from '../components/Nav';
import PreparationEditor from '../components/PreparationEditor';
import RecipeCard from '../components/RecipeCard';
import { apiUrl } from '../lib/api';

const IMPROV = 'improv';

type IngredientTypeaheadProps = {
  value: string;
  ingredients: Ingredient[];
  onSelect: (id: string) => void;
  onCreateNew: (typed: string) => void;
};

function IngredientTypeahead({ value, ingredients, onSelect, onCreateNew }: IngredientTypeaheadProps) {
  const active = ingredients.find(i => i.id === value);
  const [inputValue, setInputValue] = useState(active?.name ?? '');
  const [open, setOpen] = useState(false);

  useEffect(() => {
    setInputValue(active?.name ?? '');
  }, [value]); // eslint-disable-line react-hooks/exhaustive-deps

  const suggestions = ingredients.filter(i =>
    i.name.toLowerCase().includes(inputValue.toLowerCase())
  );

  return (
    <div style={{ position: 'relative' }}>
      <input
        type="text"
        placeholder="Search ingredient…"
        value={inputValue}
        onChange={e => { setInputValue(e.target.value); setOpen(true); }}
        onFocus={() => setOpen(true)}
        onBlur={() => setTimeout(() => setOpen(false), 150)}
        style={{ width: '180px' }}
      />
      {open && (
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
          {suggestions.map(i => (
            <li
              key={i.id}
              onMouseDown={() => { onSelect(i.id); setOpen(false); }}
              style={{
                padding: '0.3rem 0.6rem',
                cursor: 'pointer',
                display: 'flex',
                justifyContent: 'space-between',
                gap: '1rem',
              }}
            >
              <span>{i.name}</span>
              <span style={{ fontSize: '0.8em', color: '#888' }}>{i.unit}</span>
            </li>
          ))}
          <li
            onMouseDown={() => { onCreateNew(inputValue); setOpen(false); }}
            style={{
              padding: '0.3rem 0.6rem',
              cursor: 'pointer',
              fontStyle: 'italic',
              color: '#0a0',
              borderTop: suggestions.length > 0 ? '1px solid #eee' : 'none',
            }}
          >
            + New ingredient{inputValue.trim() ? ` "${inputValue.trim()}"` : ''}
          </li>
        </ul>
      )}
    </div>
  );
}

type IngredientRow = {
  ingredientId: string;
  quantity: number;
  creatingNew: boolean;
  newName: string;
  newUnit: string;
};

type FormState = {
  name: string;
  category: string;
  servings: number;
  laVeille: boolean;
  instructions: string;
  toValidate: boolean;
  ingredientRows: IngredientRow[];
};

function emptyForm(defaultCategory: string): FormState {
  return { name: '', category: defaultCategory, servings: 2, laVeille: false, instructions: '', toValidate: false, ingredientRows: [] };
}

export default function RecipeEditor() {
  const [categories, setCategories] = useState<string[]>([]);
  const [ingredients, setIngredients] = useState<Ingredient[]>([]);
  const [recipes, setRecipes] = useState<Recipe[]>([]);
  const [form, setForm] = useState<FormState>(emptyForm(''));
  const [editingId, setEditingId] = useState<string | null>(null);
  const [viewing, setViewing] = useState<Recipe | null>(null);
  const [formVersion, setFormVersion] = useState(0);

  useEffect(() => {
    Promise.all([
      fetch(apiUrl('/api/categories')).then(r => r.json()) as Promise<string[]>,
      fetch(apiUrl('/api/ingredients')).then(r => r.json()) as Promise<Ingredient[]>,
      fetch(apiUrl('/api/recipes')).then(r => r.json()) as Promise<Recipe[]>,
    ]).then(([cats, ings, recs]) => {
      setCategories(cats);
      setIngredients(ings);
      setRecipes(recs);
      setForm(emptyForm(cats[0] ?? ''));
    });
  }, []);

  function startEdit(recipe: Recipe) {
    setEditingId(recipe.id);
    setFormVersion(v => v + 1);
    setForm({
      name: recipe.name,
      category: recipe.category,
      servings: recipe.servings,
      laVeille: recipe.laVeille,
      instructions: recipe.instructions ?? '',
      toValidate: recipe.toValidate ?? false,
      ingredientRows: recipe.ingredients.map(i => ({
        ingredientId: i.ingredientId,
        quantity: i.quantity,
        creatingNew: false,
        newName: '',
        newUnit: '',
      })),
    });
  }

  function cancelEdit() {
    setEditingId(null);
    setFormVersion(v => v + 1);
    setForm(emptyForm(categories[0] ?? ''));
  }

  async function handleDelete(id: string) {
    await fetch(apiUrl(`/api/recipes/${id}`), { method: 'DELETE' });
    setRecipes(rs => rs.filter(r => r.id !== id));
    if (editingId === id) cancelEdit();
  }

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    const body = {
      name: form.name,
      category: form.category,
      servings: form.servings,
      laVeille: form.laVeille,
      instructions: form.instructions,
      toValidate: form.toValidate,
      ingredients: form.ingredientRows
        .filter(row => !row.creatingNew && row.ingredientId)
        .map(({ ingredientId, quantity }) => ({ ingredientId, quantity })),
    };

    if (editingId) {
      const res = await fetch(apiUrl(`/api/recipes/${editingId}`), {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
      });
      const updated: Recipe = await res.json();
      setRecipes(rs => rs.map(r => r.id === editingId ? updated : r));
      cancelEdit();
    } else {
      const res = await fetch(apiUrl('/api/recipes'), {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
      });
      const created: Recipe = await res.json();
      setRecipes(rs => [...rs, created]);
      setForm(emptyForm(categories[0] ?? ''));
      setFormVersion(v => v + 1);
    }
  }

  function addRow() {
    setForm(f => ({
      ...f,
      ingredientRows: [...f.ingredientRows, {
        ingredientId: '',
        quantity: 1,
        creatingNew: false,
        newName: '',
        newUnit: '',
      }],
    }));
  }

  function removeRow(idx: number) {
    setForm(f => ({ ...f, ingredientRows: f.ingredientRows.filter((_, i) => i !== idx) }));
  }

  function updateRow(idx: number, patch: Partial<IngredientRow>) {
    setForm(f => ({
      ...f,
      ingredientRows: f.ingredientRows.map((row, i) => i === idx ? { ...row, ...patch } : row),
    }));
  }

  async function saveNewIngredient(rowIdx: number) {
    const row = form.ingredientRows[rowIdx];
    if (!row.newName.trim() || !row.newUnit.trim()) return;
    const res = await fetch(apiUrl('/api/ingredients'), {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name: row.newName.trim(), unit: row.newUnit.trim() }),
    });
    const created: Ingredient = await res.json();
    setIngredients(ings => [...ings, created]);
    updateRow(rowIdx, { ingredientId: created.id, creatingNew: false, newName: '', newUnit: '' });
  }

  return (
    <div>
      <Nav />
      <h2>Recipe Editor</h2>

      <section style={{ marginBottom: '2rem' }}>
        <h3>{editingId ? 'Edit Recipe' : 'New Recipe'}</h3>
        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
          <label style={{ display: 'flex', flexDirection: 'column', gap: '0.2rem' }}>
            Name
            <input
              type="text"
              required
              value={form.name}
              onChange={e => setForm(f => ({ ...f, name: e.target.value }))}
            />
          </label>

          <label style={{ display: 'flex', flexDirection: 'column', gap: '0.2rem' }}>
            Category
            <select
              value={form.category}
              onChange={e => setForm(f => ({
                ...f,
                category: e.target.value,
                ingredientRows: e.target.value === IMPROV ? [] : f.ingredientRows,
              }))}
            >
              {categories.map(cat => <option key={cat} value={cat}>{cat}</option>)}
            </select>
          </label>

          <label style={{ display: 'flex', flexDirection: 'column', gap: '0.2rem' }}>
            Servings
            <input
              type="number"
              min={1}
              required
              value={form.servings}
              style={{ width: '80px' }}
              onChange={e => setForm(f => ({ ...f, servings: parseInt(e.target.value, 10) || 1 }))}
            />
          </label>

          <label style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <input
              type="checkbox"
              checked={form.laVeille}
              onChange={e => setForm(f => ({ ...f, laVeille: e.target.checked }))}
            />
            La veille (prepare the day before)
          </label>

          {form.category !== IMPROV && (
            <div style={{ marginTop: '0.5rem' }}>
              <strong>Ingredients</strong>
              {form.ingredientRows.map((row, idx) => (
                <div key={idx} style={{ display: 'flex', gap: '0.4rem', alignItems: 'center', marginTop: '0.3rem' }}>
                  {row.creatingNew ? (
                    <>
                      <input
                        placeholder="Name"
                        value={row.newName}
                        onChange={e => updateRow(idx, { newName: e.target.value })}
                        style={{ width: '130px' }}
                      />
                      <input
                        placeholder="Unit"
                        value={row.newUnit}
                        onChange={e => updateRow(idx, { newUnit: e.target.value })}
                        style={{ width: '70px' }}
                      />
                      <button type="button" onClick={() => saveNewIngredient(idx)}>Add</button>
                      <button type="button" onClick={() => updateRow(idx, { creatingNew: false })}>Cancel</button>
                    </>
                  ) : (
                    <>
                      <IngredientTypeahead
                        value={row.ingredientId}
                        ingredients={ingredients}
                        onSelect={id => updateRow(idx, { ingredientId: id })}
                        onCreateNew={typed => updateRow(idx, { creatingNew: true, newName: typed.trim(), newUnit: '' })}
                      />
                      {row.ingredientId && (
                        <span style={{ fontSize: '0.8em', color: '#888', minWidth: '40px' }}>
                          {ingredients.find(i => i.id === row.ingredientId)?.unit}
                        </span>
                      )}
                      <input
                        type="number"
                        step={0.5}
                        min={0}
                        value={row.quantity}
                        style={{ width: '70px' }}
                        onChange={e => updateRow(idx, { quantity: parseFloat(e.target.value) || 0 })}
                      />
                      <button type="button" onClick={() => removeRow(idx)}>✕</button>
                    </>
                  )}
                </div>
              ))}
              <button type="button" onClick={addRow} style={{ marginTop: '0.5rem' }}>
                + Add ingredient
              </button>
            </div>
          )}

          <div style={{ display: 'flex', flexDirection: 'column', gap: '0.2rem', marginTop: '0.5rem' }}>
            <strong>Preparation</strong>
            <PreparationEditor
              key={formVersion}
              value={form.instructions}
              onChange={md => setForm(f => ({ ...f, instructions: md }))}
            />
          </div>

          <label style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <input
              type="checkbox"
              checked={form.toValidate}
              onChange={e => setForm(f => ({ ...f, toValidate: e.target.checked }))}
            />
            ⚠️ To validate (untick once checked)
          </label>

          <div style={{ display: 'flex', gap: '0.5rem', marginTop: '0.75rem' }}>
            <button type="submit">{editingId ? 'Update' : 'Create'}</button>
            {editingId && <button type="button" onClick={cancelEdit}>Cancel</button>}
          </div>
        </form>
      </section>

      <section>
        <h3>All Recipes</h3>
        {recipes.length === 0 ? (
          <p>No recipes yet.</p>
        ) : (
          <table style={{ borderCollapse: 'collapse', width: '100%' }}>
            <thead>
              <tr style={{ borderBottom: '2px solid #ddd' }}>
                <th style={{ textAlign: 'left', padding: '0.3rem 0.5rem' }}>Name</th>
                <th style={{ textAlign: 'left', padding: '0.3rem 0.5rem' }}>Category</th>
                <th style={{ textAlign: 'left', padding: '0.3rem 0.5rem' }}>Servings</th>
                <th style={{ textAlign: 'left', padding: '0.3rem 0.5rem' }}>La veille</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {recipes.map(r => (
                <tr key={r.id} style={{ borderBottom: '1px solid #eee' }}>
                  <td style={{ padding: '0.3rem 0.5rem' }}>
                    {r.name}
                    {r.toValidate && <span title="To validate" style={{ marginLeft: '0.4rem', fontSize: '0.8em', color: '#b45309' }}>⚠️ à valider</span>}
                  </td>
                  <td style={{ padding: '0.3rem 0.5rem' }}>{r.category}</td>
                  <td style={{ padding: '0.3rem 0.5rem' }}>{r.servings}</td>
                  <td style={{ padding: '0.3rem 0.5rem' }}>{r.laVeille ? '✓' : ''}</td>
                  <td style={{ padding: '0.3rem 0.5rem', display: 'flex', gap: '0.4rem' }}>
                    <button onClick={() => setViewing(r)}>View</button>
                    <button onClick={() => startEdit(r)}>Edit</button>
                    <button onClick={() => handleDelete(r.id)}>Delete</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </section>

      {viewing && (
        <RecipeCard recipe={viewing} ingredients={ingredients} onClose={() => setViewing(null)} />
      )}
    </div>
  );
}
