import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import twitter4j.DirectMessage;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class conduit {
	
	public static String conkey;
	public static String consecret;
	public static String accesstoken;
	public static String accesstokensec;
	public static String mailhost;
	public static String mailport;
	public static int mailauth;
	public static int mailencrypt;
	
	public static void mail1(String statusmessage,String toaddress,String fromaddress,final String username,final String password){
		Properties props = new Properties();
		props.put("mail.smtp.host", mailhost);
		if(mailencrypt == 1) {
			props.put("mail.smtp.socketFactory.port", mailport);
			props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		}
		if(mailauth == 1) {
		   	props.put("mail.smtp.auth", "true");
		} else {
			props.put("mail.smtp.auth", "false");
		}
		props.put("mail.smtp.port", mailport);
 
		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username,password);
				}
			});
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromaddress));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toaddress));
			message.setSubject("");
			message.setText(statusmessage);
 
			Transport.send(message);
 
			//System.out.println("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void gmail(String statusmessage,String toaddress,final String fromaddress,final String username,final String password){

		System.out.println("from: " + fromaddress + "  ,  password  " + password);
		try {
	        
	        Properties props = System.getProperties();
	        props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.smtp.host", mailhost);
	        props.put("mail.smtp.user", fromaddress);
	        props.put("mail.smtp.password", password);
	        props.put("mail.smtp.port", "587");
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.debug", "true");

	        //Session session = Session.getDefaultInstance(props, null);
	        //Session session = Session.getInstance(props, new GMailAuthenticator(username, password));
	        //create Authenticator object to pass in Session.getInstance argument
	        Authenticator auth = new Authenticator() {
	            //override the getPasswordAuthentication method
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(fromaddress, password);
	            }
	        };
	        Session session = Session.getInstance(props, auth);
	        
	        
	        sendEmail(session, "mike@coffeys.net","TLSEmail Testing Subject", "TLSEmail Testing Body");
	        
	        /**
	        MimeMessage message = new MimeMessage(session);
	        Address fromAddress = new InternetAddress(fromaddress);
	        Address toAddress = new InternetAddress(toaddress);

	        message.setFrom(fromAddress);
	        message.setRecipient(Message.RecipientType.TO, toAddress);

	        message.setSubject("Testing JavaMail");
	        message.setText("Welcome to JavaMail");
	        Transport transport = session.getTransport("smtp");
	        transport.connect(mailhost, fromaddress, password);
	        message.saveChanges();
	        Transport.send(message);
	        transport.close();
	        */

	    }catch(Exception ex){

	        System.out.println("ERROR: " + ex);
	    }
	}
	
    public static void sendEmail(Session session, String toEmail, String subject, String body){
        try
        {
          MimeMessage msg = new MimeMessage(session);
          //set message headers
          msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
          msg.addHeader("format", "flowed");
          msg.addHeader("Content-Transfer-Encoding", "8bit");
 
          msg.setFrom(new InternetAddress("no_reply@journaldev.com", "NoReply-JD"));
 
          msg.setReplyTo(InternetAddress.parse("no_reply@journaldev.com", false));
 
          msg.setSubject(subject, "UTF-8");
 
          msg.setText(body, "UTF-8");
 
          msg.setSentDate(new Date());
 
          msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
          System.out.println("Message is ready");
          Transport.send(msg);  
 
          System.out.println("EMail Sent Successfully!!");
        }
        catch (Exception e) {
          e.printStackTrace();
        }
    }
	
    public static void mail(String statusmessage,String toaddress,String fromaddress,final String username,final String password) throws AddressException, MessagingException {
    	 
    	System.out.println("from: " + fromaddress + "  ,  password  " + password + "  to: "+ toaddress);
    	
    	Properties mailServerProperties;
        Session getMailSession;
        MimeMessage generateMailMessage;
        
    	//Step1     
    	        System.out.println("\n 1st ===> setup Mail Server Properties..");
    	        mailServerProperties = System.getProperties();
    	        mailServerProperties.put("mail.smtp.port", "587"); // TLS Port
    	        mailServerProperties.put("mail.smtp.auth", "true"); // Enable Authentication
    	        mailServerProperties.put("mail.smtp.starttls.enable", "true"); // Enable StartTLS
    	        System.out.println("Mail Server Properties have been setup successfully..");
    	 
    	//Step2     
    	        System.out.println("\n\n 2nd ===> get Mail Session..");
    	        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
    	        generateMailMessage = new MimeMessage(getMailSession);
    	        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toaddress));
    	        //generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("test1@crunchify.com"));
    	        generateMailMessage.setSubject("");
    	        
    	        generateMailMessage.setContent(statusmessage, "text/html");
    	        System.out.println("Mail Session has been created successfully..");
    	 
    	//Step3     
    	        System.out.println("\n\n 3rd ===> Get Session and Send mail");
    	        Transport transport = getMailSession.getTransport("smtp");
    	        // Enter your correct gmail UserID and Password
    	        transport.connect("smtp.gmail.com", username, password);
    	        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
    	        transport.close();
    	        System.out.println("Done");
    }
    
	public static void tweetdm(String msg, String recipientId){
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey(conkey)
		  .setOAuthConsumerSecret(consecret)
		  .setOAuthAccessToken(accesstoken)
		  .setOAuthAccessTokenSecret(accesstokensec);
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		
	    // The factory instance is re-useable and thread safe.
	    //Twitter sender = new TwitterFactory().getInstance();
	    DirectMessage message;
		try {
			message = twitter.sendDirectMessage(recipientId, msg);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.out.println("Sent: " + msg + " to @" + recipientId);
	   
	}
	
}
