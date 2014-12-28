import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SFunctions {

    public static int getHour() {
        DateFormat dateFormat = new SimpleDateFormat("HH");
        Date date = new Date();
        return Integer.parseInt(dateFormat.format(date));
    }
    
    public static String getcurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss MM.dd.yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }
    
	public static String getalarmtype(int alarms){
		String alarmtype = "";
		switch (alarms) {
		case 0: {
			alarmtype = "Off";
			break;
		}
		case 1: {
			alarmtype = "Burglary alarm";
			break;
		}
		case 2: {
			alarmtype = "Fire alarm";
			break;
		}
		case 3: {
			alarmtype = "Gas alarm";
			break;
		}
		case 4: {
			alarmtype = "Auxiliary alarm";
			break;
		}
		case 5: {
			alarmtype = "Freeze alarm";
			break;
		}
		case 6: {
			alarmtype = "Water alarm";
			break;
		}
		case 7: {
			alarmtype = "Duress alarm";
			break;
		}
		case 8: {
			alarmtype = "Temperature alarm";
			break;
		}
		default:
			
	    break;
		}
		return alarmtype;
	}
	public static String getmodetype(int mode){
		String modetype ="";
		switch (mode) {
		case 0: {
			modetype = "Off";
			break;
		}
		case 1: {
			modetype = "Day";
			break;
		}
		case 2: {
			modetype = "Night";
			break;
		}
		case 3: {
			modetype = "Away";
			break;
		}
		case 4: {
			modetype = "Vacation";
			break;
		}
		case 5: {
			modetype = "Day instant";
			break;
		}
		default:
			
		break;
		}
		return modetype;
	}
}
