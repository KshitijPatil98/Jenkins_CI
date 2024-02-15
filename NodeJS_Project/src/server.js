const express = require("express");
const userService = require("./userService");

const app = express();
const PORT = 3001;
console.log("hello");
app.use(express.json());

app.get("/user/:id", (req, res) => {
  const userId = parseInt(req.params.id);
  const user = userService.getUserById(userId);

  if (user) {
    res.json(user);
  } else {
    res.status(404).json({ error: "User not found" });
  }
});

app.put("/user/:id", (req, res) => {
  const userId = parseInt(req.params.id);
  const updatedUserData = req.body;

  const updatedUser = userService.updateUser(userId, updatedUserData);

  if (updatedUser) {
    res.json(updatedUser);
  } else {
    res.status(404).json({ error: "User not found" });
  }
});

app.listen(PORT, () => {
  console.log(`Server is running on http://localhost:${PORT}`);
});
