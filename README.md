# SMU Scientific Hub

This portal aims to provide an open platform that supports and facilitates collaborations between SMU and local industry. Industry can publish a challenge, and our platform will recommend SMU researcher(s), based on AI, Machine Learning, Deep Learning, Natural Language Processing, Knowledge Graph, and Data Mining techniques.

## Project Overview
This is a frontend-backend separated project, with each running on different ports.

- Frontend Port: `9038`
- Backend Port: `9039`

### How to Run the Project
To run the project, open both the backend and frontend projects and manage them using the sbt shell.

Steps
1. Open the project with the `build.sbt` file.

2. In the sbt shell, enter the following command:

```bash
run
```

This will start the frontend and backend on their respective ports. You can access the landing page by navigating to `http://localhost:9038`.

## Contribution Guidelines
For contributors, please follow these guidelines when making changes to the code:

- Create a new branch named after yourself.
- Make modifications in your own branch.
- Submit your changes via a pull request for review and merging.

By following this process, we ensure that all code changes are tracked and reviewed properly before being merged into the main project.

## Faculty Directory
SciHub now features a public **Faculty Directory** that allows users to explore the research profiles of SMU's faculty members without requiring an account.

### How to Use
- **Access the Directory**: Navigate to the homepage, locate the "SMU Researchers" section, and click the **Researcher List** link.
- **Search & Filter**: Use the search bar at the top of the directory to find researchers by their name, department, or specific area of research.
- **Interactive Word Cloud**: An interactive Research Interest Cloud is located at the top of the directory, visualizing the most common research topics across the faculty. Clicking on any keyword in the cloud will automatically filter the directory to display faculty specializing in that area.
- **Detailed Profiles**: Click on any researcher's card to view their complete profile, which includes their highest degree, contact information, and a list of their recent publications.
