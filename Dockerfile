FROM openjdk:14
ADD target/microservice-loan.jar /microservice-loan.jar
EXPOSE 8000
ENTRYPOINT ["java","-jar","/microservice-loan.jar"]