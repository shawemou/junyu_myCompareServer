package com.my.pojo;

import java.util.Date;

public class TMobileLoginLog {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_MOBILE_LOGIN_LOG.GUID
     *
     * @mbggenerated Tue Jun 19 16:57:40 CST 2018
     */
    private String guid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_MOBILE_LOGIN_LOG.MOBILE
     *
     * @mbggenerated Tue Jun 19 16:57:40 CST 2018
     */
    private String mobile;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_MOBILE_LOGIN_LOG.SECRET_KEY
     *
     * @mbggenerated Tue Jun 19 16:57:40 CST 2018
     */
    private String secretKey;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_MOBILE_LOGIN_LOG.SUCCESS
     *
     * @mbggenerated Tue Jun 19 16:57:40 CST 2018
     */
    private String success;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_MOBILE_LOGIN_LOG.CREATE_TIME
     *
     * @mbggenerated Tue Jun 19 16:57:40 CST 2018
     */
    private Date createTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_MOBILE_LOGIN_LOG.GUID
     *
     * @return the value of T_MOBILE_LOGIN_LOG.GUID
     *
     * @mbggenerated Tue Jun 19 16:57:40 CST 2018
     */
    public String getGuid() {
        return guid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_MOBILE_LOGIN_LOG.GUID
     *
     * @param guid the value for T_MOBILE_LOGIN_LOG.GUID
     *
     * @mbggenerated Tue Jun 19 16:57:40 CST 2018
     */
    public void setGuid(String guid) {
        this.guid = guid == null ? null : guid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_MOBILE_LOGIN_LOG.MOBILE
     *
     * @return the value of T_MOBILE_LOGIN_LOG.MOBILE
     *
     * @mbggenerated Tue Jun 19 16:57:40 CST 2018
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_MOBILE_LOGIN_LOG.MOBILE
     *
     * @param mobile the value for T_MOBILE_LOGIN_LOG.MOBILE
     *
     * @mbggenerated Tue Jun 19 16:57:40 CST 2018
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_MOBILE_LOGIN_LOG.SECRET_KEY
     *
     * @return the value of T_MOBILE_LOGIN_LOG.SECRET_KEY
     *
     * @mbggenerated Tue Jun 19 16:57:40 CST 2018
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_MOBILE_LOGIN_LOG.SECRET_KEY
     *
     * @param secretKey the value for T_MOBILE_LOGIN_LOG.SECRET_KEY
     *
     * @mbggenerated Tue Jun 19 16:57:40 CST 2018
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey == null ? null : secretKey.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_MOBILE_LOGIN_LOG.SUCCESS
     *
     * @return the value of T_MOBILE_LOGIN_LOG.SUCCESS
     *
     * @mbggenerated Tue Jun 19 16:57:40 CST 2018
     */
    public String getSuccess() {
        return success;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_MOBILE_LOGIN_LOG.SUCCESS
     *
     * @param success the value for T_MOBILE_LOGIN_LOG.SUCCESS
     *
     * @mbggenerated Tue Jun 19 16:57:40 CST 2018
     */
    public void setSuccess(String success) {
        this.success = success == null ? null : success.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_MOBILE_LOGIN_LOG.CREATE_TIME
     *
     * @return the value of T_MOBILE_LOGIN_LOG.CREATE_TIME
     *
     * @mbggenerated Tue Jun 19 16:57:40 CST 2018
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_MOBILE_LOGIN_LOG.CREATE_TIME
     *
     * @param createTime the value for T_MOBILE_LOGIN_LOG.CREATE_TIME
     *
     * @mbggenerated Tue Jun 19 16:57:40 CST 2018
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}