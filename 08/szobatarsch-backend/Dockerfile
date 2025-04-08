FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /src
COPY . .
RUN chmod +x gradlew
RUN ./gradlew build #--no-daemon

## JLINK: java jre release assistant tool, can create a JRE with as few modules as needed.
WORKDIR /jre
## use jdeps to analyze build, so we can include all may be used modules
RUN jdeps --print-module-deps --ignore-missing-deps /src/build/libs/szobatarsch-all.jar > deps.txt
## Now, create a JRE from this image with only the needed modules.
RUN jlink --strip-debug --generate-cds-archive \
    --add-modules $( cat deps.txt ),jdk.crypto.ec \
    --output jre


## Copy needed stuff to a smol alpine image
FROM alpine:latest
COPY --from=build /src/build/libs/szobatarsch-all.jar ./
COPY --from=build /jre/jre /jre

CMD ["/jre/bin/java", "-jar", "szobatarsch-all.jar"]