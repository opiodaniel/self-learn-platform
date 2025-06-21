package com.ceres.project.utils;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ceres.project.models.database.SystemUserModel;
import com.ceres.project.repositories.RoleRepository;
import com.ceres.project.repositories.SystemUserRepository;
import com.ceres.project.utils.mail.MailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class Helpers {

    private final SystemUserRepository systemUserRepository;
    private final RoleRepository roleRepository;

    private final MailService mailService;

    public static String generateServspaceId(String prefix) {
        return generatorRandom(prefix, 9);
    }

    public String generateTakeHomeAssignmentCode() {
        return generatorRandom("ASSN", 9);
    }

    public static String generatorRandom(String prefix, int length) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        var sample = timestamp.getTime();
        String sampleReduced = String.valueOf(sample);
        return prefix + "-" + sampleReduced.substring(sampleReduced.length() - length);
    }

    public static String generateTxRef() {
        return generatorRandom("SREF", 10);
    }

    /**
     * Attempts to define the category of the user by their servspace ID
     *
     * @param servspaceId Unique identifier on servspace
     * @return String
     */
    public static String detectUserTypeFromServSpaceId(String servspaceId) throws NotAcceptable406 {
        if (servspaceId == null) {
            throw new NotAcceptable406("Servspace ID should be provided");
        }
        if (servspaceId.toLowerCase().startsWith("co")) {
            return "company";
        } else if (servspaceId.toLowerCase().startsWith("sc")) {
            return "client";
        } else if (servspaceId.toLowerCase().startsWith("sd")) {
            return "developer";
        } else if (servspaceId.toLowerCase().startsWith("bo")) {
            return "back_office";
        }
        // return the ID to take care of the control account and servspace wallets, which are not actually users in the system.
        return servspaceId.toLowerCase();
    }



    public String generateMeetingID() {
        return generatorRandom("SI", 8);
    }

    public String extractFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] items = contentDisposition.split(";");
        for (String item : items) {
            if (item.trim().startsWith("filename")) {
                return item.substring(item.indexOf("=") + 2, item.length() - 1);
            }
        }
        return "";
    }

    public String generateResetToken() {
        return generatorRandom(null, 14);
    }

    public String generateIssueCode() {
        return generatorRandom("ISS", 11);
    }

    public String generateBudgetID() {
        return generatorRandom("BUD", 11);
    }

    public String generateNarration() {
        return generatorRandom("SEN", 11);
    }

    public String generateReference() {
        return generatorRandom("SREF", 10);
    }

    public String generateWalletSaltKey(String servspaceId) {
        return servspaceId.concat(invertString(servspaceId));
    }


    public String invertString(String input) {
        StringBuilder inverted = new StringBuilder();
        for (int i = input.length() - 1; i >= 0; i--) {
            inverted.append(input.charAt(i));
        }
        return inverted.toString();
    }

    public String formatMoney(double amount) {
        DecimalFormat formatter = new DecimalFormat("#,###.##");
        return formatter.format(amount);
    }

    public Boolean passwordRegex(String password) {
        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return Pattern.matches(regex, password);
    }

    public <T> List<T> paginate(List<T> items, int pageNumber, int pageSize) {
        // Calculate the start index of the page
        int fromIndex = (pageNumber - 1) * pageSize;
        // Calculate the end index of the page
        int toIndex = Math.min(fromIndex + pageSize, items.size());

        // Check if the fromIndex is within the list size
        if (fromIndex >= items.size() || fromIndex < 0) {
            return new ArrayList<>(); // Return an empty list if out of range
        }

        // Return the sublist representing the page
        return items.subList(fromIndex, toIndex);
    }

    public void sendBulkEmail(JSONObject request) {
        JSONObject data = request.getJSONObject("data");
        String message = data.getString("message");
        JSONArray servspaceIds = data.getJSONArray("servspaceIds");

        if (servspaceIds == null) {
            try {
                throw new NotAcceptable406("Servspace IDs can not be null");
            } catch (NotAcceptable406 e) {
                throw new RuntimeException(e);
            }
        }

        servspaceIds.forEach(scId -> {
            try {
                Optional<SystemUserModel> user = systemUserRepository.findByServpaceId(scId.toString());

                if (user.isPresent()) {
                    mailService.sendTemplateMail(user.get(), "Servspace Support", message, "email-template");
                }

            } catch (MessagingException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

