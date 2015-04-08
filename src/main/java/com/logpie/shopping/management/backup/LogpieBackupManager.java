// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.backup;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
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

    // @Scheduled(fixedRate = 1000 * 3600 * 24)
    @Scheduled(fixedRate = 1000 * 10)
    public void demoServiceMethod()
    {
        backupDatabase();
    }

    public static void backupDatabase()
    {
        try
        {
            LOG.debug("Starting backup database");
            /* NOTE: Creating Path Constraints for backup saving */
            final Date currentDate = new Date();
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            final String fileKey = "LogpieDatabaseBackup" + format.format(currentDate);
            final String savePath = "/home/ec2-user/db-backup/" + fileKey + ".sql";

            /* NOTE: Used to create a cmd command */
            final String executeCmd = "mysqldump -u" + sDBUser + " -p" + sDBPassword
                    + " --database " + sLogpieDBName + " -r " + savePath;

            /* NOTE: Executing the command here */
            final Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            final int processComplete = runtimeProcess.waitFor();

            /*
             * NOTE: processComplete=0 if correctly executed, will contain other
             * values if not
             */
            if (processComplete == 0)
            {
                LOG.debug("Backup Complete, start to upload to Aliyun OSS");
                final LogpieCloudStorage storage = new LogpieCloudStorage();
                storage.uploadFile(sAliyunDbBackupBucketName, fileKey, savePath);
            }
            else
            {
                LOG.debug("Backup Failure");
            }

        } catch (IOException | InterruptedException ex)
        {
            LOG.debug("Backup Failure", ex);
        }
    }
}
