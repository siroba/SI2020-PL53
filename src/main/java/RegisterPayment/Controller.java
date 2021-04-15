package RegisterPayment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.multi.MultiInternalFrameUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import BaseProject.SwingUtil;
import Entities.Invoice;
import Entities.Payment;
import PL53.util.Date;
import PL53.util.DateTime;
import RegisterPayment.Data;

public class Controller implements PL53.util.Controller {
	private Model model;
	private View view;
	private Data selectedRow;

	public Controller() {
		this.model = new Model();
		try {
			model.initModel();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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

		view.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Data d = model.getData(view.getSelectedInvoice());
				selectedRow = d;

			showPayments();
				
			}
				
		});

		view.getConfirmButton().addActionListener(new ActionListener() { // TODO
			public void actionPerformed(ActionEvent e) {
				boolean aux = true;

				if (selectedRow == null) {
					JOptionPane.showMessageDialog(null, "You have to select one payment");
					return;
				}
				float alreadyPayed = model.getAmountPayed(selectedRow);
				float totalPayed = alreadyPayed + view.getAmountPayed();

				if (totalPayed > selectedRow.invoice.getAmount()) {
					aux = false;
					int option = JOptionPane.showConfirmDialog(null,
							"The sum of payments (" + totalPayed
									+ ") is hihger than the fee, Do you want to return the diferrence ("
									+ (totalPayed - selectedRow.invoice.getAmount()) + ")?",
							"warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

					if (option == 0) {
						float toReturn = totalPayed - selectedRow.invoice.getAmount();
						Date payDate = view.getDateTextPane().getDate();
						Invoice invoiceReturn = new Invoice(toReturn, payDate, selectedRow.invoice.getReceiver(),
								selectedRow.invoice.getSender(), selectedRow.invoice.getAddress(),
								selectedRow.invoice.getFiscalNumber(), selectedRow.invoice.getID_fa(),
								selectedRow.invoice.getID_professional());
						try {
							model.createPayment(invoiceReturn, toReturn, payDate, view.isCash(), true);
							JOptionPane.showMessageDialog(null, "The payment has been registered");
						} catch (SQLException e1) {

							e1.printStackTrace();
						} catch (ParseException e1) {

							e1.printStackTrace();
						}

					} else if (option == 1) {
						return;
					}

				} else if (totalPayed < selectedRow.invoice.getAmount()) {
					JOptionPane.showMessageDialog(null, "The payment is lower than  the fee ");
					aux = false;
				}

				int calcTime = DateTime.daysSince(view.getDateTextPane().getDate(), selectedRow.enrollment.getTimeEn());

				if (calcTime > 2 || calcTime < -1) {
					JOptionPane.showMessageDialog(null,
							"The payment must be done with a margin of 48 hours after the enrollmet");

				}

				else {

					float amount = view.getAmountPaidTextField();
					Date payDate = view.getDateTextPane().getDate();

					try {
						model.createPayment(selectedRow.invoice.getID(), amount, payDate, view.isCash(), aux);
						JOptionPane.showMessageDialog(null, "The payment has been registered");

						model.initModel();
						view.setTable(getTableModel(model.getAllData()));
					} catch (SQLException e1) {
						e1.printStackTrace();
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}

			}
		});

	}

	public void initView() {

		view.setVisible(true);

		view.setTable(getTableModel(model.getAllData()));
	}

	public TableModel getTableModel(RegisterPayment.Data[] datas) {

		String header[] = { "Course name", "Professional name", "Professional surname", "Professional email", "Fee",
				"Date of the registration" };

		String body[][] = new String[datas.length][header.length];

		for (int i = 0; i < datas.length; i++) {
			RegisterPayment.Data d = datas[i];
			body[i] = new String[] { d.formativeAction.getName(), d.professional.getName(), d.professional.getSurname(),
					d.professional.getEmail(), Float.toString(d.invoice.getAmount()),
					d.enrollment.getTimeEn().toString() };
		}

		TableModel tm = new DefaultTableModel(header, body.length);
		// loads each of the pojos values using PropertyUtils (from apache coommons
		// beanutils)
		for (int i = 0; i < body.length; i++) {
			for (int j = 0; j < header.length; j++) {
				tm.setValueAt(body[i][j], i, j);
			}
		}

		return tm;
	}

	/*public TableModel getMovementsTable(List<Invoice> invoices) {

		String header[] = { "Sender", "Receiver", "amount" };

		List<String[]> bodytmp = new ArrayList<String[]>();

		for (Invoice i : invoices) {
			for (Payment p : i.getPayments()) {
				bodytmp.add(new String[] { i.getSender(), i.getReceiver(),
						Integer.toString((int) ((i.getSender().equals("COIIPA") ? -1 : 1) * p.getAmount())) });
			}
		}

		TableModel tm = new DefaultTableModel(header, bodytmp.size());
		for (int i = 0; i < bodytmp.size(); i++) {
			for (int j = 0; j < header.length; j++) {
				tm.setValueAt(bodytmp.get(i)[j], i, j);
			}
		}

		return tm;
	}*/

	public void showPayments() {
		String selectedProfessional = SwingUtil.getSelectedKey(view.getTable());
		List<AuxPayment> paymentList = model.getPayments(selectedRow.formativeAction.getName(), model.getData(view.getTable().getSelectedRow()));
		TableModel tmodel = SwingUtil.getTableModelFromPojos(paymentList, new String[] { "sender", "receiver","date", "amount" });
		view.getMovementsTable().setModel(tmodel);
		SwingUtil.autoAdjustColumns(view.getMovementsTable());
		
		

	/*	float amount = 0;
		for (AuxPayment payment : paymentList) {
			amount += payment.getAmount();
		}*/
		//view.getLabelSummary().setText("" + amount);

	}

}
