import { useState, useEffect } from 'react';
import type { Recipe, Ingredient, MealState, Course, CourseKey, Phase } from '../types';
import RecipeCard from './RecipeCard';

type Props = {
  meal: string;
  state: MealState;
  categories: string[];
  recipes: Recipe[];
  ingredients: Ingredient[];
  phase: Phase;
  onUpdate: (update: Partial<MealState>) => void;
};

const COURSE_ORDER: CourseKey[] = ['entree', 'dish', 'dessert'];

const LOCKED_CATEGORIES: Partial<Record<CourseKey, string>> = {
  entree: 'entrée',
  dessert: 'dessert',
};

const RESERVED_CATEGORIES = Object.values(LOCKED_CATEGORIES) as string[];

function CourseControls({
  courseKey,
  course,
  categories,
  recipes,
  ingredients,
  phase,
  people,
  onChange,
  onPeopleAdjust,
}: {
  courseKey: CourseKey;
  course: Course;
  categories: string[];
  recipes: Recipe[];
  ingredients: Ingredient[];
  phase: Phase;
  people: number;
  onChange: (update: Partial<Course>) => void;
  onPeopleAdjust: (n: number) => void;
}) {
  const lockedCategory = LOCKED_CATEGORIES[courseKey];
  const availableRecipes = lockedCategory
    ? recipes.filter(r => r.category === lockedCategory)
    : recipes;
  const activeRecipe = recipes.find(r => r.id === course.recipeId);
  const [inputValue, setInputValue] = useState(activeRecipe?.name ?? '');
  const [open, setOpen] = useState(false);
  const [showCard, setShowCard] = useState(false);

  useEffect(() => {
    setInputValue(activeRecipe?.name ?? '');
  }, [course.recipeId]); // eslint-disable-line react-hooks/exhaustive-deps

  const suggestions = availableRecipes.filter(r =>
    r.name.toLowerCase().includes(inputValue.toLowerCase())
  );

  function selectRecipe(r: Recipe) {
    onChange({ recipeId: r.id, category: r.category });
    if (people > 0 && people % r.servings !== 0) {
      onPeopleAdjust(Math.ceil(people / r.servings) * r.servings);
    }
    setInputValue(r.name);
    setOpen(false);
  }

  if (phase === 'categories') {
    if (lockedCategory) {
      return (
        <span style={{ fontSize: '0.85em', color: '#555', fontStyle: 'italic' }}>
          {lockedCategory}
        </span>
      );
    }
    const selectableCategories = categories.filter(c => !RESERVED_CATEGORIES.includes(c));
    return (
      <select value={course.category} onChange={e => onChange({ category: e.target.value })}>
        {selectableCategories.map(cat => (
          <option key={cat} value={cat}>{cat}</option>
        ))}
      </select>
    );
  }

  return (
    <>
      <span style={{ width: '80px', fontSize: '0.8em', color: '#555', fontStyle: 'italic' }}>
        {activeRecipe?.category ?? course.category}
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
      {activeRecipe?.laVeille && (
        <span title="la veille — prepare the day before" style={{ fontSize: '1.2em' }}>🎑</span>
      )}
      {activeRecipe && (
        <button
          type="button"
          title="See the recipe"
          onClick={() => setShowCard(true)}
          style={{ border: 'none', background: 'none', fontSize: '1.1em', padding: 0 }}
        >
          📖
        </button>
      )}
      {showCard && activeRecipe && (
        <RecipeCard
          recipe={activeRecipe}
          ingredients={ingredients}
          people={people}
          onClose={() => setShowCard(false)}
        />
      )}
    </>
  );
}

export default function MealRow({ meal, state, categories, recipes, ingredients, phase, onUpdate }: Props) {
  const step = (() => {
    const courses = [state.entree, state.dish, state.dessert].filter((c): c is Course => !!c);
    const steps = courses
      .map(c => recipes.find(r => r.id === c.recipeId)?.servings)
      .filter((s): s is number => !!s);
    return steps.length > 0 ? steps[0] : 1;
  })();

  function updateCourse(key: CourseKey, update: Partial<Course>) {
    const current = state[key];
    if (!current) return;
    onUpdate({ [key]: { ...current, ...update } } as Partial<MealState>);
  }

  const enabledCourses = COURSE_ORDER.filter(k => state[k] !== null);

  return (
    <div style={{ marginBottom: '0.5rem', paddingBottom: '0.25rem', borderBottom: '1px dashed #eee' }}>
      <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', marginBottom: '0.25rem' }}>
        <span style={{ width: '80px', fontWeight: 'bold' }}>{meal}</span>
        <label style={{ display: 'flex', alignItems: 'center', gap: '0.25rem', marginLeft: 'auto' }}>
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
      </div>

      {enabledCourses.map(key => {
        const course = state[key];
        if (!course) return null;
        return (
          <div
            key={key}
            style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', marginLeft: '90px', marginBottom: '0.2rem' }}
          >
            <CourseControls
              courseKey={key}
              course={course}
              categories={categories}
              recipes={recipes}
              ingredients={ingredients}
              phase={phase}
              people={state.people}
              onChange={update => updateCourse(key, update)}
              onPeopleAdjust={n => onUpdate({ people: n })}
            />
          </div>
        );
      })}
    </div>
  );
}
