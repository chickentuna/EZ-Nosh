import type { Recipe, Ingredient, MealState, Phase } from '../types';
import MealRow from './MealRow';

type Props = {
  day: string;
  meals: string[];
  dayMeals: MealState[];
  categories: string[];
  recipes: Recipe[];
  ingredients: Ingredient[];
  phase: Phase;
  onUpdateMeal: (mealIdx: number, update: Partial<MealState>) => void;
};

export default function DaySection({ day, meals, dayMeals, categories, recipes, ingredients, phase, onUpdateMeal }: Props) {
  return (
    <div style={{ marginBottom: '1rem' }}>
      <h3>{day}</h3>
      {meals.map((meal, m) => (
        <MealRow
          key={meal}
          meal={meal}
          state={dayMeals[m]}
          categories={categories}
          recipes={recipes}
          ingredients={ingredients}
          phase={phase}
          onUpdate={update => onUpdateMeal(m, update)}
        />
      ))}
    </div>
  );
}
