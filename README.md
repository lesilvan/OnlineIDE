# ASE Practical Exercise - Group 1-2 - WS20
## Online IDE

How to run this project

Start UI only (without microservice integration)
1. Change current path into subfolder UI/FRONTEND: `cd ui/frontend`
2. Run `ng serve`
3. Navigate to `http://localhost:4200/home` in the browser

Start UI (integrated in Microservice)
1. Change current path into subfolder UI/FRONTEND: `cd ui/frontend`
2. Build static files with `ng build --prod --outputPath=../src/main/resources/static/`
3. Start the spring boot UI application (main in UiApplication)
4. Navigate to `http://localhost:8080/home` in browser

Start other microservices
- Project (runs on port 8081)