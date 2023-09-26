package ezen.ams.domain;

import java.util.Formatter;

import ezen.ams.exception.NotBalanceException;

/*
 * 은행계좌 추상화
 * 
 * @author 이희영
 */
public class Account {

	// 필드 캡슐화(은닉화)
	private String accountNum;
	private String accountOwner;
	private int passwd;
	private long restMoney;

	public final static String BANK_NAME = "희영은행";
	private static int accountId;

	// static 초기화 블럭
	static {
		accountId = 1000;
	}

	// 생성자 오버로딩
	public Account() {
	}

	public Account(String accountOwner, int passwd, long restMoney) {
		this.accountNum = (accountId++) + "";
		this.accountOwner = accountOwner;
		this.passwd = passwd;
		this.restMoney = restMoney;
	}

	// setter/getter 메서드
	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getAccountOwner() {
		return accountOwner;
	}

	public void setAccountOwner(String accountOwner) {
		this.accountOwner = accountOwner;
	}

	public int getPasswd() {
		return passwd;
	}

	public void setPasswd(int passwd) {
		this.passwd = passwd;
	}

	public void setRestMoney(long restMoney) {
		this.restMoney = restMoney;
	}

	/*
	 * 입금기능
	 */
	public long deposit(long money) throws NotBalanceException {
		if (money <= 0) {
			throw new NotBalanceException("입금금액은 0이거나 음수일 수 없습니다.");
		} else if (money >= 100000000) {
			throw new NotBalanceException("1억이상 입금할 수 없습니다.");
		}
		return restMoney += money;
	}

	/*
	 * 출금기능
	 */
	public long withdraw(long money) throws NotBalanceException {
		if (money <= 0) {
			throw new NotBalanceException("출금금액은 0이거나 음수일 수 없습니다.");
		} else if (money >= restMoney) {
			throw new NotBalanceException("잔액이 부족하여 출금할 수 없습니다.");
		}
		return restMoney -= money;
	}

	/*
	 * 잔액조회 기능
	 */
	public long getRestMoney() {
		return restMoney;
	}

	/*
	 * 비밀번호 확인
	 */
	public boolean checkPasswd(int pwd) {
		return passwd == pwd ? true : false;
	}

	public void printInfo() {
		System.out.println(accountNum + "\t" + accountOwner + "\t****\t" + restMoney);
	}

	@Override
	@SuppressWarnings("resource")
	public String toString() {
		Formatter formatter = new Formatter();
		formatter.format("%1$8s%2$12s%3$14s%4$14s%5$,28d", "입출금계좌", accountNum, accountOwner, "****", restMoney);

		String str = formatter.toString();
		return str;
	}

	@Override
	public boolean equals(Object obj) {
		return toString().equals(obj.toString());
	}

	// 스태틱(클래스) 메서드
	public static int getAccountId() {
		return accountId;
	}
}