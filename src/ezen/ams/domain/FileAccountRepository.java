package ezen.ams.domain;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * 파일을 통해 은행계좌 목록 저장 및 관리(검색, 수정, 삭제) 구현체 2023-06-15
 * RandomAccessFile 이용
 * 
 * @author 이희영
 */
public class FileAccountRepository implements AccountRepository {

	// 파일 저장 경로
	private static final String FILE_PATH = "accounts.dbf";

	// 레코드 수 저장을 위한 파일 컬럼 사이즈 고정
	private static final int RECORD_COUNT_LENGTH = 4;

	// 레코드 저장을 위한 컬럼별 사이즈 고정
	private static final int ACCOUNT_NUM_LENGTH = 8;
	private static final int ACCOUNT_OWNER_LENGTH = 10;
	private static final int PASSWORD_LENGTH = 4;
	private static final int REST_MONEY = 8;
	private static final int BORROW_MONEY = 8;
	private static final int ACCOUNT_TYPE_LENGTH = 4;
	private static final int ACCOUNT_STATE_LENGTH = 4;

	public static final int RECORD_LENGTH = ACCOUNT_NUM_LENGTH + ACCOUNT_OWNER_LENGTH + PASSWORD_LENGTH + REST_MONEY
			+ BORROW_MONEY + ACCOUNT_TYPE_LENGTH + ACCOUNT_STATE_LENGTH;

	private RandomAccessFile file;

	// 저장된 계좌 수
	private int recordCount = 0;

	public FileAccountRepository() throws IOException {
		file = new RandomAccessFile(FILE_PATH, "rw");

		if (file.length() != 0) {
			recordCount = file.readInt();
		}
	}

	/**
	 * 전체 계좌 목록 수 반환
	 * 
	 * @return 목록수
	 */
	@Override
	public int getCount() {
		return recordCount;
	}

