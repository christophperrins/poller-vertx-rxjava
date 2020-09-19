const reset = require("../testUtils/resetDatabase")
const request = require("supertest");

before(async () => {
  await reset.emptyTables();
});

beforeEach(async () => {
  await reset.importData();
});

describe(process.env.POLL_ENDPOINT + " suite", () => {

  describe(process.env.POLL_ENDPOINT+ " GET suite", () => {
    it(`GET ${process.env.POLL_ENDPOINT} returns a list of 2 json objects`, done => {
      request("http://" + process.env.SERVER_HOST + ":" + process.env.SERVER_PORT)
        .get(process.env.POLL_ENDPOINT)
        .set('Accept', 'application/json')
        .expect('Content-Type', "application/json")
        .then(response => {
          if (Array.isArray(response.body) && response.body.length == 2) {
            done();
          } else {
            throw new Error("data.sql contains 2 records therefore 2 records were expected. Actual was: " + response.body.length);
          }
        })
        .catch(err => {
          done(err)
        });
    });

    it(`GET ${process.env.POLL_ENDPOINT} returns 200`, done => {
      request("http://" + process.env.SERVER_HOST + ":" + process.env.SERVER_PORT)
        .get(process.env.POLL_ENDPOINT)
        .set('Accept', 'application/json')
        .expect(200)
        .then(() => done())
        .catch(err => done(err));
    });
  })

  describe(process.env.POLL_ENDPOINT+ " POST suite", () => {
    it(`POST ${process.env.POLL_ENDPOINT} returns 201`, done => {
      request("http://" + process.env.SERVER_HOST + ":" + process.env.SERVER_PORT)
        .post(process.env.POLL_ENDPOINT)
        .set("Content-Type", "application/json")
        .set('Accept', 'application/json')
        .send({
          "name": "Wiki",
          "endpoint": "wikipedia.org",
          "frequency": 10000,
          "active": true
        })
        .expect(201)
        .then(() => done())
        .catch(err => done(err))
    })

    it(`POST ${process.env.POLL_ENDPOINT} returns created object`, done => {
      request("http://" + process.env.SERVER_HOST + ":" + process.env.SERVER_PORT)
        .post(process.env.POLL_ENDPOINT)
        .set("Content-Type", "application/json")
        .set('Accept', 'application/json')
        .expect('Content-Type', "application/json")
        .send({
          "name": "Wiki",
          "endpoint": "wikipedia.org",
          "frequency": 10000,
          "active": true
        })
        .expect(res => {
          let body = res.body;
          if (!(new RegExp("^[0-9]+$").test(body.id))) {
            throw new Error("body does not contain id")
          }
        })
        .then(() => done())
        .catch(err => done(err))
    });

  })

  describe(process.env.POLL_ENDPOINT+ " PUT suite", () => {
    it(`PUT ${process.env.POLL_ENDPOINT} should update an object`, done => {
      request("http://" + process.env.SERVER_HOST + ":" + process.env.SERVER_PORT)
        .put(process.env.POLL_ENDPOINT)
        .set("Content-Type", "application/json")
        .send({
          "id": 1,
          "name": "Githubb",
          "endpoint": "github.com",
          "frequency": 20000,
          "active": true
        }).then(async () => {
          await request("http://" + process.env.SERVER_HOST + ":" + process.env.SERVER_PORT)
            .get(process.env.POLL_ENDPOINT)
            .expect(response => {
              let githubObject = response.body.filter(obj => obj.id == 1)[0];
              if (githubObject.name != "Githubb") {
                throw new Error("Name not updated");
              } else if (githubObject.frequency != 20000) {
                throw new Error("Frequency not updated");
              }
              done()
            })

        }).catch(err => done(err))
    });

    it(`PUT ${process.env.POLL_ENDPOINT} should return 200`, done => {
      request("http://" + process.env.SERVER_HOST + ":" + process.env.SERVER_PORT)
        .put(process.env.POLL_ENDPOINT)
        .set("Content-Type", "application/json")
        .send({
          "id": 1,
          "name": "Githubb",
          "endpoint": "github.com",
          "frequency": 20000,
          "active": true
        }).expect(200)
        .then(() => done())
        .catch(err => done(err))
    });

    it(`PUT ${process.env.POLL_ENDPOINT} should return an object`, done => {
      request("http://" + process.env.SERVER_HOST + ":" + process.env.SERVER_PORT)
        .put(process.env.POLL_ENDPOINT)
        .set("Content-Type", "application/json")
        .set('Accept', 'application/json')
        .send({
          "id": 1,
          "name": "Githubb",
          "endpoint": "github.com",
          "frequency": 20000,
          "active": true
        })
        .expect('Content-Type', "application/json")
        .expect(response => {
          let githubObject = response.body;
          if (githubObject.id != 1) {
            throw new Error("id not returned");
          } else {
            done()
          }
        }).catch(err => done(err));
    })
  });

  describe(process.env.POLL_ENDPOINT+ " DELETE suite", () => {
    it(`DELETE ${process.env.POLL_ENDPOINT}/:id should remove an object`, done => {
      request("http://" + process.env.SERVER_HOST + ":" + process.env.SERVER_PORT)
        .delete(process.env.POLL_ENDPOINT+"/1")
        .then(() => {
          request("http://" + process.env.SERVER_HOST + ":" + process.env.SERVER_PORT)
            .get(process.env.POLL_ENDPOINT)
            .set('Accept', 'application/json')
            .expect(response => {
              if (response.body.length == 2) {
                throw new Error("Only one item should be in the database")
              }
            })
            .expect([{
                "id": 2,
                "name": "Gitlab",
                "endpoint": "gitlab.com",
                "frequency": 10000,
                "active": true
              }])
            .then(() => done())
            .catch(err => done(err))
        })
    });

    it(`DELETE ${process.env.POLL_ENDPOINT}/:id should return a 204`, done => {
      request("http://" + process.env.SERVER_HOST + ":" + process.env.SERVER_PORT)
        .delete(process.env.POLL_ENDPOINT+ "/1")
        .expect(204)
        .then(() => done())
        .catch(err => done(err))
    });

  });

});

afterEach(async () => {
  await reset.emptyTables();
});

after(async () => {
  await reset.emptyTables();
})


