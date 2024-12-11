# BinDetective

BinDetective is an Android application that provides features like authentication, data collection, image-based predictions, quizzes, and user profile management. This README serves as a comprehensive guide for developers and contributors.

---

## **Features**

### 1. **Login**
- Google Sign-In using Firebase Authentication.
- Collects user's name, profile picture, and Gmail address.

### 2. **Home**
- Displays the user's name and profile picture.
- Includes a RecyclerView with clickable items for navigation to detailed articles.

### 3. **Collection**
- Fetches and displays collections using the user's Gmail address.

### 4. **Camera**
- Allows users to select or capture a photo.
- Uploads the photo to an API for prediction and displays results.

### 5. **Quiz**
- Retrieves questions and options from an API.
- Provides feedback for correct answers.

### 6. **Profile**
- Displays the user's name, profile picture, and a sign-out option.

---

## **Technical Details**

### **Architecture**
- **MVVM (Model-View-ViewModel)** pattern.
- Retrofit handles API communication.
- Data Binding and View Binding enable efficient UI management.

### **Dependencies**
Key dependencies used:
- **AndroidX Libraries**: Core, AppCompat, Lifecycle, Navigation.
- **Firebase**: Authentication.
- **Retrofit**: HTTP requests and JSON parsing.
- **Glide and Picasso**: Image loading.
- **CameraX**: Camera integration.
- **JUnit and Espresso**: Testing frameworks.

---

## **API Endpoints**

### 1. **User Management**
- `POST /users`: Create a new user.
- `GET /users/{userId}`: Retrieve user details.
- `PUT /users/{userId}`: Update user details.
- `DELETE /users/{userId}`: Delete a user.

### 2. **Quiz**
- `GET /quiz`: Retrieve quiz questions.

### 3. **Image Prediction**
- `POST /predict`: Upload an image for prediction.

---

## **Project Structure**

### **Directories**
- **api**: API-related classes (`ApiService`, `ApiConfig`).
- **model**: Data models (`User`, `CreateUserResponse`, `QuizDetailResponse`).
- **ui**: UI components like activities and fragments.
- **viewmodel**: ViewModel classes for managing UI-related data.

---

## **Future Enhancements**

1. Add dark mode support.
2. Implement push notifications using Firebase Cloud Messaging.
3. Optimize image uploads with compression.
4. Add localization for multiple languages.
5. Enhance security with authentication tokens.
