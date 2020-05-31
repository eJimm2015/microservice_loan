# microservice_loan
Repository Git du microservice des prets (Loan).

Afin de lancer le microservice

- Récupérer le repository
- Lancer un "mvn clean install"
- aller sur : http://localhost:8000/api/swagger-ui.html#/

Quelques détails

- Les dates sont au format YYYY-MM-DD
- Lors de la création d'un pret, la date de retour sera à null pour signifier que le livre n'est pas encore rendu.
- L'ID est autogénéré lors de la création. Il ne faut donc pas le renseigner.
