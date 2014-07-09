package client;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.UIManager;

import server_client.User;

/**
 * 
 * @author tuS
 * @version %I%, %G%
 *
 */
public class LoginFrame extends JFrame
{
	private final Dimension FRAMESIZE;
	private JTextField txtflUsername;
	private JPasswordField txtflPassword;
	private final String FORGOTTEN_PW_LINK;
	private final String REGISTER_LINK;
	private JLabel jlbCapsNotification;
	private JLabel jlbWrongLoginNotification;
	private boolean capsLockOn;
	private Dimension textFieldDimension;
	private Dimension textLabelDimension;
	private final String strCapsNotification;
	private KeyboardListener keyListener;
	private LoginFrame loginframe;
	private DatabaseConnection dbCon;
	private JCheckBox jchbxRememberUsername;
	private int startX;
	private int startY;
	private UserSettingsClient usc;
	
	public LoginFrame()
	{
		super(JGSystem.NAME);
		this.setLayout(null);
		this.strCapsNotification = "RAGEMODE ON";
		this.FORGOTTEN_PW_LINK = "www.deineMutterDenktSieIstEinHobbitUndHeiﬂtFrodo.de";
		this.REGISTER_LINK = "http://guyforceshiswifetodressinagarbagebagforthenextthreeyears.com/";
		this.keyListener = new KeyboardListener();
		this.loginframe = this;
		this.startX = 85;
		this.startY = 100;
		this.dbCon = new DatabaseConnection();
		this.usc = UserSettingsClient.getInstance();
		
		this.textFieldDimension = new Dimension(200, 25);
		this.textLabelDimension = new Dimension(100, 25);
		this.FRAMESIZE = new Dimension(640, 480);
		this.setLayout(null);
		
		JLabel jlbTitel = new JLabel("JGame Collection");
		jlbTitel.setSize(200, 50);
		jlbTitel.setLocation(startX, startY-60);
		jlbTitel.setFont(JGSystem.FONT_BIG);
		jlbTitel.setForeground(JGSystem.COLOR_TEXT);
		this.add(jlbTitel);
		
		
		JLabel jlbUsername = new JLabel("Username");
		jlbUsername.setSize(textLabelDimension);
		jlbUsername.setLocation(startX ,startY);
		jlbUsername.setFont(JGSystem.FONT_SMALL);
		jlbUsername.setForeground(JGSystem.COLOR_TEXT);
		this.add(jlbUsername);
		
		txtflUsername = new JTextField(this.usc.getUsername());
		txtflUsername.addKeyListener(keyListener);
		txtflUsername.addFocusListener(new flTextFieldSelection(txtflUsername));
		txtflUsername.setSize(textFieldDimension);
		txtflUsername.setLocation(startX + 95, startY);
		txtflUsername.setFont(JGSystem.FONT_SMALL);
		txtflUsername.setForeground(JGSystem.COLOR_TEXT);
		this.add(txtflUsername);

		JLabel jlbPassword = new JLabel("Password");
		jlbPassword.setSize(textLabelDimension);
		jlbPassword.setLocation(startX ,startY + 35);
		jlbPassword.setFont(JGSystem.FONT_SMALL);
		jlbPassword.setForeground(JGSystem.COLOR_TEXT);
		this.add(jlbPassword);
		
		txtflPassword = new JPasswordField();
		txtflPassword.addKeyListener(keyListener);
		txtflPassword.addFocusListener(new flTextFieldSelection(txtflPassword));
		txtflPassword.setSize(textFieldDimension);
		txtflPassword.setLocation(startX + 95, startY + 35);
		txtflPassword.setForeground(JGSystem.COLOR_TEXT);
		this.add(txtflPassword);
		
		jlbWrongLoginNotification = new JLabel();
		jlbWrongLoginNotification.setSize(textFieldDimension);
		jlbWrongLoginNotification.setLocation(startX + 315, startY + (35/2));
		jlbWrongLoginNotification.setFont(JGSystem.FONT_ERROR);
		jlbWrongLoginNotification.setForeground(JGSystem.COLOR_ERROR);
		this.add(jlbWrongLoginNotification);
		
		
		jlbCapsNotification = new JLabel();
		this.checkOnCapslock();
		jlbCapsNotification.setSize(textLabelDimension);
		jlbCapsNotification.setLocation(startX + 95, startY + 70);
		jlbCapsNotification.setFont(JGSystem.FONT_WARN);
		jlbCapsNotification.setForeground(JGSystem.COLOR_WARN);
		this.add(jlbCapsNotification);
		
		jchbxRememberUsername = new JCheckBox("Remember Username");
		jchbxRememberUsername.setLocation(startX, startY + 105);
		jchbxRememberUsername.setSize(150, 25);
		jchbxRememberUsername.addKeyListener(keyListener);
		jchbxRememberUsername.setFont(JGSystem.FONT_SMALL);
		jchbxRememberUsername.setForeground(JGSystem.COLOR_TEXT);
		jchbxRememberUsername.setSelected(this.usc.isSavedUsername());
		this.add(jchbxRememberUsername);
		
		JButton jbttnLogin = new JButton("Login");
		jbttnLogin.setSize(new Dimension(80, 20));
		jbttnLogin.setLocation(startX + 215, startY + 105);
		jbttnLogin.addActionListener(new ALLogin());
		jbttnLogin.setFont(JGSystem.FONT_SMALL);
		jbttnLogin.setForeground(JGSystem.COLOR_TEXT);
		this.add(jbttnLogin);
		
		JSeparator jsprSeperator = new JSeparator();
		jsprSeperator.setLocation(startX, startY + 140);
		jsprSeperator.setSize(300, 5);
		this.add(jsprSeperator);
		
		JLabel jlbForgottenPW = new JLabel("Forgot your Password?");
		jlbForgottenPW.setSize(textFieldDimension);
		jlbForgottenPW.setLocation(startX, startY + 155);
		this.add(jlbForgottenPW);
		
		JLabel jlbForgottenPWLink = new JLabel("<html><a href=\"\">Click here!</a></html>");
		jlbForgottenPWLink.setSize(textFieldDimension);
		jlbForgottenPWLink.setLocation(startX + 200, startY + 155);
		jlbForgottenPWLink.addMouseListener(new MLLink(FORGOTTEN_PW_LINK));
		this.add(jlbForgottenPWLink);
		
		JLabel jlbRegister = new JLabel("New to the JGame Collection?");
		jlbRegister.setSize(textFieldDimension);
		jlbRegister.setLocation(startX, startY + 185);
		this.add(jlbRegister);
		
		JLabel jlbRegistryLink = new JLabel("<html><a href=\"\">Click here!</a></html>");
		jlbRegistryLink.setSize(textFieldDimension);
		jlbRegistryLink.setLocation(startX + 200, startY + 185);
		jlbRegistryLink.addMouseListener(new MLLink(REGISTER_LINK));
		this.add(jlbRegistryLink);
		

		this.getRootPane().setDefaultButton(jbttnLogin);
		this.addWindowListener(new WLLoginFrame());
		this.addKeyListener(keyListener);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(FRAMESIZE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public static void main(String args[])
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new LoginFrame();
	}
	
	private void login(){
		User user = dbCon.loginUser(new User(txtflUsername.getText(), txtflPassword.getPassword()));
		if(user.isValidLogin()){
			this.usc.setSavedUsername(jchbxRememberUsername.isSelected());
			if(jchbxRememberUsername.isSelected()){
				this.usc.setUsername(this.txtflUsername.getText());
			}else{
				this.usc.setUsername("");
			}
			loginframe.dispose();
			new MainFrame(user);
		}else{
			jlbWrongLoginNotification.setText("Login failed!");
		}
	}
	
	private void checkOnCapslock(){
		capsLockOn = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
		if(capsLockOn){
			jlbCapsNotification.setText(strCapsNotification);
		}else{
			jlbCapsNotification.setText("");
		}
	}
	
	private class MLLink implements MouseListener{

		private String uri = "www.google.de";
		
		public MLLink(String uri){
			this.uri = uri;
		}
		
		
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			try {
				URI uriRegistryLink = new URI(uri);
				Desktop.getDesktop().browse(uriRegistryLink);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
		
	}
	
	private class WLLoginFrame implements WindowListener{

		@Override
		public void windowActivated(WindowEvent e) {
			checkOnCapslock();
			
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
		}
	}

	private class flTextFieldSelection implements FocusListener{
		private JTextField txtfield;
		
		public flTextFieldSelection(JTextField txtfield){
			this.txtfield = txtfield;
		}
		
		@Override
		public void focusGained(FocusEvent arg0) {
			txtfield.selectAll();
			
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class ALLogin implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			login();
		}
	}
	
	private class KeyboardListener implements KeyListener{
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_CAPS_LOCK:
				checkOnCapslock();
				break;
			default:
				break;
			}
		}
		@Override
		public void keyReleased(KeyEvent e) {

		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}	
	}

}