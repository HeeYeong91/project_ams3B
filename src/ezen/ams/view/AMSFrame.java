package ezen.ams.view;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Formatter;
import java.util.List;

import javax.swing.JOptionPane;

import ezen.ams.domain.Account;
import ezen.ams.domain.AccountRepository;
import ezen.ams.domain.AccountType;
import ezen.ams.domain.FileAccountRepository;
import ezen.ams.domain.MinusAccount;
import ezen.ams.util.Validator;

/**
 * AMS 버전2(GUI) GridBagLayout (2023-05-23)
 * 
 * @author 이희영
 */
@SuppressWarnings("serial")
public class AMSFrame extends Frame {

	Label accountTypeL, accountNumL, accountOwnerL, passwdL, inputMoneyL, borrowMoneyL, accountsListL, wonL;
	Choice typeChoice;
	TextField accountNumTF, accountOwnerTF, passwdTF, inputMoneyTF, borrowMoneyTF;
	Button numSearchBtn, deleteBtn, ownerSearchBtn, addBtn, allAccountsBtn;
	TextArea mainTA;

	GridBagLayout grid;
	GridBagConstraints con;

	public static AccountRepository repository;

	public AMSFrame() {
		this("NoTitle");
	}

	public AMSFrame(String title) {
		super(title);

		try {
			repository = new FileAccountRepository();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			System.exit(0);
		}

		// 라벨
		accountTypeL = new Label("계좌종류", Label.CENTER);
		accountNumL = new Label("계좌번호", Label.CENTER);
		accountOwnerL = new Label("예금주명", Label.CENTER);
		passwdL = new Label("비밀번호", Label.CENTER);
		inputMoneyL = new Label("입금금액", Label.CENTER);
		borrowMoneyL = new Label("대출금액", Label.CENTER);
		accountsListL = new Label("계좌목록", Label.CENTER);
		wonL = new Label("(단위 : 원)", Label.RIGHT);

		// 선택창
		typeChoice = new Choice();
		AccountType[] accountTypes = AccountType.values();
		for (AccountType accountType : accountTypes) {
			typeChoice.add(accountType.getName());
		}

		// 입력칸
		accountNumTF = new TextField();
		accountOwnerTF = new TextField();
		passwdTF = new TextField();
		inputMoneyTF = new TextField();
		borrowMoneyTF = new TextField();

		// 버튼
		numSearchBtn = new Button("조 회");
		deleteBtn = new Button("삭 제");
		ownerSearchBtn = new Button("검 색");
		addBtn = new Button("신규등록");
		allAccountsBtn = new Button("전체조회");

		// 메인출력창
		mainTA = new TextArea();

		grid = new GridBagLayout();
		con = new GridBagConstraints();
	}

	/**
	 * Init (2023-05-25 수정)
	 */
	public void init() {
		setLayout(grid);

		// 계좌 종류
		addCom(accountTypeL, 0, 0, 1, 1, 0.0);
		addCom(typeChoice, 1, 0, 1, 1, 0.0);

		// 계좌번호 & 조회 & 삭제
		addCom(accountNumL, 0, 1, 1, 1, 0.0);
		addCom(accountNumTF, 1, 1, 1, 1, 1.0);
		addCom(numSearchBtn, 2, 1, 1, 1, 0.0);
		addCom(deleteBtn, 3, 1, 1, 1, 0.0);

		// 예금주명 & 검색
		addCom(accountOwnerL, 0, 2, 1, 1, 0.0);
		addCom(accountOwnerTF, 1, 2, 1, 1, 1.0);
		addCom(ownerSearchBtn, 2, 2, 1, 1, 0.0);

		// 비밀번호 & 입금금액
		addCom(passwdL, 0, 3, 1, 1, 0.0);
		addCom(passwdTF, 1, 3, 1, 1, 1.0);
		addCom(inputMoneyL, 2, 3, 1, 1, 0.0);
		addCom(inputMoneyTF, 3, 3, 2, 1, 1.0);

		// 대출금액 & 신규등록 & 전체조회
		addCom(borrowMoneyL, 0, 4, 1, 1, 0.0);
		addCom(borrowMoneyTF, 1, 4, 1, 1, 1.0);
		addCom(addBtn, 2, 4, 1, 1, 0.0);
		addCom(allAccountsBtn, 3, 4, 1, 1, 0.0);

		// 계좌목록 & 단위
		addCom(accountsListL, 0, 5, 1, 1, 0.0);
		addCom(wonL, 4, 5, 1, 1, 0.0);

		// 메인출력창
		addCom(mainTA, 0, 6, 5, 4, 0.0);
	}

