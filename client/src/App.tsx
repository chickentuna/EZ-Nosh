import { useState, useEffect } from 'react';
import type { Recipe, Ingredient, MealState, Course, Phase } from './types';
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
  Monday:    ['petit-déj', 'lunchbox',   'improv', 'chef'],
  Tuesday:   ['petit-déj', 'lunchbox',   'improv', 'express'],
  Wednesday: ['petit-déj', 'kiddy chef', 'chef'],
  Thursday:  ['petit-déj', 'lunchbox',   'improv', 'express'],
  Friday:    ['petit-déj', 'lunchbox',   'improv', 'chef'],
  Saturday:  ['improv',    'kiddy chef', 'chef'],
  Sunday:    ['improv',    'chef',       'express'],
};

// Meals that start ticked as "None" (nothing planned).
const SKIPPED_BY_DEFAULT: Record<string, string[]> = {
  Monday: ['Lunch'],
  Tuesday: ['Lunch'],
  Thursday: ['Lunch'],
  Friday: ['Lunch'],
};

function buildInitialWeek(): MealState[][] {
  return DAYS.map(day =>
    MEALS_BY_DAY[day].map((meal, m) => ({
      people: 4,
      skipped: SKIPPED_BY_DEFAULT[day]?.includes(meal) ?? false,
      dish: { category: DEFAULT_CATEGORIES[day][m], recipeId: '' },
      entree: null,
      dessert: null,
    }))
  );
}

// What the previous meals constrain when picking the next one.
type VarietyContext = {
  previousMains: Set<string>;
  recentRecipeIds: Set<string>;
};

function mainIngredientIds(recipe: Recipe | undefined): string[] {
  return (recipe?.ingredients ?? []).filter(i => i.main).map(i => i.ingredientId);
}

function randomOf<T>(items: T[]): T {
  return items[Math.floor(Math.random() * items.length)];
}

// Prefers recipes sharing no main ingredient with the previous meal and not served the same day or the
// day before; relaxes those rules one at a time when the category has nothing left that satisfies them.
function pickRecipeForCourse(course: Course, people: number, recipes: Recipe[], variety: VarietyContext): string {
  const byCategory = recipes.filter(r => r.category === course.category && !r.disabled);
  const fits = byCategory.filter(r => people === 0 || people % r.servings === 0);
  const pool = fits.length > 0 ? fits : byCategory;
  if (pool.length === 0) return '';
  const notRecent = pool.filter(r => !variety.recentRecipeIds.has(r.id));
  const varied = notRecent.filter(r => !mainIngredientIds(r).some(id => variety.previousMains.has(id)));
  return randomOf(varied.length > 0 ? varied : notRecent.length > 0 ? notRecent : pool).id;
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
    // Meals are picked in week order so each pick knows what came before it.
    let previousMains = new Set<string>();
    const pickedByDay: Set<string>[] = week.map(() => new Set());
    const generated = week.map((day, d) =>
      day.map(meal => {
        const variety: VarietyContext = {
          previousMains,
          recentRecipeIds: new Set([...pickedByDay[d], ...(pickedByDay[d - 1] ?? [])]),
        };
        const pick = (c: Course | null) =>
          c ? { ...c, recipeId: pickRecipeForCourse(c, meal.people, recipes, variety) } : null;
        const next = { ...meal, entree: pick(meal.entree), dish: pick(meal.dish), dessert: pick(meal.dessert) };
        if (!meal.skipped) {
          const ids = [next.entree, next.dish, next.dessert].map(c => c?.recipeId).filter((id): id is string => !!id);
          ids.forEach(id => pickedByDay[d].add(id));
          const mains = ids.flatMap(id => mainIngredientIds(recipes.find(r => r.id === id)));
          // A meal without ingredients (improv, skipped) doesn't break a streak of the same ingredient.
          if (mains.length > 0) previousMains = new Set(mains);
        }
        return next;
      })
    );
    setWeek(generated);
    setPhase('recipes');
  }

  function handleReset() {
    if (!week) return;
    const clear = (c: Course | null) => c ? { ...c, recipeId: '' } : null;
    setWeek(week.map(day => day.map(meal => ({
      ...meal,
      entree: clear(meal.entree),
      dish: clear(meal.dish),
      dessert: clear(meal.dessert),
    }))));
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
