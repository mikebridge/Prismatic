package com.bridgecanada.testhelper;

import org.apache.http.HttpResponse;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: bridge
 * Date: 30/03/13
 */
public class JettyLauncher {

    private int _port = 8080;
    private String _hostname = "localhost";
    private String _schema = "http";
    private ArrayList<ServletContextHandler> _serverContextHandlerList = new ArrayList<ServletContextHandler>();
    //private String _pathPattern;

    private Server _server;
    public JettyLauncher()  {


    }

    /**
     * start a new Jetty Server
     *
     * @throws Exception
     */
    public void start() throws RuntimeException {

        _server = createServer(getPort());

        //ServletContextHandler contextHandler = configureServletContextHandler();
        setHandlers(_server, _serverContextHandlerList);

        try {
            _server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * stop the Jetty Server
     *
     * @throws Exception
     */
    public void stop() throws Exception {
        _server.stop();
    }

    public String getHostname() {
        return _hostname;
    }

    public String getSchema() {
        return _schema;
    }

    public String getBasePath() {
        String url = _schema + "://"+_hostname;
        if (_port != 80) {
            url+= ":" + _port;
        }
        return url;
    }

    public int getPort() {
        return _port;
    }

    public void setPort(int port) {
        this._port = port;
    }

    /**
     * Create a server on this port
     * @param port
     * @return
     */
    private Server createServer(int port) {
        // TURN OFF LOGGING
        //org.apache.log4j.LogManager.getLogger("org.eclipse.jetty").setLevel(Level.WARN);
        //java.util.logging.LoggerFactory.getLogger("org.eclipse.jetty").setLevel(Level.WARNING);
        Properties p = new Properties();
        p.setProperty("org.eclipse.jetty.LEVEL", "OFF");
        org.eclipse.jetty.util.log.StdErrLog.setProperties(p);

        return new Server(port);
    }


    /**
     * Add the default server handlers given the context
     * @param contextHandlers
     */
    private void setHandlers(Server server, ArrayList<ServletContextHandler> contextHandlers) {
        HandlerCollection handlercoll = new HandlerCollection();

        Handler[] handlerArray = new Handler[contextHandlers.size()];
        contextHandlers.toArray(handlerArray);
        handlercoll.setHandlers(handlerArray);
        //handlers.setHandlers(new Handler[] { context, new DefaultHandler() });
        handlercoll.addHandler(new DefaultHandler());
        server.setHandler(handlercoll);
    }

    /**
     * add a handler for the servlet
     * @return
     */
    public void addHandler(String contextPath, Class<? extends Servlet> servletClass) {
        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setContextPath("/");

        //http://wiki.eclipse.org/Jetty/Tutorial/Embedding_Jetty
        //http://www.eclipse.org/jetty/documentation/current/quick-start-configure.html#quickstart-config-how
        //context.addServlet(new ServletHolder(new HelloServlet()),"/*");
        System.out.println("Adding servlet for "+contextPath);
        contextHandler.addServlet(servletClass, contextPath);
        _serverContextHandlerList.add(contextHandler);

    }

    public void addHandler(String contextPath, Servlet servlet) {
        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setContextPath("/");

        //http://wiki.eclipse.org/Jetty/Tutorial/Embedding_Jetty
        //http://www.eclipse.org/jetty/documentation/current/quick-start-configure.html#quickstart-config-how
        //context.addServlet(new ServletHolder(new HelloServlet()),"/*");
        System.out.println("Adding servlet for "+contextPath);
        ServletHolder holder = new ServletHolder();
        holder.setServlet(servlet);
        contextHandler.addServlet(holder, contextPath);
        _serverContextHandlerList.add(contextHandler);

    }


}