	/**
	 * Add (2023-05-25 수정)
	 * 
	 * @param c       컴포넌트
	 * @param x       x좌표
	 * @param y       y좌표
	 * @param w       가로값
	 * @param h       세로 값
	 * @param weightx 가중치
	 */
	public void addCom(Component c, int x, int y, int w, int h, double weightx) {
		con.fill = GridBagConstraints.BOTH;
		con.insets = new Insets(5, 5, 5, 5);
		con.gridx = x;
		con.gridy = y;
		con.gridwidth = w;
		con.gridheight = h;
		con.weightx = weightx;
		con.weighty = 0;
		grid.setConstraints(c, con);
		add(c);
	}

	/**
	 * 메인출력창 헤더 (2023-05-25) Formatter로 수정 (2023-05-27)
	 */
	@SuppressWarnings("resource")
	private void printHeader() {
		Formatter formatter = new Formatter();
		formatter.format("%1$10s%2$10s%3$10s%4$10s%5$20s%6$20s\n", "계좌종류", "계좌번호", "예금주명", "비밀번호", "잔액", "대출금액");

		String str = formatter.toString();

		mainTA.append("==========================================================================================\n");
		mainTA.append(str);
		mainTA.append("==========================================================================================\n");
	}

	/**
	 * 계좌 전체 목록 조회 및 메인출력창 출력 (2023-05-25 추가)
	 */
	public void allList() {
		mainTA.setText("");
		printHeader();
		List<Account> list = repository.getAccounts();
		for (Account account : list) {
			mainTA.append(account.toString() + "\n");
		}
	}

	/**
	 * 계좌 번호 조회 및 메인출력창 출력 (2023-05-25 추가)
	 */
	public void searchAccountNum() {
		String searchNum = accountNumTF.getText();
		Account account = repository.searchAccount(searchNum);
		mainTA.setText("");
		printHeader();
		if (account != null) {
			mainTA.append(account.toString());
			showDialog(searchNum + "번 계좌를 찾았습니다.");
		} else {
			allList(); // 계좌 전체 목록 메인출력창 출력
			showDialog(searchNum + "번 계좌는 존재하지 않습니다.");
		}
		resetTF(); // TextField 초기화
	}

	/**
	 * 예금주명 검색 및 메인출력창 출력 (2023-05-25 추가)
	 */
	public void serachAccountOwner() {
		int count = 0;
		String searchOwner = accountOwnerTF.getText();
		mainTA.setText(""); // 메인출력창 초기화
		printHeader();
		List<Account> accounts = repository.searchAccountByOwner(searchOwner);
		if (accounts != null) {
			for (Account account : accounts) {
				mainTA.append(account.toString() + "\n");
				count++;
			}
			showDialog(searchOwner + "님 계좌를 " + count + "개 찾았습니다.");
		} else {
			allList(); // 계좌 전체 목록 메인출력창 출력
			showDialog(searchOwner + "님 계좌를 찾을 수 없습니다.");
		}
		resetTF(); // TextField 초기화
	}

