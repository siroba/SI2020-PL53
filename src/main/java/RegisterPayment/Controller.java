package RegisterPayment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import Entities.FormativeAction;
import Entities.Professional;
import Entities.Session;
import PL53.util.Constants;
import PL53.util.Date;
import PL53.util.DateTime;
import PL53.util.FileGenerator;

public class Controller implements PL53.util.Controller {
	private Model model;
	private View view;
	private Data selectedRow;

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

	public void initController() {

		view.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selectedRow = model.getData(view.getSelectedInvoice());

				if (selectedRow != null)
					try {
						view.setMovementsTable(showPayments(selectedRow));
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
			}

		});

		// "Normal payment" button (from the professional to COIIPA)
		view.getConfirmButton().addActionListener(new ActionListener() {
			@SuppressWarnings("unused")
			public void actionPerformed(ActionEvent e) {
				if (selectedRow == null) {
					JOptionPane.showMessageDialog(null, "You have to select one payment");
					return;
				}
				
				try {
					DateTime now = DateTime.now();
	
					// Data from the form
					boolean isCash = view.isCash();
					float newPayment = view.getAmountPaid();					// The input amount to pay to COIIPA
					Date payDate = view.getDateTextPane().getDate();
					
					// Data from the database
					float professionalPaid = model.getAmountPaid(selectedRow); // Sum of all the payments to COIIPA
					float refundedAmount = model.getAmountReturned(selectedRow);// Sum of all the payments to the professional
					float totalPaid = model.getAmountTotalPaid(selectedRow);	// Sum of all the payments
	
					// Payment calculations
					float fee = selectedRow.invoice.getAmount();
					float newTotal = totalPaid + newPayment;
					float toReturn = newTotal - fee;
	
					// Dates...
					int daysFromEnrollment = DateTime.daysSince(payDate, selectedRow.enrollment.getTimeEn());
					long daysBetweenNowAction = DateTime.daysSince(now, payDate);
				
					/////////////////////////////////////////////////////////////////////////////////////
					
					// Conditions to check (validations)
					if (isCash && newPayment > 1000) {
						JOptionPane.showMessageDialog(null, "With cash the payments has to be lower than 1000€");
						return;
					} else if (newPayment <= 0) {
						JOptionPane.showMessageDialog(null, "You cannot do a payment for " + newPayment + "€");
						return;
					} else if (daysBetweenNowAction < 0) {
						JOptionPane.showMessageDialog(null,
								"Payments cannot be made in the future (the date must be on the current date or in the past).",
								"Date not valid", JOptionPane.ERROR_MESSAGE);
						return;
					}
	
					if (daysFromEnrollment > 2) {
						int freePlaces = model.getFreePlaces(selectedRow.formativeAction.getID());
	
						if (freePlaces > 0) {
							JOptionPane.showMessageDialog(null,
									"The payment has been made after the estipulated period, any way as there are still free places the payment is accepted");
						} else {
							JOptionPane.showMessageDialog(null,
									"The payment must be done with a margin of 48 hours after the enrollmet");
							return;
						}
					}
	
					if (toReturn > 0) {
						String difference = String.format("%.2f", toReturn);
	
						int option = JOptionPane.showConfirmDialog(null, "The sum of payments (" + newTotal
								+ ") is hihger than the fee, Do you want to return the diferrence (" + difference + ")?",
								"warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	
						if (option == 0) { // The user clicked YES
							
							// Register the compensation
							newTotal -= toReturn;
							
							model.createPayment(selectedRow.invoice, -toReturn, payDate, view.isCash(), true, newTotal);
						}
					} else if (toReturn < 0) {
						JOptionPane.showMessageDialog(null, "The payment is lower than  the fee ");
					}
	
					boolean enrollmentConfirmed = model.createPayment(selectedRow.invoice, newPayment, payDate, isCash, true, newTotal);
					JOptionPane.showMessageDialog(null, "The payment has been registered");
					
					if(isCash) {
						Professional p = selectedRow.professional;
						FormativeAction fA = selectedRow.formativeAction;
						List<Session> ss = fA.getSessions();
						List<String> body = FileGenerator.bodyConfirmationReceipt(fA, p, toReturn + selectedRow.fee);
						FileGenerator.generateFile(
								Constants.COIIPAemail, 
								p.getEmail(), 
								"Receipt",
								body, 
								"Receipt" + File.separator + "Confirmation_receipt" + fA.getID() + "_p" + p.getID() + ".txt");
						
						JOptionPane.showMessageDialog(null, "Receipt done to " + p.getName());
						
					}
					
					if (enrollmentConfirmed) {
						// Generate a file to confirm the enrollment
						Professional p = selectedRow.professional;
						FormativeAction fA = selectedRow.formativeAction;
						List<Session> ss = fA.getSessions();
						List<String> body = FileGenerator.bodyConfirmationEnrollment(fA, p, ss, selectedRow.fee, toReturn + selectedRow.fee);
						FileGenerator.generateFile(
								Constants.COIIPAemail, 
								p.getEmail(), 
								"Confirmation of Enrollment",
								body, 
								"ConfirmationEnrollment" + File.separator + "Confirmation_enrollment_fA" + fA.getID() + "_p" + p.getID() + ".txt");
						
						JOptionPane.showMessageDialog(null, "An email with the enrollment confirmation has been sent to " + p.getEmail());
					}
					
					view.resetAmountPaid();
					model.initModel();
					view.setTable(getTableModel(model.getAllData()));
					
				} catch (SQLException | ParseException e1) { // This is bad practice (the entire function is inside a try/catch)!!
					e1.printStackTrace();					 // Oh well... We don't get paid enough
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		});

		view.getBtnNewRefund().addActionListener(new ActionListener() {
			@SuppressWarnings("unused")
			public void actionPerformed(ActionEvent e) {
				try {
					DateTime now = DateTime.now();
	
					// Data from the form
					boolean isCash = view.isCash();
					float newRefund = view.getAmountRefund();
					Date refundDate = view.getdateTextPaneRefund().getDate();
					
					// Data from the database
					float professionalPaid = model.getAmountPaid(selectedRow);	// Sum of all the payments to COIIPA
					float refundedAmount = model.getAmountReturned(selectedRow);// Sum of all the payments to the professional
					float totalPaid = model.getAmountTotalPaid(selectedRow);	// Sum of all the payments
	
					// Payment calculations
					float fee = selectedRow.invoice.getAmount();
					float newTotal = totalPaid + newRefund;
					float toReturn = totalPaid-newRefund; //newTotal - fee;
	
					// Dates...
					int daysFromEnrollment = DateTime.daysSince(refundDate, selectedRow.enrollment.getTimeEn());
					int daysBetweenNowAction = Date.daysSince(now, refundDate);
					
					/////////////////////////////////////////////////////////////////////////////////////
					
					// Checks
					if (selectedRow == null) {
						JOptionPane.showMessageDialog(null, "You have to select one payment");
						return;
	
					} else if (newRefund <= 0) {
						JOptionPane.showMessageDialog(null, "You cannot do a payment for " + view.getAmountPaid() + "�");
	
						return;
					} else if (daysBetweenNowAction < 0) {
						JOptionPane.showMessageDialog(null,
								"Payments cannot be made in the future( the date must be on the current date or in the past).",
								"date not valid ", JOptionPane.ERROR_MESSAGE);
						return;
					}
	
					// If the user wants to compensate the movement
					if (toReturn > 0) {
						String difference = String.format("%.2f", (newRefund - toReturn));
						
						int option = JOptionPane.showConfirmDialog(null,
								"The amount of the movement (" + newRefund
										+ ") is higher than the amount that has to be returned to the professional ("
										+ toReturn + "). Do you want to return the difference ("
										+ difference + ")?",
								"Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	
						if (option == 0) { // The user clicked YES
							float amount = newRefund - toReturn; // the amount to compensate
							model.createPaymentRefund(selectedRow.invoice.getID(), amount, refundDate, view.isCash(),
									true);
						}
					}

					boolean enrollmentConfirmed = model.createPayment(selectedRow.invoice, -newRefund, refundDate,
							view.isCash(), true, totalPaid);
					JOptionPane.showMessageDialog(null, "The payment has been registered");
					
					if (enrollmentConfirmed) {
						// Generate a file to confirm the enrollment
						Professional p = selectedRow.professional;
						FormativeAction fA = selectedRow.formativeAction;
						List<Session> ss = fA.getSessions();
						List<String> body = FileGenerator.bodyConfirmationEnrollment(fA, p, ss, selectedRow.fee, totalPaid);
						FileGenerator.generateFile(
								Constants.COIIPAemail, 
								p.getEmail(), 
								"Confirmation of Enrollment",
								body, 
								"ConfirmationEnrollment" + File.separator + "Confirmation_enrollment_fA" + fA.getID() + "_p" + p.getID() + ".txt");
						
						JOptionPane.showMessageDialog(null, "An email with the enrollment confirmation has been sent to " + p.getEmail());
					}
					
					view.resetAmountPaid();
					model.initModel();
					view.setTable(getTableModel(model.getAllData()));
				} catch (SQLException | ParseException e1) { // This is bad practice (the entire function is inside a try/catch)!!
					e1.printStackTrace();					 // Oh well... We don't get paid enough
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		});

	}

	public void initView() {
		view.setVisible(true); // Show the window
		
		try {
			view.setTable(getTableModel(model.getAllData()));	// Initialise the table
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public TableModel getTableModel(List<Data> list) throws SQLException {

		String header[] = { "Course name", 			"Professional name",	"Professional surname", 
							"Professional email", 	"Fee", 					"Date of the registration", 
							"Amount paid", 			"Amount returned" };

		String body[][] = new String[list.size()][header.length];
		for (int i = 0; i < list.size(); i++) {
			Data d = list.get(i);

			body[i] = new String[] { 
					d.formativeAction.getName(),			d.professional.getName(),					d.professional.getSurname(),
					d.professional.getEmail(), 				Float.toString(d.invoice.getAmount()),		d.enrollment.getTimeEn().toString(), 
					Float.toString(model.getAmountPaid(d)),	Float.toString(model.getAmountReturned(d)) 
				};
		}

		TableModel tm = new DefaultTableModel(header, body.length);

		for (int i = 0; i < body.length; i++) {
			for (int j = 0; j < header.length; j++) {
				tm.setValueAt(body[i][j], i, j);
			}
		}

		return tm;
	}

	public TableModel showPayments(Data d) throws SQLException {
		List<AuxPayment> paymentList = model.getAuxPayments(d);
		
		String header[] = { "sender", "receiver", "date", "amount" };

		String body[][] = new String[paymentList.size()][header.length];
		
		for (int i = 0; i < paymentList.size(); i++) {
			AuxPayment aux = paymentList.get(i);
		
			body[i] = new String[] {aux.sender, aux.receiver, aux.getDate().toString(), Float.toString(aux.getAmount())};
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
