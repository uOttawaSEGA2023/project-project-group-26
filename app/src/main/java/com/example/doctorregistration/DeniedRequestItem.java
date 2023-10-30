
package com.example.doctorregistration;

/**
 *
 * The DeniedRequestItem class defines the model for each denied request item,
 * including the user's status, and user ID.
 *
 */

public class DeniedRequestItem {
    private String status;
    private String userId;

    public DeniedRequestItem(String userId, String status) {
        this.userId = userId;
        this.status = status;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getUserId() {
        return userId;
    }
}
