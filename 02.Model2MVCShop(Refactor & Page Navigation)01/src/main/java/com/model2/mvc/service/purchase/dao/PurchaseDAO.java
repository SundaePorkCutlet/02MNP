package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.dao.ProductDAO;
import com.model2.mvc.service.user.dao.UserDao;

public class PurchaseDAO {


	
	public PurchaseDAO() {
	
	}
	
	public void insertPurchase(Purchase purchase) throws Exception {
		System.out.println("이것은" +purchase.getBuyer().getUserId());
		
		User user = new User();
		
		Connection con = DBUtil.getConnection();
		
		String sql = "insert into TRANSACTION values (seq_transaction_tran_no.NEXTVAL,?,?,?,?,?,?,?,?,SYSDATE,?)";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		
		stmt.setInt(1, purchase.getPurchaseProd().getProdNo());
		stmt.setString(2,purchase.getBuyer().getUserId());
		System.out.println("이번엔"+purchase.getBuyer());
		stmt.setString(3, purchase.getPaymentOption());
		stmt.setString(4,purchase.getReceiverName());
		stmt.setString(5,purchase.getReceiverPhone());
		stmt.setString(6, purchase.getDivyAddr());
		stmt.setString(7,purchase.getDivyRequest());
		stmt.setString(8, purchase.getTranCode());
		stmt.setString(9,purchase.getDivyDate());
		
		
		stmt.executeUpdate();
		

	
		stmt.close();

		con.close();
		
		
		
	}

	public Purchase findPurchase(int tranNo) throws Exception {
		ProductDAO productDAO = new ProductDAO();
		UserDao userDAO = new UserDao();
		
		Connection con = DBUtil.getConnection();

		String sql = "select * from TRANSACTION where TRAN_NO=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, tranNo);

		ResultSet rs = stmt.executeQuery();

		Purchase purchase = null;
	
		while (rs.next()) {
			purchase = new Purchase();

			purchase.setTranNo(rs.getInt("TRAN_NO"));
			purchase.setBuyer(userDAO.findUser(rs.getString("BUYER_ID")));
			purchase.setPurchaseProd(productDAO.findProduct(rs.getInt("PROD_NO")));
			purchase.setPaymentOption(rs.getString("PAYMENT_OPTION"));
			purchase.setReceiverName(rs.getString("RECEIVER_NAME"));
			purchase.setReceiverPhone(rs.getString("RECEIVER_PHONE"));
			purchase.setDivyAddr(rs.getString("DEMAILADDR"));
			purchase.setDivyRequest(rs.getString("DLVY_REQUEST"));
			purchase.setTranCode(rs.getString("TRAN_STATUS_CODE"));
			purchase.setDivyDate(rs.getString("DLVY_DATE"));
			
			
		}

		rs.close();
		stmt.close();

		con.close();

		return purchase;
	}
	
	public Purchase findPurchase2(int prodNo) throws Exception {
		ProductDAO productDAO = new ProductDAO();
		UserDao userDAO = new UserDao();
		
		Connection con = DBUtil.getConnection();

		String sql = "select * from TRANSACTION where prod_NO=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, prodNo);

		ResultSet rs = stmt.executeQuery();

		Purchase purchase = null;
	
		while (rs.next()) {
			purchase = new Purchase();

			
			purchase.setBuyer(userDAO.findUser(rs.getString("BUYER_ID")));
			purchase.setPurchaseProd(productDAO.findProduct(rs.getInt("PROD_NO")));
			purchase.setPaymentOption(rs.getString("PAYMENT_OPTION"));
			purchase.setReceiverName(rs.getString("RECEIVER_NAME"));
			purchase.setReceiverPhone(rs.getString("RECEIVER_PHONE"));
			purchase.setDivyAddr(rs.getString("DEMAILADDR"));
			purchase.setDivyRequest(rs.getString("DLVY_REQUEST"));
			purchase.setTranCode(rs.getString("TRAN_STATUS_CODE"));
			purchase.setDivyDate(rs.getString("DLVY_DATE"));
			
			
		}

		rs.close();
		stmt.close();

		con.close();

		return purchase;
	}
	
