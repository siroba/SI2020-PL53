package RegisterPayment;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

import Utils.Database;
import Entities.Enrollment;
import Entities.Fee;
import Entities.FormativeAction;
import Entities.Movement;
import Entities.Payment;
import Entities.Professional;
import PL53.util.Date;

public class Model {

	private Database db = new Database();
	private Data[] data;

	public void initModel() throws SQLException, ParseException {
		data = initData();
	}

	/**
	 * Returns the indicated data from the array. Returns null if out of bounds.
	 * 
	 * @param i
	 * @return
	 */
	public Data getData(int i) {
		if (i < 0 || i > this.data.length - 1)
			return null;

		return this.data[i];
	}

	public ArrayList<Data> getAllData() {
		return new ArrayList<Data>(Arrays.asList(this.data));
	}

	private Data[] initData() throws SQLException, ParseException {
		List<Data> data = new ArrayList<Data>();

		String sql = "	SELECT  Invoice.* FROM Invoice WHERE Invoice.amount <> ("
				+ "	SELECT COALESCE((SELECT SUM (Payment.amount) FROM Payment GROUP BY Payment.ID_invoice "
				+ "HAVING Payment.ID_invoice=Invoice.ID_invoice), 0))";
		String queryFa = "SELECT * FROM FormativeAction WHERE ID_fa=";
		String queryProf = "SELECT * FROM Professional WHERE ID_professional=";

		List<Movement> invoices = Movement.get(sql, db);
		for (Movement in : invoices) {
			String queryEnr = "SELECT * FROM Enrollment WHERE ID_fa=" + in.getID_fa() + " AND ID_professional="
					+ in.getID_professional();
			String queryFee = "SELECT * FROM Fee WHERE ID_fa=" + in.getID_fa() + " AND category='";
			Data d = new Data();
			d.invoice = in;
			d.formativeAction = FormativeAction.getOne(queryFa + in.getID_fa(), db);
			d.professional = Professional.getOne(queryProf + in.getID_professional(), db);
			d.enrollment = Enrollment.getOne(queryEnr, db);
			d.fee = Fee.getOne(queryFee + d.enrollment.getGroup() + "'", db).getAmount();
			data.add(d);

		}

		Data data2[] = new Data[data.size()];
		return data.toArray(data2);
	}

	public List<Movement> getPayments(Data d) throws SQLException {

		String queryInvoice = "SELECT * FROM Invoice WHERE ID_fa=" + d.formativeAction.getID() + " AND ID_professional="
				+ d.professional.getID();

		return Movement.get(queryInvoice, db);
	}

	public boolean createPayment(Movement invoiceReturn, float toReturn, Date payDate, boolean cash, boolean confirmed,
			float totalAmountPayed) throws SQLException, ParseException {
		
		boolean enrollmentConfirmed = false;
		
		if (totalAmountPayed >= invoiceReturn.getAmount()) {
			String sql = "UPDATE Enrollment SET status='CONFIRMED' WHERE ID_fa=? AND ID_professional=?";
			db.executeUpdateQuery(sql, invoiceReturn.getID_fa(), invoiceReturn.getID_professional());
			enrollmentConfirmed = true;
		}
		
		// invoiceReturn.insert(db);
		int id_invoice = invoiceReturn.getID();
		Payment p = new Payment(id_invoice, toReturn, payDate, confirmed, cash, ""); // TODO: Description
		p.insert(db);
		
		return enrollmentConfirmed;
	}

	public void createPaymentRefund(int invoiceID, float toReturn, Date payDate, boolean cash, boolean confirmed)
			throws SQLException, ParseException {

		Payment p = new Payment(invoiceID, toReturn, payDate, confirmed, cash, ""); // TODO: Add description
		p.insert(db);
	}

