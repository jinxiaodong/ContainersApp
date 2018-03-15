
package com.project.container.network.http;

import android.content.Context;

import com.project.container.network.cookie.CookieJarManager;
import com.project.container.network.cookie.PersistentCookieStore;

import java.net.CookieManager;
import java.net.CookiePolicy;


public class HttpConfig {
    /*******************************************************************************
     *	Public/Protected Variables
     *******************************************************************************/

    /**
     * default 0s
     */
    private static final int DEFAULT_READTIMEOUT = 30;
    /**
     * 15s
     */
    private static final int DEFAULT_WRITETIMEOUT = 15;
    /**
     * 15s
     */
    private static final int DEFAULT_CONNECTTIMEOUT = 15;
    /**
     * cookie
     */
    public CookieManager cookieHandler = null;
    /**
     * cookie
     * */
    public boolean supportCookie;
    /**
     *  30s
     */
    public static int readTimeOut = DEFAULT_READTIMEOUT;
    /**
     * 15s
     */
    public static int writeTimeOut = DEFAULT_WRITETIMEOUT;
    /**
     * 15s
     */
    public static int connectTimeOut = DEFAULT_CONNECTTIMEOUT;
    /**
     *
     */
    public long cacheSize = 10 * 1024 * 1024;
    /**
     *
     */
    public String cacheDirectory;
    /**
     *
     * */
    public boolean isSetCacheSize;
    /**
     *
     * */
    public CookieJarManager cookieJarManager;

    public boolean logDebug;
    /*******************************************************************************
     *	Private Variables
     *******************************************************************************/

    /*******************************************************************************
     *	Overrides From Base
     *******************************************************************************/

    /*******************************************************************************
     *	Public/Protected Methods
     *******************************************************************************/
    public HttpConfig(Context context) {
        cookieHandler =  new CookieManager(new PersistentCookieStore(context),
                CookiePolicy.ACCEPT_ORIGINAL_SERVER);
        cookieJarManager = new CookieJarManager(context);
        cacheDirectory = context.getCacheDir().getAbsolutePath();
    }
    /*******************************************************************************
     *	Private Methods
     *******************************************************************************/

    /*******************************************************************************
     *	Internal Class,Interface
     *******************************************************************************/
}
