/**
 * AdExclusionRuleServiceInterface.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Mar 02, 2009 (07:08:06 PST) WSDL2Java emitter.
 */

package com.google.api.ads.dfp.axis.v201411;

public interface AdExclusionRuleServiceInterface extends java.rmi.Remote {

    /**
     * Creates new {@link AdExclusionRule} objects.
     *         
     * @param adExclusionRules the ad exclusion rules to create
     *         
     * @return the created rules with their IDs filled in
     */
    public com.google.api.ads.dfp.axis.v201411.AdExclusionRule[] createAdExclusionRules(com.google.api.ads.dfp.axis.v201411.AdExclusionRule[] adExclusionRules) throws java.rmi.RemoteException, com.google.api.ads.dfp.axis.v201411.ApiException;

    /**
     * Gets a {@link AdExclusionRulePage} of {@link AdExclusionRule}
     * objects that satisfy the
     *         given {@link Statement#query}.  The following fields are supported
     * for
     *         filtering:
     *         
     *         <table>
     *         <tr>
     *         <th scope="col">PQL Property</th> <th scope="col">Object Property</th>
     * </tr>
     *         <tr>
     *         <td>{@code id}</td>
     *         <td>{@link AdExclusionRule#id}</td>
     *         </tr>
     *         <tr>
     *         <td>{@code name}</td>
     *         <td>{@link AdExclusionRule#name}</td>
     *         </tr>
     *         <tr>
     *         <td>{@code status}</td>
     *         <td>{@link AdExclusionRule#status}</td>
     *         </tr>
     *         </table>
     *         
     *         
     * @param filterStatement a Publisher Query Language statement used to
     * filter
     *         a set of rules
     *         
     * @return the rules that match the given filter
     */
    public com.google.api.ads.dfp.axis.v201411.AdExclusionRulePage getAdExclusionRulesByStatement(com.google.api.ads.dfp.axis.v201411.Statement filterStatement) throws java.rmi.RemoteException, com.google.api.ads.dfp.axis.v201411.ApiException;

    /**
     * Performs action on {@link AdExclusionRule} objects that satisfy
     * the
     *         given {@link Statement#query}.
     *         
     *         
     * @param adExclusionRuleAction the action to perform
     *         
     * @param filterStatement a Publisher Query Language statement used to
     * filter
     *         a set of ad exclusion rules
     *         
     * @return the result of the action performed
     */
    public com.google.api.ads.dfp.axis.v201411.UpdateResult performAdExclusionRuleAction(com.google.api.ads.dfp.axis.v201411.AdExclusionRuleAction adExclusionRuleAction, com.google.api.ads.dfp.axis.v201411.Statement filterStatement) throws java.rmi.RemoteException, com.google.api.ads.dfp.axis.v201411.ApiException;

    /**
     * Updates the specified {@link AdExclusionRule} objects.
     *         
     *         
     * @param adExclusionRules the ad exclusion rules to update
     *         
     * @return the updated rules
     */
    public com.google.api.ads.dfp.axis.v201411.AdExclusionRule[] updateAdExclusionRules(com.google.api.ads.dfp.axis.v201411.AdExclusionRule[] adExclusionRules) throws java.rmi.RemoteException, com.google.api.ads.dfp.axis.v201411.ApiException;
}
