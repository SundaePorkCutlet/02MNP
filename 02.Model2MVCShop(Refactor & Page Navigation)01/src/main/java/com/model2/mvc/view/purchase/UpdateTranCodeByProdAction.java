package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.dao.PurchaseDAO;

public class UpdateTranCodeByProdAction extends Action{

	
	
	@Override
	public String execute(	HttpServletRequest request,
												HttpServletResponse response) throws Exception {

		
	PurchaseDAO purchaseDAO = new PurchaseDAO();
	Purchase purchase = new Purchase();
	String tran=request.getParameter("ProTranCode");
	int prodNo = Integer.parseInt(request.getParameter("prodNo"));
	purchase = purchaseDAO.findPurchase2(prodNo);
	if("001".equals(tran)) {
		tran = "002";
		purchase.setTranCode(tran);
		purchaseDAO.updateTranCode(purchase);
	}
	
	
	
	
	return "redirect:/listProduct.do";
	
	}
}