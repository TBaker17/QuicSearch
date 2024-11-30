chrome.runtime.onInstalled.addListener(() => {
  console.log("QuicSearch extension installed.");
});

chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  if (message.type === "logSearch") {
    console.log(`Search logged: ${message.query}`);
  }
});
