FROM maven:3.8.1-openjdk-11 AS builder
WORKDIR /usr/src/app

COPY pom.xml .
RUN mvn install --fail-never

ENV FINANCIAL_ANALYSIS_SERVER_URL "url"
ENV FINANCIAL_ANALYSIS_HEALTH_ENDPOINT "endpoint"
ENV FINANCIAL_ANALYSIS_ENDPOINT "endpoint"
ENV CREDIT_CARD_SERVER_URL "url"
ENV CREDIT_CARD_HEALTH_ENDPOINT "endpoint"
ENV CREDIT_CARD_REQUEST_ENDPOINT "endpoint"
ENV CREDIT_CARD_BLOCK_ENDPOINT "{creditCardNumber}"
ENV CREDIT_CARD_TRAVEL_NOTICE_ENDPOINT "{creditCardNumber}"
ENV CREDIT_CARD_WALLET_ENDPOINT "{creditCardNumber}"
ENV MANAGEMENT_ALLOWED_ORIGINS "origins"
ENV MARIADB_HOST "localhost"
ENV MARIADB_PORT "3306"
ENV MARIADB_SCHEMA "proposal"
ENV MARIADB_USERNAME "proposal"
ENV MARIADB_PASSWORD "proposal"
ENV OAUTH2_JWT_ISSUER_URI "http://localhost"
ENV OAUTH2_JWK_SET_URI "http://localhost"
ENV JAEGER_ENDPOINT "http://localhost"
ENV ENCRYPTION_PASSWORD "proposal"

COPY src ./src
RUN mvn clean package

WORKDIR /usr/src/app/target/dependency
RUN jar -xf ../*.jar


FROM adoptopenjdk/openjdk11:alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG DEPENDENCY=/usr/src/app/target/dependency
COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=builder ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","br.com.zupacademy.sergio.proposal.ProposalApplicationKt"]
