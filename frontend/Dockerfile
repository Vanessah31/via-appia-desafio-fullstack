FROM node:24 AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build -- --configuration production

FROM nginx:stable
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html
EXPOSE 80