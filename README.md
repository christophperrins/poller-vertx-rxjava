# Vertx Poller
[![Contributors][contributors-shield]][contributors-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
![commit][commits-shield]
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/bccc5ed2c8e1438998881d4a4ddeb8e7)](https://www.codacy.com/manual/christophperrins/poller-vertx-rxjava?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=christophperrins/poller-vertx-rxjava&amp;utm_campaign=Badge_Grade)

<!-- Links -->
[contributors-shield]: https://img.shields.io/github/contributors/christophperrins/poller-vertx-rxjava.svg
[contributors-url]: https://github.com/christophperrins/poller-vertx-rxjava/graphs/contributors

[issues-shield]: https://img.shields.io/github/issues/christophperrins/poller-vertx-rxjava.svg
[issues-url]: https://github.com/christophperrins/poller-vertx-rxjava/issues

[license-shield]: https://img.shields.io/github/license/christophperrins/poller-vertx-rxjava.svg
[license-url]: https://github.com/christophperrins/poller-vertx-rxjava/blob/master/LICENSE.txt

[commits-shield]: https://img.shields.io/github/commits-since/christophperrins/poller-vertx-rxjava/v0.0.0/developer?label=commits%20since%20v0. 0. 0

This project uses vertx and rxjava to create a backend system which can poll endpoints. Information about the speed of reply is then saved to a mysql database. Metadata can also be attached to an endpoint to tie information together. 

## Integration Testing

Integration tests have been written in Javascript using SuperTest and the Mocha testing framework. 

1) First install the packages `npm i` 

2) Next make sure the schema is correct and empty of information. 

3) Start the application. 

4) To run the tests run `npm run test` . 

5) If you want test output to a file run `npm run test > results.txt` 
