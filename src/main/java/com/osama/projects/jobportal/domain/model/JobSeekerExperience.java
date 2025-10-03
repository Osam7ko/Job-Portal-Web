package com.osama.projects.jobportal.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.Transient;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "job_seeker_experience")
public class JobSeekerExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String company;       // e.g. "ACME"
    private String roleTitle;     // e.g. "Backend Developer"
    private Integer years;        // whole years

    @Column(length = 512)
    private String proofFile;     // optional uploaded filename

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private JobSeekerProfile profile;

    @Transient
    public String getProofPath() {
        if (proofFile == null || profile == null || profile.getUserAccountId() == null) return null;
        return "/docs/candidate/" + profile.getUserAccountId() + "/experience/" + proofFile;
    }
}