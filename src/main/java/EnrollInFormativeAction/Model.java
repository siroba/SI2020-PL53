package EnrollInFormativeAction;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

import Entities.Enrollment;
import Entities.FormativeAction;
import Entities.Movement;
import Entities.Professional;
import Exceptions.InvalidFieldValue;
import Utils.Database;


public class Model {

    private Database db = new Database();
    private List<FormativeAction> formativeActions;

    public void loadFormativeActions() throws SQLException, ParseException {
        String query ="SELECT * FROM FormativeAction "
                    + "WHERE enrollmentEnd>datetime('now','localtime') "
                    + "GROUP BY FormativeAction.nameFa "
                    + "HAVING (SELECT COUNT(Enrollment.ID_fa) FROM Enrollment WHERE Enrollment.ID_fa=FormativeAction.ID_fa AND status<>'CANCELLED')<totalPlaces;";

        this.formativeActions = FormativeAction.get(query, db);
    }

    public List<FormativeAction> getFormativeActions(){
        return this.formativeActions;
    }

    public FormativeAction getFormativeAction(int n) {
        return this.formativeActions.get(n);
    }

    public Professional createProfessional(String name, String surname, String phone, String email) throws SQLException, InvalidFieldValue {
        if(!Professional.checkEmail(email)) throw new InvalidFieldValue("Email", email);
        else if(!Professional.checkPhone(phone)) throw new InvalidFieldValue("Phone", phone);

        Professional p = new Professional(name, surname, phone, email);

        return p;
    }

    public void doEnrollment(FormativeAction fa, String group, Professional p, Enrollment en , String address , String fiscalNumber, float fee) throws SQLException, ParseException {
        p.insert(db);
        en.setID_professional(p.getID());
        
        if(fee == 0)
        	en.setStatus(Enrollment.Status.CONFIRMED);
        
        en.insert(db);
        
        Movement i = new Movement(fa.getFee(group), en.getTimeEn(), p.getName(), "COIIPA" , address, fiscalNumber, en.getID_fa(), p.getID(), ""); // TODO: Description

        i.insert(db);
        
        
    }
}