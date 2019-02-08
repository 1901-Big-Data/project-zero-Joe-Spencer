import java.util.*;
import java.sql.Connection;

import java.sql.SQLException;

import util.ConnectionUtil;

public class Application {
	private static Scanner s;

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		boolean running = true;
		s = new Scanner(System.in);
		Connection con = ConnectionUtil.getConnection();
		while(running) {
			System.out.println("Do you have an account with us? (Y/N)");
			String next = s.nextLine();
			if(next.equalsIgnoreCase("y")) {
				System.out.println("Enter username:");
				String username = s.nextLine();
				//int userID = userDAO.getUserID(username);
				//System.out.println(userID);
				System.out.println("Enter password for "+ username);
				String password = s.nextLine();
				System.out.println(password);
			}
			else if(next.equalsIgnoreCase("y")) {
				System.out.println("Do you like to create an account with us? (Y/N)");
				next = s.nextLine();
				if(next.equalsIgnoreCase("y")){
					
				}
			}
			
		}

	}

}
