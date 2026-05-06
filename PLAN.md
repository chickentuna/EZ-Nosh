# EZ-Nosh — Build Plan

## Step 1: Project scaffold

- Monorepo: `/api` (Express + TypeScript) and `/client` (React + Vite + TypeScript)
- Single Docker container via multi-stage Dockerfile
  - Stage 1: build React app
  - Stage 2: Node.js runtime, serves API + static files
- `docker-compose.yml` at root with a volume mount for the JSON data file
- Targets Ubuntu 25.04 self-hosted server

## Step 2: Weekly menu view

**Layout**: a "Menu" page listing all 7 days, each with 3 meals (breakfast, lunch, dinner).

**Phase 1 — Category selection**
- Each meal has a category dropdown (categories loaded from JSON, with a default option)
- Each meal has a "number of people" number input (min 0)

**Phase 2 — Recipe selection** (after clicking a "Generate" button)
- Each meal's category dropdown is replaced by a dropdown of recipes from that category, pre-filled with a random pick
- User can re-roll by choosing another recipe from the dropdown
- The "number of people" input remains visible in both phases

**Data**: recipes stored in a JSON file (structure TBD once categories are provided)


## Step 3: there should be a recipe editor to input new recipes. 

- It should be a separate page. And adding a recipe must allow the following.
- All recipes will have a number of people served. When incrementing in the menu UI for that specific recipe, you'll have to increment by this number.
- Recipes should have a list of ingredients and a quantity per that ingredient's unit.
- Ingredients have a unit.
- If an ingredient is missing, user can i add one, include a ui for that too. 
- A recipe can have a special flag "la veille", which means it must be made for the next day instead of on that day. This must appear in the final menu screen.

## Step 4 - the categories.

They are as follows:

- lunchbox: things to bring to work
- express: easy/fast to make
- picnic
- chef: more luxirious, complex
- kiddy chef: something to make with the kids
- improv: a special one with no ingredients.

### Step 5 - Selections

When wall recipes are selected randomly or by a search field, buttons to download the menu, and the list both appear.


### Step 6 - graphics.

have this image somewhere, it's a cute little tomato guy: https://sea.hidden-street.net/sites/sea.hidden-street.net/files/monsters/Giant%20Tomato.png