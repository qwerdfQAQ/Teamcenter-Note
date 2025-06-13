package com.teamcenter.clientx;

import org.apache.log4j.Logger;

import com.teamcenter.schemas.soa._2006_03.exceptions.InvalidCredentialsException;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;
import com.teamcenter.services.strong.core.SessionService;
import com.teamcenter.services.strong.core._2006_03.Session.LoginResponse;
import com.teamcenter.services.strong.core._2007_12.Session.StateNameValue;
import com.teamcenter.soa.SoaConstants;
import com.teamcenter.soa.client.Connection;
import com.teamcenter.soa.client.model.Property;
import com.teamcenter.soa.client.model.strong.User;

public class TcSession
{
	private static final String LanguageEnv_CN = "zh_CN";
	
	private static final Logger logger = Logger.getLogger(TcSession.class);
    /**
     * Single instance of the Connection object that is shared throughout
     * the application. This Connection object is needed whenever a Service
     * stub is instantiated.
     */
	private static Connection           connection;

    /**
     * The credentialManager is used both by the Session class and the Teamcenter
     * Services Framework to get user credentials.
     *
     */
    public User user;
    
    private static AppXCredentialManager credentialManager;

    /**
     * Create an instance of the Session with a connection to the specified
     * server.
     *
     * Add implementations of the ExceptionHandler, PartialErrorListener,
     * ChangeListener, and DeleteListeners.
     *
     * @param host      Address of the host to connect to, http://serverName:port/tc
     */
    private TcSession()
    {
    	String host="";
    	PropertiesUtil prop = new PropertiesUtil();
        String ConnectionType = prop.getProperyValue("ConnectionType").toString();
        String iiop_server_host = prop.getProperyValue("iiop_server").toString();
        String http_server_host = prop.getProperyValue("http_server").toString();
        String tccs_serverr_host = prop.getProperyValue("tccs_server").toString();
        if(ConnectionType.equalsIgnoreCase("2")){
        	host=iiop_server_host;
        } else if(ConnectionType.equalsIgnoreCase("4")){
        	host=http_server_host;
        } else if(ConnectionType.equalsIgnoreCase("3")){
        	host=tccs_serverr_host;
        }

        credentialManager = new AppXCredentialManager();
        String protocol=null;
        String envNameTccs = null;
        if ( host.startsWith("http") )
        {
            protocol   = SoaConstants.HTTP;
        }
        else if ( host.startsWith("tccs") )
        {
            protocol   = SoaConstants.TCCS;
            host = host.trim();
            int envNameStart = host.indexOf('/') + 2;
            envNameTccs = host.substring( envNameStart, host.length() );
            host = "";
        }
        else
        {
            protocol   = SoaConstants.IIOP;
        }
        // Create the Connection object, no contact is made with the server until a service request is made
        connection = new Connection(host, credentialManager, SoaConstants.REST, protocol);
        if( protocol == SoaConstants.TCCS )
        {
           connection.setOption(  Connection.TCCS_ENV_NAME, envNameTccs );
        }
        // Add an ExceptionHandler to the Connection, this will handle any
        // InternalServerException, communication errors, XML marshaling errors
        // .etc
        connection.setExceptionHandler(new AppXExceptionHandler());

        // While the above ExceptionHandler is required, all of the following
        // Listeners are optional. Client application can add as many or as few Listeners
        // of each type that they want.

        // Add a Partial Error Listener, this will be notified when ever a
        // a service returns partial errors.
        connection.getModelManager().addPartialErrorListener(new AppXPartialErrorListener());

        // Add a Change and Delete Listener, this will be notified when ever a
        // a service returns model objects that have been updated or deleted.
        connection.getModelManager().addModelEventListener(new AppXModelEventListener());


        // Add a Request Listener, this will be notified before and after each
        // service request is sent to the server.
        Connection.addRequestListener( new AppXRequestListener() );

    }
  
    /**
     * Get the single Connection object for the application
     *
     * @return  connection
     */
    public static Connection getConnection()
    {
    	if(connection==null){
    		PropertiesUtil prop = new PropertiesUtil();
            String user_name = prop.getProperyValue("user_name").toString();
            String pass_word = prop.getProperyValue("pass_word").toString();
            String group_name = prop.getProperyValue("group_name").toString();
    		try {
    			String sessionId = String.valueOf(System.currentTimeMillis());
				new TcSession().login(user_name, pass_word, group_name, "", sessionId);
				logger.warn("Login Teamcenter successfully!");
			} catch (InvalidCredentialsException e) {
				logger.warn("Login Teamcenter Error!,The Error message is :"+e.getMessage());
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				logger.warn("Login Teamcenter Error!,The Error message is :"+e.getMessage());
			}
    	}
        return connection;
    }

    public User login(String name, String psw, String group, String role, String sessionDiscriminator)throws InvalidCredentialsException, ServiceException
    {
        SessionService sessionService = SessionService.getService(connection);
        try
        {
            LoginResponse out = sessionService.login(name,psw,group, role,LanguageEnv_CN, sessionDiscriminator);
            user = out.user;
            StateNameValue[] properties = new StateNameValue[1];
            properties[0] = new StateNameValue();
            properties[0].name="bypassFlag";
            properties[0].value=Property.toBooleanString(true);
            sessionService.setUserSessionState(properties);
            sessionService.refreshPOMCachePerRequest(true);
            return out.user;
        }
        catch (InvalidCredentialsException e) {
        	logger.warn("用户登录失败，可能由于用户名或密码不正确");
            throw new InvalidCredentialsException();
        }
        catch (RuntimeException re)
        {
        	logger.warn("用户登录失败，可能由于用户名或密码不正确");
           throw new ServiceException(re.getMessage());
        }
    }
    /**
     * Terminate the session with the Teamcenter Server
     *
     */
    public static void logout()
    {
        SessionService sessionService = SessionService.getService(connection);
        try
        {
            sessionService.logout();
        }
        catch (ServiceException e){}
    }
}
