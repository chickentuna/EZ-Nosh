FROM node:22-alpine AS client-builder
WORKDIR /client
COPY client/package*.json ./
RUN npm ci
COPY client/ ./
RUN BASE_URL=/ez-nosh/ npm run build

FROM node:22-alpine AS api-builder
WORKDIR /api
COPY api/package*.json ./
RUN npm ci
COPY api/ ./
RUN npm run build

FROM node:22-alpine
WORKDIR /app
COPY api/package*.json ./
RUN npm ci --omit=dev
COPY --from=api-builder /api/dist ./dist
COPY --from=client-builder /client/dist ./public
EXPOSE 3000
ENV DATA_PATH=/app/data/recipes.json
CMD ["node", "dist/index.js"]
