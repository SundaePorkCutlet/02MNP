package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;

public class UpdateProductAction extends Action {
	
	public String execute(	HttpServletRequest request,
												HttpServletResponse response) throws Exception {
		System.out.println("여기는 업데이트액션");
		int prodNo=Integer.parseInt(request.getParameter("prodNo"));
		
		Product product=new Product();
		product.setProdName(request.getParameter("prodName"));
		product.setProdDetail(request.getParameter("prodDetail"));
		product.setManuDate(request.getParameter("manuDate"));
		product.setPrice(Integer.parseInt(request.getParameter("price")));
		product.setFileName(request.getParameter("fileName"));
		product.setProdNo(prodNo);
		ProductService service=new ProductServiceImpl();
		service.updateProduct(product);
		request.setAttribute("product", product);
		
		return "redirect:/getProduct.do?prodNo="+ prodNo+"&menu=manage";
	}
}