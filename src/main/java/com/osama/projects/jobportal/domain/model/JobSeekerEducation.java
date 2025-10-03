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
@Table(name = "job_seeker_education")
public class JobSeekerEducation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String level;

    private String institutionName;

    @Column(nullable = true)
    private String degree;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private JobSeekerProfile profile;
}