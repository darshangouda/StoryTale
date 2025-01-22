import React from 'react';

const FooterComponent = () => {
  return (
    <footer className="bg-dark text-center text-white py-4">
      <section className="mb-3">
        <a className="btn btn-outline-light btn-floating mx-2" href="#!" role="button">
          <i className="fab fa-facebook-f"></i>
        </a>
        <a className="btn btn-outline-light btn-floating mx-2" href="#!" role="button">
          <i className="fab fa-twitter"></i>
        </a>
        <a className="btn btn-outline-light btn-floating mx-2" href="#!" role="button">
          <i className="fab fa-google"></i>
        </a>
        <a className="btn btn-outline-light btn-floating mx-2" href="#!" role="button">
          <i className="fab fa-instagram"></i>
        </a>
        <a className="btn btn-outline-light btn-floating mx-2" href="#!" role="button">
          <i className="fab fa-linkedin-in"></i>
        </a>
        <a className="btn btn-outline-light btn-floating mx-2" href="#!" role="button">
          <i className="fab fa-github"></i>
        </a>
      </section>
      <div className="text-center">
        © 2024 Copyright: 
        <a className="text-white ms-1" href="https://www.storytale.com/">
          StoryTale.com
        </a>
      </div>
    </footer>
  );
};

export default FooterComponent;
