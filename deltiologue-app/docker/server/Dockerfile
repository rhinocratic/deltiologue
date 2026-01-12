# syntax=docker/dockerfile:1.3
FROM clojure
COPY ./src /usr/src/app/src
COPY ./resources /usr/src/app/resources
COPY ./deps.edn /usr/src/app
WORKDIR /usr/src/app
CMD ["clojure", "-M:run-m"]
