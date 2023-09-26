package ezen.ams.util;

/**
 * 입력데이터 유효성 검증 공통 기능 정의 유틸리티
 * 
 * @author 이희영
 */
public class Validator {

	/**
	 * 입력데이터가 비어있거나 공백만 있는지 체크
	 * 
	 * @param input 입력문자열
	 * @return 유효여부
	 */
	public static boolean hasText(String input) {
		if (input != null && input.trim().length() != 0) {
			return true;
		}
		return false;
	}

	/**
	 * 입력데이터가 숫자인지 여부 체크
	 * 
	 * @param number 입력문자열
	 * @return 유효여부
	 */
	public static boolean isNumber(String number) {
		return number.matches("\\d+");

	}

	/**
	 * 입력데이터가 ID 형식인지 여부 체크
	 * 
	 * @param id 입력문자열
	 * @return 유효여부
	 */
	public static boolean isId(String id) {
		return id.matches("\\w{8,10}");
	}
}