	public Map<String,Object> getPurchaseList(Search search,String buyerId) throws Exception {
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		Connection con = DBUtil.getConnection();
		
		ProductDAO productDAO = new ProductDAO();
		UserDao userDAO = new UserDao();
		
		String sql = "select * from TRANSACTION WHERE buyer_id ='"+buyerId+"'";
		
		System.out.println("ProductDAO::Original SQL :: " + sql);
		System.out.println(buyerId);
		
		
		int totalCount = this.getTotalCount(sql);
		sql=this.makeCurrentPageSql(sql, search);
		
		PreparedStatement stmt = 
			con.prepareStatement(	sql,
														ResultSet.TYPE_SCROLL_INSENSITIVE,
														ResultSet.CONCUR_UPDATABLE);
		//stmt.setString(1, buyerId);
		ResultSet rs = stmt.executeQuery();

	

		ArrayList<Purchase> list = new ArrayList<Purchase>();
		while(rs.next()) {	
		Purchase purchase = new Purchase();
				purchase.setBuyer(userDAO.findUser(rs.getString("BUYER_ID")));
				purchase.setPurchaseProd(productDAO.findProduct(rs.getInt("PROD_NO")));
				purchase.setPaymentOption(rs.getString("PAYMENT_OPTION"));
				purchase.setReceiverName(rs.getString("RECEIVER_NAME"));
				purchase.setReceiverPhone(rs.getString("RECEIVER_PHONE"));
				purchase.setDivyAddr(rs.getString("DEMAILADDR"));
				purchase.setDivyRequest(rs.getString("DLVY_REQUEST"));
				purchase.setTranCode(rs.getString("TRAN_STATUS_CODE"));
				purchase.setTranNo(rs.getInt("TRAN_NO"));
				purchase.setDivyDate(rs.getString("DLVY_DATE"));

				list.add(purchase);
			
			
		}
		map.put("totalCount", new Integer(totalCount));
	//==> currentPage 의 게시물 정보 갖는 List 저장
		map.put("list", list);

		rs.close();
		stmt.close();
		con.close();

			
		return map;
	}

	public void updatePurchase(Purchase purchase) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "update TRANSACTION set PAYMENT_OPTION=?,RECEIVER_NAME=?,RECEIVER_PHONE=?,DEMAILADDR=?,DIVYREQUEST=?,DIVYDATE where TRAN_NO=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, purchase.getPaymentOption());
		stmt.setString(2, purchase.getReceiverName());
		stmt.setString(3, purchase.getReceiverPhone());
		stmt.setString(4, purchase.getDivyAddr());
		stmt.setString(5, purchase.getDivyRequest());
		stmt.setString(6, purchase.getDivyDate());
		stmt.setInt(7, purchase.getTranNo());
		stmt.executeUpdate();


		stmt.close();
	
		con.close();
	}
	                 
	
	public void updateTranCode(Purchase purchase) throws Exception {

		Connection con = DBUtil.getConnection();

		String sql = "update TRANSACTION SET TRAN_STATUS_CODE=? WHERE TRAN_NO =? "
				+ "or PROD_NO = ?";
		PreparedStatement stmt = con.prepareStatement(sql);		
		stmt.setString(1, purchase.getTranCode());
		stmt.setInt(2, purchase.getTranNo());
		stmt.setInt(3, purchase.getPurchaseProd().getProdNo());
		stmt.executeUpdate();

	
		stmt.close();

		con.close();
				
		
	}
private int getTotalCount(String sql) throws Exception {
		
		sql = "SELECT COUNT(*) "+
		          "FROM ( " +sql+ ") countTable";
		
		Connection con = DBUtil.getConnection();
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		int totalCount = 0;
		if( rs.next() ){
			totalCount = rs.getInt(1);
		}
		
		pStmt.close();
		con.close();
		rs.close();
		
		return totalCount;
	}

private String makeCurrentPageSql(String sql , Search search){
	sql = 	"SELECT * "+ 
				"FROM (		SELECT inner_table. * ,  ROWNUM AS row_seq " +
								" 	FROM (	"+sql+" ) inner_table "+
								"	WHERE ROWNUM <="+search.getCurrentPage()*search.getPageSize()+" ) " +
				"WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1) +" AND "+search.getCurrentPage()*search.getPageSize();
	
	System.out.println("ProductDAO :: make SQL :: "+ sql);	
	
	return sql;
}

}

	
