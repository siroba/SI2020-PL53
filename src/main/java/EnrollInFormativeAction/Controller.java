package EnrollInFormativeAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Entities.Enrollment;
import Entities.Fee;
import Entities.FormativeAction;
import Entities.Professional;
import Exceptions.InvalidFieldValue;
import PL53.util.Constants;
import PL53.util.DateTime;
import PL53.util.FileGenerator;

public class Controller implements PL53.util.Controller {
	private Model model;
	private View view;
	private FormativeAction selected = null;

	public Controller() {
		this.model = new Model();

		try {
			model.loadFormativeActions();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.view = new View();
		// no model-specific initialization, only view-specific initialization
		this.initView();
	}

	/**
	 * Controller initialization: add event handlers to the UI objects. Each event
	 * handler is instantiated in the same way, so that it invokes a private method
	 * of this handler, enclosed in a generic exception handler to display windows.
	 * Each event handler is instantiated in the same way, so that it invokes a
	 * private method of this controller, enclosed in a generic exception handler to
	 * display popup windows when a problem or exception occurs. popup windows when
	 * a problem or controlled exception occurs.
	 */
	public void initController() {
		view.getChckbxNewCheckBox().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.enableInvoice(view.getWantInvoice());
			}
		});
		
		
		view.getFAList().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!view.getFAList().getValueIsAdjusting()) {
					int index = view.getFAList().getSelectedIndex();
					if (index >= 0) {
						view.changeView(true);
						selected = selectFormativeAction(index);

						// Add groups for selected fA to ComboBox
						List<Fee> fees = selected.getFees();
						view.clearCbGroup();
						for (int i=0; i<fees.size(); i++) {
							view.addCbGroup(fees.get(i).getGroup());
						}
					}
				}
			}
		});
		
		// Set Fee according to the selection of the group
		view.getCBGroup().addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	List<Fee> fees = selected.getFees();
				float fee = 0;
				for (int i=0; i<fees.size(); i++) {
					if (fees.get(i).getGroup().equals(view.getGroup())) {
						fee = fees.get(i).getAmount();
					}
				}
				view.setTxtFee(fee + "€");
		    }
		});

		
		view.getBtnConfirmAndEnroll().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (view.getProfName().isBlank()){
					JOptionPane.showMessageDialog(null,
						    "You need to provide a name to enroll.",
						    "Name not valid",
						    JOptionPane.ERROR_MESSAGE);
					return; 
				}
				if (view.getTextSurname().isBlank()){
					JOptionPane.showMessageDialog(null,
						    "You need to provide a surname to enroll.",
						    "Name not valid",
						    JOptionPane.ERROR_MESSAGE);
					return; 
				}

				if(view.getWantInvoice()) {
					if (view.getAddress().isBlank()){
						JOptionPane.showMessageDialog(null,
							    "You need to provide an address to enroll.",
							    "Address not valid",
							    JOptionPane.ERROR_MESSAGE);
						return; 
					}
					
					if (view.getFiscalNumber().isBlank()){
						JOptionPane.showMessageDialog(null,
							    "You need to provide a fiscal number to enroll.",
							    "Fiscal number not valid",
							    JOptionPane.ERROR_MESSAGE);
						return; 
					}else {
						String regex = "^[A-Z]?[0-9]{8,8}[A-Z]$";
						if(!view.getFiscalNumber().matches(regex)) {
							JOptionPane.showMessageDialog(null,
								    "That fiscal number is not valid. E.g. \"55566677R\"",
								    "Fiscal number not valid",
								    JOptionPane.ERROR_MESSAGE);
							return; 
						}
					}
				}
				
				
				
				String name = view.getProfName();
				String surname = view.getTextSurname();
				String phone = view.getTextPhone();
				String email = view.getTextEmail();
				String group = view.getGroup();
				String address = view.getAddress();
				String fiscalNumber = view.getFiscalNumber();

				try {
					Professional p = model.createProfessional(name, surname, phone, email);
					Enrollment en = p.enroll(selected, p, Enrollment.Status.RECEIVED, DateTime.now(), group);

					List<Fee> fees = selected.getFees();
					float fee = 0;
					for (int i=0; i<fees.size(); i++) {
						if (fees.get(i).getGroup().equals(view.getGroup())) {
							fee = fees.get(i).getAmount();
						}
					}
					
					model.doEnrollment(selected, group, p, en , address, fiscalNumber, fee);

					// Generate a file to warning the professional about the fee of the payment and the period to pay it
					
					List<String> body = FileGenerator.bodyWarningEnrollment(selected, p, selected.getFee(group));
					FileGenerator.generateFile(
							Constants.COIIPAemail, 
							p.getEmail(), 
							"Warning of Enrollment",
							body, 
							"WarningEnrollment" + File.separator + "warning_enrollment_professional" +p.getName()+File.separator +"in fa"+selected.getID() + "_p" + p.getID() + ".txt");
					
					JOptionPane.showMessageDialog(null, "An email with the enrollment confitions to be confirmed has been sent to " + p.getEmail());
					
					
					model.loadFormativeActions();
					view.setFAList(model.getFormativeActions());
					

					JOptionPane.showMessageDialog(null, "Enrollment confirmed");
					view.setVisible(false);
				} catch (SQLException | ParseException e1) {
					e1.printStackTrace();
				} catch (InvalidFieldValue e2) {
					JOptionPane.showMessageDialog(null, e2.toString());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	public void initView() {
		// Open window (replaces the main generated by WindowBuilder)

		view.setFAList(model.getFormativeActions());
		view.setVisible(true);
	}

	private FormativeAction selectFormativeAction(int n) {
		return model.getFormativeAction(n);
	}
}
