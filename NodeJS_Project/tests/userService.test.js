const userService = require("../src/userService");

describe("User Service", () => {
  it("should get user by id", () => {
    const user = userService.getUserById(1);
    expect(user).toEqual({ id: 1, name: "John Doe" });
  });

  it("should return null for non-existent user id", () => {
    const user = userService.getUserById(999);
    expect(user).toBeNull();
  });

  it("should update user data", () => {
    const updatedUser = userService.updateUser(1, { name: "Jane Doe" });
    expect(updatedUser).toEqual({ id: 1, name: "Jane Doe" });
  });

  it("should update user data to John Doe if null specified", () => {
    const updatedUser = userService.updateUser(1, { name: null });
    expect(updatedUser).toEqual({ id: 1, name: "John Doe" });
  });

  it("should return null when updating non-existent user", () => {
    const updatedUser = userService.updateUser(999, { name: "Jane Doe" });
    expect(updatedUser).toBeNull();
  });
});
