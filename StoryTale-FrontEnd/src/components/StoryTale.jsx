import { useState, useEffect, useRef } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import "./StoryTale.css"; // Add CSS for loading spinner

const StoryTale = () => {
  const { storyID } = useParams();
  const navigate = useNavigate();
  const [title, setTitle] = useState(""); // Story title
  const [storyIDState, setStoryIDState] = useState(storyID ? parseInt(storyID, 10) : 0);
  const [stories, setStories] = useState([]); // List of stories in the table
  const [isLoading, setIsLoading] = useState(false); // For loading indicator
  const hasFetched = useRef(false);
  const [loadingRows, setLoadingRows] = useState([]); // Track loading rows
  // Reset state variables to their initial values
  const resetState = () => {
    setTitle("");
    setStoryIDState(0);
    setStories([]);
    hasFetched.current = false;
  };

  // Fetch story details from the API
  const fetchStoryDetails = async (id) => {
    try {
      setIsLoading(true);
      console.log("StoryID", storyID);
      const response = await axios.get(`http://localhost:8080/API/stories/${id}`, { withCredentials: true });
      setTitle(response.data.storyTitle || "");
      const sortedStories = (response.data.storyLines || []).sort((a, b) => a.index - b.index);
      setStories(sortedStories);
    } catch (error) {
      console.error("Error fetching story data:", error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    //console.log("hasFetched.current",hasFetched.current);
    if (storyID == 0) {
      resetState();
      return;
    }
    if (hasFetched.current) return;

    setStoryIDState(parseInt(storyID, 10));
    hasFetched.current = true;
    fetchStoryDetails(parseInt(storyID, 10));
  }, [storyID]);


  // Save title
  const saveTitle = () => {
    if (!title.trim()) {
      alert("Title cannot be empty!");
      return;
    }

    setIsLoading(true);
    axios
      .post(`http://localhost:8080/API/stories/${storyIDState}/title/${title}`, {}, { withCredentials: true })
      .then((response) => {
        setStoryIDState(response.data);
        alert("Title saved successfully!");
        navigate(`/storytale/${response.data}`);
      })
      .catch((error) => console.error("Error saving title:", error))
      .finally(() => setIsLoading(false));
  };

  // Add a new row
  const addNewRow = () => {
    if (storyIDState <= 0) {
      alert("Save the story title before adding sections.");
      return;
    }
    const newIndex = stories.length > 0 ? stories[stories.length - 1].index + 1 : 1;
    setStories([...stories, { index: newIndex, imagePrompt: "", storyLine: "", storyImage: "" }]);
  };

  // Handle text change in story rows
  const handleStoryTextChange = (row, event) => {
    const updatedStories = [...stories];
    updatedStories[row].storyLine = event.target.value;
    setStories(updatedStories);
  };
  // Handle Image prompt in story rows
  const handleImagePromptChange = (row, event) => {
    const updatedStories = [...stories];
    updatedStories[row].imagePrompt = event.target.value;
    setStories(updatedStories);
  };

  const handleAction = async (index, row, action) => {
    const story = stories[row];
    const storyText = story?.storyLine || "";
    const imagePrompt = story?.imagePrompt || "";

    if (action === "generate") {
      if (!imagePrompt.trim()) {
        alert(`Row ${row + 1}: enter an image prompt first.`);
        return;
      }

      setLoadingRows((prev) => [...prev, row]);
      try {
        const response = await axios.get(
          `http://localhost:8080/API/stories/${storyIDState}/image/${index}/${encodeURIComponent(imagePrompt)}`,
          { responseType: "text", withCredentials: true }
        );
        setStories((prev) =>
          prev.map((item, itemRow) =>
            itemRow === row ? { ...item, storyImage: response.data } : item
          )
        );
        alert(`Image and prompt saved for row ${row + 1}.`);
      } catch (error) {
        console.error("Error generating image:", error);
        alert(error.response?.data || "Could not generate and save the image.");
      } finally {
        setLoadingRows((prev) => prev.filter((r) => r !== row));
      }
    } else if (action === "save") {
      if (!storyText.trim()) {
        alert(`Row ${row + 1}: enter a story paragraph first.`);
        return;
      }

      try {
        await axios.post(
          `http://localhost:8080/API/stories/${storyIDState}/content/${index}/${encodeURIComponent(storyText)}`,
          {},
          { withCredentials: true }
        );
        alert(`Paragraph saved for row ${row + 1}.`);
      } catch (error) {
        console.error("Error saving paragraph:", error);
        alert(error.response?.data || "Could not save the paragraph.");
      }
    } else if (action === "delete") {
      if (!window.confirm(`Delete story section ${row + 1}? This cannot be undone.`)) {
        return;
      }
      try {
        await axios.delete(
          `http://localhost:8080/API/stories/${storyIDState}/section/${index}`,
          { withCredentials: true }
        );
        setStories((prev) => prev.filter((_, itemRow) => itemRow !== row));
      } catch (error) {
        console.error("Error deleting section:", error);
        alert(error.response?.data || "Could not delete the story section.");
      }
    }
  };

  const handlePreviewClick = () => {
    navigate(`/preview/${storyIDState}`);
  };

  return (
    <div className="container">
      {isLoading && (
        <div className="loading-overlay">
          <div className="spinner-border text-primary" role="status">
            <span className="sr-only">Loading...</span>
          </div>
        </div>
      )}
      <h2>{storyIDState === 0 ? "Create New Story" : "Edit Story"}</h2>

      <table className="table" style={{ tableLayout: "fixed", width: "100%" }}>
        <thead>
          <tr>
            <th style={{ width: "20%" }}>Story ID</th>
            <th style={{ width: "60%" }}>Story Title</th>
            <th style={{ width: "20%" }}>Action</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>
              <input type="text" className="form-control" value={storyIDState} readOnly />
            </td>
            <td>
              <input
                type="text"
                className="form-control"
                placeholder="Enter Story Title"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
              />
            </td>
            <td>
              <button className="btn btn-primary" onClick={saveTitle}>
                {storyIDState === 0 ? "Add Title" : "Save Title"}
              </button>
            </td>
          </tr>
        </tbody>
      </table>

      <h3>Story Lines</h3>
      <table className="table" style={{ tableLayout: "fixed", width: "100%" }}>
        <thead>
          <tr>
            <th style={{ width: "5%" }}>No.</th>
            <th style={{ width: "30%" }}>Image</th>
            <th style={{ width: "45%" }}>Story</th>
            <th style={{ width: "20%" }}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {stories.map((story, row) => (
            <tr key={row}>
              <td>{row + 1}</td>
              <td>
                {loadingRows.includes(row) ? (
                  <img
                    src="https://media2.giphy.com/media/v1.Y2lkPTc5MGI3NjExODRrZjQ1b3duNXdid2lqeWplaWpvdmp1MTk2YXJsM2czdWVrMmhjdiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/xTk9ZvMnbIiIew7IpW/giphy.gif"
                    alt="Loading..."
                    style={{ width: "220px", height: "auto" }}
                  />
                ) : (
                  <img
                    src={
                      story.storyImage
                        ? `data:image/jpeg;base64,${story.storyImage}`
                        : "https://placehold.jp/180x180.png"
                    }
                    alt={`Story ${story.id}`}
                    style={{ width: "220px", height: "auto" }}
                  />
                )}
              </td>

              <td>
                <b>Image Prompt</b>
                <textarea
                  className="form-control"
                  placeholder="Enter Image Prompt"
                  value={story.imagePrompt}
                  onChange={(e) => handleImagePromptChange(row, e)}
                  rows={3}
                />
                <b>Story Line</b>
                <textarea
                  className="form-control"
                  placeholder="Enter story line"
                  value={story.storyLine}
                  onChange={(e) => handleStoryTextChange(row, e)}
                  rows={3}
                />
              </td>
              <td className="d-flex flex-column gap-2">
                <button
                  type="button"
                  className="btn btn-primary btn-sm"
                  disabled={loadingRows.includes(row) || !story.imagePrompt?.trim()}
                  onClick={() => handleAction(story.index, row, "generate")}
                >
                  {loadingRows.includes(row) ? "Generating..." : "Generate Image & Save Prompt"}
                </button>
                <button
                  type="button"
                  className="btn btn-success btn-sm"
                  disabled={!story.storyLine?.trim()}
                  onClick={() => handleAction(story.index, row, "save")}
                >
                  Save Paragraph
                </button>
                <button
                  type="button"
                  className="btn btn-outline-danger btn-sm"
                  onClick={() => handleAction(story.index, row, "delete")}
                >
                  Delete Section
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {storyIDState > 0 ? (
        <button className="btn btn-primary" onClick={addNewRow}>
          +
        </button>
      ) : (
        <p className="text-muted">Save the story title before adding sections.</p>
      )}
      {storyIDState > 0 && (
        <>
          <br />
          <button className="btn btn-success mt-3" onClick={handlePreviewClick}>
            Preview
          </button>
        </>
      )}
    </div>
  );
};

export default StoryTale;
