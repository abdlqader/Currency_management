# Sanad Technical Task Project

This is a technical task project developed for **Sanad**.

## 🧠 Tech Choices

- **Redis** was chosen for in-memory data storage over using a simple in-memory `HashMap` inside the Spring Boot application. Redis provides significant advantages:
    - **Persistence options**, enabling in-memory data to survive restarts if configured.
    - **Better performance** and memory management for large-scale or concurrent data access.
    - **Decoupling** from JVM lifecycle, which allows sharing data across services or applications if needed.

## ✅ Project Features

- All technical requirements of the task have been implemented.
- Unit tests are included, though minor improvements or additional coverage might be beneficial.

## 🔗 Project URLs

- **Swagger UI** (for exploring and testing endpoints):  
  [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

- **Redis Commander** (web interface to inspect Redis):  
  [http://localhost:8081/](http://localhost:8081/)

- **Main Spring Boot application**:  
  [http://localhost:8080](http://localhost:8080)

- **JVM Debugger**:  
  Runs on port **8039**

## 🚀 Running the Project

> **Note**: The project requires **Linux OS** to run.

1. Make sure you are in the **root directory** of the project.
2. You **must** place your **personal APP_ID** in the following file:
   **container/local/.env**
3. Run the following command in your terminal:

```bash
bash build-local.sh
```

4. Once the project is up, navigate to the Swagger UI to start interacting with the API.

## 🔐 Open Exchange Rates Integration
The project is designed to **integrate** with the Open Exchange Rates API.

You **must** place your **personal OPENEXCHANGERATES_APP_ID** in the following file:
**container/local/.env** for open exchange rates integration to work.

The .env file is already included in the repository to ease project setup.