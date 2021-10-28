package metro;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URI;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class View extends JFrame implements ItemListener{

	private static final long serialVersionUID = 1L;
	static int currAudChan, currVidChan = 0;
	boolean flagAudio, flagVideo, flagSpeed, flagFirefox, flagMovMode,
	flagLights, flagDoors, flagFans, flagSpeakerInUseByTV,
	flagSpeakerInUseByRad = false;
	int numRunProc = 3;
	int semaphore  = 1;
	int numWaitProc = 0;
	int numRunThrd = 0;
	int numWebThreads = 0;
	//int cpu = numRunProc + numRunThrd;
	//int mem = numRunProc + numWaitProc + numRunThrd;
	private JPanel contentPane;
	private JTextField textFieldURL;
	private JPanel panelSpeed1;
	private JTextField txtRunProc;
	private JTextField txtRunThreads;
	private JTextField txtWaitngProc;
	private JTextField txtSemaphore;
	private JTextField txtNumRunProc;
	private JTextField txtNumWaitPrc;
	private JTextField txtNumRunThread;
	private JTextField txtNumSema;
	private JProgressBar progressBarCpuUsage;
	private JProgressBar progressBarMemUsage;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					View frame = new View();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public View() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1073, 595);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.GRAY);
		
		JPanel panelMovMode = new JPanel();
		panelMovMode.setBackground(Color.GRAY);
		
		JPanel panelSpeed = new JPanel();
		panelSpeed.setBackground(Color.GRAY);
		
		JPanel panelLights = new JPanel();
		panelLights.setBackground(Color.GRAY);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBackground(Color.GRAY);
		
		JPanel panel_7 = new JPanel();
		panel_7.setBackground(Color.GRAY);
		panel_7.setLayout(new GridLayout(0, 1, 0, 0));
		
		//     -----------   Some buttons   ------------
		JProgressBar progressBarSpeed = new JProgressBar();
		JButton btnTvPower = new JButton("On/Off");
		JButton btnRadPower = new JButton("On/Off");
		JButton btnInUseResource = new JButton("Curr Used resource");
		JButton btnRadNextChan = new JButton("Next Chan");
		btnRadNextChan.setBackground(new Color(176, 224, 230));
		btnRadNextChan.setEnabled(false);
		JButton btnRadPrevChan = new JButton("Prev Chan");
		btnRadPrevChan.setBackground(new Color(221, 160, 221));
		btnRadPrevChan.setEnabled(false);
		JButton btnTvNextChan = new JButton("Next Chan");
		btnTvNextChan.setBackground(new Color(176, 224, 230));
		btnTvNextChan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(currVidChan+1 < 6)
					currVidChan ++;
				else
					currVidChan = 0;
				AudioVideoPlayer player = new AudioVideoPlayer("Video");
				player.start();
			}
		});
		btnTvNextChan.setEnabled(false);
		JButton btnTvPrevChan = new JButton("Prev Chan");
		btnTvPrevChan.setBackground(new Color(221, 160, 221));
		btnTvPrevChan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(currVidChan-1 > -1)
					currVidChan --;
				else
					currVidChan = 5;
				AudioVideoPlayer player = new AudioVideoPlayer("Video");
				player.start();	
			}
		});
		btnTvPrevChan.setEnabled(false);
	
		
		JLabel lblTvBoard = new JLabel("TV Board");
		lblTvBoard.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblTvBoard.setHorizontalAlignment(SwingConstants.CENTER);
		panel_7.add(lblTvBoard);
		
		
		btnTvPower.setBackground(new Color(250, 128, 114));
		btnTvPower.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				if(flagSpeakerInUseByTV == false) {
					flagVideo = !flagVideo;
					AudioVideoPlayer player = new AudioVideoPlayer("Video");
					if (flagVideo == true) {
						btnTvPower.setBackground(new Color(0, 255, 127));
						btnInUseResource.setBackground(new Color(255, 215, 0));
						btnTvNextChan.setEnabled(true);
						btnTvPrevChan.setEnabled(true);					
						player.start();
						flagSpeakerInUseByRad = true;
						numRunThrd ++;
						refresh();
					}
					else {
						btnTvPower.setBackground(new Color(250, 128, 114));
						btnTvNextChan.setEnabled(false);
						btnTvPrevChan.setEnabled(false);
						player.kill("Video");
						flagSpeakerInUseByRad = false;
						numRunThrd --;
						btnInUseResource.setBackground(Color.green);
						if(semaphore == 0) {
							semaphore = 1;
							numWaitProc = 0;
							btnRadPower.doClick();
						}
						refresh();
					}
				}else {
					semaphore = 0;
					numWaitProc = 1;
					refresh();
					btnInUseResource.setBackground(Color.red);
				}
			}
		});
		panel_7.add(btnTvPower);
		
		panel_7.add(btnTvNextChan);
		
		panel_7.add(btnTvPrevChan);
		
		JPanel panelWeb = new JPanel();
		panelWeb.setBackground(Color.GRAY);
		panelWeb.setVisible(false);
		
		JPanel Control = new JPanel();
		Control.setBackground(Color.BLACK);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(panelSpeed, GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
						.addComponent(panelWeb, GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(panelLights, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panel_6, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(panelMovMode, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panel_7, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(Control, GroupLayout.PREFERRED_SIZE, 278, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(Control, 0, 0, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(panelSpeed, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panelWeb, GroupLayout.PREFERRED_SIZE, 230, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(panelLights, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
								.addComponent(panelMovMode, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 216, GroupLayout.PREFERRED_SIZE)
								.addComponent(panel_7, GroupLayout.PREFERRED_SIZE, 216, GroupLayout.PREFERRED_SIZE)))
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 383, GroupLayout.PREFERRED_SIZE))
					.addGap(164))
		);
		
		JPanel panel_12 = new JPanel();
		panel_12.setBackground(new Color(0, 0, 0));
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.BLACK);
		GroupLayout gl_Control = new GroupLayout(Control);
		gl_Control.setHorizontalGroup(
			gl_Control.createParallelGroup(Alignment.LEADING)
				.addComponent(panel_12, GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
				.addGroup(gl_Control.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 276, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_Control.setVerticalGroup(
			gl_Control.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_Control.createSequentialGroup()
					.addComponent(panel_12, GroupLayout.PREFERRED_SIZE, 206, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE))
		);
		panel.setLayout(new GridLayout(0, 2, 2, 2));
		
		txtRunProc = new JTextField();
		txtRunProc.setFont(new Font("Times New Roman", Font.BOLD, 15));
		txtRunProc.setText("Running Processes");
		panel.add(txtRunProc);
		txtRunProc.setColumns(30);
		
		txtNumRunProc = new JTextField();
		txtNumRunProc.setText(numRunProc+"");
		txtNumRunProc.setFont(new Font("Times New Roman", Font.BOLD, 25));
		txtNumRunProc.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(txtNumRunProc);
		txtNumRunProc.setColumns(10);
		
		txtWaitngProc = new JTextField();
		txtWaitngProc.setText("Waiting Processes");
		txtWaitngProc.setFont(new Font("Times New Roman", Font.BOLD, 15));
		panel.add(txtWaitngProc);
		txtWaitngProc.setColumns(20);
		
		txtNumWaitPrc = new JTextField(numWaitProc+"");
		txtNumWaitPrc.setFont(new Font("Times New Roman", Font.BOLD, 25));
		txtNumWaitPrc.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(txtNumWaitPrc);
		txtNumWaitPrc.setColumns(10);
		
		txtRunThreads = new JTextField();
		txtRunThreads.setFont(new Font("Times New Roman", Font.BOLD, 15));
		txtRunThreads.setText("Running Threads");
		panel.add(txtRunThreads);
		txtRunThreads.setColumns(30);
		
		txtNumRunThread = new JTextField();
		txtNumRunThread.setText(numRunThrd+"");
		txtNumRunThread.setFont(new Font("Times New Roman", Font.BOLD, 25));
		txtNumRunThread.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(txtNumRunThread);
		txtNumRunThread.setColumns(10);
		
		txtSemaphore = new JTextField();
		txtSemaphore.setText("Semaphore");
		txtSemaphore.setFont(new Font("Times New Roman", Font.BOLD, 15));
		panel.add(txtSemaphore);
		txtSemaphore.setColumns(20);
		
		txtNumSema = new JTextField();
		txtNumSema.setText(semaphore+"");
		txtNumSema.setFont(new Font("Times New Roman", Font.BOLD, 25));
		txtNumSema.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(txtNumSema);
		txtNumSema.setColumns(10);
		panel_12.setLayout(new GridLayout(3, 2, 2, 2));
		
		JButton btnConFirefox = new JButton("FireFox");
		btnConFirefox.setFont(new Font("Times New Roman", Font.BOLD, 15));
		btnConFirefox.setBackground(new Color(255, 165, 0));
		btnConFirefox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				flagFirefox = !flagFirefox;
				setEnableRec(panelWeb, flagFirefox);
				if(flagFirefox) {
					panelWeb.setVisible(true);
					numRunProc ++;
					refresh();
				}
				else {
					panelWeb.setVisible(false);
					numRunProc --;
					numRunThrd = numRunThrd-numWebThreads;
					numWebThreads = 0;
					refresh();
//					try {
//						Runtime.getRuntime().exec("taskkill /IM firefox.exe");
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
				}
			}
		});
		panel_12.add(btnConFirefox);
		
		JButton btnConLight = new JButton("Light Control");
		btnConLight.setFont(new Font("Times New Roman", Font.BOLD, 15));
		btnConLight.setBackground(new Color(255, 215, 0));
		btnConLight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				flagLights = !flagLights;
				setEnableRec(panelLights, flagLights);
				if (flagLights) {					
					numRunProc ++;
					refresh();
				}else {
					numRunProc --;
					refresh();
				}	
			}
		});
		panel_12.add(btnConLight);
		
		JButton btnConDoors = new JButton("Doors Control");
		btnConDoors.setFont(new Font("Times New Roman", Font.BOLD, 15));
		btnConDoors.setBackground(new Color(139, 69, 19));
		
		panel_12.add(btnConDoors);
		
		JButton btnConFans = new JButton("Fans Control");
		btnConFans.setFont(new Font("Times New Roman", Font.BOLD, 15));
		
		btnConFans.setBackground(new Color(30, 144, 255));
		panel_12.add(btnConFans);
		
		JButton btnConEngineOnOff = new JButton("Engine On/Off");
		btnConEngineOnOff.setFont(new Font("Times New Roman", Font.BOLD, 15));
		btnConEngineOnOff.setForeground(new Color(255, 255, 255));
		btnConEngineOnOff.setBackground(new Color(128, 0, 0));
		btnConEngineOnOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				flagSpeed =  !flagSpeed;
				flagMovMode= !flagMovMode;
				setEnableRec(panelSpeed, flagSpeed);
				setEnableRec(panelMovMode, flagMovMode);
				if(flagSpeed) {
					numRunProc ++;
					numRunProc ++;
				}
				else {
					numRunProc --;
					numRunProc --;
					progressBarSpeed.setValue(0);
				}
				refresh();
			}
		});
		
		JButton btnConSystemInfo = new JButton("System Info");
		btnConSystemInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = "Simple simulation for a real-life Operating System. \n \n" +
						"Number of Running Processes: " + numRunProc + ".\n"+
						"Number of Running Threads: " + numRunThrd +".\n" + 
						"Number of Waiting Processes: " + numWaitProc + ".\n" +
						"Semaphore: " + semaphore + ".";
				
				JOptionPane.showMessageDialog(null, message, "System Info", JOptionPane.PLAIN_MESSAGE);
			}
		});
		btnConSystemInfo.setFont(new Font("Times New Roman", Font.BOLD, 15));
		btnConSystemInfo.setBackground(new Color(186, 85, 211));
		panel_12.add(btnConSystemInfo);
		panel_12.add(btnConEngineOnOff);
		Control.setLayout(gl_Control);
		
		JPanel panel_10 = new JPanel();
		
		JLabel lblEnterTheUrl = new JLabel("Enter The URL Then Press Go");
		lblEnterTheUrl.setFont(new Font("Times New Roman", Font.BOLD, 17));
		
		textFieldURL = new JTextField();
		textFieldURL.setColumns(10);
		
		JButton btnURLSearch = new JButton("Go");
		btnURLSearch.setForeground(Color.WHITE);
		btnURLSearch.setBackground(new Color(107, 142, 35));
		btnURLSearch.setEnabled(false);
		btnURLSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(textFieldURL.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please Enter an address first", "Empty Field",
							JOptionPane.INFORMATION_MESSAGE);
				}else {
					String s = textFieldURL.getText();
					URI f;
					try {
						f = new URI(s);
						Desktop d = Desktop.getDesktop(); 
			            d.browse(f);
			            numWebThreads ++;
			            numRunThrd ++;
			            refresh();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		GroupLayout gl_panelWeb = new GroupLayout(panelWeb);
		gl_panelWeb.setHorizontalGroup(
			gl_panelWeb.createParallelGroup(Alignment.LEADING)
				.addComponent(panel_10, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(gl_panelWeb.createSequentialGroup()
					.addContainerGap()
					.addComponent(textFieldURL, GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnURLSearch)
					.addContainerGap())
				.addGroup(gl_panelWeb.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblEnterTheUrl, GroupLayout.PREFERRED_SIZE, 288, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panelWeb.setVerticalGroup(
			gl_panelWeb.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelWeb.createSequentialGroup()
					.addComponent(panel_10, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblEnterTheUrl, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelWeb.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnURLSearch, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(textFieldURL, GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
					.addContainerGap(14, Short.MAX_VALUE))
		);
		panel_10.setLayout(new GridLayout(2, 3, 0, 0));
		
		JButton btnFacebook = new JButton("FaceBook");
		btnFacebook.setEnabled(false);
		btnFacebook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
					URI f;
					try {
						f = new URI("www.facebook.com");
						Desktop d = Desktop.getDesktop(); 
			            d.browse(f);
			            numWebThreads ++;
			            numRunThrd ++;
			            refresh();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				
			}
		});
		btnFacebook.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnFacebook.setBackground(new Color(25, 25, 112));
		btnFacebook.setForeground(new Color(255, 255, 255));
		panel_10.add(btnFacebook);
		
		JButton btnTwitter = new JButton("twitter");
		btnTwitter.setEnabled(false);
		btnTwitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				URI t;
				try {
					t = new URI("https://twitter.com");
					Desktop d = Desktop.getDesktop(); 
		            d.browse(t); 
		            numWebThreads ++;
		            numRunThrd ++;
		            refresh();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnTwitter.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnTwitter.setBackground(new Color(32, 178, 170));
		btnTwitter.setForeground(new Color(255, 255, 255));
		panel_10.add(btnTwitter);
		
		JButton btnAskfm = new JButton("Ask.fm");
		btnAskfm.setEnabled(false);
		btnAskfm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				URI f;
				try {
					f = new URI("https://ask.fm");
					Desktop d = Desktop.getDesktop(); 
		            d.browse(f); 
		            numWebThreads ++;
		            numRunThrd ++;
		            refresh();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnAskfm.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnAskfm.setBackground(new Color(139, 0, 0));
		btnAskfm.setForeground(new Color(255, 255, 255));
		panel_10.add(btnAskfm);
		
		JButton btnGoogle = new JButton("Google");
		btnGoogle.setEnabled(false);
		btnGoogle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				URI f;
				try {
					f = new URI("www.google.com");
					Desktop d = Desktop.getDesktop(); 
		            d.browse(f); 
		            numWebThreads ++;
		            numRunThrd ++;
		            refresh();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnGoogle.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnGoogle.setBackground(new Color(128, 128, 128));
		btnGoogle.setForeground(new Color(255, 255, 255));
		panel_10.add(btnGoogle);
		
		JButton btnNewButton = new JButton("Yahoo");
		btnNewButton.setEnabled(false);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				URI f;
				try {
					f = new URI("www.yahoo.com");
					Desktop d = Desktop.getDesktop(); 
		            d.browse(f); 
		            numWebThreads ++;
		            numRunThrd ++;
		            refresh();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnNewButton.setBackground(new Color(139, 0, 139));
		btnNewButton.setForeground(new Color(255, 255, 255));
		panel_10.add(btnNewButton);
		
		JButton btnGucMail = new JButton("GUC mail");
		btnGucMail.setEnabled(false);
		btnGucMail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				URI f;
				try {
					f = new URI("https://mail.guc.edu.eg");
					Desktop d = Desktop.getDesktop(); 
		            d.browse(f); 
		            numWebThreads ++;
		            numRunThrd ++;
		            refresh();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnGucMail.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnGucMail.setBackground(new Color(255, 140, 0));
		btnGucMail.setForeground(new Color(255, 255, 255));
		panel_10.add(btnGucMail);
		panelWeb.setLayout(gl_panelWeb);
		panel_6.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblRadioBoard = new JLabel("Radio Board");
		lblRadioBoard.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblRadioBoard.setHorizontalAlignment(SwingConstants.CENTER);
		panel_6.add(lblRadioBoard);
		
		
		btnRadPower.setBackground(new Color(250, 128, 114));
		btnRadPower.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(flagSpeakerInUseByRad == false) {
					flagAudio = !flagAudio;
					AudioVideoPlayer player = new AudioVideoPlayer("Audio");
					if (flagAudio == true) {
						btnRadPower.setBackground(new Color(0, 255, 127));
						btnInUseResource.setBackground(new Color(255, 215, 0));
						btnRadNextChan.setEnabled(true);
						btnRadPrevChan.setEnabled(true);
						player.start();
						flagSpeakerInUseByTV = true;
						numRunThrd ++;
						refresh();
					}
					else {
						btnRadPower.setBackground(new Color(250, 128, 114));
						btnRadNextChan.setEnabled(false);
						btnRadPrevChan.setEnabled(false);
						player.kill("Audio");
						flagSpeakerInUseByTV = false;
						btnInUseResource.setBackground(Color.green);
						numRunThrd --;
						if(semaphore == 0) {
							semaphore = 1;
							numWaitProc = 0;
							btnTvPower.doClick();
						}
						refresh();
					}
				}else {
					semaphore = 0;
					numWaitProc = 1;
					refresh();
					btnInUseResource.setBackground(Color.red);
				}
			}
		});
		panel_6.add(btnRadPower);
		
		btnRadNextChan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(currAudChan+1 < 6)
					currAudChan ++;
				else
					currAudChan = 0;
				AudioVideoPlayer player = new AudioVideoPlayer("Audio");
				player.start();
			}
		});
		panel_6.add(btnRadNextChan);
		
		btnRadPrevChan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(currAudChan-1 > -1)
					currAudChan --;
				else
					currAudChan = 5;
				AudioVideoPlayer player = new AudioVideoPlayer("Audio");
				player.start();				
			}
		});
		panel_6.add(btnRadPrevChan);
		panelLights.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblLightBoard = new JLabel("Light Board");
		lblLightBoard.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblLightBoard.setHorizontalAlignment(SwingConstants.CENTER);
		panelLights.add(lblLightBoard);
		
		JCheckBox checkBoxLightsFront = new JCheckBox("Front Lights");
		checkBoxLightsFront.setEnabled(false);
		checkBoxLightsFront.setBackground(Color.LIGHT_GRAY);
		checkBoxLightsFront.setFont(new Font("Times New Roman", Font.BOLD, 16));
		panelLights.add(checkBoxLightsFront);
		
		JCheckBox chckbxLightBack = new JCheckBox("Back  Lights");
		chckbxLightBack.setEnabled(false);
		chckbxLightBack.setBackground(Color.LIGHT_GRAY);
		chckbxLightBack.setFont(new Font("Times New Roman", Font.BOLD, 16));
		panelLights.add(chckbxLightBack);
		
		JCheckBox chckbxLightCars = new JCheckBox("Cars  Lights");
		chckbxLightCars.setEnabled(false);
		chckbxLightCars.setBackground(Color.LIGHT_GRAY);
		chckbxLightCars.setFont(new Font("Times New Roman", Font.BOLD, 16));
		panelLights.add(chckbxLightCars);
		
		JCheckBox chckbxLightDriver = new JCheckBox("Driver Lights");
		chckbxLightDriver.setEnabled(false);
		chckbxLightDriver.setBackground(Color.LIGHT_GRAY);
		chckbxLightDriver.setFont(new Font("Times New Roman", Font.BOLD, 16));
		panelLights.add(chckbxLightDriver);
		
		JPanel panelSpeed2 = new JPanel();
		
		panelSpeed1 = new JPanel();
		panelSpeed1.setBackground(Color.GRAY);
		
		JLabel lblNewLabel_1 = new JLabel("Speed");
		lblNewLabel_1.setForeground(Color.BLACK);
		lblNewLabel_1.setFont(new Font("Plantagenet Cherokee", Font.BOLD, 16));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		progressBarSpeed.setToolTipText("Speed");
		progressBarSpeed.setForeground(Color.BLACK);
		progressBarSpeed.setStringPainted(true);
		GroupLayout gl_panelSpeed1 = new GroupLayout(panelSpeed1);
		gl_panelSpeed1.setHorizontalGroup(
			gl_panelSpeed1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelSpeed1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(progressBarSpeed, GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
					.addGap(16))
		);
		gl_panelSpeed1.setVerticalGroup(
			gl_panelSpeed1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelSpeed1.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_panelSpeed1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
						.addComponent(progressBarSpeed, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
					.addGap(30))
		);
		panelSpeed1.setLayout(gl_panelSpeed1);
		panelSpeed2.setLayout(new GridLayout(0, 2, 0, 0));
		
		JButton btnSpeedDec = new JButton("Decrease");
		btnSpeedDec.setForeground(new Color(255, 255, 255));
		btnSpeedDec.setFont(new Font("Times New Roman", Font.BOLD, 17));
		btnSpeedDec.setBackground(new Color(0, 128, 128));
		btnSpeedDec.setEnabled(false);
		btnSpeedDec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int speed = progressBarSpeed.getValue() - 5;			
				progressBarSpeed.setValue(speed);
				refreshProgressBar(progressBarSpeed,"Normal");
				refresh();
			}

		});
		panelSpeed2.add(btnSpeedDec);
		
		JButton btnSpeedInc = new JButton("Increase");
		btnSpeedInc.setForeground(new Color(255, 255, 255));
		btnSpeedInc.setFont(new Font("Times New Roman", Font.BOLD, 17));
		btnSpeedInc.setBackground(new Color(0, 128, 128));
		btnSpeedInc.setEnabled(false);
		btnSpeedInc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int speed = progressBarSpeed.getValue() + 5;			
				progressBarSpeed.setValue(speed);
				refreshProgressBar(progressBarSpeed, "Normal");
				refresh();
			}
		});
		panelSpeed2.add(btnSpeedInc);
		panelSpeed.setLayout(new GridLayout(2, 1, 0, 0));
		panelSpeed.add(panelSpeed1);
		panelSpeed.add(panelSpeed2);
		panelMovMode.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblModeBoard = new JLabel("Mode Board");
		lblModeBoard.setHorizontalAlignment(SwingConstants.CENTER);
		lblModeBoard.setFont(new Font("Times New Roman", Font.BOLD, 16));
		panelMovMode.add(lblModeBoard);
		
		JRadioButton rdbtn_PMod = new JRadioButton("P   Mode");
		rdbtn_PMod.setEnabled(false);
		rdbtn_PMod.setFont(new Font("Plantagenet Cherokee", Font.BOLD, 16));
		rdbtn_PMod.setBackground(Color.LIGHT_GRAY);
		rdbtn_PMod.setSelected(true);
		rdbtn_PMod.setHorizontalAlignment(SwingConstants.LEFT);
		panelMovMode.add(rdbtn_PMod);
		
		JRadioButton rdbtn_NMod = new JRadioButton("N   Mode");
		rdbtn_NMod.setEnabled(false);
		rdbtn_NMod.setFont(new Font("Plantagenet Cherokee", Font.BOLD, 16));
		rdbtn_NMod.setBackground(Color.LIGHT_GRAY);
		rdbtn_NMod.setHorizontalAlignment(SwingConstants.LEFT);
		panelMovMode.add(rdbtn_NMod);
		
		JRadioButton rdbtn_DMod = new JRadioButton("D   Mode");
		rdbtn_DMod.setEnabled(false);
		rdbtn_DMod.setFont(new Font("Plantagenet Cherokee", Font.BOLD, 16));
		rdbtn_DMod.setBackground(Color.LIGHT_GRAY);
		rdbtn_DMod.setHorizontalAlignment(SwingConstants.LEFT);
		panelMovMode.add(rdbtn_DMod);
		
		JRadioButton rdbtn_RMod = new JRadioButton("R   Mode");
		rdbtn_RMod.setEnabled(false);
		rdbtn_RMod.setFont(new Font("Plantagenet Cherokee", Font.BOLD, 16));
		rdbtn_RMod.setBackground(Color.LIGHT_GRAY);
		rdbtn_RMod.setHorizontalAlignment(SwingConstants.LEFT);
		panelMovMode.add(rdbtn_RMod);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.GRAY);
		
		JLabel lblNewLabel = new JLabel("Sensors Board");
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Plantagenet Cherokee", Font.BOLD, 16));
		
		JLabel lblCpuUsage = new JLabel("CPU Usage");
		lblCpuUsage.setHorizontalAlignment(SwingConstants.LEFT);
		lblCpuUsage.setForeground(Color.BLACK);
		lblCpuUsage.setFont(new Font("Plantagenet Cherokee", Font.BOLD, 13));
		
		progressBarCpuUsage = new JProgressBar();
		progressBarCpuUsage.setValue(numRunProc + numRunThrd);
		//progressBarCpuUsage.setBackground(Color.LIGHT_GRAY);
		progressBarCpuUsage.setMaximum(20);
		
		JLabel lblMemoryUsage = new JLabel("Memory Usage");
		lblMemoryUsage.setHorizontalAlignment(SwingConstants.LEFT);
		lblMemoryUsage.setForeground(Color.BLACK);
		lblMemoryUsage.setFont(new Font("Plantagenet Cherokee", Font.BOLD, 13));
		
		progressBarMemUsage = new JProgressBar();
		progressBarMemUsage.setValue(numRunProc*2 + numWaitProc + numRunThrd + numWebThreads);
		progressBarMemUsage.setMaximum(20);
		
		//progressBarMemUsage.setBackground(Color.LIGHT_GRAY);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblMemoryUsage, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(41, Short.MAX_VALUE))
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(progressBarMemUsage, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(13, Short.MAX_VALUE))
				.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(panel_2, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
						.addComponent(progressBarCpuUsage, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
						.addComponent(lblCpuUsage, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(13, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 197, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(lblCpuUsage)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(progressBarCpuUsage, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblMemoryUsage, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(progressBarMemUsage, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_2.setLayout(new GridLayout(0, 1, 0, 0));
		
		JButton btnSenDoors = new JButton("Doors");
		btnSenDoors.setFont(new Font("Times New Roman", Font.BOLD, 13));
		btnSenDoors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnSenDoors.setBackground(Color.GREEN);
		panel_2.add(btnSenDoors);
		
		JButton btnSenFans = new JButton("Fans");
		btnSenFans.setFont(new Font("Times New Roman", Font.BOLD, 13));
		btnSenFans.setBackground(Color.GREEN);
		panel_2.add(btnSenFans);
		
		JButton btnSenTemp = new JButton("Temprature");
		btnSenTemp.setFont(new Font("Times New Roman", Font.BOLD, 13));
		btnSenTemp.setBackground(Color.GREEN);
		panel_2.add(btnSenTemp);
		
		JButton btnTraffic = new JButton("Low Battery");
		btnTraffic.setFont(new Font("Times New Roman", Font.BOLD, 13));
		btnTraffic.setBackground(Color.GREEN);
		panel_2.add(btnTraffic);
		
		btnInUseResource.setFont(new Font("Times New Roman", Font.BOLD, 12));
		btnInUseResource.setBackground(Color.GREEN);
		panel_2.add(btnInUseResource);
		panel_1.setLayout(gl_panel_1);
		contentPane.setLayout(gl_contentPane);
		
		ButtonGroup btnGroupMod = new ButtonGroup();
		btnGroupMod.add(rdbtn_DMod);
		btnGroupMod.add(rdbtn_NMod);
		btnGroupMod.add(rdbtn_PMod);
		btnGroupMod.add(rdbtn_RMod);
		
		//  --------------  Action listners for some buttons
		
		btnConDoors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				flagDoors = !flagDoors;
				if(flagDoors) {
					btnSenDoors.setBackground(Color.red);
				}else {
					btnSenDoors.setBackground(Color.green);
					}
			}
		});
		
		btnConFans.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				flagFans = !flagFans;
				if(flagFans) {
					btnSenFans.setBackground(Color.red);
				}else {
					btnSenFans.setBackground(Color.green);
					}
			}
		});
		
		// jradio Buttons Action lisnter
		rdbtn_DMod.addItemListener(this);
		rdbtn_NMod.addItemListener(this);
		rdbtn_PMod.addItemListener(this);
		rdbtn_RMod.addItemListener(this);
			}
			
	//  ---------------       helping methods     ------------------- //
	
	public void itemStateChanged(ItemEvent e) {
	    if (e.getStateChange() == ItemEvent.SELECTED) {
	        // Your selected code here.
	    }
	    else if (e.getStateChange() == ItemEvent.DESELECTED) {
	        // Your deselected code here.
	    }
	}
	
	private void setEnableRec(Component container, boolean enable){
	    container.setEnabled(enable);

	    try {
	        Component[] components= ((Container) container).getComponents();
	        for (int i = 0; i < components.length; i++) {
	            setEnableRec(components[i], enable);
	        }
	    } catch (ClassCastException e) {

	    }
	}
	
	private void refreshTxtFields() {
		txtNumRunProc.setText(numRunProc+"");
		txtNumRunThread.setText(numRunThrd+"");
		txtNumSema.setText(semaphore+"");
		txtNumWaitPrc.setText(numWaitProc+"");
		progressBarCpuUsage.setValue(numRunProc + numRunThrd);
		//refreshProgressBar(progressBarCpuUsage, "Normal");
		progressBarMemUsage.setValue(numRunProc*2 + numWaitProc + numRunThrd + numWebThreads);
		//refreshProgressBar(progressBarMemUsage, "Normal");
	}
	
	private void refresh() {
		// TODO Auto-generated method stub
		refreshTxtFields();
		repaint();
		revalidate();
	}
	private void refreshProgressBar(JProgressBar j, String type) {
		if(type == "Alarm") {
			if(j.getValue() <= 33)			
				j.setForeground(Color.RED);		
			else if(j.getValue() <= 66)
				j.setForeground(Color.YELLOW);
			else 
				j.setForeground(Color.GREEN);
		}else {
			if(j.getValue() <= 33)			
				j.setForeground(Color.green);		
			else if(j.getValue() <= 66)
				j.setForeground(Color.YELLOW);
			else 
				j.setForeground(Color.red);
		}
	}
}
