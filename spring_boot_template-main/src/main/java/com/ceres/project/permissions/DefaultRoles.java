package com.ceres.project.permissions;

import com.ceres.project.models.jpa_helpers.enums.AppDomains;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum DefaultRoles {
    ADMINISTRATOR("administrator", "ADMINISTRATOR", "The Administrator of the system", AppDomains.ALL, List.of(UserPermissions.values())),
    FREELANCER("freelancer", "FREELANCER", "A freelancer who enrols on the system for jobs", AppDomains.FREELANCER, AppDomains.FREELANCER.getPermissions()),
    PROGRAMMER("programmer", "PROGRAMMER", "A programmer who enrols on the system for study", AppDomains.PROGRAMMER, AppDomains.PROGRAMMER.getPermissions()),
    HUMAN_RESOURCE("human resource", "HUMAN_RESOURCE", "In charge of human resource management", AppDomains.BACK_OFFICE, List.of(
            UserPermissions.VIEW_GRADING,
            UserPermissions.VIEW_ALL_GRADINGS,
            UserPermissions.CREATE_INTERVIEW_GRADE,
            UserPermissions.VIEW_INTERVIEW_GRADE,
            UserPermissions.UPDATE_INTERVIEW_STATUS,
            UserPermissions.EDIT_INTERVIEW_GRADE,
            UserPermissions.SCHEDULE_INTERVIEW,
            UserPermissions.VIEW_INTERVIEW_SCHEDULE,
            UserPermissions.EDIT_INTERVIEW_SCHEDULE,
            UserPermissions.CONDUCT_INTERVIEW,
            UserPermissions.DELETE_INTERVIEW_GRADE
    )),
    FINANCE("finance", "FINANCE", "Manages money related issues for example wallets and billings",
            AppDomains.BACK_OFFICE,
            List.of(
                    UserPermissions.CREATE_WALLET,
                    UserPermissions.DELETE_WALLET,
                    UserPermissions.EDIT_WALLET,
                    UserPermissions.VIEW_WALLET,
                    UserPermissions.CREATE_NOTIFICATION,
                    UserPermissions.DELETE_NOTIFICATION,
                    UserPermissions.EDIT_NOTIFICATION,
                    UserPermissions.VIEW_NOTIFICATION,
                    UserPermissions.CAN_VIEW_TRANSACTIONS)),
    TECHNICAL("technical", "TECHNICAL", "Handles technical stuff such as setting assessments as well as interviewing freelancers",
            AppDomains.BACK_OFFICE,
            List.of(UserPermissions.VIEW_USER,
                    UserPermissions.CREATE_USER,
                    UserPermissions.DELETE_USER,
                    UserPermissions.EDIT_USER,
                    UserPermissions.VIEW_ROLE,
                    UserPermissions.EDIT_ROLE,
                    UserPermissions.CREATE_ROLE,
                    UserPermissions.DELETE_ROLE,
                    UserPermissions.VIEW_PERMISSION,
                    UserPermissions.EDIT_PERMISSION,
                    UserPermissions.CREATE_PERMISSION,
                    UserPermissions.DELETE_PERMISSION,
                    UserPermissions.VIEW_JOB,
                    UserPermissions.CREATE_JOB,
                    UserPermissions.DELETE_JOB,
                    UserPermissions.EDIT_JOB,
                    UserPermissions.CREATE_SKILL,
                    UserPermissions.DELETE_SKILL,
                    UserPermissions.EDIT_SKILL,
                    UserPermissions.VIEW_SKILL,
                    UserPermissions.CREATE_OBJECTIVE_ASSESSMENT,
                    UserPermissions.EDIT_OBJECTIVE_ASSESSMENT,
                    UserPermissions.VIEW_OBJECTIVE_ASSESSMENT,
                    UserPermissions.DELETE_OBJECTIVE_ASSESSMENT,
                    UserPermissions.CREATE_STRUCTURED_ASSESSMENT,
                    UserPermissions.EDIT_STRUCTURED_ASSESSMENT,
                    UserPermissions.VIEW_STRUCTURED_ASSESSMENT,
                    UserPermissions.DELETE_STRUCTURED_ASSESSMENT,
                    UserPermissions.SCHEDULE_INTERVIEW,
                    UserPermissions.CONDUCT_INTERVIEW,
                    UserPermissions.EDIT_INTERVIEW_SCHEDULE,
                    UserPermissions.VIEW_INTERVIEW_SCHEDULE,
                    UserPermissions.CREATE_TAKE_HOME_ASSESSMENT,
                    UserPermissions.EDIT_TAKE_HOME_ASSESSMENT,
                    UserPermissions.DELETE_TAKE_HOME_ASSESSMENT,
                    UserPermissions.VIEW_TAKE_HOME_ASSESSMENT,
                    UserPermissions.VIEW_WALLET,
                    UserPermissions.CREATE_NOTIFICATION,
                    UserPermissions.DELETE_NOTIFICATION,
                    UserPermissions.EDIT_NOTIFICATION,
                    UserPermissions.VIEW_NOTIFICATION,
                    UserPermissions.CREATE_DELIVERABLE,
                    UserPermissions.DELETE_DELIVERABLE,
                    UserPermissions.EDIT_DELIVERABLE,
                    UserPermissions.VIEW_DELIVERABLE,
                    UserPermissions.CREATE_STACK,
                    UserPermissions.DELETE_STACK,
                    UserPermissions.EDIT_STACK,
                    UserPermissions.VIEW_STACK,
                    UserPermissions.VIEW_ALL_USERS,
                    UserPermissions.VIEW_ALL_WALLETS,
                    UserPermissions.VIEW_ALL_DELIVERABLES,
                    UserPermissions.VIEW_ALL_BIDS,
                    UserPermissions.VIEW_ALL_ROLES,
                    UserPermissions.VIEW_ALL_PERMISSIONS,
                    UserPermissions.VIEW_ALL_ISSUES,
                    UserPermissions.VIEW_ALL_SUBMISSIONS,
                    UserPermissions.CREATE_GRADING,
                    UserPermissions.VIEW_ALL_GRADINGS,
                    UserPermissions.EDIT_GRADING,
                    UserPermissions.DELETE_GRADING,
                    UserPermissions.VIEW_GRADING,
                    UserPermissions.CREATE_INTERVIEW_GRADE,
                    UserPermissions.VIEW_INTERVIEW_GRADE,
                    UserPermissions.EDIT_INTERVIEW_GRADE,
                    UserPermissions.UPDATE_INTERVIEW_STATUS,
                    UserPermissions.DELETE_INTERVIEW_GRADE,
                    UserPermissions.VIEW_PROPOSAL,
                    UserPermissions.CAN_VIEW_TRANSACTIONS
            ));

    private final String roleName;
    private final String roleCode;
    private final String description;
    private final AppDomains domain;
    private final List<UserPermissions> permissions;
}

