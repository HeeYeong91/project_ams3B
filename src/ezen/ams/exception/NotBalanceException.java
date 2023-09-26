package ezen.ams.exception;

/**
 * 사용자 정의 예외 클래스
 * 
 * @author 이희영
 */
@SuppressWarnings("serial")
public class NotBalanceException extends Exception {
	private int errorCode;

	public NotBalanceException() {
		super("예기치 않은 오류가 발생하였습니다. 관리자에게 문의하세요..");
	}

	public NotBalanceException(String message) {
		super(message);
	}

	public NotBalanceException(String message, int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		return getMessage() + " [에러코드 : " + getErrorCode() + "]";
	}
}