package com.os.util.ssl;

import java.security.KeyStore;

public interface SslStoreBundle {

    KeyStore getKeyStore();

    String getKeyStorePassword();

    KeyStore getTrustStore();

}
