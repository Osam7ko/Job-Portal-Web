package com.osama.projects.jobportal.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "job_seeker_profile")
public class JobSeekerProfile {

    @Id
    private Integer userAccountId;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    @MapsId
    private Users usersId;

    private String firstName;

    private String lastName;

    private String city;

    private String country;

    private String employmentType;

    private String resume;

    @Column(nullable = true, length = 512)
    private String profilePhoto;

    private String state;

    @Column(name = "linkedin_url", length = 512)
    private String linkedInUrl;

    @Column(nullable = true, length = 512)
    private String githubUrl;

    @Column(nullable = true, length = 512)
    private String portfolioUrl;

    @OneToMany(targetEntity = Skills.class, cascade = CascadeType.ALL, mappedBy = "profile")
    private List<Skills> skills;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobSeekerEducation> educations = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobSeekerExperience> experiences = new ArrayList<>();

    public JobSeekerProfile(Users users) {
        this.usersId = users;
    }

    @Transient
    public String getPhotosImagePath() {
        if (profilePhoto == null || userAccountId == null) return null;
        return "/photos/candidate/" + userAccountId + "/" + profilePhoto;
    }

    @Transient
    public String getResumePath() {
        if (resume == null || userAccountId == null) return null;
        // keep docs separate from photos
        return "/docs/candidate/" + userAccountId + "/" + resume;
    }
}