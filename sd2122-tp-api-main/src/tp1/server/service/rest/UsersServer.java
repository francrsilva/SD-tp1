package tp1.server.service.rest;

import java.net.InetAddress;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import tp1.server.service.rest.util.Discovery;
import tp1.server.service.rest.resources.UsersResource;
import util.Debug;

public class UsersServer {

	private static Logger Log = Logger.getLogger(UsersServer.class.getName());

	static {
		System.setProperty("java.net.preferIPv4Stack", "true");
	}

	public static final int PORT = 8080;
	public static final String SERVICE = "users";
	private static final String SERVER_URI_FMT = "http://%s:%s/rest";
	private static Discovery disc;
	public static void main(String[] args) {
		try {
			Debug.setLogLevel( Level.INFO, Debug.SD2122 );

			ResourceConfig config = new ResourceConfig();
			config.register(UsersResource.class);
			//config.register(CustomLoggingFilter.class);
			//config.register(GenericExceptionMapper.class);

			String ip = InetAddress.getLocalHost().getHostAddress();
			String serverURI = String.format(SERVER_URI_FMT, ip, PORT);
			JdkHttpServerFactory.createHttpServer( URI.create(serverURI), config);

			Log.info(String.format("%s Server ready @ %s\n",  SERVICE, serverURI));

			//More code can be executed here...

			disc = new Discovery(Discovery.DISCOVERY_ADDR, SERVICE, serverURI);

			disc.announce(SERVICE, serverURI);
			disc.listener();

		} catch( Exception e) {
			Log.severe(e.getMessage());
		}
	}	
	public static URI[] foundURI(String serviceName) {
		return disc.knownUrisOf(serviceName);
	}
}
