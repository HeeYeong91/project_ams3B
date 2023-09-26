package ezen.ams.domain;

/**
 * 계좌 종류 Enum
 * 
 * @author 이희영
 */
public enum AccountType {

	ALL_ACCOUNT("전체계좌"), GENERAL_ACCOUNT("입출금계좌"), MINUS_ACCONUT("마이너스계좌");

	private final String name;

	private AccountType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}