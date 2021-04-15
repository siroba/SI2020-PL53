package Utils;

import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;


/**
 * Main entry point including database initialization actions and buttons for
 * the execution of the screens. It does not follow MVC because it is only
 * temporary so that during the development it is possible to perform
 * initialization actions.
 */
public class SwingMain {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() { //NOSONAR autogenerated code
			public void run() {
				try {
					File f= new File("database.db");
					f.delete();
					
					SwingMain window = new SwingMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace(); //NOSONAR autogenerated code
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SwingMain() {
		initialize();
	}

	/**
	 * Initialize the database and contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Main");

		frame.setBounds(0, 0, 840, 323);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(8, 3, 10, 10));

		// Initialize the DB and fill it with data
		Database db = new Database();
		db.createDatabase(false);
		db.loadDatabase();

		// Secretary
		JLabel labelSecretary = new JLabel("Secretary", SwingConstants.CENTER);

		JButton btnRegisterPayments = new JButton("Register a Payment");
		btnRegisterPayments.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegisterPayment.Controller controller = new RegisterPayment.Controller();
				controller.initController();
			}
		});
		JButton btnListFormativeActions = new JButton("List formative actions");
		btnListFormativeActions.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ListFormativeActions.Controller controller = new ListFormativeActions.Controller(new ListFormativeActions.Model(), new ListFormativeActions.View());
				controller.initController();
				
			}
		});

		JButton btnStatusOfFormativeActions = new JButton("Status of formative actions");
		btnStatusOfFormativeActions.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				StatusOfFormativeAction.Controller controller = new StatusOfFormativeAction.Controller();
				controller.initController();
				
			}
		});

		JButton btnRegisterCancellations = new JButton("Register cancellations");
		btnRegisterCancellations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegisterCancellations.Controller controller = new RegisterCancellations.Controller();
				controller.initController();
			}
		});

		JButton btnPayTeacher = new JButton("Pay a teacher");
		btnPayTeacher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PayATeacher.Controller controller = new PayATeacher.Controller();
				controller.initController();
			}
		});

		/*JButton btnDelayFA = new JButton("Delay a Formative Action");
		btnDelayFA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserStory13729.Controller controller = new UserStory13729.Controller();
				controller.initController();
			}
		});*/

		JButton btnNewButton = new JButton("Cancel a Formative Action");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CancelFormativeAction.Controller controller = new CancelFormativeAction.Controller();
				controller.initController();
			}
		});
		
		JButton btnCloseFA = new JButton("Close a Formative Action");
		btnCloseFA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CloseFormativeAction.Controller controller = new CloseFormativeAction.Controller();
				controller.initController();
			}
		});

		// Professional
		JLabel labelProfessional = new JLabel("Professional", SwingConstants.CENTER);
		
		JButton btnEnrollInFormativeAction = new JButton("Enroll in formative action");
		btnEnrollInFormativeAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EnrollInFormativeAction.Controller controller = new EnrollInFormativeAction.Controller();
				controller.initController();
			}
		});
		
		
		// Training manager
		JLabel labelTrainingManager = new JLabel("Training manager", SwingConstants.CENTER);

		JButton btnPlanFormativeAction = new JButton("Plan formative action");
		btnPlanFormativeAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateFormativeAction.Controller controller = new CreateFormativeAction.Controller(new CreateFormativeAction.Model(),
						new CreateFormativeAction.View());
				controller.initController();
			}
		});
		
		JButton btnCheckFinancialBalance = new JButton("Check financial balance");

		btnCheckFinancialBalance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CheckFinancialBalance.Controller controller;
				controller = new CheckFinancialBalance.Controller(new CheckFinancialBalance.Model(), new CheckFinancialBalance.View());
				controller.initController();
			}
		});

		// Empty grid elements
		JLabel labelEmpty1 = new JLabel("");
		JLabel labelEmpty2 = new JLabel("");
		JLabel labelEmpty3 = new JLabel("");
		JLabel labelEmpty4 = new JLabel("");
		JLabel labelEmpty5 = new JLabel("");
		JLabel labelEmpty6 = new JLabel("");
		JLabel labelEmpty7 = new JLabel("");
		
		// Add elements to frame/grid
		// Row 1
		frame.getContentPane().add(labelSecretary);
		frame.getContentPane().add(labelProfessional);
		frame.getContentPane().add(labelTrainingManager);
		// Row 2
		frame.getContentPane().add(btnRegisterPayments);
		frame.getContentPane().add(btnEnrollInFormativeAction);
		frame.getContentPane().add(btnPlanFormativeAction);
		// Row 3
		frame.getContentPane().add(btnListFormativeActions);
		frame.getContentPane().add(labelEmpty1);
		frame.getContentPane().add(btnCheckFinancialBalance);
		// Row 4
		frame.getContentPane().add(btnStatusOfFormativeActions);
		frame.getContentPane().add(labelEmpty2);
		frame.getContentPane().add(labelEmpty3);
		// Row 5
		frame.getContentPane().add(btnRegisterCancellations);
		frame.getContentPane().add(labelEmpty4);
		frame.getContentPane().add(labelEmpty5);
		// Row 6
		frame.getContentPane().add(btnPayTeacher);
		frame.getContentPane().add(labelEmpty6);
		frame.getContentPane().add(labelEmpty7);
		
		// Row 7
		//frame.getContentPane().add(btnDelayFA);
//		frame.getContentPane().add(new JLabel(""));
//		frame.getContentPane().add(new JLabel(""));
		
		// Row 8
		frame.getContentPane().add(btnNewButton);
		frame.getContentPane().add(new JLabel(""));
		frame.getContentPane().add(new JLabel(""));
		
		// Row 9
		frame.getContentPane().add(btnCloseFA);
		
	}

	public JFrame getFrame() {
		return this.frame;
	}
}