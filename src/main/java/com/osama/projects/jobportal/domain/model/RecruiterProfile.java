package com.osama.projects.jobportal.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "recruiter_profile")
public class RecruiterProfile {

    @Id
    private int userAccountId;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    @MapsId
    private Users usersId;

    private String firstName;

    private String lastName;

    private String city;

    private String country;

    private String company;

    @Column(nullable = true, length = 512)
    private String profilePhoto;

    private String state;

    @Column(name = "linkedin_url", length = 512)
    private String linkedInUrl;

    @Column(nullable = true, length = 512)
    private String companyProofUrl;

    public RecruiterProfile(Users users) {
        this.usersId = users;
    }

    @Transient
    public String getPhotosImagePath() {
        if (profilePhoto == null) return null;
        return "/photos/recruiter/" + userAccountId + "/" + profilePhoto;
    }
}