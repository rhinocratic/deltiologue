# syntax=docker/dockerfile:1.3
FROM ghcr.io/baosystems/postgis:18-3.6

RUN localedef -i en_GB -c -f UTF-8 -A /usr/share/locale/locale.alias en_GB.UTF-8

ENV POSTGRES_DB=deltiologue
ENV POSTGRES_USER=deltiologue
ENV PGDATA=/data/postgres
ENV LANG=en_GB.utf8

COPY ./resources/sql/init.sql /docker-entrypoint-initdb.d/