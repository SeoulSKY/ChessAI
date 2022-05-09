FROM php:8.0-apache

WORKDIR /var/www/html/

COPY . .

RUN ["chmod", "+x", "./install-composer.sh"]

RUN php composer.phar install

EXPOSE 80
