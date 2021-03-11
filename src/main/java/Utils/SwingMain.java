package Utils;

import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import Utils.Database;

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
		EventQueue.invokeLater(new Runnable() { // NOSONAR autogenerated code
			public void run() {
				try {
					SwingMain window = new SwingMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace(); // NOSONAR autogenerated code
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
		frame.setBounds(0, 0, 800, 250);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(6, 3, 10, 10));

		// Initialize the DB and fill it with data
		Database db = new Database();
		db.createDatabase(false);
		db.loadDatabase();

		// Secretary
		JLabel labelSecretary = new JLabel("Secretary", SwingConstants.CENTER);

		JButton btnRegisterPayments = new JButton("Register Payments");

		JButton btnListFormativeActions = new JButton("List formative actions");

		JButton btnStatusOfFormativeActions = new JButton("Status of formative actions");

		JButton btnRegisterCancellations = new JButton("Register cancellations");
		btnRegisterCancellations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserStory13578.Controller controller = new UserStory13578.Controller();
				controller.initController();
			}
		});

		JButton btnRecordPayments = new JButton("Record payments");

		// Professional
		JLabel labelProfessional = new JLabel("Professional", SwingConstants.CENTER);

		JButton btnEnrollInFormativeAction = new JButton("Enroll in formative action");
		btnEnrollInFormativeAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserStory13574.Controller controller = new UserStory13574.Controller();
				controller.initController();
			}
		});
		
		// Training manager
		JLabel labelTrainingManager = new JLabel("Training manager", SwingConstants.CENTER);

		JButton btnPlanFormativeAction = new JButton("Plan formative action");
		btnPlanFormativeAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserStory13573.Controller controller = new UserStory13573.Controller(new UserStory13573.Model(),
						new UserStory13573.View());
				controller.initController();
			}
		});
		
		JButton btnCheckFinancialBalance = new JButton("Check financial balance");
		btnCheckFinancialBalance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserStory13579.Controller controller = new UserStory13579.Controller(new UserStory13579.Model(),
						new UserStory13579.View());
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
		frame.getContentPane().add(btnRecordPayments);
		frame.getContentPane().add(labelEmpty6);
		frame.getContentPane().add(labelEmpty7);
	}

	public JFrame getFrame() {
		return this.frame;
	}

}
