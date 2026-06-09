

# Project -1  CFS Chat - Spring AI Chatbot

A production-ready, industry-standard AI chatbot built with **Spring Boot 4**, **Spring AI**, and **Groq**. Designed to help Code For Success (CFS) engineers learn Java Full-Stack development through an optimized, resilient, and secure architecture.

## ✨ Key Features

*   **⚡ Sub-Millisecond Caching:** Pre-defined brand questions are served from memory in `<1ms`, saving API costs and latency.
*   **🧠 Persona Engineering:** Strict System Prompts ensure the AI always identifies as the "CFS Assistant" and focuses on Java/Microservices.
*   **🛡️ Graceful Degradation:** Handles Groq free-tier rate limits (429 errors) gracefully without crashing the application.
*   **🎨 Premium UI:** Single-page Vanilla JS frontend with glassmorphism, dark/light mode, markdown support, and local storage history.
*   **🐳 Docker Ready:** Optimized multi-stage Dockerfile for lightweight, secure container deployment.
*   **🔒 Multi-Environment Config:** Separate `dev` and `prod` profiles with environment-variable-based secrets management.

## 🛠️ Tech Stack

| Layer | Technology |
| :--- | :--- |
| **Backend** | Java 21, Spring Boot 4.0.6, Spring AI 2.0.0-RC1 |
| **AI Provider** | Groq (Llama-3.3-70b / Llama-3.1-8b-instant) |
| **Frontend** | HTML5, CSS3, Vanilla JavaScript, Marked.js, Highlight.js |
| **Container** | Docker (Multi-stage build, Alpine JRE) |
| **Deployment** | Render.com (Backend), Netlify (Frontend) |

## 🚀 Getting Started (Local Development)

### Prerequisites
*   JDK 21+
*   Maven 3.9+
*   A Groq API Key ([Get it here](https://console.groq.com))

### 1. Clone & Configure
```bash
git clone https://github.com/YOUR_USERNAME/springAIOneWayChatBot.git
cd springAIOneWayChatBot
```

### 2. Set Environment Variables
Create a `.env` file or set these in your IDE's Run Configuration:

| Variable | Description | Example |
| :--- | :--- | :--- |
| `SPRING_PROFILES_ACTIVE` | Active profile (`dev` or `prod`) | `dev` |
| `API_MODEL` | Groq model name | `llama-3.1-8b-instant` |
| `OPEN_API_URL` | Groq API endpoint *(Dev only)* | `https://api.groq.com/openai/v1` |
| `OPENAI_API_KEY` | Your Groq API key | `gsk_...` |

> ️ **Note:** The `dev` profile uses `${OPEN_API_URL}` while `prod` uses `${OPENAI_API_URL}`. Ensure your env vars match the active profile!

### 3. Run Locally
```bash
./mvnw spring-boot:run
```
Visit `http://localhost:8080` to test the chatbot.

## 🐳 Docker Deployment

### Build Image
```bash
docker build -t cfs-chatbot .
```

### Run Container
```bash
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e API_MODEL=llama-3.3-70b-versatile \
  -e OPENAI_API_URL=https://api.groq.com/openai/v1 \
  -e OPENAI_API_KEY=gsk_YOUR_KEY_HERE \
  cfs-chatbot
```

## ️ Production Deployment Guide

### Backend (Render.com)
1.  Create a new **Web Service** connected to your GitHub repo.
2.  Set **Environment** to `Docker`.
3.  Add these Environment Variables:
    *   `SPRING_PROFILES_ACTIVE` = `prod`
    *   `API_MODEL` = `llama-3.3-70b-versatile`
    *   `OPENAI_API_URL` = `https://api.groq.com/openai/v1`
    *   `OPENAI_API_KEY` = `gsk_...`
4.  Deploy and copy your service URL (e.g., `https://cfs-chatbot-api.onrender.com`).

### Frontend (Netlify)
1.  Update `index.html` → `CONFIG.API_URL` to your Render backend URL.
2.  Drag & drop the `static/` folder to [Netlify Drop](https://app.netlify.com/drop).
3.  Copy your Netlify URL and update `@CrossOrigin(origins = "...")` in `ChatController.java` to lock down CORS.

## 🏗️ Project Structure

```text
springAIOneWayChatBot/
├── src/main/java/org/sanjay/springaionewaychatbot/
│   ├── controller/
│   │   └── ChatController.java      # Core API + Caching + Error Handling
│   ├── config/
│   │   └── ChatClientConfig.java    # Spring AI Bean Configuration
│   └── SpringAiOneWayChatBotApplication.java
├── src/main/resources/
│   ├── static/
│   │   └── index.html               # Premium Vanilla JS Frontend
│   ├── application.properties       # Base config + profile selector
│   ├── application-dev.yml          # Dev settings (fast model, debug logs)
│   └── application-prod.yml         # Prod settings (smart model, warn logs)
├── Dockerfile                       # Multi-stage optimized build
├── pom.xml                          # Maven dependencies
└── README.md                        # This file
```

## 🔍 Architecture Highlights

### In-Memory Caching Strategy
```java
// Pre-loaded at startup - serves in <1ms
fastCache.put("who are you", "I am the Code For Success (CFS) Chat Assistant...");

// Runtime check before calling Groq
if (fastCache.containsKey(question)) {
    return Map.of("answer", fastCache.get(question));
}
```

### Resilient Error Handling
```java
catch (Exception e) {
    if (e.getMessage().contains("429")) {
        return Map.of("answer", "⚠️ High Traffic Alert: Please wait 60s!");
    }
    return Map.of("answer", "Technical issue. Please try again.");
}
```

## 🙏 Acknowledgments

Special thanks to my mentor **Ashwani Kumar** for making real AI engineering logical and simple to grasp. This project represents the journey from basic tutorials to industry-ready architecture.

##  License

This project is open-source under the MIT License. Feel free to use it for learning and building your own AI applications!

