# syntax=docker/dockerfile:1.3
FROM clojure
RUN apt-get update -yq
RUN apt-get install -yqq curl
RUN apt-get install -yqq imagemagick
COPY ./src /usr/src/app/src
COPY ./resources /usr/src/app/resources
COPY ./deps.edn /usr/src/app
WORKDIR /usr/src/app
CMD ["clojure", "-M:run-m"]
