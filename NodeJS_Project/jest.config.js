module.exports = {
  // Your Jest configuration options go here
  // For example:
  testEnvironment: "node",
  testMatch: ["**/*.test.js"],
  reporters: [
    "default",
    [
      "jest-sonar",
      {
        outputDirectory: "coverage",
        outputName: "sonar_ut_report.xml",
        reportedFilePath: "absolute",
      },
    ],
  ],
  coverageThreshold: {
    global: {
      branches: 90,
      functions: 90,
      lines: 80,
      statements: 90,
    },
  },
};
