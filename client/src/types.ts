export type Ingredient = {
  id: string;
  name: string;
  unit: string;
};

export type RecipeIngredient = {
  ingredientId: string;
  quantity: number;
};

export type Recipe = {
  id: string;
  name: string;
  category: string;
  servings: number;
  laVeille: boolean;
  ingredients: RecipeIngredient[];
};

export type MealTime = 'Breakfast' | 'Lunch' | 'Dinner';

export type MealState = {
  category: string;
  recipeId: string;
  people: number;
};

export type Phase = 'categories' | 'recipes';
