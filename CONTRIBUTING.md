# Contributing

When contributing to this repository, please first discuss the change you wish to make via proposing an issue.

Chosen issues will then be added to the next project release.

## Pull Request Process

 1. Before working on anything please assign yourself the work move it to the in progress column. This is so that no two people work on the same work at the same time. 
 2. Ensure Java code abides by [Google's styleguide](https://google.github.io/styleguide/javaguide.html). Please update README.md as necessary.
 3. Push your code to a new branch following the formatting: `issue-{number}-{short description}`
 4. Before merging to developer please test your branch to make sure it is working correctly. Once a pull request has been opened from your branch into developer it must be approved by another developer. In the title of the merge request append `Fixes #{issue-number}`, so that automatic movement of tickets will be picked up on in the merge request body into master. 
 5. Before merging to master it must be approved by another developer. Increase the version when pushing to master. The versioning scheme we use is [SemVer](http://semver.org/).

## Branch structure

### Master branch

  -  Only for milestone releases or weekly releases
  -  Only merges from dev branch no direct commits

### Dev branch

Only merges from feature or bugfix branches no direct commits

### Feature and bug fix branch

Contain commits to address all the tasks or issues for the branch

## General thoughts to fixing effects and creating issues 

  -  The owner of a branch should fix the bug himself if possibly
  -  Bugs should be fixed in the same branch
  -  Code linting should happen before the PR and Merge. Codestyle Fixes should be part of the branch you merge in. 

## Commits , Pushes and Merges 

### Labels for commits

To make a commit title more speakable its good to give each commit a category in the title: 

feature: This commit or branch is a new added feature.

bugfix: This commit or branch introduces changes that fix a defect.  

refactor: All codestyle or refactore related commits

doc: documentation and guides commits

style: Changes in stylesheets that dont change any behaviour or code.

build: Changes in build system. Adding libraries or for example change in pom.xml  file

### Workflow

#### 1. Creating an issue

`[label]:[area]-Title of Issue`

Discribe the issue. Give some context if not obvious. Name particular methods or files.  

#### 2. Creating a branch

`[issueNumber]-[label]-[area]-Name-of-branch`

#### 3. Committing and pushing a branch

Before pushing the branch it should be checked/linted so you can be sure it passes all the codacy tests
If it does not pass the check changes need to be addressed in this branch till it passes. Further commits are necessary.
 
#### 4. Creating a pullrequest

`[label]:[area]-Title of Pullrequest (fixes issue number #4)`

Title would be th name of the branch plus the issue number it fixes. 
In the description describe what the PR will solve or address. 

#### 5. Merge a branch into dev

`[issueNumber]-[label]:[area]-Name of branch (PR number)`

  -  Title should not contain "merge ..." but the above syntax.
  -  Shall have the name of the branch.
 