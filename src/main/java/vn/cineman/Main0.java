package vn.cineman;

import java.nio.file.*;
import org.apache.catalina.startup.Tomcat;
import vn.cineman.dao.Database0;
import vn.cineman.web.AppServlet0;

public final class Main0 {
  public static void main(String[] args) throws Exception {
    Files.createDirectories(Path.of("data"));
    var db =
        new Database0(System.getenv().getOrDefault("CINEMAN_DB_URL", "jdbc:h2:./data/cineman"));
    db.initialize();
    var server = new Tomcat();
    server.setBaseDir("build/tomcat");
    server.setPort(Integer.parseInt(System.getenv().getOrDefault("PORT", "8080")));
    server.getConnector().setProperty("address", "127.0.0.1");
    var context = server.addContext("", Path.of(".").toAbsolutePath().toString());
    context.setSessionTimeout(30);
    var wrapper = Tomcat.addServlet(context, "app", new AppServlet0(db));
    wrapper.setLoadOnStartup(1);
    context.addServletMapping("/", "app", false);
    server.start();
    System.out.println("CineMan: http://127.0.0.1:" + server.getConnector().getPort());
    server.getServer().await();
  }
}
