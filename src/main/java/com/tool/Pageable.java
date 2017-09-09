package com.tool;

import org.apache.commons.lang.StringUtils;

public class Pageable
{
  private int startPage = 1;
  private int pageSize = 10;

  private int startIndex = 1;

  private int endIndex = 5;
  private String startTime;
  private String endTime;
  private int pageRange = 0;
  private int rowCount = 0;
  private int pageCount = 0;
  private int pageOffset = 0;
  private int pageTail = 0;
  private String orderField;
  private String groupField;
  private boolean orderDirection;
  private int length = 6;
  private int[] indexs;

  public int getStartPage()
  {
    return this.startPage;
  }

  public void setStartPage(int startPage) {
    this.startPage = startPage;
  }

  public String getStartTime() {
    return this.startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return this.endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public int getPageRange() {
    return this.pageRange;
  }

  public void setPageRange(int pageRange) {
    this.pageRange = pageRange;
  }

  public int getLength()
  {
    return this.length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public int[] getIndexs() {
    int len = getEndIndex() - getStartIndex() + 1;
    this.indexs = new int[len];
    for (int i = 0; i < len; i++) {
      this.indexs[i] = (getStartIndex() + i);
    }
    return this.indexs;
  }

  public void setIndexs(int[] indexs) {
    this.indexs = indexs;
  }

  public int getStartIndex() {
    return this.pageSize * (this.startPage - 1);
  }

  public void setStartIndex(int startIndex) {
    this.startIndex = startIndex;
  }

  public int getEndIndex() {
    if (getStartIndex() < 1) {
      setStartIndex(1);
    }
    this.endIndex = (getStartIndex() + this.length <= getPageCount() ? getStartIndex() + this.length : 
      getPageCount());
    return this.endIndex;
  }

  public void setEndIndex(int endIndex) {
    this.endIndex = endIndex;
  }

  protected void doPage() {
    this.pageCount = (this.rowCount / this.pageSize + 1);

    if ((this.rowCount % this.pageSize == 0) && (this.pageCount > 1)) {
      this.pageCount -= 1;
    }

    if (this.startPage > this.pageCount) {
      this.startPage = this.pageCount;
    }

    this.pageOffset = ((this.startPage - 1) * this.pageSize);
    this.pageTail = (this.pageOffset + this.pageSize);
    if (this.pageOffset + this.pageSize > this.rowCount)
      this.pageTail = this.rowCount;
  }

  public String getOrderCondition() {
    String condition = "";
    String orderField = checkOrderFiled(this.orderField);
    if ((orderField != null) && (orderField.length() != 0)) {
      condition = " order by " + orderField + (
        this.orderDirection ? " " : " desc ");
    }
    return condition;
  }

  public String getMysqlQueryCondition() {
    this.pageOffset = ((this.startPage - 1) * this.pageSize);
    String condition = " limit " + this.pageOffset + "," + this.pageSize;
    return condition;
  }

  public void setOrderDirection(boolean orderDirection) {
    this.orderDirection = orderDirection;
  }

  public boolean isOrderDirection() {
    return this.orderDirection;
  }

  public void setOrderField(String orderField) {
    this.orderField = orderField;
  }

  public String getOrderField() {
    return this.orderField;
  }

  public void setPageCount(int pageCount) {
    this.pageCount = pageCount;
  }

  public int getPageCount() {
    return this.pageCount;
  }

  public void setPageOffset(int pageOffset) {
    this.pageOffset = pageOffset;
  }

  public int getPageOffset() {
    return this.pageOffset;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getPageSize() {
    return this.pageSize;
  }

  public void setPageTail(int pageTail) {
    this.pageTail = pageTail;
  }

  public int getPageTail() {
    return this.pageTail;
  }

  public void setRowCount(int rowCount) {
    this.rowCount = rowCount;
    doPage();
  }

  public int getRowCount() {
    return this.rowCount;
  }

  public String getGroupField() {
    String groupField = checkOrderFiled(this.groupField);
    return groupField;
  }

  public void setGroupField(String groupField) {
    this.groupField = groupField;
  }

  public String checkOrderFiled(String orderFiled)
  {
    if (StringUtils.isEmpty(orderFiled)) {
      return null;
    }

    String order = orderFiled.trim().toLowerCase();
    if ((order.contains("select")) || (order.contains("update")) || 
      (order.contains("delete")) || (order.contains("insert")) || 
      (order.contains("drop")) || (order.contains("and")) || 
      (order.contains("or"))) {
      return null;
    }
    return orderFiled;
  }
}