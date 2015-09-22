// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.backup;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author zhoyilei
 *
 */
public class LogpieBackupManager
{
    private static final Logger LOG = Logger.getLogger(LogpieBackupManager.class);
    /* NOTE: Creating Database Constraints */
    private static final String sLogpieDBName = "Logpie";
    private static final String sDBUser = "root";
    private static final String sDBPassword = "root";

    private static final String sAliyunDbBackupBucketName = "logpie-db";

    @Async
    @Scheduled(fixedRate = 1000 * 3600 * 24)
    public void demoServiceMethod()
    {
        // Logpie will auto dump the database every 24 hours.
        backupDatabase();
    }

    public List<String> getBackupHistory()
    {
        final LogpieCloudStorage storage = new LogpieCloudStorage();
        return storage.getFileKeysInBucket(sAliyunDbBackupBucketName);
    }

    public boolean backupDatabase()
    {
        try
        {
            LOG.debug("Starting backup database");
            /* NOTE: Creating Path Constraints for backup saving */
            final Date currentDate = new Date();
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_hh-mm");

            final String fileKey = "LogpieDatabaseBackup" + format.format(currentDate);
            final String savePath = "/tmp/" + fileKey + ".sql";

            /* NOTE: Used to create a cmd command */
            final String executeCmd = "mysqldump -u " + sDBUser + " -p" + sDBPassword + " "
                    + sLogpieDBName + " > " + savePath;

            LOG.debug("Running mysql dump command:" + executeCmd);
            /* NOTE: Executing the command here */
            String[] cmdarray = { "/bin/sh", "-c", executeCmd };
            final Process runtimeProcess = Runtime.getRuntime().exec(cmdarray);
            final int processComplete = runtimeProcess.waitFor();

            /*
             * NOTE: processComplete=0 if correctly executed, will contain other
             * values if not
             */
            if (processComplete == 0)
            {
                LOG.debug("Backup Complete, start to upload to Aliyun OSS");
                final LogpieCloudStorage storage = new LogpieCloudStorage();
                storage.uploadFile(sAliyunDbBackupBucketName, fileKey + ".sql", savePath);
            }
            else
            {
                LOG.error("Error stream " + convertStreamToString(runtimeProcess.getErrorStream()));
                LOG.error("Backup Failure:" + processComplete);
                deleteFileAfterUploadToCloudStorage(savePath);
                return false;
            }
            // Delete the file after we already finish dumping the database.
            deleteFileAfterUploadToCloudStorage(savePath);
            return true;
        } catch (IOException | InterruptedException e)
        {
            LOG.debug("Exception happened when trying to dump db", e);
        }
        return false;
    }

    private void deleteFileAfterUploadToCloudStorage(final String filePath)
    {
        try
        {
            final String cmd = "rm " + filePath;
            final Process runtimeProcess = Runtime.getRuntime().exec(cmd);
            final int processComplete = runtimeProcess.waitFor();

            /*
             * NOTE: processComplete=0 if correctly executed, will contain other
             * values if not
             */
            if (processComplete == 0)
            {
                LOG.debug("Fail to delete the file");
                // TODO add an warning counter here.
            }
            else
            {
                LOG.error("Error stream " + convertStreamToString(runtimeProcess.getErrorStream()));
                LOG.error("Backup Failure:" + processComplete);
            }
        } catch (IOException | InterruptedException e)
        {
            LOG.debug("Exception happened when trying to delete dump file", e);
        }
    }

    private String convertStreamToString(final InputStream inputString)
    {
        final Scanner scanner = new Scanner(inputString);
        scanner.useDelimiter("\\A");
        final String string = scanner.hasNext() ? scanner.next() : "";
        scanner.close();
        return string;
    }
}
