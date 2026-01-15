# syntax=docker/dockerfile:1.3
FROM node:22-alpine

WORKDIR /app

COPY package.json .

RUN npm install

COPY . .

RUN rm -rf /app/public/images

RUN npm run build

EXPOSE 5173

CMD [ "npm", "run", "dev" ]