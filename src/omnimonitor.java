
import com.digitaldan.jomnilinkII.*;
import com.digitaldan.jomnilinkII.MessageTypes.*;
import com.digitaldan.jomnilinkII.MessageTypes.properties.AreaProperties;
import com.digitaldan.jomnilinkII.MessageTypes.statuses.*;
import com.digitaldan.jomnilinkII.MessageTypes.events.*;
import com.digitaldan.jomnilinkII.MessageTypes.events.OtherEvent.EventType;

//import com.digitaldan.jomnilinkII.MessageTypes.
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;


public class omnimonitor {
	

	public static String toaddress = "";
	public static String to2address = "";
	public static String to3address = "";
	public static String fromaddress = "";
	public static String username = "";
	public static String password = "";
	public static String host  = "";
	public static String key = "";
	public static String zonestoalert = "";
	public static String totwitter = "";
	
	static final int dcMessage = 0x2;
	private static int debug_channels;
	
	public static int port = Integer.parseInt("4369");
	
	public static void main(String[] args) {
		
		SendMessage.test();
		//CommTest.test();
		
		System.out.println("Start"); 
		//Connect.connect();
		System.out.println("Connected to DB");
		//Connect.getconfig(1);
		System.out.println("Got Config");
		
		
		//startlistener();
		

	}

	public static void startlistener() {
		try{
			
			Message m;
			final Connection c = new Connection(host,port,key);
			
			//c.debug = true;
			c.addNotificationListener(new NotificationListener(){
				public void objectStausNotification(ObjectStatus s) {
							switch (s.getStatusType()) {
							case Message.OBJ_TYPE_AREA:
								//System.out.println("STATUS_AREA changed");
								//conduit.mail(s.toString());
								AreaStatus areastatus[] = (AreaStatus[])s.getStatuses();
								//System.out.println(areastatus[0].getAlarms());
								System.out.println(s.statusString()); 
								//conduit.mail(areastatus[0].getAlarms() + " " + areastatus[0].getMode() + " " + areastatus[0].getEntryTimer() + " " + s.toString());
								alarmstatus(areastatus[0].getNumber(),areastatus[0].getMode(),areastatus[0].getAlarms(),areastatus[0].getEntryTimer(),areastatus[0].getExitTimer());
								break;
							case Message.OBJ_TYPE_AUDIO_ZONE:
								//System.out.println("STATUS_AUDIO_ZONE changed");
								break;
							case Message.OBJ_TYPE_AUX_SENSOR:
								//System.out.println("STATUS_AUX changed");
								break;
							case Message.OBJ_TYPE_EXP:
								//System.out.println("STATUS_EXP changed");
								break;
							case Message.OBJ_TYPE_MESG:
								//System.out.println("STATUS_MESG changed");

								break;
							case Message.OBJ_TYPE_BUTTON:
								System.out.println("OBJ_TYPE_BUTTON changed");
								
								
								break;
							case Message.OBJ_TYPE_THERMO:
								//System.out.println("STATUS_THERMO changed");
								//ThermostatStatus [] thermos = (ThermostatStatus[])s.getStatuses(); 
								//for(int i=0;i<thermos.length;i++){
								//	System.out.println(thermos[i].toString());
								//	System.out.println(MessageUtils.omniToF(thermos[i].getTemperature()));
								//}
								break;
							case Message.OBJ_TYPE_UNIT:
								//System.out.println("STATUS_UNIT changed");
								break;
							case Message.OBJ_TYPE_ZONE:
								//System.out.println("STATUS_ZONE changed!");
								ZoneStatus zonestatus[] = (ZoneStatus[])s.getStatuses();
								processzonestatus(zonestatus[0],c);

								break;
							default:
								System.out.println("Unknown type " + s.getStatusType());
							break;
							}
						//System.out.println(s.toString());
						//conduit.mail(s.toString());
					}
				@Override
				public void otherEventNotification(OtherEventNotifications o) {
					System.out.println("Other Event");
					for(int k=0;k<o.Count();k++){
						
						otherEventReceive(o.getNotification(k));
						//System.out.println(o.getNotification(k).toString() + "   " + o.getMessageType() + "    " + EventType.UserMacroButton);
						
						//System.out.println( OtherEventNotifications.typeOfMessage(o.getMessageType()) );
						
						//System.out.println( OtherEventNotifications.typeOfMessage(k) );
						
						//System.out.println( o.getNotification(k) );
						
						//switch (OtherEventNotifications.typeOfMessage(o.getMessageType())) {

						
						//if( o.getNotification(k) == OtherEvent.EventType.UserMacroButton){
						//case EventType.UserMacroButton:
							//System.out.println( "in the case" );
							//UserMacroButtonEvent button = (UserMacroButtonEvent)o.getNotification(k);
							
							//System.out.println(button.getButtonNumber());
							//Connect.findbutton(button.getButtonNumber());
							
						
						//}
					}
					
				}
			});
			
			c.addDisconnectListener(new DisconnectListener(){
				@Override
				public void notConnectedEvent(Exception e) {
					System.out.println("OmniConnection lost connection trying to reconnect");
					startlistener();
					//e.printStackTrace();
					//System.exit(-1);
				}
			});
//			c.debug = true;
			c.enableNotifications();
			c.autoPingOmni();
			//System.out.println("name " + c.receiveName(Message.OBJ_TYPE_ZONE, 9));
			//System.out.println(c.getName());
			//AreaProperties testarea = (AreaProperties) c.reqObjectProperties(Message.OBJ_TYPE_AREA, 0, 1, ObjectProperties.FILTER_1_NAMED_UNAMED, ObjectProperties.FILTER_2_NONE, ObjectProperties.FILTER_3_NONE);
			//System.out.println(testarea.getMode());
			System.out.println("Print Info");
			System.out.println(c.reqSystemInformation().toString());
			System.out.println(c.reqSystemStatus().toString());
			//System.out.println("Trouble Count " + c.reqSystemTroubles().getTroubleCount());
			System.out.println(c.reqSystemFormats().toString());
			//System.out.println(c.reqSystemFeatures().toString());
			
			System.out.println("All Done, OmniConnection thread now running");
			
		} catch (OmniInvalidResponseException e){
			e.printStackTrace();
			System.out.println("Message:" + e.getInvalidResponse().getMessageType());
			System.exit(-1);
		} catch (OmniUnknownMessageTypeException e) {
			e.printStackTrace();
			System.out.println("Message:" + e);
			System.exit(-1);
		} catch (OmniNotConnectedException e){
			e.printStackTrace();
			System.out.println("Message:" + e.getNotConnectedReason());
			System.exit(-1);
		} catch (NoRouteToHostException e){
			System.out.println("OmniConnection lost connection and noroutetohost trying to reconnect" + e.toString());
			startlistener();
		} catch (ConnectException e){
			System.out.println("OmniConnection lost connection and ConnectException trying to reconnect" + e.toString());
			startlistener();
		} catch (Exception e){
			e.printStackTrace();
			System.exit(-1);
		}

	}
	

