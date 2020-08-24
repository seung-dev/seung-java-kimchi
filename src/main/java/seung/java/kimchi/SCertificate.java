package seung.java.kimchi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.cert.CertificateException;
import java.security.cert.Extension;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory;
import org.bouncycastle.util.encoders.Hex;

import seung.java.kimchi.util.SCharset;
import seung.java.kimchi.util.SKimchiException;
import seung.java.kimchi.util.SSignCertDer;

public class SCertificate {

	public SCertificate() {}
	
	public static SSignCertDer readSignCertDer(
			byte[] signCertDer
			) {
		return readSignCertDer(signCertDer, "yyyy-MM-dd HH:mm:ss.SSS", "Asia/Seoul");
	}
	public static SSignCertDer readSignCertDer(
			byte[] signCertDer
			, String datePattern
			, String timeZone
			) {
		
		SSignCertDer sSignCertDer = null;
		
		try(
				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(signCertDer);
				) {
			
			CertificateFactory certificateFactory = new CertificateFactory();
			X509Certificate x509Certificate = (X509Certificate) certificateFactory.engineGenerateCertificate(byteArrayInputStream);
			sSignCertDer = SSignCertDer.builder()
					.base64(SConvert.encodeBase64String(signCertDer))
					.hex(SConvert.encodeHexString(signCertDer, true))
					.type(x509Certificate.getType())
					.version(x509Certificate.getVersion())
					.serialNumber(Integer.toHexString(x509Certificate.getSerialNumber().intValue()))
					.sigAlgOID(x509Certificate.getSigAlgOID())
					.sigAlgName(x509Certificate.getSigAlgName())
					.issuerX500PrincipalName(x509Certificate.getIssuerX500Principal().getName())
					.subjectX500Principal(x509Certificate.getSubjectX500Principal().getName())
					.notBefore(SDate.getDateString(datePattern, x509Certificate.getNotBefore(), TimeZone.getTimeZone(timeZone)))
					.notAfter(SDate.getDateString(datePattern, x509Certificate.getNotAfter(), TimeZone.getTimeZone(timeZone)))
					.build()
					;
			
		} catch (IOException e) {
			sSignCertDer = SSignCertDer.builder()
					.errorMessage(ExceptionUtils.getStackTrace(e))
					.build()
					;
		} catch (CertificateException e) {
			sSignCertDer = SSignCertDer.builder()
					.errorMessage(ExceptionUtils.getStackTrace(e))
					.build()
					;
		}
		
		return sSignCertDer;
	}
	
	
	public static void main(String[] args) {
		
		byte[] der = SConvert.decodeBase64("MIIFhTCCBG2gAwIBAgIEB8HE1jANBgkqhkiG9w0BAQsFADBQMQswCQYDVQQGEwJLUjESMBAGA1UECgwJU2lnbktvcmVhMRUwEwYDVQQLDAxBY2NyZWRpdGVkQ0ExFjAUBgNVBAMMDVNpZ25Lb3JlYSBDQTMwHhcNMjAwMjA0MTIzMjU3WhcNMjEwMjEzMTQ1OTU5WjB4MQswCQYDVQQGEwJLUjESMBAGA1UECgwJU2lnbktvcmVhMREwDwYDVQQLDAhwZXJzb25hbDEMMAoGA1UECwwDS01CMTQwMgYDVQQDDCvrsJXsooXsirkoUGFyayBKb25nIFNldW5nKTAwMDQwMTVJMDAxMjU5MjA3MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvTL3J9ciiDWtS37D1gIB37PoYQ3Bnf1l29YC38sBos/AbdbVvaFlt7cjmx1LJ9oPADs5ULUzTuA+GfAlX4s3TM4ksj/efpueDcX+lQDjWr2oLeMimHql2q5bUYUX+HcdW8juiojf/hA2ILfFgOTLi4rsa1XR0YnOV8+hWf+8CfwN5O7Tw2vSbjyYNi534SmcwQImJYNLbJ9O0Fj8ps8e+/YwIFuFQDelSgfDEcVRckgz9+4qauFVMWaJyR4Zzmkd2+gv45aKHP+yTbF9emQhbAjIKlxx+DgI+htfjf5RkeHSk1Iq21jISDoP4ddkxmeSop0c3Ufsd0zzeGi8XmTPfwIDAQABo4ICPTCCAjkwgY8GA1UdIwSBhzCBhIAUBFRFsN4SxCecoE8CaYvVWxQUYwehaKRmMGQxCzAJBgNVBAYTAktSMQ0wCwYDVQQKDARLSVNBMS4wLAYDVQQLDCVLb3JlYSBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eSBDZW50cmFsMRYwFAYDVQQDDA1LSVNBIFJvb3RDQSA0ggIQIDAdBgNVHQ4EFgQUHL/fZFfA+8trGnZmA84WXTzL2PQwDgYDVR0PAQH/BAQDAgbAMHkGA1UdIAEB/wRvMG0wawYKKoMajJpEBQEBBTBdMC0GCCsGAQUFBwIBFiFodHRwOi8vd3d3LnNpZ25rb3JlYS5jb20vY3BzLmh0bWwwLAYIKwYBBQUHAgIwIB4ex3QAIMd4yZ3BHLKUACCs9cd4x3jJncEcx4WyyLLkMGgGA1UdEQRhMF+gXQYJKoMajJpECgEBoFAwTgwJ67CV7KKF7Iq5MEEwPwYKKoMajJpECgEBATAxMAsGCWCGSAFlAwQCAaAiBCC2VtHDcoRwbFH6G9mZdkj+phmZ8DSn6d8xRHI2QRU0VjBaBgNVHR8EUzBRME+gTaBLhklsZGFwOi8vZGlyLnNpZ25rb3JlYS5jb206Mzg5L291PWRwNXA1OTg4LG91PUFjY3JlZGl0ZWRDQSxvPVNpZ25Lb3JlYSxjPUtSMDUGCCsGAQUFBwEBBCkwJzAlBggrBgEFBQcwAYYZaHR0cDovL29jc3Auc2lnbmtvcmVhLmNvbTANBgkqhkiG9w0BAQsFAAOCAQEAGcSTSuD0/flQzvUMCDXtJO11bjjGBYEipJhR3GN6dMOFNl8B426AbfGHEp0g2gvuJcQNHiNJWrle4Wmmh3nwxj0vA3wSB9bijgfiYeHwgJZLeqr7fdYai5fV7lm6TV6rrdJZNHEmCADwIEzYGoGfty8wTo87t10nh/7eZ9qGMTQ54pEO2nbfpy/wjR+ciIchs21nnhEabR7ulF92V/AlpAqC8DtbnNUdPaARj4aOx3zEZPY7vNA3tXWwTb7OSu/cE/2GHbg9cRcSjzCTGyI5uVoHruHWYy/O1+z0bAj2WhzrV1DbADq3DWSTwwmwFd6PsOqhYTBb7D8fzj0qfkDY8A==");
		
		System.out.println(readSignCertDer(der).toJsonString(true));
//		X509Certificate x509Certificate = null;
//		
//		try(
//				ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(der);
//				) {
//			
//			
//			CertificateFactory certificateFactory = new CertificateFactory();
//			x509Certificate = (X509Certificate) certificateFactory.engineGenerateCertificate(byteArrayInputStream);
//			System.out.println(x509Certificate.getType());
//			System.out.println(x509Certificate.getVersion());
//			System.out.println(x509Certificate.getSerialNumber());
//			System.out.println(SConvert.encodeHexString("" + x509Certificate.getSerialNumber(), SCharset.UTF_8, false));
//			System.out.println(Integer.toHexString(x509Certificate.getSerialNumber().intValue()).toUpperCase());
//			System.out.println(x509Certificate.getSigAlgOID());
//			System.out.println(x509Certificate.getSigAlgName());
//			System.out.println(x509Certificate.getIssuerDN());
//			System.out.println(x509Certificate.getIssuerX500Principal().getName());
//			System.out.println(x509Certificate.getNotBefore());
//			System.out.println(x509Certificate.getNotAfter());
//			System.out.println(x509Certificate.getPublicKey());
//			System.out.println(Hex.toHexString(x509Certificate.getSignature()));
//			System.out.println(x509Certificate.getSubjectDN());
//			System.out.println(x509Certificate.getSubjectX500Principal().getName());
//			System.out.println(Hex.toHexString(x509Certificate.getEncoded()));
//			System.out.println(Hex.toHexString(x509Certificate.getTBSCertificate()));
//			ASN1ObjectIdentifier asn1ObjectIdentifier;
//			for(String criticalExtensionOIDs : x509Certificate.getCriticalExtensionOIDs()) {
//				for(Field field : Extension.class.getFields()) {
//					asn1ObjectIdentifier = (ASN1ObjectIdentifier) field.get(Extension.class);
//					if(criticalExtensionOIDs.equals(asn1ObjectIdentifier.getId())) {
//						System.out.println("??");
//						System.out.println(criticalExtensionOIDs);
//						System.out.println(field.getName());
//						break;
//					}
//				}
//			}
//			for(String criticalExtensionOIDs : x509Certificate.getNonCriticalExtensionOIDs()) {
//				for(Field field : Extension.class.getFields()) {
//					asn1ObjectIdentifier = (ASN1ObjectIdentifier) field.get(Extension.class);
//					if(criticalExtensionOIDs.equals(asn1ObjectIdentifier.getId())) {
//						System.out.println("??");
//						System.out.println(criticalExtensionOIDs);
//						System.out.println(field.getName());
//						break;
//					}
//				}
//			}
//			
//			for(String timeZone : TimeZone.getAvailableIDs()) {
//				System.out.println(timeZone);
//			}
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (CertificateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SKimchiException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	
}
