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
SciHub now includes a public Faculty Directory where users can explore the research profiles of SMU faculty members without needing to log in.
How to Use
**Open the Directory**: From the homepage, go to the "SMU Researchers" section and select the Researcher List link.
**Search and Filter**: Use the search bar to look for faculty by name, department, or research area.
**Research Interest Cloud**: At the top of the page, you’ll see a word cloud showing popular research topics across the faculty. Clicking any keyword will automatically filter the directory to show researchers connected to that area.
**View Full Profiles**: Select any faculty card to open a detailed profile page with additional information such as highest degree, contact details, and recent publications.