	public static void alarmstatus(int number, int mode, int alarms, int entryTimer, int exitTimer) {
		
		Connect.getemailconfig(1);
		Connect.gettwitterconfig(1);
		if (alarms > 0) {
			Connect.findalarmalert(alarms, number, SFunctions.getmodetype(mode));
		} else {
			if (exitTimer == 0 && entryTimer == 0){
				Connect.findmodealert(mode, number, SFunctions.getHour());
			}
		}

		
	}
	public static boolean getDebugChan( int channels ) {
        return (debug_channels & channels) == channels;
	}
	
    public static void otherEventReceive( OtherEvent event) {
        if (getDebugChan(dcMessage))  System.out.println(event.toString());  
        
        switch (event.getEventType()) {
                case UserMacroButton: {
                	System.out.println("user macro button");  
                        //OmniButton ob = buttons.get(((UserMacroButtonEvent)event).getButtonNumber());
                        //if (ob != null) {
                        //        ob.notifyPress();
                        //}
                	UserMacroButtonEvent button = ((UserMacroButtonEvent)event);
					
					System.out.println(button.getButtonNumber());
					Connect.findbutton(button.getButtonNumber());
                }
                case ProlinkMessage:
                case CentraliteSwitch:
                case Alarm:
                case ComposeCode:
                case X10Code:
                case SecurityArming:
                case LumniaModeChange:
                case UnitSwitchPress:
                case UPBLink:
                case AllSwitch: {
                	System.out.println("all switch");  
                }
                case PhoneLine:
                case Power:
                case DCM:
                case EnergyCost:
                break;
        }
    }
    
	public static void processzonestatus(ZoneStatus zonestatus, Connection c) {
		
		System.out.println("process zone status");
		int objnum = zonestatus.getNumber();
		//Message m2;
		String name = "";

		
		try {
			//m2 = c.reqObjectProperties(Message.OBJ_TYPE_ZONE, objnum, 0, ObjectProperties.FILTER_1_NAMED, ObjectProperties.FILTER_2_AREA_ALL, ObjectProperties.FILTER_3_ANY_LOAD);
			//name = ((ObjectProperties)m2).getName();
			NameData m3;
			m3 = (NameData)c.receiveName(Message.OBJ_TYPE_ZONE, (objnum - 1));
			name = m3.getName();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OmniNotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OmniInvalidResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OmniUnknownMessageTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//conduit.mail("Zone status " + name + " " + zonestatus[0].getNumber() + " " + zonestatus[0].getStatus());

		ObjectStatus status2;
		int objstatus=0;
		try {
			status2 = c.reqObjectStatus(Message.OBJ_TYPE_AREA,1,1);
			AreaStatus [] areas = (AreaStatus[])status2.getStatuses();
			objstatus= areas[0].getMode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OmniNotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OmniInvalidResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OmniUnknownMessageTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (zonestatus.getStatus() == 1) {
			try {
				Connect.findzonealert(zonestatus.getNumber(),objstatus,zonestatus.getStatus(), name );
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println(" " + SFunctions.getcurrentTime() +  " Zone status " + name + " " + zonestatus.getNumber() + " " + zonestatus.getStatus());
		//System.out.println(s.statusString());

	}	
}
