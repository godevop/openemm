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
package org.agnitas.backend;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import org.agnitas.util.Log;

/** General parent class for all types of creating mail output
 */
abstract public class MailWriter {
    /** Reference to configuration */
    protected Data			data;
    /** Collection of all available blocks */
    protected BlockCollection	allBlocks;
    /** Interface to write billing information */
    protected BillingCounter	billingCounter;
    /** To create boundaries */
    protected String		fileSequence;
    /** Prefix for message IDs */
    protected String		messageIDStart;
    /** Boundary to separate text from html part */
    protected String		innerBoundary;
    /** Boundary to separate content from images */
    protected String		outerBoundary;
    /** Boundary to separate attachmantes from rest of mail */
    protected String		attachBoundary;
    /** Instance to create a unique ID, if required */
    protected URLMaker		uidMaker;
    /** Used in subclass to create pathnames */
    protected String		dirSeparator;
    /** Start time of writing mail */
    public Date			startExecutionTime;
    /** End time of writing mail */
    public Date			endExecutionTime;
    /** Start time of writing current block */
    public Date			startBlockTime;
    /** End time of writing current block */
    public Date			endBlockTime;

    /** number of mails written */
    public long			mailCount;
    /** max. number of receiver of a single block */
    public long			blockSize;
    /** number of blocks written */
    public long			blockCount;
    /** number of mails written in current block */
    protected long		inBlockCount;
    /** pattern for creating filenames for writing blocks */
    protected String		filenamePattern;

    /** mailtype for the current receiver */
    protected int		mailType;
    /** message ID for the current receiver */
    protected String		messageID;

    /** internal used, if set we need to increase number of created mails */
    private boolean			pending;
    /** to create unique filenamnes */
    private long			uniqts = 0;
    /** to create unique filenamnes */
    private long			uniqnr = 0;
    
    /** Create a unique number based on given timestamp
     * @param ts current timestamp
     * @return unique number as string
     */
    private synchronized String getUniqueNr (long ts) {
        if (uniqts == ts)
            ++uniqnr;
        else {
            uniqts = ts;
            uniqnr = 1;
        }
        return StringOps.format_number (Long.toString (uniqnr), 5);
    }

    /** Constructor
     * @param data reference to configuration
     * @param allBlocks reference to all content blocks
     */
    public MailWriter (Data data, BlockCollection allBlocks) throws Exception {
        this.data = data;
        this.allBlocks = allBlocks;

        Calendar	today;

        // Create some random number
        today = Calendar.getInstance ();

        // setup billing interface
        if (data.isAdminMailing () || data.isTestMailing () || data.isWorldMailing ()) {
            billingCounter = new BillingCounter(data);
            billingCounter.update_log (0);
        } else
            billingCounter = null;

        // Create pre-string for the two mail files
        fileSequence = StringOps.format_number (Long.toHexString (data.mailing_id).toUpperCase (), 6);
    
        // Create start of message id
        messageIDStart = Integer.toString (today.get (Calendar.YEAR)) + 
                StringOps.format_number (Integer.toString (today.get(Calendar.MONTH)+1), 2) +
                StringOps.format_number (Integer.toString (today.get(Calendar.DAY_OF_MONTH)), 2) +
                StringOps.format_number (Integer.toString (today.get(Calendar.HOUR_OF_DAY)),2) +
                StringOps.format_number (Integer.toString (today.get(Calendar.MINUTE)),2) +
                StringOps.format_number (Integer.toString (today.get(Calendar.SECOND)),2)
                ;

        // create boundaries
        innerBoundary = "-==" + data.boundary + "INNERB164240059B29" + fileSequence + "==";
        outerBoundary = "-==" + data.boundary + "OUTER164240059B29" + fileSequence + "==";
        attachBoundary = "-==" + data.boundary + "ATTACH164240059B29" + fileSequence + "==";
        uidMaker = new URLMaker (data);
        dirSeparator = System.getProperty ("file.separator");
        startExecutionTime = new Date ();
        endExecutionTime = null;
        startBlockTime = null;
        endBlockTime = null;

        // set counters
        mailCount = 0;
        blockSize = 0;
        blockCount = 0;
        inBlockCount = 0;
        filenamePattern = null;
        messageID = null;
        pending = false;
    }


