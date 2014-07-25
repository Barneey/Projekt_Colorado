package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.thoughtworks.xstream.security.ForbiddenClassException;

import server_client.User;
import sun.security.krb5.internal.crypto.CksumType;

public class RequestNicknameFrame extends JDialog{
	
	private JTextField jtxtfInsertNick;
	private User user;
	private DatabaseConnection dbCon;
	private JLabel jlblNickTaken;
	private final int NICK_LENGTH = 18;
	
	public RequestNicknameFrame(User user){
		super();
		setTitle("Missing nickname");
		setLayout(null);
		
		this.dbCon = DatabaseConnection.getInstance();
		this.user = user;
		JLabel jlblInsertNick = new JLabel(" Please enter your Jamertag:");
		jlblInsertNick.setSize(175, 25);
		jlblInsertNick.setLocation(50, 5);
		add(jlblInsertNick);
		
		jtxtfInsertNick = new JTextField();
		jtxtfInsertNick.setDocument(new LengthRestrictedDocument(NICK_LENGTH));
		jtxtfInsertNick.setText("Jamertag");
		jtxtfInsertNick.addFocusListener(new FLInsertNickSelect());
		jtxtfInsertNick.setSize(175, 25);
		jtxtfInsertNick.setLocation(50, 35);
		add(jtxtfInsertNick);
		
		JButton JbttnOkay = new JButton("Okay");
		JbttnOkay.addActionListener(new ALNicknameOkay());
		JbttnOkay.setSize(70, 25);
		JbttnOkay.setLocation(103, 65);
		add(JbttnOkay);
		
		jlblNickTaken = new JLabel();
		jlblNickTaken.setSize(175, 25);
		jlblNickTaken.setLocation(50, 100);
		jlblNickTaken.setFont(JGSystem.FONT_WARN);
		jlblNickTaken.setForeground(JGSystem.COLOR_WARN);
		add(jlblNickTaken);
		
		getRootPane().setDefaultButton(JbttnOkay);
		setModal(true);
		setResizable(false);
		addWindowListener(new WLNickFrame());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(250, 150);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private class FLInsertNickSelect implements FocusListener{

		@Override
		public void focusGained(FocusEvent arg0) {
			jtxtfInsertNick.selectAll();
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private boolean checkNickname(String nickname){
		return !nickname.matches("(.*)[\",-\\.\'#/\\ ´`](.*)");
	}
	
	private void showErrorMessage(String message){
		jlblNickTaken.setText(message);
		while(getHeight() < 200){
			setSize(getWidth(), getHeight() + 1);
			try {
				wait(80);	
			} catch (Exception e) {
				// e.printStackTrace();
				// TODO: handle exception
			}
		}
	}
	
	private class ALNicknameOkay implements ActionListener{
		
		public void actionPerformed(ActionEvent ae){
			String nickname = jtxtfInsertNick.getText();
			if(nickname.length() <= 4){
				showErrorMessage(nickname + " is to short"); 
			}
			if(!checkNickname(nickname)){
				showErrorMessage(nickname + " contains illegal characters");
			}else if(dbCon.validateUniqueNickname(nickname)){
				user.setNick(nickname);
				dbCon.updateUser(user, DatabaseConnection.LOGIN_PORT);
				setVisible(false);
				dispose();
			}else{
				showErrorMessage(nickname + " is already taken! :(");
				}
			}
		}
	
	private class WLNickFrame implements WindowListener{


	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		JGSystem.getInstance().exit(user);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}		
	}

}