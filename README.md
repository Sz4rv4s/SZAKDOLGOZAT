# SZAKDOLGOZAT

## Klónozza a tárolót a GitHubról a következő paranccsal:

```bash
git clone https://github.com/Sz4rv4s/SZAKDOLGOZAT.git
```

## Lépjen be a projekt mappájába:

```bash
cd SZAKDOLGOZAT
```

## A projekt futtatásához szükséges a MongoDB adatbázis helyi futtatása.
Telepítse a Dockert, majd lépjen be a DOCKER mappába,  
a .env fájlokat hozza létre és állítsa be a példa fájlok alapján,  
ezután az alábbi paranccsal telepíthető és elindítható az adatbázis:  

```bash
docker-compose up -d
```

## Hozzon létre .env fájlokat a backend és a frontend számára a megfelelő mappákban (backend/src/main/resources és frontend/.env). A backend .env fájlban adja meg a példafájlban található változókat.

## A backend futtatásához lépjen a backend mappába, és futtassa a következő parancsot:

```bash
mvn spring-boot:run
```

Ez elindítja a Spring Boot alkalmazást a localhost:8080 porton.

## A frontend futtatásához lépjen a frontend mappába, telepítse a függőségeket, és indítsa el az alkalmazást:

```bash
cd frontend
npm install
npm run dev
```

Ez elindítja a Vite fejlesztői szervert, és a frontend alapértelmezés szerint a localhost:5173 porton lesz elérhető.

## A helyi futtatás során a CORS beállításokat is konfigurálni kell.
A backend WebSecurityConfig osztályában állítsa be a CORS-t úgy,  
hogy a localhost:5173 domainről érkező kéréseket elfogadja:

```java
configuration.setAllowedOrigins(List.of("http://localhost:5173"));
```

## Nyissa meg a böngészőt, és navigáljon a http://localhost:5173 címre, ahol a bejelentkezési vagy regisztrációs oldal fogadja, és a fenti lépéseket követve használhatja az alkalmazást.
