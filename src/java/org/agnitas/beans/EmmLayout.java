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

package org.agnitas.beans;

import java.io.Serializable;

/**
 *
 * @author mhe
 */
public interface EmmLayout extends Serializable {
    /**
     * Getter for property baseUrl.
     * 
     * @return Value of property baseUrl.
     */
    String getBaseUrl();

    /**
     * Getter for property footerUrl.
     * 
     * @return Value of property footerUrl.
     */
    String getFooterUrl();

    /**
     * Getter for property headerUrl.
     * 
     * @return Value of property headerUrl.
     */
    String getHeaderUrl();

    /**
     * Getter for property highlightColor.
     * 
     * @return Value of property highlightColor.
     */
    String getHighlightColor();

    /**
     * Getter for property layoutID.
     * 
     * @return Value of property layoutID.
     */
    int getLayoutID();

    /**
     * Getter for property normalColor.
     * 
     * @return Value of property normalColor.
     */
    String getNormalColor();

    /**
     * Setter for property baseUrl.
     * 
     * @param baseUrl New value of property baseUrl.
     */
    void setBaseUrl(String baseUrl);

    /**
     * Setter for property footerUrl.
     * 
     * @param footerUrl New value of property footerUrl.
     */
    void setFooterUrl(String footerUrl);

    /**
     * Setter for property headerUrl.
     * 
     * @param headerUrl New value of property headerUrl.
     */
    void setHeaderUrl(String headerUrl);

    /**
     * Setter for property highlightColor.
     * 
     * @param highlightColor New value of property highlightColor.
     */
    void setHighlightColor(String highlightColor);

    /**
     * Setter for property layoutID.
     * 
     * @param layoutID New value of property layoutID.
     */
    void setLayoutID(int layoutID);

    /**
     * Setter for property normalColor.
     * 
     * @param normalColor New value of property normalColor.
     */
    void setNormalColor(String normalColor);

    /**
     * Getter for property companyID.
     *
     * @return Value of property companyID.
     */
    public int getCompanyID();

    /**
     * Setter for property companyID.
     *
     * @param companyID New value of property companyID.
     */
    public void setCompanyID(int companyID);
}
