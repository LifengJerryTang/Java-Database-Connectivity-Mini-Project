import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;

/**
 * Name: Lifeng Tang
 * Date: 6/25/2018
 */
public class DataBaseProject1 {

	static int hour_count;
	
	public static void main(String[] args) {
	
		Scanner input = new Scanner(System.in);
		String url = "jdbc:mysql://localhost:3306/lab__information";  //Use your own MySQL database url
		String user_ID = "wwqwe32", password = "kktt12345"; //User needs to use their own username and password
	    double total_time_Spent = 0.0;
	    int num_Participants = 0, num_Time_Periods;
	    DecimalFormat df = new DecimalFormat("#.00");
	    String[] time_Periods;
	    int[] time_Periods_Count;
	    
	    System.out.println("Please Enter the number of time periods during a day that your project has: ");
	    num_Time_Periods = input.nextInt(); //Allows the user to enter the number of time periods wanted
	    time_Periods = new String[num_Time_Periods];
	    time_Periods_Count = new int[num_Time_Periods];
	    System.out.println("Please enter your time periods: ");
	    for(int i = 0; i < num_Time_Periods; i++) time_Periods[i] = input.next();
	    //Allows the user to enter the time periods
		
		try{
			
			Connection con = DriverManager.getConnection(url, user_ID, password); 
			//Connection object that will connect Eclipse to an MySQL connection
	
			System.out.println("Connection success");
			Statement stmt = con.createStatement();  //A statement is created to execute an query
			
		    String sql = "insert into lab_info " 
					    + " (Participant_Name, User_Name, Time_Started, Time_Ended)"
					    + " values ('Name', 'UserName', 'TimeStarted', 'TimeFinished')";	
		    			//User of the program needs to update the information each time when 
		    			//executing the program.
		
			stmt.execute(sql);
			ResultSet MyRs = stmt.executeQuery("SELECT * FROM lab_info");
			System.out.println("Insert Complete");
		 
			System.out.println("\nParticipants: \t" + "User Name: \t" + "Time Started: \t" + "Time Ended:" );
		
		while(MyRs.next()) {
				
				System.out.println("*************************************************************************");
				System.out.println(MyRs.getString("Participant_Name") + "\t" + MyRs.getString("User_Name")
								+ "\t\t" + MyRs.getString("Time_Started") + "\t\t" 
								  + MyRs.getString("Time_Ended"));
			
				num_Participants++;
				int time_Start_Hour = Integer.parseInt(MyRs.getString("Time_Started").substring(0, 
									 MyRs.getString("Time_Started").indexOf(':') ));
				int time_Start_Min = Integer.parseInt(MyRs.getString("Time_Started").substring( 
						              MyRs.getString("Time_Started").indexOf(':') + 1));
				int time_End_Hour = Integer.parseInt(MyRs.getString("Time_Ended").substring(0, 
									MyRs.getString("Time_Ended").indexOf(':') ));
				int time_End_Min = Integer.parseInt(MyRs.getString("Time_Ended").substring( 
									MyRs.getString("Time_Ended").indexOf(':') + 1));
				
				for(int x = 0; x < num_Time_Periods; x++){

					int time_Period_Start_Hour = Integer.parseInt(time_Periods[x].substring(0, 
							time_Periods[x].indexOf(':')));
					int time_Period_End_Hour = Integer.parseInt(time_Periods[x].substring( 
			                   time_Periods[x].indexOf('-') + 1, 
			                   time_Periods[x].lastIndexOf(':')));
				
				    if(time_Start_Hour >= time_Period_Start_Hour 
				       && time_End_Hour <= time_Period_End_Hour){			  
				    	  
				    	 time_Periods_Count[x]++;
					
				    }
				}

				if(time_End_Min < time_Start_Min){ //Makes time subtraction correct
									
					time_End_Min += 60;
					time_End_Hour--;
					
				}
				
				total_time_Spent += Double.parseDouble(time_End_Hour + "." + time_End_Min);
				total_time_Spent -= Double.parseDouble(time_Start_Hour + "." + time_Start_Min);		
				total_time_Spent = Time_Correction(total_time_Spent);
			 
		    }
			
			String total_Time_Spent = df.format(total_time_Spent);
			int indexOfPeriod = total_Time_Spent.indexOf('.');
			int total_Time_Hour = Integer.parseInt(total_Time_Spent.substring(0, indexOfPeriod));
			int total_Time_Minutes = Integer.parseInt(total_Time_Spent.substring(indexOfPeriod + 1));
			
			System.out.println("\nTotal Time Spent from all particiapnts: " + 
							total_Time_Hour +" Hours and " + total_Time_Minutes + " Minutes.");
			System.out.println("Number of Participants: " + num_Participants);
			
			total_time_Spent = total_Time_Hour * 60 + total_Time_Minutes;
			
			double average_time = total_time_Spent/num_Participants;		
			average_time = Average_Time_Spent_Correction(average_time);
			
			System.out.println("Average Time Spent from all participants: " + 
								hour_count + " Hours and "+ average_time + " Minutes\n");
			
			System.out.println("Time Periods\t#People Working");
			for(int j = 0; j < num_Time_Periods; j++){
				
				System.out.println("*************************************************************************");
				System.out.println( time_Periods[j] + "\t " + time_Periods_Count[j]);
				
			}
		
		   } catch(Exception ex) {
			ex.printStackTrace();

		}
	}
	
	public static double Time_Correction(double Time){
		
		if(Time % 1 > 0.58){
			
			Time += 1.0;
			Time -= 0.6;
			
		}
		
		return Time;
				
	}
	
	public static int Average_Time_Spent_Correction(double Avg_Time){ 
		
		while(Avg_Time > 60.0){
			
			Avg_Time -= 60.0;
			hour_count++;
			
		}
		
		return (int) Avg_Time;	
		
	}
}
