// Copyright 2015 logpie.com. All rights reserved.
package com.logpie.shopping.management.auth.logic;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhoyilei
 *
 */
public class LogpiePageAlertMessage
{
    // For logpie management operation alert message
    public static String KEY_ACTION_MESSAGE_MAP = "action_message_map";
    public static String KEY_ACTION_MESSAGE = "action_message";
    public static String KEY_ACTION_RESULT = "action_result";

    public static Map<String, Object> createMessageMap(final boolean isSuccess, final String message)
    {
        final Map<String, Object> successMessageMap = new HashMap<String, Object>();
        successMessageMap.put(KEY_ACTION_RESULT, isSuccess);
        successMessageMap.put(KEY_ACTION_MESSAGE, message);
        return successMessageMap;
    }

}
