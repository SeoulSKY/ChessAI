FROM php:8.0-cli

WORKDIR /app

COPY . .

RUN ["chmod", "+x", "./install-composer.sh"]

RUN php composer.phar install

EXPOSE 8080

CMD ["php", "-S", "localhost:8080"]
