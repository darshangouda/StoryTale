import React, { useState } from "react";
import axios from "axios";

const RegisterPage = () => {
  const [writerName, setUsername] = useState("");
  const [writerPassword, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const handleRegister = () => {
    if (writerPassword !== confirmPassword) {
      alert("Passwords do not match!");
      return;
    }

    if (!validatePassword(writerPassword)) {
      alert("Password must be at least 8 characters long, include an uppercase letter, a lowercase letter, a number, and a special character.");
      return;
    }

    axios
      .post("http://localhost:8080/register", { writerName, writerPassword })
      .then((response) => {
        if (response.status === 200) {
          alert("Registration successful! Please Login");
          window.location.href = "/login"; // Redirect to Picture Login
        } else {
          alert("Registration failed. Please try again. " + response.data);
        }
      })
      .catch((error) => {
        if (error.response) {
          if (error.response.status === 400) {
            alert("Registration failed: " + error.response.data);
          } else {
            alert("An error occurred. Status: " + error.response.status);
          }
        } else {
          console.error("Error during registration:", error);
          alert("An unexpected error occurred. Please try again later.");
        }
      });
  };

  const validatePassword = (password) => {
    const passwordRegex =
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    return passwordRegex.test(password);
  };

  return (
    <div className="container mt-5">
      <h2>Register</h2>
      <div className="mb-3">
        <label className="form-label">Username</label>
        <input
          type="text"
          className="form-control"
          value={writerName}
          onChange={(e) => setUsername(e.target.value)}
          placeholder="Enter a username"
        />
      </div>
      <div className="mb-3">
        <label className="form-label">Password</label>
        <input
          type="password"
          className="form-control"
          value={writerPassword}
          onChange={(e) => setPassword(e.target.value)}
          placeholder="Enter a password"
        />
      </div>
      <div className="mb-3">
        <label className="form-label">Retype Password</label>
        <input
          type="password"
          className="form-control"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
          placeholder="Retype your password"
        />
      </div>
      <button className="btn btn-primary" onClick={handleRegister}>
        Register
      </button>
    </div>
  );
};

export default RegisterPage;
