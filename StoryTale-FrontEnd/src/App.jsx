import React, { useState } from "react";
import { BrowserRouter, Route, Routes, Navigate } from "react-router-dom";
import HeaderComponent from "./components/HeaderComponent";
import FooterComponent from "./components/FooterComponent";
import LoginPage from "./components/LoginPage";
import RegisterPage from "./components/RegisterPage";
import StoryTale from "./components/StoryTale";
import ListStoryTale from "./components/ListStoryTale";
import Preview from "./components/Preview";

const ProtectedRoute = ({ element }) => {
  const isAuthenticated = !!localStorage.getItem("authToken");
  return isAuthenticated ? element : <Navigate to="/login" />;
};

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(
    !!localStorage.getItem("authToken")
  );

  const handleLoginSuccess = () => {
    console.log("App - Login");
    setIsAuthenticated(true); // Update authentication state
  };

  const handleLogout = () => {
    console.log("App - Logout");
    setIsAuthenticated(false); // Update authentication state
  };

  return (
    <BrowserRouter>
      <HeaderComponent
        isAuthenticated={isAuthenticated}
        onLogout={handleLogout}
      />
      <div className="container mt-4">
        <Routes>
          <Route path="/" element={<h1>Welcome to Story Tale!</h1>} />
          <Route
            path="/login"
            element={<LoginPage onLoginSuccess={handleLoginSuccess} />}
          />
          <Route path="/register" element={<RegisterPage />} />
          <Route
            path="/storytale/:storyID"
            element={<ProtectedRoute element={<StoryTale />} />}
          />
          <Route
            path="/mystories"
            element={<ProtectedRoute element={<ListStoryTale />} />}
          />
          <Route
            path="/preview/:storyID"
            element={<ProtectedRoute element={<Preview />} />}
          />
        </Routes>
      </div>
      <FooterComponent />
    </BrowserRouter>
  );
}

export default App;
