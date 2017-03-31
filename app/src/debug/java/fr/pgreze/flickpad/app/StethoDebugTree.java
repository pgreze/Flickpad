package fr.pgreze.flickpad.app;

import android.util.Log;

import com.facebook.stetho.inspector.console.ConsolePeerManager;
import com.facebook.stetho.inspector.helper.ChromePeerManager;
import com.facebook.stetho.inspector.protocol.module.Console;

import timber.log.Timber;

/**
 * Combination of Timber.DebugTree and StethoTree that:
 * (1) Avoids duplicate logs to logcat.
 * (2) Adds the line number to the logs that Timber.DebugTree sends to logcat.
 * (3) Adds the class name and line number to the logs that StethoTree sends to Chrome.
 * <p></p>
 *
 * You can find Timber.DebugTree and StethoTree here:
 * https://github.com/JakeWharton/timber/blob/master/timber/src/main/java/timber/log/Timber.java
 * https://github.com/facebook/stetho/blob/master/stetho-timber/src/main/java/com/facebook/stetho/timber/StethoTree.java
 * <p></p>
 *
 * To use it add this to your Application#onCreate():
 * <pre>
 * {@code
 * if (BuildConfig.DEBUG) {
 *       Stetho.initializeWithDefaults(this);
 *       Timber.plant(new StethoDebugTree())
 *    }
 * }
 * </pre>
 *
 * Created by Albert Vila Calvo on 9/12/16.
 */
public class StethoDebugTree extends Timber.DebugTree {

    @Override
    protected String createStackElementTag(StackTraceElement element) {
        return super.createStackElementTag(element) + "(" + element.getLineNumber() +")";
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        // This logs to logcat.
        super.log(priority, tag, message, t);

        // This logs to Chrome.
        // The following code is copied from
        // https://github.com/facebook/stetho/blob/master/stetho-timber/src/main/java/com/facebook/stetho/timber/StethoTree.java
        // but we use CLogCustom to avoid logging to logcat again, and we add the tag so that we print the
        // class name and line number.
        ConsolePeerManager peerManager = ConsolePeerManager.getInstanceOrNull();
        if (peerManager == null) {
            return;
        }

        Console.MessageLevel logLevel;

        switch (priority) {
            case Log.VERBOSE:
            case Log.DEBUG:
                logLevel = Console.MessageLevel.DEBUG;
                break;
            case Log.INFO:
                logLevel = Console.MessageLevel.LOG;
                break;
            case Log.WARN:
                logLevel = Console.MessageLevel.WARNING;
                break;
            case Log.ERROR:
            case Log.ASSERT:
                logLevel = Console.MessageLevel.ERROR;
                break;
            default:
                logLevel = Console.MessageLevel.LOG;
        }

        CLogCustom.writeToConsole(
                logLevel,
                Console.MessageSource.OTHER,
                tag + ": " + message
        );
    }

    /**
     * Copy-paste of:
     * https://github.com/facebook/stetho/blob/master/stetho/src/main/java/com/facebook/stetho/inspector/console/CLog.java
     * But removing the line:
     * LogRedirector.d(TAG, messageText);
     * Which avoids logging to logcat.
     */
    private static class CLogCustom {

        static void writeToConsole(
                ChromePeerManager chromePeerManager,
                Console.MessageLevel logLevel,
                Console.MessageSource messageSource,
                String messageText) {

            Console.ConsoleMessage message = new Console.ConsoleMessage();
            message.source = messageSource;
            message.level = logLevel;
            message.text = messageText;
            Console.MessageAddedRequest messageAddedRequest = new Console.MessageAddedRequest();
            messageAddedRequest.message = message;
            chromePeerManager.sendNotificationToPeers("Console.messageAdded", messageAddedRequest);
        }

        static void writeToConsole(
                Console.MessageLevel logLevel,
                Console.MessageSource messageSource,
                String messageText
        ) {
            ConsolePeerManager peerManager = ConsolePeerManager.getInstanceOrNull();
            if (peerManager == null) {
                return;
            }

            writeToConsole(peerManager, logLevel, messageSource, messageText);
        }
    }

}