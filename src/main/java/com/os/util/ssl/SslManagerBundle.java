package com.os.util.ssl;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public interface SslManagerBundle {

	KeyManager[] getKeyManagers();

	KeyManagerFactory getKeyManagerFactory();

	TrustManager[] getTrustManagers();

	TrustManagerFactory getTrustManagerFactory();

}