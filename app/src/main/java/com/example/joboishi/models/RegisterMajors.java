package com.example.joboishi.models;

import java.io.Serializable;
import java.util.Objects;

public class RegisterMajors implements Serializable {
    private String name_job;
    private String name_cpn;
    private Boolean isChecked;

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getName_job() {
        return name_job;
    }

    public void setName_job(String name_job) {
        this.name_job = name_job;
    }

    public String getName_cpn() {
        return name_cpn;
    }

    public void setName_cpn(String name_cpn) {
        this.name_cpn = name_cpn;
    }

    public RegisterMajors(String name_job, String name_cpn, Boolean isChecked) {
        this.name_job = name_job;
        this.name_cpn = name_cpn;
        this.isChecked = isChecked;
    }

    public RegisterMajors(RegisterMajors majors) {
        this.name_job = majors.name_job;
        this.name_cpn = majors.getName_cpn();
        this.isChecked = majors.isChecked;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RegisterMajors other = (RegisterMajors) obj;
        return Objects.equals(this.name_job, other.name_job); // So sánh các thuộc tính cần thiết
    }

}
