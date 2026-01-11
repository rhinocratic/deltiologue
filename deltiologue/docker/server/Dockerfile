# syntax=docker/dockerfile:1.3
FROM clojure
COPY ./src /usr/src/app/src
COPY ./app_data /usr/src/app/data
COPY ./resources/config.edn /usr/src/app/resources/config.edn
COPY ./resources/public/favicon.ico /usr/src/app/resources/public/favicon.ico
COPY ./resources/ZmxY_91hvXPwBFY9u5FH6.pem /usr/src/app/resources/ZmxY_91hvXPwBFY9u5FH6.pem
COPY ./deps.edn /usr/src/app
CMD ["clojure", "-M", "-m", "rhinocratic.deltiologue", "dev"]
