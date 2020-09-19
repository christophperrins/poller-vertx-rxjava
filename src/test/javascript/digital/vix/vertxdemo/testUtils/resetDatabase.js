let mysql = require('mysql');
let fs = require('fs');

let db = () => {
  let connection = mysql.createConnection({
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
  });

  connection.connect();

  let sendQuery = query => {
    let querying = connection.query(query);
    connection.commit();
    return querying;
  };

  let closeConnection = () => connection.end();

  return {
    sendQuery,
    closeConnection
  }
}

let getArrayOfQueries = location => {
  let file = fs.readFileSync(location, "utf-8");
  let lines = file.split("\r" + String.fromCharCode(10));
  let cache = "";
  let queries = [];
  for (let line of lines) {
    if (line.trim() == "") {
      continue;
    }
    if (line.trim().endsWith(";")) {
      cache += line;
      queries.push(cache);
      cache = "";
    } else {
      cache += line
    }
  }
  return queries;
}

let sendQueries = async (queries) => {
  let connect = db();

  connect.sendQuery("use " + process.env.DB_DATABASE);

  for (let query of queries) {
    await new Promise((res, rej) => {
      let queryResponse = connect.sendQuery(query);
      queryResponse.on("result", () => res());
      queryResponse.on("error", err => rej(err))
    }) ;
  }
  connect.closeConnection();

}

exports.initSchema = async () => {
  let schemaQueries = getArrayOfQueries(__dirname + "/../../../../../resources/schema.sql", "utf-8");
  sendQueries(schemaQueries);
}


exports.dropTable = () => {
  let queries = [];
  queries.push("DROP DATABASE " + process.env.DB_DATABASE);
}

exports.importData = async () => {
  let dataQueries = getArrayOfQueries(__dirname + "/../../../../../resources/data.sql", "utf-8");
  await sendQueries(dataQueries);
}

exports.emptyTables = async () => {
  let dataQueries = getArrayOfQueries(__dirname + "/../../../../../resources/empty-tables.sql", "utf-8");
  await sendQueries(dataQueries);
}
