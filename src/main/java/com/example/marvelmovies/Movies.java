package com.example.marvelmovies;

public class Movies {
    String title;
    String year;
    String sales;

    public Movies (String title, String year, String sales) {
        this.title = title;
        this.year = year;
        this.sales = sales;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }
}
