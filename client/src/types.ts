export type Ingredient = {
  id: string;
  name: string;
  unit: string;
};

export type RecipeIngredient = {
  ingredientId: string;
  quantity: number;
  // Main ingredients are what the menu generator keeps out of two meals in a row.
  main?: boolean;
};

export type Recipe = {
  id: string;
  name: string;
  category: string;
  servings: number;
  laVeille: boolean;
  instructions?: string;
  toValidate?: boolean;
  // Disabled recipes are never picked for a menu.
  disabled?: boolean;
  ingredients: RecipeIngredient[];
};

export type MealTime = 'Breakfast' | 'Lunch' | 'Dinner';

export type Course = {
  category: string;
  recipeId: string;
};

export type CourseKey = 'entree' | 'dish' | 'dessert';

export type MealState = {
  people: number;
  skipped: boolean;
  entree: Course | null;
  dish: Course | null;
  dessert: Course | null;
};

export type Phase = 'categories' | 'recipes';
