package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.dao.PurchaseDAO;

public class UpdateTranCodeAction extends Action {



		@Override
		public String execute(	HttpServletRequest request,
													HttpServletResponse response) throws Exception {

			
		PurchaseDAO purchaseDAO = new PurchaseDAO();
		Purchase purchase = new Purchase();
		String tran=request.getParameter("TranCode");
		int tranNo = Integer.parseInt(request.getParameter("tranNo"));
		purchase = purchaseDAO.findPurchase(tranNo);
		if("002".equals(tran)) {
			tran = "003";
			purchase.setTranCode(tran);
			purchaseDAO.updateTranCode(purchase);
		}
		
		
		
		
		return "redirect:/listPurchase.do";
		
		}
}