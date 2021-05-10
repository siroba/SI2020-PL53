package CloseFormativeAction;

import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;

public class View {
	
	private JFrame frame;
	private JList<String> listFormativeActions;
	private JButton btnCloseFA;
	private JTextArea lblSessionFinished;
	private JTextArea lblFeesCorrect;
	private JTextArea lblRemunerationsCorrect;;
	
	
	public View() {
		initComponents();
	}
	
	private void initComponents() {
		frame = new JFrame();
		frame.setTitle("Close Formative Action");
		frame.setName("Close Formative Action");
		frame.setBounds(0, 0, 780, 350);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[250][350][180]", "[50][100][100][100]"));
		
		JLabel label = new JLabel("Active Formative Actions");
		frame.getContentPane().add(label, "cell 0 0, t");
		
		listFormativeActions = new JList<String>();
		listFormativeActions.setModel(new DefaultListModel<String>());
		JScrollPane listPanel = new JScrollPane(listFormativeActions);
		listPanel.setMinimumSize(new Dimension(100, 200));
		frame.getContentPane().add(listPanel, "cell 0 1 1 3, grow");
		
		frame.getContentPane().add(new JLabel("All sessions finished: "), "cell 1 1, t");
		lblSessionFinished = new JTextArea("");
		lblSessionFinished.setEditable(false);
		lblSessionFinished.setBackground(frame.getBackground());
		frame.getContentPane().add(lblSessionFinished, "cell 1 1, t, w 220!, h 55!");
				
		frame.getContentPane().add(new JLabel("Fees:"), "cell 1 2, t");
		lblFeesCorrect = new JTextArea("");
		lblFeesCorrect.setEditable(false);
		lblFeesCorrect.setBackground(frame.getBackground());
		frame.getContentPane().add(lblFeesCorrect, "cell 1 2, t, w 300!, h 65!");
		
		frame.getContentPane().add(new JLabel("Remuneration:"), "cell 1 3, t");
		lblRemunerationsCorrect = new JTextArea("");
		lblRemunerationsCorrect.setEditable(false);
		lblRemunerationsCorrect.setBackground(frame.getBackground());
		frame.getContentPane().add(lblRemunerationsCorrect, "cell 1 3, t, w 270!, h 65!");
		
		btnCloseFA = new JButton("Close Formative Action");
		btnCloseFA.setEnabled(false);
		frame.getContentPane().add(btnCloseFA, "cell 2 3, b");
	}

	public JFrame getFrame() { return frame; }
	public JList<String> getListFormativeActions() { return this.listFormativeActions; }
	public JButton getBtnCloseFA() { return this.btnCloseFA; }
	public JTextArea getLblSessionFinished() { return this.lblSessionFinished; }
	public JTextArea getLblFeesCorrect() { return this.lblFeesCorrect; }
	public JTextArea getLblRemunerationsCorrect() { return this.lblRemunerationsCorrect; }
}
