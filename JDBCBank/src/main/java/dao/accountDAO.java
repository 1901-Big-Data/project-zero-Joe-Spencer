package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Optional;

import models.accountModel;
import bankExceptions.OverdraftException;


public class accountDAO {
	public static Optional<ArrayList<accountModel>> getAccounts(int id, Connection con) throws ClassNotFoundException, SQLException{
			Statement stmt = con.createStatement();
			ArrayList<accountModel> accounts = new ArrayList<accountModel>();
			ResultSet rs = stmt.executeQuery("SELECT USER_ID, ACCOUNT_NAME, ACCOUNT_ID, BALANCE FROM ACCOUNTS where User_ID = "+id);
			while (rs.next()) {
				accountModel account = new accountModel();
				account.setUser(rs.getInt(1));
				account.setName(rs.getString(2));
				account.setID(rs.getInt(3));
				account.setBalance(rs.getInt(4));
				accounts.add(account);
	
			}
			
			if(accounts.isEmpty()) {
				return Optional.empty();
			}else {
				Optional<ArrayList<accountModel>> r = Optional.of(accounts);
				return r;
			}
			
	}
	public static boolean updateBalance(int id, double newBalance, Connection con) throws SQLException, OverdraftException {
		if(newBalance <0) {
			throw new OverdraftException();
		}
		String sql = "Update accounts set balance=? where account_id=?";
		CallableStatement cs = con.prepareCall(sql);
		cs.setDouble(1, newBalance);
		cs.setInt(2, id);
		boolean r=true;
		try{
			cs.execute();
		}catch(Exception e) {
			r=false;
		}
		return r;
	}
	public static boolean createAccount(String name, int user_id, Connection con) throws SQLException {
		String sql = "insert into accounts values(?,?,?,?)";
		CallableStatement cs = con.prepareCall(sql);
		cs.setString(1, name);
		cs.setNull(2, Types.NULL);
		cs.setInt(3, user_id);
		cs.setDouble(4, 0);
		boolean r=true;
		try{
			cs.execute();
		}catch(Exception e) {
			r=false;
		}
		return r;
	}
	public static boolean deleteAccount(int id, Connection con) throws SQLException {
		String sql = "delete from accounts where account_id=?";
		CallableStatement cs = con.prepareCall(sql);
		cs.setInt(1, id);
		boolean r=true;
		try{
			cs.execute();
		}catch(Exception e) {
			r=false;
		}
		return r;
	}
}
