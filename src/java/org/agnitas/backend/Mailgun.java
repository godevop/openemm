/*********************************************************************************
 * The contents of this file are subject to the Common Public Attribution
 * License Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.openemm.org/cpal1.html. The License is based on the Mozilla
 * Public License Version 1.1 but Sections 14 and 15 have been added to cover
 * use of software over a computer network and provide for limited attribution
 * for the Original Developer. In addition, Exhibit A has been modified to be
 * consistent with Exhibit B.
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenEMM.
 * The Original Developer is the Initial Developer.
 * The Initial Developer of the Original Code is AGNITAS AG. All portions of
 * the code written by AGNITAS AG are Copyright (c) 2007 AGNITAS AG. All Rights
 * Reserved.
 * 
 * Contributor(s): AGNITAS AG. 
 ********************************************************************************/
/*
 * Mailgun.java
 *
 * Created on 23. Mai 2006, 14:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.agnitas.backend;

import java.sql.Connection;
import java.util.Hashtable;

/**
 *
 * @author mhe
 */
public interface Mailgun {
    /**
     * Initialize internal data
     * @param status_id the string version of the statusID to use
     * @param conn optional open database connection
     */
    void initializeMailgun (String status_id, Connection conn) throws Exception;

    /**
     * Execute an already setup mailgun
     * 
     * @param conn optional open database connection
     * @param opts options to control the execution beyond DB information
     */
    void executeMailgun(Connection conn, Hashtable opts) throws Exception;

    /**
     * Full execution of a mail generation
     * 
     * @param custid optional customer id
     * @return Status string
     */
    String fire(String custid) throws Exception;

    /**
     * Setup a mailgun without starting generation
     * 
     * @param conn optional open database connection
     * @param opts options to control the setup beyond DB information
     */
    void prepareMailgun(Connection conn, Hashtable opts) throws Exception;
}
