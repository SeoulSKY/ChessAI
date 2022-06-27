FROM node:18.1.0

WORKDIR /app

COPY . .

RUN npm install

ENV PORT=8080

EXPOSE 8080

RUN npm run build

RUN npm install -g serve

CMD serve -s build
