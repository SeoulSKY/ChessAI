FROM bitnami/laravel:9.1.7

WORKDIR /app

COPY . .

RUN chmod +x ./install-composer.sh && \
    ./install-composer.sh

ARG DEBIAN_FRONTEND=noninteractive

RUN apt-get update && \
    apt-get install apt-utils -y && \
    apt-get install php-xml -y && \
    apt-get install apt unzip -y && \
    php composer.phar install && \
    npm install

EXPOSE 80

CMD php artisan serve --host "0.0.0.0" --port 80
