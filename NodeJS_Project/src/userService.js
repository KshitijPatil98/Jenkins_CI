// src/userService.js

function getUserById(userId) {
  console.log("inside getUserById function");

  // Assume this is a placeholder for fetching user data from a database
  // In a real project, this function would interact with a database or external service
  if (userId === 1) {
    return { id: 1, name: "John Doe" };
  } else {
    return null;
  }
}

function updateUser(userId, updatedUserData) {
  console.log("inside update user");
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
