FROM maven:3.8.1-openjdk-11 AS builder
WORKDIR /usr/src/app
COPY src ./src
COPY pom.xml .
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
