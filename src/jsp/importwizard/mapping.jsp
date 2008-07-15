<%@ page language="java" contentType="text/html; charset=utf-8" import="org.agnitas.util.*,java.util.*, org.agnitas.web.ImportWizardForm, org.agnitas.beans.Admin" %>
<%@ taglib uri="/WEB-INF/agnitas-taglib.tld" prefix="agn" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<agn:CheckLogon/>

<agn:Permission token="wizard.import"/>

<% ImportWizardForm aForm=null;
Hashtable aDbAllColumns=new Hashtable();
ArrayList aCsvList = null;
int aMode=0;
if((aForm=(ImportWizardForm)session.getAttribute("importWizardForm"))!=null) {
    aDbAllColumns=aForm.getDbAllColumns();
    aCsvList = aForm.getCsvAllColumns();
    aMode = aForm.getMode();
} %>

<% pageContext.setAttribute("sidemenu_active", new String("Recipient")); %>
<% pageContext.setAttribute("sidemenu_sub_active", new String("csv_upload")); %>
<% pageContext.setAttribute("agnTitleKey", new String("UploadSubscribers")); %>
<% pageContext.setAttribute("agnSubtitleKey", new String("UploadSubscribers")); %>
<% pageContext.setAttribute("agnNavigationKey", new String("subscriber_import")); %>
<% pageContext.setAttribute("agnHighlightKey", new String("ImportWizard")); %>

<%@include file="/header.jsp"%>
<html:errors/>
             

<html:form action="/importwizard" enctype="multipart/form-data">
    <html:hidden property="action"/>

    <b><font color=#73A2D0><bean:message key="ImportWizStep_3_of_7"/></font></b>
    <br>
    
    <table border="0" cellspacing="0" cellpadding="0" width="100%">
    
        <tr><td colspan="2"><b><bean:message key="CsvMappingMsg"/>:</b><br>&nbsp;</td></tr>

        <tr>
            <td><b><bean:message key="CsvColumn"/></b></td>
            <td>&nbsp;&nbsp;&nbsp;&nbsp;<b><bean:message key="DbColumn"/></b></td>
        </tr>

        <tr><td colspan="2"><hr></td></tr>

        <%
        String aktCsvColname=""; 
        CsvColInfo aCsvColInfo=null;
        for(int j=0; j<aCsvList.size(); j++) {
               aCsvColInfo=(CsvColInfo)aCsvList.get(j); %>
        <tr>
            <td><%=aCsvColInfo.getName()%></td>
            <td>&nbsp;&nbsp;
                <select name="<%=new String("map_"+(j+1))%>">
                    <option value="NOOP"><bean:message key="NoMapping"/></option>
                    <agn:ShowColumnInfo id="agnTbl" table="<%= AgnUtils.getCompanyID(request) %>" hide="creation_date, bounceload">
                        <%
                        String colName=(String) pageContext.getAttribute("_agnTbl_column_name");
                        // customer_id only when no new inserts are performed!
                        // (simplify this)
                        if( !colName.equals("CUSTOMER_ID") || (colName.equals("CUSTOMER_ID") && aMode!=ImportWizardForm.MODE_ADD && aMode!=ImportWizardForm.MODE_ADD_UPDATE) ) { %>
                            <option value="<%=new String(colName)%>" <%if(colName.trim().compareTo(aCsvColInfo.getName().trim())==0) { %>"selected"<% } %>><agn:GetColumnAlias column="<%= colName %>"/></option>
                        <% } %>
                        
                    </agn:ShowColumnInfo>
                </select>
            </td>
        </tr>
        <% } %>
        
        <tr>
            <td colspan="2">
                <hr>
                <html:image src="button?msg=Back"  border="0" property="mapping_back" value="mapping_back"/>
                &nbsp;&nbsp;&nbsp;
                <html:image src="button?msg=Proceed" border="0"/>
            </td>
        </tr>
        
    </table>                      

</html:form>

<%@include file="/footer.jsp"%>