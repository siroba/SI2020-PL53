package UserStory13573;

import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import java.awt.Dimension;
import java.awt.SystemColor;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import PL53.swing.DateTimeInput;
import PL53.swing.JNumberField;
import PL53.util.DateTime;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import java.awt.Rectangle;
import javax.swing.JTextArea;

public class View {

	private JFrame frmCreateANew;
	private JTextField TFName, TFTeacher;
	private JNumberField TFHours, TFRemuneration, TFSpaces, TFFee;
	private JTextArea TFObjectives, TFContents;
	private JLabel LabelObjectives;
	private JButton BTNCreate;
	private JLabel LabelWarningStartEnroll;
	private JLabel LabelWarningEndEnroll;
	private DateTimeInput enrollStart, enrollEnd, sessionStart;
	private JLabel LabelFee;
	private JPanel panel;
	private JPanel sessionsPanel;
	private JTable table;
	private JTextField TFLocation;
	private JLabel label;

	/**
	 * Create the application.
	 */
	public View() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Border blackline = BorderFactory.createLineBorder(Color.black);

		frmCreateANew = new JFrame();
		frmCreateANew.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
		frmCreateANew.setTitle("Create a new Formative Action");
		frmCreateANew.setName("Courses");
		frmCreateANew.setBounds(0, 0, 846, 485);
		frmCreateANew.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmCreateANew.getContentPane().setLayout(new MigLayout("", "[grow]", "[grow]"));

		panel = new JPanel();
		frmCreateANew.getContentPane().add(panel, "flowx,cell 0 0,grow");
		panel.setLayout(null);

