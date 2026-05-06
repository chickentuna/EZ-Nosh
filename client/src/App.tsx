import { useState, useEffect } from 'react';
import type { Recipe, Ingredient, MealState, Phase } from './types';
import Nav from './components/Nav';
import MenuPage from './components/MenuPage';
import { apiUrl } from './lib/api';

const DAYS = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];

const GOUTER_DAYS = new Set(['Monday', 'Tuesday', 'Thursday', 'Friday']);

const MEALS_BY_DAY: Record<string, string[]> = Object.fromEntries(
  DAYS.map(day => [
    day,
    GOUTER_DAYS.has(day)
      ? ['Breakfast', 'Lunch', 'Goûter', 'Dinner']
      : ['Breakfast', 'Lunch', 'Dinner'],
  ])
);

const DEFAULT_CATEGORIES: Record<string, string[]> = {
  Monday:    ['improv', 'lunchbox',   'improv', 'chef'],
  Tuesday:   ['improv', 'lunchbox',   'improv', 'express'],
  Wednesday: ['improv', 'kiddy chef', 'chef'],
  Thursday:  ['improv', 'lunchbox',   'improv', 'express'],
  Friday:    ['improv', 'lunchbox',   'improv', 'chef'],
  Saturday:  ['improv', 'kiddy chef', 'chef'],
  Sunday:    ['improv', 'chef',       'express'],
};

function buildInitialWeek(): MealState[][] {
  return DAYS.map(day =>
    MEALS_BY_DAY[day].map((_, m) => ({ category: DEFAULT_CATEGORIES[day][m], recipeId: '', people: 4 }))
  );
}

export default function App() {
  const [categories, setCategories] = useState<string[]>([]);
  const [recipes, setRecipes] = useState<Recipe[]>([]);
  const [ingredients, setIngredients] = useState<Ingredient[]>([]);
  const [week, setWeek] = useState<MealState[][] | null>(null);
  const [phase, setPhase] = useState<Phase>('categories');

  useEffect(() => {
    Promise.all([
      fetch(apiUrl('/api/categories')).then(r => r.json()) as Promise<string[]>,
      fetch(apiUrl('/api/recipes')).then(r => r.json()) as Promise<Recipe[]>,
      fetch(apiUrl('/api/ingredients')).then(r => r.json()) as Promise<Ingredient[]>,
    ]).then(([cats, recs, ings]) => {
      setCategories(cats);
      setRecipes(recs);
      setIngredients(ings);
      setWeek(buildInitialWeek());
    });
  }, []);

  function handleGenerate() {
    if (!week) return;
    setWeek(week.map(day =>
      day.map(meal => {
        const byCategory = recipes.filter(r => r.category === meal.category);
        const fits = byCategory.filter(r => meal.people === 0 || meal.people % r.servings === 0);
        const pool = fits.length > 0 ? fits : byCategory;
        const picked = pool[Math.floor(Math.random() * pool.length)];
        return { ...meal, recipeId: picked?.id ?? '' };
      })
    ));
    setPhase('recipes');
  }

  function handleReset() {
    if (!week) return;
    setWeek(week.map(day => day.map(meal => ({ ...meal, recipeId: '' }))));
    setPhase('categories');
  }

  function updateMeal(dayIdx: number, mealIdx: number, update: Partial<MealState>) {
    if (!week) return;
    setWeek(week.map((day, d) =>
      day.map((meal, m) => d === dayIdx && m === mealIdx ? { ...meal, ...update } : meal)
    ));
  }

  if (!week) return <p>Loading…</p>;

  return (
    <div>
      <Nav />
      <MenuPage
        days={DAYS}
        mealsByDay={MEALS_BY_DAY}
        week={week}
        categories={categories}
        recipes={recipes}
        ingredients={ingredients}
        phase={phase}
        onGenerate={handleGenerate}
        onReset={handleReset}
        onUpdateMeal={updateMeal}
      />
    </div>
  );
}
