import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const LoginPage = ({ onLoginSuccess }) => {
  const [writerName, setWriterName] = useState("");
  const [writerPassword, setWriterPassword] = useState("");
  const navigate = useNavigate();

  const handleLogin = () => {
    axios
      .post(
        "http://localhost:8080/login",
        { writerName, writerPassword },
        { withCredentials: true }
      )
      .then(() => {
        alert("Login successful");
        localStorage.setItem("authToken", "VALID"); // Simulate a valid token
        onLoginSuccess(); // Notify parent component
        navigate("/"); // Redirect to homepage
      })
      .catch((error) => {
        console.error("Login error:", error);
        if (error.response?.status === 401) {
          alert("Invalid credentials. Please try again.");
        } else {
          alert("An error occurred. Please try again later.");
        }
      });
  };

  const isButtonDisabled = !writerName.trim() || !writerPassword.trim();

  return (
    <div className="container mt-5">
      <h2>Login</h2>
      <div className="mb-3">
        <label className="form-label">Username</label>
        <input
          type="text"
          className="form-control"
          value={writerName}
          onChange={(e) => setWriterName(e.target.value)}
          placeholder="Enter your username"
        />
      </div>
      <div className="mb-3">
        <label className="form-label">Password</label>
        <input
          type="password"
          className="form-control"
          value={writerPassword}
          onChange={(e) => setWriterPassword(e.target.value)}
          placeholder="Enter your password"
        />
      </div>
      <button
        className="btn btn-primary"
        onClick={handleLogin}
        disabled={isButtonDisabled}
      >
        Login
      </button>
    </div>
  );
};

export default LoginPage;
