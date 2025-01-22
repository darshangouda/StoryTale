import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import "./Preview.css"; // Add your styles if needed

const Preview = () => {
  const { storyID } = useParams();
  const [storyTitle, setStoryTitle] = useState("");
  const [stories, setStories] = useState([]);
  const [userInfo, setUserInfo] = useState({ firstName: "", lastName: "", email: "" }); // Added state for user info

  useEffect(() => {
    // Fetch story details using storyID
    axios
      .get(`http://localhost:8080/API/stories/${storyID}`, { withCredentials: true })
      .then((response) => {
        setStoryTitle(response.data.storyTitle || "Untitled");
        const sortedStories = (response.data.storyLines || []).sort((a, b) => a.id - b.id);
        setStories(sortedStories); // Sort stories by ID
      })
      .catch((error) => {
        console.error("Error fetching story data:", error);
      });

    // Fetch user profile details
    axios
      .get("http://localhost:8080/API/profile", { withCredentials: true })
      .then((response) => {
        if (response.status === 200) {
          setUserInfo(response.data);
        }
      })
      .catch((error) => {
        console.error("Error fetching profile data:", error);
      });
  }, [storyID]);

  return (
    <div className="preview-container">
      <div className="story-info d-flex align-items-center justify-content-between">
  <div>
    <h4>
      <strong>Story Title:</strong> {storyTitle}
    </h4>
    <h4>
      <strong>Story ID:</strong> {storyID}
    </h4>
  </div>
  <button className="btn btn-primary mt-3" onClick={() => window.print()}>
    Print
  </button>
</div>

      <hr />
      {/* User Profile Information */}
      <div className="user-info">
        <h3>Writer Info:</h3>
        <p>
          <strong>First Name:</strong> {userInfo.firstName}
        </p>
        <p>
          <strong>Last Name:</strong> {userInfo.lastName}
        </p>
        <p>
          <strong>Email:</strong> {userInfo.email}
        </p>
      </div>
      <hr />
      {/* Story List */}
      <div className="story-list">
        <table className="table table-bordered">
          <tbody>
            {/* Group stories in pairs for two columns */}
            {stories.reduce((rows, story, index) => {
              if (index % 2 === 0) {
                rows.push([story]); // Start a new row with the current story
              } else {
                rows[rows.length - 1].push(story); // Add to the last row
              }
              return rows;
            }, []).map((row, rowIndex) => (
              <tr key={rowIndex}>
                {row.map((story, colIndex) => (
                  <td key={colIndex} style={{ textAlign: "left" }}>
                    {story.storyImage && (
                      <img
                        src={`data:image/jpeg;base64,${story.storyImage}`}
                        alt={`Story Image ${rowIndex * 2 + colIndex + 1}`}
                        className="story-image"
                      />
                    )}
                    <p>{rowIndex * 2 + colIndex + 1}. {story.storyLine || "No story text provided."}</p>
                  </td>
                ))}
                {/* Add empty cell for rows with an odd number of items */}
                {row.length < 2 && <td></td>}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      
    </div>
  );
};

export default Preview;
