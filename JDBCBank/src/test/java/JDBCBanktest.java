import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import org.junit.*;
import org.junit.rules.ExpectedException;

import dao.accountDAO;
import dao.userDAO;
import models.accountModel;
import util.ConnectionUtil;
public class JDBCBanktest {

	public Connection con;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void testCreatUserGetUser() {
		try {
			con = ConnectionUtil.getConnection();
			con.setAutoCommit(false);
			userDAO.createUser("sfdasfd", "ljlkj", con);
			assertEquals("sfdasfd", userDAO.getUser("sfdasfd", "ljlkj", con).get().getUserName());
			con.rollback();
		}catch(Exception e){
			fail();
		}
	}
	@Test
	public void testDeleteUser() {
		try {
			con = ConnectionUtil.getConnection();
			con.setAutoCommit(false);
			userDAO.createUser("ttsfdasfd", "ljlkj", con);
			int id = userDAO.getUser("ttsfdasfd", "ljlkj", con).get().getID();
			assertEquals(true, userDAO.deleteUser(id, con));
			con.rollback();
		}catch(Exception e){
			fail();
		}
	}
	@Test
	public void testGetAllUsers() {
		try {
			con = ConnectionUtil.getConnection();
			con.setAutoCommit(false);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT USERNAME, ID, PASSWORD FROM Users");
			int count = 0;
			while(rs.next()) {
				count++;
			}
			int num = userDAO.getAll(con).get().size();
			System.out.println(count+" "+num);
			assertEquals(num, count);
			con.rollback();
		}catch(Exception e){
			fail();
		}
	}
	@Test
	public void testChangePassword() {
		try {
			con = ConnectionUtil.getConnection();
			con.setAutoCommit(false);
			userDAO.createUser("rewqerqwr", "ljlkj", con);
			String newPassword="newpass";
			int id = userDAO.getUser("rewqerqwr", "ljlkj", con).get().getID();
			userDAO.changePassword(id, newPassword, con);
			assertEquals(newPassword, userDAO.getUser("rewqerqwr", newPassword, con).get().getPassword());
			con.rollback();
		}catch(Exception e){
			fail();
		}
	}
	
	
	//accounts
	@Test
	public void testCreateAccountGetACcounts() {
		try {
			con = ConnectionUtil.getConnection();
			con.setAutoCommit(false);
			userDAO.createUser("kjty", "ljlkj", con);
			int id = userDAO.getUser("kjty", "ljlkj", con).get().getID();
			accountDAO.createAccount("testacc1", id, con);
			ArrayList<accountModel> accountModels = accountDAO.getAccounts(id, con).get();
			for(int i =0; i<accountModels.size();i++) {
				String a =accountModels.get(i).getName();
				if( a.equalsIgnoreCase("testacc1")) {
					assertEquals("testacc1", a);
				}
			}
			con.rollback();
		}catch(Exception e){
			fail();
		}
	}
	@Test
	public void testUpdateAccount() {
		try {
			con = ConnectionUtil.getConnection();
			con.setAutoCommit(false);
			userDAO.createUser("kjty", "ljlkj", con);
			int id = userDAO.getUser("kjty", "ljlkj", con).get().getID();
			accountDAO.createAccount("testacc1", id, con);
			ArrayList<accountModel> accountModels = accountDAO.getAccounts(id, con).get();
			int accid=0;
			accountModel account= new accountModel();
			for(int i =0; i<accountModels.size();i++) {
				String a =accountModels.get(i).getName();
				if( a.equalsIgnoreCase("testacc1")) {
					accid=accountModels.get(i).getID();
				}
			}
			accountDAO.updateBalance(accid, 100, con);
			accountModels = accountDAO.getAccounts(id, con).get();
			for(int i =0; i<accountModels.size();i++) {
				if(accountModels.get(i).getID() == accid) {
					account=accountModels.get(i);
				}
			}
			assertEquals(account.getBalance(), 100, 1);
			con.rollback();
		}catch(Exception e){
			fail();
		}
	}
	@Test
	public void testDeleteAccount() {
		try {
			con = ConnectionUtil.getConnection();
			con.setAutoCommit(false);
			userDAO.createUser("kjty", "ljlkj", con);
			int id = userDAO.getUser("kjty", "ljlkj", con).get().getID();
			accountDAO.createAccount("testacc1", id, con);
			ArrayList<accountModel> accountModels = accountDAO.getAccounts(id, con).get();
			int accid=0;
			accountModel account= new accountModel();
			for(int i =0; i<accountModels.size();i++) {
				String a =accountModels.get(i).getName();
				if( a.equalsIgnoreCase("testacc1")) {
					accid=accountModels.get(i).getID();
				}
			}
			assertEquals(true, accountDAO.deleteAccount(accid, con));
		}catch(Exception e) {
			fail();
		}
		
		
	}
	
}
