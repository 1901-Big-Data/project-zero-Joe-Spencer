import java.util.*;

import dao.accountDAO;
import dao.userDAO;
import models.accountModel;
import models.userModel;
import bankExceptions.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

import java.sql.SQLException;

import util.ConnectionUtil;

public class Application {
	private static Scanner s;
	private static Properties prop = new Properties();
	public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
		boolean running = true;
		s = new Scanner(System.in);
		InputStream in = new FileInputStream("C:\\Users\\Joe\\Documents\\Project0\\JDBCBank\\JDBCBank.properties");
		prop.load(in);
		Connection con = ConnectionUtil.getConnection();
		while(running) {
			System.out.println("Are you a registered user with us? (Y/N)");
			String next = s.nextLine();
			if(next.equalsIgnoreCase("y")) {
				System.out.println("Enter username:");
				String userName = s.nextLine();
				System.out.println("Enter password for "+ userName);
				String password = s.nextLine();
				String superUserName= prop.getProperty("super.name");
				String superPassword= prop.getProperty("super.pass");
				Optional<userModel> user = userDAO.getUser(userName, password, con);
				if(userName.equals(superUserName)&&password.equals(superPassword)){
					superUser(superUserName, con);
				}else if(user.isPresent()) {
					userModel u = user.get();
					boolean loggedIn=true;
					while(loggedIn) {
					Optional<ArrayList<accountModel>> accounts =accountDAO.getAccounts(u.getID(), con);
					System.out.println("What would you like to do? \n1. Access my accounts \n2. Create a new account\n3. Logout");
					int selection = s.nextInt();
						if(accounts.isPresent() && selection == 1) {
							ArrayList<accountModel> accountModels =accounts.get();
							System.out.println("Which account would you like to access?");
							int i;
							for(i =0; i<accountModels.size();i++) {
								System.out.println((i+1)+". "+accountModels.get(i).getName());
							}
							selection = s.nextInt();
							accountModel thisAccount =accountModels.get(selection-1);
							System.out.println("Account balance is "+thisAccount.getBalance());
							System.out.println("What would you like to do with this account? \n1. Withdraw \n2. Deposit \n3. delete \n4. nothing");
							selection = s.nextInt();
							if(selection == 1) {
								System.out.println("How much would you like to withdraw?");
								double amount = s.nextDouble();
								double newBalance= (thisAccount.getBalance()-amount);
								try {
									accountDAO.updateBalance(thisAccount.getID(), newBalance, con);
								}
								catch(OverdraftException e) {
									System.out.println("Withdrawl failed, overdraft exception");
								}
								
							}else if(selection == 2) {
								System.out.println("How much would you like to deposit?");
								double amount = s.nextDouble();
								double newBalance= (thisAccount.getBalance()+amount);
								try {
									accountDAO.updateBalance(thisAccount.getID(), newBalance, con);
								}
								catch(OverdraftException e) {
									System.out.println("Withdrawl failed, overdraft exception");
								}
								
							}else if(selection == 3) {
								if(thisAccount.getBalance()==0) {
									if(accountDAO.deleteAccount(thisAccount.getID(), con)) {
										System.out.println("Successfully deleted account");
									}
								}else {
									System.out.println("Cannot delete an account if the blance is not 0");
								}
							}
							
						}else if(selection == 1) {
							System.out.println("no account is present");
						}else if(selection == 2){
							s.reset();
							System.out.println("What would you like to call your account?");
							next = s.next();
							accountDAO.createAccount(next, u.getID(), con);
						}else if(selection == 3) {
							loggedIn = false;
						}
						s.nextLine();
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
	public static void superUser(String n, Connection con) throws SQLException, ClassNotFoundException {
		System.out.println("Hello superuser, "+n);
		boolean loggedIn=true;
		s = new Scanner(System.in);
		int selection;
		while(loggedIn) {
			System.out.println("What would you like to do? \n1. View all users \n2. Create a new user \n3. Delete a user \n4. Update a user's password \n5. Logout");
			selection = s.nextInt();
			if(selection == 1){
				Optional<ArrayList<userModel>> usersOptional=userDAO.getAll(con);
				if(usersOptional.isPresent()) {
					ArrayList<userModel> users= usersOptional.get();
					for(int i =0; i<users.size();i++) {
						System.out.println(users.get(i).getUserName()+" id: "+users.get(i).getID()+" password: "+users.get(i).getPassword());
					}
				}else { 
					System.out.println("no users found");
				}
				
			}else if(selection == 2){
				System.out.println("Enter a username:");
				String userName;
				s.nextLine();
				userName = s.nextLine();
				System.out.println("Enter a password:");
				String password;
				password= s.nextLine();
				if(userDAO.createUser(userName, password, con)) {
					System.out.println("user created");
				}else {
					System.out.println("Failed to create account");
				}
			}else if(selection == 3){
				System.out.println("Enter the id of the user you wish to delete");
				selection = s.nextInt();
				Optional<ArrayList<accountModel>> accounts =accountDAO.getAccounts(selection, con);
				if(accounts.isPresent()) {
					System.out.println("You cannot delete a user who has open accounts");
				}else {
					if(userDAO.deleteUser(selection, con)) {
						System.out.println("User deleted");
					}else {
						System.out.println("Failed to delete user");
					}
				}
			}else if(selection == 4){
				System.out.println("Enter the id of the user whose password you wish to change");
				int id;
				String password;
				
				id=s.nextInt();
				System.out.println("Enter the new password");
				s.nextLine();
				password=s.nextLine();
				if(userDAO.changePassword(id, password, con)) {
					System.out.println("Password changed");
				}else {
					System.out.println("Failed to change password");
				}
			}else if(selection == 5){
				s.nextLine();
				loggedIn=false;
			}
			
		}
	}

}
