package DelayFormativeAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import Entities.FormativeAction;

public class Controller implements PL53.util.Controller{
	private View view;
	private Model model;

	public Controller() {
		this.model = new Model();
		try {
			model.initModel();
		} catch (SQLException | ParseException e) {
			e.printStackTrace();
		}

		this.view = new View();
		this.initView();
	}

	@Override
	public void initController() {
		view.getBtnConfirm().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showConfirmDialog(null, "Have all the teachers agreed to this change?",
						"Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

				if (option == 0) {
					try {
						model.delay(view.getSelected(), view.getDateTimeInput());
						model.refund(view.getSelected());
						model.initModel();

						view.setTable(getTableModel(model.getAllData()));
					} catch (SQLException | ParseException e1) {
						e1.printStackTrace();
					} catch (ArrayIndexOutOfBoundsException e2) {
						JOptionPane.showMessageDialog(null, "You have to select one Formative Action");
					}	
				}
			}
		});
	}

	@Override
	public void initView() {
		// Open window (replaces the main generated by WindowBuilder)
		view.setVisible(true);

		view.setTable(getTableModel(model.getAllData()));
	}

	public TableModel getTableModel(FormativeAction data[]) {
		String header[] = { "Formative Action", "Enrollment end", "Status" };

		String body[][] = new String[data.length][header.length];

		for (int i = 0; i < data.length; i++) {
			FormativeAction d = data[i];
			body[i] = new String[] { d.getName(), d.getEnrollmentEnd().toString(), d.getStatus().toString() };
		}

		TableModel tm = new DefaultTableModel(header, body.length);

		for (int i = 0; i < body.length; i++) {
			for (int j = 0; j < header.length; j++) {
				tm.setValueAt(body[i][j], i, j);
			}
		}

		return tm;
	}
}
