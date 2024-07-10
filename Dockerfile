## FIRST CONTAINER

## Build Angular
FROM node:22 AS ngbuild

WORKDIR /final-project-frontend-resumaid

# Install Angular
# RUN npm i -g @angular/cli@17.3.8
RUN npm i -g @angular/cli

COPY final-project-frontend-resumaid/angular.json .
COPY final-project-frontend-resumaid/package*.json .
COPY final-project-frontend-resumaid/tsconfig*.json .
COPY final-project-frontend-resumaid/ngsw-config.json .
COPY final-project-frontend-resumaid/src src


# Install modules
RUN npm ci && ng build
# Or: RUN npm i
# && means only run the 2nd one if the first one is successful


## Build Spring Boot
FROM openjdk:21-jdk-bullseye AS javabuild

WORKDIR /final-project-backend-resumaid

COPY final-project-backend-resumaid/mvnw .
COPY final-project-backend-resumaid/mvnw.cmd .
COPY final-project-backend-resumaid/pom.xml .
COPY final-project-backend-resumaid/.mvn .mvn
COPY final-project-backend-resumaid/src src

# Copy Angular files to Spring Boot
COPY --from=ngbuild /final-project-frontend-resumaid/dist/final-project-frontend-resumaid/browser src/main/resources/static




RUN /final-project-backend-resumaid/mvnw package -Dmaven.test.skip=true
# RUN ./mvnw package -Dmaven.test.skip=true
# RUN mvn package -Dmaven.test.skip=true


## SECOND CONTAINER

FROM openjdk:21-jdk-bullseye

WORKDIR /app

# Copying file from builder instead of locally
COPY --from=javabuild /final-project-backend-resumaid/target/final-project-backend-resumaid-0.0.1-SNAPSHOT.jar .
COPY --from=javabuild /final-project-backend-resumaid/src/main/resources/resumaid-googlecal-credentials.json /app/src/main/resources/resumaid-googlecal-credentials.json


# Run
ENV S3_ACCESS_KEY=
ENV S3_SECRET_KEY=
ENV SECURITY_JWT_SECRETKEY=
ENV OLLAMA_HOST=
ENV SPRING_MAIL_PASSWORD=
ENV STRIPE_API_KEY=
ENV GOOGLE_CREDENTIALS_FILEPATH=


ENV PORT=8080

EXPOSE ${PORT}

ENTRYPOINT SERVER_PORT=${PORT} java -jar final-project-backend-resumaid-0.0.1-SNAPSHOT.jar
# If want to rename to weather.jar
# ENTRYPOINT SERVER_PORT=${PORT} java -jar weather.jar