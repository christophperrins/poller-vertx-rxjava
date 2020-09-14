package digital.vix.vertxdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import digital.vix.vertxdemo.controller.EndpointController;
import digital.vix.vertxdemo.controller.EndpointHistoryController;
import digital.vix.vertxdemo.controller.EndpointMetaDataController;
import digital.vix.vertxdemo.repository.EndpointHistoryRepository;
import digital.vix.vertxdemo.repository.EndpointHistoryRepositoryImpl;
import digital.vix.vertxdemo.repository.EndpointRepository;
import digital.vix.vertxdemo.repository.EndpointRepositoryImpl;
import digital.vix.vertxdemo.service.EndpointHistoryService;
import digital.vix.vertxdemo.service.EndpointHistoryServiceImpl;
import digital.vix.vertxdemo.service.EndpointService;
import digital.vix.vertxdemo.service.EndpointServiceImpl;
import digital.vix.vertxdemo.service.InformationService;
import digital.vix.vertxdemo.service.InformationServiceImpl;
import digital.vix.vertxdemo.service.PollService;
import digital.vix.vertxdemo.service.PollServiceImpl;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.jdbc.JDBCClient;
import io.vertx.reactivex.ext.sql.SQLClient;
import io.vertx.reactivex.ext.web.Router;

public class Application {

	Logger logger = LoggerFactory.getLogger(Application.class);
	private SQLClient sqlClient;

	public static void main(String[] args) {
		new Application().run();
	}

	public void run() {
		Vertx vertx = Vertx.vertx();

		JsonObject jsonObject = new JsonObject()
				.put("url", "jdbc:mysql://localhost:3306/pollervertx?serverTimezone=BST")
				.put("driver_class", "com.mysql.cj.jdbc.Driver").put("user", "root").put("password", "root")
				.put("max_pool_size", 30);

		sqlClient = JDBCClient.createShared(vertx, jsonObject);

		logger.info("Connecting to database...");
		sqlClient.getConnection(ar -> {
			if (ar.failed()) {
				throw new RuntimeException("Connection could not made to database");
			} else {
				logger.info("Connected to database");
				initialise(vertx);
			}
		});

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				sqlClient.close();
				vertx.close();
			}
		});
	}

	public void initialise(Vertx vertx) {
		logger.debug("Creating router");
		Router router = Router.router(vertx);

		logger.debug("Deploying Poller Controller");
		ObjectMapper mapper = new ObjectMapper();

		EndpointRepository endpointRepository = new EndpointRepositoryImpl(sqlClient);
		EndpointHistoryRepository endpointHistoryRepository = new EndpointHistoryRepositoryImpl(sqlClient);

		PollService pollService = new PollServiceImpl(vertx, endpointHistoryRepository);
		EndpointService endpointService = new EndpointServiceImpl(endpointRepository);
		EndpointHistoryService endpointHistoryService = new EndpointHistoryServiceImpl(endpointHistoryRepository,
				pollService);

		vertx.rxDeployVerticle(new EndpointController(router, mapper, endpointService, endpointHistoryService))
				.subscribe(data -> vertx
						.deployVerticle(new EndpointHistoryController(router, mapper, endpointHistoryService)));

		InformationService informationService = new InformationServiceImpl();
		vertx.deployVerticle(new EndpointMetaDataController(router, mapper, endpointService, informationService));
		int port = 8080;
		vertx.createHttpServer().requestHandler(router).rxListen(port)
				.subscribe(httpServer -> logger.info("HttpServer running on port: " + port));
	}

}
