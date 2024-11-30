const apiKey = 'YOUR_GOOGLE_API_KEY'; // Replace with your API Key
const cx = 'YOUR_CUSTOM_SEARCH_ENGINE_ID'; // Replace with your Custom Search Engine ID

document.getElementById("searchButton").addEventListener("click", () => {
  const query = document.getElementById("searchInput").value.trim();  // Trim to remove excess spaces
  
  if (query) {
    fetchSearchResults(query);
  } else {
    alert("Please enter a search query.");
  }
});

function fetchSearchResults(query) {
  const url = `https://www.googleapis.com/customsearch/v1?q=${encodeURIComponent(query)}&key=${apiKey}&cx=${cx}`;
  
  fetch(url)
    .then(response => response.json())
    .then(data => {
      if (data.items) {
        displaySearchResults(data.items);
      } else {
        alert("No results found.");
      }
    })
    .catch(error => {
      console.error('Error fetching search results:', error);
      alert('Error fetching results. Please try again later.');
    });
}

function displaySearchResults(results) {
  const resultsContainer = document.createElement('div');
  resultsContainer.style.maxHeight = '250px';
  resultsContainer.style.overflowY = 'auto';

  results.forEach(result => {
    const resultItem = document.createElement('div');
    resultItem.style.marginBottom = '15px';

    const title = document.createElement('h3');
    title.innerHTML = `<a href="${result.link}" target="_blank">${result.title}</a>`;
    resultItem.appendChild(title);

    const snippet = document.createElement('p');
    snippet.textContent = result.snippet;
    resultItem.appendChild(snippet);

    resultsContainer.appendChild(resultItem);
  });

  // Clear previous results and append new ones
  const iframe = document.getElementById("searchResults");
  iframe.style.display = "none";  // Hide the iframe since we are showing results inside the mini tab
  const searchContainer = document.querySelector('.search-container');
  searchContainer.appendChild(resultsContainer);
}





  
