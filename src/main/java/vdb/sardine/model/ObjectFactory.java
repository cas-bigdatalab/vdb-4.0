//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.4-10/27/2009 06:09 PM(mockbuild)-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.12.23 at 06:27:19 PM PST 
//


package vdb.sardine.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the vdb.sardine.model package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Status_QNAME = new QName("DAV:", "status");
    private final static QName _Dst_QNAME = new QName("DAV:", "dst");
    private final static QName _Src_QNAME = new QName("DAV:", "src");
    private final static QName _Timeout_QNAME = new QName("DAV:", "timeout");
    private final static QName _Depth_QNAME = new QName("DAV:", "depth");
    private final static QName _Responsedescription_QNAME = new QName("DAV:", "responsedescription");
    private final static QName _Href_QNAME = new QName("DAV:", "href");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: vdb.sardine.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Propfind }
     * 
     */
    public Propfind createPropfind() {
        return new Propfind();
    }

    /**
     * Create an instance of {@link Keepalive }
     * 
     */
    public Keepalive createKeepalive() {
        return new Keepalive();
    }

    /**
     * Create an instance of {@link Supportedlock }
     * 
     */
    public Supportedlock createSupportedlock() {
        return new Supportedlock();
    }

    /**
     * Create an instance of {@link Propertyupdate }
     * 
     */
    public Propertyupdate createPropertyupdate() {
        return new Propertyupdate();
    }

    /**
     * Create an instance of {@link Lockinfo }
     * 
     */
    public Lockinfo createLockinfo() {
        return new Lockinfo();
    }

    /**
     * Create an instance of {@link Getlastmodified }
     * 
     */
    public Getlastmodified createGetlastmodified() {
        return new Getlastmodified();
    }

    /**
     * Create an instance of {@link Exclusive }
     * 
     */
    public Exclusive createExclusive() {
        return new Exclusive();
    }

    /**
     * Create an instance of {@link Getcontentlanguage }
     * 
     */
    public Getcontentlanguage createGetcontentlanguage() {
        return new Getcontentlanguage();
    }

    /**
     * Create an instance of {@link Locktoken }
     * 
     */
    public Locktoken createLocktoken() {
        return new Locktoken();
    }

    /**
     * Create an instance of {@link Propertybehavior }
     * 
     */
    public Propertybehavior createPropertybehavior() {
        return new Propertybehavior();
    }

    /**
     * Create an instance of {@link Propstat }
     * 
     */
    public Propstat createPropstat() {
        return new Propstat();
    }

    /**
     * Create an instance of {@link Multistatus }
     * 
     */
    public Multistatus createMultistatus() {
        return new Multistatus();
    }

    /**
     * Create an instance of {@link Prop }
     * 
     */
    public Prop createProp() {
        return new Prop();
    }

    /**
     * Create an instance of {@link Remove }
     * 
     */
    public Remove createRemove() {
        return new Remove();
    }

    /**
     * Create an instance of {@link Response }
     * 
     */
    public Response createResponse() {
        return new Response();
    }

    /**
     * Create an instance of {@link Omit }
     * 
     */
    public Omit createOmit() {
        return new Omit();
    }

    /**
     * Create an instance of {@link Shared }
     * 
     */
    public Shared createShared() {
        return new Shared();
    }

    /**
     * Create an instance of {@link Lockentry }
     * 
     */
    public Lockentry createLockentry() {
        return new Lockentry();
    }

    /**
     * Create an instance of {@link Collection }
     * 
     */
    public Collection createCollection() {
        return new Collection();
    }

    /**
     * Create an instance of {@link Locktype }
     * 
     */
    public Locktype createLocktype() {
        return new Locktype();
    }

    /**
     * Create an instance of {@link Error }
     * 
     */
    public Error createError() {
        return new Error();
    }

    /**
     * Create an instance of {@link Getetag }
     * 
     */
    public Getetag createGetetag() {
        return new Getetag();
    }

    /**
     * Create an instance of {@link Displayname }
     * 
     */
    public Displayname createDisplayname() {
        return new Displayname();
    }

    /**
     * Create an instance of {@link Getcontenttype }
     * 
     */
    public Getcontenttype createGetcontenttype() {
        return new Getcontenttype();
    }

    /**
     * Create an instance of {@link Allprop }
     * 
     */
    public Allprop createAllprop() {
        return new Allprop();
    }

    /**
     * Create an instance of {@link Link }
     * 
     */
    public Link createLink() {
        return new Link();
    }

    /**
     * Create an instance of {@link Owner }
     * 
     */
    public Owner createOwner() {
        return new Owner();
    }

    /**
     * Create an instance of {@link Lockscope }
     * 
     */
    public Lockscope createLockscope() {
        return new Lockscope();
    }

    /**
     * Create an instance of {@link Set }
     * 
     */
    public Set createSet() {
        return new Set();
    }

    /**
     * Create an instance of {@link Lockdiscovery }
     * 
     */
    public Lockdiscovery createLockdiscovery() {
        return new Lockdiscovery();
    }

    /**
     * Create an instance of {@link Write }
     * 
     */
    public Write createWrite() {
        return new Write();
    }

    /**
     * Create an instance of {@link Source }
     * 
     */
    public Source createSource() {
        return new Source();
    }

    /**
     * Create an instance of {@link Activelock }
     * 
     */
    public Activelock createActivelock() {
        return new Activelock();
    }

    /**
     * Create an instance of {@link Propname }
     * 
     */
    public Propname createPropname() {
        return new Propname();
    }

    /**
     * Create an instance of {@link Resourcetype }
     * 
     */
    public Resourcetype createResourcetype() {
        return new Resourcetype();
    }

    /**
     * Create an instance of {@link Getcontentlength }
     * 
     */
    public Getcontentlength createGetcontentlength() {
        return new Getcontentlength();
    }

    /**
     * Create an instance of {@link Creationdate }
     * 
     */
    public Creationdate createCreationdate() {
        return new Creationdate();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "DAV:", name = "status")
    public JAXBElement<String> createStatus(String value) {
        return new JAXBElement<String>(_Status_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "DAV:", name = "dst")
    public JAXBElement<String> createDst(String value) {
        return new JAXBElement<String>(_Dst_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "DAV:", name = "src")
    public JAXBElement<String> createSrc(String value) {
        return new JAXBElement<String>(_Src_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "DAV:", name = "timeout")
    public JAXBElement<String> createTimeout(String value) {
        return new JAXBElement<String>(_Timeout_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "DAV:", name = "depth")
    public JAXBElement<String> createDepth(String value) {
        return new JAXBElement<String>(_Depth_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "DAV:", name = "responsedescription")
    public JAXBElement<String> createResponsedescription(String value) {
        return new JAXBElement<String>(_Responsedescription_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "DAV:", name = "href")
    public JAXBElement<String> createHref(String value) {
        return new JAXBElement<String>(_Href_QNAME, String.class, null, value);
    }

}
