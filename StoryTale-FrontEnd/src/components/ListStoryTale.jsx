import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const ListStoryTale = () => {
  const [stories, setStories] = useState([]);
  const navigate = useNavigate(); // To navigate programmatically

  useEffect(() => {
    axios.get(`http://localhost:8080/API/stories`,{ withCredentials: true })
      .then(response => {
        setStories(response.data);
      })
      .catch(error => {
        console.error("Error fetching stories:", error);
      });
  }, []);

  const handleUpdate = (storyId) => {
    navigate(`/storytale/${storyId}`);
  };
  const handlePreview = (storyId) => {
    navigate(`/preview/${storyId}`);
  };

  const handleDelete = (storyId) => {
    axios.delete(`http://localhost:8080/API/stories/${storyId}`,{ withCredentials: true })
      .then(() => {
        setStories(stories.filter(story => story.storyId !== storyId));
      })
      .catch(error => {
        console.error("Error deleting story:", error);
      });
  };

  return (
    <div className="container">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h2>My Stories</h2>
        <button
          type="button"
          className="btn btn-primary"
          onClick={() => navigate('/storytale/0')}
        >
          Create Story Tale
        </button>
      </div>
      <table className="table">
        <thead>
          <tr>
          <th style={{ width: "5%" }}>StoryID</th>
          <th style={{ width: "80%" }}>StoryName</th>
          <th style={{ width: "15%" }}>Action</th>
          </tr>
        </thead>
        <tbody>
          {stories.map((story) => (
            <tr key={story.storyId}>
              <td>{story.storyId}</td>
              <td>{story.storyTitle}</td>
              <td>
                <select
                  className="form-control"
                  onChange={(e) => {
                    const action = e.target.value;
                    if (action === 'update') {
                      handleUpdate(story.storyId);
                    } else if (action === 'delete') {
                      handleDelete(story.storyId);
                    }
                    else if (action === 'preview') {
                      handlePreview(story.storyId);
                    }
                  }}
                >
                  <option value="">Select Action</option>
                  <option value="update">Update</option>
                  <option value="delete">Delete</option>
                  <option value="preview">Preview</option>
                </select>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ListStoryTale;