		// Heading
		JLabel lblNewLabel = new JLabel("Plan a formative action");
		lblNewLabel.setBounds(18, 19, 180, 17);
		panel.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));

		TFName = new JTextField();
		TFName.setBounds(50, 48, 273, 18);
		panel.add(TFName);
		TFName.setFont(new Font("Tahoma", Font.PLAIN, 11));
		TFName.setColumns(10);

		// Name
		JLabel LabelName = new JLabel("Name:");
		LabelName.setBounds(10, 50, 37, 14);
		panel.add(LabelName);
		LabelName.setFont(new Font("Tahoma", Font.PLAIN, 11));

		// Objectives
		LabelObjectives = new JLabel("Objectives:");
		LabelObjectives.setBounds(10, 74, 63, 14);
		panel.add(LabelObjectives);
		LabelObjectives.setFont(new Font("Tahoma", Font.PLAIN, 11));

		// Main contents
		JLabel LabelContents = new JLabel("Main contents:");
		LabelContents.setBounds(210, 74, 83, 14);
		panel.add(LabelContents);
		LabelContents.setFont(new Font("Tahoma", Font.PLAIN, 11));

		TFSpaces = new JNumberField(5);
		TFSpaces.setBounds(60, 369, 69, 18);
		panel.add(TFSpaces);
		TFSpaces.setFont(new Font("Tahoma", Font.PLAIN, 11));
		TFSpaces.setColumns(10);

		// Spaces
		JLabel LabelSpaces = new JLabel("Spaces:");
		LabelSpaces.setBounds(10, 371, 46, 14);
		panel.add(LabelSpaces);
		LabelSpaces.setFont(new Font("Tahoma", Font.PLAIN, 11));

		LabelWarningStartEnroll = new JLabel("");
		LabelWarningStartEnroll.setBounds(180, 181, 210, 14);
		panel.add(LabelWarningStartEnroll);
		LabelWarningStartEnroll.setForeground(Color.RED);
		LabelWarningStartEnroll.setFont(new Font("Tahoma", Font.PLAIN, 11));

		LabelWarningEndEnroll = new JLabel("");
		LabelWarningEndEnroll.setBounds(180, 271, 202, 14);
		panel.add(LabelWarningEndEnroll);
		LabelWarningEndEnroll.setForeground(Color.RED);
		LabelWarningEndEnroll.setFont(new Font("Tahoma", Font.PLAIN, 11));

		TFFee = new JNumberField(6);
		TFFee.setBounds(180, 371, 46, 19);
		panel.add(TFFee);
		TFFee.setColumns(10);

		// Fee
		LabelFee = new JLabel("Fee:");
		LabelFee.setBounds(150, 371, 24, 14);
		panel.add(LabelFee);
		LabelFee.setFont(new Font("Tahoma", Font.PLAIN, 11));

		// Create Button
		BTNCreate = new JButton("Create formative action");
		BTNCreate.setBounds(114, 400, 186, 24);
		panel.add(BTNCreate);
		BTNCreate.setFont(new Font("Tahoma", Font.BOLD, 11));

		JLabel LabelStartEnroll = new JLabel("Start of enrollment period:");
		LabelStartEnroll.setBounds(12, 181, 152, 14);
		panel.add(LabelStartEnroll);
		LabelStartEnroll.setFont(new Font("Dialog", Font.PLAIN, 11));

		enrollStart = new DateTimeInput();
		enrollStart.setBounds(12, 196, 378, 70);
		panel.add(enrollStart);
		enrollStart.getDaysTextField().setBound(1, 31);
		enrollStart.getMonthsTextField().setBound(1, 12);
		enrollStart.getYearsTextField().setBound(2000, 3000);
		enrollStart.setBorder(blackline);

		JLabel lblEndOfEnrollment = new JLabel("End of enrollment period:");
		lblEndOfEnrollment.setBounds(12, 271, 155, 15);
		panel.add(lblEndOfEnrollment);
		lblEndOfEnrollment.setFont(new Font("Dialog", Font.PLAIN, 11));

		enrollEnd = new DateTimeInput();
		enrollEnd.setBounds(12, 287, 378, 70);
		panel.add(enrollEnd);
		enrollEnd.getDaysTextField().setBound(1, 31);
		enrollEnd.getMonthsTextField().setBound(1, 12);
		enrollEnd.getYearsTextField().setBound(2000, 3000);
		enrollEnd.setBorder(blackline);

		TFObjectives = new JTextArea();
		TFObjectives.setLineWrap(true);
		TFObjectives.setBounds(10, 93, 188, 76);
		panel.add(TFObjectives);

		TFContents = new JTextArea();
		TFContents.setBounds(210, 93, 188, 76);
		panel.add(TFContents);

		sessionsPanel = new JPanel();
		frmCreateANew.getContentPane().add(sessionsPanel, "cell 0 0,grow");
		sessionsPanel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(7, 26, 402, 103);
		sessionsPanel.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		JLabel LabelLocation = new JLabel("Location:");
		LabelLocation.setFont(new Font("Dialog", Font.PLAIN, 11));
		LabelLocation.setBounds(12, 145, 51, 14);
		sessionsPanel.add(LabelLocation);

		JLabel LabelWarningDay = new JLabel("  ");
		LabelWarningDay.setForeground(Color.RED);
		LabelWarningDay.setFont(new Font("Dialog", Font.PLAIN, 11));
		LabelWarningDay.setBounds(12, 145, 8, 14);
		sessionsPanel.add(LabelWarningDay);

		JLabel LabelDate = new JLabel("Date of the session:");
		LabelDate.setFont(new Font("Dialog", Font.PLAIN, 11));
		LabelDate.setBounds(10, 175, 134, 14);
		sessionsPanel.add(LabelDate);

		JLabel LabelHours = new JLabel("Number of hours:");
		LabelHours.setFont(new Font("Dialog", Font.PLAIN, 11));
		LabelHours.setBounds(260, 145, 101, 14);
		sessionsPanel.add(LabelHours);

		sessionStart = new DateTimeInput();
		sessionStart.setBorder(blackline);
		sessionStart.getDaysTextField().setBound(1, 31);
		sessionStart.getMonthsTextField().setBound(1, 12);
		sessionStart.getYearsTextField().setBound(2000, 3000);
		sessionStart.setBounds(10, 190, 378, 70);

		sessionsPanel.add(sessionStart);

		TFLocation = new JTextField();
		TFLocation.setBounds(81, 142, 166, 19);
		sessionsPanel.add(TFLocation);
		TFLocation.setColumns(10);

		TFHours = new JNumberField(2);
		TFHours.setBound(0, 24);
		TFHours.setBounds(365, 142, 39, 19);
		sessionsPanel.add(TFHours);

		JButton btnAddSession = new JButton("Add session");
		btnAddSession.setBounds(129, 400, 166, 25);
		sessionsPanel.add(btnAddSession);

		// Teacher
		JLabel LabelTeacher = new JLabel("Teacher:");
		LabelTeacher.setBounds(10, 272, 50, 14);
		sessionsPanel.add(LabelTeacher);
		LabelTeacher.setFont(new Font("Tahoma", Font.PLAIN, 11));

		TFTeacher = new JTextField();
		TFTeacher.setBounds(66, 272, 142, 18);
		sessionsPanel.add(TFTeacher);
		TFTeacher.setFont(new Font("Tahoma", Font.PLAIN, 11));
		TFTeacher.setColumns(10);

		TFRemuneration = new JNumberField(4);
		TFRemuneration.setBound(0, Integer.MAX_VALUE);
		TFRemuneration.setBounds(310, 272, 51, 18);
		sessionsPanel.add(TFRemuneration);
		TFRemuneration.setFont(new Font("Tahoma", Font.PLAIN, 11));
		TFRemuneration.setColumns(10);

		// Remuneration
		JLabel LabelRemuneration = new JLabel("Remuneration:");
		LabelRemuneration.setBounds(220, 272, 83, 14);
		sessionsPanel.add(LabelRemuneration);
		LabelRemuneration.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		LabelWarningDay = new JLabel("");
		LabelWarningDay.setBounds(143, 175, 229, 14);
		sessionsPanel.add(LabelWarningDay);
	}

	// Getters and Setters to access from the controller (compact representation)
	public JFrame getFrame() {
		return this.frmCreateANew;
	}

	public JButton getCreateBtn() {
		return this.BTNCreate;
	}

	public String getName() {
		return this.TFName.getText();
	}

	public String getObjectives() {
		return this.TFObjectives.getText();
	}

	public String getMainContents() {
		return this.TFContents.getText();
	}

	public String getTeacher() {
		return this.TFTeacher.getText();
	}

	public String getRemuneration() {
		return this.TFRemuneration.getText();
	}

	public String getLocation() {
		return this.TFLocation.getText();
	}

	public String getSpaces() {
		return this.TFSpaces.getText();
	}

	public DateTime getSessionDatetime() {
		return sessionStart.getDateTime();
	}

	public int getNumberOfHours() {
		return this.TFHours.getValue();
	}

	public DateTime getEnrollStart() {
		return enrollStart.getDateTime();
	}

	public DateTime getEnrollEnd() {
		return enrollEnd.getDateTime();
	}

	public String getFee() {
		return String.valueOf(this.TFFee.getText());
	}

	public void setWarningDay(String warning) {
		this.LabelWarningDay.setText(warning);
	}

	public void setWarningEnrollmentPeriodStart(String warning) {
		this.LabelWarningStartEnroll.setText(warning);
	}

	public void setWarningEnrollmentPeriodStart2(String warning) {
		this.LabelWarningStartEnroll.setText(warning);
	}

	public void setWarningEnrollmentPeriodEnd(String warning) {
		this.LabelWarningEndEnroll.setText(warning);
	}

	public void setWarningEnrollmentPeriodEnd2(String warning) {
		this.LabelWarningEndEnroll.setText(warning);
	}
}
