package ezen.ams.app;

import ezen.ams.view.AMSFrame;

/**
 * 은행 계좌 관리 애플리케이션
 * 
 * @author 이희영
 */
public class AMS {

	public static void main(String[] args) {

		AMSFrame frame = new AMSFrame("HY_BANK_AMS");
		frame.init();
		frame.setSize(600, 450);
		frame.setResizable(false);
		frame.addEventListner();
		frame.setVisible(true);
	}
}