	/**
	 * 계좌번호로 계좌 삭제 (2023-05-25 추가)
	 */
	public void deleteAccount() {
		boolean result = false;
		String accountNum = accountNumTF.getText();
		result = repository.removeAccount(accountNum);
		if (result == true) {
			showDialog(accountNum + "번 계좌가 삭제 되었습니다.");
		} else {
			showDialog(accountNum + "번 계좌가 존재하지 않습니다.");
		}
		allList(); // 계좌 전체 목록 메인출력창 출력
		resetTF(); // TextField 초기화
	}

	/**
	 * 계좌 신규 등록 (2023-05-25 추가) 등록 시 계좌 종류 선택 안하면 메세지 출력 기능 추가 (2023-05-27)
	 */
	public void addAccount() {
		Account account = null;
		String accountOwner = accountOwnerTF.getText();
		int password = Integer.parseInt(passwdTF.getText());
		long inputMoney = Long.parseLong(inputMoneyTF.getText());
		String selectedItem = typeChoice.getSelectedItem();

		if (selectedItem.equals(AccountType.GENERAL_ACCOUNT.getName())) {
			account = new Account(accountOwner, password, inputMoney);
		} else if (selectedItem.equals(AccountType.MINUS_ACCONUT.getName())) {
			long borrowMoney = Long.parseLong(borrowMoneyTF.getText());
			account = new MinusAccount(accountOwner, password, inputMoney, borrowMoney);
		}

		repository.addAccount(account);

		showDialog("정상 등록 처리되었습니다.");
		resetTF(); // TextField 초기화
		allList(); // 계좌 전체 목록 메인출력창 출력
	}

	/**
	 * 계좌 종류 선택 (2023-05-25 추가)
	 * 
	 * @param accountType 계좌 종류
	 */
	public void selectAccountType(AccountType accountType) {

		switch (accountType) {

		case GENERAL_ACCOUNT:
			borrowMoneyTF.setEnabled(false);
			inputMoneyTF.setEnabled(true);
			break;

		case MINUS_ACCONUT:
			borrowMoneyTF.setEnabled(true);
			inputMoneyTF.setEnabled(true);
			inputMoneyTF.setText("0");
			break;

		case ALL_ACCOUNT:
			borrowMoneyTF.setEnabled(true);
			inputMoneyTF.setEnabled(true);
			break;
		}
	}

