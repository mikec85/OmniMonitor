import java.io.IOException;
import java.sql.*;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.digitaldan.jomnilinkII.Message;
import com.digitaldan.jomnilinkII.OmniInvalidResponseException;
import com.digitaldan.jomnilinkII.OmniNotConnectedException;
import com.digitaldan.jomnilinkII.OmniUnknownMessageTypeException;
import com.digitaldan.jomnilinkII.MessageTypes.ObjectStatus;
import com.digitaldan.jomnilinkII.MessageTypes.statuses.AreaStatus;

   public class Connect
   {
	   public static Connection conn = null;
	   
       public static void connect ()
       {
           //Connection conn = null;

           try
           {
               String userName = "root";
               String password = "root";
               String url = "jdbc:mysql://127.0.0.1/omni";
               //String url = "jdbc:mysql://192.168.1.36/omni";
               Class.forName ("com.mysql.jdbc.Driver").newInstance ();
               conn = DriverManager.getConnection (url, userName, password);
               System.out.println ("Database connection established");
           }
           catch (Exception e)
           {
        	   System.err.println(e.toString());
               System.err.println ("Cannot connect to database server");
           }
           finally
           {
               if (conn != null)
               {
                   try
                   {
                       //conn.close ();
                       //System.out.println ("Database connection terminated");
                   }
                   catch (Exception e) { /* ignore close errors */ }
               }
           }
       }
       public static void put(int zone, int status, String txt) {
    	   int retries = 2;
    	   
    	   while(retries > 0) {
			   try
			   {
			       Statement s = conn.createStatement ();
			       s.executeUpdate ("insert into log (time,zone,status,text) values (NOW(),'" + zone + "','" + status + "','"+ txt +"');"); // issue invalid query
			       s.close ();
			       break;
			   }
			   catch (SQLException e)
			   {
			       System.err.println ("Error message: " + e.getMessage ());
			       System.err.println ("Error number: " + e.getErrorCode ());
			       try {
					if ( conn.isClosed() ) {
						   retries = retries - 1;
					       Connect.connect();
					   }else {
					       break;
					   }
				   } catch (SQLException e1) {
					// TODO Auto-generated catch block
				       System.err.println ("Error message: " + e1.getMessage ());
				       System.err.println ("Error number: " + e1.getErrorCode ());
				   }
			   } 
    	   }
		   
       }
       
       public static void getconfig(int id){
    	   try {
    	   String query = "SELECT * FROM omni_system where id = " + id;

    	      // create the java statement
    	      Statement st = conn.createStatement();
    	      
    	      // execute the query, and get a java resultset
    	      ResultSet rs = st.executeQuery(query);
    	      
    	      // iterate through the java resultset
    	      while (rs.next())
    	      {
    	        String name = rs.getString("name");
    	        String ip = rs.getString("ip");
    	        String key = rs.getString("key");
    	        int port = rs.getInt("port");
    	        
    	        omnimonitor.host = ip;
    	        omnimonitor.key = key;
    	        
    	        // print the results
    	        //System.out.format("%s, %s, %s, %s, %s\n", id, name, ip, key, port);
    	      }
    	      st.close();
    	    }
    	    catch (SQLException e)
    	    {
    	      System.err.println("Got an exception! ");
    	      System.err.println(e.getMessage());
    	    }
    	   
    	   
       }
       public static void getemailconfig(int id){
    	   try {
    	   String query = "SELECT * FROM email_settings";

    	      // create the java statement
    	      Statement st = conn.createStatement();
    	      
    	      // execute the query, and get a java resultset
    	      ResultSet rs = st.executeQuery(query);
    	      
    	      // iterate through the java resultset
    	      while (rs.next())
    	      {
    	    	  
    	        omnimonitor.username = rs.getString("username");
    	    	omnimonitor.fromaddress = rs.getString("from");
    	    	omnimonitor.username = rs.getString("username");
    	    	omnimonitor.password = rs.getString("password");
    	    	conduit.mailhost = rs.getString("mailhost");
    	    	conduit.mailport = rs.getString("port");
    	    	conduit.mailauth = rs.getInt("auth");
    	    	conduit.mailencrypt = rs.getInt("encryption");
    	    	
   	        
    	        // print the results
    	        //System.out.format("%s, %s, %s, %s, %s\n", id, name, ip, key, port);
    	      }
    	      st.close();
    	    }
    	    catch (SQLException e)
    	    {
    	      System.err.println("Got an exception! ");
    	      System.err.println(e.getMessage());
    	    }
    	   
    	   
       }
       public static void gettwitterconfig(int id){
    	   try {
    	   String query = "SELECT * FROM twitter_settings";

    	      // create the java statement
    	      Statement st = conn.createStatement();
    	      
    	      // execute the query, and get a java resultset
    	      ResultSet rs = st.executeQuery(query);
    	      
    	      // iterate through the java resultset
    	      while (rs.next())
    	      {
    	    	  
    	        conduit.conkey = rs.getString("ConKey");
    	        conduit.consecret = rs.getString("ConSecret");
    	        conduit.accesstoken = rs.getString("AccessToken");
    	        conduit.accesstokensec = rs.getString("AccessTokenSec");
    	        
    	        
    	        // print the results
    	        //System.out.format("%s, %s, %s, %s, %s\n", id, name, ip, key, port);
    	      }
    	      st.close();
    	    }
    	    catch (SQLException e)
    	    {
    	      System.err.println("Got an exception! ");
    	      System.err.println(e.getMessage());
    	    }
    	   
    	   
       }
       
       public static void findzonealert(int zone, int areastatus, int status, String name) throws SQLException{
    	   int retries = 2;
    	   System.out.println("findzonealert");
    	   while(true) {
    		   System.out.println("findzone while");
	    	   try {
	    		   Connect.getemailconfig(1);
	    		   String query = "SELECT * FROM notification where zones like '%," + zone + ",%' and modealert=" + areastatus;
	
	    	      // create the java statement
	    	      Statement st = conn.createStatement();
	    	      
	    	      // execute the query, and get a java resultset
	    	      ResultSet rs = st.executeQuery(query);
	    	      
	    	      // iterate through the java resultset
	    	      while (rs.next())
	    	      {
	    	    	  
	    	        String contact = rs.getString("contact");
	    	        int contacttype = rs.getInt("contacttype");
	    	        
					if (contacttype == 1){
						try {
							conduit.mail("Zone "+zone+" "+name+" AlarmMode is "+SFunctions.getmodetype(status),contact,omnimonitor.fromaddress,omnimonitor.username,omnimonitor.password);
						} catch (AddressException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (MessagingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if (contacttype == 2){
						conduit.tweetdm("Zone "+zone+" "+name+" AlarmMode is "+SFunctions.getmodetype(status),contact);
					}
	    	        
	    	        // print the results
	    	        //System.out.format("%s, %s, %s, %s, %s\n", id, name, ip, key, port);
	    	      }
	    	      st.close();
	    	      break;
	    	    }
	    	    catch (SQLException e)
	    	    {
	    	      System.err.println("Got an exception! ");
	    	      System.err.println(e.getMessage());
			       if ( conn.isClosed() ) {
			    	   retries = retries - 1;
				       Connect.connect();
				       put(0,0,"db reconnect");
				   }else {
				       break;
				   }
			       
	    	    }
    	   }
    	   
       }
       public static void findalarmalert(int alarms, int area, String mode){
    	   try {
    		   Connect.getemailconfig(1);
    		   String query = "SELECT * FROM alarmnotification where alarmtype=" + alarms;

    	      // create the java statement
    	      Statement st = conn.createStatement();
    	      
    	      // execute the query, and get a java resultset
    	      ResultSet rs = st.executeQuery(query);
    	      
    	      // iterate through the java resultset
    	      while (rs.next())
    	      {
    	    	  
    	        String contact = rs.getString("contact");
    	        int contacttype = rs.getInt("contacttype");
    	        
				if (contacttype == 1){
					try {
						conduit.mail(SFunctions.getalarmtype(alarms)+" for Area "+area+" Alarm was in mode " + mode,contact,omnimonitor.fromaddress,omnimonitor.username,omnimonitor.password);
					} catch (AddressException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (contacttype == 2){
					conduit.tweetdm(SFunctions.getalarmtype(alarms)+" for Area "+area+" Alarm was in mode " + mode,contact);
				}
    	        
    	        // print the results
    	        //System.out.format("%s, %s, %s, %s, %s\n", id, name, ip, key, port);
    	      }
    	      st.close();
    	    }
    	    catch (SQLException e)
    	    {
    	      System.err.println("Got an exception! ");
    	      System.err.println(e.getMessage());
    	    }
    	   
    	   
       }
       
       public static void findmodealert(int mode, int area, int hour){
    	   try {
    		   Connect.getemailconfig(1);
    		   String query = "SELECT * FROM alertnotification where modealert=" + mode+" and time1 < "+hour+" and time2 > "+hour;

    	      // create the java statement
    	      Statement st = conn.createStatement();
    	      
    	      // execute the query, and get a java resultset
    	      ResultSet rs = st.executeQuery(query);
    	      
    	      // iterate through the java resultset
    	      while (rs.next())
    	      {
    	    	  
    	        String contact = rs.getString("contact");
    	        int contacttype = rs.getInt("contacttype");
    	        
				if (contacttype == 1){
					try {
						conduit.mail("Area "+area+" Alarm now in mode "+SFunctions.getmodetype(mode),contact,omnimonitor.fromaddress,omnimonitor.username,omnimonitor.password);
					} catch (AddressException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (contacttype == 2){
					conduit.tweetdm("Area "+area+" Alarm now in mode "+SFunctions.getmodetype(mode),contact);
				}
    	        
    	        // print the results
    	        //System.out.format("%s, %s, %s, %s, %s\n", id, name, ip, key, port);
    	      }
    	      st.close();
    	    }
    	    catch (SQLException e)
    	    {
    	      System.err.println("Got an exception! ");
    	      System.err.println(e.getMessage());
    	    }
    	   
    	   
       }
       
       public static void findbutton(int button){
    	   try {
    		   Connect.getemailconfig(1);
    		   String query = "SELECT * FROM macrobutton where button=" + button;

    	      // create the java statement
    	      Statement st = conn.createStatement();
    	      
    	      // execute the query, and get a java resultset
    	      ResultSet rs = st.executeQuery(query);
    	      
    	      // iterate through the java resultset
    	      while (rs.next())
    	      {
    	    	  
    	    	int cmd = rs.getInt("cmd");

    	    	if (cmd == 1){
					Connect.wakeonlan(button);
			    }

    	        
    	        // print the results
    	        //System.out.format("%s, %s, %s, %s, %s\n", id, name, ip, key, port);
    	      }
    	      st.close();
    	    }
    	    catch (SQLException e)
    	    {
    	      System.err.println("Got an exception! ");
    	      System.err.println(e.getMessage());
    	    }
    	   
    	   
       }
       
       public static void wakeonlan(int button){
    	   try {
    		   Connect.getemailconfig(1);
    		   String query = "SELECT * FROM wakeonlan where button=" + button;

    	      // create the java statement
    	      Statement st = conn.createStatement();
    	      
    	      // execute the query, and get a java resultset
    	      ResultSet rs = st.executeQuery(query);
    	      
    	      // iterate through the java resultset
    	      while (rs.next())
    	      {
    	    	  
      	    	String ipbcast = rs.getString("ipbcast");
      	    	String mac = rs.getString("mac");
      	    	
  				WakeOnLan.wake(ipbcast,mac);
  				
    	      }
    	      st.close();
    	    }
    	    catch (SQLException e)
    	    {
    	      System.err.println("Got an exception! ");
    	      System.err.println(e.getMessage());
    	    }
    	   
    	   
       }
   }