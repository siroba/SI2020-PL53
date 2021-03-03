package UserStory13573;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
//import java.util.Date;
import java.util.List;
import java.time.Duration;

import javax.swing.table.TableModel;

import Entities.FormativeAction;
import Entities.Teacher;
import PL53.SI2020_PL53.Date;
import Utils.SwingUtil;
import Utils.Util;

public class Controller {
	private Model model;
	private View view;
	private String lastSelectedKey=""; //remembers the last selected row to restore it when changing the race table
	
	public Controller(Model m, View v) {
		this.model = m;
		this.view = v;
		//no model-specific initialization, only view-specific initialization
		this.initView();
	}
	
	/**
	 * Controller initialization: add event handlers to the UI objects.
	 * Each event handler is instantiated in the same way, so that it invokes a private method of this handler, enclosed in a generic exception handler to display windows.
	 * Each event handler is instantiated in the same way, so that it invokes a private method of this controller, enclosed in a generic exception handler to display popup windows when a problem or exception occurs.
	 * popup windows when a problem or controlled exception occurs.
	 */
	public void initController() {
		view.getCreateBtn().addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//does not use mouseClicked because when setting single selection in the race table
				//the user could drag the mouse over several rows and only the last one is of interest.
				SwingUtil.exceptionWrapper(() -> createFormativeAction());
			}
		});
	}
	
	
	public void initView() {
		//Open window (replaces the main generated by WindowBuilder)
		view.getFrame().setVisible(true); 

	}

	
	/**
	 * Create a new formative action object and add it to the db
	 */
	public void createFormativeAction() {
		
		// Reset warnings
		view.setWarningDay("");
		view.setWarningEnrollmentPeriodStart("");
		view.setWarningEnrollmentPeriodEnd("");
		view.setWarningEnrollmentPeriodStart2("");
		view.setWarningEnrollmentPeriodEnd2("");
		
		// Get dates
		Date dateFormativeAction = Date.parseString(view.getDayYear() + "-"+ view.getDayMonth() + "-" + view.getDayDay());
		Date dateEnrollStart = Date.parseString(view.getEnrollStartYear() + "-"+ view.getEnrollStartMonth() + "-" + view.getEnrollStartDay());
		Date dateEnrollEnd = Date.parseString(view.getEnrollEndYear() + "-"+ view.getEnrollEndMonth() + "-" + view.getEnrollEndDay());
		
		// TODO: Fix date validation bug resulting from custom date class 
//		// Validate dates  
//		if (validateDates(dateFormativeAction, dateEnrollStart, dateEnrollEnd)==false) {
//			return;
//		}
		
		// Get Teacher
		Teacher teacher = model.getTeacher(view.getTeacher());
		
		// Create new formative action and add it to DB 
		FormativeAction formativeAction = new FormativeAction(view.getName(), view.getObjectives(), view.getMainContents(),teacher, Float.parseFloat(view.getRemuneration()), view.getLocation(), dateFormativeAction, Integer.parseInt(view.getNumberOfHours()), Integer.parseInt(view.getSpaces()), dateEnrollStart, dateEnrollEnd, Float.parseFloat(view.getFee()));
		model.setFormativeAction(formativeAction);
		view.getFrame().setVisible(false); 
		
	}
	
	/**
	 * Check if the provided dates for the formative action, the start & end of the enrollment period are valid 
	 */
	public boolean validateDates(Date formativeAction,Date enrollStart, Date enrollEnd) {
		Date now = Date.now();
		long daysBetweenStartAction = daysBetween(enrollStart, formativeAction);
		long daysBetweenEndAction = daysBetween(enrollEnd, formativeAction);
		long daysBetweenStartEnd = daysBetween(enrollStart, enrollEnd);
		long daysBetweenNowStart = daysBetween(now, enrollStart);
		long daysBetweenNowEnd = daysBetween(now, enrollEnd);
		long daysBetweenNowAction = daysBetween(now, formativeAction);
		if (daysBetweenNowAction <= 0) {
			view.setWarningDay("Can't take place in the past");
			return false;
		}
		if (daysBetweenNowStart <= 0) {
			view.setWarningEnrollmentPeriodStart2("Can't start in the past");
			return false;
		}
		if (daysBetweenNowEnd <= 0) {
			view.setWarningEnrollmentPeriodEnd2("Can't end in the past");
			return false;
		}
		if (daysBetweenStartAction < 21) {
			view.setWarningEnrollmentPeriodStart2("Should begin at least 3 weeks before formative action");
			return false;
		}
		if (daysBetweenEndAction <= 0) {
			view.setWarningEnrollmentPeriodEnd2("Should end before formative action begins");
			return false;
		}
		if (daysBetweenStartEnd <= 0) {
			view.setWarningEnrollmentPeriodEnd2("Not enough time between start and end of enrollment period");
			return false;
		}
		
		return true; 
	}


	/**
	 * Compute the difference between 2 Dates in days 
	 */
	public int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	}
}