	/**
	 * showMessageDialog (2023-05-27 추가)
	 * 
	 * @param message
	 */
	public void showDialog(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	/**
	 * TextField 초기화 (2023-05-25 추가)
	 */
	public void resetTF() {
		accountNumTF.setText("");
		accountOwnerTF.setText("");
		passwdTF.setText("");
		inputMoneyTF.setText("");
		borrowMoneyTF.setText("");
	}

	/**
	 * 종료 버튼
	 */
	public void exit() {
		setVisible(false);
		dispose();
		System.exit(0);
	}

	public void addEventListner() {
		/**
		 * 버튼 클릭 시 동작 처리 (내부 클래스) 신규 등록 시 빈칸 있으면 알려주는 기능 추가 (2023-05-27)
		 */
		class ActionHandler implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object eventSource = e.getSource();
				// 신규등록
				if (eventSource == addBtn) {
					String selectedItem = typeChoice.getSelectedItem();

					if (selectedItem.equals(AccountType.ALL_ACCOUNT.getName())) {
						showDialog("계좌 종류를 선택해주세요.");
						typeChoice.requestFocus();
					} else if (!Validator.hasText(accountOwnerTF.getText())) {
						accountOwnerTF.setText("예금주명을 입력해주세요.");
						accountOwnerTF.requestFocus();
					} else if (!Validator.hasText(passwdTF.getText())) {
						passwdTF.setText("비밀번호를 입력해주세요.");
						passwdTF.requestFocus();
					} else if (!Validator.hasText(inputMoneyTF.getText())) {
						inputMoneyTF.setText("입금금액을 입력해주세요.");
						inputMoneyTF.requestFocus();
					} else if (Validator.isNumber(accountOwnerTF.getText())) {
						showDialog("예금주명은 숫자를 포함할 수 없습니다.");
						accountOwnerTF.setText("");
						accountOwnerTF.requestFocus();
					} else if (!Validator.isNumber(passwdTF.getText())) {
						showDialog("비밀번호는 숫자만 입력해주세요.");
						passwdTF.setText("");
						passwdTF.requestFocus();
					} else if (!Validator.isNumber(inputMoneyTF.getText())) {
						showDialog("입금금액은 숫자만 입력해주세요.");
						inputMoneyTF.setText("");
						inputMoneyTF.requestFocus();
					} else if (selectedItem.equals(AccountType.MINUS_ACCONUT.getName())
							&& !Validator.hasText(borrowMoneyTF.getText())) {
						borrowMoneyTF.setText("대출금액을 입력해주세요.");
						borrowMoneyTF.requestFocus();
					} else if (selectedItem.equals(AccountType.MINUS_ACCONUT.getName())
							&& !Validator.isNumber(borrowMoneyTF.getText())) {
						showDialog("대출금액은 숫자만 입력해주세요.");
						borrowMoneyTF.setText("");
						borrowMoneyTF.requestFocus();
					} else {
						addAccount();
					}

					// 전체조회
				} else if (eventSource == allAccountsBtn) {
					allList();

					// 계좌번호 검색
				} else if (eventSource == numSearchBtn) {
					if (!Validator.hasText(accountNumTF.getText())) {
						accountNumTF.setText("계좌번호를 입력해주세요.");
						accountNumTF.requestFocus();
					} else if (!Validator.isNumber(accountNumTF.getText())) {
						showDialog("계좌번호는 숫자만 입력해주세요.");
						accountNumTF.setText("");
						accountNumTF.requestFocus();
					} else {
						searchAccountNum();
					}

					// 계좌번호 삭제
				} else if (eventSource == deleteBtn) {
					if (!Validator.hasText(accountNumTF.getText())) {
						accountNumTF.setText("계좌번호를 입력해주세요.");
						accountNumTF.requestFocus();
					} else if (!Validator.isNumber(accountNumTF.getText())) {
						showDialog("계좌번호는 숫자만 입력해주세요.");
						accountNumTF.setText("");
						accountNumTF.requestFocus();
					} else {
						deleteAccount();
					}

					// 예금주명 검색
				} else if (eventSource == ownerSearchBtn) {
					if (!Validator.hasText(accountOwnerTF.getText())) {
						accountOwnerTF.setText("예금주명을 입력해주세요.");
						accountOwnerTF.requestFocus();
					} else if (Validator.isNumber(accountOwnerTF.getText())) {
						showDialog("예금주명은 숫자를 포함할 수 없습니다.");
						accountOwnerTF.setText("");
						accountOwnerTF.requestFocus();
					} else {
						serachAccountOwner();
					}
				}
			}
		}
		ActionListener actionListener = new ActionHandler();

		// 종료 처리
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});

		// 창이 열릴때
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				allList();
			}
		});

		// 계좌 등록
		addBtn.addActionListener(actionListener);

		// 예금주명 검색
		ownerSearchBtn.addActionListener(actionListener);

		// 계좌 삭제
		deleteBtn.addActionListener(actionListener);

		// 계좌번호 검색
		numSearchBtn.addActionListener(actionListener);

		// 전체계좌 조회
		allAccountsBtn.addActionListener(actionListener);

		// 계좌 선택
		typeChoice.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (typeChoice.getSelectedItem().equals("입출금계좌")) {
						selectAccountType(AccountType.GENERAL_ACCOUNT);
					} else if (typeChoice.getSelectedItem().equals("마이너스계좌")) {
						selectAccountType(AccountType.MINUS_ACCONUT);
					} else if (typeChoice.getSelectedItem().equals("전체계좌")) {
						selectAccountType(AccountType.ALL_ACCOUNT);
					}
				}
			}
		});
	}
}