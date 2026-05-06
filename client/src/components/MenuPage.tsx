import type { Recipe, Ingredient, MealState, Phase } from '../types';
import DaySection from './DaySection';

type Props = {
  days: string[];
  mealsByDay: Record<string, string[]>;
  week: MealState[][];
  categories: string[];
  recipes: Recipe[];
  ingredients: Ingredient[];
  phase: Phase;
  onGenerate: () => void;
  onReset: () => void;
  onUpdateMeal: (dayIdx: number, mealIdx: number, update: Partial<MealState>) => void;
};

function triggerDownload(filename: string, content: string) {
  const url = URL.createObjectURL(new Blob([content], { type: 'text/plain' }));
  const a = document.createElement('a');
  a.href = url;
  a.download = filename;
  a.click();
  URL.revokeObjectURL(url);
}

function buildMenuText(
  week: MealState[][],
  days: string[],
  mealsByDay: Record<string, string[]>,
  recipes: Recipe[]
): string {
  const lines = ['EZ-NOSH — WEEKLY MENU', '='.repeat(22), ''];
  for (let d = 0; d < days.length; d++) {
    const meals = mealsByDay[days[d]];
    lines.push(days[d]);
    for (let m = 0; m < meals.length; m++) {
      const meal = week[d][m];
      const recipe = recipes.find(r => r.id === meal.recipeId);
      const name = recipe?.name ?? '—';
      const flag = recipe?.laVeille ? ' 🎑' : '';
      lines.push(`  ${meals[m].padEnd(10)}: ${name} — ${meal.people} people${flag}`);
    }
    lines.push('');
  }
  return lines.join('\n');
}

function buildShoppingList(
  week: MealState[][],
  recipes: Recipe[],
  ingredients: Ingredient[]
): string {
  const totals = new Map<string, number>();
  for (const day of week) {
    for (const meal of day) {
      if (!meal.recipeId || meal.people === 0) continue;
      const recipe = recipes.find(r => r.id === meal.recipeId);
      if (!recipe) continue;
      const multiplier = meal.people / recipe.servings;
      for (const ri of recipe.ingredients) {
        totals.set(ri.ingredientId, (totals.get(ri.ingredientId) ?? 0) + ri.quantity * multiplier);
      }
    }
  }

  const lines = Array.from(totals.entries())
    .map(([id, qty]) => {
      const ing = ingredients.find(i => i.id === id);
      return ing ? `${ing.name}: ${Math.ceil(qty)} ${ing.unit}` : null;
    })
    .filter((l): l is string => l !== null)
    .sort();

  return ['EZ-NOSH — SHOPPING LIST', '='.repeat(24), '', ...lines].join('\n');
}

export default function MenuPage({ days, mealsByDay, week, categories, recipes, ingredients, phase, onGenerate, onReset, onUpdateMeal }: Props) {
  const allSelected = phase === 'recipes' && week.every(day => day.every(meal => meal.recipeId !== ''));

  return (
    <div>
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0 2rem' }}>
        {days.map((day, d) => (
          <DaySection
            key={day}
            day={day}
            meals={mealsByDay[day]}
            dayMeals={week[d]}
            categories={categories}
            recipes={recipes}
            phase={phase}
            onUpdateMeal={(m, update) => onUpdateMeal(d, m, update)}
          />
        ))}
      </div>
      <div style={{ marginTop: '1rem', display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
        {phase === 'categories'
          ? <button onClick={onGenerate}>Generate Menu</button>
          : <button onClick={onReset}>Start Over</button>
        }
        {allSelected && (
          <>
            <button onClick={() => triggerDownload('menu.txt', buildMenuText(week, days, mealsByDay, recipes))}>
              Download Menu
            </button>
            <button onClick={() => triggerDownload('shopping-list.txt', buildShoppingList(week, recipes, ingredients))}>
              Download Shopping List
            </button>
          </>
        )}
      </div>
    </div>
  );
}
