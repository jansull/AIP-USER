package com.os.util.ssl;

import javax.net.ssl.SSLContext;

public interface SslBundle {
	SslStoreBundle getStores();

	SslManagerBundle getManagers();

	SSLContext createSslContext();


}
