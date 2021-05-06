package CloseFormativeAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Controller {
	
	Model model;
	View view;
	
	public Controller() {
		model = new Model();
		view = new View();
		
		initView();
	}
	
	public void initController() {
		view.getListFormativeActions().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				String current_sel = view.getListFormativeActions().getSelectedValue();
				if (current_sel.isEmpty()) {
					view.getBtnCloseFA().setEnabled(false);
				}
				else {
					view.getBtnCloseFA().setEnabled(true);
					checkFormativeAction(false);
				}
			}
		});
		
		view.getBtnCloseFA().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkFormativeAction(true)) {
					closeFormativeAction();
					view.getFrame().setVisible(false);
				}
				
			}
		});
		
	}
	
	public void initView(){
		getListFormativeActionList();
		view.getFrame().setVisible(true);
	}
	
	public void getListFormativeActionList() {
		List<String> listFormativActions = model.getListFormativeAction();
		DefaultListModel<String> m = (DefaultListModel<String>) view.getListFormativeActions().getModel();

		for (int i = 0; i < listFormativActions.size(); i++) {
			m.addElement(listFormativActions.get(i));
		}
	}
	
	public boolean checkFormativeAction(boolean close) {
		
		String error = "The following error occured while trying to close the formative action:\n\n";
		Boolean errorOccured = false;
		Boolean ret = false;
		String fa = view.getListFormativeActions().getSelectedValue();
		
		int confirmedIncomeProfessional = model.getSumIncomeConfirmedProfessional(fa);
		int expectedIncomeProfessional = model.getSumIncomeExpectedProfessional(fa);
		if (confirmedIncomeProfessional != expectedIncomeProfessional) {
			errorOccured = true;
			error += "The income from the professionals does not correspond with the expected income\n";
			view.getLblFeesCorrect().setText("The income from the professionals \ndoes not correspond with the expected income\n"
					+ "Expected: " + expectedIncomeProfessional + "\n Actual: " + confirmedIncomeProfessional);
		}
		else {
			view.getLblFeesCorrect().setText("Correct");
		}
		
		int confirmedPaymentsToTeacher = model.getPaymentTeacherConfirmed(fa);
		int expectedPaymentsToTeacher = model.getPaymentTeacherExpected(fa);
		if (confirmedPaymentsToTeacher != expectedPaymentsToTeacher) {
			errorOccured = true;
			error += "The payments to the teacher does not correspond with the expected payments\n";
			view.getLblRemunerationsCorrect().setText("The payments to the teacher does not \ncorrespond with the expected payments\n"
					+ "Expected: " + expectedPaymentsToTeacher + "\n Actual: " + confirmedPaymentsToTeacher);
		}
		else {
			view.getLblRemunerationsCorrect().setText("Correct");
		}
		
		
		Date lastSession = model.getDateOfLastSession(fa);
		if (lastSession.after(new Date()) || lastSession.equals(new Date())) {
			errorOccured = true;
			error += "Not all sessions of the formative action have been celebrated\n";
			view.getLblSessionFinished().setText("Not all sessions of the formative \naction have been celebrated");
		}
		else {
			view.getLblSessionFinished().setText("All session have been celebrated");
		}
		if (errorOccured && close) {
			int i = JOptionPane.showConfirmDialog(null, error + "\nYou still want to close the formative action?", "Confirm close of Formative Action", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (i == 0) {
				ret = true;
			}
			else {
				ret = false;
			}
		}
		return ret;
		
	}
	
	public void closeFormativeAction() {
		model.closeFormativeAction(view.getListFormativeActions().getSelectedValue());
	}

}
