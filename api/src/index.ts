import crypto from 'crypto';
import express from 'express';
import path from 'path';
import fs from 'fs';
import type { DataFile, Ingredient, Recipe } from './types';

const app = express();
const PORT = process.env.PORT || 3000;
const DATA_PATH = process.env.DATA_PATH || path.join(__dirname, '../../data/recipes.json');
const BACKUP_DIR = path.join(path.dirname(DATA_PATH), 'backups');
const MAX_BACKUPS = 200;

app.use(express.json());

function loadData(): DataFile {
  return JSON.parse(fs.readFileSync(DATA_PATH, 'utf-8'));
}

// Copy the current data file aside before each write, keeping the last MAX_BACKUPS versions.
// Never blocks a save: any backup error is only logged.
function backupData(): void {
  try {
    if (!fs.existsSync(DATA_PATH)) return;
    fs.mkdirSync(BACKUP_DIR, { recursive: true });
    const stamp = new Date().toISOString().replace(/[:.]/g, '-');
    fs.copyFileSync(DATA_PATH, path.join(BACKUP_DIR, `recipes-${stamp}.json`));
    const backups = fs.readdirSync(BACKUP_DIR).filter(f => /^recipes-.*\.json$/.test(f)).sort();
    for (const old of backups.slice(0, -MAX_BACKUPS)) {
      fs.unlinkSync(path.join(BACKUP_DIR, old));
    }
  } catch (err) {
    console.error('Backup failed:', err);
  }
}

function saveData(data: DataFile): void {
  backupData();
  fs.writeFileSync(DATA_PATH, JSON.stringify(data, null, 2), 'utf-8');
}

app.get('/api/categories', (_req, res) => {
  res.json(loadData().categories);
});

app.get('/api/ingredients', (_req, res) => {
  res.json(loadData().ingredients);
});

app.post('/api/ingredients', (req, res) => {
  const data = loadData();
  const ingredient: Ingredient = { ...req.body, id: crypto.randomUUID() };
  data.ingredients.push(ingredient);
  saveData(data);
  res.status(201).json(ingredient);
});

app.get('/api/recipes', (_req, res) => {
  res.json(loadData().recipes);
});

app.post('/api/recipes', (req, res) => {
  const data = loadData();
  const recipe: Recipe = { ...req.body, id: crypto.randomUUID() };
  data.recipes.push(recipe);
  saveData(data);
  res.status(201).json(recipe);
});

app.put('/api/recipes/:id', (req, res) => {
  const data = loadData();
  const idx = data.recipes.findIndex(r => r.id === req.params.id);
  if (idx === -1) { res.status(404).json({ error: 'Not found' }); return; }
  data.recipes[idx] = { ...req.body, id: req.params.id };
  saveData(data);
  res.json(data.recipes[idx]);
});

app.delete('/api/recipes/:id', (req, res) => {
  const data = loadData();
  const idx = data.recipes.findIndex(r => r.id === req.params.id);
  if (idx === -1) { res.status(404).json({ error: 'Not found' }); return; }
  data.recipes.splice(idx, 1);
  saveData(data);
  res.status(204).send();
});

const publicPath = path.join(__dirname, '../public');
if (fs.existsSync(publicPath)) {
  app.use(express.static(publicPath));
  app.get('*', (_req, res) => {
    res.sendFile(path.join(publicPath, 'index.html'));
  });
}

app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
