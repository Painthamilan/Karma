package com.doordelivery.karma.activities;

public class Courses {
    String CourseFee,CourseId,CourseName,Details,Duration,ProductCatagory,ProductId,ProductImage,ProductName,Requirement,CourseImage,Teacher;

    public Courses() {
    }

    public Courses(String courseFee, String courseId, String courseName, String details, String duration, String productCatagory, String productId, String productImage, String productName, String requirement, String courseImage, String teacher) {
        CourseFee = courseFee;
        CourseId = courseId;
        CourseName = courseName;
        Details = details;
        Duration = duration;
        ProductCatagory = productCatagory;
        ProductId = productId;
        ProductImage = productImage;
        ProductName = productName;
        Requirement = requirement;
        CourseImage = courseImage;
        Teacher = teacher;
    }


    public String getCourseFee() {
        return CourseFee;
    }

    public void setCourseFee(String courseFee) {
        CourseFee = courseFee;
    }

    public String getCourseId() {
        return CourseId;
    }

    public void setCourseId(String courseId) {
        CourseId = courseId;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getProductCatagory() {
        return ProductCatagory;
    }

    public void setProductCatagory(String productCatagory) {
        ProductCatagory = productCatagory;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getRequirement() {
        return Requirement;
    }

    public void setRequirement(String requirement) {
        Requirement = requirement;
    }

    public String getCourseImage() {
        return CourseImage;
    }

    public void setCourseImage(String courseImage) {
        CourseImage = courseImage;
    }

    public String getTeacher() {
        return Teacher;
    }

    public void setTeacher(String teacher) {
        Teacher = teacher;
    }
}