	/**
	 * 전체 계좌 목록 조회
	 * 
	 * @return 전체계좌 목록 계좌 상태가 활성화(숫자 0)된 계좌들만 조회
	 */
	@Override
	public List<Account> getAccounts() {
		List<Account> list = null;
		Account account = null;
		int accountState = 0;
		try {
			list = new ArrayList<>();
			for (int i = 0; i < recordCount; i++) {
				file.seek((i * RECORD_LENGTH) + RECORD_COUNT_LENGTH + RECORD_LENGTH - ACCOUNT_STATE_LENGTH);
				accountState = file.readInt();
				if (accountState == 0) {
					account = read(i);
					list.add(account);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return list;
	}

	/**
	 * 특정 위치의 레코드 정보를 Account로 반환
	 * 
	 * @param index 특정 위치
	 * @return 계좌
	 */
	@SuppressWarnings("unused")
	private Account read(int index) throws IOException {
		Account account = null;
		String accountNum = "";
		String accountOwner = "";
		int password = 0;
		long restMoney = 0;
		long borrowMoney = 0;
		int accountType = 0;
		int accountState = 0;

		file.seek((index * RECORD_LENGTH) + RECORD_COUNT_LENGTH);
		
		for (int i = 0; i < (ACCOUNT_NUM_LENGTH / 2); i++) {
			accountNum += file.readChar();
		}
		
		for (int i = 0; i < (ACCOUNT_OWNER_LENGTH / 2); i++) {
			accountOwner += file.readChar();
		}
		
		accountOwner = accountOwner.trim();
		password = file.readInt();
		restMoney = file.readLong();
		borrowMoney = file.readLong();
		accountType = file.readInt();
		accountState = file.readInt();

		if (accountType == 10) {
			account = new Account();
		} else if (accountType == 20) {
			account = new MinusAccount();
		}
		
		account.setAccountNum(accountNum);
		account.setAccountOwner(accountOwner);
		account.setPasswd(password);
		
		if (accountType == 20) {
			account.setRestMoney(restMoney + borrowMoney);
			((MinusAccount) account).setBorrowMoney(borrowMoney);
		} else {
			account.setRestMoney(restMoney);
		}
		
		return account;
	}

	/**
	 * 신규계좌 등록
	 * 
	 * @param account 신규계좌
	 * @return 등록 여부
	 */
	@Override
	public boolean addAccount(Account account) {
		try {
			file.seek((recordCount * RECORD_LENGTH) + RECORD_COUNT_LENGTH);
			account.setAccountNum(recordCount + 1000 + "");
			String accountNum = account.getAccountNum();
			String accountOwner = account.getAccountOwner();
			int password = account.getPasswd();
			long restMoney = account.getRestMoney();
			long borrowMoney = 0;
			int accountType = 0;
			int accountState = 0;
			
			if (account instanceof MinusAccount) {
				borrowMoney = ((MinusAccount) account).getBorrowMoney();
				accountType = 20;
			} else {
				accountType = 10;
			}

			int charCount = accountNum.length();
			for (int i = 0; i < (ACCOUNT_NUM_LENGTH / 2); i++) {
				file.writeChar(accountNum.charAt(i));
			}
			charCount = accountOwner.length();
			
			for (int i = 0; i < (ACCOUNT_OWNER_LENGTH / 2); i++) {
				file.writeChar((i < charCount ? accountOwner.charAt(i) : ' '));
			}
			
			file.writeInt(password);
			file.writeLong(restMoney);
			file.writeLong(borrowMoney);
			file.writeInt(accountType);
			file.writeInt(accountState);
			file.seek(0);
			file.writeInt(++recordCount);
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return true;
	}

	/**
	 * 계좌번호로 계좌 검색 기능
	 * 
	 * @param accountNum 검색 계좌번호
	 * @return 검색된 계좌 계좌 상태가 활성화(숫자 0)된 계좌만 조회
	 */
	@Override
	public Account searchAccount(String accountNum) {
		Account account = null;
		int accountState = 0;
		try {
			for (int i = 0; i < recordCount; i++) {
				file.seek((i * RECORD_LENGTH) + RECORD_COUNT_LENGTH + RECORD_LENGTH - ACCOUNT_STATE_LENGTH);
				accountState = file.readInt();
				
				if (accountState == 0) {
					account = read(i);
					if (account.getAccountNum().equals(accountNum)) {
						return account;
					}
				}
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 예금주명으로 계좌 검색 기능
	 * 
	 * @param accountOwner 검색 예금주명
	 * @return 검색된 계좌목록 계좌 상태가 활성화(숫자 0)된 계좌들만 조회
	 */
	@Override
	public List<Account> searchAccountByOwner(String accountOwner) {
		List<Account> list = null;
		Account account = null;
		int accountState = 0;
		
		try {
			list = new ArrayList<Account>();
			for (int i = 0; i < recordCount; i++) {
				file.seek((i * RECORD_LENGTH) + RECORD_COUNT_LENGTH + RECORD_LENGTH - ACCOUNT_STATE_LENGTH);
				accountState = file.readInt();
				
				if (accountState == 0) {
					account = read(i);
					if (account.getAccountOwner().equals(accountOwner)) {
						list.add(account);
					}
				}
			}
			return list;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 계좌번호로 계좌 삭제 기능
	 * 
	 * @param accountNum 검색 계좌번호
	 * @return 계좌 삭제 여부 삭제한 계좌는 파일에서 지워지지 않고, 계좌 상태 비활성화(숫자 1)로 변경
	 */
	@Override
	public boolean removeAccount(String accountNum) {
		Account account = null;
		int accountState = 0;
		
		try {
			for (int i = 0; i < recordCount; i++) {
				file.seek((i * RECORD_LENGTH) + RECORD_COUNT_LENGTH + RECORD_LENGTH - ACCOUNT_STATE_LENGTH);
				accountState = file.readInt();
				
				if (accountState == 0) {
					account = read(i);
					
					if (account.getAccountNum().equals(accountNum)) {
						file.seek((i * RECORD_LENGTH) + RECORD_COUNT_LENGTH + RECORD_LENGTH - ACCOUNT_STATE_LENGTH);
						file.writeInt(1);
						return true;
					}
				}
			}
			return false;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

//	// 테스트용 main
//	public static void main(String[] args) throws IOException {
//
//		AccountRepository accountRepository = new FileAccountRepository();
//		List<Account> list = null;
//		Account account = null;
//
//		// 계좌 등록 테스트
//		System.out.println("########## 계좌 등록 테스트  ##########");
//		account = new Account("이희영", 1111, 10000);
//		accountRepository.addAccount(account);
//		account = new MinusAccount("이대출", 1111, 0, 100000);
//		accountRepository.addAccount(account);
//		account = new Account("이희영", 1111, 70000);
//		accountRepository.addAccount(account);
//
//		System.out.println("계좌 등록 완료");
//
//		// 전체 계좌 조회 테스트
//		list = accountRepository.getAccounts();
//		System.out.println("########## 전체 계좌 조회 테스트 ##########");
//		for (Account account2 : list) {
//			System.out.println(account2);
//		}
//
////		// 계좌번호 검색 테스트
////		account = accountRepository.searchAccount("1000");
////		System.out.println("########## 계좌번호 검색 테스트 ##########");
////		System.out.println(account);
////			
////		// 예금주명 검색 테스트
////		List<Account> searchList = accountRepository.searchAccountByOwner("이희영");
////		System.out.println("########## 예금주명 검색 테스트 ##########");
////		for (Account searchAccount : searchList) {
////			System.out.println(searchAccount);
////		}
////			
////		// 계좌번호 삭제 테스트
////		accountRepository.removeAccount("1002");
////		System.out.println("########## 계좌번호 삭제 테스트 ##########");
////		list = accountRepository.getAccounts();
////		for (Account account2 : list) {
////			System.out.println(account2);
////		}
//	}
}