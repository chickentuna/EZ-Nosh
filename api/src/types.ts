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
  instructions?: string;
  toValidate?: boolean;
  ingredients: RecipeIngredient[];
};

export type DataFile = {
  categories: string[];
  ingredients: Ingredient[];
  recipes: Recipe[];
};