    /** Cleanup
     */
    protected void done () throws Exception {
        endBlock ();
        
        if (billingCounter != null) {
            billingCounter.write_db (blockSize, blockCount);
            billingCounter.output ();
            billingCounter.done ();
            billingCounter = null;
        }
        endExecutionTime = new Date ();
    }
    
    /** Setup everything to start a new block
     */
    protected void startBlock () throws Exception {
        long	timestamp, now;

        ++blockCount;
        inBlockCount = 0;
        timestamp = data.sendSeconds;
        if (data.step > 0)
            if (data.isCampaignMailing ())
                timestamp += data.step * 60;
            else if (blockCount > 2)
                timestamp += (data.step * 60) * ((blockCount - 2) / data.blocksPerStep);

        now = System.currentTimeMillis () / 1000;
        
        if (timestamp < now)
            timestamp = now;
        data.currentSendDate = new Date (timestamp * 1000);
        
        String			tsstr;
        SimpleDateFormat	tmp;
            
        tmp = new SimpleDateFormat ("'D'yyyyMMddHHmmss");
        tsstr = tmp.format (data.currentSendDate).toString ();

        String	unique;
        
        if (data.isCampaignMailing ())
            unique = Long.toString (data.pass) + "C" + StringOps.format_number (Long.toString(data.campaignTransactionID > 0 ? data.campaignTransactionID : data.campaignCustomerID), 8);
        else if (data.isAdminMailing () || data.isTestMailing () || data.isPreviewMailing ())
            unique = getUniqueNr (timestamp);
        else
            unique = StringOps.format_number (Long.toString (blockCount), 3);
        filenamePattern = "AgnMail" +
                  data.getFilenameDetail () +
                  "=" + tsstr +
                  "=" + data.company_id +
                  "=" + data.mailing_id +
                  "=" + unique +
                  "=liaMngA";
        data.logging (Log.INFO, "writer", "Start block " + blockCount + " using blocksize " + blockSize);
        if (billingCounter != null)
            billingCounter.debug_out ();
        startBlockTime = new Date ();
    }
    
    /** Mark everything for ending the block
     */
    protected void endBlock () throws Exception {
        endBlockTime = new Date ();
        data.logging (Log.INFO, "writer", "End block " + blockCount);
    }
    
    /** Check if we need to create a new block
     * @param force force start of a new block
     */
    protected void checkBlock (boolean force) throws Exception {
        if (blockCount == 0)
            startBlock ();
        if ((blockSize > 0) && (force || (inBlockCount >= blockSize))) {
            endBlock ();
            startBlock ();
        }
    }

    /** When a mail is written, increase counter
     */
    protected void writeMailDone () {
        if (pending) {
            pending = false;
            ++mailCount;
            ++inBlockCount;
        }
    }

    /** Write information for a single receiver
     * @param cinfo Information about the customer
     * @param mcount if more than one mail is written for this receiver
     * @param mailtype the mailtype for this receiver
     * @param icustomer_id the customer ID
     * @param tag_names the available tags
     * @param urlMaker to create the URLs
     */
    protected void writeMail (Custinfo cinfo,
                  int mcount, int mailtype, long icustomer_id,
                  String mediatypes, Hashtable tag_names,
                  URLMaker urlMaker) throws Exception {
        EMMTag	mid, uid;

        writeMailDone ();
        mailType = mailtype;
        uidMaker.setPrefix (messageIDStart + "-" + (mcount > 0 ? Integer.toString (mcount) : "") + mailtype);
        uidMaker.setCustomerID (icustomer_id);
        uidMaker.setURLID (0);
        messageID = uidMaker.makeUID () + "@" + data.domain;
        mid = (EMMTag) tag_names.get (EMMTag.internalTag (EMMTag.TI_MESSAGEID));
        if (mid != null) {
            mid.mTagValue = messageID;
        }
        uid = (EMMTag) tag_names.get (EMMTag.internalTag (EMMTag.TI_UID));
        if (uid != null) {
            uidMaker.setPrefix (null);
            uid.mTagValue = uidMaker.makeUID ();
        }
        checkBlock (false);
        pending = true;
    }
}
