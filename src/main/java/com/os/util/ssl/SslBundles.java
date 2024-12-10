package com.os.util.ssl;

import org.springframework.boot.ssl.NoSuchSslBundleException;

public interface SslBundles {

	SslBundle getBundle(String bundleName) throws NoSuchSslBundleException;

}