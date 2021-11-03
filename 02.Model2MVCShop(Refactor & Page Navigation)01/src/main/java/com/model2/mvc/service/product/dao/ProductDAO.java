package com.model2.mvc.service.product.dao;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Product;




public class ProductDAO {
	
	public ProductDAO(){
	}

	public void insertProduct(Product product) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "INSERT INTO PRODUCT VALUSE (seq_product_prod_no.NEXTVAL,?,?,?,?,?,SYSDATE)";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		
		stmt.setString(1, product.getProdName());
		stmt.setString(2, product.getProdDetail());
		stmt.setString(3, product.getManuDate());
		stmt.setInt(4, product.getPrice());
		stmt.setString(5, product.getFileName());
		
		stmt.executeUpdate();
		
		con.close();
	}

public Product findProduct(int prodNo) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "SELECT * FROM PRODUCT WHERE PROD_NO=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		
		stmt.setInt(1, prodNo);

		ResultSet rs = stmt.executeQuery();

		Product product = null;
		while (rs.next()) {
			product = new Product();
			product.setProdNo(rs.getInt("PROD_NO"));
			product.setProdName(rs.getString("PROD_NAME"));
			product.setProdDetail(rs.getString("PROD_DETAIL"));
			product.setManuDate(rs.getString("MANUFACTURE_DAY"));
			product.setPrice(rs.getInt("PRICE"));
			product.setFileName(rs.getString("IMAGE_FILE"));
			product.setRegDate(rs.getDate("REG_DATE"));
		}
		
		con.close();

		return product;
	}

public Map<String,Object> getProductList(Search search) throws Exception {
	
	Map<String , Object>  map = new HashMap<String, Object>();
	
	Connection con = DBUtil.getConnection();
	
	String sql = "SELECT pr.*, NVL(ts.tran_status_code, '000') TSC FROM product pr LEFT JOIN transaction ts"
			+ " ON pr.prod_no = ts.prod_no";
	if (search.getSearchCondition() != null) {
		if (search.getSearchCondition().equals("0")&&  !search.getSearchKeyword().equals("")) {
			sql += " where PROD_NAME like '%" + search.getSearchKeyword()
					+ "%'";
		} else if (search.getSearchCondition().equals("1")&& !search.getSearchKeyword().equals("")) {
			sql += " where PROD_NO like '%" + search.getSearchKeyword()
					+ "%'";
		}
	}
	sql += " order by PROD_NAME";
	System.out.println("ProductDAO::Original SQL :: " + sql);
	int totalCount = this.getTotalCount(sql);
	System.out.println("ProductDAO :: totalCount  :: " + totalCount);
	
	
	sql = makeCurrentPageSql(sql,search);
	PreparedStatement stmt = con.prepareStatement(sql);
	ResultSet rs = stmt.executeQuery();

	System.out.println(search);

	ArrayList<Product> list = new ArrayList<Product>();

	while(rs.next()) {
		Product product = new Product();
		product.setProdNo(rs.getInt("PROD_NO"));
		product.setProdName(rs.getString("PROD_NAME"));
		product.setProdDetail(rs.getString("PROD_DETAIL"));
		product.setManuDate(rs.getString("MANUFACTURE_DAY"));
		product.setPrice(rs.getInt("PRICE"));
		product.setFileName(rs.getString("IMAGE_FILE"));
		product.setRegDate(rs.getDate("REG_DATE"));
		System.out.println(rs.getString("TSC"));
		product.setProTranCode(rs.getString("TSC"));
		list.add(product);			
	}
	//==> totalCount 정보 저장
			map.put("totalCount", new Integer(totalCount));
			//==> currentPage 의 게시물 정보 갖는 List 저장
			map.put("list", list);

			rs.close();
			stmt.close();
			con.close();

			return map;
		}

public void updateProduct(Product product) throws Exception {
	
	Connection con = DBUtil.getConnection();

	String sql = "update Product set PROD_NAME=?,PROD_DETAIL=?,MANUFACTURE_DAY=?,PRICE=?,IMAGE_FILE=? where PROD_NO=?";
	
	PreparedStatement stmt = con.prepareStatement(sql);
	stmt.setString(1, product.getProdName());
	stmt.setString(2, product.getProdDetail());
	stmt.setString(3, product.getManuDate());
	stmt.setInt(4, product.getPrice());
	stmt.setString(5, product.getFileName());
	stmt.setInt(6, product.getProdNo());
	stmt.executeUpdate();
	
	con.close();
}

//게시판 Page 처리를 위한 전체 Row(totalCount)  return
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

	