package com.seb.math.client;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.seb.math.shared.MathAssigment;

public class MathClientGUi
{

	private JFrame frame;
	private JTextField txtAddress;
	private JTextField txtPort;
	private JTextField txtAnswear;
	private final MathClient theClient;

	private boolean isConnected = false;
	private JLabel lblNumber1;
	private JLabel lblNumber2;
	private JLabel lblOperator;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					final MathClientGUi window = new MathClientGUi();
					window.frame.setVisible(true);
				}
				catch (final Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MathClientGUi()
	{
		theClient = new MathClient();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 348, 170);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		txtAddress = new JTextField();
		txtAddress.setText("localhost");
		txtAddress.setBounds(10, 11, 86, 20);
		frame.getContentPane().add(txtAddress);
		txtAddress.setColumns(10);

		txtPort = new JTextField();
		txtPort.setText("10005");
		txtPort.setBounds(10, 42, 50, 20);
		frame.getContentPane().add(txtPort);
		txtPort.setColumns(10);

		final JLabel lblServerIp = new JLabel("Server IP");
		lblServerIp.setBounds(106, 14, 46, 14);
		frame.getContentPane().add(lblServerIp);

		final JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(70, 45, 46, 14);
		frame.getContentPane().add(lblPort);

		final JButton btnConnect = new JButton("Connect");
		btnConnect.setBounds(162, 10, 140, 23);
		frame.getContentPane().add(btnConnect);

		lblNumber1 = new JLabel("");
		lblNumber1.setBounds(10, 76, 18, 14);
		frame.getContentPane().add(lblNumber1);

		lblNumber2 = new JLabel("");
		lblNumber2.setBounds(52, 76, 18, 14);
		frame.getContentPane().add(lblNumber2);

		lblOperator = new JLabel("");
		lblOperator.setBounds(38, 76, 30, 14);
		frame.getContentPane().add(lblOperator);

		final JLabel label = new JLabel("=");
		label.setBounds(70, 76, 18, 14);
		frame.getContentPane().add(label);

		txtAnswear = new JTextField();
		txtAnswear.setBounds(80, 73, 46, 20);
		frame.getContentPane().add(txtAnswear);
		txtAnswear.setColumns(10);

		final JButton btnAnswear = new JButton("Answear");
		btnAnswear.setBounds(136, 72, 89, 23);
		frame.getContentPane().add(btnAnswear);

		final JLabel lbltext = new JLabel("Answear:");
		lbltext.setBounds(10, 101, 57, 14);
		frame.getContentPane().add(lbltext);

		final JLabel lblAnswear = new JLabel("");
		lblAnswear.setBounds(70, 101, 129, 14);
		frame.getContentPane().add(lblAnswear);

		final JButton btnNextAssigment = new JButton("next assigment");

		btnNextAssigment.setBounds(209, 97, 113, 23);
		frame.getContentPane().add(btnNextAssigment);

		final JFileChooser chooser = new JFileChooser();
		final FileNameExtensionFilter filter = new FileNameExtensionFilter("Text file", "txt");
		chooser.setFileFilter(filter);

		final JButton btnSendfile = new JButton("sendFile");
		btnSendfile.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					final int returnVal = chooser.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION)
					{
						System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
					}

					String newFile;
					if ((newFile = theClient.sendFile(chooser.getSelectedFile())) != null)
					{
						JOptionPane.showMessageDialog(null, "File correction is done, answears are in file: " + newFile);
					}
				}
				catch (final Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});
		btnSendfile.setBounds(233, 41, 89, 23);
		frame.getContentPane().add(btnSendfile);

		btnConnect.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (isConnected)
				{
					isConnected = false;
					theClient.disconnect();
					btnConnect.setText("Connect");
				}
				else
				{
					isConnected = true;
					theClient.connect(txtAddress.getText(), txtPort.getText());
					getAssigment();
					btnConnect.setText("Disconnect");
				}

			}

		});

		btnAnswear.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (txtAnswear.getText().isEmpty() == false)
				{
					final boolean answear = theClient.sendAnswear(txtAnswear.getText());
					if (answear)
					{
						lblAnswear.setText("is Correct");
					}
					else
					{
						lblAnswear.setText("is Wrong");
					}
				}
			}
		});

		btnNextAssigment.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				getAssigment();
			}
		});

	}

	private void getAssigment()
	{
		final MathAssigment assigment = theClient.getAssigment();

		lblNumber1.setText(String.valueOf(assigment.getNumber1()));
		lblNumber2.setText(String.valueOf(assigment.getNumber2()));
		lblOperator.setText(assigment.getOperatorString());
		txtAnswear.setText("");
	}
}
