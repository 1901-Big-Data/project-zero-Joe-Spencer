import java.util.*;

import dao.accountDAO;
import dao.userDAO;
import models.accountModel;
import models.userModel;

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
			System.out.println("Are you a registered user with us? (Y/N)");
			String next = s.nextLine();
			if(next.equalsIgnoreCase("y")) {
				System.out.println("Enter username:");
				String username = s.nextLine();
				System.out.println("Enter password for "+ username);
				String password = s.nextLine();
				Optional<userModel> user = userDAO.getUser(username, password, con);
				userModel u = user.get();
				if(user.isPresent()) {
					Optional<ArrayList<accountModel>> accounts =accountDAO.getAccounts(u.getID(), con);
					if(accounts.isPresent()) {
						ArrayList<accountModel> accountModels =accounts.get();
						System.out.println("Which account would you like to access?");
						for(int i =0; i<accountModels.size();i++) {
							System.out.println((i+1)+". "+accountModels.get(i).getName());
						}
						int selection = s.nextInt();
						accountModel thisAccount =accountModels.get(selection-1);
						System.out.println("Account balance is "+thisAccount.getBalance());
						System.out.println("What would you like to do? \n1. Withdraw \n2. Deposit \n3. delete \n4. nothing");
						selection = s.nextInt();
						if(selection == 1) {
							System.out.println("How much would you like to withdraw?");
							double amount = s.nextDouble();
							double newBalance= (thisAccount.getBalance()-amount);
							if(amount >=0 && accountDAO.updateBalance(thisAccount.getID(), newBalance, con)) {
								System.out.println("Withdrawl successful, your new balance is "+newBalance);
							}else {
								System.out.println("Withdrawl failed");
							}
							
						}else if(selection == 2) {
							System.out.println("How much would you like to deposit?");
							double amount = s.nextDouble();
							double newBalance= (thisAccount.getBalance()+amount);
							if(amount >=0 && accountDAO.updateBalance(thisAccount.getID(), newBalance, con)) {
								System.out.println("Deposit successful, your new balance is "+newBalance);
							}else {
								System.out.println("Deposit failed");
							}
							
						}else if(selection == 3) {
							System.out.println("How much would you like to deposit?");
							selection = s.nextInt();
						}
						s.nextLine();
						
					}else {
						System.out.println("You do not have an account open, would you like to create an account? (Y/N)");
						next = s.nextLine();
						if(next.equalsIgnoreCase("y")) {
							System.out.println("What would you like to call your account?");
							next = s.nextLine();
							accountDAO.createAccount(next, u.getID(), con);
						}
					}
				}else {
					System.out.println("Incorrect username or password");
				}
			}
			else if(next.equalsIgnoreCase("n")) {
				System.out.println("Would you like to create a profile with us? (Y/N)");
				next = s.nextLine();
				if(next.equalsIgnoreCase("y")){
					System.out.println("Enter your desired username:");
					String username = s.nextLine();
					System.out.println("Enter your desired password:");
					String password = s.nextLine();
					if(userDAO.createUser(username, password, con)) {
						System.out.println("user created");
					}else {
						System.out.println("Failed to create account");
					}
				}
			}
			
		}

	}

}
