/*********************************************************************************
 * The contents of this file are subject to the OpenEMM Public License Version 1.1
 * ("License"); You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.agnitas.org/openemm.
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied.  See the License for
 * the specific language governing rights and limitations under the License.
 *
 * The Original Code is OpenEMM.
 * The Initial Developer of the Original Code is AGNITAS AG. Portions created by
 * AGNITAS AG are Copyright (C) 2006 AGNITAS AG. All Rights Reserved.
 *
 * All copies of the Covered Code must include on each user interface screen,
 * visible to all users at all times
 *    (a) the OpenEMM logo in the upper left corner and
 *    (b) the OpenEMM copyright notice at the very bottom center
 * See full license, exhibit B for requirements.
 ********************************************************************************/

package org.agnitas.beans;

import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.rowset.*;
import java.util.*;

/**
 * Bean representing the Status of a recipient on a mailinglist
 * 
 * @author Martin Helff, Andreas Rehak
 */
public interface BindingEntry extends org.springframework.context.ApplicationContextAware {
    
    int MEDIATYPE_EMAIL = 0;
    int MEDIATYPE_FAX = 1;
    int MEDIATYPE_MMS = 3;
    int MEDIATYPE_PRINT = 2;
    int MEDIATYPE_SMS = 4;
        
    /**
     * Global Constants
     */
    int USER_STATUS_ACTIVE = 1;
    int USER_STATUS_ADMINOUT = 3;
    int USER_STATUS_BOUNCED = 2;
    int USER_STATUS_OPTOUT = 4;
    int USER_STATUS_WAITING_FOR_CONFIRM = 5;
    String USER_TYPE_ADMIN="A";
    String USER_TYPE_TESTUSER="T";
    String USER_TYPE_WORLD="W";

    /**
     * Getter for property customerID.
     *
     * @return Value of property customerID.
     */
    int getCustomerID();

    /**
     * Getter for property mailinglistID.
     *
     * @return Value of property mailinglistID.
     */
    int getMailinglistID();

    /**
     * Getter for property userType.
     * 
     * @return Value of property userType.
     */
    String getUserType();

    /**
     * Getter for property userStatus.
     * 
     * @return Value of property userStatus.
     */
    int getUserStatus();

    /**
     * Getter for property userRemark.
     * 
     * @return Value of property userRemark.
     */
    String getUserRemark();

    /**
     * Getter for property changeDate.
     * 
     * @return Value of property changeDate.
     */
    Date getChangeDate();

    /**
     * Getter for property exitMailingID.
     * 
     * @return Value of property exitMailingID.
     */
    int getExitMailingID();

    /**
     * Getter for property mediaType.
     * 
     * @return Value of property mediaType.
     */
    int getMediaType();


    /**
     * Setter for property customerID.
     * 
     * @param ci New value of property customerID.
     */
    void setCustomerID(int ci);

    /**
     * Setter for property exitMailingID.
     * 
     * @param mi New value of property exitMailingID.
     */
    void setExitMailingID(int mi);

    /**
     * Setter for property mailinglistID.
     * 
     * @param ml New value of property mailinglistID.
     */
    void setMailinglistID(int ml);

    /**
     * Setter for property mediaType.
     * 
     * @param mediaType New value of property mediaType.
     */
    void setMediaType(int mediaType);

    /**
     * Setter for property userRemark.
     * 
     * @param remark New value of property userRemark.
     */
    void setUserRemark(String remark);

    /**
     * Setter for property changeDate.
     * 
     * @param ts New value of property changeDate.
     */
    void setChangeDate(Date ts);

    /**
     * Setter for property userStatus.
     *
     * @param us New value of property userStatus.
     */
    void setUserStatus(int us);

    /**
     * Setter for property userType.
     * 
     * @param ut New value of property userType.
     */
    void setUserType(String ut);

    /**
     * Inserts this Binding in the Database
     * 
     * @param companyID The company ID of the Binding
     * @return true on Sucess, false otherwise.
     */
    boolean insertNewBindingInDB(int companyID);

    /**
     * Updates this Binding in the Database
     * 
     * @param companyID The company ID of the Binding
     * @return true on Sucess, false otherwise.
     */
    boolean updateBindingInDB(int companyID);

    /**
     * Updates or Creates this Binding in the Database
     * 
     * @param companyID The company ID of the Binding
     * @param allCustLists bindings to check for save/update.
     * @return true on Sucess, false otherwise.
     */
    boolean saveBindingInDB(int companyID, Map allCustLists);

    /**
     * Updates the status of this Binding in the Database
     * 
     * @param companyID The company ID of the Binding
     * @return true on Sucess, false otherwise.
     */
    boolean updateStatusInDB(int companyID);
    
    /**
     * Check whether this binding is active on the given mailinglist.
     * 
     * @param companyID The company ID of the Binding
     * @return true if binding is active on the mailinglist, false otherwise.
     */
    boolean isActiveOnMailinglist(int mailinglistID, int companyID);

    /**
     * Mark binding as opted out.
     * 
     * @param email Emailaddress to set inactive.
     * @param companyID The company ID of the Binding
     * @return true if binding is active on the mailinglist, false otherwise.
     */
    boolean optOutEmailAdr(String email, int companyID);
}