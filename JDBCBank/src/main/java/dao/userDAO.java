package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Optional;

import models.accountModel;
import models.userModel;


public class userDAO {
	public static Optional<userModel> getUser(String username, String password, Connection con) throws ClassNotFoundException, SQLException{
		
		userModel user = new userModel();
		String sql = "call attempt_login(?,?,?,?)";
		CallableStatement cs = con.prepareCall(sql);
		cs.setString(1, username);
		cs.setString(2, password);
		cs.registerOutParameter(3, Types.INTEGER);
		cs.registerOutParameter(4, Types.INTEGER);
		cs.execute();
		Integer success = cs.getInt(3);
		Integer id = cs.getInt(4);
		if(success == 1) {
			user.setUserName(username);
			user.setPassword(password);
			user.setID(id);
			Optional<userModel> r = Optional.of(user);
			return(r);
		}else {
			return Optional.empty();
		}
		
		
	}
	public static boolean createUser(String username, String password, Connection con) throws SQLException{
		String sql = "insert into users values(?,?,?)";
		
		CallableStatement cs = con.prepareCall(sql);
		cs.setString(1, username);
		cs.setNull(2, Types.NULL);
		cs.setString(3, password);
		boolean r=true;
		try{
			cs.execute();
		}catch(Exception e) {
			r=false;
		}
		return r;
	}
	public static boolean deleteUser(int id, Connection con) throws SQLException{
		String sql = "DELETE FROM USERS WHERE ID=?";
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
	public static boolean changePassword(int id, String pass, Connection con) throws SQLException {
		String sql = "Update users set password=? where id=?";
		CallableStatement cs = con.prepareCall(sql);
		cs.setString(1, pass);
		cs.setInt(2, id);
		boolean r=true;
		try{
			cs.execute();
		}catch(Exception e) {
			r=false;
		}
		return r;
	}
	public static Optional<ArrayList<userModel>> getAll(Connection con) throws SQLException {
		Statement stmt = con.createStatement();
		ArrayList<userModel> users = new ArrayList<userModel>();
		ResultSet rs = stmt.executeQuery("SELECT USERNAME, ID, PASSWORD FROM Users");
		while (rs.next()) {
			userModel user = new userModel();
			user.setUserName(rs.getString(1));
			user.setID(rs.getInt(2));
			user.setPassword((rs.getString(3)));
			users.add(user);
		}
		
		if(users.isEmpty()) {
			return Optional.empty();
		}else {
			Optional<ArrayList<userModel>> r = Optional.of(users);
			return r;
		}
		
	}
}
