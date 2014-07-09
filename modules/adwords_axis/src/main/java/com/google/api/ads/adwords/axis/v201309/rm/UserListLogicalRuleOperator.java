/**
 * UserListLogicalRuleOperator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Mar 02, 2009 (07:08:06 PST) WSDL2Java emitter.
 */

package com.google.api.ads.adwords.axis.v201309.rm;

public class UserListLogicalRuleOperator implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected UserListLogicalRuleOperator(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _ALL = "ALL";
    public static final java.lang.String _ANY = "ANY";
    public static final java.lang.String _NONE = "NONE";
    public static final java.lang.String _UNKNOWN = "UNKNOWN";
    public static final UserListLogicalRuleOperator ALL = new UserListLogicalRuleOperator(_ALL);
    public static final UserListLogicalRuleOperator ANY = new UserListLogicalRuleOperator(_ANY);
    public static final UserListLogicalRuleOperator NONE = new UserListLogicalRuleOperator(_NONE);
    public static final UserListLogicalRuleOperator UNKNOWN = new UserListLogicalRuleOperator(_UNKNOWN);
    public java.lang.String getValue() { return _value_;}
    public static UserListLogicalRuleOperator fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        UserListLogicalRuleOperator enumeration = (UserListLogicalRuleOperator)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static UserListLogicalRuleOperator fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UserListLogicalRuleOperator.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/rm/v201309", "UserListLogicalRule.Operator"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}