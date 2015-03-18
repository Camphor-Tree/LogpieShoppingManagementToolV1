// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

public class SimpleAPIConnection
{
    private static final Logger LOG = Logger.getLogger(SimpleAPIConnection.class);

    public static String doGetQuery(String stringUrl)
    {
        try
        {
            URL url = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Charset", "UTF-8");

            int responsecode = connection.getResponseCode();
            if (responsecode >= 200 && responsecode < 300)
            {
                LOG.debug("Sending 'GET' request to URL : " + url);

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
                in.close();
                String responseString = response.toString();
                // print result
                LOG.debug("Receiving response from server: " + responseString);

                return responseString;
            }
            else if (responsecode >= 300 && responsecode < 400)
            {
                LOG.debug("redirection happen when sending data to server. error code:"
                        + responsecode);
            }
            else if (responsecode >= 400 && responsecode < 500)
            {

                LOG.debug("client error happen when sending data to server. error code:"
                        + responsecode);
            }
            else if (responsecode >= 500)
            {

                LOG.debug("server error when sending data to server. error code:" + responsecode);
            }
            else if (responsecode == -1)
            {

                LOG.debug("no valid response code when sending data to server. error code:"
                        + responsecode);
            }
            else
            {

                LOG.debug("unknown error when sending data to server. error code:" + responsecode);
            }
            return null;
        } catch (MalformedURLException e)
        {
            LOG.debug("The query url is mal-formatted!", e);
            return null;
        } catch (IOException e)
        {
            LOG.debug("IOException when open the Connection", e);
            return null;
        }
    }
}