	public List<AuxPayment> getAuxPayments(Data data2) throws SQLException {
		Connection cn = db.getConnection();

		PreparedStatement ps = cn.prepareStatement(
				"select fa.nameFa, i.sender, i.receiver, pay.amount, pay.datePay from Professional p "
						+ "inner join Enrollment e on p.ID_professional = e.ID_professional "
						+ "inner join FormativeAction fa on e.ID_fa = fa.ID_fa "
						+ "inner join Invoice i on e.ID_professional = i.ID_professional and e.ID_fa = i.ID_fa "
						+ "inner join Payment pay on i.ID_invoice = pay.ID_invoice "
						+ "where p.ID_professional=? AND fa.ID_fa=? ;");
		ps.setInt(1, data2.professional.getID());
		ps.setInt(2, data2.formativeAction.getID());

		ResultSet rs = ps.executeQuery();

		List<AuxPayment> listPayments = new ArrayList<>();

		while (rs.next()) {
			String sender = rs.getString("sender");
			String receiver = rs.getString("receiver");
			float amount = rs.getFloat("amount");

			// If the amount is negative, the payment was from COIIPA
			if (amount < 0) {
				String tmp = sender;
				sender = receiver;
				receiver = tmp;

				// Otherwise, if the sender is COIIPA, just set the amount to be negative
			} else if (sender.equals("COIIPA")) {
				amount *= -1;
			}

			listPayments.add(new AuxPayment(Date.parse(rs.getTimestamp("datePay")), amount, sender, receiver));
		}

		return listPayments;
	}

	/**
	 * Returns all the data but the invoices that have COIIPA as the sender
	 */
	public List<Data> getAllDataNoCoiipa() {
		List<Data> dataAux = new ArrayList<Data>();

		for (int i = 0; i < data.length; i++) {
			if (!data[i].invoice.getSender().equals("COIIPA"))
				dataAux.add(data[i]);
		}

		return dataAux;
	}

	public Data getDataNoCoiipa(int i) {
		int count = 0;

		for (int j = 0; j < data.length; j++) {
			if (!data[j].invoice.getSender().equals("COIIPA")) {
				if (count == i)
					return data[j];

				count++;
			}
		}

		return null;
	}

	public int getFreePlaces(int ID_fa) throws SQLException, ParseException {
		String sql = "SELECT FormativeAction.totalPlaces- COUNT(Enrollment.ID_fa) FROM Enrollment INNER JOIN FormativeAction ON FormativeAction.ID_fa = Enrollment.ID_fa WHERE Enrollment.ID_fa=? AND Enrollment.status<>'CANCELLED';";
		return (int) db.executeQueryArray(sql, ID_fa).get(0)[0];
	}

	/**
	 * The sum of all the negative payments
	 *
	 * @param d
	 * @return
	 */
	public float getAmountReturned(Data d) {

		String sql = "SELECT COALESCE((SELECT SUM (Payment.amount) FROM Payment "
				+ "INNER JOIN Invoice ON Payment.ID_invoice=Invoice.ID_Invoice AND Payment.amount < 0 "
				+ "GROUP BY Payment.ID_invoice "
				+ "HAVING Invoice.ID_fa=? AND Invoice.ID_professional=? AND Payment.amount < 0 ), 0.0);";

		double sumPayments =  (double) (db
				.executeQueryArray(sql, d.invoice.getID_fa(), d.invoice.getID_professional()).get(0)[0]);
		
		return (float)sumPayments;
	}

	/**
	 * The sum of all the positive payments
	 *
	 * @param d
	 * @return
	 * @throws SQLException 
	 */
	public float getAmountPaid(Data d) throws SQLException {
		String sql = "SELECT COALESCE((SELECT SUM (Payment.amount) FROM Payment "
				+ "INNER JOIN Invoice ON Payment.ID_invoice=Invoice.ID_Invoice AND Payment.amount >0 " 
				+ "GROUP BY Payment.ID_invoice "
				+ "HAVING Invoice.ID_fa=? AND Invoice.ID_professional=? AND Payment.amount >0 ), 0.0) as paid;";

		
		double sumPayments = (double) (db
				.executeQueryArray(sql, d.invoice.getID_fa(), d.invoice.getID_professional()).get(0)[0]);
		
		
		return (float) sumPayments;
	}

	/**
	 * The sum of all the payments (positive and negative)
	 *
	 * @param selectedRow
	 * @return
	 */
	public float getAmountTotalPaid(Data d) {

		String sql = "SELECT COALESCE((SELECT SUM (Payment.amount) FROM Payment "
				+ "INNER JOIN Invoice ON Payment.ID_invoice=Invoice.ID_Invoice " + " GROUP BY Payment.ID_invoice "
				+ "HAVING Invoice.ID_fa=? AND Invoice.ID_professional=?), 0.0);";

		float sumPayments = (float) ((double) (db
				.executeQueryArray(sql, d.invoice.getID_fa(), d.invoice.getID_professional()).get(0)[0]));
		
		return sumPayments;
	}
}
