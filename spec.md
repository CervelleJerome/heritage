# 📚 Application de Gestion des Personnes dans un Établissement

## 🧩 Description

Cette application Spring Boot permet de gérer différentes catégories de personnes dans un établissement : **enseignants**, **étudiants** et **personnels administratifs**. Chaque personne possède des informations de base communes, mais selon sa catégorie, elle possède également des attributs spécifiques :

- Les **enseignants** ont des **heures supplémentaires**.
- Les **étudiants** disposent de **bourses**.
- Les **personnels administratifs** perçoivent des **primes**.

L'application utilise l'héritage pour gérer proprement ces entités avec des classes Java modélisées selon la hiérarchie objet, persistées via **Hibernate ORM** dans une base de données **MySQL**. L'interface utilisateur est rendue avec **Thymeleaf**. L'application est conteneurisée avec **Docker**.

---

## 🔧 Technologies utilisées

- Java 17+
- Spring Boot
- 
    - Spring Web
    - Spring Data JPA (Hibernate)
    - Thymeleaf
    - BOOTSTRAP v5.3
- MySQL
- Docker & Docker Compose
- PhpMyAdmin (pour l'administration de la base)
- Lombok (pour simplifier le code)
- Maven

---

## 🧱 Architecture

### 📦 Modèle Objet

```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Personne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String rue;
    private String codePostal;
    private String ville;
    private String telephone;
}
```

```java
@Entity
public class Enseignant extends Personne {
    private int heuresSupplementaires;
}
```
```java
@Entity
public class PersonnelAdministratif extends Personne {
private double prime;
}
```

🗃️ Base de données
MySQL

Utilisation du schéma relationnel généré automatiquement par Hibernate (DDL-auto = update)

Stratégie d’héritage : JOINED (chaque classe dans sa propre table)

Exemple de configuration application.properties :

```java
spring.datasource.url=jdbc:mysql://localhost:3306/gestion_personnes
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

🖥️ Interface utilisateur

Thymeleaf : affichage des listes de personnes, formulaires d’ajout/modification pour chaque type (enseignant, étudiant, personnel administratif)

Routes principales :

```
/enseignants

/etudiants

/personnels
```

🚢 Docker & Docker Compose

```Docker
FROM openjdk:17-jdk-slim

COPY target/gestion-personnes.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
```
```Docker Compose
version: '3.8'
services:
  db:
    image: mysql:8
    restart: always
    environment:
      MYSQL_DATABASE: gestion_personnes
      MYSQL_ROOT_PASSWORD: your_password
    ports:
      - "3306:3306"
    volumes:
      - db-data:/var/lib/mysql

  phpmyadmin:
    image: phpmyadmin/phpmyadmin
    restart: always
    ports:
      - "8081:80"
    environment:
      PMA_HOST: db
      MYSQL_ROOT_PASSWORD: 

  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - db
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/gestion_personnes
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: your_password

volumes:
  db-data:

```

✅ Fonctionnalités
```
CRUD complet sur chaque type de personne

Affichage filtré par rôle

Utilisation d’une hiérarchie propre côté Java & base

Affichage dynamique avec Thymeleaf

Base MySQL persistée et conteneurisée avec Docker
```

📁 Structure du projet
src/

├── main/

│   ├── java/com/example/gestionpersonnes/

│   │   ├── controller/

│   │   ├── entity/

│   │   ├── repository/

│   │   └── service/

│   └── resources/

│       ├── templates/

│       ├── static/

│       └── application.properties

🎯 Navigation et Vues  :
```
Aide-moi à intégrer Bootstrap 5 à un projet Spring Boot utilisant Thymeleaf comme moteur de template.

Le projet suit la structure classique src/main/resources/templates pour les vues.

Je veux utiliser le CDN officiel de Bootstrap pour ne pas gérer les fichiers localement.

Réalise toute le navigation simple et fluide de l'application

Utilise des couleurs primaires
```




