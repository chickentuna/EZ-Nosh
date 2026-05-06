import type { Recipe, MealState, Phase } from '../types';
import MealRow from './MealRow';

type Props = {
  day: string;
  meals: string[];
  dayMeals: MealState[];
  categories: string[];
  recipes: Recipe[];
  phase: Phase;
  onUpdateMeal: (mealIdx: number, update: Partial<MealState>) => void;
};

export default function DaySection({ day, meals, dayMeals, categories, recipes, phase, onUpdateMeal }: Props) {
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
          phase={phase}
          onUpdate={update => onUpdateMeal(m, update)}
        />
      ))}
    </div>
  );
}
