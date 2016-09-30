package com.leo.test.embedded.jetty;

import org.eclipse.jetty.annotations.ServletContainerInitializersStarter;
import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
import org.eclipse.jetty.jsp.JettyJspServlet;
import org.eclipse.jetty.plus.annotation.ContainerInitializer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Senchenko Viktor on 30.09.2016.
 */
public class App {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        App main = new App(port);
        main.start();
    }

    private final String WEBROOT_INDEX = "/";

    private int port;

    private Server server;

    private App(int port) {
        this.port = port;
    }

    private void start() throws Exception {
        server = new Server();

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.addConnector(connector);

        URI baseUri = this.getClass().getResource(WEBROOT_INDEX).toURI();

        //        // Set JSP to use Standard JavaC always
        //        System.setProperty("org.apache.jasper.compiler.disablejsr199", "false");

        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/");
//        webAppContext.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/[^/]*servlet-api-[^/]*\\.jar$|.*/javax.servlet.jsp.jstl-.*\\.jar$|.*/.*taglibs.*\\.jar$");
        webAppContext.setResourceBase(baseUri.toASCIIString());

        ContainerInitializer initializer = new ContainerInitializer(new JettyJasperInitializer(), null);
        List<ContainerInitializer> jspInitializers = new ArrayList<>();
        jspInitializers.add(initializer);

        webAppContext.setAttribute("org.eclipse.jetty.containerInitializers", jspInitializers);
        //        webAppContext.setAttribute(InstanceManager.class.getName(), new SimpleInstanceManager());
        webAppContext.addBean(new ServletContainerInitializersStarter(webAppContext), true);
        //        webAppContext.setClassLoader(new URLClassLoader(new URL[0], this.getClass().getClassLoader()));

        ServletHolder holderJsp = new ServletHolder("jsp", JettyJspServlet.class);
        //        holderJsp.setInitOrder(0);
        //        holderJsp.setInitParameter("logVerbosityLevel", "DEBUG");
        //        holderJsp.setInitParameter("fork", "false");
        //        holderJsp.setInitParameter("xpoweredBy", "false");
        //        holderJsp.setInitParameter("compilerTargetVM", "1.7");
        //        holderJsp.setInitParameter("compilerSourceVM", "1.7");
        //        holderJsp.setInitParameter("keepgenerated", "true");

        webAppContext.addServlet(holderJsp, "*.jsp");

        //
        //        ServletHolder holderDefault = new ServletHolder("default", DefaultServlet.class);
        //        holderDefault.setInitParameter("resourceBase", baseUri.toASCIIString());
        //        holderDefault.setInitParameter("dirAllowed", "true");
        //
        //
        //        webAppContext.addServlet(holderDefault, "/");

        server.setHandler(webAppContext);

        // Start Server
        server.start();
        System.out.println("Server started on http://localhost:" + port);
    }
}
