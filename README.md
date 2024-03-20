# NodeJS Project with end-to-end Jenkins CI pipeline and SonarQube

## Table of Contents

- [Introduction](#introduction)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)

## Introduction

This project showcases a Node.js application with continuous integration set up using Jenkins and SonarQube for code quality analysis.
The pipeline is configured with the following stages.

**1. Clone from GitHub: Clone the repository to start the pipeline.
  2. Install Dependencies: Install Node.js project dependencies.
  3. Linting: Perform linting on the Node.js project.
  4. Unit Testing: Execute unit tests for the Node.js application.
  5. Coverage: Generate code coverage report.
  6. SonarQube: Run SonarQube analysis and wait for the quality gate.
  7. Docker Image Build and Push: Build and push Docker image to Docker Hub. We tag the docker image with the latest commit ID
  Note: The pipeline includes cleanup steps in case of success or failure, removing Docker images to maintain a clean environment.**


## Prerequisites

Before you begin, ensure you have the following installed:

- Node.js and npm
- Docker and Docker Compose
- Jenkins
- SonarQube

## Getting Started

1. **Fork the Repository:**
   - Click on the "Fork" button at the top right of this repository to create your copy.

2. **Clone Your Fork:**
   ```bash
   git clone https://github.com/your-username/your-nodejs-project.git  
   
3. **Create Declarative Jenkins Pipeline:**
   Copy the contents of the Jenkinsfile and create a new Jenkins pipeline in your Jenkins instance. Use this script to define your CI/CD pipeline.
   Additionally, you can also create a webhook on Git Hub and configure the pipeline to start the build after changes are pushed on the main branch.
   
5. **Start SonarQube:**
   docker-compose -f sonarqube-docker-compose.yml up -d

4. **Configure SonarQube:**
   Access SonarQube on http://localhost:9000.
   Configure a new project for your Node.js application. Also, create a token for this Node.js project.
   Now make sure you create a webhook for Jenkins. This webhook will allow SonarQube to send back quality gate status to the Jenkins pipeline in the Sonarqube stage.
   
6. **Configure SonarQube and sonar-scanner on Jenkins:**
   Open your Jenkins. Navigate to "Manage Jenkins -> Tools" and add the SonarQube Scanner installations.
   Now navigate to "Manage Jenkins -> System -> SonarQube servers" and add details of the SonarQube server that you started using docker.

7. **You are all set to build your end-to-end Jenkins, SonarQube CI pipeline**











