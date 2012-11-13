/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.net.http;

<<<<<<< HEAD

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.DefaultHostnameVerifier;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.X509TrustManager;
import org.apache.harmony.security.provider.cert.X509CertImpl;
import org.apache.harmony.xnet.provider.jsse.SSLParametersImpl;
import org.apache.harmony.xnet.provider.jsse.TrustManagerImpl;

/**
 * Class responsible for all server certificate validation functionality
 *
 * {@hide}
 */
public class CertificateChainValidator {
=======
import android.os.SystemClock;

import java.io.IOException;

import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpHost;

import org.bouncycastle.asn1.x509.X509Name;

/**
 * Class responsible for all server certificate validation functionality
 * 
 * {@hide}
 */
class CertificateChainValidator {

    private static long sTotal = 0;
    private static long sTotalReused = 0;
>>>>>>> 54b6cfa... Initial Contribution

    /**
     * The singleton instance of the certificate chain validator
     */
<<<<<<< HEAD
    private static final CertificateChainValidator sInstance
            = new CertificateChainValidator();

    private static final DefaultHostnameVerifier sVerifier
            = new DefaultHostnameVerifier();

    /**
     * @return The singleton instance of the certificates chain validator
     */
    public static CertificateChainValidator getInstance() {
=======
    private static CertificateChainValidator sInstance;

    /**
     * Default trust manager (used to perform CA certificate validation)
     */
    private X509TrustManager mDefaultTrustManager;

    /**
     * @return The singleton instance of the certificator chain validator
     */
    public static CertificateChainValidator getInstance() {
        if (sInstance == null) {
            sInstance = new CertificateChainValidator();
        }

>>>>>>> 54b6cfa... Initial Contribution
        return sInstance;
    }

    /**
<<<<<<< HEAD
     * Creates a new certificate chain validator. This is a private constructor.
     * If you need a Certificate chain validator, call getInstance().
     */
    private CertificateChainValidator() {}

    /**
     * Performs the handshake and server certificates validation
     * Notice a new chain will be rebuilt by tracing the issuer and subject
     * before calling checkServerTrusted().
     * And if the last traced certificate is self issued and it is expired, it
     * will be dropped.
=======
     * Creates a new certificate chain validator. This is a pivate constructor.
     * If you need a Certificate chain validator, call getInstance().
     */
    private CertificateChainValidator() {
        try {
            TrustManagerFactory trustManagerFactory
                = TrustManagerFactory.getInstance("X509");
            trustManagerFactory.init((KeyStore)null);
            TrustManager[] trustManagers =
                trustManagerFactory.getTrustManagers();
            if (trustManagers != null && trustManagers.length > 0) {
                for (TrustManager trustManager : trustManagers) {
                    if (trustManager instanceof X509TrustManager) {
                        mDefaultTrustManager = (X509TrustManager)(trustManager);
                        break;
                    }
                }
            }
        } catch (Exception exc) {
            if (HttpLog.LOGV) {
                HttpLog.v("CertificateChainValidator():" +
                          " failed to initialize the trust manager");
            }
        }
    }

    /**
     * Performs the handshake and server certificates validation
>>>>>>> 54b6cfa... Initial Contribution
     * @param sslSocket The secure connection socket
     * @param domain The website domain
     * @return An SSL error object if there is an error and null otherwise
     */
    public SslError doHandshakeAndValidateServerCertificates(
<<<<<<< HEAD
            HttpsConnection connection, SSLSocket sslSocket, String domain)
            throws IOException {
        // get a valid SSLSession, close the socket if we fail
        SSLSession sslSession = sslSocket.getSession();
        if (!sslSession.isValid()) {
            closeSocketThrowException(sslSocket, "failed to perform SSL handshake");
        }

        // retrieve the chain of the server peer certificates
        Certificate[] peerCertificates =
            sslSocket.getSession().getPeerCertificates();

        if (peerCertificates == null || peerCertificates.length == 0) {
            closeSocketThrowException(
                sslSocket, "failed to retrieve peer certificates");
        } else {
            // update the SSL certificate associated with the connection
            if (connection != null) {
                if (peerCertificates[0] != null) {
                    connection.setCertificate(
                        new SslCertificate((X509Certificate)peerCertificates[0]));
                }
            }
        }

        return verifyServerDomainAndCertificates((X509Certificate[]) peerCertificates, domain, "RSA");
    }

    /**
     * Similar to doHandshakeAndValidateServerCertificates but exposed to JNI for use
     * by Chromium HTTPS stack to validate the cert chain.
     * @param certChain The bytes for certificates in ASN.1 DER encoded certificates format.
     * @param domain The full website hostname and domain
     * @param authType The authentication type for the cert chain
     * @return An SSL error object if there is an error and null otherwise
     */
    public static SslError verifyServerCertificates(
        byte[][] certChain, String domain, String authType)
        throws IOException {

        if (certChain == null || certChain.length == 0) {
            throw new IllegalArgumentException("bad certificate chain");
        }

        X509Certificate[] serverCertificates = new X509Certificate[certChain.length];

        for (int i = 0; i < certChain.length; ++i) {
            serverCertificates[i] = new X509CertImpl(certChain[i]);
        }

        return verifyServerDomainAndCertificates(serverCertificates, domain, authType);
    }

    /**
     * Handles updates to credential storage.
     */
    public static void handleTrustStorageUpdate() {

        try {
            X509TrustManager x509TrustManager = SSLParametersImpl.getDefaultTrustManager();
            if( x509TrustManager instanceof TrustManagerImpl ) {
                TrustManagerImpl trustManager = (TrustManagerImpl) x509TrustManager;
                trustManager.handleTrustStorageUpdate();
            }
        } catch (KeyManagementException ignored) {
        }
    }

    /**
     * Common code of doHandshakeAndValidateServerCertificates and verifyServerCertificates.
     * Calls DomainNamevalidator to verify the domain, and TrustManager to verify the certs.
     * @param chain the cert chain in X509 cert format.
     * @param domain The full website hostname and domain
     * @param authType The authentication type for the cert chain
     * @return An SSL error object if there is an error and null otherwise
     */
    private static SslError verifyServerDomainAndCertificates(
            X509Certificate[] chain, String domain, String authType)
            throws IOException {
        // check if the first certificate in the chain is for this site
        X509Certificate currCertificate = chain[0];
        if (currCertificate == null) {
            throw new IllegalArgumentException("certificate for this site is null");
        }

        boolean valid = domain != null
                && !domain.isEmpty()
                && sVerifier.verify(domain, currCertificate);
        if (!valid) {
            if (HttpLog.LOGV) {
                HttpLog.v("certificate not for this host: " + domain);
            }
            return new SslError(SslError.SSL_IDMISMATCH, currCertificate);
        }

        try {
            SSLParametersImpl.getDefaultTrustManager().checkServerTrusted(chain, authType);
            return null;  // No errors.
        } catch (GeneralSecurityException e) {
            if (HttpLog.LOGV) {
                HttpLog.v("failed to validate the certificate chain, error: " +
                    e.getMessage());
            }
            return new SslError(SslError.SSL_UNTRUSTED, currCertificate);
        }
    }


    private void closeSocketThrowException(
            SSLSocket socket, String errorMessage, String defaultErrorMessage)
            throws IOException {
=======
        HttpsConnection connection, SSLSocket sslSocket, String domain)
        throws SSLHandshakeException, IOException {

        ++sTotal;

        SSLContext sslContext = HttpsConnection.getContext();
        if (sslContext == null) {
            closeSocketThrowException(sslSocket, "SSL context is null");
        }

        X509Certificate[] serverCertificates = null;

        long sessionBeforeHandshakeLastAccessedTime = 0;
        byte[] sessionBeforeHandshakeId = null;

        SSLSession sessionAfterHandshake = null;

        synchronized(sslContext) {
            // get SSL session before the handshake
            SSLSession sessionBeforeHandshake =
                getSSLSession(sslContext, connection.getHost());
            if (sessionBeforeHandshake != null) {
                sessionBeforeHandshakeLastAccessedTime =
                    sessionBeforeHandshake.getLastAccessedTime();

                sessionBeforeHandshakeId =
                    sessionBeforeHandshake.getId();
            }

            // start handshake, close the socket if we fail
            try {
                sslSocket.setUseClientMode(true);
                sslSocket.startHandshake();
            } catch (IOException e) {
                closeSocketThrowException(
                    sslSocket, e.getMessage(),
                    "failed to perform SSL handshake");
            }

            // retrieve the chain of the server peer certificates
            Certificate[] peerCertificates =
                sslSocket.getSession().getPeerCertificates();

            if (peerCertificates == null || peerCertificates.length <= 0) {
                closeSocketThrowException(
                    sslSocket, "failed to retrieve peer certificates");
            } else {
                serverCertificates =
                    new X509Certificate[peerCertificates.length];
                for (int i = 0; i < peerCertificates.length; ++i) {
                    serverCertificates[i] =
                        (X509Certificate)(peerCertificates[i]);
                }

                // update the SSL certificate associated with the connection
                if (connection != null) {
                    if (serverCertificates[0] != null) {
                        connection.setCertificate(
                            new SslCertificate(serverCertificates[0]));
                    }
                }
            }

            // get SSL session after the handshake
            sessionAfterHandshake =
                getSSLSession(sslContext, connection.getHost());
        }

        if (sessionBeforeHandshakeLastAccessedTime != 0 &&
            sessionAfterHandshake != null &&
            Arrays.equals(
                sessionBeforeHandshakeId, sessionAfterHandshake.getId()) &&
            sessionBeforeHandshakeLastAccessedTime <
            sessionAfterHandshake.getLastAccessedTime()) {

            if (HttpLog.LOGV) {
                HttpLog.v("SSL session was reused: total reused: "
                          + sTotalReused
                          + " out of total of: " + sTotal);

                ++sTotalReused;
            }

            // no errors!!!
            return null;
        }

        // check if the first certificate in the chain is for this site
        X509Certificate currCertificate = serverCertificates[0];
        if (currCertificate == null) {
            closeSocketThrowException(
                sslSocket, "certificate for this site is null");
        } else {
            if (!DomainNameChecker.match(currCertificate, domain)) {
                String errorMessage = "certificate not for this host: " + domain;

                if (HttpLog.LOGV) {
                    HttpLog.v(errorMessage);
                }

                sslSocket.getSession().invalidate();
                return new SslError(
                    SslError.SSL_IDMISMATCH, currCertificate);
            }
        }

        //
        // first, we validate the chain using the standard validation
        // solution; if we do not find any errors, we are done; if we
        // fail the standard validation, we re-validate again below,
        // this time trying to retrieve any individual errors we can
        // report back to the user.
        //
        try {
            synchronized (mDefaultTrustManager) {
                mDefaultTrustManager.checkServerTrusted(
                    serverCertificates, "RSA");

                // no errors!!!
                return null;
            }
        } catch (CertificateException e) {
            if (HttpLog.LOGV) {
                HttpLog.v(
                    "failed to pre-validate the certificate chain, error: " +
                    e.getMessage());
            }
        }

        sslSocket.getSession().invalidate();

        SslError error = null;

        // we check the root certificate separately from the rest of the
        // chain; this is because we need to know what certificate in
        // the chain resulted in an error if any
        currCertificate =
            serverCertificates[serverCertificates.length - 1];
        if (currCertificate == null) {
            closeSocketThrowException(
                sslSocket, "root certificate is null");
        }

        // check if the last certificate in the chain (root) is trusted
        X509Certificate[] rootCertificateChain = { currCertificate };
        try {
            synchronized (mDefaultTrustManager) {
                mDefaultTrustManager.checkServerTrusted(
                    rootCertificateChain, "RSA");
            }
        } catch (CertificateExpiredException e) {
            String errorMessage = e.getMessage();
            if (errorMessage == null) {
                errorMessage = "root certificate has expired";
            }

            if (HttpLog.LOGV) {
                HttpLog.v(errorMessage);
            }

            error = new SslError(
                SslError.SSL_EXPIRED, currCertificate);
        } catch (CertificateNotYetValidException e) {
            String errorMessage = e.getMessage();
            if (errorMessage == null) {
                errorMessage = "root certificate not valid yet";
            }

            if (HttpLog.LOGV) {
                HttpLog.v(errorMessage);
            }

            error = new SslError(
                SslError.SSL_NOTYETVALID, currCertificate);
        } catch (CertificateException e) {
            String errorMessage = e.getMessage();
            if (errorMessage == null) {
                errorMessage = "root certificate not trusted";
            }

            if (HttpLog.LOGV) {
                HttpLog.v(errorMessage);
            }

            return new SslError(
                SslError.SSL_UNTRUSTED, currCertificate);
        }

        // Then go through the certificate chain checking that each
        // certificate trusts the next and that each certificate is
        // within its valid date range. Walk the chain in the order
        // from the CA to the end-user
        X509Certificate prevCertificate =
            serverCertificates[serverCertificates.length - 1];

        for (int i = serverCertificates.length - 2; i >= 0; --i) {
            currCertificate = serverCertificates[i];

            // if a certificate is null, we cannot verify the chain
            if (currCertificate == null) {
                closeSocketThrowException(
                    sslSocket, "null certificate in the chain");
            }

            // verify if trusted by chain
            if (!prevCertificate.getSubjectDN().equals(
                    currCertificate.getIssuerDN())) {
                String errorMessage = "not trusted by chain";

                if (HttpLog.LOGV) {
                    HttpLog.v(errorMessage);
                }

                return new SslError(
                    SslError.SSL_UNTRUSTED, currCertificate);
            }

            try {
                currCertificate.verify(prevCertificate.getPublicKey());
            } catch (GeneralSecurityException e) {
                String errorMessage = e.getMessage();
                if (errorMessage == null) {
                    errorMessage = "not trusted by chain";
                }

                if (HttpLog.LOGV) {
                    HttpLog.v(errorMessage);
                }

                return new SslError(
                    SslError.SSL_UNTRUSTED, currCertificate);
            }

            // verify if the dates are valid
            try {
              currCertificate.checkValidity();
            } catch (CertificateExpiredException e) {
                String errorMessage = e.getMessage();
                if (errorMessage == null) {
                    errorMessage = "certificate expired";
                }

                if (HttpLog.LOGV) {
                    HttpLog.v(errorMessage);
                }

                if (error == null ||
                    error.getPrimaryError() < SslError.SSL_EXPIRED) {
                    error = new SslError(
                        SslError.SSL_EXPIRED, currCertificate);
                }
            } catch (CertificateNotYetValidException e) {
                String errorMessage = e.getMessage();
                if (errorMessage == null) {
                    errorMessage = "certificate not valid yet";
                }

                if (HttpLog.LOGV) {
                    HttpLog.v(errorMessage);
                }

                if (error == null ||
                    error.getPrimaryError() < SslError.SSL_NOTYETVALID) {
                    error = new SslError(
                        SslError.SSL_NOTYETVALID, currCertificate);
                }
            }

            prevCertificate = currCertificate;
        }

        // if we do not have an error to report back to the user, throw
        // an exception (a generic error will be reported instead)
        if (error == null) {
            closeSocketThrowException(
                sslSocket,
                "failed to pre-validate the certificate chain due to a non-standard error");
        }

        return error;
    }

    private void closeSocketThrowException(
        SSLSocket socket, String errorMessage, String defaultErrorMessage)
        throws SSLHandshakeException, IOException {
>>>>>>> 54b6cfa... Initial Contribution
        closeSocketThrowException(
            socket, errorMessage != null ? errorMessage : defaultErrorMessage);
    }

<<<<<<< HEAD
    private void closeSocketThrowException(SSLSocket socket,
            String errorMessage) throws IOException {
=======
    private void closeSocketThrowException(SSLSocket socket, String errorMessage)
        throws SSLHandshakeException, IOException {
>>>>>>> 54b6cfa... Initial Contribution
        if (HttpLog.LOGV) {
            HttpLog.v("validation error: " + errorMessage);
        }

        if (socket != null) {
            SSLSession session = socket.getSession();
            if (session != null) {
                session.invalidate();
            }

            socket.close();
        }

        throw new SSLHandshakeException(errorMessage);
    }
<<<<<<< HEAD
=======

    /**
     * @param sslContext The SSL context shared accross all the SSL sessions
     * @param host The host associated with the session
     * @return A suitable SSL session from the SSL context
     */
    private SSLSession getSSLSession(SSLContext sslContext, HttpHost host) {
        if (sslContext != null && host != null) {
            Enumeration en = sslContext.getClientSessionContext().getIds();
            while (en.hasMoreElements()) {
                byte[] id = (byte[]) en.nextElement();
                if (id != null) {
                    SSLSession session =
                        sslContext.getClientSessionContext().getSession(id);
                    if (session.isValid() &&
                        host.getHostName().equals(session.getPeerHost()) &&
                        host.getPort() == session.getPeerPort()) {
                        return session;
                    }
                }
            }
        }

        return null;
    }
>>>>>>> 54b6cfa... Initial Contribution
}
