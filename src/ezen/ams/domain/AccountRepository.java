package ezen.ams.domain;

import java.util.List;

/**
 * 은행계좌 목록 저장 및 관리 명세
 * 
 * @author 이희영
 */
public interface AccountRepository {

	public int getCount();

	public List<Account> getAccounts();

	public boolean addAccount(Account account);

	public Account searchAccount(String accountNum);

	public List<Account> searchAccountByOwner(String accountOwner);

	public boolean removeAccount(String accountNum);
}