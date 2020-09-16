# Contributing

When contributing to this repository, please first discuss the change you wish to make via proposing an issue.

Chosen issues will then be added to the next project release.

## Pull Request Process

1. Before working on anything please assign yourself the work move it to the in progress column. This is so that no two people work on the same work at the same time. 
1. Ensure Java code abides by [Google's styleguide](https://google.github.io/styleguide/javaguide.html). Please update README.md as necessary.
1. Push your code to a new branch following the formatting: `issue-{number}-{short description}`
1. Before merging to developer please test your branch to make sure it is working correctly. Once a pull request has been opened from your branch into developer it must be approved by another developer. In the title of the merge request append `Fixes #{issue-number}`, so that automatic movement of tickets will be picked up on in the merge request body into master. 
1. Before merging to master it must be approved by another developer. Increase the version when pushing to master. The versioning scheme we use is [SemVer](http://semver.org/).
