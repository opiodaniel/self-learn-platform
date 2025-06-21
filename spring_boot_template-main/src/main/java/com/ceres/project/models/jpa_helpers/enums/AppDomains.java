package com.ceres.project.models.jpa_helpers.enums;

import com.ceres.project.permissions.UserPermissions;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * App domains are a way of categorizing your users.
 * This will become more handy while categorizing permissions
 *
 * Please feel free to change the default domains here to anything you want.
 * After Changing, remember to re-run the project for these changes to be reflected in your database
 */
//public enum AppDomains {
//    BACK_OFFICE, // Users in this domain are more of administrators
//    CLIENT_SIDE, // roles here will only affect the client side, should not even be visible on the BACK_OFFICE
//    ALL // roles that cut across all domains example is like "VIEW_USERS", everyone can view em but with extra perms
//}

@Getter
@AllArgsConstructor
public enum AppDomains {
    BACK_OFFICE("Back Office", List.of(
            UserPermissions.VIEW_USER,
            UserPermissions.VIEW_DASHBOARD,
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
            UserPermissions.CREATE_WALLET,
            UserPermissions.DELETE_WALLET,
            UserPermissions.EDIT_WALLET,
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
            UserPermissions.APPROVE_COMPANY,
            UserPermissions.VIEW_GRADING,
            UserPermissions.CREATE_INTERVIEW_GRADE,
            UserPermissions.VIEW_PROPOSAL,
            UserPermissions.VIEW_INTERVIEW_GRADE,
            UserPermissions.EDIT_INTERVIEW_GRADE,
            UserPermissions.UPDATE_INTERVIEW_STATUS,
            UserPermissions.VIEW_GRADING,
            UserPermissions.UPDATE_INTERVIEW_STATUS,
            UserPermissions.DELETE_INTERVIEW_GRADE,
            UserPermissions.CAN_VIEW_TRANSACTIONS,
            UserPermissions.CREATE_MODERATOR
    )), // Users in this domain are more of administrators
    CLIENT("Client Side", List.of(
            UserPermissions.VIEW_DASHBOARD,
            UserPermissions.VIEW_USER,
            UserPermissions.CREATE_PROPOSAL,
            UserPermissions.EDIT_PROPOSAL,
            UserPermissions.VIEW_PROPOSAL,
            UserPermissions.DELETE_PROPOSAL,
            UserPermissions.CREATE_MODERATOR
    )),
    FREELANCER("Freelancer", List.of(
            UserPermissions.VIEW_JOB,
            UserPermissions.VIEW_SKILL,
            UserPermissions.VIEW_OBJECTIVE_ASSESSMENT,
            UserPermissions.VIEW_STRUCTURED_ASSESSMENT,
            UserPermissions.VIEW_INTERVIEW_SCHEDULE,
            UserPermissions.VIEW_TAKE_HOME_ASSESSMENT,
            UserPermissions.VIEW_WALLET,
            UserPermissions.VIEW_NOTIFICATION,
            UserPermissions.VIEW_DELIVERABLE,
            UserPermissions.VIEW_STACK,
            UserPermissions.CREATE_BID,
            UserPermissions.VIEW_BID,
            UserPermissions.EDIT_BID,
            UserPermissions.DELETE_BID,
            UserPermissions.CREATE_SUBMISSION,
            UserPermissions.VIEW_SUBMISSION,
            UserPermissions.EDIT_SUBMISSION,
            UserPermissions.DELETE_SUBMISSION
    )),
    ALL("All", List.of(UserPermissions.values())); // roles that cut across all domains example is like "VIEW_USERS", everyone can view em but with extra perms

    private final String name;
    private final List<UserPermissions> permissions;
}
