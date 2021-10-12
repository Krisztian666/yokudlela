# YOKUDLELA
Java Microservice projekt kidolgozott minta.

#Docker
https://docs.docker.com/desktop/windows/install/

# Xubuntu VirtualBox (fileokat egyenként töltsd le)
https://www.dropbox.com/sh/mr6non6u1futfba/AAD24jiDjnmDhRJthh2TKWLVa?dl=0

# Bevezető
https://microservices.io/
https://12factor.net/

# Feladatok
## Asztalfoglalás (kidolgozott minta)
Étteremben lévő asztalok kezelésére, és foglalást megvalósító szolgáltatás.
### Egy asztalról tároljuk
- Nevét
- Székek számát
- Fényképeket az asztalról, és környékéről
- Elérhetőségi státuszát. => Télen nincs terasz, akkor ideiglenesen letiltjuk és összepakoljuk az ottani asztalokat...
### Egy foglalásról nyílvántartjuk
- A foglaló nevét
- Kezteti időt
- Lejárati időt
- Létszám
### Fontös üzleti funkciók:
- Asztalok kezelése (CRUD)
- Foglalás
- Megadott időben elérhető szabad asztalok listája.

## Raktár
A raktárban lévő alapanyagokat egy hierarchikus listába rendezve tároljuk. Egy alapanyag több csoportban is szerepelhet. Minden beérkezett alapanyagról nyílvántartjuk a lejárati idejét.   
### Árucsoportokról nyilvántartjuk
- Nevét
- Leírását
- A csoportban lévő konkrét árukat
### Árukról nyilvántartjuk
- Nevét
- Leírását
- Lejárati idejét => ugyan azon áruból egyszerre lehett több lejárati idejővel más-más darabszámban- 
- Beszerzési árát
- Mennyiség
- Fényképeket
- Helyét a raktárban => lehet több rakár/hűtő, és polc is
### Fontos üzleti funkciók
- Ki és bevételezés.
- Aktuális készlet meghatározása- termékenként
- Selejtezés
- Adott időn belül lejáró szavatosságú termékek listája. 

## Étlap és Itallap

## Recept

## Menü

## Folyamatok

## Pincér

## Beszállítók

## Selejtezés, hulladék kezelés

## Kassza


# Mérföldkövek
## Bevezető
### Hogyan és miért kezdjük el
  https://microservices.io/
  https://microservices.io/patterns/index.html
### Adatszerkezet
  https://github.com/Krisztian666/yokudlela/tree/01-data-models
### Szolgáltatások
  https://github.com/Krisztian666/yokudlela/tree/02-business-logic
### REST, OpenAPI, CORS
  https://github.com/Krisztian666/yokudlela/tree/03-spring-rest
## Docker
### CI/CD megoldja
### Maven plugin
  https://github.com/Krisztian666/yokudlela/tree/04-spring-maven-docker
## Hitelesítés, Validáció Hibakezelés
### OpenID, OAUTH2, IAM
  https://github.com/Krisztian666/yokudlela/tree/05-spring-iam
### Validáció és hibakezelés
  https://github.com/Krisztian666/yokudlela/tree/06-spring-validation
## Relációs Adatbázis kezelés
### JPA, SpringData
### Adatbázis tartalmak verziókezelés
## Cache
### Redish
### NoSQL(MongoDB)
## Szolgáltatások összekapcsolása
### Közvetlen áthívások
### RabitMQ
### Kafka
## Log
### SLF4J
### Graylog, ElasticSearch


