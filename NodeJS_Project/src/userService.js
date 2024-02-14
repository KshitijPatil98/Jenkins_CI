// src/userService.js

function getUserById(userId) {
  console.log("inside getUserById function");
  console.log("added a new line");
  console.log("added one more line here")
  console.log("one liner")
  console.log("two liner")
  console.log("three liner")
  console.log("final commit")
  console.log("three liner")
  console.log("Four liner")
  console.log("final edit")
  console.log("State of mind")
  console.log("Hello world")
  console.log("Shalmu Balish")
  console.log("nnnnnnn")
  console.log("mmmmm")
  console.log("mmm")
  console.log("mmm")
  console.log("ssss")
  console.log("Jan 19th")
  console.log("Jan 20th")
  console.log("June 20th")
  console.log("Dec 20th")
  console.log("Dec 21th")
  console.log("hello")
  console.log("hello 1")
  console.log("hello 2")
  console.log("hello 3")
  console.log("gym")
  console.log("shamu")
  console.log("shamu1")
  console.log("kushmith")
  console.log("kushmith1")
  console.log("chicodi gatti porgi")
  // Assume this is a placeholder for fetching user data from a database
  // In a real project, this function would interact with a database or external service
  if (userId === 1) {
    return { id: 1, name: "John Doe" };
  } else {
    return null;
  }
}

function updateUser(userId, updatedUserData) {
  console.log("hello world")
  // Assume this is a placeholder for updating user data in a database
  // In a real project, this function would interact with a database or external service
  if (userId === 1) {
    return { id: 1, name: updatedUserData.name || "John Doe" };
  } else {
    return null;
  }
}

module.exports = {
  getUserById,
  updateUser,
};
