import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";

const HeaderComponent = ({ isAuthenticated, onLogout }) => {
  const [profileVisible, setProfileVisible] = useState(false);
  const [userInfo, setUserInfo] = useState({ firstName: "", lastName: "", email: "" });
  const [isEditing, setIsEditing] = useState(false);

  useEffect(() => {
    //localStorage.removeItem("authToken");
    if (isAuthenticated) {
      // Fetch user profile data
      axios
        .get("http://localhost:8080/API/profile", { withCredentials: true })
        .then((response) => {
          if (response.status === 200) {
            setUserInfo(response.data);
          }
        })
        .catch((error) => console.error("Error fetching profile:", error));
    }
  }, [isAuthenticated]);

  const handleLogout = () => {
    axios
      .post("http://localhost:8080/logout", {}, { withCredentials: true })
      .then(() => {
        localStorage.removeItem("authToken");
        alert("Logout successful");
        onLogout();
        window.location.href = "/";
      })
      .catch((error) => {
        console.error("Logout error:", error);
        alert("An error occurred while logging out. Please try again.");
      });
  };

  const handleProfileEdit = () => {
    axios
      .put("http://localhost:8080/API/profile", userInfo, { withCredentials: true })
      .then((response) => {
        if (response.status === 200) {
          alert("Profile updated successfully!");
          setIsEditing(false);
        }
      })
      .catch((error) => {
        console.error("Error updating profile:", error);
        alert("An error occurred while updating the profile. Please try again.");
      });
  };

  return (
    <header>
      <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
        <div className="container-fluid">
          <Link className="navbar-brand" to="/">
            Story Tale
          </Link>
          <button
            className="navbar-toggler"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#navbarNav"
            aria-controls="navbarNav"
            aria-expanded="false"
            aria-label="Toggle navigation"
          >
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarNav">
            <ul className="navbar-nav ms-auto">
              {isAuthenticated ? (
                <>
                  <li className="nav-item">
                    <Link className="nav-link" to="/storytale/0">
                      Create Story Tale
                    </Link>
                  </li>
                  <li className="nav-item">
                    <Link className="nav-link" to="/mystories">
                      My Stories
                    </Link>
                  </li>
                  <li className="nav-item dropdown">
                    <button
                      className="btn btn-link nav-link"
                      onClick={() => setProfileVisible(!profileVisible)}
                      style={{ position: "relative" }}
                    >
                      <img
                        src="https://cdn-icons-png.flaticon.com/512/1077/1077012.png"
                        alt="Profile"
                        className="rounded-circle"
                        width="40"
                        height="40"
                      />
                    </button>
                    {profileVisible && (
                      <div
                        className="dropdown-menu dropdown-menu-end"
                        style={{
                          display: "block",
                          position: "absolute",
                          right: 0,
                          top: "50px",
                          zIndex: 1000,
                          backgroundColor: "#fff",
                          padding: "1rem",
                          border: "1px solid #ccc",
                          borderRadius: "0.25rem",
                          boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)",
                          width: "250px", // Increased width for the dropdown
                        }}
                      >
                        {isEditing ? (
                          <div>
                            <input
                              type="text"
                              className="form-control mb-2"
                              value={userInfo.firstName}
                              onChange={(e) =>
                                setUserInfo({ ...userInfo, firstName: e.target.value })
                              }
                              placeholder="First Name"
                            />
                            <input
                              type="text"
                              className="form-control mb-2"
                              value={userInfo.lastName}
                              onChange={(e) =>
                                setUserInfo({ ...userInfo, lastName: e.target.value })
                              }
                              placeholder="Last Name"
                            />
                            <input
                              type="text"
                              className="form-control mb-2"
                              value={userInfo.email}
                              onChange={(e) =>
                                setUserInfo({ ...userInfo, email: e.target.value })
                              }
                              placeholder="Email"
                            />
                            <button
                              className="btn btn-success btn-sm"
                              onClick={handleProfileEdit}
                            >
                              Save
                            </button>
                            <button
                              className="btn btn-secondary btn-sm ms-2"
                              onClick={() => setIsEditing(false)}
                            >
                              Cancel
                            </button>
                          </div>
                        ) : (
                          <div>
                            <p>
                              <strong>Name:</strong> {userInfo.firstName} {userInfo.lastName}
                            </p>
                            <p>
                              <strong>Email:</strong> {userInfo.email}
                            </p>
                            <button
                              className="btn btn-primary btn-sm"
                              onClick={() => setIsEditing(true)}
                            >
                              Edit Profile
                            </button>
                            <button
                              className="btn btn-danger btn-sm"
                              onClick={handleLogout}
                            >
                              Logout
                            </button>
                          </div>
                        )}
                      </div>
                    )}
                  </li>
                </>
              ) : (
                <>
                  <li className="nav-item">
                    <Link className="nav-link" to="/login">
                      Login
                    </Link>
                  </li>
                  <li className="nav-item">
                    <Link className="nav-link" to="/register">
                      Register
                    </Link>
                  </li>
                </>
              )}
            </ul>
          </div>
        </div>
      </nav>
    </header>
  );
};

export default HeaderComponent;
