# StoryTale

StoryTale is a Spring Boot application designed to help storywriters create books using AI-generated images. Writers can pair images with storylines for each book, making the process creative and engaging.

---

## Project Description

StoryTale leverages AI technology to generate images that writers can use to complement their storylines. Writers can create books with multiple chapters, each paired with a unique image to enhance the storytelling experience.

---

## Features

- AI-generated image creation.
- Secure authentication and authorization with JWT Security.

---

## Technologies Used

### Frontend:
- ReactJS
- Bootstrap

### Backend:
- Java
- Spring Boot
  - Spring Web
  - RESTful API
  - Spring Security
  - Spring Data JPA
  - Spring AI (OpenAI)
- PostgreSQL
- JWT Token

---

## Setup Instructions

### Frontend:
1. Navigate to the frontend directory.
2. Install dependencies:
   ```bash
   npm install
   ```
3. Run the development server:
   ```bash
   npm run dev
   ```
4. The frontend will run on `http://localhost:3000`.

### Backend:
1. Ensure you have Java and Maven installed.
2. Clone the repository:
   ```bash
   git clone https://github.com/darshangouda/storytale.git
   ```
3. Navigate to the backend directory.
4. Image generation uses random test photos from Lorem Picsum by default. To use OpenAI image generation instead, set these environment variables:
   ```env
   IMAGE_GENERATION_PROVIDER=openai
   OPENAI_KEY="your_openai_api_key"
   ```
   Picsum images are random photos and do not reflect the story prompt. To use the default Picsum test images, no image-generation API key is required.
5. Build and run the Spring Boot application.
   ```bash
   mvn spring-boot:run
   ```
6. The backend will run on `http://localhost:8080`.

---

## Usage

- The StoryTale application consists of a ReactJS frontend and a Spring Boot backend.
- Users can log in securely using JWT authentication.
- AI-generated images can be created and paired with storylines for each chapter of a book.

---

## Environment Variables

To enable OpenAI image generation, set these variables in the backend environment (or `StoryTale-BackEnd/.env`):

```env
IMAGE_GENERATION_PROVIDER=openai
OPENAI_KEY="your_openai_api_key"
```

---

## Contributors/Contact

Developed by [Darshangouda](https://github.com/darshangouda).

---

## License

This project is not licensed.
